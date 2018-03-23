package ar.edu.itba.ss.domain;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Particle {

    private int id;
    private double x;
    private double y;
    private Cell cell;
    private double speed;
    private double angle;
    private double nextAngle;
    private List<Particle> neighbours = new ArrayList<>();
    private double radix;

    public Particle(double x, double y, double speed, double angle) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
    }

    public Particle(int id, double x, double y, double radix) {
        this.id=id;
        this.x = x;
        this.y = y;
        this.radix = radix;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadix() {
        return radix;
    }

    public void getNewAngle(double eta, RandomDataGenerator rng) {
        //System.out.println(neighbours.size());
        double nextSin = FastMath.sin(angle);
        double nextCos = FastMath.cos(angle);
        for (Particle p:neighbours) {
            nextSin += FastMath.sin(p.angle);
            nextCos += FastMath.cos(p.angle);
        }
        nextSin/=(neighbours.size()+1);
        nextCos/=(neighbours.size()+1);
        //long start = System.currentTimeMillis();
        if(eta==0.){
            eta=0.000000001;
        }
        nextAngle=FastMath.atan2(nextSin,nextCos)+rng.nextUniform(-eta/2,eta/2,true);
        //long end = System.currentTimeMillis();
        //System.out.println("atan: "+ (end-start));
    }

    public void updateAngle() {
        angle=nextAngle;
    }

    public void updateLocation(double length) {

        this.x+=speed*FastMath.cos(angle);
        this.y+=speed*FastMath.sin(angle);
        if (this.x>length)
            this.x-=length;
        else if (this.x<0)
            this.x+=length;
        if (this.y>length)
            this.y-=length;
        else if (this.y<0)
            this.y+=length;

    }

    public Double distanceBorderToBorder(Particle particle){
        return distanceCenterToCenter(particle) - (getRadix() + particle.getRadix());
    }

    private double distanceCenterToCenter(Particle particle) {
        double difx = getX() - particle.getX();
        double dify = getY() - particle.getY();
        return Math.sqrt(Math.pow(difx, 2) + Math.pow(dify, 2));
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public boolean isCloseEnough(Particle particle, double maxDistance) {
        return distanceCenterToCenter(particle) <= maxDistance;
    }

    public boolean isNeighbourCloseEnough(Particle particle, double maxDistance, boolean periodicContourCondition) {
        if (periodicContourCondition) {
            List<Cell> calculated = getCell().calculateNeighbourCells();
            if (!calculated.contains(particle.getCell())) {
                //debo dar la vuelta

                //defino las direcciones en cada una de las componentes
                double newX = getNewX(calculated,particle);
                double newY = getNewY(calculated,particle);



                Particle newParticle = new Particle(newX, newY, particle.speed, particle.getAngle());
                return distanceCenterToCenter(newParticle) <= maxDistance;
            }
        }
        return isCloseEnough(particle, maxDistance);
    }

    private double getNewX(List<Cell> calculated, Particle particle) {
        if (!hasRangeX(calculated, particle.getCell().getRangeX())) {
            if (getX() - particle.getX() > 0) {
                return particle.getX() + getCell().getRangeX().getHighest();
            } else {
                return particle.getX() - getCell().getRangeX().getHighest();
            }
        }
        return particle.getX();
    }
    private double getNewY(List<Cell> calculated, Particle particle) {
        if (!hasRangeY(calculated, particle.getCell().getRangeY())) {
            if (getY() - particle.getY() > 0) {
                return particle.getY() + getCell().getRangeY().getHighest();
            } else {
                return particle.getY() - getCell().getRangeY().getHighest();
            }
        }
        return particle.getY();

    }

    private boolean hasRangeX(List<Cell> calculated, Range rangex) {
        for (Cell c : calculated) {
            if (c != null && c.getRangeX().equals(rangex)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRangeY(List<Cell> calculated, Range rangey) {
        for (Cell c : calculated) {
            try {
                if (c != null && c.getRangeY().equals(rangey)) {
                    return true;
                }
            } catch (Exception e) {
                System.out.print(1);
            }

        }
        return false;
    }

    public List<Particle> getOtherParticlesInCell() {
        return getCell().getParticles().stream().filter(p -> !p.equals(this)).collect(Collectors.toList());
    }

    public String printParticle() {
        return String.format("(%f,%f)", x, y);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%.6f %.6f %.6f %.6f %.6f", x, y, getVx(),getVy(), MathUtils.normalizeAngle(angle,FastMath.PI));
    }

    public void addNeighbour(Particle particle) {
        if (particle == null) {
            throw new RuntimeException("La particula no puede ser nula");
        }
        neighbours.add(particle);
    }

    public double getVx() {
        return speed*FastMath.cos(angle);
    }

    public double getVy() {
        return speed*FastMath.sin(angle);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void clearNeighbours() {
        neighbours.clear();
    }

    public List<Particle> getNeighbours() {
        return neighbours;
    }
}