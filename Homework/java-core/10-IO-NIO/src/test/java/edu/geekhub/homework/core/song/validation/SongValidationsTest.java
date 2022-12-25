package edu.geekhub.homework.core.song.validation;

import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.core.song.SongMP3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongValidationsTest {

    private SongValidations validations;

    @Mock
    private SongIOUtil IOUtilMock;

    @BeforeEach
    void setUp() {
        validations = new SongValidations(IOUtilMock);
    }

    @Test
    void process_returnEmptyOptional_whenIsUrlValid_givenValidUrl() {
        String url = "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3";
        Optional<String> result = validations.isUrlValid(url);
        assertThat(result).isNotPresent();
    }

    @Test
    void process_returnProtocolError_whenIsUrlValid_givenNotValidUrl() {
        String url = "error://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3";

        Optional<String> result = validations.isUrlValid(url);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("URL isn't valid, [unknown protocol: error]");
    }

    @Test
    void process_returnNullError_whenIsUrlValid_givenNull() {
        String url = null;

        Optional<String> result = validations.isUrlValid(url);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song url isn't valid, [shouldn't be null]");
    }

    @Test
    void process_returnSizeError_whenIsUrlValid_givenUrlReferToLargeFile() {
        String url = "https://ia903003.us.archive.org/34/items/metallicatheunforgiven/Metallica%20-%20The%20Unforgiven.mp3";

        Optional<String> result = validations.isUrlValid(url);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song isn't valid, [shouldn't be more then 10MB, current 15.00MB]");
    }

    @Test
    void process_returnShouldNotBeEmptyError_whenIsUrlValid_givenEmptyString() {
        String url = "";

        Optional<String> result = validations.isUrlValid(url);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song url isn't valid, [shouldn't be empty]");
    }

    @Test
    void process_returnShouldBeMP3Error_whenIsUrlValid_givenURLIsNotReferToMP3() {
        String url = "https://www.google.com/";

        Optional<String> result = validations.isUrlValid(url);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("URL isn't refer to mp3, [should refer to mp3 file]");
    }

    @Test
    void process_returnEmptyOptional_whenIsNameValid_givenValidName() {
        String name = "Song name";

        Optional<String> result = validations.isNameValid(name);

        assertThat(result).isNotPresent();
    }

    @Test
    void process_returnNullError_whenIsNameValid_givenNullAsName() {
        String name = null;

        Optional<String> result = validations.isNameValid(name);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song name isn't valid, [shouldn't be null]");
    }

    @Test
    void process_returnEmptyError_whenIsNameValid_givenEmptyName() {
        String name = "";

        Optional<String> result = validations.isNameValid(name);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song name isn't valid, [shouldn't be empty]");
    }

    @Test
    void process_returnSizeError_whenIsNameValid_givenToSortName() {
        String name = ":";

        Optional<String> result = validations.isNameValid(name);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song name isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnSizeError_whenIsNameValid_givenToLongName() {
        String name = "This is long name".repeat(50);

        Optional<String> result = validations.isNameValid(name);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song name isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnEmptyOptional_whenIsGroupValid_givenValidGroup() {
        String group = "Song group";

        Optional<String> result = validations.isGroupValid(group);

        assertThat(result).isNotPresent();
    }

    @Test
    void process_returnNullError_whenIsGroupValid_givenNullAsGroup() {
        String group = null;

        Optional<String> result = validations.isGroupValid(group);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song group isn't valid, [shouldn't be null]");
    }

    @Test
    void process_returnEmptyError_whenIsGroupValid_givenEmptyGroup() {
        String group = "";

        Optional<String> result = validations.isGroupValid(group);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song group isn't valid, [shouldn't be empty]");
    }

    @Test
    void process_returnSizeError_whenIsGroupValid_givenToSortGroup() {
        String group = "1";

        Optional<String> result = validations.isGroupValid(group);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song group isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnSizeError_whenIsGroupValid_givenToLongGroup() {
        String group = "AC\\DC".repeat(50);

        Optional<String> result = validations.isGroupValid(group);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song group isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnEmptyOptional_whenIsGenreValid_givenValidGenre() {
        String genre = "Song genre";

        Optional<String> result = validations.isGenreValid(genre);

        assertThat(result).isNotPresent();
    }

    @Test
    void process_returnNullError_whenIsGenreValid_givenNullAsGenre() {
        String genre = null;

        Optional<String> result = validations.isGenreValid(genre);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song genre isn't valid, [shouldn't be null]");
    }

    @Test
    void process_returnEmptyError_whenIsGenreValid_givenEmptyGenre() {
        String genre = "";

        Optional<String> result = validations.isGenreValid(genre);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song genre isn't valid, [shouldn't be empty]");
    }

    @Test
    void process_returnSizeError_whenIsGenreValid_givenToSortGenre() {
        String genre = "1";

        Optional<String> result = validations.isGenreValid(genre);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song genre isn't valid, [size should be between 2 and 40 characters]");
    }

    @Test
    void process_returnSizeError_whenIsGenreValid_givenToLongGenre() {
        String genre = "Rock".repeat(50);

        Optional<String> result = validations.isGenreValid(genre);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song genre isn't valid, [size should be between 2 and 40 characters]");
    }

    @Test
    void process_returnEmptyOptional_whenIsAlbumValid_givenValidAlbum() {
        String album = "Song album";

        Optional<String> result = validations.isAlbumValid(album);

        assertThat(result).isNotPresent();
    }

    @Test
    void process_returnNullError_whenIsAlbumValid_givenNullAsAlbum() {
        String album = null;

        Optional<String> result = validations.isAlbumValid(album);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song album isn't valid, [shouldn't be null]");
    }

    @Test
    void process_returnEmptyError_whenIsAlbumValid_givenEmptyAlbum() {
        String album = "";

        Optional<String> result = validations.isAlbumValid(album);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song album isn't valid, [shouldn't be empty]");
    }

    @Test
    void process_returnSizeError_whenIsAlbumValid_givenToSortAlbum() {
        String album = "1";

        Optional<String> result = validations.isAlbumValid(album);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song album isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnSizeError_whenIsAlbumValid_givenToLongAlbum() {
        String album = "Human".repeat(50);

        Optional<String> result = validations.isAlbumValid(album);

        assertThat(result)
                .isPresent()
                .get()
                .asString()
                .contains("Song album isn't valid, [size should be between 2 and 30 characters]");
    }

    @Test
    void process_returnSongExistError_whenIsSongPresent_givenSongIsPresent() {
        Song song = new SongMP3("a","b","c","d","e");

        when(IOUtilMock.buildFullFilePath(any(Song.class))).thenReturn(System.getProperty("user.home"));

        Optional<String> result = validations.isSongPresent(song);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo("Song is already downloaded, [No need to download again]");
    }

    @Test
    void process_returnEmptyOptional_whenIsSongPresent_givenSongIsNotPresent() {
        Song song = new SongMP3("a","b","c","d","e");

        when(IOUtilMock.buildFullFilePath(any(Song.class))).thenReturn("path");

        Optional<String> result = validations.isSongPresent(song);

        assertThat(result)
                .isNotPresent();
    }

}