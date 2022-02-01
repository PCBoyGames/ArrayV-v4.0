package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class DisparityShellSort extends Sort {

    public DisparityShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Disparity Shell");
        this.setRunAllSortsName("Disparity Shell Sort");
        this.setRunSortName("Disparity Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int disparity(int[] array, int start, int end) {
        if(end-start < 3)
            return end-1;
        int min = start, max = start;
        for(int i=start+1; i<end; i++) {
            if(Reads.compareIndices(array, i, max, 1, true) == 1) {
                max = i;
            } else if(Reads.compareIndices(array, i, min, 1, true) == -1) {
                min = i;
            }
        }
        return Math.abs(max-min);
    }

    private boolean isSorted(int[] array, int end) {
        int comp = Reads.compareIndices(array, 0, 1, 0.1, true);
        if(end == 2) {
            if(comp == 1)
                Writes.swap(array, 0, 1, 0.1, true, false);
            return true;
        }
        if(comp == 0)
            comp = -1;
        for(int i=0; i<end-1; i++) {
            if(Reads.compareIndices(array, i, i+1, 0.1, true) != comp) {
                if(comp == 1) {
                    Writes.reversal(array, 0, i, 0.1, true, false);
                }
                return false;
            }
        }
        if(comp == 1) {
            Writes.reversal(array, 0, end-1, 0.1, true, false);
        }
        return true;
    }
    private void shell(int[] array, int start, int end, int gap) {
        if(end-start <= gap || gap < 1)
            return;

        for(int i=start+gap; i<end; i++) {
            int t = array[i], j=i-gap;
            Highlights.markArray(1, j);
            Delays.sleep(0.1);
            while(j >= start) {
                if(Reads.compareValues(array[j], t) < 0) {
                    break;
                }
                Writes.write(array, j+gap, array[j], 0.1, true, false);
                j -= gap;
            }
            if(j < i - gap) {
                Writes.write(array, j+gap, t, 0.1, true, false);
            }
        }
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int size = currentLength;

        while(size > 1) {
            int disparity = this.disparity(array, 0, size);

            if(disparity == size - 1 && isSorted(array, currentLength))
                return;

            this.shell(array, 0, currentLength, disparity);

            size = disparity;
        }
    }
}