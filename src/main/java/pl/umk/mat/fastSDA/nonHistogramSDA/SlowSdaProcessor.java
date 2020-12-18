package pl.umk.mat.fastSDA.nonHistogramSDA;

import pl.umk.mat.fastSDA.image.BitScale;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.values.SoftMethod;
import pl.umk.mat.fastSDA.values.SoftSDAParams;

import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.classicSdaMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.ClassicSDA.shiftedClassicMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.MaskSDA.floatMaskMultiThread;
import static pl.umk.mat.fastSDA.nonHistogramSDA.MaskSDA.intMaskMultiThread;
import static pl.umk.mat.fastSDA.sdaUtils.ImageRewriters.getSource2Dimige;
import static pl.umk.mat.fastSDA.sdaUtils.ImageRewriters.putBufferIntoImage2D;
import static pl.umk.mat.fastSDA.sdaUtils.MatrixFabric.getFloatMask;
import static pl.umk.mat.fastSDA.sdaUtils.MatrixFabric.getIntMask;

public class SlowSdaProcessor {

    public static void sda(Image2D image, SoftSDAParams params){
        final int X = image.getShape().getX();
        final int Y = image.getShape().getY();
        final int R = params.getR();
        final BitScale scale = image.getScale();
        int[][] src = getSource2Dimige(image);
        int[][] result = new int[X][Y];
        processSdaOnTables(params, X, Y, R, src, result, scale);

        putBufferIntoImage2D(image,result);

    }

    static void processSdaOnTables(SoftSDAParams params,
                                   int x, int y, int r, int[][] src, int[][] result,
                                   BitScale scale) {
        final SoftMethod method = params.getMethod();
        final boolean isMaskFloat = params.isMaskFloat();
        classicSdaMultiThread(src,result, x, y, r,scale);
//        switch (method){
//            case CLASSIC_ORIGIN:classicSdaMultiThread(src,result, x, y, r,scale); break;
////            case CLASSIC:
////                shiftedClassicMultiThread(src,result, x, y, r);
////                classicSdaMultiThread(src,result, x, y, r,scale);
////                break;
//            default: {
//                if(isMaskFloat) floatMaskMultiThread(src,result,getFloatMask(params), x, y, r);
//                else intMaskMultiThread(src,result,getIntMask(params), x, y, r);
//            }
//        }
    }
}
