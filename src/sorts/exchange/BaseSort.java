package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BaseSort extends Sort {
    public BaseSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Base");
        this.setRunAllSortsName("Base Sort");
        this.setRunSortName("Base Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter base:", 2);
    }

    private class Counter {
        private int   next, max, a;
        private int[] cts;

        public Counter(int base, int a, int b) {
            this.max = base - 1;
            this.a   = a;

            this.cts = Writes.createExternalArray(b - a);

            Writes.write(this.cts, 0, 1, 0, false, true);
            this.next = 0;
        }

        private void stabilize() {
            for (int i = 0; i < this.cts.length - 1; i++) {
                if (Reads.compareOriginalIndexValue(this.cts, i, this.max, 0, false) > 0) {
                    Writes.write(this.cts, i + 1, this.cts[i + 1] + 1, 0, false, true);
                    Writes.write(this.cts,     i,                   0, 0, false, true);

                    this.next = i;
                }
            }
        }

        public boolean isSorted() {
            for (int i = 0; i < this.cts.length; i++)
                if (Reads.compareOriginalIndexValue(this.cts, i, this.max, 0, false) < 0)
                    return false;
            return true;
        }

        public int nextComp() {
            int tmp = this.next;
            Writes.write(this.cts, 0, this.cts[0] + 1, 0, false, true);
            this.stabilize();

            return this.a + tmp;
        }
    }

    private void compSwap(int[] array, int idx) {
        if (Reads.compareIndices(array, idx, idx + 1, 1, true) > 0)
            Writes.swap(array, idx, idx + 1, 1, true, false);
    }

    public void sort(int[] array, int a, int b, int base) {
        Counter ctr = new Counter(base, a, b);
        do this.compSwap(array, ctr.nextComp()); while (!ctr.isSorted());
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length, bucketCount);
    }
}