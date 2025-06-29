import Tasks.Task;

public class ThreadPool {
    private Buffer buffer;

    public ThreadPool(int bufferLimit, WorkerCounter wc, int worker){
        this.buffer=new Buffer(worker);
        for (int i = 0; i < worker; i++) {
            Worker launchingWorker = new Worker(buffer, wc);
            launchingWorker.run();
        }
    }

    public synchronized void launch(Task task) throws InterruptedException{
        buffer.produce(task);        
    }
}
