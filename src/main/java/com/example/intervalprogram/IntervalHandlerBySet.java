package com.example.intervalprogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The subclass implements the method {@code processIntervals} using natural behavior of Set
 * that can not store identical numbers. In addition TreeSet provides an ordered (sorted) sequence.
 * Excluding intervals from others is executed by a Collection's method.
 * The method is fast and simple but can have high memory costs
 */
public class IntervalHandlerBySet extends IntervalHandler {

    @Override
    public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
        // simplify includes
        TreeSet<Integer> includesNormalized = getIntegers(includes);

        // simplify excludes
        TreeSet<Integer> excludesNormalized = getIntegers(excludes);

        // interact includes and excludes
        includesNormalized.removeAll(excludesNormalized);

        return getIntervals(includesNormalized);
    }

    /**
     * Fills a TreeSet with ranges of integers corresponding to given intervals
     *
     * @param includes list of intervals
     * @return ordered Set with integers incoming into intervals
     */
    private TreeSet<Integer> getIntegers(List<Interval> includes) {
        return includes.stream()
                    .flatMap(i -> IntStream.rangeClosed(i.getStart(), i.getEnd()).boxed())
                    .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Converts a given sorted set of integers into list of intervals
     *
     * @param sortedSet sorted set of integers
     * @return list of intervals
     */
    private List<Interval> getIntervals(SortedSet<Integer> sortedSet) {
        if (sortedSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> filledIntervals = new ArrayList<>(sortedSet);
        List<Interval> result = new ArrayList<>();

        int lowBound = 0;

        for (int i = 0; i < filledIntervals.size(); i++) {
            Integer current = filledIntervals.get(i);
            int type = getTypeAdjacent(filledIntervals, i);
            switch (type) {
                case 0:
                    result.add(new Interval(current, current));
                    break;
                case 2:
                    lowBound = current;
                    break;
                case 3:
                    result.add(new Interval(lowBound, current));
                    break;
            }
        }
        return result;
    }

    private int getTypeAdjacent(List<Integer> integers, int index) {
        int value = integers.get(index);
        if (index == 0) {
            return integers.get(index + 1) == value + 1 ? 2 : 0;
        } else if (index == integers.size() - 1) {
            return integers.get(index - 1) == value - 1 ? 3 : 0;
        } else {
            int prev = integers.get(index - 1);
            int next = integers.get(index + 1);
            if (prev == value - 1 && next == value + 1) {
                return 1;
            } else if (next == value + 1) {
                return 2;
            } else if (prev == value - 1) {
                return 3;
            } else {
                return 0;
            }
        }
    }

}
