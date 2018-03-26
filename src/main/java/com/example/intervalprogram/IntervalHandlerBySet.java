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
        TreeSet<Integer> includesNormalized = includes.stream()
                .flatMap(i -> IntStream.rangeClosed(i.getStart(), i.getEnd()).boxed())
                .collect(Collectors.toCollection(TreeSet::new));

        // simplify excludes
        TreeSet<Integer> excludesNormalized = excludes.stream()
                .flatMap(i -> IntStream.rangeClosed(i.getStart(), i.getEnd()).boxed())
                .collect(Collectors.toCollection(TreeSet::new));

        // interact includes and excludes
        includesNormalized.removeAll(excludesNormalized);

        return getIntervals(includesNormalized);
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

        int lowBound = filledIntervals.get(0);
        int highBound;
        for (int i = 1; i < filledIntervals.size() - 1; i++) {
            int current = filledIntervals.get(i);
            int previous = filledIntervals.get(i - 1);
            int next = filledIntervals.get(i + 1);
            // get low bound of an interval
            if (current - 1 != previous) {
                lowBound = current;
            }
            // get high bound of an interval
            if (current + 1 != next) {
                highBound = current;
                result.add(new Interval(lowBound, highBound));
            }
        }
        result.add(new Interval(lowBound, filledIntervals.get(filledIntervals.size() - 1)));

        return result;
    }
}
