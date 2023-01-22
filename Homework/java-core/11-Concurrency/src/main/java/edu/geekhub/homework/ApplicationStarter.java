package edu.geekhub.homework;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ApplicationStarter {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("""
                С - car;
                M - moped;
                T - truck;
                ║ - start zone;
                ▒ - finish zone;
                █ - road zone;
                ♦ - abyss zone.
                
                Execution automatically start in 6 seconds!
                """);
        sleep(6000);
        RatRace ratRace = new RatRace(10);
    }
}
