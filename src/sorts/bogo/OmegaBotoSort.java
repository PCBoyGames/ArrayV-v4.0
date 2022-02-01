package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Omega Botosort is coming after your family, and you soon after.
 * When you start it, you have a 5 second headstart, and if
 * you don't take it, your survival will become nothing but
 * pure luck. Omega Botosort knows where you are at all times,
 * and it's a matter of slowing the inevitable.
 *
 * Best case: O(n) ops
 * Average case: O((n*(n-1))!) ops?
 * Worst case: O(inf)
 */
public final class OmegaBotoSort extends BogoSorting {
    public OmegaBotoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Omega Boto");
        this.setRunAllSortsName("Omega Boto Sort");
        this.setRunSortName("\u03A9 Botosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(5);
        this.setBogoSort(true);
    }

    /** Pushes start to end **/
    private void omegaPush(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, end-1, start, 0.001, true, false);
        }
    }

    /** Pushes end to start **/
    private void omegaPushRev(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, start, end-1, 0.001, true, false);
        }
    }

    private void omegaSwap(int[] array, int start, int end, int depth) {
        if(start >= end)
            return;
        Writes.swaps++;
        this.omegaPush(array, start, end+1);
        this.omegaPushRev(array, start, end);
        Writes.recursion();
        this.omegaSwap(array, start+1, end-1, depth + 1);
        Writes.recursion();
        this.omegaSwap(array, start+1, end-1, depth + 1);
    }

    public void omegaOmegaReversal(int[] array, int a, int b, int depth) {
        Writes.reversals++;
        this.omegaSwap(array, a, b, depth);

        for(int i = a + 1; i < a + ((b - a + 1) / 2); i++) {
            this.omegaSwap(array, i, a + b - i, depth);
            Writes.recursion();
            omegaOmegaReversal(array, i + 1, a + b - i - 1, depth + 1);
        }
    }

    public void nOmegaReversal(int[] array, int order, int a, int b, int depth) {
        if(a > b)
            return;
        Writes.reversals++;
        if(order <= 0) {
            Writes.swap(array, a, b, 1, true, false);
            for(int i = a + 1; i < a + ((b - a + 1) / 2); i++) {
                Writes.swap(array, i, a+b-i, 1, true, false);
                Writes.recursion();
                nOmegaReversal(array, 0, i + 1, a + b - i - 1, depth + 1);
                murderCheck(array, 1, a+b-i-1, depth);
            }
        } else if(order == 1) {
            this.omegaOmegaReversal(array, a, b, depth);
            murderCheck(array, 2, b, depth);
        } else {
            Writes.recursion();
            this.nOmegaReversal(array, order-1, a, b, depth + 1);
            for(int i=1; i<(b-a+1)/2; i++)
                this.nOmegaReversal(array, order, a+i+1, b-i-1, depth + 1);
        }
    }
    private void murderCheck(int[] array, int order, int length, int depth) {
        Writes.recordDepth(depth);
        if(order == 0 || length<2)
            return;
        int r1 = randInt(0, length),
            r2 = randInt(r1, length);
        for(int i=0; i<r2-r1; i++)
            for(int j=0; j<order*(i+1); j++)
                for(int k=0; k<length*(j+1); k++) {
                    Writes.recursion();
                    this.murderCheck(array, order-1, length, depth + 1);
                    Writes.recursion();
                    this.murderCheck(array, order, length-1, depth + 1);
                }
        if(Reads.compareValues(array[r1], array[r2]) == 1) {
            this.nOmegaReversal(array, order, r1, r2, depth);
            Writes.recursion();
            this.murderCheck(array, order, length-1, depth + 1);
            Writes.recursion();
            this.murderCheck(array, order, r2-1, depth + 1);
            Writes.recursion();
            this.murderCheck(array, order, r1-1, depth + 1);
        }
        for(int i=0; i<r2-r1; i++)
            for(int j=0; j<order*(i+1); j++)
                for(int k=0; k<length*(j+1); k++) {
                    Writes.recursion();
                    this.murderCheck(array, order-1, length, depth + 1);
                    Writes.recursion();
                    this.murderCheck(array, order, length-1, depth + 1);
                }

        this.nOmegaReversal(array, order, 0, length-1, depth);
        Writes.recursion();
        this.murderCheck(array, order, length-1, depth + 1);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length))
            this.murderCheck(array, length-1, length, 0);
    }
}
