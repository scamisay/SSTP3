package ar.edu.itba.ss.helper;

public class Range implements Comparable<Range>{
    private double min;
    private double max;

    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return String.format("[%.3f,%.3f)",min,max);
    }

    public boolean isIn(double time) {
        return time >= min && time < max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (Double.compare(range.min, min) != 0) return false;
        return Double.compare(range.max, max) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(min);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(Range o) {
        return new Double(min).compareTo(o.getMin());
    }
}