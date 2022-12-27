package edu.geekhub.homework.core.song.validation;

import edu.geekhub.homework.core.song.SongMP3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.validation.Validator;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
class SongValidatorTest {

    private SongValidator validator;

    @Mock
    private SongValidations validationsMock;

    @BeforeEach
    void setUp() {
        validator = new SongValidator(validationsMock);
    }

    @Test
    void process_shouldReturnEmptyOptional_whenSongIsValid() {
        SongMP3 song = new SongMP3(
                "Тестування",
                "Тестувальники",
                "Тестувальний",
                "Тест",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );

        Optional<String> result = validator.validate(song);

        assertThat(result).isNotPresent();
    }

    @Test
    void process_shouldReturnNameError_whenValidate_givenSongWithNotValidName() {

        when(validationsMock.isNameValid(anyString())).thenReturn(Optional.of("Song name isn't valid"));

        SongMP3 song = new SongMP3(
                "Тестування",
                "Тестувальники",
                "Тестувальний",
                "",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );


        Optional<String> result = validator.validate(song);

        assertThat(result)
                .isPresent()
                .asString()
                .contains("Song name isn't valid");
    }

    @Test
    void process_shouldReturnGroupError_whenValidate_givenSongWithNotValidGroup() {

        when(validationsMock.isGroupValid(anyString())).thenReturn(Optional.of("Song group isn't valid"));

        SongMP3 song = new SongMP3(
                "Тестування",
                "",
                "Тестувальний",
                "Тест",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );


        Optional<String> result = validator.validate(song);

        assertThat(result)
                .isPresent()
                .asString()
                .contains("Song group isn't valid");
    }

    @Test
    void process_shouldReturnMultipleErrors_whenValidate_givenSongWithNotValidValues() {

        when(validationsMock.isAlbumValid(anyString())).thenReturn(Optional.of("Song album isn't valid"));
        when(validationsMock.isGenreValid(anyString())).thenReturn(Optional.of("Song group isn't valid"));

        SongMP3 song = new SongMP3(
                "",
                "Тестувальники",
                "",
                "Тест",
                "https://mp3ukr.com/uploads/files/2022-03/1647583174_bmbkslzchrvnkln.mp3"
        );


        Optional<String> result = validator.validate(song);

        assertThat(result)
                .isPresent()
                .asString()
                .contains("Song group isn't valid")
                .contains("Song album isn't valid");
    }

    @Test
    void process_shouldCreateValidationInstance_whenCreateValidator() {
        SongValidator validator = new SongValidator();

        assertThat(validator.getValidations()).isNotNull();
    }

}