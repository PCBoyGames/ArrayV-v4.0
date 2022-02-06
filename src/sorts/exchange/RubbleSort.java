package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class RubbleSort extends Sort {
    public RubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rubble");
        this.setRunAllSortsName("Rubble Sort");
        this.setRunSortName("Rubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    int sig(int a, int b, int d) {
        return ((a + b) + d * Math.abs(a - b)) / 2;
    }
    // taken from BPDM
    private int run(int[] array, int start, int end) {
        if(start >= end - 1)
            return start + 1;
        int cmp = -Reads.compareIndices(array, start++, start, 1, true) | 1, k = start - 1, d;
        do {
            d = Reads.compareIndices(array, start++, start, 1, true);
        } while(start < end && d != cmp);
        int m = (start - k) / 2, q = sig(k, start-1, -cmp);
        for(int i=0; i<m; i++) {
            Writes.swap(array, k+i, q+cmp*i, 1, true, false);
        }
        return start;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] runs = Writes.createExternalArray(length/2);
        int rf = 0, r = 0;
        while(r < length) {
            Writes.write(runs, rf++, (r = run(array, r, length)) - 1, 0, false, true);
        }
        while(rf > 1) {
            for(int i=1; i<rf; i++) {
                if(Reads.compareIndices(array, runs[i-1], runs[i], 1, true) > 0) {
                    int t = array[runs[i-1]], temp=runs[i-1]-1;
                    while(temp>=(i-2<0?0:runs[i-2]+1)) {
                        if(Reads.compareValues(array[temp], array[runs[i]]) <= 0)
                            break;
                        Writes.write(array, temp+1, array[temp--], 0.1, true, false);
                    }
                    Writes.write(array, temp+1, array[runs[i]], 0.1, true, false);
                    Writes.write(array, runs[i], t, 0.1, true, false);
                }
            }
            if(runs[rf-1] == runs[rf-2]) {
                Writes.write(runs, --rf, 0, 1, true, true);
            } else {
                Writes.write(runs, rf-1, runs[rf-1]-1, 1, true, true);
            }
        }
        Writes.deleteExternalArray(runs);
    }
}