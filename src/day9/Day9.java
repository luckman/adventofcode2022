package day9;

import tools.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Day9 {
    private boolean[][] headPositions = new boolean[2000][2000];
    private boolean[][] tailPositions = new boolean[2000][2000];

    private boolean[][][] positions = new boolean[10][2000][2000];

    // R, U, L, D
    int[] dx = {0, -1, 0, 1};
    int[] dy = {1, 0, -1, 0};
    public void solve() {
        List<String> lines = InputReader.readAllLines();
        List<Move> moves = new ArrayList<>();
        for (String line : lines) {
            moves.add(new Move(line));
        }
        int ans = calcTailCells2(moves, 10);
        System.out.println(ans);
    }

    public int calcTailCells2(List<Move> moves, int ropeLength) {
        int[] x = new int[ropeLength];
        int[] y = new int[ropeLength];
        for (int i = 0; i < ropeLength; i++) {
            x[i] = 1000;
            y[i] = 1000;
        }
        positions[ropeLength - 1][1000][1000] = true;
        for (Move move : moves) {
//            System.out.println(move.direction + " " + move.amount);
            int k = directionIndex(move.direction);
            for (int j = 0; j < move.amount; j++) {
                x[0] = x[0] + dx[k];
                y[0] = y[0] + dy[k];
                for (int i = 1; i < ropeLength; i++) {
                    if (x[i - 1] == x[i] && abs(y[i - 1] - y[i]) == 2) {
                        y[i] = (y[i] + y[i - 1]) / 2;
                    } else if (y[i - 1] == y[i] && abs(x[i - 1] - x[i]) == 2) {
                        x[i] = (x[i - 1] + x[i]) / 2;
                    } else if (abs(x[i] - x[i - 1]) >= 2 || abs(y[i - 1] - y[i]) >= 2) {
                        if (x[i] < x[i - 1]) {
                            x[i]++;
                        }
                        if (x[i] > x[i - 1]) {
                            x[i]--;
                        }
                        if (y[i] > y[i - 1]) {
                            y[i]--;
                        }
                        if (y[i] < y[i - 1]) {
                            y[i]++;
                        }
                    }
                }
                positions[ropeLength - 1][x[ropeLength - 1]][y[ropeLength - 1]] = true;
            }
//            printSnapshot(x, y, ropeLength);
        }
        int result = 0;
        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 2000; j++) {
                if (positions[ropeLength - 1][i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    public void printSnapshot(int[] x, int[] y, int ropeLength) {
        int[][] output = new int[1100][1100];
        for (int i = 0; i < 1100; i++) {
            Arrays.fill(output[i], -1);
        }
        for (int i = ropeLength - 1; i >= 0; i--) {
            output[x[i]][y[i]] = i;
        }
        for (int xx = 990; xx <= 1010; xx++) {
            for (int yy = 990; yy <= 1010; yy++) {
                if (output[xx][yy] == -1) {
                    System.out.print(".");
                } else {
                    System.out.print(output[xx][yy]);
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public int calcTailCells(List<Move> moves) {
        int hx = 1000;
        int hy = 1000;
        int tx = 1000;
        int ty = 1000;
        tailPositions[1000][1000] = true;
        for (Move move : moves) {
            int k = directionIndex(move.direction);
            for (int j = 0; j < move.amount; j++) {
                hx = hx + dx[k];
                hy = hy + dy[k];

                // move tail
                if (abs(hx - tx) == 2) {
                    ty = hy;
                    tx = tx + dx[k];
                }
                if (abs(hy - ty) == 2) {
                    tx = hx;
                    ty = ty + dy[k];
                }
                tailPositions[tx][ty] = true;

            }
        }
        int result = 0;
        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 2000; j++) {
                if (tailPositions[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    public int directionIndex(char c) {
        if (c == 'R') {
            return 0;
        } else if (c == 'U') {
            return 1;
        } else if (c == 'L') {
            return 2;
        } else if (c == 'D') {
            return 3;
        } else {
            throw new RuntimeException();
        }
    }


    public static class Move {
        char direction;
        int amount;
        Move(char direction, int amount) {
            this.direction = direction;
            this.amount = amount;
        }
        Move(String line) {
            this.direction = line.charAt(0);
            this.amount = Integer.parseInt(line.split(" ")[1]);
        }
    }

    public static void main(String[] args) {
        new Day9().solve();
    }
}
