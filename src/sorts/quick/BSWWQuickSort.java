package sorts.quick;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BSWWQuickSort extends MadhouseTools {

    ArrayList<int[]> splits = new ArrayList<>();

    public BSWWQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Binary Search Won't Work");
        this.setRunAllSortsName("\"Binary Search Won't Work\" Sort");
        this.setRunSortName("\"Binary Search Won't Work\" Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int tryBinarySearchAnyway(int[] array, int start, int end) {
        int min = start;
        int max = start;
        for (int i = start + 1; i < end; i++) {
            int cmp = Reads.compareValues(array[i], array[min]);
            if (cmp < 0) min = i;
            else if (cmp > 0) {
                cmp = Reads.compareValues(array[i], array[max]);
                if (cmp > 0) max = i;
            }
        }
        int a = stableReturn(array[min]);
        int b = stableReturn(array[max]);
        int sleuth = a + (b - a) / 2;
        arrayVisualizer.setExtraHeading(" / BSWW EXPECTS: " + sleuth);
        return binarySearch(array, start, end - 1, sleuth, true, 10, true);
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

    public void binarySearchWontWork(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        int[] part = new int[2];
        while (end - start >= 16) {
            if (!isSorted(array, start, end)) {
                int pivot = stableReturn(array[tryBinarySearchAnyway(array, start, end)]);
                arrayVisualizer.setExtraHeading(arrayVisualizer.getExtraHeading() + ", GETS: " + pivot);
                part = partition(array, start, end, pivot);
                splits.add(new int[] {start, part[0], part[1], end});
                Writes.recursion();
                binarySearchWontWork(array, part[1], end, depth + 1);
                end = part[0];
            } else break;
        }
        InsertionSort insertionSort = new InsertionSort(arrayVisualizer);
        insertionSort.customInsertSort(array, start, end, 1, false);
    }

    protected void calcEffective() {
        double effective = 0;
        for (int i = 0; i < splits.size(); i++) {
            int[] entry = splits.get(i);
            if (entry[1] != entry[0] && entry[3] != entry[2]) effective += Math.min(
                (1.0 * (entry[1] - entry[0])) / (1.0 * (entry[3] - entry[2])),
                (1.0 * (entry[3] - entry[2])) / (1.0 * (entry[1] - entry[0]))
            );
        }
        effective /= splits.size();
        Highlights.clearAllMarks();
        arrayVisualizer.setExtraHeading(" / EFFECTIVE: " + (100 * effective) + "%");
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        splits.clear();
        binarySearchWontWork(array, 0, currentLength, 0);
        calcEffective();
    }
}