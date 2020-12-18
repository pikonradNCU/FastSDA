package pl.umk.mat.fastSDA.nonHistogramSDA;

import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;
import pl.umk.mat.fastSDA.sdaUtils.Ring;

import java.util.stream.IntStream;

import static pl.umk.mat.fastSDA.image.BitScale.GRAY_8;

public class ClassicSDA {
    public static void classicSDA(int[][] inputData, int[][] result, int X, int Y, int R,
                                  BitScale scale) {

        int[][] m = makeColorsPositive(inputData, X, Y, scale);

        for (int x0 = 0; x0 < X; x0++) {
            classicSDAforX(m, result, X, Y, R, scale, x0);
        }
    }

    public static void classicSdaMultiThread(int[][] inputData, int[][] result, int X, int Y, int R,
                                             BitScale scale) {
        int[][] m = makeColorsPositive(inputData, X, Y, scale);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            classicSDAforX(m, result, X, Y, R, scale,x0);
        });
    }

    private static int[][] makeColorsPositive(int[][] inputData, int X, int Y, BitScale scale) {
        int m[][];
        if (scale ==GRAY_8){
            m=new int[X][Y];
            IntStream.range(0, X).parallel().forEach(x -> {
                for (int y = 0; y< Y; y++){
                    m[x][y]=(inputData[x][y] <0) ? inputData[x][y]+256 : inputData[x][y];
                }
            });
        } else m= inputData;
        return m;
    }

    private static void classicSDAforX(int[][] m, int[][] result, int X, int Y, int R,
                                       BitScale scale, int x0) {

        PikoLog log = PikoLog.getInstance();
        int SCALE = 256;
        int T=9;
        for (int y0 = 0; y0 < Y; y0++) {
            int c = 0;
            int p = 0;
            int xmr = Math.max(x0-R,0);
            int xpr = Math.min(x0+R+1,X);
            int ymr = Math.max(y0-R,0);
            int ypr = Math.min(y0+R+1,Y);
            for (int x = xmr; x < xpr; x++) {
                for (int y = ymr; y <ypr; y++) {
                    if ((x - x0) * (x - x0) + (y - y0) * (y - y0) < R * R) {
                        p++;
                        if (m[x][y] +T < m[x0][y0] ) {
                            c++ ;
                            if(x0==32 && y0==220) log.info("m[x][y] = "+m[x][y]);
                        }
                    }
                }
            }
            final int bc = (c * SCALE) / p;
            result[x0][y0] = bc;
        }
    }
    public static void tunedClassicMultiThread(int[][] m, int[][] result, int X, int Y, int R) {
        int[][] pola = new int[X][Y];
        Ring halfRing = new Ring(R);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dx = x0 - x;
                    final int ry = halfRing.getHalfRing(dx);
                    IntStream.range(Math.max(0, y0 - ry), Math.min(Y, y0 + ry + 1)).forEach(y -> {
                        pola[x0][y0]++;
                        if (m[x0][y0] < m[x][y]) result[x0][y0]++;
                    });
                });
            });
        });
    }

    public static void tunedClassicMultiThread2(int[][] m, int[][] result, int X, int Y, int R) {
        int[][] pola = new int[X][Y];
        Ring halfRing = new Ring(R);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dx = x0 - x;
                    final int ry = halfRing.getHalfRing(dx);
                    IntStream.range(Math.max(0, y0 - ry), Math.min(Y, y0 + ry + 1)).forEach(y -> {
                        pola[x0][y0]++;
                        result[x0][y0] = (m[x0][y0] < m[x][y]) ? result[x0][y0] + 1 : result[x0][y0];
                    });
                });
            });
        });
    }

    public static void shiftedClassicMultiThread(int[][] m, int[][] result, int X, int Y, int R) {
        int[][] pola = new int[X][Y];
        Ring halfRing = new Ring(R);
        IntStream.range(0, X).parallel().forEach(x0 -> {
            int lxbound = Math.max(0, x0 - R);
            int rxbound = Math.min(X, x0 + R + 1);
            IntStream.range(0, Y).forEach(y0 -> {
                IntStream.range(lxbound, rxbound).forEach(x -> {
                    final int dx = x0 - x;
                    final int ry = halfRing.getHalfRing(dx);
                    IntStream.range(Math.max(0, y0 - ry), Math.min(Y, y0 + ry + 1)).forEach(y -> {
                        pola[x0][y0]++;
                        result[x0][y0] += ((m[x0][y0] - m[x][y]) >> 31);
                    });
                });
                result[x0][y0]=(result[x0][y0]*256)/pola[x0][y0];
            });
        });
    }
}
