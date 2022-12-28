package edu.geekhub.homework.web;

import edu.geekhub.homework.core.SongService;
import edu.geekhub.homework.core.request.Request;
import edu.geekhub.homework.core.request.Response;
import edu.geekhub.homework.core.request.ResponseStatus;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongMP3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SongControllerTest {
    private SongService service;
    private SongController controller;

    @BeforeEach
    void setUp() {
        service = mock(SongService.class);
        controller = new SongController(service);
    }

    @Test
    void process_returnResponseOk_whenDownloadSongByUrl() {
        when(service.downloadSongHTTP(any(Song.class))).thenReturn(true);
        Request<Song> request = new Request<>(new SongMP3());

        Response<Boolean> actualResponse = controller.downloadSongByUrl(request);

        assertThat(actualResponse)
                .matches(r -> r.getData().equals(true))
                .extracting(Response::getStatus).isEqualTo(ResponseStatus.SUCCESS);
        verify(service, times(1)).downloadSongHTTP(any(Song.class));
    }

    @Test
    void process_returnResponseFail_whenDownloadSongByUrl() {
        when(service.downloadSongHTTP(any(Song.class))).thenReturn(false);
        Request<Song> request = new Request<>(new SongMP3());

        Response<Boolean> actualResponse = controller.downloadSongByUrl(request);

        assertThat(actualResponse)
                .matches(r -> r.getData().equals(false))
                .extracting(Response::getStatus).isEqualTo(ResponseStatus.FAIL);
        verify(service, times(1)).downloadSongHTTP(any(Song.class));
    }

    @Test
    void process_returnResponseOk_whenGetListOfSongs() {
        when(service.readAllSongsFromPlayList()).thenReturn(Collections.emptyList());

        Response<List<Song>> actualResponse = controller.getListOfSongs();

        assertThat(actualResponse)
                .matches(r -> r.getData().equals(Collections.emptyList()))
                .extracting(Response::getStatus).isEqualTo(ResponseStatus.SUCCESS);
        verify(service, times(1)).readAllSongsFromPlayList();
    }
}