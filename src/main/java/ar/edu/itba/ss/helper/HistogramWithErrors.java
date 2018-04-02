package ar.edu.itba.ss.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by scamisay on 30/03/18.
 */
public class HistogramWithErrors<X,Y> {
    private Map<X,HistError<Y>> values;

    public HistogramWithErrors(Map<X, HistError<Y>> values) {
        this.values = values;
    }

    public HistError<Y> getValue(X range) {
        return values.get(range);
    }

    public List<X> rangeList() {
        return values.keySet().stream().sorted().collect(Collectors.toList());
    }

    public List<Double> averageValues(){
        return rangeList().stream().map( k -> values.get(k).getAverage()).collect(Collectors.toList());
    }

    public List<HistError<Y>> errorList(){
        return rangeList().stream().map( k -> values.get(k)).collect(Collectors.toList());
    }


    public Map<X, HistError<Y>> getMap() {
        return values;
    }
}
