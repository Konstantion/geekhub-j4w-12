package edu.geekhub.homework.task2;


import static edu.geekhub.homework.util.NotImplementedException.TODO_TYPE;
import static java.util.Objects.requireNonNull;

public class LosesInWarParser {

     LosesStatistic parseLosesStatistic(String input) {
         validateInputPresent(input);
         validateContainsStatistics(input);

        return TODO_TYPE();
    }

    private void validateContainsStatistics(String input) {
         String[] statisticsLines = input.split(System.lineSeparator());
    }

    private void validateInputPresent(String input) {
        requireNonNull(input);

        if(input.isBlank()) {
            throw new IllegalArgumentException(
                    "Cant process empty input state"
            );
        }
    }

}
