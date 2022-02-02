package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class PullSelectionSortD extends BogoSorting {
    public PullSelectionSortD(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pull Selection Heap");
        this.setRunAllSortsName("Pull Selection Heap Sort");
        this.setRunSortName("Pull Selection Heapsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int kl = length;
        while(!isArraySorted(array, kl)) {
            while(length > 0 && !isArraySorted(array, length)) {
                int swaps = 0;
                for(int i=0; i < length; i++) {
                    int min = i;
                    for(int j=i+1; j<length; j++) {
                        Highlights.markArray(2, j);
                        Delays.sleep(0.01);
                        if(Reads.compareValues(array[j], array[min]) == -1) {
                            Writes.multiSwap(array, min, j-1, 0.1, true, false);
                            if(min < j-1)
                                swaps++;
                            min=j;
                        }
                    }
                }
                if(swaps == 0) {
                    length--;
                    Writes.multiSwap(array, 0, length, 1, true, false);
                }
            }
            length = kl;
        }
    }
}
