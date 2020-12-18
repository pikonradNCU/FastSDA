package pl.umk.mat.fastSDA.nonHistogramSDA;

import org.junit.jupiter.api.Test;
import pl.umk.mat.fastSDA.shiftTests.TestValBuilder;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.umk.mat.fastSDA.nonHistogramSDA.NonHistTestTools.startRandomizedValues;
import static pl.umk.mat.fastSDA.performanceTestTools.RandTools.randMatrix;

public class ShiftTestEF {

    @Test
    public void showTimes() {
        int X = 4000;
        int Y = 3000;
        int R = 100;
        boolean startSingle = false;
        boolean visible=true;

        startRandomizedValues(X, Y, R, visible, startSingle);
    }


    @Test
    public void checkMatrixGen() {
        int X = 5, Y = 3;
        int[][] m = randMatrix(X, Y);
        IntStream.range(0, Y).forEach(y -> {
            IntStream.range(0, X).forEach(x -> {
                System.out.print("," + m[x][y]);
            });
            System.out.println();
        });
    }

    @Test
    public void buildTest() {
        TestValBuilder x = TestValBuilder
                .builder()
                .a(7)
                .build();
        System.out.println(x.getA());
        System.out.println(x.getB());
    }

    @Test
    public void isAutomaticStarted() {
        assertTrue(false);
    }
}
