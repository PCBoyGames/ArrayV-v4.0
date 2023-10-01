package utils;

public class ImplQueue<E> {
    private static int maxGrow = 64;
    private E[] queue;
    private int head, tail, streak;
    public ImplQueue() {
        this(256);
    }

    @SuppressWarnings("unchecked")
    public ImplQueue(int initialCapacity) {
        queue = (E[]) new Object[initialCapacity];
        head = tail = streak = 0;
    }

    @SuppressWarnings("unchecked")
    private void grow(int target) {
        E[] nw = (E[]) new Object[target];
        int left = Math.min(tail - head, queue.length - (head % queue.length));
        System.arraycopy(queue, head % queue.length, nw, 0, left);
        if (head % queue.length >= tail % queue.length) {
            System.arraycopy(queue, 0, nw, left, tail % queue.length);
        }
        queue = nw;
        tail -= head;
        head = 0;
    }
    public int size() {
        return tail - head;
    }
    public boolean isEmpty() {
        return head == tail;
    }
    public void add(E val) {
        if (head % queue.length == tail % queue.length && head < tail) {
            grow(queue.length + maxGrow);
        }
        streak = 0;
        queue[tail++ % queue.length] = val;
    }
    public void add(int index, E val) {
        if (head % queue.length == tail % queue.length && head < tail) {
            grow(queue.length + maxGrow);
        }
        streak = 0;
        for (int i=tail; i>index+head;) {
            queue[i%queue.length] = queue[--i%queue.length];
        }
        queue[(index + head) % queue.length] = val;
    }
    public E shift() {
        if (head >= tail) {
            return (E) null;
        }
        if (streak++ >= maxGrow) {
            grow(queue.length - maxGrow);
            streak = 0;
        }
        return queue[head++ % queue.length];
    }
    public E pop() {
        if (head >= tail) {
            return (E) null;
        }
        if (streak++ >= maxGrow) {
            grow(queue.length - maxGrow);
            streak = 0;
        }
        return queue[--tail % queue.length];
    }
    public E peek(int index) {
        assert index >= 0 && index < tail - head : "OOB";
        return queue[(head+index)%queue.length];
    }
    public E remove(int index) {
        assert index >= 0 && index < tail - head : "OOB";
        if (streak++ >= maxGrow) {
            grow(queue.length - maxGrow);
            streak = 0;
        }
        int g = head + index;
        E t = queue[g%queue.length];
        for (int i=g; i<tail-1;) {
            queue[i%queue.length] = queue[++i%queue.length];
        }
        tail--;
        return t;
    }
}
