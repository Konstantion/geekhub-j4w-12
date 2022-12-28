package edu.geekhub.homework.core.request;

import java.util.Objects;

import static java.util.Objects.isNull;

public class Response<T> {

    private T data;
    private final ResponseStatus status;

    public Response(ResponseStatus status) {
        this.status = status;
    }

    private Response(T data, ResponseStatus status) {
        this.data = data;
        this.status = status;
    }

    public static <T> Response<T> ok() {
        return new Response<>(ResponseStatus.SUCCESS);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data, ResponseStatus.SUCCESS);
    }

    public static <T> Response<T> fail() {
        return new Response<>(ResponseStatus.FAIL);
    }

    public static <T> Response<T> fail(T data) {
        return new Response<>(data, ResponseStatus.FAIL);
    }

    public T getData() {
        return data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String responseData = isNull(data) ? "no data" : data.toString();
        return "Response {" +
               "data=" + responseData +
               ", status=" + status +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response<?> response = (Response<?>) o;
        return Objects.equals(data, response.data) && status == response.status;
    }
}
