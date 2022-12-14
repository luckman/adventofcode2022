package day11;

import tools.InputReader;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Day11 {
    public List<LinkedList<Long>> items = new ArrayList<>();
    public List<Function<Long, Long>> operations = new ArrayList<>();
    public List<Predicate<Long>> tests = new ArrayList<>();
    public List<Integer> ifTrue = new ArrayList<>();
    public List<Integer> ifFalse = new ArrayList<>();

    public void solve() {
        List<String> lines = InputReader.readAllLines();
        int currentMonkey = 0;
        for (String line : lines) {
            if (line.contains("Monkey")) {
                currentMonkey = Integer.parseInt(line.replace(":", "").split(" ")[1]);
            }
            if (line.contains("Starting items:")) {
                if (items.size() <= currentMonkey) {
                    items.add(new LinkedList<>());
                }
                String[] split = line.replace("Starting items: ", "").split(", ");
                for (String s : split) {
                    items.get(currentMonkey).add(Long.parseLong(s.trim()));
                }
            }
            if (line.contains("Operation:")) {
                operations.add(old -> {
                    String s = line.split(" = ")[1];
                    if (s.contains("old * old")) {
                        return old * old;
                    } else if (s.contains("old + ")) {
                        Long addition = Long.parseLong(s.split("old \\+ ")[1]);
                        return (old + addition);
                    } else if (s.contains("old * ")) {
                        Long multiplier = Long.parseLong(s.split("old \\* ")[1]);
                        return (old * multiplier);
                    }
                    throw new RuntimeException("Cannot parse Operation");
                });
            }
            if (line.contains("Test:")) {
                tests.add(value -> {
                    long div = Long.parseLong(line.split("by ")[1]);
                    return value % div == 0;
                });
            }
            if (line.contains("If true")) {
                int newMonkey = Integer.parseInt(line.split("monkey ")[1]);
                ifTrue.add(newMonkey);
            }
            if (line.contains("If false")) {
                int newMonkey = Integer.parseInt(line.split("monkey ")[1]);
                ifFalse.add(newMonkey);
            }
        }

        System.out.println("Input read ok");
        System.out.println(calcSimulation2());
    }

    public Long calcSimulation() {
        List<Integer> countTimes = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            countTimes.add(0);
        }
        for (int round = 1; round <= 20; round++) {
            for (int monkey = 0; monkey < items.size(); monkey++) {
                var cItems = items.get(monkey);
                while (!cItems.isEmpty()) {
                    long item = cItems.poll();
                    long newItem = operations.get(monkey).apply(item);
                    int newCountVal = countTimes.get(monkey) + 1;
                    countTimes.set(monkey, newCountVal);
                    boolean testResult = tests.get(monkey).test(newItem);
                    int newMonkey = -1;
                    if (testResult) {
                        newMonkey = ifTrue.get(monkey);
                    } else {
                        newMonkey = ifFalse.get(monkey);
                    }
                    items.get(newMonkey).add(newItem);
                }
            }
            System.out.println("After round " + round);
            for (int i = 0; i < items.size(); i++) {
                System.out.print(i + ": ");
                System.out.println(items.get(i));
            }
        }
        System.out.println("Count times: " + countTimes);
        countTimes.sort(Comparator.reverseOrder());
        return (long)countTimes.get(0) * countTimes.get(1);
    }

    public Long calcSimulation2() {
        Long totalDivisible = 19 * 5 * 11L * 17 * 7 * 13 * 3 * 2;
//        Long totalDivisible = 23L * 19 * 13 * 17;
        List<Integer> countTimes = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            countTimes.add(0);
        }
        for (int round = 1; round <= 10000; round++) {
            for (int monkey = 0; monkey < items.size(); monkey++) {
                var cItems = items.get(monkey);
                while (!cItems.isEmpty()) {
                    long item = cItems.poll();
                    long newItem = operations.get(monkey).apply(item) % totalDivisible;
                    int newCountVal = countTimes.get(monkey) + 1;
                    countTimes.set(monkey, newCountVal);
                    boolean testResult = tests.get(monkey).test(newItem);
                    int newMonkey = -1;
                    if (testResult) {
                        newMonkey = ifTrue.get(monkey);
                    } else {
                        newMonkey = ifFalse.get(monkey);
                    }
                    items.get(newMonkey).add(newItem);
                }
            }
//            System.out.println("After round " + round);
//            for (int i = 0; i < items.size(); i++) {
//                System.out.print(i + ": ");
//                System.out.println(items.get(i));
//            }
        }
        System.out.println("Count times: " + countTimes);
        countTimes.sort(Comparator.reverseOrder());
        return (long)countTimes.get(0) * countTimes.get(1);
    }

    public static void main(String[] args) {
        new Day11().solve();
    }
}
