package ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas;

public interface Particle {
    public double collidesX();
    public double collidesY();
    public double collides(Particle b);
    public void bounceX();
    public void bounceY();
    public void bounce(Particle b);
    public int getCollisionCount();
}
