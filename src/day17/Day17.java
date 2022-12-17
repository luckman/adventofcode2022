package day17;

import tools.InputReader;
import tools.IntPoint2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17 {
    private static final boolean SECOND_TASK = true;
    char[] jetPattern;
    List<Rock> rockTypes = new ArrayList<>();
    char[][] chamber = new char[10000000][9];
    boolean printTick = false;

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
        State state = simulate(new State(), 2022);
        return state.towerHeight;
    }

    public long solvePart2() {
        State state = new State();
        Set<String> masks = new HashSet<>();
        String firstRepeatedMask = null;
        long rockIdx1 = -1;
        long rockIdx2 = -1;
        long height1 = -1;
        long height2 = -1;

        int repN = 0;
        for (int i = 0; i < 10000; i++) {
            state = simulate(state, 5 * (i + 1));

            String mask = stateMask(state.tick, (int)state.towerHeight, 10);
            if (masks.contains(mask) && firstRepeatedMask == null) {
                firstRepeatedMask = mask;
            }
            masks.add(mask);
            if (mask.equals(firstRepeatedMask)) {
                if (repN == 3) {
                    rockIdx1 = state.rockIdx;
                    height1 = state.towerHeight;
                }
                if (repN == 4) {
                    rockIdx2 = state.rockIdx;
                    height2 = state.towerHeight;
                    break;
                }
                repN++;
                System.out.println(mask);
                System.out.println("tick == " + state.tick + " rockIdx == " + state.rockIdx + " height == " + state.towerHeight);
            }
        }
        long totalRocks = 1000000000000L;
        long rockDelta = rockIdx2 - rockIdx1;
        long heightDelta = height2 - height1;
        long rocksAfterSecondCheckpoint = totalRocks - rockIdx2;
        long multiplier = rocksAfterSecondCheckpoint / rockDelta;

        long rocksToAdditionalCalculation = rocksAfterSecondCheckpoint - multiplier * rockDelta;
        state = simulate(state, (int)(rocksToAdditionalCalculation + rockIdx2));
        state.rockIdx += multiplier * rockDelta;
        state.towerHeight += multiplier * heightDelta;

        return state.towerHeight;
    }

    private String stateMask(int tick, int fromHeight, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(tick % jetPattern.length);
        for (int chY = fromHeight; chY > Math.max(fromHeight - depth, 0); chY--) {
            for (int chX = 0; chX < 9; chX++) {
                sb.append(chamber[chY][chX]);
            }
        }
        return sb.toString();
    }

    static class State {
        public long towerHeight = 0;
        public long rockIdx = 0;
        public int tick = 0;
        public boolean stopped = true;
        public IntPoint2d tempRockPosition = null;
    }
    public State simulate(State initialState, int maxRocks) {
        State state = initialState;

        IntPoint2d rockPosition = state.tempRockPosition;

        for (int tick = state.tick; tick < Integer.MAX_VALUE && state.rockIdx < maxRocks; tick++) {
            if (state.stopped) {
                rockPosition = rockAppears((int)state.towerHeight);
                state.stopped = false;
            }
            state.tick = tick;
            Rock rock = rockTypes.get((int)state.rockIdx % rockTypes.size());

            if (jetPattern[tick % jetPattern.length] == '<') {
                rockPosition = tryMoveRock(rock, rockPosition, -1, 0);
            } else {
                rockPosition = tryMoveRock(rock, rockPosition, 1, 0);
            }
            IntPoint2d movedDown = tryMoveRock(rock, rockPosition, 0, -1);

            if (movedDown.equals(rockPosition)) {
                addStoppedRockToChamber(rock, rockPosition);
                state.towerHeight = Math.max(state.towerHeight, rockPosition.y + rock.h - 1);
//                printChamber(0, (int)state.towerHeight + 6, null, null);

                state.rockIdx++;
                state.stopped = true;
                state.tempRockPosition = null;
            } else {
                rockPosition = movedDown;
                state.tempRockPosition = rockPosition;
            }
        }
        state.tick++;
        return state;
    }

    private IntPoint2d tryMoveRock(Rock rock, IntPoint2d rockPosition, int dx, int dy) {
        IntPoint2d newRockPosition = IntPoint2d.of(rockPosition.x + dx, rockPosition.y + dy);
        for (int rX = 0; rX < rock.w; rX++) {
            for (int rY = 0; rY < rock.h; rY++) {
                if (rock.form[rY][rX] == '#') {
                    int chY = newRockPosition.y + rY;
                    int chX = newRockPosition.x + rX;
                    if (chamber[chY][chX] != '.') {
                        return rockPosition;
                    }
                }
            }
        }
        return newRockPosition;
    }

    private void addStoppedRockToChamber(Rock rock, IntPoint2d position) {
        for (int rX = 0; rX < rock.w; rX++) {
            for (int rY = 0; rY < rock.h; rY++) {
                if (rock.form[rY][rX] == '#') {
                    int chY = position.y + rY;
                    int chX = position.x + rX;
                    if (chamber[chY][chX] != '.') {
                        throw new RuntimeException();
                    }
                    chamber[chY][chX] = '#';
                }
            }
        }
    }

    private IntPoint2d rockAppears(int towerHeight) {
        return IntPoint2d.of(3, towerHeight + 4);
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        jetPattern = lines.get(0).toCharArray();
        chamber[0] = "+-------+".toCharArray();
        for (int i = 1; i < chamber.length; i++) {
            chamber[i] = "|.......|".toCharArray();
        }
        rockTypes.add(new Rock(List.of(
                "####"
        )));
        rockTypes.add(new Rock(List.of(
                ".#.",
                "###",
                ".#."
        )));
        rockTypes.add(new Rock(List.of(
                "###",      // bottom
                "..#",
                "..#"
        )));
        rockTypes.add(new Rock(List.of(
                "#",
                "#",
                "#",
                "#"
        )));
        rockTypes.add(new Rock(List.of(
                "##",
                "##"
        )));
    }

    public void printChamber(int fromHeight, int toHeight, Rock tempRock, IntPoint2d tempRockPosition) {
        for (int chY = toHeight; chY >= fromHeight; chY--) {
            for (int chX = 0; chX < 9; chX++) {
                if (tempRockPosition != null && tempRock != null &&
                        chX >= tempRockPosition.x && chX < tempRockPosition.x + tempRock.w &&
                        chY >= tempRockPosition.y && chY < tempRockPosition.y + tempRock.h) {
                    if (tempRock.form[chY - tempRockPosition.y][chX - tempRockPosition.x] == '#') {
                        System.out.print('@');
                    } else {
                        System.out.print(chamber[chY][chX]);
                    }
                } else {
                    System.out.print(chamber[chY][chX]);
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }


    static class Rock {
        char[][] form;
        int h;
        int w;
        IntPoint2d leftBottomEdge;  // always [0, 0]

        public Rock(List<String> strings) {
            h = strings.size();
            w = strings.get(0).length();
            form = new char[h][w];
            leftBottomEdge = new IntPoint2d(10, 10);
            for (int rY = 0; rY < h; rY++) {
                for (int rX = 0; rX < w; rX++) {
                    form[rY][rX] = strings.get(rY).charAt(rX);
                    if (form[rY][rX] == '#') {
                        leftBottomEdge.x = Math.min(leftBottomEdge.x, rX);
                        leftBottomEdge.y = Math.min(leftBottomEdge.y, rY);
                    }
                }
            }
            if (!IntPoint2d.of(0, 0).equals(leftBottomEdge)) {
                throw new RuntimeException();
            }
        }
    }

    public static void main(String[] args) {
        new Day17().solve();
    }
}
