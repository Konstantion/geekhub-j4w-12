package edu.geekhub.homework.task2;


import static edu.geekhub.homework.util.NotImplementedException.TODO_TYPE;
import static java.util.Objects.requireNonNull;

public class LosesInWarParser {
    private static final String[] parameters = new String[]{
            "Танки", "ББМ", "Гармати", "РСЗВ", "Засоби ППО",
            "Літаки", "Гелікоптери", "БПЛА", "Крилаті ракети",
            "Кораблі (катери)", "Cпeцiaльнa тeхнiкa", "Особовий склад"
    };

    LosesStatistic parseLosesStatistic(String input) {
        validateInputPresent(input);

        if(!containsStatistics(input)) {
            return LosesStatistic.empty();
        }

        return TODO_TYPE();
    }

    private boolean containsStatistics(String input) {
        for (int i = 0; i < parameters.length; i++) {
            if(!input.contains(parameters[i])) {
                return false;
            }
        }
        return true;
    }

    private void validateInputPresent(String input) {
        requireNonNull(input);

        if (input.isBlank()) {
            throw new IllegalArgumentException(
                    "Cant process empty input state"
            );
        }
    }

}
