package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class ImprovedWeaveMergeSortIII extends Sort {
    public ImprovedWeaveMergeSortIII(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Improved Weave Merge III");
        this.setRunAllSortsName("Improved Weave Merge Sort III");
        this.setRunSortName("Improved Weave Mergesort III");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int log(int v) {
        return 31 - Integer.numberOfLeadingZeros(v);
    }

    //pow of 2 only (O(n))
    private void bitReversal(int[] array, int a, int b) {
        int len = b-a, m = 0;
        int d1 = len>>1, d2 = d1+(d1>>1);

        for (int i = 1; i < len-1; i++) {
            int j = d1;

            for (
                int k = i, n = d2;
                (k&1) == 0;
                j -= n, k >>= 1, n >>= 1
            );
            m += j;
            if (m > i) Writes.swap(array, a+i, a+m, 1, true, false);
        }
    }

    private int bitreverse(int val, int log) {
        int v = 0;
        while (log > 0) {
            v |= ((val & 1) << --log);
            val >>= 1;
        }
        return v;
    }

    private void circleBitReverse(int[] array, int a, int s, int z, int b, int depth) {
        Writes.recordDepth(depth);
        int c = log(b-a);
        for (int i=s; i<s+(z-s)/2; i++) {
            int l=a+bitreverse(i, c), r=a+bitreverse(z-(i-s)-1, c);
            if (Reads.compareValues(array[l], array[r]) > 0) {
                Writes.swap(array, l, r, 1, true, false);
            }
        }
        int m = (z-s)/2;
        if (m>0) {
            Writes.recursion();
            circleBitReverse(array, a, s, s + m, b, depth + 1);
            Writes.recursion();
            circleBitReverse(array, a, s + m, z, b, depth + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i=2; i<=currentLength; i*=2) {
            for (int j=0; j<currentLength; j+=i)
                circleBitReverse(array, j, 0, i, j+i, 0);
        }
        bitReversal(array, 0, currentLength);
    }
}