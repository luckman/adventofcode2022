package day12;

import tools.InputReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day12 {
    char[][] grid = new char[200][200];
    int[] dx = {1, 0, -1, 0};
    int[] dy = {0, 1, 0, -1};

    public void solve() {
        List<String> lines = InputReader.readAllLines();
        for (int i = 0; i < 200; i++) {
            Arrays.fill(grid[i], '˚');
        }
        Point start = null;
        Point end = null;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                int x = i + 1;
                int y = j + 1;
                grid[x][y] = lines.get(i).charAt(j);
                if (lines.get(i).charAt(j) == 'S') {
                    grid[x][y] = 'a';
                    start = new Point(x, y);
                }
                if (lines.get(i).charAt(j) == 'E') {
                    grid[x][y] = 'z';
                    end = new Point(x, y);
                }
            }
            grid[i + 1][lines.get(i).length() + 1] = 1000;
        }
//        int ans = findMinPath(start, end);
//        System.out.println(ans);
        int minAns = Integer.MAX_VALUE;
        for (int i = 1; i < 199; i++) {
            for (int j = 1; j < 199; j++) {
                if (grid[i][j] == 'a') {
                    int cur = findMinPath(new Point(i, j), end);
                    if (cur < minAns) {
                        minAns = cur;
                    }
                }
            }
        }
        System.out.println(minAns);
    }

    public int findMinPath(Point start, Point end) {
        int[][] dist = new int[200][200];
        for (int i = 0; i < 200; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE / 2);
        }
        LinkedList<Point> queue = new LinkedList<>();
        queue.add(start);
        dist[start.x][start.y] = 0;
        while (!queue.isEmpty()) {
            Point cur = queue.poll();
//            System.out.println(cur.x + " " + cur.y);
            for (int k = 0; k < 4; k++) {
                Point next = new Point(cur.x + dx[k], cur.y + dy[k]);
                if (grid[next.x][next.y] > 'z') {
                    continue;
                }
                if (grid[next.x][next.y] - grid[cur.x][cur.y] <= 1) {
                    if (dist[next.x][next.y] > dist[cur.x][cur.y] + 1) {
                        queue.add(next);
                        dist[next.x][next.y] = dist[cur.x][cur.y] + 1;
                    }
                }
            }
        }
        return dist[end.x][end.y];
    }

    static class Point {
        int x;
        int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        new Day12().solve();
    }
}
