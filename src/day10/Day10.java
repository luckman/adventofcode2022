package day10;

import tools.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day10 {
    public void solve() {
        List<String> lines = InputReader.readAllLines();
        List<Command> commands = new ArrayList<>();
        for (String line : lines) {
            Command command = new Command(line);
            commands.add(command);
        }
        int ans = calculateStrength(commands);
        System.out.println(ans);
        drawScreen(commands);
    }

    public int calculateStrength(List<Command> commands) {
        int[] xValues = calculateXValues(commands);
        int result = 0;
        for (int j = 19; j < 220; j = j + 40) {
            result = result + (j + 1) * xValues[j];
        }
        return result;
    }

    public void drawScreen(List<Command> commands) {
        int[] xValues = calculateXValues(commands);
        for (int i = 0; i < 240; i++) {
            int px = i % 40;
            int py = i / 40;
            if (py >= 1 && i % 40 == 0) {
                System.out.println();
            }
            if (Math.abs(xValues[i] - px) <= 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }
        }
    }

    public int[] calculateXValues(List<Command> commands) {
        int[] xValues = new int[250];
        int pos = 0;
        xValues[pos] = 1;
        for (Command command : commands) {
            if ("noop".equals(command.type)) {
                pos++;
                xValues[pos] = xValues[pos - 1];
            }
            if ("addx".equals(command.type)) {
                pos++;
                xValues[pos] = xValues[pos - 1];
                pos++;
                xValues[pos] = xValues[pos - 1] + command.value;
            }
            if (pos >= 245) {
                break;
            }
        }
        return xValues;
    }

    class Command {
        String type;
        int value;
        Command(String line) {
            this.type = line.split(" ")[0];
            if (this.type.equals("addx")) {
                value = Integer.parseInt(line.split(" ")[1]);
            }
        }
    }

    public static void main(String[] args) {
        new Day10().solve();
    }
}
