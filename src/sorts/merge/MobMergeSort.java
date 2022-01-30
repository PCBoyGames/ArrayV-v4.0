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
    }
    
    protected void bubble(int[] array, int start, int end) {
        int c = 1;
        int s;
        int f = start + ((end - start) / 2);
        boolean a = false;
        for (int j = end - 1; j > 0; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    if (!a) f = i;
                    a = true;
                    c = 1;
                } else c++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        int index = 0;
        while (len <= currentLength) {
            index = 0;
            while (index + len <= currentLength) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, 0.025, true) > 0) Writes.swap(array, index, index + 1, 0.075, true, false);
                } else bubble(array, index, index + len);
                index += len;
            }
            if (index != currentLength) bubble(array, index, currentLength);
            len *= 2;
        }
        bubble(array, 0, currentLength);
    }
}