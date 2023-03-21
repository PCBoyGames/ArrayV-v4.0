package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
final public class BinomialVelvetSort extends Sort {
    public BinomialVelvetSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binomial Velvet");
        this.setRunAllSortsName("Binomial Velvet Sort");
        this.setRunSortName("Binomial Velvet Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Based off of Adaptive Velvet, which is based off of Thehf Fiseg Wnida Dwoiqel
    // (draft algorithm for The Epsilon Committee)

    // Invariant: Base children of any given root must be in order, and all leaves (in
    // order of traversal) must be in ascending index order

    private void heap(int[] array, int a, int b) {
        int m=a+(b-a)/2;
        if (a < m) {
            // rebuild base children using invariant
            heap(array, a, m);
            heap(array, m, b);

            // invariant remains maintained on left child either way
            // invariant might have already been broken on root
            if (Reads.compareIndices(array, a, m, 1, true) > 0) {
                int z = array[a];
                Writes.write(array, a, array[m], 1, true, false);
                // maintain invariant with right child of root
                sift(array, m, m, b, z, 1);
            }
        }
    }

    private void sift(int[] array, int a, int a1, int b, int tmp, int steps) {
        int p = b - a, lp, min, minp;

        // find first "right child" after a1
        do {
            lp = p;
            minp = p = (p + 1) / 2;
            min = b - p;
        } while (p != lp && b - p <= a1);

        // traverse "right children", find minimum root after a1
        while (p != lp) {
            while (b - p > a1) {
                b -= p;
                if (min != b && Reads.compareIndices(array, min, b, 0.1, true) >= 0) {
                    min = b;
                    minp = p;
                }
                p = (p + 1) / 2;
            }
            lp = p;
            p = (p + 1) / 2;
        }
        // push array[a1]/tmp to leaf that maintains invariant
        if (min > a1 && Reads.compareValueIndex(array, tmp, min, 1, true) > 0) {
            Writes.write(array, a1, array[min], 1, true, false);
            // iterate onto next subsection with likely broken invariant
            sift(array, min, min, min+minp, tmp, ++steps);
        } else // write tmp only if moves were made prior
            if (steps > 0) Writes.write(array, a1, tmp, 1, true, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        heap(array, 0, currentLength);
        for (int i=1; i<currentLength-1; i++)
            sift(array, 0, i, currentLength, array[i], 0);
    }
}