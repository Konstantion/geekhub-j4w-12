package edu.geekhub.homework.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class UTFString {
    private static final String PROBE = StandardCharsets.UTF_8.name();
    private static final String[] CHARSETS = new String[]
            {"windows-1251", "ISO-8859-1", "UTF-8", "UTF-16", "US_ASCII"};

    private UTFString() {
    }

    public static String of(String string) {
        String charset = charset(string);

        if (!charset.equals(PROBE)) {
            string = convertToUTF(string, charset);
        }

        return string;
    }

    private static String charset(String value, String[] charsets) {
        for (String c : charsets) {
            Charset charset = Charset.forName(c);
            if (value.equals(convert(convert(value, charset.name(), PROBE), PROBE, charset.name()))) {
                return c;
            }
        }
        return StandardCharsets.UTF_8.name();
    }

    public static String charset(String value) {
        return charset(value, CHARSETS);
    }

    private static String convert(String value, String fromEncoding, String toEncoding) {
        try {
            return new String(value.getBytes(fromEncoding), toEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertToUTF(String value, String fromEncoding) {
        try {
            return new String(value.getBytes(fromEncoding), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
