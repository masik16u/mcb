package com.masik.mythiccharms.util;

import java.util.ArrayList;
import java.util.List;

public class TextHelper {

    public static List<String> wrapText(String text, int width) {
        List<String> lines = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String word : text.split(" ")) {
            if (current.length() + word.length() + 1 > width || word.equals("\n")) {
                lines.add(current.toString());
                current = new StringBuilder();
            }
            if (!current.isEmpty()) current.append(" ");
            if (!word.equals("\n")) current.append(word);
        }
        if (!current.isEmpty()) lines.add(current.toString());
        return lines;
    }

}
