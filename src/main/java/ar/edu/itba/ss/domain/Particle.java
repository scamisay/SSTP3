package ar.edu.itba.ss.domain;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface Particle {
    public double collidesX();
    public double collidesY();
    public double collides(Particle b);
    public void bounceX();
    public void bounceY();
    public void bounce(Particle b);
    public int getCollisionCount();
    public boolean isSuperposed(Vector2D position, double radius);
}
