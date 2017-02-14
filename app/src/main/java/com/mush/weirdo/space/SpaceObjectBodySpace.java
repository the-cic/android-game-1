package com.mush.weirdo.space;

import android.util.Log;

import com.mush.weirdo.GameContent;
import com.mush.weirdo.map.MapProvider;

import java.util.ArrayList;

/**
 * Created by Cic on 1.2.2017.
 */
public class SpaceObjectBodySpace {

    // Foreground objects
    private ArrayList<SpaceObject> objects;
    // Foreground blocking objects' bodies
    private ArrayList<SpaceObjectBody> objectBodies;

    // Group objects by int z

    public SpaceObjectBodySpace(MapProvider mapProvider) {
        objects = new ArrayList<>();
        objectBodies = new ArrayList<>();
        // use mapProvider to create z-line list of objects
        // how do we update lists for an object that moved?
    }

    public ArrayList<SpaceObject> getObjects() {
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
            // maybe check z line change here?
            // Also, velocity and position update should be part of object, not body
            body.applyPositionUpdate();

            // this is just for player, move it
            // use global position?
            Point3F bodyLocalPosition = body.getNode().localPosition;

            if (bodyLocalPosition.z < GameContent.MAP_TOP_Z) {
//                bodyLocalPosition.z = GameContent.MAP_TOP_Z;
                bodyLocalPosition.set(bodyLocalPosition.x, bodyLocalPosition.y, GameContent.MAP_TOP_Z);
            }
            if (bodyLocalPosition.z > GameContent.MAP_BOTTOM_Z) {
//                bodyLocalPosition.z = GameContent.MAP_BOTTOM_Z;
                bodyLocalPosition.set(bodyLocalPosition.x, bodyLocalPosition.y, GameContent.MAP_BOTTOM_Z);
            }
            if (bodyLocalPosition.x < 0) {
                bodyLocalPosition.x = 0;
                bodyLocalPosition.set(0, bodyLocalPosition.y, bodyLocalPosition.z);
            }
        }
    }

    private void checkObjectBodyCollisions() {
        for (SpaceObjectBody body : objectBodies) {
            if (body.isMoving()) {
                boolean blocked = false;

                for (SpaceObjectBody other : objectBodies) {
                    if (body != other /*&& !blocked*/) {
                        if (body.willIntersect(other) && other.isSolid()) {
                            blocked = true;
                        }
                    }
                }
                if (blocked) {
//                    Log.i("CHECK", "blocked");
                    //body.clearNextPosition();
                }
            }
        }
    }

    private int getObjectZIndex(SpaceObject object){
        return 0;
    }

}
