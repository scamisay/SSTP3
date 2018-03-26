package ar.edu.itba.ss.domain;

public class EventImpl implements Event {
    private Particle particle1;
    private Particle particle2;
    private double time;
    private int collisionsP1 = 0;
    private int collisionsP2 = 0;


    public EventImpl(Particle particle1, Particle particle2, double time) {
        this.particle1 = particle1;
        this.particle2 = particle2;
        if (particle1 != null)
            collisionsP1 = particle1.getCollisionCount();
        if (particle2 != null)
            collisionsP2 = particle2.getCollisionCount();
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
        return !((particle1 != null && particle1.getCollisionCount() != collisionsP1)
               || (particle2 != null && particle2.getCollisionCount() != collisionsP2) );
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(time,o.getTime());
    }
}
