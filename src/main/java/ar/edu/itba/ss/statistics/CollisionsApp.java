package ar.edu.itba.ss.statistics;

import ar.edu.itba.ss.domain.CollisionSystem;
import ar.edu.itba.ss.helper.HistError;
import ar.edu.itba.ss.helper.Histogram;
import ar.edu.itba.ss.helper.HistogramWithErrors;
import ar.edu.itba.ss.helper.Range;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by scamisay on 30/03/18.
 */
public class CollisionsApp {

    static final int SAMPLES = 10;

    public static void main(String[] args){
        int time = 30;
        int n = 50;
        double dt2 = .02;

        boolean countCollisions = true;

        Map<Range, List<Integer>> histCollector = new HashMap<>();
        for(int i=0; i< SAMPLES; i++){
            CollisionSystem pc = CollisionSystem.getInstance();
            pc.init(time,n,dt2,new RandomDataGenerator(new JDKRandomGenerator(i)), countCollisions);
            pc.calculate(null);
            Histogram<Range,Integer> hist = pc.buildCollisionHistogram();
            for(Range range: hist.rangeList()){
                Integer value = hist.getValue(range);
                if(!histCollector.keySet().contains(range)){
                    histCollector.put(range, new ArrayList<>());
                }
                histCollector.get(range).add(value);
            }
        }

        //esto solo vale si todos los histogramas son generados con los mismos parametros
        Map<Range, HistError<Integer>> histWithErrors = new HashMap<>();
        for(Range range : histCollector.keySet()){
            Integer min = histCollector.get(range).stream().mapToInt(Integer::intValue).min().getAsInt();
            Integer max = histCollector.get(range).stream().mapToInt(Integer::intValue).max().getAsInt();
            Double average = histCollector.get(range).stream().mapToInt(Integer::intValue).average().getAsDouble();
            histWithErrors.put(range, new HistError<Integer>(min, average, max));
        }
        HistogramWithErrors<Range, Integer> histogramWithErrors = new HistogramWithErrors<>(histWithErrors);
        String rangesJson = histogramWithErrors.rangeList().stream().map(a ->"\""+a.toString()+"\"").collect(Collectors.joining(", "));
        String heights = histogramWithErrors.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String errors =  histogramWithErrors.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));
        System.out.print(rangesJson);
    }

}
