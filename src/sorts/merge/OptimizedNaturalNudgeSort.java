package sorts.merge;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedNaturalNudgeSort extends NaturalNudgeSort {

    protected int insertlimit = 8;
    int seglimit = 16;

    public OptimizedNaturalNudgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Natural Nudge");
        this.setRunAllSortsName("Optimized Natural Nudge Sort");
        this.setRunSortName("Optimized Natural Nudgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean threeResolves(int x) {
        int y = 1;
        while (true) {
            int z = (int) (Math.pow(2, y) + 1);
            if (z > x) return false;
            else if (z == x) return true;
            y++;
        }
    }

    protected void attemptSmall(int[] array, int[] sizes, int runs) {
        int min2 = 0;
        for (int i = 0; i + 1 < runs; i++) if (sizes[i] + sizes [i + 1] < sizes[min2] + sizes[min2 + 1]) min2 = i;
        int sum = 0;
        for (int i = 0; i < min2; i++) sum += sizes[i];
        merge(array, sum, sum + sizes[min2], sum + sizes[min2] + sizes[min2 + 1], 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i, j, k;
        int extlen = 1;
        for (int ext = 2 * seglimit; ext < currentLength; ext += 2 * seglimit, extlen++);
        int[] sizes = Writes.createExternalArray(extlen);
        while (true) {
            int l = 0;
            i = mergeFindRun(array, 0, currentLength);
            if (i >= currentLength) break;
            j = mergeFindRun(array, i, currentLength);
            merge(array, 0, i, j, 0);
            Writes.write(sizes, l++, j, 0.1, true, true);
            Highlights.clearMark(2);
            if (j >= currentLength) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, currentLength);
                if (i >= currentLength) {
                    int sum = 0;
                    for (int m = 0; m < l; m++) sum += sizes[m];
                    Writes.write(sizes, l++, currentLength - sum, 0.1, true, true);
                    break;
                }
                j = mergeFindRun(array, i, currentLength);
                merge(array, k, i, j, 0);
                int sum = 0;
                for (int m = 0; m < l; m++) sum += sizes[m];
                Writes.write(sizes, l++, j - sum, 0.1, true, true);
                if (j >= currentLength) break;
                k = j;
            }
            attemptSmall(array, sizes, l);
            if (l <= 2) break;
        }
        Writes.deleteExternalArray(sizes);
    }
}