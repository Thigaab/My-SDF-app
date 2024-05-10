package com.example.mysdfapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class textSanitizer {
    public static void main(String[] args) {
        String inputText = "This is a sample text with some bad words like damn and hell.";
        String filteredText = filterBadWords(inputText);
        System.out.println(filteredText);
    }

    public static String filterBadWords(String text) {
        List<String> badWords = Arrays.asList(
                "fuck", "shit", "asshole", "bitch", "bastard", "damn", "hell", "crap",
                "cunt", "cock", "pussy", "dick", "fucker", "motherfucker", "ass", "slut",
                "whore", "twat", "wanker", "bollocks", "arsehole", "bullshit", "shithead",
                "douchebag", "son of a bitch", "jackass", "dipshit", "asshat", "fuckwit",
                "fucktard", "dumbass", "shitface", "bitchass", "fuckface", "piss", "pissed",
                "pissing", "pissed off", "bugger", "wank", "fanny", "nigger", "nigga",
                "faggot", "dyke", "kike", "spic", "chink", "gook", "slope", "raghead",
                "towelhead", "camel jockey", "beaner", "wetback", "coon", "darkie",
                "porch monkey", "jungle bunny", "muffdiver", "muff", "tramp", "skank", "ho",
                "hoebag", "hoe", "jerk", "loser", "moron", "retard", "idiot", "douche",
                "douchenozzle", "dumbfuck", "cockhead", "cockwomble", "numbnuts", "numbskull",
                "knob", "knobhead", "knobend", "bellend", "tosser", "twatwaffle", "twatsicle",
                "asswipe", "assclown", "butthead", "buttface", "buttmunch", "butthole",
                "buttwipe", "dickhead", "dickwad", "dickweed", "douchecanoe", "fuckstick",
                "shitbag", "shitforbrains", "shitlet", "shithouse", "shitstain", "assnugget",
                "asshat", "assface", "assgoblin", "assmonkey", "assnugget", "asspirate",
                "assrocket", "asswaffle", "assweasel", "asswipe", "cumbubble", "cumdumpster",
                "cumguzzler", "cumstain", "dickcheese", "dickface", "dickhole", "dicknose",
                "dickweed", "fuckface", "fucknugget", "fucktrumpet", "shitbreath", "shitdick",
                "shitspitter", "titwank"
        );


        String[] words = text.split("\\s+");
        StringBuilder filteredText = new StringBuilder();

        for (String word : words) {
            if (badWords.contains(word.toLowerCase())) {
                filteredText.append(replaceBadWord(word)).append(" ");
            } else {
                filteredText.append(word).append(" ");
            }
        }

        return filteredText.toString().trim();
    }

    public static String replaceBadWord(String word) {

        return "S%#@!T";
    }
}
