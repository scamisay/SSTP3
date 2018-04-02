package ar.edu.itba.ss.helper;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by scamisay on 01/04/18.
 */
public class MeanSquaredDisplacementManager {
    private List<Double> tList;

    //mapas de timpo X MSD
    private Map<Double, List<Double>> bigBallMap;
    private Map<Double, List<Double>> smallBallMap;

    private MeanSquaredDisplacementManager(){}

    public MeanSquaredDisplacementManager(List<Double> tList) {
        this.tList = tList;
        initMaps();
    }

    private void initMaps() {
        bigBallMap = new HashMap<>();
        for(Double time : tList){
            bigBallMap.put(time, new ArrayList<>());
        }

        smallBallMap = new HashMap<>();
        for(Double time : tList){
            smallBallMap.put(time, new ArrayList<>());
        }
    }

    public void addValuesForBigBall(List<Vector2D> positions){
        addValuesForMap(positions, bigBallMap);
    }

    public void addValuesForSmallBall(List<Vector2D> positions){
        addValuesForMap(positions, smallBallMap);
    }

    private void addValuesForMap(List<Vector2D> positions, Map<Double, List<Double>> map){
        MeanSquaredDisplacement msd = new MeanSquaredDisplacement(positions);

        List<Double> msdList = IntStream.range(1,positions.size()).boxed()
                .map(time -> msd.calculate(time))
                .collect(Collectors.toList());

        List<Double> times = map.keySet().stream().sorted().collect(Collectors.toList());
        for(int index = 0; index < times.size() ;index++ ){
            map.get(times.get(index)).add(msdList.get(index));
        }
    }

    public HistogramWithErrors<Double,Double> buildHistogramWithErrorsForBigBall(){
        return buildHistogramWithErrorsForMap(bigBallMap);
    }

    public HistogramWithErrors<Double,Double> buildHistogramWithErrorsForSmallBall(){
        return buildHistogramWithErrorsForMap(smallBallMap);
    }

    private HistogramWithErrors<Double,Double> buildHistogramWithErrorsForMap(Map<Double, List<Double>> particleMap){
        Map<Double, HistError<Double>> mapHist = new HashMap<>();
        for(Double time : particleMap.keySet()){
            double min = particleMap.get(time).stream().mapToDouble(Double::doubleValue).min().getAsDouble();
            double max = particleMap.get(time).stream().mapToDouble(Double::doubleValue).max().getAsDouble();
            double average = particleMap.get(time).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            mapHist.put(time, new HistError<>(min, average, max));
        }
        return new HistogramWithErrors<>(mapHist);
    }

    public HistogramWithErrors<Double, Double> buildHistogramCdWithErrorsForBigBall() {
        return buildHistogramForDiffusionCoeficient(buildHistogramWithErrorsForBigBall());
    }

    private HistogramWithErrors<Double, Double> buildHistogramForDiffusionCoeficient(HistogramWithErrors<Double, Double> msdHist) {
        Map<Double, HistError<Double>> mapHist = new HashMap<>();
        for(Double time : msdHist.getMap().keySet()){
            HistError<Double> histError = msdHist.getMap().get(time);
            double min = histError.getMin()/time;
            double max = histError.getMax()/time;
            double average = histError.getAverage()/time;
            mapHist.put(time, new HistError<>(min, average, max));
        }
        return new HistogramWithErrors<>(mapHist);
    }

    public HistogramWithErrors<Double, Double> buildHistogramCdWithErrorsForSmallBall() {
        return buildHistogramForDiffusionCoeficient(buildHistogramWithErrorsForSmallBall());
    }
}
