package ar.edu.itba.ss.domain;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ParticleImpl implements Particle {

    private final double mass;
    private final double radius;
    private Vector2D position;
    private Vector2D velocity;
    private int collisionCount;


    public ParticleImpl(double mass, double radius, Vector2D position, Vector2D velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void evolve (double time) {
        position = position.add(time,velocity);
    }

    public boolean isSuperposed(Vector2D position, double radius) {
        return FastMath.hypot(position.getX()-this.position.getX(),position.getY()-this.position.getY()) <= radius + this.radius;
    }

    @Override
    public double collidesX() {
        return 0;
    }

    @Override
    public double collidesY() {
        return 0;
    }

    @Override
    public double collides(Particle b) {
        return 0;
    }

    @Override
    public void bounceX() {

        collisionCount++;
    }

    @Override
    public void bounceY() {

        collisionCount++;
    }

    @Override
    public void bounce(Particle b) {

        collisionCount++;
    }

    @Override
    public int getCollisionCount() {
        return collisionCount;
    }
}