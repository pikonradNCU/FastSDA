package pl.umk.mat.fastSDA.imageJ;

import ij.IJ;
import pl.umk.mat.fastSDA.procesUtils.Messenger;

public class IJMessenger implements Messenger {
    @Override
    public void showMessage(String m) {
        IJ.showMessage(m);
    }
}
