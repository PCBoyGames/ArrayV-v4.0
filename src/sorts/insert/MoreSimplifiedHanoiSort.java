package sorts.insert;

import sorts.templates.Sort;

import main.ArrayVisualizer;


final public class MoreSimplifiedHanoiSort extends Sort {

    public MoreSimplifiedHanoiSort(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);

        this.setSortListName("Further Simplified Hanoi");
        this.setRunAllSortsName("Further Simplified Hanoi Sort");
        this.setRunSortName("Further Simplified Hanoisort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private Rod main, stk1, stk2;
    private class Rod {
        private int[] arr;
        private int sz;
        private boolean AUXWRITE;
        public Rod(int capacity) {
            arr = Writes.createExternalArray(capacity);
            sz = -1;
            AUXWRITE = false;
        }
        public Rod aux(boolean val) {
            this.AUXWRITE = val;
            return this;
        }
        public Rod(int[] array) {
            arr = array;
            sz = -1;
        }
        public boolean empty() {
            return sz < 0;
        }
        public int size() {
            return sz + 1;
        }
        public void dispose() {
            Writes.deleteExternalArray(arr);
            sz = -2;
        }
        public int peekTop(int index) {
            if(empty() || index > sz)
                return Integer.MIN_VALUE;
            return arr[sz-index];
        }
        public void drop(int value) {
            Highlights.markArray(1, sz+1);
            Writes.write(arr, ++sz, value, 1, true, AUXWRITE);
        }
        public int lift() {
            if(empty())
                return Integer.MIN_VALUE;
            Highlights.markArray(2, sz);
            return arr[sz--];
        }
    }

    private void towerHanoi(Rod from, Rod aux, Rod to, int n) {
        if(n == 1) {
            if(from.size() > 0)
                to.drop(from.lift());
        } else if(n > 1) {
            towerHanoi(from, to, aux, n-1);
            if(from.size() > 0)
                to.drop(from.lift());
            towerHanoi(aux, from, to, n-1);
        }
    }

    private void pushAll(int num) {
        int index = 0;
        if(main.size() == 0)
            return;

        while(index < main.size() && Reads.compareValues(main.peekTop(index), num) > 0) {
            index++;
        }
        this.towerHanoi(main, stk2, stk1, index);
    }

    private void runHanoi(int[] array, int end) {
        for(int i=0; i<end; i++) {
            int top = array[i];
            this.pushAll(top);
            main.drop(top);
            this.towerHanoi(stk1, stk2, main, stk1.size());
        }
        stk1.dispose();
        stk2.dispose();
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        main = new Rod(array).aux(false);
        stk1 = new Rod(length).aux(true);
        stk2 = new Rod(length).aux(true);
        this.runHanoi(array, length);
    }
}