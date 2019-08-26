import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class ex3_laplaciano implements PlugInFilter {
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
		ImagePlus ip_r = new ImagePlus("Laplaciano", ipf_r);
		
		int A = 3, B = 3;
		int a = A/2, b = B/2;
		
		double[][] kernel = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
		double dx_2_dy_2;
		
		for (int i1=a; i1< (h-a) ;i1++) {
			for(int j1=b; j1< (w-b) ; j1++) {
				dx_2_dy_2 = 0;
				for (int i2=-a; i2<=a; i2++) {
					for (int j2=-b; j2<=b; j2++) {
						dx_2_dy_2 = dx_2_dy_2 + kernel[i2+a][j2+b] * (double)ipf2.getf(i1-i2, j1-j2);
						ipf_r.setf(i1, j1, (float)dx_2_dy_2);
					}
				}
			}
		}
		
		ip_r.show();
	}
}