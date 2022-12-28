package edu.geekhub.homework.core.song;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class SongIOUtilTest {
    private static final String ROOT = System.getProperty("user.home");
    private static final String DIR = System.getProperty("user.dir");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String MUSIC_DIRECTORY = "Music Library";

    SongIOUtil IOUtil;
    Song song;
    String name;
    String genre;
    String group;
    String album;
    String url;

    String correctPath;

    @BeforeEach
    void setUp() {
        IOUtil = new SongIOUtil();
        name = "name";
        genre = "genre";
        group = "group";
        album = "album";
        url = "url";

        song = new SongMP3(
                genre,
                group,
                album,
                name,
                url
        );

        String returnedPath = String.join(SEPARATOR,
                DIR,
                "Homework",
                "java-core",
                "10-IO-NIO",
                "src",
                "main",
                "resources",
                "playlist.txt");
        String pathDiff = String.join(SEPARATOR.repeat(2),
                "",
                "Homework",
                "java-core",
                "10-IO-NIO");
        correctPath = returnedPath.replaceFirst(pathDiff, "");
    }

    @Test
    void process_shouldReturnDirectoriesPath_whenBuildDirectoriesPath() {
        String actualPath = IOUtil.buildDirectoriesPath(song);

        String expectedPath = String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY,
                genre,
                group,
                album
        );

        assertThat(actualPath)
                .isEqualTo(expectedPath);
    }

    @Test
    void process_shouldReturnFileNameWithExtension_whenBuildFileName() {
        String actualPath = IOUtil.buildFileName(song);

        String expectedPath = String.join(
                ".",
                name,
                "mp3"
        );

        assertThat(actualPath)
                .isEqualTo(expectedPath)
                .endsWith("mp3");
    }

    @Test
    void process_shouldReturnFullSongPath_whenBuildFullFilePath() {
        String actualPath = IOUtil.buildFullFilePath(song);

        String expectedPath = String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY,
                genre,
                group,
                album + SEPARATOR
        ).concat(
                String.join(
                        ".",
                        name,
                        "mp3"
                )
        );

        assertThat(actualPath).isEqualTo(expectedPath);
    }

    @Test
    void process_shouldReturnPlayListPath_whenBuildPlayListPath() {
        SongIOUtil IOUtil = spy(SongIOUtil.class);
        when(IOUtil.buildPlayListPath())
                .thenReturn(correctPath);
        String actualPath = IOUtil.buildPlayListPath();

        String expectedPath = String.join(
                SEPARATOR,
                System.getProperty("user.dir"),
                "src",
                "main",
                "resources",
                "playlist.txt");

        assertThat(actualPath).isEqualTo(expectedPath);
    }

    @Test
    void process_shouldReturnListOfLines_whenRead() {
        SongIOUtil IOUtil = spy(SongIOUtil.class);
        when(IOUtil.buildPlayListPath())
                .thenReturn(correctPath);
        List<String> actualLines = IOUtil.readAllSongFromPlayList();
        File file = new File(IOUtil.buildPlayListPath());

        assertThat(file)
                .exists()
                .canRead()
                .canWrite();

        assertThat(actualLines).isNotNull();
    }

    @Test
    void process_shouldCallMethodToBuildPath_whenWriteSongInFile() throws IOException {
        Song testSong = new SongMP3(
                "Testing song",
                group,
                album,
                name,
                url
        );
        IOUtil.writeSong(testSong, new byte[]{1, 2, 3});

        File actualFile = new File(IOUtil.buildFullFilePath(testSong));

        assertThat(actualFile)
                .exists()
                .hasBinaryContent(new byte[]{1, 2, 3});
        try (Stream<Path> walk = Files.walk(Path.of(String.join(
                SEPARATOR,
                ROOT,
                MUSIC_DIRECTORY,
                testSong.getGenre())))) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        }
    }
}