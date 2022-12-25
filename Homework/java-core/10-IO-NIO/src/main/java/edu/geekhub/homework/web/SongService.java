package edu.geekhub.homework.web;

import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.core.song.validation.SongValidator;
import edu.geekhub.homework.web.request.Response;

import java.io.IOException;
import java.util.Optional;

import static edu.geekhub.homework.web.request.ResponseStatus.FAIL;
import static edu.geekhub.homework.web.request.ResponseStatus.SUCCESS;

public class SongService {
    private final SongIOUtil IOUtil;
    private final SongClient client;
    private final SongValidator validator;
    private final Logger log;

    public SongService(SongIOUtil pathBuilder, SongClient client) {
        this.IOUtil = pathBuilder;
        this.client = client;
        validator = new SongValidator();
        log = Logger.getInstance();
    }

    public boolean downloadSongHTTP(Song song) {
        Optional<String> validationResult = validator.validate(song);
        if (validationResult.isPresent()) {
            log.error(validationResult.orElseThrow());
            return false;
        }

        String url = song.getUrl();

        Response<Object> response = client.downloadSongViaNIO(url);

        if (response.getStatus().equals(SUCCESS)) {
            try {
                IOUtil.writeSong(song, (byte[]) response.getData());
                log.info(String.format(
                        "Song successfully saved, [%s].",
                        song
                ));
                return true;
            } catch (IOException e) {
                log.error(
                        String.format(
                                "Failed to save %s, [%s].",
                                song,
                                e.getMessage()
                        )
                );
            }
        }

        if (response.getStatus().equals(FAIL)) {
            log.error(response.getData().toString());
        }

        return false;
    }
}
