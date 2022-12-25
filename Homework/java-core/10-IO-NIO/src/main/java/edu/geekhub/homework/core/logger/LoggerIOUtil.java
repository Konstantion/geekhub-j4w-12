package edu.geekhub.homework.core.logger;

import java.io.*;
import java.util.List;

public class LoggerIOUtil {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String LOG_FILE = "log.txt";
    private boolean isFirstRecord = false;

    public String buildResourcesPath() {
        return String.join(SEPARATOR,
                PROJECT_DIRECTORY,
                "src",
                "main",
                "resources"
        );
    }

    public String buildFullLogPath() {
        return String.join(
                SEPARATOR,
                buildResourcesPath(),
                LOG_FILE
        );
    }

    public boolean createIfNotExist(File logFile) {
        try {
            return logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void fillMessagesListFromFile(List<Message> messages) {
        File logFile = new File(buildFullLogPath());
        if (!createIfNotExist(logFile)) {
            try (FileInputStream fis = new FileInputStream(buildFullLogPath())) {
                if (fis.available() != 0) {
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    while (fis.available() > 1) {
                        messages.add((Message) ois.readObject());
                    }
                    ois.close();
                }
            } catch (IOException | ClassCastException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (logFile.length() == 0) {
            isFirstRecord = true;
        }
    }

    public void appendMessageToFile(Message message) {
        if (isFirstRecord) {
            try (FileOutputStream fos = new FileOutputStream(buildFullLogPath());
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                isFirstRecord = false;
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (FileOutputStream fos = new FileOutputStream(buildFullLogPath(), true);
                 ObjectOutputStream oos = new ObjectOutputStream(fos) {
                     @Override
                     protected void writeStreamHeader() {
                     }
                 }
            ) {
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveMessages(List<Message> messages) {
        try (FileOutputStream fos = new FileOutputStream(buildFullLogPath());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessages(List<Message> messages) {
        try (FileInputStream fis = new FileInputStream(buildFullLogPath())) {
            if (fis.available() != 0) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                messages.addAll((List<Message>) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


