package ar.edu.itba.ss.statistics;

import ar.edu.itba.ss.domain.CollisionSystem;
import ar.edu.itba.ss.helper.HistogramWithErrors;
import ar.edu.itba.ss.helper.MeanSquaredDisplacementManager;
import ar.edu.itba.ss.helper.Range;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Created by scamisay on 01/04/18.
 */
public class MeanSquaredDisplacementInTime {
    static final int SAMPLES = 10;

    public static void main(String[] args) {
        int time = 30;
        int n = 400;
        double dt2 = .002;

        List<Double> timeList = IntStream.range(1, 11).boxed().map( i -> i*.09*time ).collect(Collectors.toList());

        MeanSquaredDisplacementManager manager = new MeanSquaredDisplacementManager(timeList);
        for (int i = 0; i < SAMPLES; i++) {
            CollisionSystem pc = CollisionSystem.getInstance();
            pc.init(time,n,dt2,new RandomDataGenerator(new JDKRandomGenerator(i)));
            pc.setTrackingByTimeList(timeList);
            pc.calculate(null);
            manager.addValuesForBigBall(pc.getBigBallTracker());
            manager.addValuesForSmallBall(pc.getSmallBallTracker());
        }

        HistogramWithErrors<Double,Double> bigBallH = manager.buildHistogramWithErrorsForBigBall();
        HistogramWithErrors<Double,Double> smallBallH = manager.buildHistogramWithErrorsForSmallBall();

        /***
         * msd graph
         */
        String rangesJson = bigBallH.rangeList().stream().map(a ->"\""+a.toString()+"\"").collect(Collectors.joining(", "));

        String msdForBB = bigBallH.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String errorsForBB =  bigBallH.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));

        String msdForSB = smallBallH.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String errorsForSB =  smallBallH.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));


        /***
         * Coeficiente de Difusion
         */
        HistogramWithErrors<Double,Double> bigBallCdH = manager.buildHistogramCdWithErrorsForBigBall();
        HistogramWithErrors<Double,Double> smallBallCdH = manager.buildHistogramCdWithErrorsForSmallBall();

        String cdForBB = bigBallCdH.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String cdErrorsForBB =  bigBallCdH.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));

        String cdForSB = smallBallCdH.averageValues().stream().map(a ->a.toString()).collect(Collectors.joining(", "));
        String cdErrorsForSB =  smallBallCdH.errorList().stream()
                .map(e ->"["+e.getMin()+","+e.getMax()+"]")
                .collect(Collectors.joining(", "));
        int a= 3;
    }
}
