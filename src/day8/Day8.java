package day8;

import tools.InputReader;

import java.util.Arrays;

public class Day8 {
    private static int[][] forest = new int[100][100];
    private static int n;
    private static int m = -1;

    private static int[] dx = {-1, 0, 1, 0};
    private static int[] dy = {0, -1, 0, 1};
    private static int k = dx.length;

    public static void main(String[] args) {
        var lines = InputReader.readAllLines();
        n = lines.size();
        for (int i = 0; i <= n; i++) {
            Arrays.fill(forest[i], -1);
        }
        for (int i = 0; i < n; i++) {
            if (m == -1) {
                m = lines.get(i).length();
            }
            for (int j = 0; j < m; j++) {
                forest[i + 1][j + 1] = lines.get(i).charAt(j) - '0';
            }
        }

        int ans = calculateScenicScore();
        System.out.println(ans);
    }

    public static int calculateVisible() {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                boolean visible = false;
                int treeHeight = forest[i][j];
                for (int k = 0; k < 4; k++) {
                    int x = i;
                    int y = j;
                    while (true) {
                        x = x + dx[k];
                        y = y + dy[k];
                        if (x < 1 || x > n) {
                            visible = true;
                            break;
                        }
                        if (y < 1 || y > m) {
                            visible = true;
                            break;
                        }
                        if (forest[x][y] >= treeHeight) {
                            break;
                        }
                    }
                }
                if (visible) {
                    result++;
                }
            }
        }
        return result;
    }

    public static int calculateScenicScore() {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int[] byDirection = new int[4];
                Arrays.fill(byDirection, 0);
                for (int k = 0; k < 4; k++) {
                    int x = i;
                    int y = j;
                    while (true) {
                        x = x + dx[k];
                        y = y + dy[k];
                        if (x < 1 || x > n) {
                            break;
                        }
                        if (y < 1 || y > m) {
                            break;
                        }
                        byDirection[k]++;
                        if (forest[x][y] >= forest[i][j]) {
                            break;
                        }
                    }
                }
                int current = byDirection[0] * byDirection[1] * byDirection[2] * byDirection[3];
                if (current > result) {
                    System.out.println(String.format("i = %s, j = %s, current = %s", i, j, current));
                    result = current;
                }
            }
        }
        return result;
    }
}
