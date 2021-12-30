package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Kiriko-chan
in collaboraation with PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author PCBoy
 *
 */
public final class RandomShellMergeSort extends BogoSorting {

    public RandomShellMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Randomized Shell Merge");
        this.setRunAllSortsName("Randomized Shell Merge Sort");
        this.setRunSortName("Random Shell Mergesort");
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
            boolean change = false;
            while (j >= a && Reads.compareValues(key, array[j]) < 0) {
                Writes.write(array, j + gap, array[j], 0.5, true, false);
                j -= gap;
                change = true;
            }
            if (change)
                Writes.write(array, j + gap, key, 0.5, true, false);
        }
        Highlights.clearAllMarks();
    }
    
    public void shellSort(int[] array, int a, int b) {
        int gap = b - a;
        while(gap != 1) {
            gap = BogoSorting.randInt((int) Math.sqrt(gap), gap > 1 ? gap : gap - 1);
            shellPass(array, a, b, gap);
        }
    }
    
    public void mergeSort(int[] array, int a, int b) {
        InsertionSort smallSort = new InsertionSort(arrayVisualizer);
        int mRun = b - a;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        int i;
        for(i = a; i+mRun<b; i+=mRun)
            smallSort.customInsertSort(array, i, i + mRun, 0.5, false);
        smallSort.customInsertSort(array, i, b, 0.5, false);
        for (int j = mRun; j < (b - a); j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                shellSort(array, i, i + 2 * j);
            if (i + j < b)
                shellSort(array, i, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
