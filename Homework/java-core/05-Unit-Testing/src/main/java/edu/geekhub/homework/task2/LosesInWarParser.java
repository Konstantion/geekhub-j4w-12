package edu.geekhub.homework.task2;


import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class LosesInWarParser {
    private static final String[] parameters = new String[]{
            "Танки", "ББМ", "Гармати", "РСЗВ", "Засоби ППО",
            "Літаки", "Гелікоптери", "БПЛА", "Крилаті ракети",
            "Кораблі (катери)", "Автомобілі та автоцистерни", "Спеціальна техніка", "Особовий склад"
    };
    private static final String SEPARATORS = ";—&";

    LosesStatistic parseLosesStatistic(String input) {
        validateInputPresent(input);

        input = removeHtmlTags(input);

        if (!containsStatistics(input)) {
            return LosesStatistic.empty();
        }

        return createStatistics(input);
    }

    private LosesStatistic createStatistics(String input) {
        String[] inputLines = input.split("\n");
        int[] parametersValues = new int[parameters.length];
        for (int lineIndex = 0; lineIndex < inputLines.length; lineIndex++) {

            String bareStatisticsLine = removeSeparatorsFromLine(inputLines[lineIndex]);

            if (bareStatisticsLine.isBlank()) continue;

            boolean parameterWasTakenFromTheCurrentLine = false;

            for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {

                if (parameterWasTakenFromTheCurrentLine) break;

                if (bareStatisticsLine.contains(parameters[parameterIndex])) {

                    Integer parameterValue = tryToGetIntegerValueInString(bareStatisticsLine);

                    while (isNull(parameterValue)) {

                        int nextLineIndex = lineIndex + 1;

                        if (nextLineIndex < inputLines.length) {

                            String nextBareStatisticLine = removeSeparatorsFromLine(inputLines[nextLineIndex]);

                            if (lineContainsParameter(nextBareStatisticLine)) {
                                parameterValue = 0;
                                break;
                            }

                            parameterValue = tryToGetIntegerValueInString(nextBareStatisticLine);

                        } else {
                            parameterValue = 0;
                            break;
                        }
                    }

                    parametersValues[parameterIndex] = parameterValue;
                    parameterWasTakenFromTheCurrentLine = true;
                }
            }
        }
        return losesStatisticFromParametersValues(parametersValues);
    }

    private String removeSeparatorsFromLine(String line) {
        return line.replace(SEPARATORS, " ");
    }

    private boolean containsStatistics(String input) {
        for (String parameter : parameters) {
            if (!input.contains(parameter)) {
                return false;
            }
        }
        return true;
    }

    private Integer tryToGetIntegerValueInString(String bareStatisticsLine) {
        String[] bareStatisticWords = bareStatisticsLine.split(" ");
        for (String word : bareStatisticWords) {
            if (isInteger(word)) {
                return Integer.valueOf(word);
            }
        }
        return null;
    }

    private void validateInputPresent(String input) {
        requireNonNull(input);

        if (input.isBlank()) {
            throw new IllegalArgumentException(
                    "Cant process empty input state"
            );
        }
    }

    private boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '0') {
                if (s.length() != 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    private String removeHtmlTags(String input) {
        return removeHtmlTags(input, "");
    }

    private String removeHtmlTags(String input, String replacement) {
        StringBuffer inputBuffer = new StringBuffer(input);
        while (inputBuffer.indexOf("<") != -1) {
            int openIndex = inputBuffer.indexOf("<");
            int closeIndex = inputBuffer.indexOf(">") + 1;
            inputBuffer.replace(openIndex, closeIndex, replacement);
        }
        return inputBuffer.toString();
    }

    private boolean lineContainsParameter(String line) {
        for (String parameter : parameters) {
            if (line.contains(parameter)) return true;
        }
        return false;
    }

    private LosesStatistic losesStatisticFromParametersValues(int[] parametersValues) {
        return LosesStatistic.newStatistic()
                .withTanks(parametersValues[0])
                .withArmouredFightingVehicles(parametersValues[1])
                .withCannons(parametersValues[2])
                .withMultipleRocketLaunchers(parametersValues[3])
                .withAntiAirDefenseDevices(parametersValues[4])
                .withPlanes(parametersValues[5])
                .withHelicopters(parametersValues[6])
                .withDrones(parametersValues[7])
                .withCruiseMissiles(parametersValues[8])
                .withShipsOrBoats(parametersValues[9])
                .withCarsAndTankers(parametersValues[10])
                .withSpecialEquipment(parametersValues[11])
                .withPersonnel(parametersValues[12])
                .build();
    }
}
