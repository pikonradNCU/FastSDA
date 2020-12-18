package pl.umk.mat.fastSDA.nonHistogramSDA;

import pl.umk.mat.fastSDA.sdaUtils.Ring;

import java.util.stream.IntStream;

public class MaskSDA {



    public static void intMaskMultiThread(int[][] m, int[][] result, int[][] mask, int X, int Y, int R) {
        int[][] pola = new int[X][Y];
        Ring halfRing = new Ring(R);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dx = x0 - x;
                    final int dRx = dx + R;
                    final int ry = halfRing.getHalfRing(dx);
                    IntStream.range(Math.max(0, y0 - ry + 1), Math.min(Y, y0 + ry)).forEach(y -> {
                        pola[x0][y0]++;
                        final int dry = y + R - y0;
                        result[x0][y0] += ((m[x0][y0] - m[x][y]) >> 31) * mask[dRx][dry];
                    });
                });
            });
        });
    }
    public static  void floatMaskMultiThread(int[][] m, int[][] result, float[][] mask, int X, int Y, int R) {
        float[][] pola = new float[X][Y];
        float[][] floatRes = new float[X][Y];
        Ring halfRing = new Ring(R);
        int ringTab[] = new int[2 * R + 1];
        IntStream.range(0, 2 * R + 1).forEach(d -> {
            ringTab[d] = halfRing.getHalfRing(d - R);
        });
        parallelCalcFloatMaskSDA(m, mask, X, Y, R, pola, floatRes, ringTab);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            IntStream.range(0, Y).forEach(y0 -> {
                result[x0][y0] = (int) floatRes[x0][y0];
            });
        });
    }

    private static void parallelCalcFloatMaskSDA(int[][] m, float[][] mask, int X, int Y, int R, float[][] pola, float[][] floatRes, int[] ringTab) {
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                floatRes[x0][y0] = 0;
                pola[x0][y0] = 0;
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dRx = x0 - x + R;
                    final int ry = ringTab[dRx];
                    IntStream.range(Math.max(0, y0 - ry + 1), Math.min(Y, y0 + ry)).forEach(y -> {
                        final int dry = y + R - y0;
                        pola[x0][y0] += mask[dRx][dry];
                        floatRes[x0][y0] += ((m[x0][y0] - m[x][y]) >> 31) * mask[dRx][dry];
                    });
                });
            });
        });
    }

    private static void parallelCalcFloatMaskSDA(int[][] m, float[][] mask, int X, int Y, int R, float[][] pola, float[][] floatRes) {
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                floatRes[x0][y0] = 0;
                pola[x0][y0] = 0;
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dRx = x0 - x + R;
                    IntStream.range(Math.max(0, y0 - R + 1), Math.min(Y, y0 + R)).forEach(y -> {
                        final int dry = y + R - y0;
                        pola[x0][y0] += mask[dRx][dry];
                        floatRes[x0][y0] += ((m[x0][y0] - m[x][y]) >> 31) * mask[dRx][dry];
                    });
                });
            });
        });
    }
}
