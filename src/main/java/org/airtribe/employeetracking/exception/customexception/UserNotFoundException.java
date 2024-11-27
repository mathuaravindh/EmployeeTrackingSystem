package org.airtribe.employeetracking.exception.customexception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("Employee not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
