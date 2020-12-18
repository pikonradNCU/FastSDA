package pl.umk.mat.fastSDA.procesUtils;

public class DevNullPikoTools implements PikoProgress, Messenger{
    @Override
    public void showProgress(int step, int allSteps) {

    }

    @Override
    public void showMessage(String m) {
        System.out.println(m);
    }
}
