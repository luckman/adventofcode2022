package day24;

import tools.InputReader;

import java.util.Arrays;
import java.util.List;

public class Day24 {
    private static final boolean SECOND_TASK = true;
    char[][] field;
    int h;
    int w;
    // down, left, up, right, stay
    int[] dx = {1, 0, -1, 0, 0};
    int[] dy = {0, -1, 0, 1, 0};

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
        printField();
        return findFastestPath(0, 0, 1, h - 1, w - 2);
    }

    public long solvePart2() {
        int checkpoint1 = findFastestPath(0, 0, 1, h - 1, w - 2);
        int checkpoint2 = findFastestPath(checkpoint1, h - 1, w - 2, 0, 1);
        return findFastestPath(checkpoint2, 0, 1, h - 1, w - 2);
    }

    private int findFastestPath(int startingMinute, int fromX, int fromY, int toX, int toY) {
        final int MAX_MINUTES = 5000;
        boolean[][][] d = new boolean[MAX_MINUTES + 1][h][w];
        d[startingMinute][fromX][fromY] = true;
        for (int t = startingMinute; t < MAX_MINUTES; t++) {
            for (int x = 0; x < h; x++) {
                for (int y = 0; y < w; y++) {
                    if (x == toX && y == toY && d[t][x][y]) {
                        return t;
                    }
                    if (d[t][x][y]) {
                        for (int k = 0; k < 5; k++) {
                            int nx = x + dx[k];
                            int ny = y + dy[k];
                            if (nx < 0 || ny < 0 || nx >= h || ny >= w) {
                                continue;
                            }
                            if (isPointFreeOnMinute(t + 1, nx, ny)) {
                                d[t + 1][nx][ny] = true;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }


    private boolean isPointFreeOnMinute(int minute, int x, int y) {
        if ((x <= 0 || x >= h - 1) && field[x][y] == '.') {
            return true;
        }
        if (field[x][y] == '#') {
            return false;
        }
        int hm = h - 2;
        int wm = w - 2;
        boolean result = true;

        int yr = mod(y - minute - 1, wm) + 1;
        result &= field[x][yr] != '>';
        int yl = mod(y + minute - 1, wm) + 1;
        result &= field[x][yl] != '<';
        int xu = mod(x + minute - 1, hm) + 1;
        result &= field[xu][y] != '^';
        int xd = mod(x - minute - 1, hm) + 1;
        result &= field[xd][y] != 'v';

        return result;
    }


    private int mod(int a, int mod) {
        return (a + 1000 * mod) % mod;
    }

    private void printField() {
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                System.out.print(field[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        field = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            Arrays.fill(field[i], '.');
        }
        h = 0;
        for (String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                field[h][j] = line.charAt(j);
            }
            w = Math.max(w, line.length());
            h++;
        }
    }

    public static void main(String[] args) {
        new Day24().solve();
    }
}
