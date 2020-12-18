package pl.umk.mat.fastSDA.imageJ.softSdaPlugin;

import ij.gui.GenericDialog;
import pl.umk.mat.fastSDA.values.SoftMethod;
import pl.umk.mat.fastSDA.values.SoftSDAParams;

public class SoftSdaDialogBox {
    private static SoftSDAParams parseDialog(GenericDialog gd){

        int R = (int) gd.getNextNumber();
        int methodIndex =  gd.getNextChoiceIndex();
        SoftMethod method = SoftMethod.values()[methodIndex];
        boolean isMaskFloat = gd.getNextBoolean();;
        float rampSize=(float) gd.getNextNumber();


        return SoftSDAParams.builder()
                .R(R)
                .method(method)
                .isMaskFloat(isMaskFloat)
                .rampFactor(rampSize)
                .build();
    }

    public static SoftSDAParams showDialog(){
        String[] methods=SoftMethod.getMethods();
        SoftSDAParams defaultParams = SoftSDAParams.getDefault();
        int methodIdx=SoftMethod.getIdx(defaultParams.getMethod());
        GenericDialog gd = new GenericDialog("Soft SDA 2D");

        gd.addNumericField("Radius",defaultParams.getR(),0);
        gd.addChoice("Soft method",methods,"CLASSIC");
        gd.addCheckbox("Use float mask",defaultParams.isMaskFloat());
        gd.addNumericField("Ramp size between 0 an 1",defaultParams.getRampFactor(),2);

        gd.showDialog();

        if (gd.wasCanceled())  return null;

        return parseDialog(gd);
    }

}
