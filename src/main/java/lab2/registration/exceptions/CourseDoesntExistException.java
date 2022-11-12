package lab2.registration.exceptions;

public class CourseDoesntExistException extends Exception{
    public CourseDoesntExistException(String message) {
        super(message);
    }
}