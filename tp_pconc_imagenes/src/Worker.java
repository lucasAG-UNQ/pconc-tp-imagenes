import Exceptions.PoisonPillException;
import Tasks.Task;

public class Worker extends Thread {
    private Buffer buffer;
    private WorkerCounter counter;
    private boolean running;

    public Worker(Buffer buffer, WorkerCounter wc){
        this.buffer=buffer;
        counter=wc;
        running=true;
    }

    public void run(){
        while (running) {
            Task task;
            try {
                task=buffer.read();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (PoisonPillException e){
                kill();
            }   
        }
    }

    private void kill(){
        running=false;
        counter.removeWorker();
    }
}
