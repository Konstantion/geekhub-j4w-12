package edu.geekhub.homework.core;

import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.request.Response;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.core.song.SongMP3;
import edu.geekhub.homework.core.song.validation.SongValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.geekhub.homework.core.request.ResponseStatus.FAIL;
import static edu.geekhub.homework.core.request.ResponseStatus.SUCCESS;

public class SongService {
    private final SongIOUtil IOUtil;
    private final SongClient client;
    private final SongValidator validator;
    private final Logger log;

    public SongService(SongIOUtil pathBuilder, SongClient client, Logger log) {
        this.log = log;
        this.IOUtil = pathBuilder;
        this.client = client;
        validator = new SongValidator();
    }

    public boolean downloadSongHTTP(Song song) {
        Optional<String> validationResult = validator.validate(song);
        if (validationResult.isPresent()) {
            log.error(validationResult.orElseThrow());
            return false;
        }

        String url = song.getUrl();

        Response<Object> response = client.downloadSongViaIO(url);

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

    public List<Song> readAllSongsFromPlayList() {
        List<String> songLines = IOUtil.readAllSongFromPlayList();
        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < songLines.size(); i++) {
            String[] args = songLines.get(i).split("\\|");
            if (args.length >= 5) {
                SongMP3 temp = new SongMP3(
                        args[0].trim(),
                        args[1].trim(),
                        args[2].trim(),
                        args[3].trim(),
                        args[4].trim()
                );
                songs.add(temp);
            } else {
                log.error(
                        String.format(
                                "PlayList line error, not enough args in line %s",
                                i
                        )
                );
            }
        }

        return songs;
    }
}
