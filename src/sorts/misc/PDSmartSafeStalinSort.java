package sorts.misc;

import main.ArrayVisualizer;
import sorts.merge.NaturalMergeSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV THEN IMPROVED BY PCBOYGAMES
BASED IN PART ON CODE FROM GEEKSFORGEEKS

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDSmartSafeStalinSort extends Sort {

    int segmentcount;

    public PDSmartSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pattern-Defeating Smart Safe Stalin");
        this.setRunAllSortsName("Pattern-Defeating Smart Safe Stalin Sort");
        this.setRunSortName("Pattern-Defeating Smart Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b) dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else dir = true;
        if (dir) while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0) i++;
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected int stepDown(int[] array, int end) {
        for (int i = end - 1; i > 0; i--) for (int j = i - 1; j >= 0; j--) if (Reads.compareIndices(array, j, i, 0.025, true) > 0) return i + 1;
        return 0;
    }

    protected void checkSegments(int[] array, int end) {
        segmentcount = 0;
        for (int i = 0; i + 1 < end && segmentcount < 2; i++) if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) segmentcount++;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength)) return;
        boolean pass = false;
        currentLength = stepDown(array, currentLength);
        while (!pass) {
            pass = true;
            // And here begins the code I stole from Strand Sort.
            if (currentLength > 0) {
                int[] subList = Writes.createExternalArray(currentLength);
                int j = currentLength, k = j;
                Writes.write(subList, 0, array[0], 0.25, true, true);
                k--;
                for (int i = 0, p = 0, m = 1; m < j; m++) {
                    Highlights.markArray(1, m);
                    if (Reads.compareValues(array[m], subList[i]) >= 0) {
                        Writes.write(subList, ++i, array[m], 0.5, false, true);
                        k--;
                    }
                    else {
                        Highlights.markArray(2, p);
                        if (p != m) {
                            Writes.write(array, p++, array[m], 0.5, false, false);
                        } else p++;
                        pass = false;
                    }
                }
                Highlights.clearAllMarks();
                Writes.arraycopy(subList, 0, array, k, currentLength - k, 0.5, true, false);
                Writes.deleteExternalArray(subList);
            }
            checkSegments(array, currentLength);
            if (segmentcount < 2) break;
            currentLength--;
            if (currentLength - 1 > 0) {
                int cmp = Reads.compareIndices(array, currentLength - 1, currentLength, 1, true);
                while (cmp == 0 && currentLength - 1 >= 0) {
                    currentLength--;
                    if (currentLength - 1 >= 0) cmp = Reads.compareIndices(array, currentLength - 1, currentLength, 4, true);
                }
            }
            currentLength = stepDown(array, currentLength);
        }
        if (segmentcount < 2) {
            NaturalMergeSort merge = new NaturalMergeSort(arrayVisualizer);
            merge.runSort(array, currentLength, 0);
        }
    }
}