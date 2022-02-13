package sorts.bogo;

import java.util.Random;

import sorts.templates.Sort;
import main.ArrayVisualizer;


final public class PatternDefeatingSafeBogoSort extends Sort {
    public PatternDefeatingSafeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pattern-Defeating Safe Bogo");
        this.setRunAllSortsName("Pattern-Defeating Safe Bogo Sort");
        this.setRunSortName("Pattern-Defeating Safe Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    int sig(int a, int b, int d) {
        return ((a + b) + d * Math.abs(a - b)) / 2;
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

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Random r = new Random();
        int p = this.run(array, 0, length)-1;

        while(p < length-1) {
            Writes.swap(array, p, p + r.nextInt(length-p), 1, true, false);
            int cmp = p > 0 ? Reads.compareValues(array[p-1], array[p]) : Reads.compareValues(array[p], array[p+1]);
            if(cmp == 0) cmp = -1;
            if(cmp == 1 && p > 0) p--;
            else if(cmp < 0) {
                do {
                    p++;
                } while(p < length && Reads.compareValues(array[p-1], array[p]) <= 0);
                p--;
            }
        }
    }
}