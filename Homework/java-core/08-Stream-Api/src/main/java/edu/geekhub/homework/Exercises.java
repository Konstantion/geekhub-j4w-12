package edu.geekhub.homework;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Exercises {
    private static final Cities citiesRepo = new Cities();

    public Map<String, Long> getCountryCitiesCount() {
        // Find the number of cities of each country (use grouping)
        var cities = citiesRepo.getAllCities();
        return cities
                .values()
                .stream()
                .collect(Collectors.groupingBy(City::getCountryCode,
                                Collectors.counting()
                        )
                );
    }

    public City mostPopulatedCity() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .max(Comparator.comparingInt(City::getPopulation))
                .orElseThrow();
    }

    public City minPopulatedCity() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .min(Comparator.comparingInt(City::getPopulation))
                .orElseThrow();
    }

    public String mostPopulatedCountry() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .collect(Collectors.toMap(
                        City::getCountryCode,
                        City::getPopulation,
                        Integer::sum
                ))
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    public String minPopulatedCountry() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .collect(Collectors.toMap(
                        City::getCountryCode,
                        City::getPopulation,
                        Integer::sum
                ))
                .entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    public Long totalPopulation() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .mapToLong(City::getPopulation)
                .sum();
    }

    public Map<String, Integer> populationOfEachCountry() {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .collect(Collectors.toMap(
                        City::getCountryCode,
                        City::getPopulation,
                        Integer::sum
                ));
    }

    public Integer populationOfSpecificCountry(String countryCode) {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .filter(city -> city.getCountryCode().equals(countryCode))
                .map(City::getPopulation)
                .reduce(0, Integer::sum);
    }

    public City specificCityByName(String name) {
        var cities = citiesRepo.getAllCities();

        return cities
                .values()
                .stream()
                .filter(city -> city.getName().equals(name))
                .findFirst()
                .orElse(new City());
    }
}
