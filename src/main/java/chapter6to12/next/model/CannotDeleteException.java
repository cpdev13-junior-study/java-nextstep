package chapter6to12.next.model;

public class CannotDeleteException extends RuntimeException {

    public CannotDeleteException(String message) {
        super(message);
    }
}
