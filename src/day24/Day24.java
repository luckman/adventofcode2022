package day24;

import tools.InputReader;
import java.util.List;

public class Day24 {
    private static final boolean SECOND_TASK = false;

    public void solve() {
        readInput();
        if (!SECOND_TASK) {
            long ans = solvePart1();
            System.out.println(ans);
        } else {
            long ans = solvePart2();
            System.out.println(ans);
        }
    }

    public long solvePart1() {
        long result = 0;

        return result;
    }

    public long solvePart2() {
        long result = 0;

        return result;
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (String line : lines) {

        }
    }

    public static void main(String[] args) {
        new Day24().solve();
    }
}
