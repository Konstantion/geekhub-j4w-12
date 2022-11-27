package edu.geekhub.orcostat;

import edu.geekhub.orcostat.DBmigration.TrivialMigration;
import edu.geekhub.orcostat.UI.UserInterface;
import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.beans.MilitaryLossControllerBean;
import edu.geekhub.orcostat.beans.StatisticTableControllerBean;

import java.util.Scanner;

import static java.lang.System.in;

public class ApplicationStarter {
    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface(
                MilitaryLossControllerBean.INSTANCE.getSingleton(),
                StatisticTableControllerBean.INSTANCE.getSingleton(),
                new TrivialMigration(DatabaseBean.INSTANCE.getSingleton()),
                new Scanner(in)
                );
        userInterface.run();
    }
}
