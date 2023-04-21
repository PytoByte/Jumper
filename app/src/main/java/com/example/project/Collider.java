package com.example.project;

import android.util.Log;

public class Collider {
    public Position pos;
    public int width;
    public int height;

    public Collider(Position pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public Collider() {
        this.pos = null;
        this.width = 0;
        this.height = 0;
    }

    public void set(Position pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public boolean checkCollide(Collider col) {
        Boolean nw = (col.pos.x >= pos.x) && (col.pos.x < pos.x + width) && (col.pos.y >= pos.y) && (col.pos.y < pos.y + height);
        Boolean ne =(col.pos.x+col.width > pos.x) && (col.pos.x+col.width < pos.x + width) && (col.pos.y >= pos.y) && (col.pos.y < pos.y + height);
        Boolean sw =(col.pos.x >= pos.x) && (col.pos.x < pos.x + width) && (col.pos.y+col.width > pos.y) && (col.pos.y+col.width < pos.y + height);
        Boolean se =(col.pos.x+col.width > pos.x) && (col.pos.x+col.width < pos.x + width) && (col.pos.y+col.width > pos.y) && (col.pos.y+col.width < pos.y + height);

        return (nw || ne || sw || se);
    }
}
