package ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas;

import ar.edu.itba.ss.domain.Particle;
import ar.edu.itba.ss.domain.printers.Printer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ParticleContainer {

    private List<Particle> particles;
    private double executionTimeInSeconds;
    private Instant simulationStart;
    private static final double SIDE=0.5;
    private static ParticleContainer instance;

    public void init(List<Particle> particles,
                              double executionTimeInSeconds) {
        this.particles = particles;
        this.executionTimeInSeconds = executionTimeInSeconds;
    }

    protected ParticleContainer(){
        //Singleton
    }

    public static ParticleContainer getInstance() {
        if (instance==null){
            instance = new ParticleContainer();
        }
        return instance;
    }

    public void calculate(Printer printer){
        simulationStart = Instant.now();
        while (!timeIsOver()){

        }
        System.out.println("Simulacion terminada");
    }

    private boolean timeIsOver() {
        return Duration.between(simulationStart, Instant.now()).getSeconds() > executionTimeInSeconds;
    }
}
