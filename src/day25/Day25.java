package day25;

import tools.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day25 {
    private static final boolean SECOND_TASK = false;
    private List<SnafuNumber> numbers = new ArrayList<>();

    public void solve() {
        readInput();
        if (!SECOND_TASK) {
            SnafuNumber ans = solvePart1();
            System.out.println(ans.presentation);
        } else {
            long ans = solvePart2();
            System.out.println(ans);
        }
    }

    public SnafuNumber solvePart1() {
        long sum = 0;
        for (SnafuNumber snafuNumber : numbers) {
            sum += snafuNumber.decimalValue();
        }
        return SnafuNumber.fromDecimal(sum);
    }

    public long solvePart2() {
        long result = 0;

        return result;
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (String line : lines) {
            numbers.add(new SnafuNumber(line));
        }
    }

    static class SnafuNumber {
        public String presentation;
        final static Map<Character, Integer> digitValue = Map.of(
                '=', -2,
                '-', -1,
                '0', 0,
                '1', 1,
                '2', 2
        );
        final static Map<Integer, Character> snafuDigits = Map.of(
                -2, '=',
                -1, '-',
                0, '0',
                1, '1',
                2, '2'
        );
        final static long[] pow5 = new long[20];
        static {
            pow5[19] = 1;
            for (int j = 18; j >= 0; j--) {
                pow5[j] = pow5[j + 1] * 5;
            }
        }

        public SnafuNumber(String s) {
            this.presentation = s;
        }

        public static SnafuNumber fromDecimal(long decimal) {
            char[] snafu = new char[20];
            Arrays.fill(snafu, '=');
            char[] chars = "210-=".toCharArray();
            for (int i = 0; i < 20; i++) {
                for (char c : chars) {
                    snafu[i] = c;
                    if (new SnafuNumber(String.valueOf(snafu)).decimalValue() <= decimal) {
                        break;
                    }
                }
            }

            StringBuilder result = new StringBuilder();
            int from = 0;
            while (snafu[from] == '0') {
                from++;
            }
            for (int i = from; i < 20; i++) {
                result.append(snafu[i]);
            }

            return new SnafuNumber(result.toString());
        }

        public long decimalValue() {
            long result = 0;
            long pow5 = 1;
            for (int j = presentation.length() - 1; j >= 0; j--) {
                result = result + digitValue.get(presentation.charAt(j)) * pow5;
                pow5 *= 5;
            }
            return result;
        }

    }

    public static void main(String[] args) {
        new Day25().solve();
    }
}
