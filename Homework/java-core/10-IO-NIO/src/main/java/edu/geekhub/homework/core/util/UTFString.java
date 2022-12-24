package edu.geekhub.homework.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class UTFString {
    private UTFString() {

    }

    public static String of(String string) {
        try {
            byte[] bytes = string.getBytes("windows-1251");
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
