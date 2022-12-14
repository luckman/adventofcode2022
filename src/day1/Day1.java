package day1;

import tools.InputReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        List<String> lines = InputReader.readAllLines();
        System.out.println(calcMax2(lines));
    }

    public static long calcMax(List<String> lines) {
        long max = -1;
        long current = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                max = Math.max(max, current);
                current = 0;
            } else {
                current += Long.parseLong(line);
            }
        }
        return max;
    }

    public static long calcMax2(List<String> lines) {
        List<Long> calories = new ArrayList<>();
        long current = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                calories.add(current);
                current = 0;
            } else {
                current += Long.parseLong(line);
            }
        }
        if (current != 0) {
            calories.add(current);
        }
        Collections.sort(calories);
        Collections.reverse(calories);
        return calories.get(0) + calories.get(1) + calories.get(2);
    }
}