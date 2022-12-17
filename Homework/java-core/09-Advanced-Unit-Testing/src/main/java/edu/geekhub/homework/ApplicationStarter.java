package edu.geekhub.homework;

import edu.geekhub.homework.analytics.AnalyticsService;
import edu.geekhub.homework.client.JsonConverter;
import edu.geekhub.homework.client.LosesStatisticHttpClient;
import edu.geekhub.homework.domain.LosesStatisticService;

public class ApplicationStarter {

    public static void main(String[] args) {
        // Write code here :)

        var losesStatisticService = new LosesStatisticService(new JsonConverter(),
                new LosesStatisticHttpClient());

        var losesStatistic = losesStatisticService.getById(13);
        losesStatisticService.create(losesStatistic);
        losesStatisticService.deleteById(losesStatistic.id());

        var analyticsService = new AnalyticsService();

        print(analyticsService.totalCountOfLosesForStatistic(losesStatistic));
        print(analyticsService.totalCountOfLosesForAllStatistics(losesStatisticService.getAll()));
        print(analyticsService.findStatisticWithMinLosesAmounts(losesStatisticService.getAll()));
        print(analyticsService.findStatisticWithMaxLosesAmounts(losesStatisticService.getAll()));

        losesStatisticService.deleteAll();
    }

    static void print(Object objectToPrint) {
        System.out.println(objectToPrint);
    }
}
