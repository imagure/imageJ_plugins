import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class gaussiano implements PlugInFilter {
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

        float sigma = 1.5f; // testar com 4.5 também
        float mean;

        int A = 2*(int)(3*sigma)+1;
        int a = A/2;

        int B = A;
        int b = a;

        float[][] weight = new float[B][A];
        float k = 0;

        for(int r=0; r<B; r++){
            for(int c=0; c<A; c++ ){
                double v = -0.5*((c-a)*(c-a)+(r-b)*(r-b))/(sigma)*(sigma);
                weight[r][c] = (float)Math.exp(v);
                k = k + weight[r][c];
            }
        }

        for(int r=0; r<B; r++) {
            for(int c=0; c<A; c++){
                weight[r][c] = weight[r][c]/k; //normalizando valores dos pesos
            }
        }

        for(int y=b; y < (h-b); y++){
            for(int x=a; x < (w-a); x++){
                mean = 0;
                for(int r=-b; r<=b; r++){
                    for(int c=-a; c<=a; c++){
                        mean = mean + weight[r+b][c+a]*ipf2.getf(x-c, y-r);
                    }
                }
                ip_r.setf(x,y,mean);
            }
        }

        imp_r.show();

        // Emax, acumulators and pixel values
        float e_max = ipf2.getf(0, 0) - ip_r.getf(0, 0);
        float p1, p2;
        float fi =0 , gi = 0, fi_2 = 0, gi_2 = 0, fi_gi = 0, fi_minus_gi = 0, fi_minus_gi_2 = 0, n = 0;

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