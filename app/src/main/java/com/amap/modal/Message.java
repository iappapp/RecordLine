package com.amap.modal;

/**
 * Created by tree on 17/2/18.
 */
public class Message {
    private String messageg;
    private int code;
    private String error;

    public Message() {
    }

    public Message(String messageg, int code, String error) {
        this.messageg = messageg;
        this.code = code;
        this.error = error;
    }

    public String getMessageg() {
        return messageg;
    }

    public void setMessageg(String messageg) {
        this.messageg = messageg;
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
                "messageg='" + messageg + '\'' +
                ", code=" + code +
                ", error='" + error + '\'' +
                '}';
    }
}
