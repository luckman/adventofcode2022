package day5;

import tools.InputReader;

import java.util.LinkedList;
import java.util.List;

public class Day5 {
    public static void main(String[] args) {
        LinkedList<Character>[] stacks = new LinkedList[10];
        List<String> lines = InputReader.readAllLines();
        int total = 0;
        for (String line : lines) {
            if (line.contains("[")) {
                for (int j = 1; j <= 50; j+= 4) {
                    if (line.length() < j) {
                        break;
                    }
                    if (stacks[j / 4] == null) {
                        stacks[j / 4] = new LinkedList<>();
                    }
                    if (line.charAt(j) != ' ') {
                        stacks[j / 4].addFirst(line.charAt(j));
                    }
                    total = Math.max(total, 1 + j / 4);
                }
            }
            if (line.contains("move")) {
                line = line.replace("move ", "");
                int amount = Integer.parseInt(line.split(" from")[0]);
                int from = Integer.parseInt(line.split(" to")[0].split("from ")[1]) - 1;
                int to = Integer.parseInt(line.split("to ")[1]) - 1;
                proceedMove2(stacks, from, to, amount);
            }
        }
        for (int i = 0; i < total; i++) {
            System.out.print(stacks[i].getLast());
        }
        System.out.println();
    }

    public static void proceedMove(LinkedList<Character>[] stacks, int from, int to, int amount) {
        for (int i = 0; i < amount; i++) {
            Character ch = stacks[from].removeLast();
            stacks[to].addLast(ch);
        }
    }

    public static void proceedMove2(LinkedList<Character>[] stacks, int from, int to, int amount) {
        LinkedList<Character> extraStack = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            Character ch = stacks[from].removeLast();
            extraStack.addLast(ch);
        }
        for (int i = 0; i < amount; i++) {
            Character ch = extraStack.removeLast();
            stacks[to].addLast(ch);
        }
    }
}
