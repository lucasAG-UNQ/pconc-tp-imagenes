import java.awt.image.WritableRaster;
import Tasks.Task;

import Tasks.AffineTask;
import Tasks.PoisonPill;

public class TaskLauncher extends Thread {
    private ThreadPool tp;
    private int totalWorkers;
    private WritableRaster srcRaster;
    private WritableRaster outputRaster;
    private float[] transformMatrix;

    public TaskLauncher(ThreadPool tp, WritableRaster srcRaster, WritableRaster outputRaster, int totalWorkers, float[] transformMatrix){
        this.tp=tp;
        this.totalWorkers=totalWorkers;
        this.outputRaster=outputRaster;
        this.transformMatrix=transformMatrix;
        this.srcRaster=srcRaster;
    }

    @Override
    public void run() {
        int width=outputRaster.getWidth();
        int height=outputRaster.getHeight();
        int columnWidth=width/totalWorkers;
        for (int i = 0; i < totalWorkers; i++) {
            int xAnchor=i*columnWidth;
            int sectionWidth= (i==totalWorkers-1) ? width - xAnchor : columnWidth;
            WritableRaster columnRaster=outputRaster.createWritableChild(xAnchor, 0, sectionWidth, height, 0, 0, null);
            Task task= new AffineTask(srcRaster, columnRaster, transformMatrix);
            try {
                tp.launch(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (int i = 0; i < totalWorkers; i++) {
            try {
                tp.launch(new PoisonPill());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
