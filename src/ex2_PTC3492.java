/*
Nome: Ricardo Ciriaco Camargo Imagure
Exercício 2 - PTC3492
*/
import ij.*;
import ij.io.Opener;
import ij.process.*;
import ij.plugin.filter.*;

public class ex2_PTC3492 implements PlugInFilter {
    private ImagePlus imp_float1;

    public int setup(String arg, ImagePlus imp_float1) {
        this.imp_float1 = imp_float1;
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {

        // choose and open noisy image
        Opener im2 = new Opener();
        IJ.showMessage("Escolha a 2a imagem ");
        ImagePlus imp_float2 = im2.openImage("");
        imp_float2.show();

        // creates float processors
        ImageProcessor ip2 = imp_float2.getProcessor();
        FloatProcessor ip_float1 = (FloatProcessor)ip.convertToFloat();
        FloatProcessor ip_float2 = (FloatProcessor)ip2.convertToFloat();

        // gets image size
        int w = ip_float1.getWidth();
        int h = ip_float1.getHeight();

        // Emax, acumulators and pixel values
        float e_max = ip_float1.getf(0, 0) - ip_float2.getf(0, 0);
        float p1, p2;
        float fi =0 , gi = 0, fi_2 = 0, gi_2 = 0, fi_gi = 0, fi_minus_gi = 0, fi_minus_gi_2 = 0, n = 0;

        for (int i=0;i<w;i++) {
            for(int j=0; j<h; j++) {
                p1 = ip_float1.getf(i, j);
                p2 = ip_float2.getf(i, j);
                fi += p1;
                gi += p2;
                fi_2 += (p1*p1);
                gi_2 += (p2*p2);
                fi_gi += (p1*p2);
                fi_minus_gi = (p1-p2);
                fi_minus_gi_2 += fi_minus_gi*fi_minus_gi;
                n +=1 ;
                if (Math.abs(fi_minus_gi) > e_max) e_max = fi_minus_gi;
            }
        }

        float f_mean = fi/n;
        float g_mean = gi/n;
        float f_var = (fi_2/n) - f_mean*f_mean;
        float g_var = (gi_2/n) - g_mean*g_mean;
        float f_sd = (float)Math.sqrt(f_var);
        float g_sd = (float)Math.sqrt(g_var);
        float cov_fg = (fi_gi/n) - f_mean*g_mean;

        float ssim = (cov_fg/(f_sd*g_sd))*(2*f_mean*g_mean/((f_mean*f_mean)+(g_mean*g_mean)))*(2*f_sd*g_sd/(f_var+g_var));
        float nrmse = (float)Math.sqrt(fi_minus_gi_2/fi_2);

        IJ.log(
         "Emax: " + e_max +
            "\nNRMSE: " + nrmse +
            "\nSSIM:  " + ssim
        );
    }
}