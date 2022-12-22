package edu.geekhub.homework.core.song.validation;

import edu.geekhub.homework.core.song.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongValidator {
    private final SongValidations validations;

    public SongValidator() {
        validations = new SongValidations();
    }

    public Optional<List<String>> validate(Song song) {
        List<String> validationErrors = new ArrayList<>();

        validations.isUrlValid(song.getUrl()).ifPresent(validationErrors::add);

        validations.isNameValid(song.getName()).ifPresent(validationErrors::add);

        validations.isGenreValid(song.getGenre()).ifPresent(validationErrors::add);

        validations.isGroupValid(song.getGroup()).ifPresent(validationErrors::add);

        validations.isAlbumValid(song.getAlbum()).ifPresent(validationErrors::add);

        if(validationErrors.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(validationErrors);
    }


}
