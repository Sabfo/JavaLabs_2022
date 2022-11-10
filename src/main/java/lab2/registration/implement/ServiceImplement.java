package lab2.registration.implement;

import lab2.registration.model.*;
import lab2.registration.reader.CourseDataReader;
import lab2.registration.reader.InstructorDataReader;
import lab2.registration.reader.StudentDataReader;
import lab2.registration.service.CourseInstructorService;
import lab2.registration.service.StudentService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
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
     * @return результат выполнения регистрации
     */
    @Override
    public ActionStatus subscribe(long studentId, long courseInstanceId) {
        long amountTakenPlaces = registration.values().stream().flatMap(Collection::stream)
                .filter(i -> i == courseInstanceId).count();
        CourseInstance ourCourseInstance = Arrays.stream(courseInstances)
                .filter(i -> i.getCourseId() == courseInstanceId)   // Проверяем, что такой курс есть
                .filter(i -> i.getStartDate().isAfter(LocalDate.now())) // он не начался
                .filter(i -> i.getCapacity() >= amountTakenPlaces) // И есть места
                .findFirst().orElse(null);
        if (ourCourseInstance == null)
            return ActionStatus.NOK;

        CourseInfo courseInfo = Arrays.stream(courseInfos).filter(i -> i.getId() == ourCourseInstance.getCourseId())
                .findFirst().orElse(null);
        if (courseInfo == null) // Нет такой дисциплины
            return ActionStatus.NOK;

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
                .findFirst().orElse(null); // Нашли студента и видим, что он ещё не записан на этот курс
        if (student == null)
            return ActionStatus.NOK;

        long[] prerequisites = courseInfo.getPrerequisites();
        if (prerequisites != null) {
            if(!Arrays.stream(prerequisites).allMatch(i ->
                    Arrays.stream(student.getCompletedCourses()).anyMatch(c -> c == i)))
                return ActionStatus.NOK;
        }

        list.add(ourCourseInstance.getId());
        return ActionStatus.OK;
    }

    /**
     * Отмена регистрации студента на курс, которая возможна только в том случае, когда
     * курс еще не начался.
     *
     * @param studentId        идентификатор студента
     * @param courseInstanceId идентификатор курса, соответствующий CourseInstance.id
     * @return результат выполнения отмены регистрации
     */
    @Override
    public ActionStatus unsubscribe(long studentId, long courseInstanceId) {
        CourseInstance courseInstance = Arrays.stream(courseInstances)
                .filter(c -> c.getId() == courseInstanceId).findFirst().orElse(null);
        if (courseInstance == null)
            return ActionStatus.NOK;
        if (courseInstance.getStartDate().isAfter(LocalDate.now()))
            return ActionStatus.NOK;
        if (registration.get(studentId).remove(courseInstanceId))
            return ActionStatus.NOK;
        return ActionStatus.OK;
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
