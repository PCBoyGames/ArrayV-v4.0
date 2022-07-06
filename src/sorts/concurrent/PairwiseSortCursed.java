package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PairwiseSortCursed extends Sort {
    public PairwiseSortCursed(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pairwise (Cursed)");
        this.setRunAllSortsName("Cursed Pairwise Sorting Network");
        this.setRunSortName("Cursed Pairwise Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int N;

    private void compSwap(int[] array, int a, int b) {
        if (b < N && Reads.compareIndices(array, a, b, 1, true) > 0)
            Writes.swap(array, a, b, 0, false, false);
    }

    private void Pw(int[] array, int a, int b, int g) {
        // bounds check
        if (a >= b - g) return;

        // split list into pairs
        for (int i = a; i <= b - 2 * g; i += 2 * g) {
            compSwap(array, i, i + g);
        }

        // iterate on each weaved half of the list with pairwise-merge
        Pdw(array, a, b, 2 * g);
        Pdw(array, a + g, b + g, 2 * g);

        // deweave halves iteratively
        int p = 1;
        while (p < (b - a) / g) p |= p << 1;
        while (p > 0) {
            for (int i = a + g; i + p * g < b; i += 2 * g)
                compSwap(array, i, i+p*g);
            p >>= 1;
        }
    }

    private void Pdw(int[] array, int a, int b, int g) {
        // bounds check
        if (a >= b - g) return;

        int n = (b - a) / g, m = n / 2, o = m;
        m = a + m * g;

        // imperfectly halve the list
        for (int i = a; i < m; i += g) {
            compSwap(array, i, m + i - a);
        }

        // iterate on halves with pairwise
        Pw(array, a, m, g);
        Pw(array, m, b, g);

        // deweave halves iteratively
        for (int i = n >> 2; i > 0; i >>= 1) {
            for (int j = i; j < (o - i) << 1; j += i << 1) {
                for (int k = 0; k < i; k++)
                    compSwap(array, a+(j+k)*g, a+(i+j+k)*g);
            }
        }
    }

    public void pairwise(int[] array, int a, int b) {
        int n = 1;
        while (n < b - a) n *= 2;
        N = b;
        Pdw(array, a, a + n, 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.pairwise(array, 0, sortLength);
    }
}