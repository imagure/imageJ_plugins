import ij.*;
import ij.process.*;
import ij.plugin.filter.*;

public class primeiro_plugin implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }

    public void run(ImageProcessor ip) {
        ip.invert();
    }
}
