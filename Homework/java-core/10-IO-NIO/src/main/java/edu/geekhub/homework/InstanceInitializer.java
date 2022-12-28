package edu.geekhub.homework;

import edu.geekhub.homework.core.SongClient;
import edu.geekhub.homework.core.SongService;
import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.logger.LoggerIOUtil;
import edu.geekhub.homework.core.song.SongIOUtil;
import edu.geekhub.homework.core.song.validation.SongValidator;
import edu.geekhub.homework.web.SongController;

import java.util.Scanner;


public class InstanceInitializer {
    private SongIOUtil songIOUtil;
    private SongClient songClient;
    private SongService songService;
    private SongController songController;
    private LoggerIOUtil loggerIOUtil;
    private Logger logger;
    private SongValidator validator;
    private Scanner scanner;

    public InstanceInitializer() {
        initialize();
    }

    private void initialize() {
        validator = new SongValidator();
        loggerIOUtil = new LoggerIOUtil();
        logger = new Logger(loggerIOUtil);
        songIOUtil = new SongIOUtil();
        songClient = new SongClient();
        songService = new SongService(songIOUtil, songClient, logger, validator);
        songController = new SongController(songService);
        scanner = new Scanner(System.in);
    }

    public Scanner getScanner() {
        return scanner;
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
