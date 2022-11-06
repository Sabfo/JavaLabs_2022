package lab2.registration.model;

/**
 * Класс для информации о преподавателе
 */
public class Instructor extends Person {

    /**
     * Идентификаторы курсов, которые может вести преподаватель
     */
    int[] canTeach;

    public int[] getCanTeach() {
        return canTeach;
    }

    public void setCanTeach(int[] canTeach) {
        this.canTeach = canTeach;
    }
}
