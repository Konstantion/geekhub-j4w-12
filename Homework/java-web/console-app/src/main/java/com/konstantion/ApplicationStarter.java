package com.konstantion;

import com.konstantion.view.ViewFabric;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationStarter {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.konstantion");

        ViewFabric viewFabric = context.getBean(ViewFabric.class);

        viewFabric.printMainDialog();
    }
}
