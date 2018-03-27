package com.example.intervalprogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The subclass implements the method {@code processIntervals} that uses direct interaction
 * between each include and each exclude.
 * The method is not so fast but efficiency does not drop in case of wide intervals
 */
public class IntervalHandlerByDirectSearch extends IntervalHandler {

    @Override
    public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
        List<Interval> includesOrdered = orderIntervals(includes);
        List<Interval> excludesOrdered = orderIntervals(excludes);

        return complementIntervals(includesOrdered, excludesOrdered);
    }

    /**
     * Merges and orders given intervals
     *
     * @param intervals list of non ordered intervals
     * @return list of merged (non overlapped) ordered intervals
     */
    private List<Interval> orderIntervals(List<Interval> intervals) {
        if (intervals.size() < 2) {
            return intervals;
        }
        // be sure a next start is greater than previous one
        List<Interval> intervalsSorted = intervals.stream()
                .sorted(Comparator.comparingInt(Interval::getStart))
                .collect(Collectors.toList());

        // merge intervals
        Interval previous = intervalsSorted.get(0);
        List<Interval> ordered = new ArrayList<>();

        for (int i = 1; i < intervalsSorted.size(); i++) {
            Interval current = intervalsSorted.get(i);
            if (current.getStart() <= previous.getEnd()) {
                // overlapped
                previous = new Interval(previous.getStart(), Math.max(previous.getEnd(), current.getEnd()));
            } else {
                // not overlapped
                ordered.add(previous);
                previous = current;
            }
        }
        ordered.add(previous); // last interval

        return ordered;
    }

    /**
     * Performs complement (difference in Set theory) of given includes with given excludes
     * Logic consists in interaction of each exclude with whole list of includes
     *
     * @param includes list of includes
     * @param excludes list of excludes
     * @return list of intervals representing difference between includes and excludes(includes with excluded excludes)
     */
    private List<Interval> complementIntervals(List<Interval> includes, List<Interval> excludes) {
        if (excludes.isEmpty() || includes.isEmpty()) {
            return includes;
        }
        List<Interval> interacted = new ArrayList<>(includes);
        for (Interval exclude : excludes) {
            // interact the same list of includes with each exclude
            interacted = interacted.stream()
                    .flatMap(i -> interactIntervals(i, exclude).stream())
                    .collect(Collectors.toList());
        }
        return orderIntervals(interacted);
    }

    /**
     * Performs complement of given include and exclude
     *
     * @param include include
     * @param exclude exclude
     * @return list of intervals representing include with excluded exclude
     */
    private List<Interval> interactIntervals(Interval include, Interval exclude) {
        List<Interval> result = new ArrayList<>();
        int inStart = include.getStart();
        int inEnd = include.getEnd();
        int exStart = exclude.getStart();
        int exEnd = exclude.getEnd();

        if (inStart > exEnd || exStart > inEnd) { // no overlap
            result.add(include);
        } else if (inStart > exStart && inEnd > exEnd) {
            result.add(new Interval(exEnd + 1, inEnd));
        } else if (exStart > inStart && exEnd > inEnd) {
            result.add(new Interval(inStart, exStart - 1));
        } else if (exStart > inStart && inEnd > exEnd) {
            result.add(new Interval(inStart, exStart - 1));
            result.add(new Interval(exEnd + 1, inEnd));
        }
        return result;
    }
}
