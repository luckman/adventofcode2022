package day22;

import tools.InputReader;
import tools.IntPoint2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day22 {
    //   ------->   y,columns
    //   |
    //   |
    //   |
    //   v  x,rows

    private static final boolean SECOND_TASK = false;

    private final int SIZE = 205;
    char[][] field = new char[SIZE][SIZE];
    int rows;
    int columns;
    List<PathPart> path = new ArrayList<>();

    // right, down, left, up
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};

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
        IntPoint2d initialPoint = findInitialPosition();
        int direction = 0;
        Position position = new Position(initialPoint, direction);
        for (PathPart pathPart : path) {
            position = applyPathPart(position, pathPart);
        }
        return 1000L * (position.point.x + 1) + 4 * (position.point.y + 1) + position.direction;
    }

    public long solvePart2() {
        long result = 0;

        return result;
    }

    private Position applyPathPart(Position position, PathPart pathPart) {
        if (pathPart.isMove()) {
            int k = position.direction;
            int x = position.point.x;
            int y = position.point.y;
            for (int j = 0; j < pathPart.length; j++) {
                int nx = mod(x + dx[k], SIZE);
                int ny = mod(y + dy[k], SIZE);

                while (field[nx][ny] == ' ') {
                    nx = mod(nx + dx[k], SIZE);
                    ny = mod(ny + dy[k], SIZE);
                }
                if (field[nx][ny] == '#') {
                    break;
                } else {
                    x = nx;
                    y = ny;
                }
            }
            System.out.println(String.format("Went to %s %s", x, y));
            return new Position(IntPoint2d.of(x, y), position.direction);
        } else {
            int nDirection;
            if (pathPart.direction == 'L') {
                nDirection = mod(position.direction - 1, 4);
            } else {
                nDirection = mod(position.direction + 1, 4);
            }
            return new Position(position.point, nDirection);
        }
    }

    private int mod(int x, int mod) {
        return (x + mod) % mod;
    }

    private IntPoint2d findInitialPosition() {
        for (int y = 0; y < columns; y++) {
            if (field[0][y] == '.') {
                return IntPoint2d.of(0, y);
            }
        }
        throw new RuntimeException();
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], ' ');
        }
        int row = 0;
        for (String line : lines) {
            if (line.contains(" ") || line.contains(".")) {
                for (int j = 0; j < line.length(); j++) {
                    field[row][j] = line.charAt(j);
                }
                columns = Math.max(columns, line.length());
                row++;
            }
            if (line.contains("L")) {
                line = line.replace("L", " L ").replace("R", " R ");
                String[] split = line.split(" ");
                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals("L") || split[i].equals("R")) {
                        path.add(new PathPart(split[i].charAt(0)));
                    } else {
                        path.add(new PathPart(Integer.parseInt(split[i])));
                    }
                }
            }
        }
        rows = row;
    }

    private static class Position {
        public IntPoint2d point;
        public int direction;

        public Position(IntPoint2d point, int direction) {
            this.point = point;
            this.direction = direction;
        }
    }

    private static class PathPart {
        public int length = 0;
        public char direction = 0;
        PathPart(int length) {
            this.length = length;
        }
        PathPart(char direction) {
            this.direction = direction;
        }
        public boolean isTurn() {
            return direction != 0;
        }
        public boolean isMove() {
            return length != 0;
        }

        @Override
        public String toString() {
            if (length != 0) {
                return String.valueOf(length);
            } else {
                return String.valueOf(direction);
            }
        }
    }

    public static void main(String[] args) {
        new Day22().solve();
    }
}
