package ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas;

public interface Event extends Comparable<Event> {
    public double getTime();
    public Particle getParticle1();
    public Particle getParticle2();
    public boolean wasSuperveningEvent();

}
