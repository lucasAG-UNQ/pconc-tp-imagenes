import Tasks.Task;

public class ThreadPool {
    private Buffer buffer;

    public ThreadPool(int bufferLimit, WorkerCounter wc, int workerQ){
        this.buffer=new Buffer(workerQ);
        for (int i = 0; i < workerQ; i++) {
            Worker launchingWorker = new Worker(buffer, wc);
            launchingWorker.start();
            wc.addWorker();
        }
    }

    public synchronized void launch(Task task) throws InterruptedException{
        buffer.produce(task);        
    }
}
