package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
final public class BinomialVelvetSortStackless extends Sort {
    public BinomialVelvetSortStackless(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binomial Velvet (Stackless)");
        this.setRunAllSortsName("Stackless Binomial Velvet Sort");
        this.setRunSortName("Stackless Binomial Velvet Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Based off of Binomial Velvet, which is based off of Adaptive Velvet, which is
    // based off of Thehf Fiseg Wnida Dwoiqel (draft algorithm for The Epsilon Committee)

    // Thehf Fiseg Wnida Dwoiqel unknown (likely logbound), Adaptive Velvet likely logbound,
    // Binomial Velvet confirmed logbound, Stackless Binomial Velvet confirmed non-logbound

    // Invariant: Base children of any given root must be in order, and all leaves (in
    // order of traversal) must be in ascending index order

    // potgte
    private int pot(int v) {
        if(v < 3) return v;
        int w = 1;
        while(w < v) w *= 2;
        return w;
    }

    // stackless variant of heapify, template ripped from stackless topdown merge
    private void heap(int[] array, int a, int b) {
        // iterate through sublists using ctz method
        for(int j = 2; j <= b - a; j += 2) {
            for(int i = j, p = 1; i % 2 == 0; i >>>= 1, p *= 2) {
                int A = a + j - 2 * p, M = a + j - p;
                // both children were already rebuilt to maintain invariant:
                // invariant remains maintained on left child either way
                // invariant might have already been broken on root
                if(Reads.compareIndices(array, A, M, 1, true) > 0) {
                    int tmp = array[A];
                    Writes.write(array, A, array[M], 1, true, false);
                    sift(array, M, M, a+j, tmp, 1);
                }
            }
        }
        // find lowest set bit
        int n = b - a, j = 1;
        while(n % 2 == 0) {n >>>= 1; j *= 2;}
        int k = j;
        n >>>= 1; j *= 2;
        for(; n > 0; j *= 2, n >>>= 1)
            // iterate on set bits after lowest
            if(n % 2 == 1) {
                int A = b - k - j, M = b - k;
                if(Reads.compareIndices(array, A, M, 1, true) > 0) {
                    int tmp = array[A];
                    Writes.write(array, A, array[M], 1, true, false);
                    sift(array, M, M, b, tmp, 1);
                }
                k += j;
            }
    }

    // stackless sifting function, basic logbound-compliant
    // (centered around powers of 2, index-restrained)
    private void sift(int[] array, int a, int a1, int b, int tmp, int steps) {
        int b1 = b;
        // until minimum is hit for any root element:
        for(;;) {
            int p = pot(b - a), lp = 0, min, minp;
            // b is coerced to potgte of its length
            b = a + p;

            // find first "right child" after a1
            do {
                lp = p;
                minp = p = (p + 1) / 2;
                min = b - p;
            } while(p != lp && b - p <= a1);

            // traverse "right children", find minimum root after a1
            while(p != lp) {
                while(b - p > a1) {
                    b -= p;
                    if(b < b1 && min != b && (min >= b1 || Reads.compareIndices(array, min, b, 0.1, true) >= 0)) {
                        min = b;
                        minp = p;
                    }
                    p = (p + 1) / 2;
                }
                lp = p;
                p = (p + 1) / 2;
            }
            // push array[a1]/tmp to child whose root maintains leftmost invariant, if not minimum
            if(min < b1 && min > a1 && Reads.compareValueIndex(array, tmp, min, 1, true) > 0) {
                Writes.write(array, a1, array[min], 1, true, false);
                // iterate onto child with likely broken invariant
                a = a1 = min;
                b = Math.min(min + minp, b1);
                steps++;
            } else { // write tmp only if moves were made prior
                if(steps > 0) Writes.write(array, a1, tmp, 1, true, false);
                break;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        // build list according to invariant
        heap(array, 0, currentLength);
        for(int i=1; i<currentLength-1; i++)
            // pick lowest element
            sift(array, 0, i, currentLength, array[i], 0);
    }
}