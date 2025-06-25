public class WorkerCounter {
    int counter;

    public WorkerCounter(){
        counter=0;
    }

    public synchronized void addWorker(){
        counter++;
    }

    public synchronized void removeWorker(){
        counter--;
        if(counter==0){
            notify();
        }
    }

    public synchronized void endMain() throws InterruptedException{
        while (counter>0) {
            wait();
        }
    }
}
