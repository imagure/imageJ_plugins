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
        float min = ipf2.getf(0, 0);
        float max = ipf2.getf(0, 0);

        for (int i=0;i<w;i++) {
            for(int j=0; j<h; j++) {
                n = n + 1;
                v = ipf2.getf(i, j);
                if (v < min) {
                    min = v;
                }
                if (v > max) {
                    max = v;
                }
                acum = acum + v;
                acum_2 = acum_2 + v*v;
            }
        }

        float mean = acum/n;
        float sd = (float)Math.sqrt((acum_2/n) - mean*mean);

        IJ.log(
                "Mean: "  + mean +
                "\nstdDev: " + sd   +
                "\nMax: "    + max  +
                "\nMin: "    + min
        );
    }
}
