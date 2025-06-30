import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class TransformacionAfin {
public void transformar(BufferedImage bi, double a, double b, double c, double d, double e, double f, WritableRaster raster2, int primeraFila, int ultimaFila, int minX, int minY) {
		
		//MATRIZ INVERSA
		double aInvertida = e/((a*e) - (b*d));
		double bInvertida = b/((b*d) - (a*e));
		double cInvertida = ((c*e)-(b*f))/((b*d) - (a*e));
		double dInvertida = d/((b*d) - (a*e));
		double eInvertida = a/((a*e) - (b*d));
		double fInvertida = ((c*d)-(a*f))/((a*e) - (b*d));
		
		
		WritableRaster rasterOriginal = bi.getRaster();
		double ancho = rasterOriginal.getWidth();
		double alto = rasterOriginal.getHeight();
		int canales = rasterOriginal.getNumBands ();
		
		for (int y = primeraFila; y < ultimaFila; y++) {
			for(int x = 0; x < raster2.getWidth(); x++) {
			/*Dado que el WritableRaster no puede tomar números negativos, hay que sumarle minX y minY a los ejes
			 * para que no se c de la imagen los píxeles
			 */
			int xOriginal = (int) Math.round((x+minX)*aInvertida +(y+minY)*bInvertida +cInvertida);
			int yOriginal = (int) Math.round((x+minX)*dInvertida +(y+minY)*eInvertida +fInvertida);
			
			
			int color;
			
			if(xOriginal >= 0 && xOriginal < ancho && yOriginal >= 0 && yOriginal < alto) {
				for(int canal=0; canal<canales; canal++) {
					color = rasterOriginal.getSample(xOriginal, yOriginal, canal);
					raster2.setSample(x, y, canal, color);
				}
			} else {
				for(int canal=0; canal<canales; canal++) {				
					raster2.setSample(x, y, canal, 255); //pinta de blanco
				}
			}
			
			
			}
			}
		}
}
