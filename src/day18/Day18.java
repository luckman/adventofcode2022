package day18;

import tools.InputReader;
import tools.IntPoint3d;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 {
    private static final boolean SECOND_TASK = true;

    int[] dx = {1, -1, 0, 0, 0, 0};
    int[] dy = {0, 0, 1, -1, 0, 0};
    int[] dz = {0, 0, 0, 0, 1, -1};
    private static int SHIFT = 1;   // shift for every coordinate, needed for dfs

    private Set<IntPoint3d> points = new HashSet<>();
    int mx = -1;
    int my = -1;
    int mz = -1;
    int[][][] area;

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
        for (IntPoint3d point : points) {
            for (int k = 0; k < 6; k++) {
                IntPoint3d neighbour = IntPoint3d.of(point.x + dx[k], point.y + dy[k], point.z + dz[k]);
                if (!points.contains(neighbour)) {
                    result++;
                }
            }
        }
        return result;
    }

    public long solvePart2() {
        area = new int[mx][my][mz];

        for (int x = 0; x < mx; x++) {
            for (int y = 0; y < my; y++) {
                for (int z = 0; z < mz; z++) {
                    if (points.contains(IntPoint3d.of(x, y, z))) {
                        area[x][y][z] = 1;
                    } else {
                        area[x][y][z] = 0;
                    }
                }
            }
        }
        // area: 0 - free; 1 - lava; 2 - visited
        dfs(mx - 1, my - 1, mz - 1);

        long result = 0;
        for (int x = 0; x < mx; x++) {
            for (int y = 0; y < my; y++) {
                for (int z = 0; z < mz; z++) {
                    if (area[x][y][z] == 1) {
                        for (int k = 0; k < 6; k++) {
                            IntPoint3d neighbour = IntPoint3d.of(x + dx[k], y + dy[k], z + dz[k]);
                            if (!pointInsideArea(neighbour) || area[neighbour.x][neighbour.y][neighbour.z] == 2) {
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void dfs(int x, int y, int z) {
        area[x][y][z] = 2;
        for (int k = 0; k < 6; k++) {
            IntPoint3d neighbour = IntPoint3d.of(x + dx[k], y + dy[k], z + dz[k]);
            if (pointInsideArea(neighbour) && area[neighbour.x][neighbour.y][neighbour.z] == 0) {
                dfs(neighbour.x, neighbour.y, neighbour.z);
            }
        }
    }

    private boolean pointInsideArea(IntPoint3d point3d) {
        return point3d.x >= 0 && point3d.x < mx &&
                point3d.y >= 0 && point3d.y < my &&
                point3d.z >= 0 && point3d.z < mz;
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (String line : lines) {
            IntPoint3d point = IntPoint3d.fromStringWithComma(line);
            point = IntPoint3d.of(point.x + SHIFT, point.y + SHIFT, point.z + SHIFT);
            points.add(point);
            mx = Math.max(point.x, mx);
            my = Math.max(point.y, my);
            mz = Math.max(point.z, mz);
        }
        mx += 2;
        my += 2;
        mz += 2;
    }

    public static void main(String[] args) {
        new Day18().solve();
    }
}
