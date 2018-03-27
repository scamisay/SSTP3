package ar.edu.itba.ss.domain;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface Particle {
    double collidesX();
    double collidesY();
    double collides(Particle b);
    void bounceX();
    void bounceY();
    void bounce(Particle b);
    int getCollisionCount();
    boolean isSuperposed(Vector2D position, double radius);
    void evolve(double time);
    Vector2D getVelocity();
    Vector2D getPosition();
}
