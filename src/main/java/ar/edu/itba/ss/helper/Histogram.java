package ar.edu.itba.ss.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by scamisay on 30/03/18.
 */
public class Histogram<X,Y extends Number> {

    private Map<X,Y> values;
    private double sumOfValues;
    private double bucketWidth;

    public Histogram(Map<X, Y> values) {
        this.values = values;
        for(Number number : values.values()){
            sumOfValues += number.doubleValue();
        }
    }

    public Histogram(Map<X, Y> values, double bucketWidth){
        this.values = values;
        for(Number number : values.values()){
            sumOfValues += number.doubleValue();
        }
        this.bucketWidth = bucketWidth;
    }

    public List<X> rangeList() {
        return values.keySet().stream().sorted().collect(Collectors.toList());
    }

    public Y getValue(X range) {
        return values.get(range);
    }

    public double getProbability(X range){
        return getValue(range).doubleValue()/sumOfValues;
    }

    public double getProbabilityDistributed(X range){
        return getValue(range).doubleValue()/(sumOfValues*bucketWidth);
    }

}
