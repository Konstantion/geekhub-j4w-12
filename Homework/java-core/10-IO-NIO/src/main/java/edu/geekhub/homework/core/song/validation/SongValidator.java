package edu.geekhub.homework.core.song.validation;

import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.core.song.SongIOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongValidator {
    private final SongValidations validations;

    public SongValidator(SongValidations validations) {
        this.validations = validations;
    }

    public SongValidator() {
        this.validations = new SongValidations(new SongIOUtil());
    }

    public Optional<String> validate(Song song) {
        List<String> validationErrors = new ArrayList<>();

        validations.isUrlValid(song.getUrl()).ifPresent(validationErrors::add);

        validations.isNameValid(song.getName()).ifPresent(validationErrors::add);

        validations.isGenreValid(song.getGenre()).ifPresent(validationErrors::add);

        validations.isGroupValid(song.getGroup()).ifPresent(validationErrors::add);

        validations.isAlbumValid(song.getAlbum()).ifPresent(validationErrors::add);

        validations.isSongPresent(song).ifPresent(validationErrors::add);

        if (validationErrors.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                String.format(
                        "Failed to validate %s:%n%s.",
                        song,
                        String.join(
                                System.lineSeparator(),
                                validationErrors)
                )
        );
    }

    public SongValidations getValidations() {
        return validations;
    }
}
