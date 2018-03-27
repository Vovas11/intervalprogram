package com.example.intervalprogram;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntervalHandlerTest {

    private IntervalHandler handler;

    private static final String INTERVAL_NORMAL = "10-19, 31-100";
    private static final String INTERVAL_OVERLAP = "10-19, 5-35";
    private static final String INTERVAL_WRONG_ORDER = "19-10, 31-100";
    private static final String INTERVAL_NEGATIVE = "-10--5, -7-35";
    private static final String INTERVAL_ILLEGAL = "10-19, 7.5-35, 31-100";

    private static final List<Interval> RESULT_NORMAL = Arrays.asList(new Interval(10, 19), new Interval(31, 100));
    private static final List<Interval> RESULT_OVERLAP = Arrays.asList(new Interval(10, 19), new Interval(5, 35));
    private static final List<Interval> RESULT_NEGATIVE = Arrays.asList(new Interval(-10, -5), new Interval(-7, 35));


    @Before
    public void start() {
        handler = new IntervalHandler() {
            @Override
            public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
                return null;
            }
        };
    }


    @Test
    public void testParseIntervalsOk() {
        assertEquals(RESULT_NORMAL, handler.parseIntervals(INTERVAL_NORMAL));
    }

    @Test
    public void testParseIntervalsOverlap() {
        assertEquals(RESULT_OVERLAP, handler.parseIntervals(INTERVAL_OVERLAP));
    }

    @Test
    public void testParseIntervalsWrongStart() {
        assertEquals(RESULT_NORMAL, handler.parseIntervals(INTERVAL_WRONG_ORDER));
    }

    @Test
    public void testParseIntervalsNegative() {
        assertEquals(RESULT_NEGATIVE, handler.parseIntervals(INTERVAL_NEGATIVE));
    }

    @Test
    public void testParseIntervalsIllegal() {
        assertEquals(RESULT_NORMAL, handler.parseIntervals(INTERVAL_ILLEGAL));
    }
}
