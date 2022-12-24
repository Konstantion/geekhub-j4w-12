package edu.geekhub.homework.core.song.validation;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class SongValidations {
    private static final long MEGA_BYTE_IN_BYTE = 1_048_576;

    public Optional<String> isUrlValid(String url) {
        List<String> errorList = new ArrayList<>();

        if (isNull(url)) {
            errorList.add(
                    "Song url isn't valid, [shouldn't be null]"
            );

            return listToOptionalString(errorList);
        }

        if (url.isBlank()) {
            errorList.add(
                    "Song url isn't valid, [shouldn't be empty]"
            );
        }

        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            errorList.add(
                    String.format(
                            "URL isn't valid, [%s]",
                            e.getMessage()
                    )
            );
        }

        if (!isUrlMP3File(url)) {
            errorList.add(
                    "URL isn't refer to mp3, [should refer to mp3 file]"
            );
        }

        isContentLengthLessThen(url, 10 * MEGA_BYTE_IN_BYTE).ifPresent(len -> errorList.add(
                String.format(
                        "Song isn't valid, [shouldn't be more then 10MB, current %.2fMB]",
                        ((double) len) / (MEGA_BYTE_IN_BYTE * 1.0)
                )
        ));

        return listToOptionalString(errorList);
    }

    public Optional<String> isNameValid(String name) {
        List<String> errorList = new ArrayList<>();

        if (isNull(name)) {
            errorList.add(
                    "Song name isn't valid, [shouldn't be null]"
            );

            return listToOptionalString(errorList);
        }

        if (name.isBlank()) {
            errorList.add(
                    "Song name isn't valid, [shouldn't be empty]"
            );
        }

        if (!checkLength(name, 2, 30)) {
            errorList.add(
                    "Song name isn't valid, [size should be between 2 and 30 characters]"
            );
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isAlbumValid(String album) {
        List<String> errorList = new ArrayList<>();

        if (isNull(album)) {
            errorList.add(
                    "Song album isn't valid, [shouldn't be null]"
            );

            return listToOptionalString(errorList);
        }

        if (album.isBlank()) {
            errorList.add(
                    "Song album isn't valid, [shouldn't be empty]"
            );
        }

        if (!checkLength(album, 2, 30)) {
            errorList.add(
                    "Song album isn't valid, [size should be between 2 and 30 characters]"
            );
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isGenreValid(String genre) {
        List<String> errorList = new ArrayList<>();
        if (isNull(genre)) {
            errorList.add(
                    "Song genre isn't valid, [shouldn't be null]"
            );

            return listToOptionalString(errorList);
        }

        if (genre.isBlank()) {
            errorList.add(
                    "Song genre isn't valid, [shouldn't be empty]"
            );
        }

        if (!checkLength(genre, 2, 40)) {
            errorList.add(
                    "Song genre isn't valid, [size should be between 2 and 40 characters]"
            );
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isGroupValid(String group) {
        List<String> errorList = new ArrayList<>();
        if (isNull(group)) {
            errorList.add(
                    "Song group isn't valid, [shouldn't be null]"
            );

            return listToOptionalString(errorList);
        }

        if (group.isBlank()) {
            errorList.add(
                    "Song group isn't valid, [shouldn't be empty]"
            );
        }

        if (!checkLength(group, 2, 30)) {
            errorList.add(
                    "Song group isn't valid, [size should be between 2 and 30 characters]"
            );
        }

        return listToOptionalString(errorList);
    }

    private boolean checkLength(String string, int lb, int ub) {
        return (string.length() >= lb) && (string.length() <= ub);
    }

    public Optional<String> listToOptionalString(List<String> errorList) {
        String data = String.join(
                System.lineSeparator(),
                errorList
        );

        if (data.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }

    public boolean isUrlMP3File(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getFile().contains(".mp3");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Optional<Long> isContentLengthLessThen(String urlString, long maxLengthBytes) {
        try {
            long contentLength = new URL(urlString)
                    .openConnection()
                    .getContentLengthLong();
            if (contentLength > maxLengthBytes) {
                return Optional.of(contentLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
