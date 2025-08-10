package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ThreeThreeSort extends MadhouseTools {

    int tT_Level = 3;

    public ThreeThreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Three-Three");
        this.setRunAllSortsName("Three-Three Sort");
        this.setRunSortName("Three-Three Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void foldThrees(int[] array, int start, int easyEnd) {
        int end = easyEnd;
        InPlaceUnstableSergioSort sergioSort = new InPlaceUnstableSergioSort(arrayVisualizer);
        int s = array[start];
        while (end - start > tT_Level) {
            int fold = start;
            for (int i = start; i + tT_Level - 1 < end; i += tT_Level) {
                sergioSort.runSergioSort(array, i, tT_Level);
                safeSwap(array, i + (tT_Level / 2), fold, 0.1, true, false);
                fold++;
            }
            end = fold;
        }
        int times = 1;
        while (Reads.compareValues(array[start], s) != 0 || times < 2) {
            s = array[start];
            end = easyEnd;
            while (end - start > tT_Level) {
                int fold = start;
                for (int i = start; i + tT_Level - 1 < end; i += tT_Level) {
                    sergioSort.runSergioSort(array, i, tT_Level);
                    safeSwap(array, i + (tT_Level / 2), fold, 0.1, true, false);
                    fold++;
                }
                end = fold;
            }
            times++;
        }
    }

    protected int partition(int[] array, int start, int end, int pivot) {
        int left = start - 1;
        int right = end;
        while (true) {
            while (++left < right) {
                Highlights.markArray(1, left);
                Delays.sleep(1);
                if (Reads.compareValues(array[left], pivot) > 0) break;
            }
            while (--right > left) {
                Highlights.markArray(2, right);
                Delays.sleep(1);
                if (Reads.compareValues(array[right], pivot) <= 0) break;
            }
            if (left < right) Writes.swap(array, left, right, 1, true, false);
            else break;
        }
        return left;
    }

    public void threeThree(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        int part = end;
        while (true) {
            end = part;
            if (end - start < 15) {
                InsertionSort insertionSort = new InsertionSort(arrayVisualizer);
                insertionSort.customInsertSort(array, start, end, 1, false);
                break;
            } else if (!isSorted(array, start, end)) {
                foldThrees(array, start, end);
                int pivot = array[start];
                int partB = part;
                part = partition(array, start, end, pivot);
                if (partB == part || partB == start) {
                    InPlaceUnstableSergioSort sergioSort = new InPlaceUnstableSergioSort(arrayVisualizer);
                    sergioSort.runSergioSort(array, start, end - start);
                    return;
                }
                Writes.recursion();
                threeThree(array, part, end, depth + 1);
            } else break;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        threeThree(array, 0, currentLength, 0);
    }
}