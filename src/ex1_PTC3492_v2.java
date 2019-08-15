/*
Nome: Ricardo Ciriaco Camargo Imagure
Exercício 1 - PTC3492
*/
import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class ex1_PTC3492_v2 implements PlugInFilter {
    private ImagePlus impf2;

    public int setup(String arg, ImagePlus impf2) {
        this.impf2 = impf2;
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {
        FloatProcessor ipf2=(FloatProcessor)ip.convertToFloat();
        this.impf2.setProcessor(ipf2);
        ImageStatistics stats = ipf2.getStatistics();

        float mean = (float) stats.mean;
        float stdDev = (float) stats.stdDev;
        float min = (float) stats.min;
        float max = (float) stats.max;

        IJ.log(
                "Mean: "   + mean +
                "\nstdDev: " + stdDev +
                "\nMax: "    + max +
                "\nMin: "    + min
        );
    }
}