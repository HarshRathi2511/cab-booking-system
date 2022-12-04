package Exceptions;

public class TripRequestNotPresentException extends Exception {

    public TripRequestNotPresentException(String errorMessage) {
        super(errorMessage);
    }
}
