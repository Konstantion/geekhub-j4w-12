package edu.geekhub.homework.core.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void process_shouldResponseOkShouldHaveResponseStatusOk(){
        Response<String> responseConstructor = new Response<>(ResponseStatus.SUCCESS);
        Response<String> responseMethod = Response.ok();

        assertThat(responseConstructor).isEqualTo(responseMethod);

        assertThat(responseConstructor)
                .extracting(Response::toString)
                .matches(s -> s.contains("no data"));

        assertThat(responseMethod)
                .extracting(Response::toString)
                .matches(s -> s.contains("no data"));
    }

    @Test
    void process_shouldResponseFailShouldHaveResponseStatusFail(){
        Response<String> responseConstructor = new Response<>(ResponseStatus.FAIL);
        Response<String> responseMethod = Response.fail();

        assertThat(responseConstructor).isEqualTo(responseMethod);

        assertThat(responseConstructor)
                .extracting(Response::toString)
                .matches(s -> s.contains("no data"));

        assertThat(responseMethod)
                .extracting(Response::toString)
                .matches(s -> s.contains("no data"));
    }

}