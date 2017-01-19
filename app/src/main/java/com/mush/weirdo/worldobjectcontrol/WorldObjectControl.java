package com.mush.weirdo.worldobjectcontrol;

import com.mush.weirdo.WorldObject;

/**
 * Created by mirko on 20/03/2016.
 */
public interface WorldObjectControl {

    void update(WorldObject object, double secondsPerFrame);
}
