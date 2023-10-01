package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class NaturalRandomShellMergeSort extends BogoSorting {

    public NaturalRandomShellMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Randomized Shell Merge");
        this.setRunAllSortsName("Natural Randomized Shell Merge Sort");
        this.setRunSortName("Natural Randomized Shell Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void shellPass(int[] array, int a, int b, int gap) {
        for (int i = a + gap; i < b; i++) {
            int key = array[i];
            int j = i - gap;
            while (j >= a && Reads.compareValues(key, array[j]) < 0) {
                Writes.write(array, j + gap, array[j], 0.5, true, false);
                j -= gap;
            }
            if (j + gap != i) Writes.write(array, j + gap, key, 0.5, true, false);
        }
        Highlights.clearAllMarks();
    }

    public void shellSort(int[] array, int a, int b) {
        int gap = b - a;
        while (gap != 1) {
            gap = BogoSorting.randInt((int) Math.sqrt(gap), gap > 1 ? gap : gap - 1);
            shellPass(array, a, b, gap);
        }
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b) dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else dir = true;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                break;
            i++;
        }
        if (!dir)
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                break;
            j = findRun(array, i, b);
            shellSort(array, a, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                shellSort(array, k, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
