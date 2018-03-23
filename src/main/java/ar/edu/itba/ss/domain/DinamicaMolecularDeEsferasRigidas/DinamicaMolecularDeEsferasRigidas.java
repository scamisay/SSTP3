package ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas;

import ar.edu.itba.ss.domain.Particle;
import ar.edu.itba.ss.domain.printers.Printer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class DinamicaMolecularDeEsferasRigidas {

    private List<Particle> particles;
    private Room room;
    private double executionTimeInSeconds;
    private Instant simulationStart;

    public DinamicaMolecularDeEsferasRigidas(List<Particle> particles, Room room,
                                             double executionTimeInSeconds) {
        this.particles = particles;
        this.room = room;
        this.executionTimeInSeconds = executionTimeInSeconds;
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
