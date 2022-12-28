package edu.geekhub.homework.core.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void process_shouldReturnData_whenCreateRequestWithData() {
        String data = "DATA";
        Request<String> request = new Request<>(data);

        assertThat(request)
                .extracting(Request::getData)
                .isNotNull()
                .isEqualTo(data);
        assertThat(request)
                .extracting(Request::toString)
                .matches(s -> s.contains("Request"));
    }

    @Test
    void process_shouldReturnNoData_whenCreateRequestWithNull() {
        String data = null;
        Request<String> request = new Request<>(data);

        assertThat(request)
                .extracting(Request::getData)
                .isNull();
        assertThat(request)
                .extracting(Request::toString)
                .matches(s -> s.contains("no data"));
    }
}