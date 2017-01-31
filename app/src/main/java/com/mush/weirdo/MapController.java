package com.mush.weirdo;

import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.space.SpaceObjectBody;

import java.util.ArrayList;

/**
 * Created by Cic on 31.1.2017.
 */
public class MapController {

    private SpaceNode rootNode;
    // Foreground objects
    private ArrayList<SpaceObject> objects;
    // Foreground blocking objects' bodies
    private ArrayList<SpaceObjectBody> objectBodies;


    public MapController(SpaceNode rootNode1, ArrayList<SpaceObject> objects1, ArrayList<SpaceObjectBody> objectBodies1) {
        this.rootNode = rootNode1;
        this.objects = objects1;
        this.objectBodies = objectBodies1;
    }
}
