package pl.umk.mat.fastSDA.sdaUtils;


import pl.umk.mat.fastSDA.sdaUtils.maskCurves.CosinusCurve;
import pl.umk.mat.fastSDA.sdaUtils.maskCurves.Curve;
import pl.umk.mat.fastSDA.sdaUtils.maskCurves.FlatCurve;
import pl.umk.mat.fastSDA.sdaUtils.maskCurves.RampedCosinusCurve;
import pl.umk.mat.fastSDA.sdaUtils.maskCurves.TriangleCurve;
import pl.umk.mat.fastSDA.values.SoftSDAParams;

import java.util.stream.IntStream;

public class MatrixFabric {
    public static float[][] getFloatMask(SoftSDAParams params){
        final int R = params.getR();
        final int R21=2*R+1;
        float[][] mask=new float[R21][R21];
        Curve curve = getCurve(params);
        IntStream.range(0,R21).parallel().forEach(x->{
            final int sqdx = (x - R) * (x - R);
            IntStream.range(0,R21).forEach(y->{
                final float r= (float) Math.sqrt(sqdx +(y-R)*(y-R));
                mask[x][y]= curve.extGetVal(r);
            });
        });

        return mask;
    }
    public static int[][] getIntMask(SoftSDAParams params){
        final int R = params.getR();
        final int R21=2*R+1;
        int[][] mask=new int[R21][R21];
        Curve curve = getCurve(params);
        IntStream.range(0,R21).parallel().forEach(x->{
            final int sqdx = (x - R) * (x - R);
            IntStream.range(0,R21).forEach(y->{
                final float r= (float) Math.sqrt(sqdx +(y-R)*(y-R));
                mask[x][y]=(r<=R)?(int) curve.getVal(r) : 0;
            });
        });

        return mask;
    }

    private static Curve getCurve(SoftSDAParams params) {
        Curve curve;
        switch (params.getMethod()){
            case COSINES:curve = new CosinusCurve(params.getR());
                    break;
            case TRIANGLE:curve= new TriangleCurve(params.getR());
                    break;
            case RAMPED_COS:curve=new RampedCosinusCurve(params.getR(), params.getRampFactor());
                break;
            default:curve = new FlatCurve(params.getR());
        }
        return curve;
    }
}
