// this file contains test Payload

public class Payload {
    private int count;

    public Payload() {
        this.count = (int) (Math.random() * 1234);
    }

    public int get() {
        return this.count;
    }
}