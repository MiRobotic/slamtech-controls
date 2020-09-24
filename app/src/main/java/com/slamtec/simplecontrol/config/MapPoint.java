package com.slamtec.simplecontrol.config;

public class MapPoint {

    private int id;
    private String name;
    private float x;
    private float y;
    private float z;
    private float rotation;

    public MapPoint(String name, float x, float y, float z, float rotation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
    }

    public MapPoint(int id, String name, float x, float y, float z, float rotation) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getRotation() {
        return rotation;
    }

    public int getId() {
        return id;
    }
}
