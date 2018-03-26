package com.example.intervalprogram;

/**
 * Immutable data holder representing an entity of interval (range)
 * Stores two values: starting number of an interval and ending number
 *
 */
public class Interval {

    private final int start;
    private final int end;

    /**
     * Constructor makes sure that the field {@code start} gets a lower value
     *
     * @param start starting number of the interval
     * @param end ending number of the interval
     */
    public Interval(int start, int end) {
        if (end < start) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }


    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;

        Interval interval = (Interval) o;

        return start == interval.start && end == interval.end;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        return result;
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }
}
