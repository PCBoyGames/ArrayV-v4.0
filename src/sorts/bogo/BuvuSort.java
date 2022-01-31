package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Buwusort rotates a random part of the array <0,rand> [very recursively] until sorted.
 */
public final class BuvuSort extends BogoSorting {
    public BuvuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buvu");
        this.setRunAllSortsName("BUvU Sort (Omega Buwusort)");
        this.setRunSortName("Buvusort");
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
    public void omega2Push(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            omegaPushBW(array, start, end);
        }
    }
    public void omega2PushBW(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            omegaPush(array, start, end);
        }
    }
    // O(2n-1 * (2^(n/2 + 1)))?
    private void omegaSwap(int[] array, int start, int end, int r) {
        if(start >= end)
            return;
        Writes.recordDepth(r++);
        this.omega2Push(array, start, end+1);
        this.omega2PushBW(array, start, end);
        this.omegaSwap(array, start+1, end-1, r);
        this.omegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaPush1(int[] array, int start, int end, int depth) { // Clamber-esque push, because I want it to be the worst possible.
        depth++;
        for(int j=end-1; j>=start; j--) {
            omegaSwap(array, j, end-1, depth);
        }
    }
    private void omegaOmegaPushBW1(int[] array, int start, int end, int depth) {
        depth++;
        for(int j=start+1; j<end; j++) {
            omegaSwap(array, start, j, depth);
        }
    }
    private void omegaOmegaPush(int[] array, int start, int end, int depth) {
        depth++;
        for(int i=start; i<end-1; i++) {
            omegaOmegaPushBW1(array, start, end, depth);
        }
    }
    private void omegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for(int i=start; i<end-1; i++) {
            omegaOmegaPush1(array, start, end, depth);
        }
    }
    private void omegaOmegaSwap(int[] array, int start, int end, int r) {
        if(start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaOmegaPush(array, start, end+1, r);
        this.omegaOmegaPushBW(array, start, end, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for(int j=start+1; j<end; j++) {
            omegaOmegaSwap(array, start, j, depth);
        }
    }
    private void what_why(int[] array, int start, int end, int d) {
        Writes.recordDepth(d++);
        int m=(end-start)/2;
        if(m==0)
            return;
        for(int i=0;i<m;i++) {
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
    private void buvuRotate(int[] array, int length, int order, int order2, int rec) {
        if(order < 1 && order2 > 0)
            buvuRotate(array, length, order2, order2-1, rec);
        if(length < 1 || order2 < 1)
            return;
        for(int j=0; j<=order2; j++)
            for(int k=0; k<order; k++)
                for(int p=0; p<length; p++)
                    buvuRotate(array, length-1, j, k, rec);
        Writes.recordDepth(rec++);
        int r = randInt(0, length+1),
            r2 = randInt(r, length+1);
        while(r2 > 0) {
            for(int h=0; h<r2; h++) {
                this.wotateOwO(array, r2, r, rec);
                for(int i=1; i<r2; i++)
                    for(int j=0; j<order2; j++)
                        for(int k=0; k<order; k++)
                            for(int o=0; o<length; o++)
                                if(Reads.compareValues(array[r], array[r2]) < 0)
                                    buvuRotate(array, length-i, j, k, rec);
                                else
                                    buvuRotate(array, length-1, order+1, order2, rec);
                    for(int l=1; l<r; l++)
                        for(int m=0; m<order2; m++)
                            for(int n=0; n<order; n++)
                                for(int p=0; p<length; p++)
                                    if(Reads.compareValues(array[m], array[n]) < 0)
                                        buvuRotate(array, length-1, m, n, rec);
                                    else
                                        buvuRotate(array, length-1, order+1, order2, rec);
            }
            if(r2 > 0)
                r2--;
            if(r > 0)
                r--;
            for(int j=0; j<=order2; j++)
                for(int k=0; k<order; k++)
                    for(int p=0; p<length; p++)
                        buvuRotate(array, length-1, j, k, rec);
        }
        for(int j=0; j<=order2; j++)
            for(int k=0; k<order; k++)
                for(int p=0; p<length; p++)
                    buvuRotate(array, length-1, j, k, rec);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length)) {
            buvuRotate(array, length, length, length, 0);
        }
    }
}
