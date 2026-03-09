package org.example.domain;

public class Location {

    private final double x;
    private final Double y;   // не может быть null
    private final Integer z;  // не может быть null

    public Location(double x, Double y, Integer z) {

        if (y == null) {
            throw new IllegalArgumentException("Поле y не может быть null.");
        }

        if (z == null) {
            throw new IllegalArgumentException("Поле z не может быть null.");
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}