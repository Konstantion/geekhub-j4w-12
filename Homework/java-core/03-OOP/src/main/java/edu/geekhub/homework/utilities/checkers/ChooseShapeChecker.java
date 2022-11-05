package edu.geekhub.homework.utilities.checkers;

public class ChooseShapeChecker {
    private static Integer[] possibleChoices = {1, 2, 3, 4};

    public static boolean isPossibleChoice(Integer choice) {
        for (Integer possibleChoice : possibleChoices) {
            if (possibleChoice.equals(choice)) {
                return true;
            }
        }
        return false;
    }

    private ChooseShapeChecker() {

    }
}
