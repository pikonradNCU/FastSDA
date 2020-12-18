package pl.umk.mat.fastSDA.SDA;


import lombok.AccessLevel;
import lombok.Getter;
import pl.umk.mat.fastSDA.flyingHistogram.Histogram;
import pl.umk.mat.fastSDA.flyingHistogram.HistogramSpecTools;
import pl.umk.mat.fastSDA.image.Image2D;
import pl.umk.mat.fastSDA.procesUtils.PikoLog;
import pl.umk.mat.fastSDA.sdaUtils.Ring;

import java.util.concurrent.atomic.AtomicBoolean;

public class SdaTool {
    public static final boolean pikoTest=false;
    public static final boolean COPY_BACK_POLICY = false;
    public static final boolean CORRECT_TOGETHER_POLICY = true;
    final Image2D inputImage;
    final Histogram histogram;
    @Getter(AccessLevel.MODULE) private final Ring ring;
    private final int R;
    private final int X;
    private final int Y;
    static AtomicBoolean isRingloged = new AtomicBoolean(false);

    @Getter private int posX;
    @Getter int posY=0;

    private byte[][] checkTable;

    public SdaTool(Image2D inputImage, Histogram histogram, Ring ring) {
        this.inputImage = inputImage;
        this.histogram = histogram;
        this.ring = ring;
        R = ring.getR();
        X=inputImage.getShape().getX();
        Y=inputImage.getShape().getY();
        if (pikoTest){
            if (!isRingloged.get()) {
                isRingloged.set(true);
                for (int i = -R; i <= R; i++) {
                    PikoLog.getInstance().info("hr[" + i + "] = " + ring.getHalfRing(i));
                }
            }
            checkTable = new byte[X][Y];
            for (int i=0;i<X;i++){
                for(int j=0;j<Y;j++){
                    checkTable[i][j]=0;
                }
            }
        }
    }

    void testLog(int cx,int cy, int delta, int hr, int x0,int y0,String method, String anomally){
        PikoLog.getInstance().info(" On method "+method+" problem "+anomally
            + "in pint x="+cx+" y="+cy
            + ", delta "+delta+", half Ring "+hr
            + " with center in x="+x0+" y="+y0);
    }

    void init(int x){
        posX = x;
        for (int delta = -R;delta<=R;delta++){
            int locX=x+delta;
            if (0<=locX && locX<X){
                int boundY=Math.min(ring.getHalfRing(delta),Y);
                if (pikoTest) PikoLog.getInstance().info("init histogram on x "+locX+" y between 0 and "+boundY);
                for (int y =0;y<boundY;y++){
                    histogram.addPoint(inputImage.getGray16Colur(locX,y));
                    if (pikoTest){
                        checkTable[locX][y]=1;
                    }
                }
            }
        }
    }

    void correctRight(Histogram baseHist,int boundX){
        boolean baseHistPositive = HistogramSpecTools.checkPositive(baseHist);
        if (!baseHistPositive){
            PikoLog.getInstance().info("corect on nonpositive x"+posX);
        }
        if (posX+1<boundX) {
            int gray16Colur;
            //todo remove it to make it outside of the method
            histogram.became(baseHist);
            int boundY = Math.min(R, Y);
            for (int delta = 0; delta <= boundY; delta++) {
                int hr = ring.getHalfRing(delta);
                int removeX = posX - hr+1;
                if (0 <= removeX) {
                    gray16Colur = inputImage.getGray16Colur(removeX, delta);
                    histogram.substractPoint(gray16Colur);
                    baseHist.substractPoint(gray16Colur);
                    // for debug only
//                    if (!HistogramSpecTools.checkPositive(baseHist)){
//                        PikoLog.getInstance().info("corect on nonpositive x"+posX);
//                        System.exit(777);
//                    }
                }
                int addX = posX  + hr;
                if (addX < X) {
                    gray16Colur = inputImage.getGray16Colur(addX, delta);
                    histogram.addPoint(gray16Colur);
                    baseHist.addPoint(gray16Colur);
                }
            }
            posY = 0;
        }
        posX++;
        if (baseHistPositive){
            if (!HistogramSpecTools.checkPositive(baseHist)){
                PikoLog.getInstance().info("Loose positive on x"+posX);
            }
        }
    }

    void liftUp(){
        if (posY<Y-1) {
            for (int delta = -Math.min(R, posX); delta <= Math.min(R, X - posX - 1); delta++) {
                int hr = ring.getHalfRing(delta);
                int subY = posY - hr+1;
                int addY = posY + hr ;
                int locX = posX + delta;
                if (0 <= subY) {
                    short substrattedColor = inputImage.getGray16Colur(locX, subY);
                    histogram.substractPoint(substrattedColor);
//                      FOR DEBUG ONLY:
//                    if (!HistogramSpecTools.checkPositive(histogram)){
//                        PikoLog.getInstance().info("liftUp nonpositive x"+posX+" y"+posY
//                                +" delta "+delta+" subY "+subY);
//                        System.exit(777);
//                    }
                }
                if (addY < Y) histogram.addPoint(inputImage.getGray16Colur(locX, addY));
            }
        }
        posY++;
    }
}
