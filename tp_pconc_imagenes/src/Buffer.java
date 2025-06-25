import java.util.ArrayDeque;
import java.util.Queue;

import Tasks.Task;

public class Buffer {
    private Queue<Task> tasks;
    private int bufferLimit;

    public Buffer(int limit){
        tasks=new ArrayDeque<Task>();
        bufferLimit=limit;
    }

    public synchronized Task read() throws InterruptedException{
        while (tasks.isEmpty()) {
            wait();
        }
        notifyAll();
        return tasks.remove();
    }

    public synchronized void produce(Task task) throws InterruptedException{
        while (tasks.size()==bufferLimit) {
            wait();
        }
        tasks.add(task);
        notifyAll();
    }
}
