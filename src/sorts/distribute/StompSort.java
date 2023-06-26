package sorts.distribute;

import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class StompSort extends Sort {
    public StompSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stomp");
        this.setRunAllSortsName("Stomp Sort");
        this.setRunSortName("Stomp Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int[] getMinMax(int[] array, int len) {
        int[] minmax = new int[2];
        Arrays.fill(minmax, array[0]);
        Highlights.toggleAnalysis(true);
        Highlights.markArray(1, 0);
        Delays.sleep(1);
        for (int i=1; i<len; i++) {
            if (minmax[0] > array[i]) {
                minmax[0] = array[i];
            }

            if (minmax[1] < array[i]) {
                minmax[1] = array[i];
            }

            Highlights.markArray(1, i);
            Delays.sleep(1);
        }

        Highlights.toggleAnalysis(false);
        return minmax;
    }

    void sort(int[] array, int len) {
        int[] minmax = this.getMinMax(array, len);
        int min = minmax[0], max = minmax[1];
        int[] count = new int[max-min+1];
        for (int i=0; i<len; i++) {
            count[array[i]-min]++;
        }
        Writes.changeAuxWrites(len);

        int pos = len-1, idx = count.length-1;
        while (true) {
            Highlights.markArray(2, pos);
            Delays.sleep(1);
            for (int i=0; i<=pos; i++) {
                Writes.write(array, i, idx+min, 0.01, true, false);
            }

            pos -= count[idx];
            if (pos < 0) {
                break;
            }

            count[idx] = 0;
            while (count[idx] == 0) {
                idx--;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.sort(array, sortLength);
    }
}
