import java.util.ArrayList;
import java.util.List;

import Tasks.PoisonPill;
import Tasks.Task;

public class Buffer {
    private List<Task> tasks;
    private int bufferLimit;

    public Buffer(int limit){
        tasks=new ArrayList<>();
        bufferLimit=limit;
    }

    public synchronized Task read() throws InterruptedException{
        while (tasks.isEmpty()) {
            wait();
        }
        notifyAll();
        return tasks.removeFirst();
    }

    public synchronized void produce(Task task) throws InterruptedException{
        while (tasks.size()==bufferLimit) {
            wait();
        }
        tasks.addFirst(task);
        notifyAll();
    }
}
