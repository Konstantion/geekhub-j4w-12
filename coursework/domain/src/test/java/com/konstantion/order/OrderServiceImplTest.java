package com.konstantion.order;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    @Test
    void arraytest() {
        String lol = "lol";
        List<String> list = new ArrayList<>();
        list.add(lol);
        list.add(lol);
        list.remove(lol);
        System.out.println(list);
    }
}