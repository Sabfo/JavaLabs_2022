package lab2.registration.implement;

import lab2.registration.exceptions.*;
import lab2.registration.model.*;
import lab2.registration.reader.CourseDataReader;
import lab2.registration.reader.InstructorDataReader;
import lab2.registration.reader.StudentDataReader;
import lab2.registration.service.CourseInstructorService;
import lab2.registration.service.StudentService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ServiceImplement implements StudentService, CourseInstructorService {
    private Instructor[] instructors;
    private Student[] bachelorStudents, masterStudents;
    private CourseInfo[] courseInfos;
    private CourseInstance[] courseInstances;
    private HashMap<Long, List<Long>> registration; // Ключ - id студента, значение - список курсов, куда зарегался

    public ServiceImplement() {
        try {
            StudentDataReader studentDataReader = new StudentDataReader();
            bachelorStudents = studentDataReader.readBachelorStudentData();
            masterStudents = studentDataReader.readMasterStudentData();

            CourseDataReader courseDataReader = new CourseDataReader();
            courseInfos = courseDataReader.readCourseInfoData();
            courseInstances = courseDataReader.readCourseInstanceData();

            InstructorDataReader instructorDataReader = new InstructorDataReader();
            instructors = instructorDataReader.readInstructorData();
        } catch (IOException e) {
            System.out.println("[ERROR] Проблема при чтении файлов");
        }

        registration = new HashMap<Long, List<Long>>();
    }
    /**
     * @param courseInstanceId идентификатор курса
     * @return список студентов, зарегистрированных на данный курс
     */
    @Override
    public Student[] findStudentsByCourseId(long courseInstanceId) {
        return registration.entrySet().stream().filter(x -> x.getValue().contains(courseInstanceId))
                .filter(x -> Arrays.stream(courseInstances)
                        .filter(c -> x.getValue().contains(c.getId())).findFirst().orElse(null) != null)
                .map(Map.Entry::getKey).map(this::findStudentByStudentId)
                .toArray(Student[]::new);
    }

    private Student findStudentByStudentId(long studentId) {
        Student bach = Arrays.stream(bachelorStudents).filter(s -> s.getId() == studentId).findFirst().orElse(null);
        Student master = Arrays.stream(masterStudents).filter(s -> s.getId() == studentId).findFirst().orElse(null);
        if (bach != null)
            return bach;
        if (master != null)
            return master;
        return null;
    }

    /**
     * @param instructorId идентификатор преподавателя
     * @return список студентов, посещающих один из курсов данного преподавателя
     */
    @Override
    public Student[] findStudentsByInstructorId(long instructorId) {
        Instructor instructor = Arrays.stream(instructors)
                .filter(i -> i.getId() == instructorId)
                .findFirst()
                .orElse(null);
        if (instructor == null)
            return new Student[0];
        if (instructor.getCanTeach().length == 0)
            return new Student[0];

        return Arrays.stream(courseInstances).filter(i -> i.getInstructorId() == instructorId)
                .map(c -> findStudentsByCourseId(c.getId()))
                .flatMap(x -> Arrays.stream(x).distinct())
                .toArray(Student[]::new);
    }

    /**
     * @param instructorId идентификатор преподавателя
     * @param courseInfoId идентификатор курса
     * @return список преподавателей, которые могут прочитать данный курс вместо данного преподавателя
     */
    @Override
    public Instructor[] findReplacement(long instructorId, long courseInfoId) {
        return Arrays.stream(instructors)
                .filter(i -> Arrays.stream(i.getCanTeach()).anyMatch(c -> c == courseInfoId) && i.getId() != instructorId)
                .toArray(Instructor[]::new);
    }

    /**
     * Регистрация студента на курс. Регистрация возможна при следующих условиях:
     * - курс еще не начался; READY
     * - курс предназначен для категории данного студента (магистра/бакалавра); READY
     * - студент прошел все обязательные курсы, необходимые для посещения данного курса; READY
     * - в курсе есть свободные места. READY
     *
     * @param studentId        идентификатор студента
     * @param courseInstanceId идентификатор курса, соответствующий CourseInstance.id
     */
    @Override
    public void subscribe(long studentId, long courseInstanceId) throws CourseDoesntExistException,
            CourseHasAlreadyStartedException, CourseHasNoPlaceException, SubjectDoesNotExistException,
            StudentHasAlreadySubscribedException, StudentDoesNotHavePrerequisitesException {

        long amountTakenPlaces = registration.values().stream().flatMap(Collection::stream)
                .filter(i -> i == courseInstanceId).count();
        CourseInstance ourCourseInstance = Arrays.stream(courseInstances)
                .filter(i -> i.getCourseId() == courseInstanceId)
                .findFirst().orElse(null);
        if (ourCourseInstance == null)
            throw new CourseDoesntExistException("The course doesn't exist");
        if (ourCourseInstance.getStartDate().isAfter(LocalDate.now()))
            throw new CourseHasAlreadyStartedException("The course has already started so we couldn't subscribe it");
        if (ourCourseInstance.getCapacity() <= amountTakenPlaces)
            throw new CourseHasNoPlaceException("There are no places in the course");


        CourseInfo courseInfo = Arrays.stream(courseInfos).filter(i -> i.getId() == ourCourseInstance.getCourseId())
                .findFirst().orElse(null);
        if (courseInfo == null) // Нет такой дисциплины
            throw new SubjectDoesNotExistException("There is no subject");

        List<Long> list = registration.computeIfAbsent(studentId, k -> new ArrayList<>());

        Student student = Arrays.stream(courseInfo.getStudentCategories())
                .flatMap(x -> {
                    switch (x) {
                        case BACHELOR:
                            return Arrays.stream(bachelorStudents);
                        case MASTER:
                            return Arrays.stream(masterStudents);
                    }
                    return Stream.empty();
                })
                .filter(i -> i.getId() == studentId)
                .filter(i -> registration.get(i.getId()).stream().noneMatch(x -> x == ourCourseInstance.getId()))
                .findFirst().orElse(null);
        if (student == null)
            throw new StudentHasAlreadySubscribedException("The student has already subscribed to this course");

        long[] prerequisites = courseInfo.getPrerequisites();
        if (prerequisites != null) {
            if(!Arrays.stream(prerequisites).allMatch(i ->
                    Arrays.stream(student.getCompletedCourses()).anyMatch(c -> c == i)))
                throw new StudentDoesNotHavePrerequisitesException("The student has not completed all the required courses");
        }

        list.add(ourCourseInstance.getId());
    }

    /**
     * Отмена регистрации студента на курс, которая возможна только в том случае, когда
     * курс еще не начался.
     *
     * @param studentId        идентификатор студента
     * @param courseInstanceId идентификатор курса, соответствующий CourseInstance.id
     */
    @Override
    public void unsubscribe(long studentId, long courseInstanceId)
            throws CourseDoesntExistException, CourseHasAlreadyStartedException, StudentHasNotSubscribedException {

        CourseInstance courseInstance = Arrays.stream(courseInstances)
                .filter(c -> c.getId() == courseInstanceId).findFirst().orElse(null);
        if (courseInstance == null)
            throw new CourseDoesntExistException("The course doesn't exist");
        if (courseInstance.getStartDate().isAfter(LocalDate.now()))
            throw new CourseHasAlreadyStartedException("The course has already started so we couldn't unsubscribe it");
        if (registration.get(studentId).remove(courseInstanceId))
            throw new StudentHasNotSubscribedException("The student did not register for the course");
    }

    /**
     * @param studentId идентификатор студента
     * @return список всех курсов, на которые записан студент
     */
    @Override
    public CourseInstance[] findAllSubscriptionsByStudentId(long studentId) {
        return Arrays.stream(courseInstances)
                .filter(course -> registration.get(studentId).stream().anyMatch(s -> s == course.getId()))
                .toArray(CourseInstance[]::new);
    }
}
