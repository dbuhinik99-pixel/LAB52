package org.example.domain;

public class Coordinates {

    private final long x;
    private final double y; // Максимальное значение: 663

    public Coordinates(long x, double y) {

        if (y > 663) {
            throw new IllegalArgumentException("Значение поля y не может быть больше 663.");
        }

        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}