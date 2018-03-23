package ar.edu.itba.ss.domain;

import ar.edu.itba.ss.helper.DependecyPrinter;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;
import java.util.Map;

/**
 * Created by scamisay on 14/03/18.
 */
public class BandadasDeAgentesAutopropulsados {

    private double eta;
    private List<Particle> particles;
    private double L;
    private int M;
    private double rc;
    private boolean periodicContourCondition;
    private int steps;
    private int time=0;
    private RandomDataGenerator rng;
    private Map<Particle, List<Particle>> calculated;

    public BandadasDeAgentesAutopropulsados(List<Particle> particles, double l, int m, double rc, double eta,
                                            boolean periodicContourCondition, int steps, RandomDataGenerator rng) {
        if(particles == null){
            throw new RuntimeException("Todos los argumentos son obligatorios");
        }

        if(particles.isEmpty()){
            throw new RuntimeException("Tiene que haber al menos una particula");
        }

        if( steps < 1){
            throw new RuntimeException("Los pasos tienen q ser mayor a 1");
        }

        this.particles = particles;
        L = l;
        M = m;
        this.rc = rc;
        this.eta=eta;
        this.periodicContourCondition = periodicContourCondition;
        this.steps = steps;

        this.rng = rng;
    }

    public void run(DependecyPrinter printer){
        for (int i=0;i<steps;i++) {
            //limpieza de valores anteriores de vecinos
            for (Particle p:particles) {
                p.clearNeighbours();
            }

            //calculo de vecinos
            CellIndexMethod cim = new CellIndexMethod(M, L, rc, particles, periodicContourCondition);
            cim.calculate();

            if(printer != null){
                printer.printFiles(i, particles);
            }

            //actualizo angulo y posiciones
            for (Particle p:particles) {
                p.getNewAngle(eta, rng);
                p.updateLocation(L);
            }
            for (Particle p:particles) {
                p.updateAngle();
            }
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
