package edu.geekhub.homework.web;

import edu.geekhub.homework.core.SongService;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.request.Request;
import edu.geekhub.homework.core.request.Response;

public class SongController {
    private final SongService service;

    public SongController(SongService service) {
        this.service = service;
    }

    public Response<Object> downloadSongByUrl(Request<Song> request) {
        boolean result = service.downloadSongHTTP(request.getData());

        return result ? Response.ok() : Response.fail();
    }
}
