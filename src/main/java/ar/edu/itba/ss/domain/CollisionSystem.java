package ar.edu.itba.ss.domain;

import ar.edu.itba.ss.domain.printers.Printer;
import ar.edu.itba.ss.helper.CollisionCounter;
import ar.edu.itba.ss.helper.Histogram;
import ar.edu.itba.ss.helper.Range;
import ar.edu.itba.ss.helper.RmsVelocityManager;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class CollisionSystem {

    private List<Particle> particles = new ArrayList<>();
    private RandomDataGenerator rng;
    private static final double SIDE = 0.5;
    private static final double BIG_RADIUS = 0.05;
    private static final double SMALL_RADIUS = 0.005;
    private static final double BIG_MASS = 100;
    private static final double SMALL_MASS = 0.1;
    private static final double MIN_SPEED = -0.1;
    private static final double MAX_SPEED = 0.1;
    private double dt2;
    private static CollisionSystem instance;
    private double simTime;
    private CollisionCounter collisionCounter;
    private RmsVelocityManager rmsVelocityManager;
    private boolean countCollisions;
    private double startCalculatingRmsFrom = -1;//por defecto no la calcula nunca

    /***
     * estas variables se calculan al principio de la simulacion y se mantienen constantes
     */
    private double temperature;
    private double totalSystemMass;

    // Boltzmann constant (J/K)
    private static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    //(kg·m2/sec2)/K·mol
    private static final double IDEAL_GAS_CONSTANT = 8.314_5;

    public void init(double simTime, int amount,double dt2, RandomDataGenerator rng) {
        this.simTime = simTime;
        this.rng = rng;
        this.dt2=dt2;
        particles = new ArrayList<>();
        particles.add(new ParticleImpl(BIG_MASS, BIG_RADIUS,
                new Vector2D(rng.nextUniform(BIG_RADIUS, SIDE - BIG_RADIUS), rng.nextUniform(BIG_RADIUS, SIDE - BIG_RADIUS)),
                new Vector2D(0, 0)));
        for (int i = 0; i < amount; i++) {
            particles.add(new ParticleImpl(SMALL_MASS, SMALL_RADIUS, generatePosition(SMALL_RADIUS),
                    new Vector2D(rng.nextUniform(MIN_SPEED, MAX_SPEED), rng.nextUniform(MIN_SPEED, MAX_SPEED))));
        }

        rmsVelocityManager = new RmsVelocityManager();

        temperature = calculateTemperature();
        totalSystemMass = calculateTotalSystemMass();
    }

    private double calculateTotalSystemMass() {
        return particles.stream().mapToDouble(Particle::getMass).sum();
    }

    public double getTemperature() {
        return temperature;
    }

    private double calculateTemperature() {
        double sum = particles.stream()
                .map( p -> p.getMass() * p.getVelocity().getNormSq())
                .mapToDouble(Double::doubleValue)
                .sum();
        return sum/particles.size()/(2*BOLTZMANN_CONSTANT);
    }


    /**
     * METODOS PARA CONTROL DEL A SIMULACION - COMIENZO
     */

    public void setCountCollisions() {
        this.countCollisions = true;
    }

    public void setStartCalculatingRmsFrom(double startCalculatingRmsFrom) {
        this.startCalculatingRmsFrom = startCalculatingRmsFrom;
    }

    /**
     * METODOS PARA CONTROL DEL A SIMULACION - FIN
     */



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
        PriorityQueue<Event> pq = new PriorityQueue<>();
        double currentSimTime = 0;
        double lastSimTime=0;
        //System.out.println(particles.size());
        for (Particle p1: particles) {
            calculateWallCollisions(pq,p1,currentSimTime);
            for (Particle p2:particles){
                if (p2!=p1)
                    calculateParticleCollisions(pq,p1,p2,currentSimTime);
            }
        }

        if(countCollisions){
            collisionCounter = new CollisionCounter(simTime);
        }

        while (!timeIsOver(currentSimTime)) {
            //System.out.println(pq.size());
            Event e = pq.remove();
            if (!e.wasSuperveningEvent()) {
                currentSimTime=e.getTime();
                //System.out.println("Total: " + (currentSimTime-lastSimTime));
                int n;
                for (n=1;n*dt2<(currentSimTime-lastSimTime);n++){
                    evolveSystem(dt2);

                    //en caso de varias corridas para construccion de instagramas es util no imprimir
                    if(printer!= null){
                        printer.print(particles);
                    }

                    double currentAbsoluteTime = lastSimTime + n*dt2;
                    if(startCalculatingRmsFrom != -1 && startCalculatingRmsFrom < currentAbsoluteTime){
                        //calculo RMS velocity
                        addRMSVelocity(currentAbsoluteTime, calculateRmsVelocity());
                    }
                }
                evolveSystem((currentSimTime-lastSimTime)-(n-1)*dt2);
                //evolveSystem(currentSimTime-lastSimTime);
                //printer.print(particles);
                if (e.getParticle1() == null) {
                    e.getParticle2().bounceY();

                    //cuento una colision
                    addCollision(currentSimTime,1);

                    calculateWallCollisions(pq,e.getParticle2(),currentSimTime);
                    for (Particle p:particles) {
                        if (p!=e.getParticle2())
                            calculateParticleCollisions(pq,p,e.getParticle2(),currentSimTime);
                    }
                } else if (e.getParticle2() == null) {
                    e.getParticle1().bounceX();

                    //cuento una colision
                    addCollision(currentSimTime,1);

                    calculateWallCollisions(pq,e.getParticle1(),currentSimTime);
                    for (Particle p:particles) {
                        if (p!=e.getParticle1())
                            calculateParticleCollisions(pq,p,e.getParticle1(),currentSimTime);
                    }

                } else {
                    e.getParticle1().bounce(e.getParticle2());

                    //cuento dos colisiones
                    addCollision(currentSimTime,2);

                    calculateWallCollisions(pq,e.getParticle2(),currentSimTime);
                    calculateWallCollisions(pq,e.getParticle1(),currentSimTime);
                    for (Particle p:particles) {
                        if (p!=e.getParticle2() && p!=e.getParticle1()) {
                            calculateParticleCollisions(pq, p, e.getParticle2(), currentSimTime);
                            calculateParticleCollisions(pq, p, e.getParticle1(), currentSimTime);
                        }
                    };


                }
                //System.out.printf("Time since last crash: %.0f"+(currentSimTime-lastSimTime));
                lastSimTime=currentSimTime;
                //double totalEnergy = 0;
                /*for (Particle p:particles){
                    totalEnergy+= 0.5*p.getMass()*p.getVelocity().getNormSq();
                }*/
                //System.out.println("Energy: "+totalEnergy);

                System.out.println("Sim Time: "+currentSimTime);

            }

        }

        System.out.println("Simulacion terminada");
        System.out.println("DT2: "+dt2);
        System.out.println("N: "+particles.size());
    }

    private void addRMSVelocity(double time, double rmsVelocity) {
        rmsVelocityManager.add(time,rmsVelocity);
    }

    private double calculateRmsVelocity() {
        return Math.sqrt(3*IDEAL_GAS_CONSTANT*getTemperature()/totalSystemMass);
    }


    private void addCollision(double currentSimTime, int quantity) {
        if(collisionCounter != null){
            collisionCounter.addCollision(currentSimTime,quantity);
        }
    }

    private void calculateParticleCollisions(PriorityQueue<Event> pq, Particle p1, Particle p2, double currentSimTime) {
        double c = p1.collides(p2);
        if (c>=0)
            pq.add(new EventImpl(p1,p2,currentSimTime+c));
    }

    private void evolveSystem(double time) {
        for (Particle p:particles){
            p.evolve(time);
        }

    }

    private void calculateWallCollisions(PriorityQueue<Event> pq, Particle p, double currentSimTime) {
        double cx = p.collidesX();
        double cy = p.collidesY();

        if (cx>=0)
            pq.add(new EventImpl(p,null,currentSimTime+cx));
        if (cy>=0)
            pq.add(new EventImpl(null,p,currentSimTime+cy));
    }

    private boolean timeIsOver(double currentSimTime) {
        return currentSimTime>simTime;
    }

    public Histogram<Range,Integer> buildCollisionHistogram() {
        return collisionCounter.buildHistogram();
    }

    public Histogram<Range, Double> buildRmsVelocityHistogram(int bucketQuantity) {
        return rmsVelocityManager.buildHistogram(bucketQuantity);
    }
}
