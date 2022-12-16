package day15;

import tools.InputReader;
import tools.IntPoint2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 {
    private static boolean SECOND_TASK = true;
    List<IntPoint2d> sensors = new ArrayList<>();
    List<IntPoint2d> beacons = new ArrayList<>();


    public void solve() {
        readInput();
        if (!SECOND_TASK) {
            int ans = calcPointsWithoutBeacon(2000000);
            System.out.println(ans);
        } else {
            long ans = findDistressSignalFrequency(4000000, 4000000);
            System.out.println(ans);
        }
    }

    public int calcPointsWithoutBeacon(int line) {
        final int MAX_X = 7000000;
        final int SHIFT = 2000000;

        boolean[] noBeacon = new boolean[MAX_X];
        Arrays.fill(noBeacon, false);

        for (int i = 0; i < sensors.size(); i++) {
            IntPoint2d sensor = sensors.get(i);
            int distToBeacon = manhattanDistance(sensor, beacons.get(i));
            System.out.println("Dist to beacon == " + distToBeacon);
            int distToLine = Math.abs(sensor.y - line);
            int diff = distToBeacon - distToLine;
            if (diff < 0) {
                continue;
            }
            for (int x = SHIFT + sensor.x - diff; x <= SHIFT + sensor.x + diff; x++) {
                noBeacon[x] = true;
            }
        }
        for (IntPoint2d beacon : beacons) {
            if (beacon.y == line) {
                noBeacon[beacon.x + SHIFT] = false;
            }
        }
        int result = 0;
        for (int i = 0; i < MAX_X; i++) {
            if (noBeacon[i]) {
                result++;
            }
        }
        return result;
    }

    public long findDistressSignalFrequency(int maxX, int maxY) {
        IntPoint2d resultPoint = null;
        for (int i = 0; i < sensors.size(); i++) {
            IntPoint2d sensor = sensors.get(i);
            IntPoint2d beacon = beacons.get(i);

            // because it is the only one point, then distance to at least one sensor
            // should be equal to sensor coverage distance + 1
            int dist = manhattanDistance(sensor, beacon) + 1;

            int fromX = Math.max(0, sensor.x - dist);
            int toX = Math.min(maxX, sensor.x + dist);
            for (int x = fromX; x <= toX; x++) {
                int dy = dist - Math.abs(x - sensor.x);

                int y = sensor.y + dy;
                if (y >= 0 && y <= maxY) {
                    IntPoint2d possibleDistress = IntPoint2d.of(x, y);
                    if (!pointCoveredBySomeSensor(possibleDistress)) {
                        resultPoint = possibleDistress;
                        System.out.println("Result point: " + resultPoint);
                    }
                }

                y = sensor.y - dy;
                if (y >= 0 && y <= maxY) {
                    IntPoint2d possibleDistress = IntPoint2d.of(x, y);
                    if (!pointCoveredBySomeSensor(possibleDistress)) {
                        resultPoint = possibleDistress;
                        System.out.println("Result point: " + resultPoint);
                    }
                }
            }
        }
        return resultPoint.x * 4000000L + resultPoint.y;
    }

    private boolean pointCoveredBySomeSensor(IntPoint2d point) {
        for (int i = 0; i < sensors.size(); i++) {
            int coverageDist = manhattanDistance(sensors.get(i), beacons.get(i));
            if (coverageDist >= manhattanDistance(sensors.get(i), point)) {
                return true;
            }
        }
        return false;
    }

    private static int manhattanDistance(IntPoint2d first, IntPoint2d second) {
        return Math.abs(first.x - second.x) + Math.abs(first.y - second.y);
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (String line : lines) {
            String sensorStr = line.split(":")[0];
            String beaconStr = line.split(":")[1];
            IntPoint2d sensorPoint = IntPoint2d.fromStringWithXAndYLetters(sensorStr.split("at ")[1]);
            IntPoint2d beaconPoint = IntPoint2d.fromStringWithXAndYLetters(beaconStr.split("at ")[1]);
            sensors.add(sensorPoint);
            beacons.add(beaconPoint);
        }
    }

    public static void main(String[] args) {
        new Day15().solve();
    }

}
