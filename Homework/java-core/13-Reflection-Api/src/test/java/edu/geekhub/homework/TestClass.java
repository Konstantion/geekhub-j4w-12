package edu.geekhub.homework;

import edu.geekhub.homework.inject.Injectable;

import java.time.Month;

/**
 * Simple class for testing reflection
 */
public class TestClass {
    @Injectable(propertyName = "testString")
    public String stringField;
    @Injectable
    public Integer integerField;
    public int intField;
    @Injectable
    public Month enumField;
}
