package day13;

import com.fasterxml.jackson.databind.ObjectMapper;
import tools.InputReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day13 {
    public void solve() throws Exception {
        List<String> lines = InputReader.readAllLines();
        List leftValues = new ArrayList<>();
        List rightValues = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < lines.size(); i++) {
            if (i % 3 == 2) {
                continue;
            }
            List serialized = mapper.readValue(lines.get(i), List.class);
            if (i % 3 == 0) {
                leftValues.add(serialized);
            } else {
                rightValues.add(serialized);
            }
        }
//        List<Integer> pairsInRightOrder = findPairsInRightOrder(leftValues, rightValues);
//        int ans = 0;
//        for (int idx : pairsInRightOrder) {
//            ans += (idx + 1);
//        }
//        System.out.println(ans);
        List allValues = new ArrayList(leftValues);
        allValues.addAll(rightValues);
        int ans = calcDecoderKey(allValues);
        System.out.println(ans);
    }

    public List<Integer> findPairsInRightOrder(List leftValues, List rightValues) {
        ValueComparator valueComparator = new ValueComparator();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < leftValues.size(); i++) {
            if (valueComparator.compare(leftValues.get(i), rightValues.get(i)) < 0) {
                result.add(i);
            }
        }
        return result;
    }

    public Integer calcDecoderKey(List values) {
        List firstDecoder = List.of(List.of(2));
        List secondDecoder = List.of(List.of(6));
        List valuesWithDecoder = new ArrayList(values);
        valuesWithDecoder.add(firstDecoder);
        valuesWithDecoder.add(secondDecoder);
        valuesWithDecoder.sort(new ValueComparator());
        int idx1 = valuesWithDecoder.indexOf(firstDecoder) + 1;
        int idx2 = valuesWithDecoder.indexOf(secondDecoder) + 1;
        return idx1 * idx2;
    }

    public static class ValueComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return Integer.compare((int)o1, (int)o2);
            } else if (o1 instanceof List && o2 instanceof List) {
                List l1 = (List)o1;
                List l2 = (List)o2;
                int minLen = Math.min(l1.size(), l2.size());
                for (int i = 0; i < minLen; i++) {
                    int compareElement = compare(l1.get(i), l2.get(i));
                    if (compareElement != 0) {
                        return compareElement;
                    }
                }
                // lists until minLen are equal
                return Integer.compare(l1.size(), l2.size());
            } else if (o1 instanceof List && o2 instanceof Integer) {
                return compare(o1, List.of(o2));
            } else if (o1 instanceof Integer && o2 instanceof List) {
                return compare(List.of(o1), o2);
            }
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws Exception {
        new Day13().solve();
    }
}
