package com.amap.message;

/**
 * Created by tree on 17/2/18.
 */
public class Message {
    private String msg;
    private int code;
    private String error;

    public Message(String msg, int code, String error) {
        this.msg = msg;
        this.code = code;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", error='" + error + '\'' +
                '}';
    }
}
