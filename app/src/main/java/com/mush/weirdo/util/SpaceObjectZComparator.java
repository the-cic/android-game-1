package com.mush.weirdo.util;

import com.mush.weirdo.world.SpaceObject;

import java.util.Comparator;

/**
 * Created by mirko on 21/01/2017.
 */
public class SpaceObjectZComparator implements Comparator<SpaceObject> {

    @Override
    public int compare(SpaceObject a, SpaceObject b) {
        return (int)(b.spaceNode.localToGlobal().z - a.spaceNode.localToGlobal().z);
    }
}
