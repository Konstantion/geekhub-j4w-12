package org.example.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExercisesTest {
    private Exercises exercises;
    private Cities citiesRepo = new Cities();

    @BeforeEach
    void setUp() {
        exercises = new Exercises();
    }

    @Test
    void get_country_cities_count_test() {
        Map<String, Long> map = exercises.getCountryCitiesCount();

        System.out.println(map);
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
}