package com.mush.weirdo;

import java.util.ArrayList;

/**
 * Created by mirko on 30/03/2016.
 */
public class WorldObjectRepository {

    private ArrayList<WorldObject> objects;
    private ArrayList<WorldObject> obstacles;
    private ArrayList<WorldObject> movables;
    private ArrayList<WorldObject> backgrounds;

    public WorldObjectRepository() {
        this.objects = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.movables = new ArrayList<>();
        this.backgrounds = new ArrayList<>();
    }

    public void addBackground(WorldObject object) {
        this.backgrounds.add(object);
    }

    public void removeBackground(WorldObject object) {
        this.backgrounds.remove(object);
    }

    public void add(WorldObject object) {
        this.objects.add(object);
        if (object.getBounds() != null) {
            this.obstacles.add(object);
        }
        if (object.getVelocity() != null) {
            this.movables.add(object);
        }
    }

    public void remove (WorldObject object) {
        this.objects.remove(object);
        if (object.getBounds() != null) {
            this.obstacles.remove(object);
        }
        if (object.getVelocity() != null) {
            this.movables.remove(object);
        }
    }

    public ArrayList<WorldObject> getObjects() {
        return objects;
    }

    public ArrayList<WorldObject> getObstacles() {
        return obstacles;
    }

    public ArrayList<WorldObject> getMovables() {
        return movables;
    }

    public ArrayList<WorldObject> getBackgrounds() {
        return backgrounds;
    }
}
