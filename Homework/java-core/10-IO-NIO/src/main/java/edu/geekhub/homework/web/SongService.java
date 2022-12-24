package edu.geekhub.homework.web;

import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOPathBuilder;
import edu.geekhub.homework.core.song.validation.SongValidator;
import edu.geekhub.homework.web.request.Response;

import java.io.IOException;
import java.util.Optional;

import static edu.geekhub.homework.web.request.ResponseStatus.FAIL;
import static edu.geekhub.homework.web.request.ResponseStatus.SUCCESS;

public class SongService {
    private final SongIOPathBuilder pathIOBuilder;
    private final SongClient client;
    private final SongValidator validator;

    public SongService(SongIOPathBuilder pathBuilder, SongClient client) {
        this.pathIOBuilder = pathBuilder;
        this.client = client;
        validator = new SongValidator();
    }

    public boolean downloadSongHTTP(Song song) {
        Optional<String> validationResult = validator.validate(song);
        if (validationResult.isPresent()) {
            //log.error(validationResult.orElseThrow());
            return false;
        }

        String url = song.getUrl();

        Response<Object> response = client.downloadSongViaHttpRequest(url);

        if (response.getStatus().equals(SUCCESS)) {
            try {
                pathIOBuilder.writeSong(song, (byte[]) response.getData());
                //log.info(Song successfully saved);
                return true;
            } catch (IOException e) {
                //log.error(
                // String.format(
                // "Failed to save file, [%s]",
                // e.getMessage()
                // )
                // );
            }
        }

        if (response.getStatus().equals(FAIL)) {
            //log.error(response.getData());
        }

        return false;
    }
}
