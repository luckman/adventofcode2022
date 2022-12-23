package day23;

import tools.InputReader;
import tools.IntPoint2d;

import java.util.Arrays;
import java.util.List;

public class Day23 {
    private static final boolean SECOND_TASK = true;
    static final int SIZE = 400;
    static final int SHIFT = 100;
    char[][] field = new char[SIZE][SIZE];
    //   ------->   y,columns
    //   |
    //   |
    //   |
    //   v  x,rows

    //          0,   1,  2,  3,  4,  5,  6,  7
    //          NW,  N, NE,  E, SE,  S, SW,  W
    int[] dx = {-1, -1, -1,  0,  1,  1,  1,  0};
    int[] dy = {-1,  0,  1,  1,  1,  0, -1, -1};
    int[] directionIdx = {1, 5, 7, 3};

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
        for (int round = 0; round < 10; round++) {
            printField();
            runSimulation(round % 4);
        }
        printField();

        int minX = SIZE, maxX = -1, minY = SIZE, maxY = -1;
        int elvesAmount = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (field[x][y] == '#') {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                    elvesAmount++;
                }
            }
        }
        return (long)(maxX - minX + 1) * (maxY - minY + 1) - elvesAmount;
    }

    public long solvePart2() {
        for (int round = 0; round < 10000; round++) {
            boolean simulResult = runSimulation(round % 4);
            if (!simulResult) {
                return round + 1;
            }
        }
        return -1;
    }

    private boolean runSimulation(int directionIdxIdx) {
        int[][] busy = new int[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            Arrays.fill(busy[x], 0);
        }
        IntPoint2d[][] chosenPoint = new IntPoint2d[SIZE][SIZE];

        boolean result = false;

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (field[x][y] == '#') {
                    int directionIdxChosen = -1;
                    boolean elfNearby = false;
                    for (int di = 0; di < 8; di++) {
                        if (field[x + dx[di]][y + dy[di]] == '#') {
                            elfNearby = true;
                            result = true;
                            break;
                        }
                    }

                    if (elfNearby) {
                        for (int k = 0; k < 4; k++) {
                            int mainDirectionIdx = directionIdx[mod(directionIdxIdx + k, 4)];
                            int di1 = mod(mainDirectionIdx - 1, 8);
                            int di2 = mod(mainDirectionIdx + 1, 8);
                            if (field[x + dx[mainDirectionIdx]][y + dy[mainDirectionIdx]] == '.' &&
                                    field[x + dx[di1]][y + dy[di1]] == '.' &&
                                    field[x + dx[di2]][y + dy[di2]] == '.') {
                                directionIdxChosen = mainDirectionIdx;
                                break;
                            }
                        }
                    }
                    IntPoint2d nextPoint;
                    if (directionIdxChosen >= 0) {
                        nextPoint = new IntPoint2d(x + dx[directionIdxChosen], y + dy[directionIdxChosen]);
                    } else {
                        nextPoint = new IntPoint2d(x, y);
                    }
                    chosenPoint[x][y] = nextPoint;
                    busy[nextPoint.x][nextPoint.y]++;
                }
            }
        }
        char[][] newField = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(newField[i], '.');
        }

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (field[x][y] == '#') {
                    IntPoint2d nextPoint = chosenPoint[x][y];
                    if (busy[nextPoint.x][nextPoint.y] <= 1) {
                        newField[nextPoint.x][nextPoint.y] = '#';
                    } else {
                        newField[x][y] = '#';
                    }
                }
            }
        }
        field = newField;

        return result;
    }

    private int mod(int a, int mod) {
        return (a + mod) % mod;
    }

    private void printField() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                System.out.print(field[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        int row = SHIFT;
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], '.');
        }
        for (String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                field[row][SHIFT + j] = line.charAt(j);
            }
            row++;
        }
    }

    public static void main(String[] args) {
        new Day23().solve();
    }
}
