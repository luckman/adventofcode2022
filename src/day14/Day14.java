package day14;

import tools.InputReader;
import tools.IntPoint2d;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Day14 {
    private static boolean SECOND_TASK = true;

    int n = 1000;
    int m = 200;
    char[][] cave = new char[n][m];

    int[] dx = {0, -1, 1};
    int[] dy = {1, 1, 1};


    public void solve() {
        readInput();
        int ans = calcRestSandUnits();
        System.out.println(ans);
    }

    public int calcRestSandUnits() {
        int ans = 0;
        Supplier<Boolean> simulation = SECOND_TASK ? this::simulateSingleUnitFailure2 : this::simulateSingleUnitFailure1;
        while (simulation.get()) {
            ans++;
        }
        return ans;
    }

    public boolean simulateSingleUnitFailure1() {
        int x = 500;
        int y = 0;
        while (y < m - 2) {
            boolean moved = false;
            for (int k = 0; k < 3; k++) {
                if (cave[x + dx[k]][y + dy[k]] == '.') {
                    x = x + dx[k];
                    y = y + dy[k];
                    moved = true;
                    break;
                }
            }
            if (!moved) {
                break;
            }
        }
        cave[x][y] = 'o';
        return y < m - 2;
    }

    public boolean simulateSingleUnitFailure2() {
        int x = 500;
        int y = 0;
        if (cave[x][y] == 'o') {
            return false;
        }
        boolean moved = true;
        while (moved) {
            moved = false;
            for (int k = 0; k < 3; k++) {
                if (cave[x + dx[k]][y + dy[k]] == '.') {
                    x = x + dx[k];
                    y = y + dy[k];
                    moved = true;
                    break;
                }
            }
        }
        cave[x][y] = 'o';
        return true;
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (int x = 0; x < n; x++) {
            Arrays.fill(cave[x], '.');
        }
        int maxY = -1;
        for (String line : lines) {
            String[] pointsStr = line.split(" -> ");
            List<IntPoint2d> rockPoints = Arrays.stream(pointsStr).map(IntPoint2d::fromStringWithComma).toList();
            for (int i = 1; i < rockPoints.size(); i++) {
                IntPoint2d point = rockPoints.get(i);
                IntPoint2d prevPoint = rockPoints.get(i - 1);
                for (int x = Math.min(point.x, prevPoint.x); x <= Math.max(point.x, prevPoint.x); x++) {
                    for (int y = Math.min(point.y, prevPoint.y); y <= Math.max(point.y, prevPoint.y); y++) {
                        cave[x][y] = '#';
                        if (y > maxY) {
                            maxY = y;
                        }
                    }
                }
            }
        }
        if (SECOND_TASK) {
            for (int x = 0; x < n; x++) {
                cave[x][maxY + 2] = '#';
            }
        }
        printCave();
    }

    public void printCave() {
        for (int y = 0; y < m; y++) {
            for (int x = 400; x < 600; x++) {
                System.out.print(cave[x][y]);
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        new Day14().solve();
    }
}
