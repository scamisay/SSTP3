package ar.edu.itba.ss.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by scamisay on 31/03/18.
 */
public class RmsVelocityManager {

    private List<Double> timeList;
    private List<Double> rmsVelocityList;

    public RmsVelocityManager() {
        timeList = new ArrayList<>();
        rmsVelocityList = new ArrayList<>();
    }

    public void add(double time, double rmsVelocity){
        timeList.add(time);
        rmsVelocityList.add(rmsVelocity);
    }

    public List<Double> getTimeList() {
        return timeList;
    }

    public List<Double> getRmsVelocityList() {
        return rmsVelocityList;
    }

    public Histogram<Range, Double> buildHistogram(int bucket_quantity) {
        double globalMin = rmsVelocityList.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        double globalMax = rmsVelocityList.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        double step = (globalMax - globalMin) / bucket_quantity;

        List<Range> ranges = IntStream.range(0, bucket_quantity).boxed()
                .map(i -> new Range(globalMin+i*step, globalMin+(i+1)*step))
                .collect(Collectors.toList());

        Map<Range,Integer> histogram = new HashMap<>();
        for(Range range: ranges){
            int amount = 0;

            //cuento colisiones para el bucket
            for(int index= 0; index< rmsVelocityList.size() ; index++){
                double time = rmsVelocityList.get(index);
                if(range.isIn(time)){
                    amount++;
                }
            }

            histogram.put(range, amount);
        }

        return new Histogram(histogram);
    }
}
