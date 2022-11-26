package edu.geekhub.orcostat.model.request;

public class Request {
    Object data;

    public Request(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
