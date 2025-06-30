import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        verifyArgs(args);
        File imagen = new File (args[0]);
        BufferedImage bi = null;
        try {
            bi = ImageIO . read ( imagen );
        } catch (IOException e) {
            System.out.println("Hubo un problema");
            System.exit(1);
        }
        WritableRaster raster = bi.getRaster();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int channels = raster.getNumBands();

        String[] stringTransformMatrix= Arrays.copyOfRange(args,4,10);
        float[] transformMatrix= new float[stringTransformMatrix.length];
        for (int i = 0; i < stringTransformMatrix.length; i++) {
            transformMatrix[i]=Float.parseFloat(stringTransformMatrix[i]);
        }

        float a=transformMatrix[0];
        float b=transformMatrix[1];
        float c=transformMatrix[2];
        float d=transformMatrix[3];
        float e=transformMatrix[4];
        float f=transformMatrix[5];
        
        int[] xCornersValues= {(int) ((a*0)+(b*0)+c), (int) ((a*width)+(b*0)+c), (int) ((a*0)+(b*height)+c), (int) ((a*width)+(b*height)+c)};
        int[] yCornersValues= {(int) ((d*0)+(e*0)+f), (int) ((d*width)+(e*0)+f), (int) ((d*0)+(e*height)+f), (int) ((d*width)+(e*height)+f)};
        int minX= Arrays.stream(xCornersValues).min().orElseThrow();
        int maxX= Arrays.stream(xCornersValues).max().orElseThrow();
        int minY= Arrays.stream(yCornersValues).min().orElseThrow();
        int maxY= Arrays.stream(yCornersValues).max().orElseThrow();

        int newWidth=maxX-minX;
        int newHeight=maxY-minY;

        WritableRaster raster2 = raster.createCompatibleWritableRaster (newWidth, newHeight);
        raster2.setPixels (minX, maxY ,newWidth ,newHeight ,new double[ newWidth * newHeight * channels ]);

        int bufferLimit= Integer.parseInt(args[2]);
        int totalWorkers= Integer.parseInt(args[3]);
        WorkerCounter wc= new WorkerCounter();
        ThreadPool tp= new ThreadPool(bufferLimit, wc, totalWorkers);
        
        long startTime=System.currentTimeMillis();

        TaskLauncher taskLauncher= new TaskLauncher(tp,raster,raster2,totalWorkers,transformMatrix);
        
        taskLauncher.start();

        try {
            wc.endMain();
        } catch (InterruptedException exc) {
            System.out.println("Hubo un problema");
            System.exit(1);
        }

        long endTime=System.currentTimeMillis();

        BufferedImage bi_salida = new BufferedImage (
        bi . getColorModel () , raster2 , bi . isAlphaPremultiplied () , null);
        File outputfile = new File ( args[1] );
        try {
            ImageIO . write ( bi_salida , "jpg" , outputfile );
        } catch (IOException e1) {
            System.out.println("Hubo un problema");
            System.exit(1);
        }

        System.out.println("El tiempo de ejecución fue: "+(endTime-startTime));
    }

    private static void verifyArgs(String[] args) {
        if(args.length!=10){
            System.out.println("Debe haber 10 argumentos");
            System.exit(1);
        }

        if(args[0].isEmpty() || !args[0].endsWith(".jpg")){
            System.out.println("El primer argumento debe ser un la direccion de una imagen en formato jpg");
            System.exit(1);
        }

        if(args[1].isEmpty() || !args[1].endsWith(".jpg")){
            System.out.println("El segundo argumento debe ser un la direccion de destino de la imagen generada en formato jpg. Ej: './test.jpg' ");
            System.exit(1);
        }

        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("El tercer argumento debe ser un numero para el tamaño del buffer");
            System.exit(1);
        }

        try {
            Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("El cuarto argumento debe ser un numero para la cantidad de Workers");
            System.exit(1);
        }

        try {
            for (int i = 4; i < 10; i++) {
                Float.parseFloat(args[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Los argumentos 5 a 10 deben ser la matriz de 3x2 ordenada de izquierda a derecha de arriba a abajo");
            System.exit(1);
        }
    }
}
