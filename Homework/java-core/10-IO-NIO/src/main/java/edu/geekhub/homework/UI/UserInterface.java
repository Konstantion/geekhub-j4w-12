package edu.geekhub.homework.UI;

import edu.geekhub.homework.core.logger.Logger;
import edu.geekhub.homework.core.logger.Message;
import edu.geekhub.homework.core.logger.MessageType;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class UserInterface {
    private final Logger logger;
    private LogType logType;
    private OrderingType orderingType;
    private SortParameter sortParameter;
    private Predicate<Message> filter;
    private Comparator<Message> comparator;
    private final Scanner scanner;
    private static final String NO_SUCH_OPTION = "No such option provided";

    public UserInterface(Logger logger, Scanner scanner) {
        this.scanner = scanner;
        this.logger = logger;
        logType = LogType.ALL;
        orderingType = OrderingType.DESCENDING;
        sortParameter = SortParameter.DATE;
    }

    private void initialize() {
        initializeFilter();
        initializeComparator();
    }

    public void drawMainMenu() {
        initialize();
        String menu = String.format(
                "Current configuration [LOG TYPE : %s] [ORDERING : %s] [SORT PARAMETER : %s]%n" +
                "If you want to change [LOG TYPE] enter 1%n" +
                "If you want to change [ORDERING] enter 2%n" +
                "If you want to change [SORT PARAMETER] enter 3%n" +
                "If you want to see logs with current configuration enter 4%n",
                logType.name(),
                orderingType.name(),
                sortParameter.name()
        );
        System.out.print(menu);
        mainMenuHandler();
    }

    public void mainMenuHandler() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> logTypeChooseMenu();
                case 2 -> orderingChooseMenu();
                case 3 -> sortParameterChooseMenu();
                case 4 -> showLogs();
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
        drawMainMenu();
    }

    public void logTypeChooseMenu() {
        String menu = String.format(
                "Current [LOG TYPE : %s]%n" +
                "If you want to change [LOG TYPE] to ALL enter 1%n" +
                "If you want to change [LOG TYPE] to INFO enter 2%n" +
                "If you want to change [LOG TYPE] to ERROR enter 3%n",
                logType.name()
        );
        System.out.print(menu);
        handleLogTypeChange();
    }

    public void handleLogTypeChange() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> logType = LogType.ALL;
                case 2 -> logType = LogType.ONLY_INFO;
                case 3 -> logType = LogType.ONLY_ERROR;
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
    }

    public void orderingChooseMenu() {
        String menu = String.format(
                "Current [ORDERING : %s]%n" +
                "If you want to change [ORDERING] to ASCENDING enter 1%n" +
                "If you want to change [ORDERING] to DESCENDING enter 2%n",
                orderingType.name()
        );
        System.out.print(menu);
        handleOrderingChange();
    }

    public void handleOrderingChange() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> orderingType = OrderingType.ASCENDING;
                case 2 -> orderingType = OrderingType.DESCENDING;
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
    }

    public void sortParameterChooseMenu() {
        String menu = String.format(
                "Current [SORT PARAMETER : %s]%n" +
                "If you want to change [SORT PARAMETER] to DATE enter 1%n" +
                "If you want to change [SORT PARAMETER] to TYPE enter 2%n",
                sortParameter.name()
        );
        System.out.print(menu);
        handleSortParameterChange();
    }

    public void handleSortParameterChange() {
        if (scanner.hasNextInt()) {
            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1 -> sortParameter = SortParameter.DATE;
                case 2 -> sortParameter = SortParameter.TYPE;
                default -> System.out.println(NO_SUCH_OPTION);
            }
        } else {
            scanner.next();
            System.out.println(NO_SUCH_OPTION);
        }
    }

    private void showLogs() {
        List<Message> messages = logger.getMessages();
        System.out.println(center("LOG MESSAGES:", 100));
        if (!messages.isEmpty()) {
            messages.stream()
                    .filter(filter)
                    .sorted(comparator)
                    .forEach(m -> System.out.println(m.getLogString()));
        } else {
            System.out.println("Logs are empty");
        }
    }

    private void initializeFilter() {
        switch (logType) {
            case ALL -> filter = m -> true;
            case ONLY_INFO -> filter = m -> m.getType().equals(MessageType.INFO);
            case ONLY_ERROR -> filter = m -> m.getType().equals(MessageType.ERROR);
        }
    }

    private void initializeComparator() {
        switch (sortParameter) {
            case DATE -> comparator = Comparator.comparing(Message::getDate);
            case TYPE -> comparator = Comparator.comparingInt(m -> m.getType().val);
        }

        if (orderingType == OrderingType.DESCENDING) {
            comparator = comparator.reversed();
        }
    }

    private String center(String text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }
}
