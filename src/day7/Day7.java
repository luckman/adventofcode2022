package day7;

import tools.InputReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day7 {
    public static void main(String[] args) {
        List<String> lines = InputReader.readAllLines();
        Node current = new Node("/", 0, true, null);
        Node root = current;
        for (String line : lines) {
            if (line.startsWith("$ cd")) {
                String name = line.split("cd ")[1];
                if ("..".equals(name)) {
                    current = current.parent;
                } else if (!"/".equals(name)){
                    Node node = new Node(name, 0, true, current);
                    current.children.add(node);
                    current = node;
                }
            } else if (!line.startsWith("$")) {
                boolean isDir = false;
                int size = 0;
                if (line.startsWith("dir")) {
                    isDir = true;
                } else {
                    size = Integer.parseInt(line.split(" ")[0]);
                }
                String name = line.split(" ")[1];
                Node child = new Node(name, size, isDir, current);
                if (!child.isDir) {
                    current.children.add(child);
                }
            }
        }
        calcSize(root);

//        calcSizeSum(root);
//        System.out.println(calcSizeSum);

        calcDirectorySizes(root);
        Collections.sort(directorySizes);
        Long unusedSpace = 70000000 - root.size;
        Long needToFree = 30000000 - unusedSpace;
        Long ans = 0L;
        for (int i = 0; i < directorySizes.size(); i++) {
            if (directorySizes.get(i) >= needToFree) {
                ans = directorySizes.get(i);
                break;
            }
        }
        System.out.println(ans);
    }

    public static long calcSize(Node node) {
        if (node.size == 0) {
            long sum = 0L;
            for (Node child : node.children) {
                sum += calcSize(child);
            }
            node.size = sum;
        }
        return node.size;
    }

    private static long calcSizeSum = 0;
    public static void calcSizeSum(Node node) {
        if (node.isDir && node.size <= 100000) {
            calcSizeSum += node.size;
        }
        for (Node child : node.children) {
            calcSizeSum(child);
        }
    }

    private static List<Long> directorySizes = new ArrayList<>();
    public static void calcDirectorySizes(Node node) {
        if (node.isDir) {
            directorySizes.add(node.size);
        }
        for (Node child : node.children) {
            calcDirectorySizes(child);
        }
    }

    private static class Node {
        String name;
        long size;
        boolean isDir;

        List<Node> children = new ArrayList<>();
        Node parent = null;

        public Node(String name, long size, boolean isDir, Node parent) {
            this.name = name;
            this.size = size;
            this.isDir = isDir;
            this.parent = parent;
        }
    }
}
