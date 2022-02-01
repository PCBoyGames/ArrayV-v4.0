package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;


final public class QuantumBubbleSort extends BogoSorting {
    public QuantumBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Quantum Bubble");
        this.setRunAllSortsName("Quantum Bubble Sort");
        this.setRunSortName("Quantum Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private boolean siftLo(int[] array, int start, int end) {
        if(start >= end)
            return false;
        int mid = start + (end-start) / 2;
        boolean f = false;
        if (Reads.compareValues(array[start],array[end]) == 1) {
            Writes.swap(array, start, end, 1, true, false);
            f = true;
        }
        if(start == mid) {
            //if (Reads.compareValues(array[start],array[end]) == -1) {
            //    Writes.swap(array, start, end, 1, true, false);
            //}
            return false;
        }
        boolean l = this.siftLo(array, start, mid);
        boolean r = this.siftLo(array, mid, end);
        return f||l||r;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int l=length-1;
        do {
            // Writes.swap(array, i, length-1, 1, true, false);
            l--;
        } while(l > 0 && this.siftLo(array, 0, length-1));
    }
}