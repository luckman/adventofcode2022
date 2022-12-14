package day2;

import tools.InputReader;

import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        List<String> lines = InputReader.readAllLines();
        int totalScore = 0;
        for (String line : lines) {
            char first = line.charAt(0);
            char second = line.charAt(2);
            totalScore += calcScore2(first, second);
        }
        System.out.println(totalScore);
    }

    public static int calcScore(char first, char second) {
        int score = 0;
        if (second == 'X') {
            score += 1;
        } else if (second == 'Y') {
            score += 2;
        } else if (second == 'Z') {
            score += 3;
        }
        int p1 = getFigureFromChar(first);
        int p2 = getFigureFromChar(second);
        if (p1 == p2) {
            score += 3;
        } else if ((p2 - p1 + 3) % 3 == 1) {
            score += 6;
        }
        return score;
    }

    public static int calcScore2(char first, char second) {
        int score = 0;
        int firstNum = getFigureFromChar(first);
        int secondNum = -1;
        if (second == 'X') {
            secondNum = (firstNum - 1 + 3) % 3;
        } else if (second == 'Y') {
            secondNum = firstNum;
        } else if (second == 'Z') {
            secondNum = (firstNum + 1) % 3;
        }
        score += (secondNum + 1);
        if (second == 'Y') {
            score += 3;
        } else if (second == 'Z') {
            score += 6;
        }
//        System.out.println(score);
        return score;
    }

    public static char getFigureFromChar(char c) {
        if (c == 'A' || c == 'X') {
            return 0;
        }
        if (c == 'B' || c == 'Y') {
            return 1;
        }
        if (c == 'C' || c == 'Z') {
            return 2;
        }
        throw new RuntimeException("Unknown char: " + c);
    }
}
