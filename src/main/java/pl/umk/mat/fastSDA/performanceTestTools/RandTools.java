package pl.umk.mat.fastSDA.performanceTestTools;

import java.util.Random;
import java.util.stream.IntStream;

public class RandTools {
    public static float[][] randFloatMask(int X, int Y) {
        Random r = new Random();
        float[][] m = new float[X][Y];
        IntStream.range(0, X).parallel().forEach(x -> {
            IntStream.range(0, Y).forEach(y -> {
                m[x][y] = r.nextFloat();
            });
        });
        return m;
    }
    public static String stringTime(long time) {
        long min = time / 60000;
        long sek = (time % 60000) / 1000;
        long msek = time % 1000;
        return " " + min + "min. " + sek + "s. " + msek + "ms.";
    }

    public static int[][] randMatrix(int X, int Y) {
        Random r = new Random();
        int[][] m = new int[X][Y];
        IntStream.range(0, X).parallel().forEach(x -> {
            IntStream.range(0, Y).forEach(y -> {
                m[x][y] = r.nextInt(65000);
            });
        });
        return m;
    }
}
