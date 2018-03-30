package ar.edu.itba.ss.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by scamisay on 30/03/18.
 */
public class Histogram<X,Y> {

    private Map<X,Y> values;

    public Histogram(Map<X, Y> values) {
        this.values = values;
    }

    public List<X> rangeList() {
        return values.keySet().stream().sorted().collect(Collectors.toList());
    }

    public Y getValue(X range) {
        return values.get(range);
    }
}
