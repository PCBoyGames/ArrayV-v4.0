package sorts.merge;

import main.ArrayVisualizer;
import sorts.hybrid.NaBismuthSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NaturalNudgeSort extends MadhouseTools {

    int seglimit = 16;
    NaBismuthSort bismuth = new NaBismuthSort(arrayVisualizer);

    public NaturalNudgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Nudge");
        this.setRunAllSortsName("Natural Nudge Sort");
        this.setRunSortName("Natural Nudgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int mergeFindRun(int[] array, int a, int b) {
        int i = findRun(array, a, b, 0.5, true, false);
        int j;
        for (; i < a + seglimit && i < b; i = j) {
            j = findRun(array, i, b, 0.5, true, false);
            grailMergeWithoutBuffer(array, a, i - a, j - i);
        }
        return i;
    }

    protected void merge(int[] array, int a, int m, int b, int d) {
        bismuth.colors = false;
        bismuth.giveUpFair(array, a, m, b, true, true, false, d);
    }

    protected int findRun(int[] array, int p1, int p2) {
        if (p1 == p2) return p2;
        return findRun(array, p1, p2, 0.5, true, false);
    }

    public void nudgeMerge(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b) return;
            j = findRun(array, i, b);
            merge(array, a, i, j, 0);
            if (j >= b) return;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b) break;
                j = findRun(array, i, b);
                merge(array, k, i, j, 0);
                if (j >= b) break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        nudgeMerge(array, 0, currentLength);
    }
}