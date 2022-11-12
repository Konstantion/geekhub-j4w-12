package edu.geekhub.utils.generator;

public class UserNotesContainer {
    private static String[] notes = {
            "Ukraine’s glory has not yet died, nor her freedom,",
            "Upon us, my young brothers, fate shall yet smile.",
            "Our enemies will perish, like dew in the morning sun,",
            "And we too shall rule, brothers, in our own land."
    };

    public static String getNote(int index) {
        index = Math.abs(index) % notes.length;
        return notes[index];
    }

    private UserNotesContainer() {
    }
}
