package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BruhMomentSort extends Sort {
    public BruhMomentSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("BRUH MOMENT");
        this.setRunAllSortsName("Bruh Moment Sort");
        this.setRunSortName("Bruh Moment Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Bruh Moment Sort - Order n Pop Sort
    protected void bubbleSort(int[] array, int start, int end, boolean right) {
        int swap = end, comp = right ? 1 : -1;
        while(swap > start) {
            int lastSwap = start;
            for(int i=start; i<swap-1; i++) {
                if(Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                }
            }
            swap = lastSwap;
        }
    }
    protected void bubblePop(int[] array, int start, int end, boolean right) {
        int swap = end, comp = right ? 1 : -1;
        while(swap > start) {
            int lastSwap = start;
            for(int i=start; i<swap-1; i++) {
                if(Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                } else if(lastSwap > start)
                    break;
            }
            swap = lastSwap;
        }
    }
    protected void pop(int[] array, int start, int end, int order, boolean invert) {
        if(start >= end)
            return;
        if(end-start <= 4) {
            this.bubbleSort(array, start, end, !invert);
            return;
        }
        int quarter = (end - start + 1) / 4, half = (end - start + 1) / 2;
        if(order <= 1) {
            this.bubbleSort(array, start, start + quarter, !invert);
            this.bubbleSort(array, start + quarter, start + half, invert);
            this.bubbleSort(array, start + half, end - quarter, !invert);
            this.bubbleSort(array, end - quarter, end, invert);
            this.bubblePop(array, start, start + half, !invert);
            this.bubblePop(array, start + half, end, invert);
            this.bubblePop(array, start, end, !invert);
        } else {
            this.pop(array, start, start+quarter, order-1, invert);
            this.pop(array, start+quarter, start+half, order-1, !invert);
            this.pop(array, start+half, end-quarter, order-1, invert);
            this.pop(array, end-quarter, end, order-1, !invert);
            this.pop(array, start, start+half, order-1, invert);
            this.pop(array, start+half, end, order-1, !invert);
            this.pop(array, start, end, order-1, invert);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.pop(array, 0, currentLength, currentLength, false);
    }
}