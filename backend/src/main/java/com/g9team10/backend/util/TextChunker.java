package com.g9team10.backend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextChunker {

    private static final int CHUNK_SIZE = 200;
    private static final int OVERLAP = 40;

    public static List<String> split(String text) {
        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        String[] words = text.trim().split("\\s+");

        if (words.length < CHUNK_SIZE) {
            chunks.add(String.join(" ", words));
            return chunks;
        }

        int step = CHUNK_SIZE - OVERLAP;

        for (int start = 0; start < words.length; start += step) {
            int end = Math.min(start + CHUNK_SIZE, words.length);

            chunks.add(String.join(" ", Arrays.copyOfRange(words, start, end)));

            if (end == words.length) {
                break;
            }
        }
        return chunks;
    }
}
