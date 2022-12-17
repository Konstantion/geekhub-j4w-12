package edu.geekhub.homework.analytics;

import edu.geekhub.homework.domain.LosesStatistic;

import java.util.Comparator;
import java.util.List;

/**
 * Service shows interesting analytics information
 */
public class AnalyticsService {

    public LosesStatistic findStatisticWithMaxLosesAmounts(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
                .max(Comparator.comparingInt(
                        LosesStatistic::losesAmount
                ))
                .orElse(LosesStatistic.EMPTY_STATISTIC);
    }

    public LosesStatistic findStatisticWithMinLosesAmounts(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
                .min(Comparator.comparingInt(
                        LosesStatistic::losesAmount
                ))
                .orElse(LosesStatistic.EMPTY_STATISTIC);
    }

    public int totalCountOfLosesForStatistic(LosesStatistic losesStatistic) {
        return losesStatistic.losesAmount();
    }

    public int totalCountOfLosesForAllStatistics(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
                .mapToInt(LosesStatistic::losesAmount)
                .sum();
    }

}
