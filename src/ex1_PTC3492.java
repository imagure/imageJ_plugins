/*
Nome: Ricardo Ciriaco Camargo Imagure
Exercício 1 - PTC3492
*/
import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class ex1_PTC3492 implements PlugInFilter {
    private ImagePlus impf2;

    public int setup(String arg, ImagePlus impf2) {
        this.impf2 = impf2;
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {
        FloatProcessor ipf2=(FloatProcessor)ip.convertToFloat();
        this.impf2.setProcessor(ipf2);

        int w= ipf2.getWidth();
        int h= ipf2.getHeight();
        float acum = 0;
        float acum_2 = 0;
        float n = 0;
        float v;
        for (int i=0;i<w;i++) {
            for(int j=0; j<h; j++) {
                n = n + 1;
                v = ipf2.getf(i, j);
                acum = acum + v;
                acum_2 = acum_2 + v*v;
            }
        }
        float mean = acum/n;
        float sd = (float)Math.sqrt((acum_2/n) - mean*mean);
        IJ.log("Media: "+ mean + "\nDP: " + sd);
    }
}
// Resultado sobre a imagem CT.dcm:
// Media :  ~ -267.5
// DP    : 	~ 188.5