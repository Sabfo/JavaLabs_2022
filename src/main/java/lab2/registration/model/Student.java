package lab2.registration.model;

/**
 * Класс для информации о студенте
 */
public class Student extends Person {

    /**
     * список идентификаторов курсов (CourseInstance.id), пройденных студентом
     */
    private long[] completedCourses;

    public long[] getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(long[] completedCourses) {
        this.completedCourses = completedCourses;
    }
}
