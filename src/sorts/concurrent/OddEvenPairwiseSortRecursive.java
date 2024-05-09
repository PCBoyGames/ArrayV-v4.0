package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author PiotrGrochowski
 *
 */
public final class OddEvenPairwiseSortRecursive extends Sort {

    public OddEvenPairwiseSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Odd-Even Pairwise (Recursive)");
        this.setRunAllSortsName("Recursive Odd-Even Pairwise Sorting Network");
        this.setRunSortName("Recursive Odd-Even Pairwise Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void pairwisemerge2(int[] array, int start, int end, int gap, int depth, double sleep) {
        if (start >= end - gap) return;
        if ((end - start)/gap <= depth) return;
        this.pairwisemerge2(array, start, end, gap, 2*depth, sleep);
        this.pairwisemerge2(array, start+(gap*depth), end, gap, 2*depth, sleep);
        int a = start+(gap*depth);
        while (a < end) {
            if (Reads.compareIndices(array, a - (gap * (depth - 1)), a, sleep, true) > 0) {
                Writes.swap(array, a - (gap * (depth - 1)), a, sleep, true, false);
            }
            a += (gap * depth);
        }
    }
    
    private void pairwiserecursive2(int[] array, int start, int end, int gap, double sleep) {
        if (start == end - gap) return;
        int b = start + gap;
        while (b < end) {
            if (Reads.compareIndices(array, b - gap, b, sleep, true) > 0)
                Writes.swap(array, b - gap, b, sleep, true, false);
            b += (2 * gap);
        }
        if (((end - start) / gap) % 2 == 0) {
            this.pairwiserecursive2(array, start, end, gap * 2, sleep);
            this.pairwiserecursive2(array, start + gap, end + gap, gap * 2, sleep);
        } else {
            this.pairwiserecursive2(array, start, end + gap, gap * 2, sleep);
            this.pairwiserecursive2(array, start + gap, end, gap * 2, sleep);
        }
        this.pairwisemerge2(array, start, end, gap, 2, sleep);
    }
    
    public void customSort(int[] array, int start, int end) {
        this.pairwiserecursive2(array, start, end, 1, 0.5);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.pairwiserecursive2(array, 0, length, 1, 0.5);

    }

}
