import java.awt.image.WritableRaster;

public class TaskLauncher extends Thread {
    private ThreadPool tp;
    private int totalWorkers;
    private WritableRaster outputRaster;

    public TaskLauncher(ThreadPool tp, WritableRaster outputRaster, int totalWorkers){
        this.tp=tp;
        this.totalWorkers=totalWorkers;
        this.outputRaster=outputRaster;
    }

    @Override
    public void run() {
        int width=outputRaster.getWidth();
        int height=outputRaster.getHeight();
        int channels=outputRaster.getNumBands();
        int columnWidth=width/totalWorkers;
        for (int i = 0; i < totalWorkers; i++) {
            int xAnchor=i*columnWidth;
            int sectionWidth= (i==totalWorkers-1) ? width - xAnchor : width;
            WritableRaster column=outputRaster.createWritableChild(xAnchor, 0, sectionWidth, height, 0, 0, null);
            
        }
    }
}
