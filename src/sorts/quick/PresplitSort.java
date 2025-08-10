package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Basically Quick with middle bias, then recur into the halves.

*/
public class PresplitSort extends MadhouseTools {
    public PresplitSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Presplit");
        this.setRunAllSortsName("Presplit Sort");
        this.setRunSortName("Presplit Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        int i1 = a, i = a-1, j = b, j1 = b;
        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) safeSwap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) safeSwap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                if (i1 == b) return new int[] {a, b};
                else if (j < i) j++;
                while (i1 > a) safeSwap(array, --i, --i1, 1, true, false);
                while (j1 < b) safeSwap(array, j++, j1++, 1, true, false);
                break;
            }
        }
        return new int[] {i, j};
    }

    public void presplit(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        int[] part = new int[2];
        while (end - start >= 16) {
            int sq = (int) Math.sqrt(end - start);
            int mod = sq / 2;
            if (!isSorted(array, start, end)) {
                int sSave = start;
                int eSave = end;
                boolean split = false;
                while (!split) {
                    boolean mDir = false;
                    if (end - start < 2) split = true;
                    if (!split) {
                        int pivot = array[start + ((mod + 1) * (end - start) / (sq + 1))];
                        part = partition(array, start, end, pivot);
                        if (part[0] > (sSave + (eSave - sSave) / 2)) {
                            end = part[0];
                            mDir = true;
                        } else if (part[1] < (sSave + (eSave - sSave) / 2)) start = part[1];
                        else split = true;
                    }
                    sq = (int) Math.sqrt(end - start);
                    mod = mDir ? (mod - 1) % sq : (mod + 1) % sq;
                    if (!split && isSorted(array, start, end)) split = true;
                }
                start = sSave;
                end = eSave;
                Writes.recursion();
                presplit(array, start, start + (end - start) / 2, depth + 1);
                start = start + (end - start) / 2;
            } else return;
        }
        InsertionSort insertionSort = new InsertionSort(arrayVisualizer);
        insertionSort.customInsertSort(array, start, end, 1, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        presplit(array, 0, currentLength, 0);
    }
}