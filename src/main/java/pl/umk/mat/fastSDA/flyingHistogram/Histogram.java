package pl.umk.mat.fastSDA.flyingHistogram;

import pl.umk.mat.fastSDA.image.BitScale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Histogram {

    final static int FACTOR = 0x10;
    final static int F=0xF;
    final static int F0=0xF0;
    final static int F00=0xF00;
    final static int F000=0xF000;
    final int offset;

    final BitScale histogramType;
    final int histLevels;

    final List<int[]> hisTab = new ArrayList<>();

    public Histogram(BitScale type){
        histogramType = type;
        switch (type){
            case GRAY_8:{
                hisTab.add(new int[0x100]);
                hisTab.add(new int[0x10]);
                hisTab.add(new int[1]);
                offset = 0x100;
                break;
            }
            case GRAY_16:{
                hisTab.add(new int[0x10000]);
                hisTab.add(new int[0x1000]);
                hisTab.add(new int[0x100]);
                hisTab.add(new int[0x10]);
                hisTab.add(new int[1]);
                offset = 0;//0x8000;
                break;
            }
            default:{
                offset = 0;
            }
        }
        histLevels = hisTab.size()-1;
    }

    public void addPoint(int color) {
        color=(color<0)?color+offset:color;
        for (int p=0;p<histLevels+1;p++){
            hisTab.get(p)[color>>(p*4)]++;
        }
    }
    public void substractPoint(int color) {
        color=(color<0)?color+offset:color;
        for (int p=0;p<histLevels+1;p++){
            hisTab.get(p)[color>>(p*4)]--;
        }
    }


    public int getSize(){
        return hisTab.get(histLevels)[0];
    }

    public int checkColor(int color) {
        color=(color<0)?color+offset:color;
        if (color<0) return 0;
        if (color>= hisTab.get(0).length) return getSize();
        int h=0;//number of point with smaller value
        int shift=0;
        for (int level=histLevels-1;level >=0;level --) {
            int index = color >> (4 * level);
            int[] levelHist = hisTab.get(level);
            for (int i = shift; i < index; i++) {
                h += levelHist[i];
            }
            shift = index * FACTOR;
        }
        return h;
    }

    private Histogram(Histogram h){
        histogramType=h.histogramType;
        histLevels=h.histLevels;
        int[] ints;
        for (int level = 0;level<h.hisTab.size();level++){
            ints = h.hisTab.get(level);
            hisTab.add(Arrays.copyOf(ints, ints.length));
        }
        offset=h.offset;
    }
    public Histogram getCopy(){
        return new Histogram(this);
    }

    public void became(Histogram h){
        for (int level=0;level<hisTab.size();level++){
            int[] src = h.hisTab.get(level);
            System.arraycopy(src,0,hisTab.get(level),0,src.length);
        }
    }

    public int getColor(int color){
        try{
           return hisTab.get(0)[color];
        } catch (Exception e) {
            return 0;
        }
    }

    @Deprecated
    public boolean checkConsistency(){
        int size = getSize();
        boolean consistency = true;
        for (int level = 1; level <= histLevels; level++){
            int sum = 0;
            int[] checked = hisTab.get(level);
            for (int i = 0;i<checked.length;i++) sum+=checked[i];
            consistency = (consistency && (sum == size));
        }
        return consistency;
    }

}
