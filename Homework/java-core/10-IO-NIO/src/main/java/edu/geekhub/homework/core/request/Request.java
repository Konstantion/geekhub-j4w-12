package edu.geekhub.homework.core.request;

import static java.util.Objects.isNull;

public class Request <T> {

    private T data;

    public Request(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        String requestData = isNull(data) ? "no data" : data.toString();
        return "Request{" +
                "data=" + requestData +
                '}';
    }
}
