package com.example.intervalprogram;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class IntervalTest {


    private static final Interval intervalCorrect = new Interval(2, 100);
    private static final Interval intervalInCorrect = new Interval(100, 2);

    @Test
    public void testConstructorLogic() {
        assertEquals(intervalCorrect.getStart(), intervalInCorrect.getStart());
        assertEquals(intervalCorrect.getEnd(), intervalInCorrect.getEnd());
    }

    @Test
    public void testEqualsNotInstances() {
        assertFalse(intervalCorrect == intervalInCorrect);
    }

    @Test
    public void testEqualsMethodEquals() {
        assertTrue(intervalCorrect.equals(intervalInCorrect));
    }

    @Test
    public void testEqualsMethodHashCode() {
        assertEquals(intervalCorrect.hashCode(), intervalInCorrect.hashCode());
    }


}
