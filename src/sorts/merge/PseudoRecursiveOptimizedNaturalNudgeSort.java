package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PseudoRecursiveOptimizedNaturalNudgeSort extends NaturalNudgeSort {

    ArrayList<int[]> offering;
    int offeringIndex;

    public PseudoRecursiveOptimizedNaturalNudgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pseudo-Recursive Optimized Natural Nudge");
        this.setRunAllSortsName("Pseudo-Recursive Optimized Natural Nudge Sort");
        this.setRunSortName("Pseudo-Recursive Optimized Natural Nudgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    protected void offer(int[] array, int[] gives) {
        Writes.auxWrites++;
        offering.add(gives);
        if (offeringIndex > 0) {
            while (offering.get(offeringIndex - 1)[2] == offering.get(offeringIndex)[2]) {
                int[] a = offering.get(offeringIndex - 1);
                int[] b = offering.get(offeringIndex);
                merge(array, a[0], a[1], b[1], 0);
                int[] reset = {a[0], b[1], a[2] + 1};
                Writes.auxWrites++;
                offering.set(offeringIndex - 1, reset);
                Writes.auxWrites++;
                offering.remove(offeringIndex);
                offeringIndex = offering.size() - 1;
                if (offeringIndex == 0) break;
            }
        }
        offeringIndex = offering.size();
    }

    protected void collapse(int[] array) {
        while (offering.size() > 1) {
            int[] a = offering.get(offering.size() - 2);
            int[] b = offering.get(offering.size() - 1);
            merge(array, a[0], a[1], b[1], 0);
            int[] reset = {a[0], b[1], a[2] + 1};
            Writes.auxWrites++;
            offering.set(offering.size() - 2, reset);
            Writes.auxWrites++;
            offering.remove(offering.size() - 1);
        }
    }

    protected void init(int[] array, int start, int end) {
        for (int i = start, j = start; i < end; i = j) {
            j = mergeFindRun(array, i, end);
            offer(array, new int[] {i, j, 0});
        }
        collapse(array);
    }

    public void prONNM(int[] array, int start, int end) {
        offeringIndex = 0;
        Writes.changeAllocAmount(log2(end - start));
        offering = new ArrayList<int[]>(log2(end - start));
        offering.clear();
        init(array, start, end);
        Writes.changeAllocAmount(-1 * log2(end - start));
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        prONNM(array, 0, currentLength);
    }
}