package edu.geekhub.homework.web;

import edu.geekhub.homework.web.request.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SongClient {

    public Response<Object> downloadSongViaNIO(String url) {
        try (InputStream in = new URL(url).openStream()) {
            byte[] songBytes = in.readAllBytes();
            return Response.ok(songBytes);
        } catch (IOException e) {
            return Response.fail(String.format(
                            "Failed read %s, [%s]",
                            url,
                            e.getMessage()
                    )
            );
        }
    }
}
