package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PancakeQuickSort extends Sort {
    public PancakeQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Respectful Panquick");
        this.setRunAllSortsName("Panquick Sort (PancakePrinciple-compliant)");
        this.setRunSortName("Respectful Panquick Sort (LL)");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Copied from OptiPancakeSort
    private void flip(int[] array, int idx, double sleep) {
        Writes.reversal(array, 0, idx, sleep, true, false);
    }

    private void rotate(int[] array, int cycle1, int cycle2, double sleep) {
        this.flip(array, cycle1-1, sleep/3d);
        this.flip(array, cycle2-1, sleep/3d);
        this.flip(array, cycle2-cycle1-1, sleep/3d);
    }

    private int medOf3(int[] array, int p1, int p2, int p3) {
        if(p1 == p2) {
            return p1;
        }
        if(p2 == p3) {
            return p2;
        }
        if(Reads.compareValues(array[p1], array[p2]) <= 0) {
            if(Reads.compareValues(array[p2], array[p3]) <= 0)
                return p2;
            if(Reads.compareValues(array[p1], array[p3]) <= 0)
                return p3;
            return p1;
        }
        if(Reads.compareValues(array[p2], array[p3]) <= 0)
            return p2;
        if(Reads.compareValues(array[p1], array[p3]) <= 0)
            return p1;
        return p3;
    }

    private void pancakeLLQS(int[] array, int length, double sleep) {
        if(length == 2) {
            if(Reads.compareValues(array[0], array[length-1]) > 0) {
                this.flip(array, length-1, sleep*10d);
            }
            return;
        } else if(length < 2)
            return;
        int j=0, mid = (length - 1) / 2, piv = array[this.medOf3(array, 0, mid, length-1)];
        for(int i=0; i<length; i++) {
            int k=i;
            while(k < length && Reads.compareValues(piv, array[k]) >= 0) {
                k++;
                j++;
            }
            if(k > i) { // Minor reversal optimization
                this.flip(array, i-1, sleep/4d);
                this.flip(array, k-1, sleep/4d);
                i = k-1;
            }
        }
        this.pancakeLLQS(array, j, sleep);
        this.rotate(array, j, length, sleep);
        this.pancakeLLQS(array, length - j, sleep);
        this.rotate(array, length - j, length, sleep);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.pancakeLLQS(array, length, 0.5);
    }
}