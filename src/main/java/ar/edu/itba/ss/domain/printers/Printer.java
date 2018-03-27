package ar.edu.itba.ss.domain.printers;

import ar.edu.itba.ss.domain.Particle;

import java.util.List;

public class Printer {

    private int c = 0;
    public void print(List<Particle> particles) {
        System.out.println(particles.size());
        System.out.println(++c);
        for (Particle p:particles) {
            System.out.println(p);
        }


    }
}
