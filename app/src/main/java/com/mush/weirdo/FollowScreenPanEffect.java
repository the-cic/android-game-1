package com.mush.weirdo;

/**
 * Created by mirko on 20/03/2016.
 */
public class FollowScreenPanEffect implements ScreenPanEffect {

    public static final FollowScreenPanEffect INSTANCE = new FollowScreenPanEffect();

    @Override
    public void panWorldObject(double screenX, double screenY, WorldObject object) {
        object.getSprite().setPosition(object.getX() - screenX, object.getY() - screenY);
    }
}
