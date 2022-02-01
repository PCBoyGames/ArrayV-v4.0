package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedWhySort extends Sort {
    public OptimizedWhySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Why");
        this.setRunAllSortsName("Optimized Why Sort");
        this.setRunSortName("Optimized Whysort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int start, int end, int dir) {
        int i = start;
        int j = start;
        int lastswap = end - 1;
        while (lastswap >= start + 1) {
            i = start;
            j = start;
            while (i <= lastswap) {
                if (Reads.compareIndices(array, i - 1, i, 0.001, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.001, true, false);
                    j = i;
                }
                i++;
            }
            lastswap = j;
        }
    }

    protected void pop19(int[] array, int start, int end, int dir) {
        bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
        bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
        bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
        bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
        bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
        bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
        bubble(array, start, end, dir);
    }

    protected void pop18(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop19(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop19(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop19(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop19(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop19(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop19(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop19(array, start, end, dir);
        }
    }

    protected void pop17(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop18(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop18(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop18(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop18(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop18(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop18(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop18(array, start, end, dir);
        }
    }

    protected void pop16(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop17(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop17(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop17(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop17(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop17(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop17(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop17(array, start, end, dir);
        }
    }

    protected void pop15(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop16(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop16(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop16(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop16(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop16(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop16(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop16(array, start, end, dir);
        }
    }

    protected void pop14(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop15(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop15(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop15(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop15(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop15(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop15(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop15(array, start, end, dir);
        }
    }

    protected void pop13(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop14(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop14(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop14(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop14(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop14(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop14(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop14(array, start, end, dir);
        }
    }

    protected void pop12(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop13(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop13(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop13(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop13(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop13(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop13(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop13(array, start, end, dir);
        }
    }

    protected void pop11(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop12(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop12(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop12(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop12(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop12(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop12(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop12(array, start, end, dir);
        }
    }

    protected void pop10(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop11(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop11(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop11(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop11(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop11(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop11(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop11(array, start, end, dir);
        }
    }

    protected void pop9(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop10(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop10(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop10(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop10(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop10(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop10(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop10(array, start, end, dir);
        }
    }

    protected void pop8(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop9(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop9(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop9(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop9(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop9(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop9(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop9(array, start, end, dir);
        }
    }

    protected void pop7(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop8(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop8(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop8(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop8(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop8(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop8(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop8(array, start, end, dir);
        }
    }

    protected void pop6(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop7(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop7(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop7(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop7(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop7(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop7(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop7(array, start, end, dir);
        }
    }

    protected void pop5(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop6(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop6(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop6(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop6(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop6(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop6(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop6(array, start, end, dir);
        }
    }

    protected void pop4(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop5(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop5(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop5(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop5(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop5(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop5(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop5(array, start, end, dir);
        }
    }

    protected void pop3(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop4(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop4(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop4(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop4(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop4(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop4(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop4(array, start, end, dir);
        }
    }

    protected void pop2(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop3(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop3(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop3(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop3(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop3(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop3(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop3(array, start, end, dir);
        }
    }

    protected void pop(int[] array, int start, int end, int dir) {
        if (end - start < 4) {
            bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            bubble(array, start, end, dir);
        } else {
            pop2(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
            pop2(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
            pop2(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
            pop2(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
            pop2(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
            pop2(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
            pop2(array, start, end, dir);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (currentLength < 4) {
            bubble(array, 1, (int) Math.floor((currentLength + 1) / 4), -1);
            bubble(array, (int) Math.floor((currentLength + 1) / 4) + 1, (int) Math.floor((currentLength + 1) / 2), 1);
            bubble(array, (int) Math.floor((currentLength + 1) / 2) + 1, (int) Math.floor (((currentLength + 1) * 3) / 4), -1);
            bubble(array, (int) Math.floor(((currentLength + 1) * 3) / 4) + 1, currentLength, 1);
            bubble(array, 1, (int) Math.floor((currentLength + 1) / 2), -1);
            bubble(array, (int) Math.floor((currentLength + 1) / 2) + 1, currentLength, 1);
            bubble(array, 1, currentLength, 1);
        } else {
            pop(array, 1, (int) Math.floor((currentLength + 1) / 4), -1);
            pop(array, (int) Math.floor((currentLength + 1) / 4) + 1, (int) Math.floor((currentLength + 1) / 2), 1);
            pop(array, (int) Math.floor((currentLength + 1) / 2) + 1, (int) Math.floor (((currentLength + 1) * 3) / 4), -1);
            pop(array, (int) Math.floor(((currentLength + 1) * 3) / 4) + 1, currentLength, 1);
            pop(array, 1, (int) Math.floor((currentLength + 1) / 2), -1);
            pop(array, (int) Math.floor((currentLength + 1) / 2) + 1, currentLength, 1);
            pop(array, 1, currentLength, 1);
        }
    }
}