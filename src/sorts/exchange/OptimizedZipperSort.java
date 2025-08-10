package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedZipperSort extends MadhouseTools {
    public OptimizedZipperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Zipper");
        this.setRunAllSortsName("Optimized Zipper Sort");
        this.setRunSortName("Optimized Zippersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    protected void ending(int[] array, int currentLength) {
        if (findRun(array, 0, currentLength, 0.1, true, false) >= currentLength) return;
        Highlights.clearAllMarks();
        for (int gap = currentLength; gap >= 1; ) {
            for (int h = gap, i = h; i < currentLength; i++) {
                int v = array[i], j = i;
                boolean w = false;
                for (; j >= h && j - h >= 0 && Reads.compareValues(array[j - h], v) > 0; j -= h) Writes.write(array, j, array[j - h], 1, w = true, false);
                if (w) Writes.write(array, j, v, 1, true, false);
            }
            if (gap == 1) break;
            int newG = (int) Math.max(1, gap / 2.3601);
            while (newG > 1 && !coprime(gap, newG)) newG--;
            gap = newG;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength, false, 0.1, true, false)) return;
        int gap = currentLength;
        int first = 0;
        while (gap > Math.max(2 * log2(currentLength), Math.sqrt(currentLength))) {
            gap = 1;
            int i = first > 1 ? first - 1 : 0;
            while (i + gap < currentLength) {
                if (Reads.compareIndices(array, i, i + gap, 0.05, true) > 0) {
                    Writes.swap(array, i, i + gap, 0.1, true, false);
                    if (gap == 1) first = i;
                    gap++;
                } else i++;
            }
        }
        if (gap != 1) ending(array, currentLength);
    }
}