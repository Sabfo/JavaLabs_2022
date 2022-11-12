package lab2.registration.exceptions;

public class CourseHasAlreadyStartedException extends Exception{
    public CourseHasAlreadyStartedException(String message) {
        super(message);
    }
}