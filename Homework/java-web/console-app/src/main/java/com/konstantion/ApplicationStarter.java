package com.konstantion;

import com.konstantion.view.CliUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationStarter {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.konstantion");
        CliUI uI = context.getBean(CliUI.class);

        uI.printMainDialog();
    }
}
