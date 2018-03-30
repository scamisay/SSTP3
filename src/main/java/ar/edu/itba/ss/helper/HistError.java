package ar.edu.itba.ss.helper;

/**
 * Created by scamisay on 30/03/18.
 */
public class HistError<Y>{
    private Y min;
    private double average;
    private Y max;

    public HistError(Y min, double average, Y max) {
        this.min = min;
        this.average = average;
        this.max = max;
    }

    public Y getMin() {
        return min;
    }

    public double getAverage() {
        return average;
    }

    public Y getMax() {
        return max;
    }
}