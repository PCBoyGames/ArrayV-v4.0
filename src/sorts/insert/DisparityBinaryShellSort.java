package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class DisparityBinaryShellSort extends Sort {

    public DisparityBinaryShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Disparity Binary Shell");
        this.setRunAllSortsName("Disparity Binary Shell Sort");
        this.setRunSortName("Disparity Binary Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int disparity(int[] array, int end) {
        if(end < 3)
            return end-1;
        int min = 0, max = 0;
        for(int i=1; i<end; i++) {
            if(Reads.compareIndices(array, i, max, 1, true) == 1) {
                max = i;
            } else if(Reads.compareIndices(array, i, min, 1, true) == -1) {
                min = i;
            }
        }
        return Math.abs(max-min);
    }

    private boolean isSorted(int[] array, int end) {
        for(int i=0; i<end-1; i++) {
            if(Reads.compareIndices(array, i, i+1, 0.1, true) == 1)
                return false;
        }
        return true;
    }

    // Grid-esque optimization could be made, but for a gapped Shell Insertion, it would take gap auxiliary
    private void binaryShell(int[] array, int start, int end, int gap) {
        if(end-start <= gap || gap < 1)
            return;

        for(int i=start+gap; i<end; i++) {
            int t = array[i],
                l = start + ((i - start) % gap),
                h = i;
            if(Reads.compareValues(array[i-gap], t) <= 0)
                continue;
            while(l < h) {
                int m = l + (int) ((h - l) / (2 * gap)) * gap;
                if(Reads.compareValues(array[m], t) >= 0) {
                    h = m;
                } else {
                    l = m + gap;
                }
            }
            int j=i-gap;
            while(j >= l) {
                Writes.write(array, j+gap, array[j], 0.5, true, false);
                j -= gap;
            }

            if(l < i)
                Writes.write(array, l, t, 0.5, true, false);
        }
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int size = currentLength;

        while(size > 1) {
            int disparity = this.disparity(array, size);

            if(disparity == size - 1 && isSorted(array, currentLength))
                return;

            this.binaryShell(array, 0, currentLength, disparity);

            size = disparity;
        }
    }
}