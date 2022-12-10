package org.example.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExercisesTest {
    private Exercises exercises;
    private final Cities citiesRepo = new Cities();

    @BeforeEach
    void setUp() {
        exercises = new Exercises();
    }

    @Test
    void get_country_cities_count_test() {
        Map<String, Long> actualCountryCitiesCount = exercises.getCountryCitiesCount();


        Map<String, Long> expectedCountryCitiesCount = new HashMap<>();
        for (City city : citiesRepo.getAllCities().values()) {
            if (!expectedCountryCitiesCount.containsKey(city.getCountryCode())) {
                expectedCountryCitiesCount.put(
                        city.getCountryCode(),
                        1L
                );
            } else {
                Long tempSum = expectedCountryCitiesCount.get(city.getCountryCode()) + 1L;
                expectedCountryCitiesCount.put(city.getCountryCode(), tempSum);
            }
        }

        assertEquals(
                expectedCountryCitiesCount,
                actualCountryCitiesCount
        );
    }

    @Test
    void most_populated_city_test() {
        City city = exercises.mostPopulatedCity();

        Integer actualPopulation = city.getPopulation();
        Integer expectedPopulation = citiesRepo
                .getAllCities()
                .values()
                .stream()
                .map(City::getPopulation)
                .max(Integer::compare)
                .orElseThrow();


        assertEquals(
                expectedPopulation,
                actualPopulation
        );
    }

    @Test
    void min_population_city_test() {
        City city = exercises.minPopulatedCity();

        Integer actualPopulation = city.getPopulation();
        Integer expectedPopulation = citiesRepo
                .getAllCities()
                .values()
                .stream()
                .map(City::getPopulation)
                .max(Comparator.reverseOrder())
                .orElseThrow();


        assertEquals(
                expectedPopulation,
                actualPopulation
        );
    }

    @Test
    void most_populated_country_test() {
        String actualCountry = exercises.mostPopulatedCountry();

        String expectedCountry;
        Map<String, Integer> countriesPopulation = new HashMap<>();
        for (City city : citiesRepo.getAllCities().values()) {
            if (!countriesPopulation.containsKey(city.getCountryCode())) {
                countriesPopulation.put(
                        city.getCountryCode(),
                        city.getPopulation()
                );
            } else {
                Integer tempSum = countriesPopulation.get(city.getCountryCode()) + city.getPopulation();
                countriesPopulation.put(city.getCountryCode(), tempSum);
            }
        }
        expectedCountry = countriesPopulation
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow()
                .getKey();

        assertEquals(
                expectedCountry,
                actualCountry
        );
    }

    @Test
    void min_populated_country_test() {
        String actualCountry = exercises.minPopulatedCountry();

        String expectedCountry;
        Map<String, Integer> countriesPopulation = new HashMap<>();
        for (City city : citiesRepo.getAllCities().values()) {
            if (!countriesPopulation.containsKey(city.getCountryCode())) {
                countriesPopulation.put(
                        city.getCountryCode(),
                        city.getPopulation()
                );
            } else {
                Integer tempSum = countriesPopulation.get(city.getCountryCode()) + city.getPopulation();
                countriesPopulation.put(city.getCountryCode(), tempSum);
            }
        }
        expectedCountry = countriesPopulation
                .entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow()
                .getKey();

        assertEquals(
                expectedCountry,
                actualCountry
        );
    }

    @Test
    void total_population_test() {
        Long actualTotalPopulation = exercises.totalPopulation();

        Long expectedTotalPopulation = 0L;
        for (City city : citiesRepo.getAllCities().values()) {
            expectedTotalPopulation += city.getPopulation();
        }

        assertEquals(
                expectedTotalPopulation,
                actualTotalPopulation
        );
    }

    @Test
    void population_of_each_country_test() {
        Map<String, Integer> actualPopulationOfEachCountry = exercises.populationOfEachCountry();

        Map<String, Integer> expectedPopulationOfEachCountry = new HashMap<>();
        for (City city : citiesRepo.getAllCities().values()) {
            if (!expectedPopulationOfEachCountry.containsKey(city.getCountryCode())) {
                expectedPopulationOfEachCountry.put(
                        city.getCountryCode(),
                        city.getPopulation()
                );
            } else {
                Integer tempSum = expectedPopulationOfEachCountry.get(city.getCountryCode()) + city.getPopulation();
                expectedPopulationOfEachCountry.put(city.getCountryCode(), tempSum);
            }
        }

        assertEquals(
                expectedPopulationOfEachCountry,
                actualPopulationOfEachCountry
        );
    }

    @Test
    void population_of_specific_country_test() {
        String specificCountry = "FRA";
        Integer actualPopulationOfSpecificCountry = exercises
                .populationOfSpecificCountry(specificCountry);

        Integer expectedPopulationOfSpecificCountry = 0;
        for (City city : citiesRepo.getAllCities().values()) {
            if (city.getCountryCode().equals(specificCountry)) {
                expectedPopulationOfSpecificCountry += city.getPopulation();
            }
        }

        assertEquals(
                expectedPopulationOfSpecificCountry,
                actualPopulationOfSpecificCountry
        );
    }

    @Test
    void find_specific_city_by_name() {
        String cityName = "Metz";
        City actualCity = exercises.specificCityByName(cityName);

        City expectedCity = new City();
        for (City city : citiesRepo.getAllCities().values()) {
            if (city.getName().equals(cityName)) {
                expectedCity = city;
                break;
            }
        }

        assertEquals(
                expectedCity,
                actualCity
        );
    }
}