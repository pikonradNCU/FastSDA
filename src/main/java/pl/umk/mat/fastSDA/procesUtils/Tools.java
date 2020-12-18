package pl.umk.mat.fastSDA.procesUtils;

public class Tools {

    public static int[] getBounds(int shapeX, int numberOfThreads) {
        int d=shapeX/numberOfThreads;
        int r=shapeX%numberOfThreads;
        int[] bounds = new int[numberOfThreads+1];
        bounds[0]=0;
        for(int step = 0;step<numberOfThreads;step++){
            bounds[step+1] = bounds[step]+d+((step<r)?1:0);
        }
        return bounds;
    }
}
