package com.mush.weirdo.util;

/**
 * Created by Cic on 20.1.2017.
 */
public class AnimatedValue {
    private float value;
    private float startValue;
    private float finishValue;
    private float velocity;

    public AnimatedValue(float v) {
        value = v;
        velocity = 0;
    }

    public void setValue(float v) {
        value = v;
    }

    public float getValue() {
        return value;
    }

    public float getVelocity() {
        return velocity;
    }

    public void transitionTo(double newValue, double seconds) {
        transitionTo((float)newValue, (float)seconds);
    }

    public void transitionTo(float newValue, float seconds) {
        startValue = value;
        finishValue = newValue;
        if (seconds > 0) {
            velocity = (finishValue - startValue) / seconds;
        } else {
            velocity = 0;
            value = finishValue;
        }
    }

    public void update(double secondsPerFrame) {
        if (velocity == 0) {
            return;
        }
        value += velocity * secondsPerFrame;
        if ((velocity > 0 && value > finishValue) || (velocity < 0 && value < finishValue)) {
            value = finishValue;
            velocity = 0;
        }
    }
}