package ar.edu.itba.ss.helper;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by scamisay on 01/04/18.
 */
public class MeanSquaredDisplacement {
    private List<Vector2D> positions;

    private MeanSquaredDisplacement(){}

    public MeanSquaredDisplacement(List<Vector2D> positions) {
        this.positions = positions;
    }

    public double calculate(int time){
        return IntStream.range(time,positions.size()) .boxed()
                .mapToDouble( i -> sqrDistance(positions.get(i),positions.get(i-time)))
                .average().getAsDouble();
    }

    private Double sqrDistance(Vector2D pos1, Vector2D pos2) {
        double distance = pos1.distance(pos2);
        return Math.pow(distance,2);
    }
}
