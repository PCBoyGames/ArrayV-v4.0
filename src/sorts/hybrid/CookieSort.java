package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.insert.PDBinaryInsertionSort;
import sorts.merge.IterativeMilkSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CookieSort extends Sort {

    PDBinaryInsertionSort insert = new PDBinaryInsertionSort(arrayVisualizer);
    BlockInsertionSortNeon blocksert = new BlockInsertionSortNeon(arrayVisualizer);

    // TODO: Build ItrMilk into the algorithm. Its logic as is is not good enough.
    IterativeMilkSort milk = new IterativeMilkSort(arrayVisualizer);

    public CookieSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cookie [WIP]");
        this.setRunAllSortsName("Cookie Sort [WIP]");
        this.setRunSortName("Cookie Sort [WIP]");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }

    protected void manageSize(int[] array, int start, int length, int bufferbegin) {
        if (Reads.compareIndices(array, start + length - 1, start + length, 1, true) > 0) {
            for (int i = 0; i < length; i++) Writes.swap(array, start + i, bufferbegin + i, 1, true, false);
            int left = 0;
            int right = 0;
            int balance = start;
            while (left < length && right < length) {
                if (Reads.compareIndices(array, start + length + left, bufferbegin + right, 1, true) <= 0) {
                    if (start + length + left != balance) Writes.swap(array, start + length + left, balance, 1, true, false);
                    left++;
                } else {
                    Writes.swap(array, bufferbegin + right, balance, 1, true, false);
                    right++;
                }
                balance++;
                if (left >= length) {
                    while (right < length) {
                        Writes.swap(array, bufferbegin + right, balance, 1, true, false);
                        right++;
                        balance++;
                    }
                }
            }
        }
    }

    public void cookie(int[] array, int start, int length) {
        int blockLen = pow2lte((int) Math.sqrt(length));
        int endpoint = blockLen;
        while (endpoint + blockLen < length) endpoint += blockLen;
        for (int i = 0; i + blockLen <= endpoint; i += blockLen) insert.pdbinsert(array, start + i, start + i + blockLen, 1, false);
        for (int i = 0; i + 2 * blockLen <= start + endpoint; i += 2 * blockLen) manageSize(array, start + i, blockLen, start + endpoint);
        insert.pdbinsert(array, start + endpoint, start + length, 0.5, false);
        milk.milksortlen(array, start, start + length, blockLen, 0.5);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (currentLength < 32) insert.pdbinsert(array, 0, currentLength, 1, false);
        else {
            int effectivelen = 2;
            while (effectivelen <= currentLength) effectivelen *= 2;
            effectivelen /= 2;
            cookie(array, 0, effectivelen);
            if (effectivelen != currentLength) {
                if (currentLength - effectivelen <= 16) insert.pdbinsert(array, effectivelen, currentLength, 0.5, false);
                else milk.milksort(array, effectivelen, currentLength, 0.5);
                blocksert.insertionSort(array, 0, currentLength);
            }
        }
    }
}