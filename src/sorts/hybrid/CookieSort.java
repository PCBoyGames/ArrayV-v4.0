package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.GrailSorting;
import utils.Rotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CookieSort extends GrailSorting {

    BlockInsertionSortNeon blocksert = new BlockInsertionSortNeon(arrayVisualizer);
    PDBinaryInsertionSort insert = new PDBinaryInsertionSort(arrayVisualizer);

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

    // UTIL
    protected int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int a, int b) {
        boolean[] max = new boolean[b - a];
        int maximum = stablereturn(array[a]);
        for (int i = 1; i < b - a; i++) {
            if (stablereturn(array[a + i]) > maximum) {
                maximum = stablereturn(array[a + i]);
                max[i] = true;
            }
        }
        int i = b - a - 1;
        int p = 1;
        int j = b - a - 1;
        while (j >= 0 && i >= p) {
            while(!max[j] && j > 0) j--;
            maximum = stablereturn(array[a + j]);
            while (maximum <= stablereturn(array[a + i]) && i >= p) i--;
            if (stablereturn(array[a + j]) > stablereturn(array[a + i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.neon(array, pos, lenA, lenB, 1, true, false);
    }

    // SHELL
    protected int shellPass(int[] array, int a, int b, int gap, int par, int lastgap) {
        if (gap >= lastgap) return lastgap;
        if (gap == lastgap - 1 && gap != 1) return lastgap;
        lastgap = gap;
        for (int i = a + gap; i < b; i++) {
            int key = array[i];
            int j = i - gap;
            boolean change = false;
            while (j >= a && Reads.compareValues(key, array[j]) < 0) {
                Writes.write(array, j + gap, array[j], 1, true, false);
                j -= gap;
                change = true;
            }
            if (change)
                Writes.write(array, j + gap, key, 1, true, false);
        }
        Highlights.clearAllMarks();
        return gap;
    }

    public void shellSort(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        double truediv = 3;
        int lastpar = b - a;
        int lastgap = b - a;
        while (true) {
            int par = par(array, a, b);
            int passpar = par;
            if (par >= lastpar) par = lastpar - (int) truediv;
            if (par / (int) truediv <= 1) {
                shellPass(array, a, b, 1, par, lastgap);
                break;
            }
            lastgap = shellPass(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), passpar, lastgap);
            if (lastpar - par <= Math.sqrt(lastpar)) truediv *= 1.5;
            lastpar = par;
        }
    }

    // MILK
    protected void milkPass(int[] array, int start, int end, double delay) {
        int a = start;
        int b = start + ((end - start) / 2);
        int lasta = start;
        int consecutive = 0;
        boolean faultout = false;
        if (Reads.compareIndices(array, b - 1, b, delay, true) > 0) {
            while (a < b && !faultout) {
                if (Reads.compareIndices(array, a, b, delay, true) > 0) {
                    for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), delay, true, false);
                    if (a - lasta < 3) {
                        consecutive++;
                        if (consecutive == 8) {
                            shellSort(array, a, end);
                            faultout = true;
                        }
                    }
                    lasta = a;
                } else if (a - lasta > 1) consecutive = 0;
                a++;
            }
            if (!faultout) blocksert.insertionSort(array, b, end);
        }
    }

    protected void milkNon2N(int[] array, int start, int end, int len, double delay) {
        int b = start + (len / 2);
        if (b < end) {
            if (Reads.compareIndices(array, b - 1, b, delay, true) > 0) {
                Highlights.clearAllMarks();
                if (end - b <= len / 8) grailMergeWithoutBuffer(array, start, b - start, end - (b + start));
                else shellSort(array, start, end);
            }
        }
    }

    public void milkSortLen(int[] array, int start, int end, int lengthstart, double delay) {
        int len = lengthstart;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) Writes.swap(array, index, index + 1, delay, true, false);
                } else milkPass(array, index, index + len, delay);
                index += len;
            }
            if (index != end) milkNon2N(array, index, end, len, delay);
            len *= 2;
        }
        if (len == end - start) milkPass(array, start, end, delay);
        else milkNon2N(array, start, end, len, delay);
    }

    // COOKIE
    protected void handleInsert(int[] array, int start, int end) {
        if (end - start <= 16) insert.pdbinsert(array, start, end, 1, false);
        else shellSort(array, start, end);
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
        for (int i = 0; i + blockLen <= endpoint; i += blockLen) handleInsert(array, start + i, start + i + blockLen);
        for (int i = 0; i + 2 * blockLen <= start + endpoint; i += 2 * blockLen) manageSize(array, start + i, blockLen, start + endpoint);
        handleInsert(array, start + endpoint, start + length);
        milkPass(array, start + endpoint - blockLen, start + length, 0.5);
        milkSortLen(array, start, start + length, 4 * blockLen, 0.5);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int effectivelen = 2;
        while (effectivelen <= currentLength) effectivelen *= 2;
        effectivelen /= 2;
        cookie(array, 0, effectivelen);
        if (effectivelen != currentLength) {
            handleInsert(array, effectivelen, currentLength);
            milkNon2N(array, 0, currentLength, 2 * effectivelen, 0.5);
        }
    }
}