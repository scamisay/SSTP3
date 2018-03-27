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
    private int collisionCount = 0;


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

    @Override
    public void evolve (double time) {
        position = position.add(time,velocity);
    }

    @Override
    public boolean isSuperposed(Vector2D position, double radius) {
        return FastMath.hypot(position.getX()-this.position.getX(),position.getY()-this.position.getY()) <= radius + this.radius;
    }

    @Override
    public double collidesX() {
        if (velocity.getX()>0)
            return (0.5 - radius - position.getX())/velocity.getX();
        if (velocity.getX()<0)
            return (radius - position.getX())/velocity.getX();
        return -1;
    }

    @Override
    public double collidesY() {
        if (velocity.getY()>0)
            return (0.5 - radius - position.getY())/velocity.getY();
        if (velocity.getY()<0)
            return (radius - position.getY())/velocity.getY();
        return -1;
    }

    @Override
    public double collides(Particle b) {
        ParticleImpl pb = (ParticleImpl) b;
        Vector2D dv = new Vector2D(velocity.getX()-pb.velocity.getX(),velocity.getY()-pb.velocity.getY());
        Vector2D dr = new Vector2D(position.getX()-pb.position.getX(),position.getY()-pb.position.getY());
        double dvdr = dv.dotProduct(dr);
        if (dvdr>=0)
            return -1;
        double dvdv = dv.dotProduct(dv);
        double drdr = dr.dotProduct(dr);
        double sigma = radius+pb.radius;
        double d = FastMath.pow(dvdr,2)-dvdv*(drdr-FastMath.pow(sigma,2));
        if (d<0)
            return -1;
        return -(dvdr+FastMath.sqrt(d))/dvdv;
    }

    @Override
    public void bounceX() {
        velocity = new Vector2D(-velocity.getX(),velocity.getY());
        collisionCount++;
    }

    @Override
    public void bounceY() {
        velocity = new Vector2D(velocity.getX(),-velocity.getY());
        collisionCount++;
    }

    @Override
    public void bounce(Particle b) {
        ParticleImpl pb = (ParticleImpl) b;
        Vector2D dv = new Vector2D(velocity.getX()-pb.velocity.getX(),velocity.getY()-pb.velocity.getY());
        Vector2D dr = new Vector2D(position.getX()-pb.position.getX(),position.getY()-pb.position.getY());
        double dvdr = dv.dotProduct(dr);
        double j = (2*mass*pb.mass*dvdr)/((mass+pb.mass)*(radius+pb.radius));
        double jx=j*dr.getX()/(radius+pb.radius);
        double jy=j*dr.getY()/(radius+pb.radius);
        velocity = new Vector2D(velocity.getX()-jx/mass,velocity.getY()-jy/mass);
        pb.velocity=new Vector2D(pb.velocity.getX()+jx/mass,pb.velocity.getY()-jy/mass);
        collisionCount++;
        pb.collisionCount++;
    }

    @Override
    public int getCollisionCount() {
        return collisionCount;
    }

    @Override
    public String toString() {
        return position.getX()+ " " + position.getY() + " "
                + velocity.getX() + " " + velocity.getY() + " "
                + mass + " "
                + radius;
    }
}