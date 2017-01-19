package com.mush.weirdo.screenpan;

import com.mush.weirdo.GameContent;
import com.mush.weirdo.WorldObject;

/**
 * Created by mirko on 20/03/2016.
 */
public class CycleScreenPanEffect implements ScreenPanEffect {

    public static final CycleScreenPanEffect INSTANCE = new CycleScreenPanEffect();

    @Override
    public void panWorldObject(double screenX, double screenY, WorldObject object) {
        double screenXOverWidth =  screenX % GameContent.WIDTH;
        object.getSprite().setPosition(object.getX() - screenXOverWidth, object.getY() - screenY);
    }
}
