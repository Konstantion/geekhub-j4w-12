package edu.geekhub.homework.web.request;

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
        return "Response {" +
               "data=" + data +
               ", status=" + status +
               '}';
    }
}
