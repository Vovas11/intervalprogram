package com.example.intervalprogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides interface to accept input intervals in different formats, processes the main logic, forms the output.
 * The method implementing the main logic is overridden in subtypes. The methods provide the same result
 * but are implemented by different ways
 */
public abstract class IntervalHandler {

    /**
     * Main method processing input intervals and handling their interaction.
     * Normalizes (excludes overlapping and sorts) the given lists
     * Returns a list of includes with removed excludes
     *
     * @param includes list of includes
     * @param excludes list of excludes
     *
     * @return resulting list after deducting excludes from includes
     */
    public abstract List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes);


    /**
     * Converts a string like "10-19, 31-100" into list of intervals
     *
     * @param intervals intervals of integers in string view, may be empty
     * @return list of intervals or empty list
     */
    public List<Interval> parseIntervals(String intervals) {
        if (intervals.isEmpty()) {
            return Collections.emptyList();
        }
        List<Interval> result = new ArrayList<>();
        String[] initArray = intervals.split(",\\s*");
        for (String str : initArray) {
            if (str.matches("^\\d+-\\d+$")) {
                String[] interval = str.split("-");
                result.add(new Interval(Integer.parseInt(interval[0]), Integer.parseInt(interval[1])));
            }
        }
        return result;
    }

    /**
     * Forms a handy string from the given list of intervals
     *
     * @param intervals list of intervals
     * @return a formatted string containing intervals
     */
    public String formatOutput(List<Interval> intervals) {
        return intervals.stream()
                .map(Interval::toString)
                .collect(Collectors.joining(", "));
    }
}
