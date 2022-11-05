package edu.geekhub.homework;

import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.messages.UserInteractionMessages;

public class ApplicationStarter {
    public static void main(String[] args) {
        Figure figure1;
        Figure figure2;
        for (int i = 1; i <= 2; i++) {
            System.out.println(UserInteractionMessages.selectShapeMessage(i));
            //todo: finish user interface
        }
    }
}
