package net.mcreator.energioom;

public class upgradeHandler {
    public static double getSpeed(int count) {
        return Math.pow((count + 1), 0.5D);
    }

    public static double getEff(int count) {
        return Math.pow((count + 1), -0.5D);
    }
}
