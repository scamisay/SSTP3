package ar.edu.itba.ss.helper;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by scamisay on 30/03/18.
 */
public class CollisionCounter {
    private List<TimeCollission> collissionList = new ArrayList<>();

    private double globalMin = 0;
    private double globalMax;

    private final int BUCKET_QUANTITY = 7;

    public CollisionCounter(double simTime) {
        globalMax = simTime;
    }

    public void addCollision(double time, int numberOfCollsions){
        collissionList.add(new TimeCollission(time, numberOfCollsions));
    }

    public Histogram buildHistogram(){
        //primero necesito la lista ordenada
        Collections.sort(collissionList);

        double step = (globalMax - globalMin) / BUCKET_QUANTITY;

        List<Range> ranges = IntStream.range(0, BUCKET_QUANTITY).boxed()
                            .map(i -> new Range(globalMin+i*step, globalMin+(i+1)*step))
                            .collect(Collectors.toList());

        Map<Range,Integer> histogram = new HashMap<>();
        for(Range range: ranges){
            int collisionForBucket = 0;

            //cuento colisiones para el bucket
            for(TimeCollission timeCollission : collissionList){
                if(range.isIn(timeCollission.getTime())){
                    collisionForBucket += timeCollission.getNumberOfCollsions();
                }
            }

            histogram.put(range, collisionForBucket);
        }

        return new Histogram(histogram);
    }

    class TimeCollission implements Comparable<TimeCollission>{
        private double time;
        private int numberOfCollsions;

        public TimeCollission(double time, int numberOfCollsions) {
            if(time < 0 || numberOfCollsions < 0){
                throw new RuntimeException("Valores invalidos");
            }
            this.time = time;
            this.numberOfCollsions = numberOfCollsions;
        }

        public double getTime() {
            return time;
        }

        public int getNumberOfCollsions() {
            return numberOfCollsions;
        }

        @Override
        public int compareTo(TimeCollission other) {
            return new Double(time).compareTo(other.time);
        }
    }
}
