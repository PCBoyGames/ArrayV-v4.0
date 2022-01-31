package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Buwusort rotates a random part of the array <0,rand> [very recursively] until sorted.
 */
public final class BuwuSort extends BogoSorting {
    public BuwuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buwu");
        this.setRunAllSortsName("BUwU Sort (Omega Bowosort)");
        this.setRunSortName("Buwusort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
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
    private void omegaSwap(int[] array, int start, int end, int r) {
        if(start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaPush(array, start, end+1);
        this.omegaPushBW(array, start, end);
        Writes.recursion();
        this.omegaSwap(array, start+1, end-1, r);
        Writes.recursion();
        this.omegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaPush(int[] array, int start, int end, int depth) { // Clamber-esque push, because I want it to be the worst possible.
        for(int j=start; j<end; j++) {
            omegaSwap(array, j, end, depth);
        }
    }
    private void cursedReversal(int[] array, int start, int end, int d) {
        for(int i=start; i<=end; i++) {
            omegaOmegaPush(array, start, i, d);
        }
    }
    private void wotateOwO(int[] array, int range, int amount, int d) {
        this.cursedReversal(array, 0, range-amount, d);
        this.cursedReversal(array, range-amount, range, d);
        this.cursedReversal(array, 0, range, d);
    }
    private void buwuRotate(int[] array, int length, int rec) {
        if(length < 1)
            return;
        Writes.recordDepth(rec++);
        int r = randInt(0, length),
            r2 = randInt(r, length);
        for(int i=0; i<r; i++) {
            this.wotateOwO(array, r2, r, rec);
            if(r != r2 && Reads.compareValues(array[r], array[r2]) == 1) {
                Writes.recursion();
                this.buwuRotate(array, r-i, rec);
            } else {
                Writes.recursion();
                this.buwuRotate(array, r2-i,rec);
            }
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length)) {
            buwuRotate(array, length, 0);
        }
    }
}
