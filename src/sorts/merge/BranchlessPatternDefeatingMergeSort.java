package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BranchlessPatternDefeatingMergeSort extends Sort {
    private utils.Timer Timer;
    public BranchlessPatternDefeatingMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Branchless Pattern Defeating Merge");
        this.setRunAllSortsName("Branchless Pattern-Defeating Merge Sort");
        this.setRunSortName("Branchless PDMsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.Timer = arrayVisualizer.getTimer();
    }
    int sig(int a, int b, int d) {
        return ((a + b) + d * Math.abs(a - b)) / 2;
    }
    int oneWayCmp(int a, int b, int bias) {
        Reads.addComparison();
        int c = 0;
        Timer.startLap("Compare");
        c += ((bias * (b - a)) >> 31);
        Timer.stopLap();
        return c;
    }
    private int run(int[] array, int start, int end) {
        if(start >= end - 1)
            return start + 1;
        int cmp = -Reads.compareIndices(array, start++, start, 1, true) | 1,
            k = start - 1, d;
        do {
            d = Reads.compareIndices(array, start++, start, 1, true);
        } while(start < end && d != cmp);
        int m = (start - k) / 2,
            q = sig(k, start-1, -cmp);
        for(int i=0; i<m; i++) {
            Writes.swap(array, k+i, q+cmp*i, 1, true, false);
        }
        return start;
    }
    private void merge(int[] array, int[] buff, int start, int mid, int end) {
        int l, r, t, c;
        if(mid-start < end-mid) {
            Writes.arraycopy(array, start, buff, 0, mid-start, 1, true, true);
            l = 0; r = mid; t = start;
            while(l < mid-start && r < end) {
                c = oneWayCmp(buff[l], array[r], -1);
                Writes.write(array, t++, (c & buff[l]) | (~c & array[r]), 1, true, false);
                l -= c; r -= ~c;
            }
            while(l < mid-start)
                Writes.write(array, t++, buff[l++], 1, true, false);
            return;
        }
        Writes.arraycopy(array, mid, buff, 0, end-mid, 1, true, true);
        l = mid-1; r = end-mid-1; t=end-1;
        while(l >= start && r >= 0) {
            c = oneWayCmp(array[l], buff[r], 1);
            Writes.write(array, t--, (c & array[l]) | (~c & buff[r]), 1, true, false);
            l += c; r += ~c;
        }
        while(r >= 0)
            Writes.write(array, t--, buff[r--], 1, true, false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] runs = Writes.createExternalArray(1 + length/2);
        int Run = 0, rf = 1;
        while(Run < length) {
            Writes.write(runs, rf++, Run= run(array, Run, length), 1, true, true);
        }
        int[] buff = Writes.createExternalArray(length/2);
        while(rf > 1) {
            int j=0;
            for(int i=0; i<rf; i+=2, j++) {
                if(i+1 >= rf);
                else if(i+2 >= rf) {
                    merge(array, buff, runs[i], runs[i+1], length);
                } else {
                    merge(array, buff, runs[i], runs[i+1], runs[i+2]);
                }
                if(j != i)
                    Writes.write(runs, j, runs[i], 1, true, false);
            }
            rf = j;
        }
        Writes.deleteExternalArrays(runs, buff);
    }
}