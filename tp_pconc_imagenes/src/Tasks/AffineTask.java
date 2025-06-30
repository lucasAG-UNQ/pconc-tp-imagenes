package Tasks;

import java.awt.image.WritableRaster;

public class AffineTask extends Task{

    private WritableRaster srcRaster;
    private WritableRaster sectionRaster;
    private float[] transformMatrix;

    public AffineTask(WritableRaster srcRaster, WritableRaster sectionRaster, float[] transformMatrix) {
        this.srcRaster = srcRaster;
        this.sectionRaster = sectionRaster;
        this.transformMatrix=transformMatrix;
    }



    @Override
    public void run() {
        float a=transformMatrix[0];
        float b=transformMatrix[1];
        float c=transformMatrix[2];
        float d=transformMatrix[3];
        float e=transformMatrix[4];
        float f=transformMatrix[5];
        int sectionWidth= sectionRaster.getWidth();
        int sectionHeight= sectionRaster.getHeight();
        int xOrigin= sectionRaster.getMinX();
        int yOrigin= sectionRaster.getMinY();

        for (int x = xOrigin; x < xOrigin+sectionWidth; x++) {
            for (int y = yOrigin; y < yOrigin+sectionHeight; y++) {
                int[] srcPixel=inverse(x,y, a, b, c, d, e, f);
                int srcX= srcPixel[0];
                int srcY= srcPixel[1];

                int[] pixel= new int[srcRaster.getNumBands()];
                try {
                    srcRaster.getPixel(srcX,srcY,pixel);
                } catch (ArrayIndexOutOfBoundsException exc) {
                    sectionRaster.getPixel(x, y, pixel);
                }

                sectionRaster.setPixel(x, y, pixel);
            }
        }

        
    }

    private int[] inverse(int x, int y, float a,float b,float c,float d,float e,float f){
        int xValue= (int) (((e/(a*e-b*d))*x)+((b/(b*d-a*e))*y)+((c*e-b*f)/(b*d-a*e)));
        int yValue= (int) (((d/(b*d-a*e))*x)+((a/(a*e-b*d))*y)+((d*d-a*f)/(a*e-b*d)));
        int[] res=new int[2];
        res[0]=xValue;
        res[1]=yValue;
        return res;
    }
}
