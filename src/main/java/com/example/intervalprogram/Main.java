package com.example.intervalprogram;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Example 1
        runExample(1, "10-100", "20-30");
        // Example 2
        runExample(2, "50-5000", "");
        // Example 3
        runExample(3, "10-100, 200-300", "95-205");
        // Example 4
        runExample(4, "10-100, 200-300, 400-500", "95-205, 410-420");
    }


    private static void runExample(int n, String includesStr, String excludesStr) {
        IntervalHandler handler = new IntervalHandlerByDirectSearch();
        List<Interval> includes = handler.parseIntervals(includesStr);
        List<Interval> excludes = handler.parseIntervals(excludesStr);
        List<Interval> intervals = handler.processIntervals(includes, excludes);
        String output = handler.formatOutput(intervals);
        System.out.println("Example " + n + ":");
        System.out.println("Includes:" + includes);
        System.out.println("Excludes:" + excludes);
        System.out.println("Result: " + output);
        System.out.println();
    }
}
