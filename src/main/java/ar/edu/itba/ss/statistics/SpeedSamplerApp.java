package ar.edu.itba.ss.statistics;

import ar.edu.itba.ss.domain.CollisionSystem;
import ar.edu.itba.ss.helper.HistError;
import ar.edu.itba.ss.helper.Histogram;
import ar.edu.itba.ss.helper.HistogramWithErrors;
import ar.edu.itba.ss.helper.Range;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ar.edu.itba.ss.domain.CollisionSystem.MAX_SPEED;

/**
 * Created by scamisay on 31/03/18.
 */
public class SpeedSamplerApp {

    static final int SAMPLES = 20;

    public static void main(String[] args) {
        int time = 30;
        int n = 100;
        double dt2 = .02;
        int bucketQuantity = 51;

        double first = time*0.;
        double second = time*2/3.;
        List<Double> snapshots = Arrays.asList(first,second);

        Map<Range, List<Double>> histProbCollectorForFirst = new HashMap<>();
        Map<Range, List<Double>> histProbCollectorForSecond = new HashMap<>();
        for(int i=0; i< SAMPLES; i++){
            CollisionSystem pc = CollisionSystem.getInstance();
            pc.init(time,n,dt2,new RandomDataGenerator(new JDKRandomGenerator(i)));
            pc.setSpeedSnapshots(snapshots);//fijo los instantes donde me interesa saber las rapideces
            pc.calculate(null);

            Histogram<Range,Integer> histForFirst = buildSpeedHistogram(bucketQuantity, first, pc.getSpeedsByTime());
            for(Range range: histForFirst.rangeList()){
                double prob = histForFirst.getProbabilityDistributed(range);
                if(!histProbCollectorForFirst.keySet().contains(range)){
                    histProbCollectorForFirst.put(range, new ArrayList<>());
                }
                histProbCollectorForFirst.get(range).add(prob);
            }

            Histogram<Range,Integer> histForSecond = buildSpeedHistogram(bucketQuantity, second, pc.getSpeedsByTime());
            for(Range range: histForSecond.rangeList()){
                double prob = histForSecond.getProbabilityDistributed(range);
                if(!histProbCollectorForSecond.keySet().contains(range)){
                    histProbCollectorForSecond.put(range, new ArrayList<>());
                }
                histProbCollectorForSecond.get(range).add(prob);
            }
        }

        Map<Range, HistError<Double>> histsProbsWithErrorsForFirst = new HashMap<>();
        for(Range range : histProbCollectorForFirst.keySet()){
            double minProb = histProbCollectorForFirst.get(range).stream().mapToDouble(Double::doubleValue).min().getAsDouble();
            double maxProb = histProbCollectorForFirst.get(range).stream().mapToDouble(Double::doubleValue).max().getAsDouble();
            double averageProb = histProbCollectorForFirst.get(range).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            histsProbsWithErrorsForFirst.put(range, new HistError<>(minProb, averageProb, maxProb));
        }
        HistogramWithErrors<Range, Double> histogramProbWithErrorsForFirst = new HistogramWithErrors<>(histsProbsWithErrorsForFirst);

        Map<Range, HistError<Double>> histsProbsWithErrorsForSecond = new HashMap<>();
        for(Range range : histProbCollectorForSecond.keySet()){
            double minProb = histProbCollectorForSecond.get(range).stream().mapToDouble(Double::doubleValue).min().getAsDouble();
            double maxProb = histProbCollectorForSecond.get(range).stream().mapToDouble(Double::doubleValue).max().getAsDouble();
            double averageProb = histProbCollectorForSecond.get(range).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            histsProbsWithErrorsForSecond.put(range, new HistError<>(minProb, averageProb, maxProb));
        }
        HistogramWithErrors<Range, Double> histogramProbWithErrorsForSecond = new HistogramWithErrors<>(histsProbsWithErrorsForSecond);


        String probabilitiesForFirst = histogramProbWithErrorsForFirst.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String probabilitiesErrorsForFirst =  histogramProbWithErrorsForFirst.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));

        String probabilitiesForSecond = histogramProbWithErrorsForSecond.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String probabilitiesErrorsForSecond =  histogramProbWithErrorsForSecond.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));

        String rangesJson = histogramProbWithErrorsForFirst.rangeList().stream().map(a ->"\""+a.toString()+"\"").collect(Collectors.joining(", "));

        System.out.print(rangesJson);
    }

    public static Histogram<Range, Integer> buildSpeedHistogram(int bucket_quantity, double time, Map<Double, List<Double>> sampling) {
        double globalMin = 0;
        double globalMax = 2*MAX_SPEED;
        double step = (globalMax - globalMin) / bucket_quantity;

        List<Range> ranges = IntStream.range(0, bucket_quantity).boxed()
                .map(i -> new Range(globalMin+i*step, globalMin+(i+1)*step))
                .collect(Collectors.toList());

        Map<Range,Integer> histogram = new HashMap<>();
        for(Range range: ranges){
            int amount = 0;

            //cuento speed para el bucket
            for(Double speed : sampling.get(time)){
                if(range.isIn(speed)){
                    amount++;
                }
            }
            histogram.put(range, amount);
        }

        return new Histogram(histogram,step);
    }

    private static double findMaxSpeedInSampling(Map<Double, List<Double>> sampling) {
        double max = 0;
        for(Double time : sampling.keySet()){
            double localMax = sampling.get(time).stream().mapToDouble(Double::doubleValue).max().getAsDouble();
            if(max < localMax){
                max = localMax;
            }
        }
        double delta = 0.001;//para que no quede ni un valor fuera del bucket
        return max + delta;
    }
}
