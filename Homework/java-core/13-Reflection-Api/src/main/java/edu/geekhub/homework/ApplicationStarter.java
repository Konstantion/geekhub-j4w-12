package edu.geekhub.homework;

import edu.geekhub.homework.inject.InjectProcessor;

public class ApplicationStarter {
    public static void main(String[] args) {
        InjectProcessor injectProcessor = new InjectProcessor();
        GeekHubCourse course = new GeekHubCourse();
        injectProcessor.process(course);
        System.out.println(course);
    }
}
