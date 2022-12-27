package edu.geekhub.homework;

import edu.geekhub.homework.core.SongClient;
import edu.geekhub.homework.core.SongService;
import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.logger.LoggerIOUtil;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.web.SongController;



public class InstanceInitializer {
    private SongIOUtil songIOUtil;
    private SongClient songClient;
    private SongService songService;
    private SongController songController;
    private LoggerIOUtil loggerIOUtil;
    private Logger logger;

    public InstanceInitializer() {
        initialize();
    }

    private void initialize() {
        loggerIOUtil = new LoggerIOUtil();
        logger = new Logger(loggerIOUtil);
        songIOUtil = new SongIOUtil();
        songClient = new SongClient();
        songService = new SongService(songIOUtil, songClient, logger);
        songController = new SongController(songService);
    }

    public SongIOUtil getSongIOUtil() {
        return songIOUtil;
    }

    public SongClient getSongClient() {
        return songClient;
    }

    public SongService getSongService() {
        return songService;
    }

    public SongController getSongController() {
        return songController;
    }

    public Logger getLogger() {
        return logger;
    }
}
