package sorts.hybrid;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BismergeSort extends BismuthSort {
    public BismergeSort (ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bismerge [WIP]");
        this.setRunAllSortsName("Bismerge Sort");
        this.setRunSortName("Bismerge Sort");
    }

    public void bisMerge(int[] array, int start, int end, int bSet) {
        Highlights.retainColorMarks(true);
        int currentLength = end - start, i = start, bSize = bSet == 0 ? (int) Math.sqrt(end - start) : bSet, len = bSize * 2;
        for (; i + bSize - 1 < end; i += bSize) insertions(array, i, i + bSize);
        for (; len < 2 * currentLength; len *= 2) {
            int index = start;
            for (; index + len - 1 < end; index += len) {
                blockSelect(array, index, index + len, index + (len / 2), bSize);
                for (int j = index == start ? index + bSize : index, p = j; j + 2 * bSize <= index + len; j += bSize) if (j + bSize >= p) p = twoBlocks(array, j, j + bSize, j + 2 * bSize, start, index + len, bSize, true, 0);
            }
            if (index + (len / 2) < i) {
                blockSelect(array, index, i, index + (len / 2), bSize);
                for (int j = index == start ? index + bSize : index, p = j; j + 2 * bSize <= i; j += bSize) if (j + bSize >= p) p = twoBlocks(array, j, j + bSize, j + 2 * bSize, start, i, bSize, true, 0);
            }
            giveUpFair(array, start, start + bSize, Math.min(start + len, i), true, true, true, 0);
        }
        if (i < end) giveUpFair(array, start, i, end, false, true, true, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        arrayVisualizer.setExtraHeading(" / bSize = " + getSize);
        bisMerge(array, 0, currentLength, getSize);
    }
}