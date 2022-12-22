package edu.geekhub.homework.core.song;

import java.nio.file.Path;

public class SongPathBuilder {
    private static final String ROOT = System.getProperty("user.home");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String MUSIC_DIRECTORY = "Music Library";

    private SongPathBuilder() {
    }

    public static Path buildDirectoriesPath(Song song) {
        String genre = song.getGenre();
        String group = song.getGroup();
        String album = song.getAlbum();
        String name = song.getName();

        return Path.of(
                String.join(
                        SEPARATOR,
                        ROOT,
                        MUSIC_DIRECTORY,
                        genre,
                        group,
                        album,
                        name
                )
        );
    }

    public static String buildFileName(Song song) {
        return String.join(
                ".",
                song.getName(),
                song.getFormat()
        );
    }
}
