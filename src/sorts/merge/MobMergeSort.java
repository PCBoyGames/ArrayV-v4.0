package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class MobMergeSort extends Sort {
    public MobMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Mob Merge");
        this.setRunAllSortsName("Mob Merge Sort");
        this.setRunSortName("Mob Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    protected void bubble(int[] array, int start, int end) {
        for (int c = 1, j = end - 1, s, f = start; j > 0; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            boolean a = false;
            c = 1;
            for (int k = s; k < j; k++) {
                if (Reads.compareIndices(array, k, k + 1, 0.025, true) > 0) {
                    if (!a) f = k;
                    Writes.swap(array, k, k + 1, 0.075, a = true, false);
                    c = 1;
                } else c++;
            }
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = base;
        int index = 0;
        while (len <= currentLength) {
            index = 0;
            for (; index + len <= currentLength; index += len) {
                if (len == 2) {if (Reads.compareIndices(array, index, index + 1, 0.025, true) > 0) Writes.swap(array, index, index + 1, 0.075, true, false);}
                else bubble(array, index, index + len);
            }
            if (index != currentLength) bubble(array, index, currentLength);
            len *= base;
        }
        bubble(array, 0, currentLength);
    }
}