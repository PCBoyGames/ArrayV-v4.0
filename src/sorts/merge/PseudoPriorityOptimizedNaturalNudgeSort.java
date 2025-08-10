package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PseudoPriorityOptimizedNaturalNudgeSort extends NaturalNudgeSort {

    ArrayList<Integer> indexes;

    public PseudoPriorityOptimizedNaturalNudgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pseudo-Priority Optimized Natural Nudge");
        this.setRunAllSortsName("Pseudo-Priority Optimized Natural Nudge Sort");
        this.setRunSortName("Pseudo-Priority Optimized Natural Nudgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void initPass(int[] array, int start, int end) {
        indexes.add(start);
        Writes.auxWrites++;
        int i = mergeFindRun(array, start, end);
        if (i >= end) return;
        int j = mergeFindRun(array, i, end);
        merge(array, start, i, j, 0);
        if (j >= end) return;
        int k = j;
        while (true) {
            indexes.add(k);
            Writes.auxWrites++;
            i = mergeFindRun(array, k, end);
            if (i >= end) break;
            j = mergeFindRun(array, i, end);
            merge(array, k, i, j, 0);
            if (j >= end) break;
            k = j;
        }
    }

    protected void findSmall(int[] array, int end) {
        int sel = 0;
        int size1 = indexes.get(1) - indexes.get(0);
        int size2;
        if (indexes.size() == 2) size2 = end - indexes.get(1);
        else size2 = indexes.get(2) - indexes.get(1);
        int selLenA = size1;
        int selLenB = size2;
        for (int i = 1; i + 1 < indexes.size(); i++) {
            size1 = indexes.get(i + 1) - indexes.get(i);
            if (indexes.size() == i + 2) size2 = end - indexes.get(i + 1);
            else size2 = indexes.get(i + 2) - indexes.get(i + 1);
            Reads.addComparison();
            if (size1 + size2 < selLenA + selLenB) {
                sel = i;
                selLenA = size1;
                selLenB = size2;
            }
        }
        merge(array, indexes.get(sel), indexes.get(sel) + selLenA, indexes.get(sel) + selLenA + selLenB, 0);
        indexes.remove(indexes.get(sel + 1));
        Writes.auxWrites++;
    }

    public void ppONNM(int[] array, int start, int end) {
        int extlen = 1;
        for (int ext = 2 * seglimit; ext < end - start; ext += 2 * seglimit, extlen++);
        indexes = new ArrayList<>(extlen);
        Writes.changeAllocAmount(extlen);
        initPass(array, start, end);
        while (indexes.size() > 1) findSmall(array, end);
        indexes.clear();
        Writes.changeAllocAmount(-1 * extlen);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ppONNM(array, 0, currentLength);
    }
}