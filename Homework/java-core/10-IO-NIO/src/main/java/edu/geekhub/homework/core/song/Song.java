package edu.geekhub.homework.core.song;

import edu.geekhub.homework.core.util.UTFString;

import java.util.Objects;

public abstract class Song {
    private String genre;
    private String group;
    private String album;
    private String name;
    private String url;

    protected Song() {
    }

    protected Song(String genre,
                   String group,
                   String album,
                   String name,
                   String url) {
        this.genre = UTFString.of(genre);
        this.group = UTFString.of(group);
        this.album = UTFString.of(album);
        this.name = UTFString.of(name);
        this.url = UTFString.of(url);
    }

    public abstract String getFormat();

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song song)) return false;
        return Objects.equals(genre, song.genre) && Objects.equals(group, song.group) && Objects.equals(album, song.album) && Objects.equals(name, song.name) && Objects.equals(url, song.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre, group, album, name, url);
    }

    @Override
    public String toString() {
        return "Song{" +
               "genre='" + genre + '\'' +
               ", group='" + group + '\'' +
               ", album='" + album + '\'' +
               ", name='" + name + '\'' +
               ", url='" + url + '\'' +
               '}';
    }
}
