package ar.edu.itba.ss.domain;

public class EventImpl implements Event {
    private Particle particle1;
    private Particle particle2;
    private double time;


    public EventImpl(Particle particle1, Particle particle2, double time) {
        this.particle1 = particle1;
        this.particle2 = particle2;
        this.time = time;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public Particle getParticle1() {
        return particle1;
    }

    @Override
    public Particle getParticle2() {
        return particle2;
    }

    @Override
    public boolean wasSuperveningEvent() {
        return false;
    }

    @Override
    public int compareTo(Event o) {
        return 0;
    }
}
