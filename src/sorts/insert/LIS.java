package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class LIS extends Sort {
    public LIS(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("LIS");
        this.setRunAllSortsName("Longest Increasing Subsequence");
        this.setRunSortName("Longest Increasing Subsequence");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int pileSearch(int[] array, int[] pilesIdx, int b, int val) {
        int a = 0;

        while (a < b) {
            int m = (a+b)/2;

            Highlights.markArray(2, pilesIdx[m]);
            Delays.sleep(0.5);

            if (Reads.compareValues(val, array[pilesIdx[m]]) < 0)
                b = m;
            else
                a = m+1;
        }
        Highlights.clearMark(2);

        return a;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] pilesIdx = Writes.createExternalArray(length);
        int[] ptr = Writes.createExternalArray(length);

        ptr[0] = -1;
        int pCnt = 1;

        for (int i = 1; i < length; i++) {
            Highlights.markArray(1, i);
            int loc = this.pileSearch(array, pilesIdx, pCnt, array[i]);

            Writes.write(pilesIdx, loc, i, 0, false, true);
            Writes.write(ptr, i, loc > 0 ? pilesIdx[loc-1] : -1, 0, false, true);
            if (loc == pCnt) pCnt++;
        }
        int delay = Math.min(10, length/pCnt), next = pilesIdx[pCnt-1];

        do {
            int root = next;

            Highlights.markArray(2, root);
            Writes.write(array, --length, array[root], delay, true, false);

            next = ptr[root];
        }
        while (next != -1);

        System.out.println(pCnt);
    }
}