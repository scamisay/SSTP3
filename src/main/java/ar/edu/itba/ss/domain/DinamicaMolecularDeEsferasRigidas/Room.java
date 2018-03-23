package ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas;

public class Room {

    double rightWall, leftWall, ceiling, floor;

    public Room(double rightWall, double leftWall, double ceiling, double floor) {
        this.rightWall = rightWall;
        this.leftWall = leftWall;
        this.ceiling = ceiling;
        this.floor = floor;
    }

    public double getRightWall() {
        return rightWall;
    }

    public double getLeftWall() {
        return leftWall;
    }

    public double getCeiling() {
        return ceiling;
    }

    public double getFloor() {
        return floor;
    }
}
