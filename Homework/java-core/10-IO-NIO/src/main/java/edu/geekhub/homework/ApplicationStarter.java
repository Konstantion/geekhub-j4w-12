package edu.geekhub.homework;

import edu.geekhub.homework.core.UI.UserInterface;
import edu.geekhub.homework.core.request.Request;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.web.SongController;

import java.util.List;

public class ApplicationStarter {
    public static void main(String[] args) {
        InstanceInitializer instanceInitializer = new InstanceInitializer();
        SongController controller = instanceInitializer.getSongController();
        List<Song> songs = controller.getListOfSongs().getData();

        songs.stream()
                .map(Request::new)
                .forEach(controller::downloadSongByUrl);

        UserInterface userInterface = new UserInterface(
                instanceInitializer.getLogger(),
                instanceInitializer.getScanner()
        );
        userInterface.drawMainMenu();
    }
}
