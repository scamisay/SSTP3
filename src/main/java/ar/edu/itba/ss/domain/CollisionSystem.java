package ar.edu.itba.ss.domain;

import ar.edu.itba.ss.domain.printers.Printer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class CollisionSystem {

    private List<Particle> particles = new ArrayList<>();
    private double executionTimeInSeconds;
    private Instant simulationStart;
    private RandomDataGenerator rng;
    private static final double SIDE = 0.5;
    private static final double BIG_RADIUS = 0.05;
    private static final double SMALL_RADIUS = 0.005;
    private static final double BIG_MASS = 100;
    private static final double SMALL_MASS = 0.1;
    private static final double MIN_SPEED = -0.1;
    private static final double MAX_SPEED = 0.1;
    private static CollisionSystem instance;

    public void init(double executionTimeInSeconds, int amount, RandomDataGenerator rng) {
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.rng = rng;
        particles.add(new ParticleImpl(BIG_MASS, BIG_RADIUS,
                new Vector2D(rng.nextUniform(BIG_RADIUS, SIDE - BIG_RADIUS), rng.nextUniform(BIG_RADIUS, SIDE - BIG_RADIUS)),
                new Vector2D(0, 0)));
        for (int i = 0; i < amount; i++) {
            particles.add(new ParticleImpl(SMALL_MASS, SMALL_RADIUS, generatePosition(SMALL_RADIUS),
                    new Vector2D(rng.nextUniform(MIN_SPEED, MAX_SPEED), rng.nextUniform(MIN_SPEED, MAX_SPEED))));
        }
    }

    private Vector2D generatePosition(double radius) {
        boolean isValid;
        Vector2D position;
        do {
            isValid = true;
            position = new Vector2D(rng.nextUniform(radius, SIDE - radius), rng.nextUniform(radius, SIDE - radius));
            for (Particle p : particles) {
                if (p.isSuperposed(position, radius)) {
                    isValid = false;
                    break;
                }
            }
        } while (!isValid);
        return position;
    }

    private CollisionSystem() {
        //Singleton
    }

    public static CollisionSystem getInstance() {
        if (instance == null) {
            instance = new CollisionSystem();
        }
        return instance;
    }

    public void calculate(Printer printer) {
        simulationStart = Instant.now();
        PriorityQueue<Event> pq = new PriorityQueue<>();
        double currentSimTime = 0;
        for (Particle p: particles) {
            calculateWallCollisions(pq,p,currentSimTime);
            for (Particle pp:particles){
                calculateParticleCollisions(pq,p,pp,currentSimTime);
            }
        }
        while (!timeIsOver()) {


        }
        System.out.println("Simulacion terminada");
    }

    private void calculateParticleCollisions(PriorityQueue<Event> pq, Particle p, Particle pp, double currentSimTime) {
        //TODO:Complete
        return;
    }

    private void calculateWallCollisions(PriorityQueue<Event> pq, Particle p, double currentSimTime) {
        //TODO:Complete
        return;
    }

    private boolean timeIsOver() {
        return Duration.between(simulationStart, Instant.now()).getSeconds() > executionTimeInSeconds;
    }
}