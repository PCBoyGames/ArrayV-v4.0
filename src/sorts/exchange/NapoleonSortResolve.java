package sorts.exchange;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeonLessInsert;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES
FIXING ORIGINAL CODE BY ANONYMOUS0726

------------------------------
- THIS WAS ALREADY IN ARRAYV -
------------------------------

*/
final public class NapoleonSortResolve extends Sort {
    
    int oob = 0;
    
    public NapoleonSortResolve(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Napoleon (Resolved)");
        this.setRunAllSortsName("Napoleon Sort (Resolved)");
        this.setRunSortName("Napoleon Sort (Resolved)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void tilsit(int[] array, int currentLen) {
        for (int i = 0, j = currentLen - 1; i < currentLen / 2; i++, j--) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, j);
            Delays.sleep(0.01);
            if (Reads.compareIndices(array, i, j, 1, true) > 0) Writes.swap(array, i, j, 1, true, false);
        }
    }
    
    protected boolean isRotationSolve(int[] array, int end) {
        int seg = 1;
        boolean equal = false;
        for (int i = 0; i < end && seg <= (equal ? 3 : 2); i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0, true);
            if (cmp > 0) seg++;   
            if (cmp == 0) equal = true;
        }
        boolean cmp = false;
        if (seg == 2) {
            BlockInsertionSortNeonLessInsert blocksert = new BlockInsertionSortNeonLessInsert(arrayVisualizer);
            blocksert.insertionSort(array, 0, end + 1);
        } else if (seg == 3 && equal) {
            cmp = Reads.compareIndices(array, 0, end, 0, true) == 0;
            if (cmp) {
                BlockInsertionSortNeonLessInsert blocksert = new BlockInsertionSortNeonLessInsert(arrayVisualizer);
                blocksert.insertionSort(array, 0, end + 1);
            }
        }
        return seg < 3 || cmp;
    }

    private void napoleon(int[] array, int end) {
        int lo = 0;
        int hi = end;
        int prev = array[0];
        boolean goSmaller = true;
        boolean noneFound = false;
        boolean twoSeg = isRotationSolve(array, end);
        while (hi > lo && !twoSeg) {
            if (goSmaller) {
                int next = lookEast(array, prev, lo + 1, hi);
                if (next == lo) {
                    if (noneFound) {
                        lo++;
                        hi--;
                    }
                    prev = noneFound ? array[lo] : array[hi];
                    goSmaller = noneFound;
                    noneFound = !noneFound;
                } else if (next == hi) {
                    prev = array[hi];
                    conquer(array, hi, lo);
                    noneFound = false;
                } else {
                    prev = array[next];
                    conquer(array, next, hi);
                    noneFound = false;
                    goSmaller = false;
                }
            } else {
                int next = lookWest(array, prev, hi - 1, lo);
                if (next == hi) {
                    if(noneFound) {
                        lo++;
                        hi--;
                    }
                    prev = array[lo];
                    goSmaller = true;
                    noneFound = !noneFound;
                } else if (next == lo) {
                    prev = array[lo];
                    conquer(array, lo, hi);
                    noneFound = false;
                } else {
                    prev = array[next];
                    conquer(array, next, lo);
                    noneFound = false;
                    goSmaller = true;
                }
            }
            twoSeg = isRotationSolve(array, end);
        }
    }

    private int lookEast(int[] array, int prev, int start, int end) {
        for (int i = start; i <= end; i++) if (Reads.compareValues(array[i], prev) < 0) return i;
        return start - 1;
    }

    private int lookWest(int[] array, int prev, int start, int end) {
        for (int i = start; i >= end; i--) if (Reads.compareValues(array[i], prev) > 0) return i;
        return start + 1;
    }

    private void conquer(int[] array, int index, int target) {
        int blockSize = 1;
        while (index + blockSize - 1 < target) {
            int marchTo = recruit(array, array[index], index+blockSize, target);
            int spaceBetween = marchTo - index - blockSize + 1;
            march(array, index, blockSize, spaceBetween);
            index += spaceBetween;
            blockSize++;
        }
        while (index - blockSize + 1 > target) {
            int marchTo = recruit(array, array[index], index-blockSize, target);
            int spaceBetween = index - blockSize - marchTo + 1;
            march(array, marchTo, spaceBetween, blockSize);
            index -= spaceBetween;
            blockSize++;
        }
    }

    private int recruit(int[] array, int identicalTo, int start, int end) {
        if (start < end) {
            for (int i = start; i <= end; i++) if (Reads.compareValues(array[i], identicalTo) == 0) return i - 1;
            return end;
        }
        for (int i = start; i >= end; i--) if (Reads.compareValues(array[i], identicalTo) == 0) return i + 1;
        return end;
    }

    private void march(int[] array, int index, int len1, int len2) {
        while (len1 != 0 && len2 != 0) {
            if (len1 <= len2) {
                attack(array, index, index+len1, len1);
                index += len1;
                len2 -= len1;
            } else {
                attack(array, index + len1, index + len1 - len2, len2);
                len1 -= len2;
            }
        }
    }

    private void attack(int[] array, int startA, int startB, int swapsLeft) {
        while (swapsLeft != 0 && startA < oob && startB < oob) {
            Writes.swap(array, startA++, startB++, 0.01, true, false);
            swapsLeft--;
        }
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        oob = currentLen;
        if (isRotationSolve(array, currentLen - 1)) return;
        tilsit(array, currentLen);
        Highlights.clearAllMarks();
        napoleon(array, currentLen - 1);
    }
}