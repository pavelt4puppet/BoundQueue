// this file contains test for the Bounded Producer Consumer queue
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    private static final int test_capacity  = 123;
    private static final int test_threads   = 5;
    private static final int test_messages  = 1000;

    // this methos provide randomized delay (within given limit)
    private static void delay(int max) {
        try {
            Thread.sleep((int) (Math.random() * max));
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private static void setUpAndRun(Bounded<Payload> queue) {

        AtomicInteger ai = new AtomicInteger();
        AtomicInteger produced = new AtomicInteger();
        AtomicInteger consumed = new AtomicInteger();

        for (int i = 0; i < test_threads; i++) {
            // producer
            new Thread(() -> {
                ai.incrementAndGet();
                delay(1000);
                System.out.println("producer started");
                try {
                    for (int m = 0; m < test_messages; m++) {
                        Payload load = new Payload();
                        produced.addAndGet(load.get());
                        queue.enqueue(load);
                        // delay(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("producer complete: " + queue.count());
                ai.decrementAndGet();
            }).start();

            // consumer
            new Thread(() -> {
                ai.incrementAndGet();
                delay(1000);
                System.out.println("consumer started");
                try {
                    for (int m = 0; m < test_messages; m++) {
                        Payload back = queue.dequeue();
                        consumed.addAndGet(back.get());
                        // delay(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("consumer complete: " + queue.count());
                ai.decrementAndGet();
            }).start();
        }

        try {
            // wait a bit to allow initial population to happen
            Thread.sleep(1000);

            // (primitive) wait for the completion
            while (ai.get() > 0) {
                System.out.println("    waiting. "+ queue.count());;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // this is just an extra check to make sure we consumed exactly what we produced
        int pro = produced.get();
        int con = consumed.get();
        if (pro != con) {
            System.out.println("Error: found a discrepancy between produced (" + pro + ") and consumed (" + con + ").");
        }
    }

    public static void main(String args[]) {

        try {
            Bounded<Payload> one = new Bounded<Payload>(test_capacity);

            setUpAndRun(one);
            System.out.println("=========================================================="+one.count());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}