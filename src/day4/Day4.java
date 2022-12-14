package day4;

import tools.InputReader;

import java.util.List;

public class Day4 {
    public static void main(String[] args) {
        List<String> inputLines = InputReader.readAllLines();
        int ans = 0;
        for (String line : inputLines) {
            String firstPart = line.split(",")[0];
            String secondPart = line.split(",")[1];
            int x1 = Integer.parseInt(firstPart.split("-")[0]);
            int y1 = Integer.parseInt(firstPart.split("-")[1]);
            int x2 = Integer.parseInt(secondPart.split("-")[0]);
            int y2 = Integer.parseInt(secondPart.split("-")[1]);
            Line line1 = Line.of(x1, y1);
            Line line2 = Line.of(x2, y2);
            if (overlap(line1, line2)) {
                System.out.println(line);
                ans++;
            }
        }
        System.out.println(ans);
    }

    public static boolean fullyContains(Line line1, Line line2) {
        return (line1.from >= line2.from && line1.to <= line2.to) ||
                (line2.from >= line1.from && line2.to <= line1.to);
    }

    public static boolean overlap(Line line1, Line line2) {
        return (line1.from <= line2.to && line1.to >= line2.from);
    }

    static class Line {
        public int from;
        public int to;

        public static Line of(int from, int to) {
            Line line = new Line();
            line.from = from;
            line.to = to;
            return line;
        }
    }
}
