package ar.edu.itba.ss.domain;

public interface Event extends Comparable<Event> {
    double getTime();
    Particle getParticle1();
    Particle getParticle2();
    boolean wasSuperveningEvent();

}
