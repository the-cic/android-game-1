package com.mush.weirdo.util;

/**
 * Created by Cic on 3.2.2017.
 */
public class RandomKey {
    public int[] values;

    public RandomKey(int... values1) {
        this.setValues(values1);
    }

    public void setValues(int[] values1){
        values = new int[values1.length];
        System.arraycopy(values1, 0, values, 0, values1.length);
    }

    public void advance(int index) {
        for (int i = 0; i < index; i++) {
            advance();
        }
    }

    public void advance() {
        int temp = values[0];
        for (int i = 1; i < values.length; i++) {
            temp += values[i];
            values[i - 1] = values[i];
        }
        values[values.length - 1] = (int) temp & 0xffff;
    }

}
