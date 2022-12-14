package day6;

import tools.InputReader;

import java.util.HashSet;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) {
        String s = InputReader.readAllLines().get(0);
        int ans = solve(s, 14);
        System.out.println(ans);
    }

    public static int solve(String s, int diffAmt) {

        for (int i = diffAmt - 1; i < s.length(); i++) {
            Set<Character> diffChars = new HashSet<>();
            for (int j = 0; j < diffAmt; j++) {
                diffChars.add(s.charAt(i - j));
            }
            if (diffChars.size() == diffAmt) {
                return i + 1;
            }
        }
        return -1;
    }
}
