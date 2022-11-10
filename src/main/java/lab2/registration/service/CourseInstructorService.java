package lab2.registration.service;

import lab2.registration.model.Instructor;
import lab2.registration.model.ActionStatus;
import lab2.registration.model.Student;

/**
 * Интерфейс сервиса для преподавателя
 */
public interface CourseInstructorService {
    
    /**
     * @param courseInstanceId идентификатор курса
     * @return список студентов, зарегистрированных на данный курс
     */
    Student[] findStudentsByCourseId(long courseInstanceId);

    /**
     * @param instructorId идентификатор преподавателя
     * @return список студентов, посещающих один из курсов данного преподавателя
     */
    Student[] findStudentsByInstructorId(long instructorId);

    /**
     * @param instructorId идентификатор преподавателя
     * @param courseInfoId идентификатор курса
     * @return список преподавателей, которые могут прочитать данный курс вместо данного преподавателя
     */
    Instructor[] findReplacement(long instructorId, long courseInfoId);

}
