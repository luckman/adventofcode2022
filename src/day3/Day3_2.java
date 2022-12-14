package day3;

import tools.InputReader;

import java.util.*;

public class Day3_2 {
    private static char[] letters = new char[53];
    private static Map<Character, Integer> priorityByChar = new HashMap<>();

    public static void main(String[] args) {
        int j = 1;
        for (char c = 'a'; c <= 'z'; c++) {
            letters[j] = c;
            priorityByChar.put(c, j);
            j++;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            letters[j] = c;
            priorityByChar.put(c, j);
            j++;
        }
        List<String> lines = InputReader.readAllLines();
        int ans = 0;
        for (int i = 0; i < lines.size(); i += 3) {
            ans += calcPriority(lines.get(i), lines.get(i + 1), lines.get(i + 2));
        }
        System.out.println(ans);
    }

    public static int calcPriority(String line1, String line2, String line3) {
        for (Character c : letters) {
            if (line1.contains(c.toString()) && line2.contains(c.toString()) && line3.contains(c.toString())) {
                return priorityByChar.get(c);
            }
        }
        throw new RuntimeException("No same char");
    }

}
