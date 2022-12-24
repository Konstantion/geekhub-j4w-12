package edu.geekhub.homework.core.song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SongIOPathBuilder {
    private static final String ROOT = System.getProperty("user.home");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String MUSIC_DIRECTORY = "Music Library";

    public String buildDirectoriesPath(Song song) {
        String genre = song.getGenre();
        String group = song.getGroup();
        String album = song.getAlbum();
        String name = song.getName();

        return String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY,
                genre,
                group,
                album,
                name);
    }

    public String buildFileName(Song song) {
        return String.join(
                ".",
                song.getName(),
                song.getFormat()
        );
    }

    public String buildFullFilePath(Song song) {
        return String.join(
                SEPARATOR,
                buildDirectoriesPath(song),
                buildFileName(song)
        );
    }

    public void createDirectoriesForSong(Song song) {
        Path path = Path.of(buildDirectoriesPath(song));
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSong(Song song, byte[] songBytes) throws IOException {
        createDirectoriesForSong(song);
        String songFullPath = buildFullFilePath(song);
        Files.write(Path.of(songFullPath), songBytes);
    }
}
