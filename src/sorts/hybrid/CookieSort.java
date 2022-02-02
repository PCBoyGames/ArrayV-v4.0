package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

This sort is currently incomplete. The original CEO worked on a draft of
this algorithm before he got infected with STG-22 Alpha. I still wait for
him to get better, so I've ported our progress so far.

*/
final public class CookieSort extends Sort {

    int blockLen;
    int endpoint;
    PDBinaryInsertionSort insert = new PDBinaryInsertionSort(arrayVisualizer);
    BlockInsertionSortNeon blocksert = new BlockInsertionSortNeon(arrayVisualizer);

    public CookieSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cookie [WIP]");
        this.setRunAllSortsName("Cookie Sort [WIP]");
        this.setRunSortName("Cookie Sort [WIP]");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }

    protected void createBlock(int[] array, int start, int end) {
        insert.pdbinsert(array, start, end, 0.05, false);
    }

    protected void secondSize(int[] array, int start, int length, int bufferbegin) {
        for (int i = 0; i < length; i++) Writes.swap(array, start + i, bufferbegin + i, 0.25, true, false);
        int left = 0;
        int right = 0;
        int balance = start;
        while (left < length && right < length) {
            if (Reads.compareIndices(array, start + length + left, bufferbegin + right, 0.25, true) <= 0) {
                if (start + length + left != balance) Writes.swap(array, start + length + left, balance, 0.25, true, false);
                left++;
            } else {
                Writes.swap(array, bufferbegin + right, balance, 0.25, true, false);
                right++;
            }
            balance++;
            if (left >= length) {
                while (right < length) {
                    Writes.swap(array, bufferbegin + right, balance, 0.25, true, false);
                    right++;
                    balance++;
                }
            }
        }
    }

    protected void milkTwo(int[] array, int start, int end) {
        int a = start;
        int b = start + ((end - start) / 2);
        if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            while (a < b) {
                if (Reads.compareIndices(array, a, b, 1, true) > 0) for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), 1, true, false);
                a++;
            }
            blocksert.insertionSort(array, b, end);
        }
    }

    protected void milkFour(int[] array, int start, int end) {
        milkTwo(array, start, start + ((end - start) / 2));
        milkTwo(array, start + ((end - start) / 2), end);
        milkTwo(array, start, end);
    }

    public void cookie(int[] array, int start, int length) {
        blockLen = pow2lte((int) Math.sqrt(length));
        endpoint = 4 * blockLen;
        while (endpoint + 4 * blockLen < length) endpoint += 4 * blockLen;
        for (int i = 0; i + blockLen <= endpoint; i += blockLen) {
            createBlock(array, start + i, start + i + blockLen);
        }
        for (int i = 0; i + 2 * blockLen <= endpoint; i += 2 * blockLen) {
            secondSize(array, start + i, blockLen, start + endpoint);
        }
        for (int i = 0; i + 4 * blockLen <= endpoint; i += 4 * blockLen) {
            secondSize(array, start + i, 2 * blockLen, start + endpoint);
        }
        int bufferinserts;
        for (bufferinserts = endpoint; bufferinserts + blockLen <= length; bufferinserts += blockLen) {
            createBlock(array, start + bufferinserts, start + bufferinserts + blockLen);
        }
        milkFour(array, start + endpoint, start + length);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (currentLength < 32) insert.pdbinsert(array, 0, currentLength, 0.25, false);
        else {
            int effectivelen = 2;
            while (effectivelen <= currentLength) effectivelen *= 2;
            effectivelen /= 2;
            cookie(array, 0, effectivelen);
        }
    }
}