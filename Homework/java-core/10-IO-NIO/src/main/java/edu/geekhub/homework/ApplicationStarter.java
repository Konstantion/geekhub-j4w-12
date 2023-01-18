package edu.geekhub.homework;

import edu.geekhub.homework.UI.UserInterface;
import edu.geekhub.homework.core.request.Request;
import edu.geekhub.homework.core.song.Song;
import edu.geekhub.homework.web.SongController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationStarter {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        InstanceInitializer instanceInitializer = new InstanceInitializer();
        SongController controller = instanceInitializer.getSongController();
        List<Song> songs = controller.getListOfSongs().getData();

        songs.stream()
                .map(Request::new)
                //parallelization
                .forEach(request -> executor.submit(() -> {
                    controller.downloadSongByUrl(request);
                }));
                //default
                //.forEach(controller::downloadSongByUrl);
        UserInterface userInterface = new UserInterface(
                instanceInitializer.getLogger(),
                instanceInitializer.getScanner()
        );
        userInterface.drawMainMenu();
    }
}
