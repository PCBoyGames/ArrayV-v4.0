package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BadBadSort extends Sort {
    public BadBadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad Bad");
        this.setRunAllSortsName("Bad Bad Sort");
        this.setRunSortName("Badsort^2");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }
    public void omegaPush(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, end-1, start, 0.01, true, false);
        }
    }
    public void omegaPushBW(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, start, end-1, 0.01, true, false);
        }
    }
    // O(2n-1 * (2^(n/2 + 1)))?
    private void omegaSwap(int[] array, int start, int end) {
        if(start >= end)
            return;
        this.omegaPush(array, start, end+1);
        this.omegaPushBW(array, start, end);
        this.omegaSwap(array, start+1, end-1);
        this.omegaSwap(array, start+1, end-1);
    }
    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        for (int i = 0; i < currentLen; i++) {
            int shortest = i, breaks = 0, maxBreaks = 0;
            Delays.sleep(0.05);

            for (int j = i; j < currentLen; j++) {
                Highlights.markArray(3, j);
                Delays.sleep(0.05);
                if(Reads.compareValues(array[i], array[j]) == 1) {
                    if(breaks == 0) {
                        j=i;
                        breaks = ++maxBreaks;
                    } else {
                        if(Reads.compareValues(array[shortest], array[j]) == 1)
                            shortest = j;
                        Highlights.markArray(2, shortest);
                    }
                } else if(breaks > 0) {
                    breaks--;
                }
            }
            this.omegaSwap(array, i, shortest);
        }
    }
}