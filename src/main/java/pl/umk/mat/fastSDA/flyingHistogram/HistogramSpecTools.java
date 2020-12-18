package pl.umk.mat.fastSDA.flyingHistogram;

public class HistogramSpecTools {

    public static boolean checkConsistency(Histogram h){
        int size = h.getSize();
        boolean consistency = true;
        for (int level = 1; level <= h.histLevels; level++){
            int sum = 0;
            int[] checked = h.hisTab.get(level);
            for (int i = 0;i<checked.length;i++) sum+=checked[i];
            consistency = (consistency && (sum == size));
        }
        return consistency;
    }

    public static boolean checkPositive(Histogram h){
        boolean isPositive=true;
        for (int level = 0; level <= h.histLevels; level++){
            int[] checked = h.hisTab.get(level);
            for (int i = 0;i<Math.min(checked.length,256);i++) isPositive = (isPositive && (0<=checked[i]));
        }
        return isPositive;
    }
}
