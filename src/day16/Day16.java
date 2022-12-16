package day16;

import tools.InputReader;

import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
    private static boolean SECOND_TASK = true;
    List<Node> initialGraph = new ArrayList<>();
    Map<String, Integer> initialNodeIdxByName = new HashMap<>();
    int[][] dist;

    public void solve() {
        readInput();
        if (!SECOND_TASK) {
            int ans = calcMaxReleasedPressure();
            System.out.println(ans);
        } else {
            long ans = calcMaxReleasedPressure2();
            System.out.println(ans);
        }
    }

    public int calcMaxReleasedPressure2() {
        calcDistMatrix();
        List<Node> allNonZeroNodes = calcNonZeroNodes();
        int pow2 = 1;
        for (int i = 0; i < allNonZeroNodes.size(); i++) {
            pow2 *= 2;
        }
        System.out.println("pow2 == " + pow2);
        int result = -1;
        for (int mask = 1; mask < pow2; mask++) {
            if (mask % 1000 == 0) {
                System.out.println("currentMask == " + mask);
            }
            List<Node> nodes1 = new ArrayList<>();
            List<Node> nodes2 = new ArrayList<>();
            for (int j = 0; j < allNonZeroNodes.size(); j++) {
                if ((mask & (1 << j)) > 0) {
                    nodes1.add(allNonZeroNodes.get(j));
                } else {
                    nodes2.add(allNonZeroNodes.get(j));
                }
            }
            int released1 = new MaxReleasedPressureCalculator(nodes1, 26).calc();
            int released2 = new MaxReleasedPressureCalculator(nodes2, 26).calc();
            result = Math.max(result, released1 + released2);
        }
        return result;
    }

    public int calcMaxReleasedPressure() {
        calcDistMatrix();
        List<Node> nodesToCalculate = calcNonZeroNodes();
        return new MaxReleasedPressureCalculator(nodesToCalculate, 30).calc();
    }


    class MaxReleasedPressureCalculator {
        private int result = -1;
        private List<Node> nodes;
        private boolean[] opened;
        private int initialMinutes;

        MaxReleasedPressureCalculator(List<Node> nodes, int initialMinutes) {
            this.nodes = nodes;
            this.initialMinutes = initialMinutes;
            opened = new boolean[nodes.size()];
        }

        public int calc() {
            for (int i = 0; i < nodes.size(); i++) {
                Arrays.fill(opened, false);
                int minutesLeft = initialMinutes - dist[initialNodeIdxByName.get("AA")][initialNodeIdxByName.get(nodes.get(i).name)];
                rec(i, minutesLeft, 0);
            }
            return result;
        }

        void rec(int nzNodeIdx, int minutesLeft, int currentReleasedPressure) {
            if (minutesLeft <= 0) {
                return;
            }
            Node node = nodes.get(nzNodeIdx);
            opened[nzNodeIdx] = true;
            minutesLeft--;
            int nodeReleasedPressure = minutesLeft * node.flow;
            this.result = Math.max(this.result, currentReleasedPressure + nodeReleasedPressure);
            for (int j = 0; j < nodes.size(); j++) {
                if (nzNodeIdx != j && !opened[j]) {
                    int deltaMinutes = dist[initialNodeIdxByName.get(node.name)][initialNodeIdxByName.get(nodes.get(j).name)];
                    rec(j, minutesLeft - deltaMinutes, currentReleasedPressure + nodeReleasedPressure);
                }
            }
            opened[nzNodeIdx] = false;
        }

    }

    private List<Node> calcNonZeroNodes() {
        return initialGraph.stream()
                .filter(node -> node.flow > 0)
                .collect(Collectors.toList());
    }

    private void calcDistMatrix() {
        dist = new int[initialGraph.size()][initialGraph.size()];
        for (int i = 0; i < initialGraph.size(); i++) {
            Node node1 = initialGraph.get(i);
            for (int j = 0; j < initialGraph.size(); j++) {
                Node node2 = initialGraph.get(j);
                if (node1.neighbours.contains(node2)) {
                    dist[i][j] = 1;
                } else {
                    dist[i][j] = Integer.MAX_VALUE / 2;
                }
            }
        }

        for (int k = 0; k < initialGraph.size(); k++) {
            for (int i = 0; i < initialGraph.size(); i++) {
                for (int j = 0; j < initialGraph.size(); j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
    }


    public void readInput() {
        List<String> lines = InputReader.readAllLines().stream().filter(l -> l.length() > 5).toList();
        int idx = 0;
        for (String line : lines) {
            String valveName = line.split("Valve ")[1].split(" has")[0];
            int flowValue = Integer.parseInt(line.split("rate=")[1].split(";")[0]);
            Node node = new Node(valveName, flowValue);
            initialGraph.add(node);
            initialNodeIdxByName.put(node.name, idx);
            idx++;
        }
        for (String line : lines) {
            String valveName = line.split("Valve ")[1].split(" has")[0];
            Node node = initialGraph.get(initialNodeIdxByName.get(valveName));
            String[] neighbourNames;
            if (line.contains("to valves ")) {
                neighbourNames = line.split("to valves ")[1].split(", ");
            } else {
                neighbourNames = line.split("to valve ")[1].split(", ");
            }
            for (String neighbourStr : neighbourNames) {
                Node neighbour = initialGraph.get(initialNodeIdxByName.get(neighbourStr));
                node.addNeighbour(neighbour);
            }
        }
    }

    static class Node {
        public String name;
        public int flow;
        public Set<Node> neighbours = new HashSet<>();
        public Node(String name, int flow) {
            this.name = name;
            this.flow = flow;
        }
        public void addNeighbour(Node neighbour) {
            this.neighbours.add(neighbour);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public static void main(String[] args) {
        new Day16().solve();
    }
}
