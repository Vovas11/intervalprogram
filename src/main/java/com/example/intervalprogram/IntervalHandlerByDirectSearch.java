package com.example.intervalprogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class IntervalHandlerByDirectSearch extends IntervalHandler {

    @Override
    public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
        List<Interval> includesOrdered = orderIntervals(includes);
        List<Interval> excludesOrdered = orderIntervals(excludes);

        return complementIntervals(includesOrdered, excludesOrdered);
    }


    /**
     * Makes conjunction and ordering of given intervals
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


    private List<Interval> interactIntervals(Interval include, Interval exclude) {
        List<Interval> result = new ArrayList<>();
        if (include.getStart() > exclude.getEnd() || exclude.getStart() > include.getEnd()) { // no overlap
            result.add(include);
        } else if (include.getStart() > exclude.getStart() && include.getEnd() > exclude.getEnd()) {
            result.add(new Interval(exclude.getEnd() + 1, include.getEnd()));
        } else if (exclude.getStart() > include.getStart() && exclude.getEnd() > include.getEnd()) {
            result.add(new Interval(include.getStart(), exclude.getStart() - 1));
        } else if (exclude.getStart() > include.getStart() && include.getEnd() > exclude.getEnd()) {
            result.add(new Interval(include.getStart(), exclude.getStart() - 1));
            result.add(new Interval(exclude.getEnd() + 1, include.getEnd()));
        }
        return result;
    }


}
