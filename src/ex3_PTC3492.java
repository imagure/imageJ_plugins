import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class ex3_PTC3492 implements PlugInFilter {
    ImagePlus impf2;

    public int setup(String arg, ImagePlus impf2) {
        this.impf2 = impf2;
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {
        FloatProcessor ipf2=(FloatProcessor)ip.convertToFloat();

        this.impf2.setProcessor(ipf2);

        int w= ipf2.getWidth();
        int h= ipf2.getHeight();
        FloatProcessor ipf_r = new FloatProcessor(w, h);
        ImagePlus ip_r = new ImagePlus("Modulo", ipf_r);

        FloatProcessor ipf_r2 = new FloatProcessor(w, h);
        ImagePlus ip_r2 = new ImagePlus("Angulo", ipf_r2);

        FloatProcessor ipf_r3 = new FloatProcessor(w, h);
        ImagePlus ip_r3 = new ImagePlus("Laplaciano", ipf_r3);

        int A = 3, B = 3;
        int a = A/2, b = B/2;

        double[][] x_kernel = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
        double[][] y_kernel = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
        double[][] l_kernel = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        double dx, dy, dx_2_dy_2;;
        float mod, ang;

        double double_pixel = 0;

        for (int i1=a; i1< (w-a) ;i1++) {
            for(int j1=b; j1< (h-b) ; j1++) {
                dx = 0;
                dy = 0;
                dx_2_dy_2 = 0;
                for (int i2=-a; i2<=a; i2++) {
                    for (int j2=-b; j2<=b; j2++) {
                        double_pixel = ipf2.getf(i1-i2, j1-j2);
                        dx = dx + x_kernel[i2+a][j2+b] * double_pixel;
                        dy = dy + y_kernel[i2+a][j2+b] * double_pixel;
                        dx_2_dy_2 = dx_2_dy_2 + l_kernel[i2+a][j2+b] * double_pixel;
                        mod = (float)Math.sqrt( (dx*dx) + (dy*dy) );
                        ang = (float)Math.atan2(dy, dx);
                        ipf_r.setf(i1, j1, mod);
                        ipf_r2.setf(i1, j1, ang);
                        ipf_r3.setf(i1, j1, (float)dx_2_dy_2);
                    }
                }
            }
        }

        ip_r.show();
        ip_r2.show();
        ip_r3.show();
    }
}