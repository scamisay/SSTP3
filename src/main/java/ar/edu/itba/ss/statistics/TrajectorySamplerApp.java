package ar.edu.itba.ss.statistics;

import ar.edu.itba.ss.domain.CollisionSystem;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by scamisay on 01/04/18.
 */
public class TrajectorySamplerApp {


    public static void main(String[] args) {
        int time = 300;
        int n = 200;
        double dt2 = .02;


        CollisionSystem pc = CollisionSystem.getInstance();
        pc.init(time, n, dt2, new RandomDataGenerator(new JDKRandomGenerator(1)));
        pc.activateTrajectoryTrackerForBigBall();
        pc.calculate(null);
        Sample aSample = new Sample(pc.getTemperature(), pc.getParticleQuantity(), pc.getBigBallTrajectory());
        String printedTrajectory = aSample.trajectory.stream()
                .map(t ->"["+t.getX()+","+t.getY()+"]")
                .collect(Collectors.joining(", "));
        System.out.print(printedTrajectory);
    }

    static class Sample{
        double temperature;
        int particles;
        List<Vector2D> trajectory;

        public Sample(double temperature, int particles, List<Vector2D> trajectory) {
            this.temperature = temperature;
            this.particles = particles;
            this.trajectory = trajectory;
        }
    }
}
