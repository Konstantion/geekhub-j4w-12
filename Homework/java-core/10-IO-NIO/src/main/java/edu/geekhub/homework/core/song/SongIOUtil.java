package edu.geekhub.homework.core.song;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

public class SongIOUtil {
    private static final String ROOT = System.getProperty("user.home");
    private static final String DIR = System.getProperty("user.dir");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String MUSIC_DIRECTORY = "Music Library";

    public String buildDirectoriesPath(Song song) {
        String genre = song.getGenre();
        String group = song.getGroup();
        String album = song.getAlbum();

        return String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY,
                genre,
                group,
                album
        );
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
        Files.write(Path.of(songFullPath), songBytes, StandardOpenOption.CREATE);
    }

    public String buildPlayListPath() {
        return String.join(
                SEPARATOR,
                DIR,
                "Homework",
                "java-core",
                "10-IO-NIO",
                "src",
                "main",
                "resources",
                "playlist.txt");
    }

    public List<String> readAllSongFromPlayList() {
        try {
            String path = buildPlayListPath();
            return Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void clearMusicLibrary() {
        String pathString = String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY);
        clearMusicLibrary(pathString);
    }

    public void clearMusicLibrary(String pathString) {
        File mainFolder = new File(pathString);
        File[] innerFolders = mainFolder.listFiles();
        if (innerFolders != null) {
            for (File folder : innerFolders) {
                deleteDirectory(folder);
            }
        }
    }

    public void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
