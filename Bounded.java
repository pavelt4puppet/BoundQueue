import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bounded<T> implements ProducerConsumerQueue<T> {

    private final T[] storage;
    private final int capacity;

    private int headIndex;
    private int tailIndex;
    private int count;

    private Lock locker;
    private Condition empty;
    private Condition full;

    public Bounded(int capacity) throws Exception {

        // check capacity for being positive
        if (capacity < 1) {
            throw new Exception("Queue size should be positive");
        }

        @SuppressWarnings("unchecked")
        final T[] arr = (T[]) new Object[capacity];

        this.storage = arr;
        this.capacity = capacity;
        this.headIndex = 0;
        this.tailIndex = 0;

        this.locker = new ReentrantLock();
        this.empty = locker.newCondition();
        this.full = locker.newCondition();
    }

    // implementation of the base ProducerConsumerQueue interface
    @Override
    public void enqueue(T item) {
        lock();
        if (count < capacity) {
            storage[headIndex] = item;
            headIndex = (headIndex + 1) % capacity;
            count ++;
            dataAvailable();
            unlock();
        }
        else {
            wait4space();
            unlock();
            enqueue(item);  // recursion
        }
    }

    @Override
    public T dequeue() {
        lock();
        if (count > 0) {
            T one = storage[tailIndex];
            storage[tailIndex] = null;
            tailIndex = (tailIndex + 1) % capacity;
            count--;
            spaceAvailable();
            unlock();
            return one;
        }
        wait4data();
        unlock();
        return dequeue();   // recursion
    }

    // Section responsible for locking and waiting
    //
    // Various different user configurable strategies for behavior when the queue is full or empty can be implemented
    // by providing different implementations of the methods below without altering above enqueue/dequeue methods.

    private void lock() {
        locker.lock();
    }

    private void unlock() {
        locker.unlock();
    }

    private void wait4space() {
        try {
            full.await();
        } catch (InterruptedException ie) {
            // todo: log this?
            ie.printStackTrace();
        }
    }

    private void wait4data() {
        try {
            empty.await();
        } catch (InterruptedException ie) {
            // todo: log this?
            ie.printStackTrace();
        }
    }

    // method to be called upon data availability
    private void dataAvailable() {
        empty.signalAll();
    }

    // method to be called upon space availability
    private void spaceAvailable() {
        full.signalAll();
    }

    // not really need this method. used for debugging only
    public int count() {
        lock();
        int tmp = this.count;
        unlock();
        return tmp;
    }
}