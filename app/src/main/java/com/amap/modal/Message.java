package com.amap.modal;

/**
 * Created by tree on 17/2/18.
 */
public class Message {
    private String message;
    private int code;
    private String error;

    public Message() {
    }

    public Message(String message, int code, String error) {
        this.message = message;
        this.code = code;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", error='" + error + '\'' +
                '}';
    }
}
