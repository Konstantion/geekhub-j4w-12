package edu.geekhub.homework;

import edu.geekhub.homework.core.UI.UserInterface;

public class ApplicationStarter {
    public static void main(String[] args) {
        InstanceInitializer instanceInitializer = new InstanceInitializer();
        UserInterface userInterface = new UserInterface(instanceInitializer.getLogger());
        userInterface.drawMainMenu();
    }
}
