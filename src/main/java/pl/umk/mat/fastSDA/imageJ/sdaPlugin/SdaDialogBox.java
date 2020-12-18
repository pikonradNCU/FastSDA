package pl.umk.mat.fastSDA.imageJ.sdaPlugin;


import ij.gui.GenericDialog;
import pl.umk.mat.fastSDA.values.SDA_Params;

import javax.swing.*;
import java.awt.*;

public class SdaDialogBox {


    private static SdaPluginParams parseDialogParameters(GenericDialog gd){
        //todo zmiana na liste wyboru, pierwsza opcja zwraca false
        boolean isNegative = (gd.getNextChoiceIndex()==1);
        int R= (int) gd.getNextNumber();
        int threshold = (int) gd.getNextNumber();
        int Z = (int) gd.getNextNumber();
        int overWihite = (int) gd.getNextNumber();

        int methodIndex =  gd.getNextChoiceIndex();
        boolean normalizeTo8Bit = gd.getNextBoolean();


        return new SdaPluginParams(new SDA_Params(R,threshold,overWihite,Z,isNegative,normalizeTo8Bit),methodIndex);
    }

    public static SdaPluginParams showDialog(){

        SDA_Params defaultParams = SDA_Params.getDefault();

        GenericDialog gd = new GenericDialog("SDA 2D/3D");

        Font c = new Font("Calibri", Font.PLAIN, 11);
        JTextArea txt = new JTextArea(2,25);
        txt.append("Statistical Dominance Algorithm\n" +
                "http://home.agh.edu.pl/pioro/sda/\n" +
                "Adam Piorkowski, AGH Univ. Sci. and Tech., PL\n" +
                "Piotr Wisniewski, Nicolaus Copernicus University, PL");
        txt.setFont(c);
        txt.setBorder(BorderFactory.createEtchedBorder());
        txt.setForeground(new Color(0, 32, 128));
        txt.setBackground(gd.getBackground());
        txt.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        txt.setEditable(false);

        Panel paneltext = new Panel();
        paneltext.add(txt);
        gd.addPanel(paneltext,GridBagConstraints.NORTH, new Insets(0,0,0,0));

        gd.addChoice("Relationship direction ", SdaPluginConstans.relationShipDirections,
                SdaPluginConstans.relationShipDirections[(defaultParams.isRelationshipDirection()?1:0)]);

        gd.addNumericField(" Radius  ",defaultParams.getR(),0);
        gd.addNumericField(" Treshold  ",defaultParams.getTreshold(),0);
        gd.addNumericField("Menthos height",defaultParams.getZ(),0);
        gd.addNumericField("Bound of white (ignored for Gray 8)",defaultParams.getOverWhite(),0 );

        gd.addChoice("Method", SdaPluginConstans.methods,"2D on slices");

        gd.addCheckbox("Normalize to 8 bit (NOT READY)",defaultParams.isNormalizeTo8Bit());

        gd.showDialog();

        if (gd.wasCanceled())  return null;

        return parseDialogParameters(gd);
    }
}
