package edu.geekhub.homework.utilities.checkers;

public class ChooseShapeChecker {
    private static String[] possibleChoices = {"1", "2", "3", "4"};

    public static boolean isPossibleChoice(String choice) {
        for (String possibleChoice : possibleChoices) {
            if (possibleChoice.equals(choice)) {
                return true;
            }
        }
        return false;
    }

    private ChooseShapeChecker() {

    }
}
