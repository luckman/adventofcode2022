package day19;

import tools.InputReader;

import java.util.*;

public class Day19 {
    private static final boolean SECOND_TASK = true;
    private List<Blueprint> blueprints = new ArrayList<>();

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
        for (Blueprint blueprint : blueprints) {
            result += new BlueprintQualityCalculator(blueprint, blueprint.id).calculateQuality();
        }
        return result;
    }

    public long solvePart2() {
        long result = 1;
        for (int i = 0; i < Math.min(3, blueprints.size()); i++) {
            result *= new BlueprintQualityCalculator(blueprints.get(i), 1).calculateQuality();
        }
        return result;
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        int idx = 1;
        for (String line : lines) {
            String[] robotStrings = line.split(":")[1].split("\\.");
            Blueprint blueprint = new Blueprint();
            blueprint.id = idx;
            for (String robotStr : robotStrings) {
                RssType collectType = RssType.fromString(robotStr.split("Each ")[1].split(" robot")[0]);
                Robot robot = new Robot(collectType);
                int orePrice = Integer.parseInt(robotStr.split("costs ")[1].split(" ore")[0]);
                robot.price.put(RssType.ORE, orePrice);
                if (robotStr.contains(" and ")) {
                    RssType rss = RssType.fromString(robotStr.split("and ")[1].split(" ")[1]);
                    int price = Integer.parseInt(robotStr.split("and ")[1].split(" ")[0]);
                    robot.price.put(rss, price);
                }
                blueprint.robotTypes.put(collectType, robot);
            }
            blueprints.add(blueprint);
            idx++;
        }
    }

    static class BlueprintQualityCalculator {
        private final int multiplier;
        private final EnumMap<RssType, Robot> robotTypes;
        List<HashSet<State>> statesByMinute = new ArrayList<>();
        static int minutes = SECOND_TASK ? 32 : 24;

        public BlueprintQualityCalculator(Blueprint blueprint, int multiplier) {
            this.multiplier = multiplier;
            this.robotTypes = blueprint.robotTypes;
        }

        public int calculateQuality() {
            for (int i = 0; i <= minutes; i++) {
                statesByMinute.add(new HashSet<>());
            }
            State initial = new State();
            initial.addFreeRobot(RssType.ORE);
            statesByMinute.get(0).add(initial);
            for (int minute = 0; minute < minutes; minute++) {
                if (minute > minutes * 0.85) {
                    System.out.println(String.format("i == %s, states == %s", minute, statesByMinute.get(minute).size()));
                }
                removeUnnecessaryStates(minute);

                List<RssType> rssTypesRev = Arrays.asList(RssType.values());
                Collections.reverse(rssTypesRev);
                for (State state : statesByMinute.get(minute)) {
                    for (RssType rssType : rssTypesRev) {
                        Robot robot = robotTypes.get(rssType);
                        if (state.isEnoughRssForRobot(robot)) {
                            State newState = State.copyOf(state);
                            newState.collectResources();
                            newState.buildRobot(robot);
                            statesByMinute.get(minute + 1).add(newState);
                            // no need to try other robots if we can create GEODE or OBSIDIAN
                            if (rssType == RssType.GEODE || rssType == RssType.OBSIDIAN) {
                                break;
                            }
                        }
                    }
                    State newState = State.copyOf(state);
                    newState.collectResources();
                    statesByMinute.get(minute + 1).add(newState);
                }
            }
            int maxGeode = 0;
            for (State state : statesByMinute.get(minutes)) {
                maxGeode = Math.max(maxGeode, state.rssAmount.get(RssType.GEODE));
            }
            int result = maxGeode * multiplier;
            System.out.println(String.format("quality == %s", result));
            return result;
        }

        private void removeUnnecessaryStates(int minute) {
            Set<State> statesToRemove = new HashSet<>();
            for (State state : statesByMinute.get(minute)) {
                for (RssType rssType : RssType.values()) {
                    // remove states that have less rss
                    for (int j = -3; j < 0; j++) {
                        State newState = State.copyOf(state);
                        newState.addRss(rssType, j);
                        if (statesByMinute.get(minute).contains(newState)) {
                            statesToRemove.add(newState);
                        }
                    }
                    // remove states that have less robots
                    for (int j = -2; j < 0; j++) {
                        State newState = State.copyOf(state);
                        int prevAmt = newState.robotAmount.get(rssType);
                        if (prevAmt > 0) {
                            newState.robotAmount.put(rssType, prevAmt + j);
                            if (statesByMinute.get(minute).contains(newState)) {
                                statesToRemove.add(newState);
                            }
                        }
                    }
                }
            }
            if (statesToRemove.size() > 0) {
                statesByMinute.get(minute).removeAll(statesToRemove);
            }
        }

    }

    static class Blueprint {
        int id;
        public EnumMap<RssType, Robot> robotTypes = new EnumMap<>(RssType.class);
    }

    static class State {
        public EnumMap<RssType, Integer> rssAmount = new EnumMap<>(RssType.class);
        public EnumMap<RssType, Integer> robotAmount = new EnumMap<>(RssType.class);

        public State() {
            for (RssType rssType : RssType.values()) {
                rssAmount.put(rssType, 0);
                robotAmount.put(rssType, 0);
            }
        }

        public static State copyOf(State state) {
            State newState = new State();
            for (RssType rssType : RssType.values()) {
                newState.rssAmount.put(rssType, state.rssAmount.get(rssType));
                newState.robotAmount.put(rssType, state.robotAmount.get(rssType));
            }
            return newState;
        }

        public void addFreeRobot(RssType collectType) {
            int prevAmt = robotAmount.get(collectType);
            robotAmount.put(collectType, prevAmt + 1);
        }

        public void addRss(RssType rssType, int amount) {
            int prevAmt = rssAmount.get(rssType);
            rssAmount.put(rssType, prevAmt + amount);
        }

        public boolean isEnoughRssForRobot(Robot robot) {
            for (RssType rssType : robot.price.keySet()) {
                if (rssAmount.get(rssType) < robot.price.get(rssType)) {
                    return false;
                }
            }
            return true;
        }

        public void buildRobot(Robot robot) {
            addFreeRobot(robot.collectType);
            for (Map.Entry<RssType, Integer> entry : robot.price.entrySet()) {
                if (entry.getValue() != 0) {
                    addRss(entry.getKey(), -entry.getValue());
                }
            }
        }

        public void collectResources() {
            for (RssType rssType : RssType.values()) {
                int robots = robotAmount.get(rssType);
                addRss(rssType, robots);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return rssAmount.equals(state.rssAmount) && robotAmount.equals(state.robotAmount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rssAmount, robotAmount);
        }
    }

    static class Robot {
        public RssType collectType;
        public EnumMap<RssType, Integer> price = new EnumMap<>(RssType.class);

        public Robot(RssType collectType) {
            this.collectType = collectType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Robot robot = (Robot) o;
            return collectType == robot.collectType && price.equals(robot.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(collectType, price);
        }
    }

    enum RssType {
        ORE, CLAY, OBSIDIAN, GEODE;

        public static RssType fromString(String s) {
            return RssType.valueOf(s.toUpperCase());
        }
    }

    public static void main(String[] args) {
        new Day19().solve();
    }
}
