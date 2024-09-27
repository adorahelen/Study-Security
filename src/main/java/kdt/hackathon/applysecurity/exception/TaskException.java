package kdt.hackathon.applysecurity.exception;

public class TaskException extends RuntimeException {

    private String message;
    private int code;


    public TaskException(String message, int code) {
        this.message = message;
        this.code = code;

    }
}
