package day3;

import tools.InputReader;

import java.util.*;

public class Day3 {
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
        for (String line : lines) {
            int current = calcPriority(line);
//            System.out.println(current);
            ans += calcPriority(line);
        }
        System.out.println(ans);
    }

    public static int calcPriority(String line) {
        int n = line.length();
        int k = n / 2;
        Set<Character> rucksackContent = new HashSet<>();

        for (int i = 0; i < k; i++) {
            rucksackContent.add(line.charAt(i));
        }
        for (int i = k; i < n; i++) {
            if (rucksackContent.contains(line.charAt(i))) {
//                System.out.println(line.charAt(i));
                return priorityByChar.get(line.charAt(i));
            }
        }
        return -1;
    }
}
