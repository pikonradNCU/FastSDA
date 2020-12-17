package pl.umk.mat.fastSDA.sdaUtils;

import lombok.Getter;

public class Ring {
    @Getter final int R;
    @Getter private final int D;
    int[] halfRing;


    public Ring(int r) {
        R = r;
        D = 2 * R + 1;
        halfRing = new int[D];
        int sqr = r*r;
        for (int i=0;i<D;i++){
            int j=1;
            int delta = R-i;
            int quadDiff = sqr-delta*delta;
            while (j*j<=quadDiff) j++;
            halfRing[i]=j;
        }
    }
    public int getHalfRing(int delta){
        try{return halfRing[delta+R];}
        catch (ArrayIndexOutOfBoundsException e) {return 0;}
    }

}
