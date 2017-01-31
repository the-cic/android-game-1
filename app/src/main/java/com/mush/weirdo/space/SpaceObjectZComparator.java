package com.mush.weirdo.space;

import com.mush.weirdo.space.SpaceObject;

import java.util.Comparator;

/**
 * Created by mirko on 21/01/2017.
 */
public class SpaceObjectZComparator implements Comparator<SpaceObject> {

    @Override
    public int compare(SpaceObject a, SpaceObject b) {
        float dif = b.spaceNode.localToGlobal().z - a.spaceNode.localToGlobal().z;
        return dif == 0 ? 0 : (dif > 0 ? 1 : -1);
//        return (int)dif;
    }
}
