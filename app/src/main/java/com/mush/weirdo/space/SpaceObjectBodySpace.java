package com.mush.weirdo.space;

import com.mush.weirdo.GameContent;

import java.util.ArrayList;

/**
 * Created by Cic on 1.2.2017.
 */
public class SpaceObjectBodySpace {

    // Foreground objects
    private ArrayList<SpaceObject> objects;
    // Foreground blocking objects' bodies
    private ArrayList<SpaceObjectBody> objectBodies;

    public SpaceObjectBodySpace(){
        objects = new ArrayList<>();
        objectBodies = new ArrayList<>();
    }

    public ArrayList<SpaceObject> getObjects(){
        return objects;
    }

    public void collectBodies() {
        objectBodies.clear();
        for (SpaceObject spaceObject : objects) {
            if (spaceObject.body != null) {
                objectBodies.add(spaceObject.body);
            }
        }
    }

    public void update(double secondsPerFrame) {
        for (SpaceObject object : objects) {
            object.updateShape(secondsPerFrame);
        }

        for (SpaceObjectBody body : objectBodies) {
            body.update(secondsPerFrame);
        }

        checkObjectBodyCollisions();

        for (SpaceObjectBody body : objectBodies) {
            body.applyPositionUpdate();

            // this is just for player, move it
            // use global position?
            if (body.getNode().localPosition.z < GameContent.MAP_TOP_Z) {
                body.getNode().localPosition.z = GameContent.MAP_TOP_Z;
            }
            if (body.getNode().localPosition.z > GameContent.MAP_BOTTOM_Z) {
                body.getNode().localPosition.z = GameContent.MAP_BOTTOM_Z;
            }
            if (body.getNode().localPosition.x < 0) {
                body.getNode().localPosition.x = 0;
            }
        }
    }

    private void checkObjectBodyCollisions() {
        for (SpaceObjectBody body : objectBodies) {
            boolean blocked = false;
            for (SpaceObjectBody other : objectBodies) {
                if (body != other && !blocked) {
                    if (body.willIntersect(other)) {
                        blocked = true;
                        //System.out.println(body.getNode().localPosition + " blocked by "+ other.getNode().localPosition);
                    }
                }
            }
            if (blocked) {
                body.clearNextPosition();
            }
        }
    }

}
