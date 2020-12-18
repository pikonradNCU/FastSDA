package pl.umk.mat.fastSDA.imageJ.imigeContainers;

import ij.IJ;
import pl.umk.mat.fastSDA.procesUtils.PikoProgress;

public class ImageJProgressBar implements PikoProgress {
    @Override
    public void showProgress(int step, int allSteps) {
        IJ.showProgress(step, allSteps);
    }
}
