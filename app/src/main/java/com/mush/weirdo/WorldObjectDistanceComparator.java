package com.mush.weirdo;

import java.util.Comparator;

/**
 * Created by mirko on 24/03/2016.
 */
@Deprecated
public class WorldObjectDistanceComparator implements Comparator<WorldObject> {
    @Override
    public int compare(WorldObject a, WorldObject b) {
        return (int)(a.getY() - b.getY());
    }
}
