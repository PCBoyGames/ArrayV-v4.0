package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * You'll likely die before this sorts n=2
 */
public class VutuuwuSort extends BogoSorting {
    public VutuuwuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Vutuuwu");
        this.setRunAllSortsName("VutuUwU Sort");
        this.setRunSortName("Vutuuwusort v0.1");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }
    public void omegaPush(int[] array, int start, int end) {
        for (int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, end-1, start, 0.01, true, false);
        }
    }
    public void omegaPushBW(int[] array, int start, int end) {
        for (int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, start, end-1, 0.01, true, false);
        }
    }
    public void omegaPush(int[] array, int start, int end, int k) {
        if (k==0) {
            omegaPush(array, start, end);
        } else
            for (int i=0; i<end-start-1; i++) {
                omegaPushBW(array, start, end, k-1);
            }
    }
    public void omegaPushBW(int[] array, int start, int end, int k) {
        if (k==0) {
            omegaPushBW(array, start, end);
        } else
            for (int i=0; i<end-start-1; i++) {
                omegaPush(array, start, end, k-1);
            }
    }
    // O(2n-1 * (2^(n/2 + 1)))?
    private void omegaSwap(int[] array, int start, int end, int r) {
        if (start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaPush(array, start, end+1, end-start);
        this.omegaPushBW(array, start, end, end-start);
        this.omegaSwap(array, start+1, end-1, r);
        this.omegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaPush1(int[] array, int start, int end, int depth) { // Clamber-esque push, because I want it to be the worst possible.
        depth++;
        for (int j=end-1; j>=start; j--) {
            omegaSwap(array, j, end-1, depth);
        }
    }
    private void omegaOmegaPushBW1(int[] array, int start, int end, int depth) {
        depth++;
        for (int j=start+1; j<end; j++) {
            omegaSwap(array, start, j, depth);
        }
    }
    private void omegaOmegaPush(int[] array, int start, int end, int depth) {
        depth++;
        for (int i=start; i<end-1; i++) {
            omegaOmegaPushBW1(array, start, end, depth);
        }
    }
    private void omegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for (int i=start; i<end-1; i++) {
            omegaOmegaPush1(array, start, end, depth);
        }
    }
    private void omegaOmegaSwap(int[] array, int start, int end, int r) {
        if (start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaOmegaPush(array, start, end+1, r);
        this.omegaOmegaPushBW(array, start, end, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for (int j=start+1; j<end; j++) {
            omegaOmegaSwap(array, start, j, depth);
        }
    }
    private void what_why(int[] array, int start, int end, int d) {
        Writes.recordDepth(d++);
        int m=(end-start)/2;
        if (m==0)
            return;
        for (int i=0;i<m;i++) {
            omegaOmegaOmegaPushBW(array, start, end, d);
        }
        what_why(array, start, start+m, d);
        what_why(array, start+m, end, d);
    }
    private void wotateOwO(int[] array, int range, int amount, int d) {
        this.what_why(array, 0, range-amount, d);
        this.what_why(array, range-amount, range, d);
        this.what_why(array, 0, range, d);
    }
    private void vutuuwu(int[] array, int length, int order, int order2, int rec) {
        if (length==1||Math.min(order2, order)==0)return;
        Writes.recordDepth(rec++);
        int z = (int)Math.pow(order*order2, length);
        while (z-->~(-1>>>1)) {
            for (int i=1; i<=order*order2; i++) {
                for (int j=0; j<i*order*order2; j++) {
                    vutuuwu(array, length, order-1, order2, rec);
                    vutuuwu(array, length, order, order2-1, rec);
                    vutuuwu(array, length-1, order, order2, rec);
                }
                int j=randInt(0,length), k=randInt(j,length);
                while (j>=0 && Reads.compareIndices(array, j, k, 1, true) == 1) {
                    what_why(array, 0, k, rec);
                    wotateOwO(array, k--, j--, rec);
                }
            }
            what_why(array, 0, length, rec);
            for (int i=1; i<=order*order2*length; i++) {
                for (int j=0; j<i*order*order2*length; j++) {
                    vutuuwu(array, length, order-1, order2, rec);
                    vutuuwu(array, length, order, order2-1, rec);
                    vutuuwu(array, length-1, order, order2, rec);
                }
                int j=randInt(0,length), k=randInt(j,length);
                while (j>=0 && Reads.compareIndices(array, j, k, 1, true) == 1) {
                    what_why(array, j--, k, rec);
                }
            }
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        do {
            vutuuwu(array, length, length, length, 0);
        } while (!this.isArraySorted(array, length));
    }
}
