package ar.edu.itba.ss.domain;

import java.util.Objects;

/**
 * Es un intetvalo cerra-abierto
 * [,)
 */
public class Range {
    private double lowest;
    private double highest;
    private int id;

    public Range(int id, double lowest, double highest) {
        this.lowest = lowest;
        this.highest = highest;
        this.id=id;
    }


    public int getId() {
        return id;
    }

    public double getLowest() {
        return lowest;
    }

    public double getHighest() {
        return highest;
    }

    public boolean isInRange(double value){
        return lowest <= value && value < highest;
    }

    @Override
    public String toString() {
        return String.format("[%f,%f)",lowest, highest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return id == range.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}