package edu.geekhub.homework;

public class ApplicationStarter {

    public static void main(String[] args) {
        RatRace ratRace = new RatRace(40);
        String field = ratRace.getGameFieldAsString();
        System.out.println(field);
    }
}
