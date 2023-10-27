package dev.maxuz.vas3ksanta.exception;

public class UserException extends RuntimeException {
    private final String userMessage;

    public UserException(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
