package edu.geekhub.homework.core.song;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SongTest {

    @Test
    void simpleSongTest() {
        String name = "name";
        String genre = "genre";
        String group = "group";
        String album = "album";
        String url = "url";

        Song songGetSet = new SongMP3();
        songGetSet.setName(name);
        songGetSet.setGenre(genre);
        songGetSet.setGroup(group);
        songGetSet.setAlbum(album);
        songGetSet.setUrl(url);

        Song songConstructor = new SongMP3(
                genre,
                group,
                album,
                name,
                url
        );

        assertThat(songGetSet).isEqualTo(songConstructor);

        assertThat(songGetSet.getAlbum()).isNotBlank();
        assertThat(songGetSet.getGenre()).isNotBlank();
        assertThat(songGetSet.getFormat()).isNotBlank();
        assertThat(songGetSet.getName()).isNotBlank();
        assertThat(songGetSet.getUrl()).isNotBlank();
        assertThat(songGetSet.getGroup()).isNotBlank();
        assertThat(songGetSet.hashCode())
                .isEqualTo(songConstructor.hashCode())
                .isNotEqualTo(0);
    }
}