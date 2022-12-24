package edu.geekhub.homework.web;

import edu.geekhub.homework.web.request.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SongClient {
    private static final double MEGA_BYTE_IN_BYTE = 1_048_576;
    private static final String CONTENT_LENGTH = "content-length";

    public Response<Object> downloadSongViaHttpRequest(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(1))
                .GET()
                .build();
        try {
            HttpResponse<byte[]> httpResponse = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            if (checkContentLength(httpResponse.headers())) {
                return Response.ok(httpResponse.body());
            } else {
                return Response.fail(
                        String.format(
                                "File isn't valid, [size should be less then 10 MB, current size is %.2f MB]",
                                getContentLengthInMB(httpResponse.headers()) / MEGA_BYTE_IN_BYTE
                        )
                );
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Response.fail(
                    String.format(
                            "Failed to send request, [%s]",
                            e.getMessage()
                    )
            );
        }
    }

    private boolean checkContentLength(HttpHeaders headers) {
        if (headers.firstValue(CONTENT_LENGTH).isEmpty()) {
            return false;
        }
        double dataLength = getContentLengthInMB(headers);

        return dataLength < 10 * MEGA_BYTE_IN_BYTE;
    }

    private double getContentLengthInMB(HttpHeaders headers) {
        return Double.parseDouble(headers.firstValue(CONTENT_LENGTH).orElse("0"));
    }
}
