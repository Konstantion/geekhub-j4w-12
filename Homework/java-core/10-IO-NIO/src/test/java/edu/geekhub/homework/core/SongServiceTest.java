package edu.geekhub.homework.core;

import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.logger.LoggerIOUtil;
import edu.geekhub.homework.core.request.Response;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.core.song.SongMP3;
import edu.geekhub.homework.core.song.validation.SongValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {
    @Mock
    private SongIOUtil songIOUtil;
    @Mock
    private LoggerIOUtil loggerIOUtil;
    @Mock
    private SongClient client;
    @Mock
    private SongValidator validator;
    @Mock
    private Logger log;

    private SongService service;

    @BeforeEach
    void setUp() {
        service = new SongService(songIOUtil, client, log, validator);
    }

    @Test
    void process_shouldReturnTrue_whenDownloadSongHTTP_withValidaValues() throws IOException {
        when(validator.validate(any(Song.class))).thenReturn(Optional.empty());
        when(client.downloadSongViaIO(anyString())).thenReturn(Response.ok(new byte[]{}));
        doNothing().when(songIOUtil).writeSong(any(Song.class), any(byte[].class));
        doNothing().when(log).info(anyString());

        SongMP3 song = new SongMP3(
                "Testing",
                "Testers",
                "Tester",
                "Test",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );
        boolean actualResult = service.downloadSongHTTP(song);

        assertThat(actualResult).isTrue();

        verify(validator, times(1)).validate(any(Song.class));
        verify(client, times(1)).downloadSongViaIO(anyString());
        verify(songIOUtil, times(1)).writeSong(any(Song.class), any(byte[].class));
        verify(log, times(1)).info(anyString());
    }

    @Test
    void process_shouldReturnFalse_whenDownloadSongHTTP_withNotValidaSong() throws IOException {
        when(validator.validate(any(Song.class))).thenReturn(Optional.of("Song is not Valid"));
        doNothing().when(log).error(anyString());

        SongMP3 song = new SongMP3(
                "Testing",
                "Testers",
                "Tester",
                "Test",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );
        boolean actualResult = service.downloadSongHTTP(song);

        assertThat(actualResult).isFalse();

        verify(validator, times(1)).validate(any(Song.class));
        verify(log, times(1)).error(anyString());
    }

    @Test
    void process_shouldReturnFalse_whenDownloadSongHTTP_withErrorInClient() throws IOException {
        when(validator.validate(any(Song.class))).thenReturn(Optional.empty());
        when(client.downloadSongViaIO(anyString())).thenReturn(Response.fail("Error"));
        doNothing().when(log).error(anyString());

        SongMP3 song = new SongMP3(
                "Testing",
                "Testers",
                "Tester",
                "Test",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );
        boolean actualResult = service.downloadSongHTTP(song);

        assertThat(actualResult).isFalse();

        verify(validator, times(1)).validate(any(Song.class));
        verify(client, times(1)).downloadSongViaIO(anyString());
        verify(log, times(1)).error(anyString());
    }

    @Test
    void process_shouldReturnFalse_whenDownloadSongHTTP_withErrorWhileWriteSong() throws IOException {
        when(validator.validate(any(Song.class))).thenReturn(Optional.empty());
        when(client.downloadSongViaIO(anyString())).thenReturn(Response.ok(new byte[]{}));
        doThrow(IOException.class)
                .when(songIOUtil).writeSong(any(Song.class), any(byte[].class));
        doNothing().when(log).error(anyString());

        SongMP3 song = new SongMP3(
                "Testing",
                "Testers",
                "Tester",
                "Test",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );
        boolean actualResult = service.downloadSongHTTP(song);

        assertThat(actualResult).isFalse();

        verify(validator, times(1)).validate(any(Song.class));
        verify(client, times(1)).downloadSongViaIO(anyString());
        verify(songIOUtil, times(1)).writeSong(any(Song.class), any(byte[].class));
        verify(log, times(1)).error(anyString());
    }

    @Test
    void process_shouldReturnListOfSongs_whenReadAllSongsFromPlayList() {
        when(songIOUtil.readAllSongFromPlayList()).thenReturn(
                List.of(
                        "Українська музика | Бумбокс | Патріотичні пісні | Ой у лузі червона калина | https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3",
                        "Українська музика | Шабля | Гімн Оборони України | Браття українці | https://mp3ukr.com/uploads/files/2022-03/1646805644_2xrshblbrttkrncgmnbrnkrnqf2rlsb8128kbs.mp3"
                )
        );

        List<Song> actualSongs = service.readAllSongsFromPlayList();

        assertThat(actualSongs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    void process_shouldReturnEmptyListAndLogErrors_whenReadAllSongsFromPlayList_withIncorrectInput() {
        when(songIOUtil.readAllSongFromPlayList()).thenReturn(
                List.of(
                        "Бумбокс | Патріотичні пісні | Ой у лузі червона калина | https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3",
                        "Шабля | Гімн Оборони України | Браття українці | https://mp3ukr.com/uploads/files/2022-03/1646805644_2xrshblbrttkrncgmnbrnkrnqf2rlsb8128kbs.mp3"
                )
        );
        doNothing().when(log).error(anyString());

        List<Song> actualSongs = service.readAllSongsFromPlayList();

        assertThat(actualSongs)
                .isNotNull()
                .isEmpty();
        verify(log, times(2)).error(anyString());
    }
}