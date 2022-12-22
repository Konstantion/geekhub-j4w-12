package edu.geekhub.homework.core.song;

public class SongMP3 extends Song{

    private static final String FORMAT = "mp3";

    public SongMP3() {
    }

    public SongMP3(String genre, String group, String album, String name, String url) {
        super(genre, group, album, name, url);
    }

    @Override
    public String getFormat() {
        return FORMAT;
    }


}
