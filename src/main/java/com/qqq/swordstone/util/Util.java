package com.qqq.swordstone.util;

import java.util.List;

public class Util {

    public static float[] listToFloatArray(List<Float> floatList) {
        float[] floatArray = new float[floatList.size()];
        int i = 0;

        for (Float f : floatList) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    public static int[] listToIntArray(List<Integer> integerList) {
        return integerList.stream().mapToInt(Integer::valueOf) // Or whatever default you want.
                .toArray();
    }

    public static float gouGuf(float a, float b) {
        return (float) Math.sqrt(a * a + b * b);
    }

    public static double gouGud(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }
}
