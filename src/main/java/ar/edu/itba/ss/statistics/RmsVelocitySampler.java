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
 * Created by scamisay on 31/03/18.
 */
public class RmsVelocitySampler {

    static final int SAMPLES = 10;

    public static void main(String[] args){
        int time = 30;
        int n = 100;
        double dt2 = .02;
        int bucketQuantity = 9;


        Map<Range, List<Double>> histCollector = new HashMap<>();
        Map<Range, List<Double>> histProbCollector = new HashMap<>();
        for(int i=0; i< SAMPLES; i++){
            CollisionSystem pc = CollisionSystem.getInstance();
            pc.init(time,n,dt2,new RandomDataGenerator(new JDKRandomGenerator(i)));
            pc.setStartCalculatingRmsFrom(time*2/3);//calculo a partir del ultimo tercio
            pc.calculate(null);

            Histogram<Range,Double> hist = pc.buildRmsVelocityHistogram(bucketQuantity);
            for(Range range: hist.rangeList()){
                Double value = hist.getValue(range);
                if(!histCollector.keySet().contains(range)){
                    histCollector.put(range, new ArrayList<>());
                }
                histCollector.get(range).add(value);

                double prob = hist.getProbability(range);
                if(!histProbCollector.keySet().contains(range)){
                    histProbCollector.put(range, new ArrayList<>());
                }
                histProbCollector.get(range).add(prob);
            }
        }



        //valores para graficar


        System.out.print(1);
    }
}
