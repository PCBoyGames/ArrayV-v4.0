package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ZipperSort extends Sort {
    public ZipperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Zipper");
        this.setRunAllSortsName("Zipper Sort");
        this.setRunSortName("Zippersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        int gap = 2;
        int first = 0;
        while (gap > 1) {
            gap = 1;
            i = first > 1 ? first - 1 : 0;
            while (i + gap < currentLength) {
                if (Reads.compareIndices(array, i, i + gap, 0.05, true) > 0) {
                    Writes.swap(array, i, i + gap, 0.1, true, false);
                    if (gap == 1) first = i;
                    gap++;
                } else i++;
            }
        }
    }
}