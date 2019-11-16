import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

import java.awt.*;

public class wiener_lee implements PlugInFilter{
    ImagePlus impf;

    public int setup(String arg, ImagePlus impf) {
        this.impf = impf;
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {
        FloatProcessor ipf2=(FloatProcessor)ip.convertToFloat();
        this.impf.setProcessor(ipf2);

        int w= ipf2.getWidth();
        int h= ipf2.getHeight();

        FloatProcessor ip_r = new FloatProcessor(w,h);
        ImagePlus imp_r=new ImagePlus("Resultado", ip_r);

        Rectangle roi=ip.getRoi();
        int h_ROI=roi.height;
        int h_0_ROI=roi.y;
        int w_ROI=roi.width;
        int w_0_ROI=roi.x;

        double sum=0, sum_2=0;
        double aux;
        double C2;

        int n = 0;

        for(int y = h_0_ROI; y < (h_0_ROI+h_ROI); y++){
            for(int x = w_0_ROI; x < (w_0_ROI+w_ROI); x++){
                aux = ipf2.getf(x,y);
                sum += aux;
                sum_2 += aux*aux;
                n += 1;
            }
        }

        double mean = sum/n;
        double var = sum_2/(n-1) - mean*mean*n/(n-1);
        double c2 = var/(mean*mean);

        int N = 5;
        int NC = N/2;

        for(int y1 = NC; y1 < (h-NC); y1++) {
            for (int x1 = NC; x1 < (w - NC); x1++) {
                sum = 0;
                sum_2 = 0;
                n = 0;
                for (int y2 = (y1 - NC); y2 <= (y1 + NC); y2++) {
                    for (int x2 = (x1 - NC); x2 <= (x1 + NC); x2++) {
                        aux = ipf2.getf(x2, y2);
                        sum += aux;
                        sum_2 += aux * aux;
                        n += 1;
                    }
                }
                mean = sum/n;
                var = sum_2/(n-1) - mean*mean*n/(n-1);
                c2 = var/(mean*mean);
                double aux_2 = c2/(var/(mean*mean));

                if(aux_2 < 0){
                    aux_2 = 0;
                }
                if(aux_2 > 1){
                    aux_2 = 1;
                }
                aux = (1-aux_2)*(ipf2.getf(x1, y1))+aux_2*mean;
                ip_r.setf(x1, y1, (float)aux);
            }
        }
        imp_r.show();

        // Emax, acumulators and pixel values
        float e_max = ipf2.getf(0, 0) - ip_r.getf(0, 0);
        float p1, p2;
        float fi =0 , gi = 0, fi_2 = 0, gi_2 = 0, fi_gi = 0, fi_minus_gi = 0, fi_minus_gi_2 = 0, n_2 = 0;

        for (int i=0;i<w;i++) {
            for(int j=0; j<h; j++) {
                p1 = ipf2.getf(i, j);
                p2 = ip_r.getf(i, j);
                fi += p1;
                gi += p2;
                fi_2 += (p1*p1);
                gi_2 += (p2*p2);
                fi_gi += (p1*p2);
                fi_minus_gi = (p1-p2);
                fi_minus_gi_2 += fi_minus_gi*fi_minus_gi;
                n_2 +=1 ;
                if (Math.abs(fi_minus_gi) > e_max) e_max = fi_minus_gi;
            }
        }

        float f_mean = fi/n_2;
        float g_mean = gi/n_2;
        float f_var = (fi_2/n_2) - f_mean*f_mean;
        float g_var = (gi_2/n_2) - g_mean*g_mean;
        float f_sd = (float)Math.sqrt(f_var);
        float g_sd = (float)Math.sqrt(g_var);
        float cov_fg = (fi_gi/n_2) - f_mean*g_mean;

        float ssim = (cov_fg/(f_sd*g_sd))*(2*f_mean*g_mean/((f_mean*f_mean)+(g_mean*g_mean)))*(2*f_sd*g_sd/(f_var+g_var));
        float nrmse = (float)Math.sqrt(fi_minus_gi_2/fi_2);

        IJ.log(
                "Emax: " + e_max +
                        "\nNRMSE: " + nrmse +
                        "\nSSIM:  " + ssim
        );
    }
}
