package pl.umk.mat.fastSDA.sdaUtils;


import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.image.Shape2D;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;

import java.util.stream.IntStream;

public class ImageRewriters {
    public static void putBufferIntoImage2D(Image2D image, int[][] buffer){
        final BitScale scale = image.getScale();
        final Shape2D shape = image.getShape();
        putBufferIntoImage2D(image, scale, shape, buffer, null);
    }

    public static void putBufferIntoImage2D(Image2D image, BitScale scale, Shape2D shape, int[][] buffer, PikoLog log) {
        for (int x = 0; x < shape.getX(); x++) {
            for (int y = 0; y < shape.getY(); y++) {
                final int intNewColor = buffer[x][y];
                switch (scale) {
                    case GRAY_8: {
                        final byte c = (intNewColor <128)? (byte) intNewColor :(byte) (intNewColor -256);
                        image.put(x, y, c);
                        break;
                    }
                    case GRAY_16: {
                        image.put(x, y, (short) intNewColor);
                        break;
                    }
                }
            }
        }
    }

    public static int[][] getSource2Dimige(Image2D image){
        final int X = image.getShape().getX();
        final int Y = image.getShape().getY();
        int[][] src = new int[X][Y];
        IntStream.range(0,X).parallel().forEach(x->{
            IntStream.range(0,Y).forEach(y->{
                src[x][y] = image.getGray16Colur(x,y);
            });
        });
        return src;
    }
}
