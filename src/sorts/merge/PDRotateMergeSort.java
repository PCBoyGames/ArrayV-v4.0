package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Kiriko-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author aphitorite
 * @author Gaming32
 *
 */
public class PDRotateMergeSort extends Sort {

    public PDRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Pattern Defeating Rotate Merge");
        setRunAllSortsName("Pattern-Defeating Rotate Merge Sort");
        setRunSortName("Pattern-Defeating Rotate Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected int runCount;

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

    protected int binarySearch(int[] array, int a, int b, int value, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;

            boolean comp = left
                         ? Reads.compareValues(value, array[m]) <= 0
                         : Reads.compareValues(value, array[m]) < 0;

            if (comp)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    public void merge(int[] array, int a, int m, int b) {
        int m1, m2, m3;

        if (m - a >= b - m) {
            m1 = a + (m - a) / 2;
            m2 = binarySearch(array, m, b, array[m1], true);
            m3 = m1 + (m2 - m);
        } else {
            m2 = m + (b - m) / 2;
            m1 = binarySearch(array, a, m, array[m2], false);
            m3 = (m2++) - (m - m1);
        }
        rotate(array, m1, m, m2);

        if (m2 - (m3 + 1) > 0 && b - m2 > 0)
            merge(array, m3 + 1, m2, b);
        if (m1 - a > 0 && m3 - m1 > 0)
            merge(array, a, m1, m3);
    }

    protected boolean compare(int a, int b) {
        return Reads.compareValues(a, b) <= 0;
    }

    protected int identifyRun(int[] array, int index, int maxIndex) {
        int startIndex = index;

        Highlights.markArray(1, index);
        if (index >= maxIndex)
            return -1;

        boolean cmp = compare(array[index], array[index + 1]);
        index++;
        Highlights.markArray(1, index);

        while (index < maxIndex) {
            Delays.sleep(1);
            boolean checkCmp = compare(array[index], array[index + 1]);
            if (checkCmp != cmp)
                break;
            index++;
            Highlights.markArray(1, index);
        }
        Delays.sleep(1);

        if (!cmp) {
            // arrayVisualizer.setHeading("PDMerge -- Reversing Run");
            Writes.reversal(array, startIndex, index, 1, true, false);
            Highlights.clearMark(2);
            // arrayVisualizer.setHeading("PDMerge -- Finding Runs");
        }
        if (index >= maxIndex)
            return -1;
        return index + 1;
    }

    protected int[] findRuns(int[] array, int maxIndex) {
        int[] runs = Writes.createExternalArray(maxIndex / 2 + 2);
        runCount = 0;

        int lastRun = 0;
        while (lastRun != -1) {
            Writes.write(runs, runCount++, lastRun, 0.5, true, true);
            int newRun = identifyRun(array, lastRun, maxIndex);
            lastRun = newRun;
        }

        return runs;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] runs = findRuns(array, length - 1);
        while (runCount > 1) {
            for (int i = 0; i < runCount - 1; i += 2) {
                int end = i + 2 >= runCount ? length : (runs[i + 2]);
                merge(array, runs[i], runs[i + 1], end);
            }
            for (int i = 1, j = 2; i < runCount; i++, j += 2, runCount--)
                Writes.write(runs, i, runs[j], 0.5, true, true);
        }
        Writes.deleteExternalArray(runs);

    }

}
