package com.example.project.sprites.extensions;

public class Position {
    public float x;
    public float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "Pos("+x+", "+y+")";
    }
}
