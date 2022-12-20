package day20;

import tools.InputReader;

import java.util.*;

public class Day20 {
    private static final boolean SECOND_TASK = true;
    List<Element> sequence = new ArrayList<>();

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
        List<Element> movingSequence = new ArrayList<>(sequence);
        Element zeroElement = null;
        for (Element element : sequence) {
            int movedIdx = movingSequence.indexOf(element);
            proceedMove(movingSequence, movedIdx);
            if (element.value == 0) {
                zeroElement = element;
            }
        }
        int zeroIdx = movingSequence.indexOf(zeroElement);
        int idx1 = (zeroIdx + 1000) % movingSequence.size();
        int idx2 = (zeroIdx + 2000) % movingSequence.size();
        int idx3 = (zeroIdx + 3000) % movingSequence.size();
        System.out.println("Zero idx == " + zeroIdx);

        return movingSequence.get(idx1).value + movingSequence.get(idx2).value + movingSequence.get(idx3).value;
    }

    public long solvePart2() {
        final long DECRIPTION_KEY = 811589153L;
        List<Element> decriptedSequence = new ArrayList<>();
        for (Element element : sequence) {
            decriptedSequence.add(new Element(element.initialIndex, element.value * DECRIPTION_KEY));
        }
        List<Element> movingSequence = new ArrayList<>(decriptedSequence);
        Element zeroElement = null;
        for (int t = 0; t < 10; t++) {
            for (Element element : decriptedSequence) {
                int movedIdx = movingSequence.indexOf(element);
                proceedMove(movingSequence, movedIdx);
                if (element.value == 0) {
                    zeroElement = element;
                }
            }
        }
        int zeroIdx = movingSequence.indexOf(zeroElement);
        int idx1 = (zeroIdx + 1000) % movingSequence.size();
        int idx2 = (zeroIdx + 2000) % movingSequence.size();
        int idx3 = (zeroIdx + 3000) % movingSequence.size();

        return movingSequence.get(idx1).value + movingSequence.get(idx2).value + movingSequence.get(idx3).value;
    }

    private void proceedMove(List<Element> elements, int elementIdx) {
        Element element = elements.get(elementIdx);
        int moveRightAmt = mod(element.value, elements.size() - 1);
        if (moveRightAmt > 0) {
            for (int j = elementIdx; j < elementIdx + moveRightAmt; j++) {
                int i1 = mod(j, elements.size());
                int i2 = mod(j + 1, elements.size());
                elements.set(i1, elements.get(i2));
            }
            int i1 = mod(elementIdx + moveRightAmt, elements.size());
            elements.set(i1, element);
        }
    }

    public static int mod(long value, int mod) {
        long multiplier = Math.abs(value / mod);
        value += (multiplier + 2) * mod;
        return (int)(value % mod);
    }

    public void readInput() {
        List<String> lines = InputReader.readAllLines();
        int idx = 0;
        for (String line : lines) {
            sequence.add(new Element(idx, Integer.parseInt(line)));
            idx++;
        }
    }

    static class Element {
        public int initialIndex;
        public long value;
        public Element(int initialIndex, long value) {
            this.initialIndex = initialIndex;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return initialIndex == element.initialIndex && value == element.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(initialIndex, value);
        }

        @Override
        public String toString() {
            return String.format("%s", value);
        }
    }

    public static void main(String[] args) {
        new Day20().solve();
    }
}
