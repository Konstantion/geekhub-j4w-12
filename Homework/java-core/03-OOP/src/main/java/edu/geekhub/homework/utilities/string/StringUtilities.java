package edu.geekhub.homework.utilities.string;

public class StringUtilities {
    public static String center(String text, int len) {
        String out = String.format("%" + "%d" + "s%s%" + "d" + "s", len, "", text, len, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }

    private StringUtilities() {

    }
}
