package ar.edu.itba.ss.domain;

import java.util.*;

public class Cell {

    private Range rangeX;
    private Range rangeY;
    private Set<Cell> neigbours = new HashSet<>();
    private List<Particle> particles = new ArrayList<>();

    public Cell(Range rangeX, Range rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
    }

   /*todo: eliminar esto despues del test.xyz del main
   public Cell(List<Particle> particles) {
        setParticles(particles);
    }*/

    public void addParticle(Particle particle){
        this.particles.add(particle);
        particle.setCell(this);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Set<Cell> getNeighbours() {
        return neigbours;
    }

    public void setNeigbours(Set<Cell> neigbours) {
        this.neigbours = neigbours;
    }

    public boolean isInside(Particle particle){
        return rangeX.isInRange(particle.getX()) && rangeY.isInRange(particle.getY());
    }

    @Override
    public String toString() {
        return String.format("x:%s y:%s particles:%d",rangeX.toString(), rangeY.toString(), particles.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(rangeX, cell.rangeX) &&
                Objects.equals(rangeY, cell.rangeY);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rangeX, rangeY);
    }

    private double getWidth(){
        return rangeX.getHighest() - rangeX.getLowest();
    }

    public List<Cell> calculateNeighbourCells() {
        double w = getWidth();
        return
                Arrays.asList(
                        generateXY(-1,1,-w,w),           generateXY(0,1,0.,w),      generateXY(1,1,w,w),
                        generateXY(-1,0,-w,0.),                                 generateXY(1,0,w,0.),
                        generateXY(-1,-1,-w,-w),          generateXY(0,-1,0.,-w),     generateXY(1,-1,w,-w)
                );
    }

    private Cell generateXY(int xIdOff, int yIdOff, double xOffset, double yOffset) {
        double x1 = rangeX.getLowest() + xOffset;
        double x2 = rangeX.getHighest() + xOffset;

        double y1 = rangeY.getLowest() + yOffset;
        double y2 = rangeY.getHighest() + yOffset;

        if(x1 <0 || y1 <0){
            return null;
        }

        Range new_rangex = new Range(rangeX.getId()+xIdOff,x1,x2);
        Range new_rangey = new Range(rangeY.getId()+yIdOff,y1,y2);
        return new Cell(new_rangex, new_rangey);
    }

    public Range getRangeX() {
        return rangeX;
    }

    public Range getRangeY() {
        return rangeY;
    }
}