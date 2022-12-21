package day21;

import tools.InputReader;

import java.util.*;

public class Day21 {
    private static final boolean SECOND_TASK = true;
    private Map<String, Node> nodeByName = new HashMap<>();

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
        Node root = nodeByName.get("root");
        return dfs(root);
    }

    public long solvePart2() {
        Node humn = nodeByName.get("humn");
        Node root = nodeByName.get("root");
        root.value = 0L;
        root.operation = Operation.EQUALITY;
        humn.value = null;
        return dfs2(humn);
    }

    public long dfs(Node node) {
        if (node.value == null) {
            long firstValue = dfs(node.first);
            long secondValue = dfs(node.second);
            node.value = node.operation.apply(firstValue, secondValue);
        }
        return node.value;
    }

    private Set<Node> dfs2Tree = new HashSet<>();

    public Long dfs2(Node node) {
        dfs2Tree.add(node);
        if (node.value == null) {
            Node parent = node.parent;
            if (node.first != null && node.second != null && !dfs2Tree.contains(node.first) && !dfs2Tree.contains(node.second)) {
                long firstValue = dfs2(node.first);
                long secondValue = dfs2(node.second);
                node.value = node.operation.apply(firstValue, secondValue);
            } else {    // value == null, and has no children that can be calculated
                if (parent == null) {
                    throw new RuntimeException("Parent is null!");
                }
                if (node == parent.first) {
                    node.value = parent.operation.findFirst(dfs2(parent), dfs2(parent.second));
                } else {
                    node.value = parent.operation.findSecond(dfs2(parent), dfs2(parent.first));
                }
            }
        }
        dfs2Tree.remove(node);
        return node.value;
    }

    static class Node {
        public String name;
        public Long value = null;
        public Node first;
        public Node second;
        public Node parent;
        public Operation operation = Operation.UNKNOWN;

        public Node(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s; f: %s, s: %s, op: %s]", name, value,
                    first != null ? first.name : "no",
                    second != null ? second.name : "no",
                    operation);
        }
    }

    enum Operation {
        UNKNOWN,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        EQUALITY;

        public static Operation fromString(String s) {
            if ("+".equals(s)) {
                return Operation.PLUS;
            } else if ("-".equals(s)) {
                return Operation.MINUS;
            } else if ("*".equals(s)) {
                return Operation.MULTIPLY;
            } else if ("/".equals(s)) {
                return Operation.DIVIDE;
            } else {
                return UNKNOWN;
            }
        }

        public long apply(long first, long second) {
            switch (this) {
                case PLUS -> { return first + second; }
                case MINUS -> { return first - second; }
                case MULTIPLY -> { return first * second; }
                case DIVIDE -> { return first / second; }
                default -> { throw new RuntimeException(); }
            }
        }

        public long findFirst(long parent, long second) {
            switch (this) {
                case PLUS -> { return parent - second; }
                case MINUS -> { return parent + second; }
                case MULTIPLY -> { return parent / second; }
                case DIVIDE -> { return parent * second; }
                case EQUALITY -> { return second; }
                default -> throw new RuntimeException();
            }
        }

        public long findSecond(long parent, long first) {
            switch (this) {
                case PLUS -> { return parent - first; }
                case MINUS -> { return first - parent; }
                case MULTIPLY -> { return parent / first; }
                case DIVIDE -> { return first / parent; }
                case EQUALITY -> { return first; }
                default -> throw new RuntimeException();
            }
        }
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        for (String line : lines) {
            String nodeName = line.split(":")[0];
            Node node = nodeByName.getOrDefault(nodeName, new Node(nodeName));
            if (line.length() > 10) {
                String firstChildStr = line.split(": ")[1].split(" ")[0];
                String secondChildStr = line.split(": ")[1].split(" ")[2];
                Node first = nodeByName.getOrDefault(firstChildStr, new Node(firstChildStr));
                first.parent = node;
                Node second = nodeByName.getOrDefault(secondChildStr, new Node(secondChildStr));
                second.parent = node;
                node.first = first;
                node.second = second;
                node.operation = Operation.fromString(line.split(" ")[2]);
                node.value = null;
                nodeByName.putIfAbsent(firstChildStr, first);
                nodeByName.putIfAbsent(secondChildStr, second);
            } else {
                node.value = Long.parseLong(line.split(": ")[1]);
            }
            nodeByName.putIfAbsent(nodeName, node);
        }
    }

    public static void main(String[] args) {
        new Day21().solve();
    }
}
