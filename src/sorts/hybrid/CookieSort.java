package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CookieSort extends GrailSorting {

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

    protected int pdUnstable(int[] array, int start, int end) {
        int forward = start;
        int cmp = Reads.compareIndices(array, forward, forward + 1, 1, true);
        while (cmp <= 0 && forward + 1 < end) {
            forward++;
            if (forward + 1 < end) cmp = Reads.compareIndices(array, forward, forward + 1, 1, true);
        }
        int reverse = start;
        if (forward == start) {
            boolean different = false;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            while (cmp >= 0 && reverse + 1 < end) {
                if (cmp != 0) different = true;
                reverse++;
                if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            }
            if (reverse > start && different) {
                if (reverse < start + 3) Writes.swap(array, start, reverse, 1, true, false);
                else Writes.reversal(array, start, reverse, 1, true, false);
            }
        }
        return Math.max(forward, reverse);
    }

    protected int pdUnstableNF(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp != 0) different = true;
            reverse++;
            if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 1, true, false);
            else Writes.reversal(array, start, reverse, 1, true, false);
        }
        return reverse;
    }

    protected int pdUnstableCNF(int[] array, int start, int end) {
        int firstreverse = start;
        boolean first = true;
        int reverse = start;
        int newstart = start;
        while (reverse + 1 < end) {
            boolean different = false;
            int cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            while (cmp >= 0 && reverse + 1 < end) {
                if (cmp != 0) different = true;
                reverse++;
                if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            }
            if (reverse > newstart + 1 && different) {
                if (reverse < newstart + 3) Writes.swap(array, newstart, reverse, 1, true, false);
                else Writes.reversal(array, newstart, reverse, 1, true, false);
            }
            if (first) firstreverse = reverse;
            first = false;
            reverse++;
            newstart = reverse;
        }
        return firstreverse;
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        int end = pos + lenA + lenB;
        while (lenA > 0 && lenB > 0) {
            if (lenA < lenB) {
                for (int i = 0; i < lenA; i++) {
                    int t = array[pos + i], j = pos + i + lenA;
                    for (; j < end; j += lenA) {
                        Writes.write(array, j - lenA, array[j], 1, true, false);
                    }
                    Writes.write(array, j - lenA, t, 1, true, false);
                }
                pos += lenB;
                lenB %= lenA;
                lenA -= lenB;
            } else {
                for (int i = 0; i < lenB; i++) {
                    int t = array[pos + i + lenA], j = pos + i + lenA - lenB;
                    for(; j >= pos; j -= lenB) {
                        Writes.write(array, j + lenB, array[j], 1, true, false);
                    }
                    Writes.write(array, j + lenB, t, 1, true, false);
                }
                end = pos+lenB;
                lenA %= lenB;
                lenB -= lenA;
            }
        }
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
        int pd = pdUnstableNF(array, a, b);
        if (pd + 1 < b) {
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
    }

    // BLOCKSERT
    protected int blockFindRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b)
            return i;
        if(Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1) i++;
            if (i - a > 3) Writes.reversal(array, a, i - 1, 1, true, false);
            else Writes.swap(array, a, i - 1, 1, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    public void blockInsertionSort(int[] array, int a, int b) {
        int i, j, len;
        i = blockFindRun(array, a, b);
        while (i < b) {
            j = blockFindRun(array, i, b);
            len = j - i;
            grailMergeWithoutBuffer(array, a, i - a, len);
            i = j;
        }
    }

    // BINSERT
    public void pdbinsertUnstable(int[] array, int start, int end) {
        int pattern = pdUnstable(array, start, end);
        Highlights.clearAllMarks();
        for (int i = pattern + 1; i < end; i++) {
            int item = array[i];
            int left = binarySearch(array, start, i, item);
            Highlights.clearAllMarks();
            Highlights.markArray(2, left);
            boolean w = false;
            for (int right = i; right > left; right--) {
                Writes.write(array, right, array[right - 1], 1 / 20, true, false);
                w = true;
            }
            if (w) Writes.write(array, left, item, 1, true, false);
            Highlights.clearAllMarks();
        }
    }

    // MILK
    protected void milkPass(int[] array, int start, int end) {
        int a = start;
        int b = start + ((end - start) / 2);
        int lasta = start;
        int consecutive = 0;
        boolean faultout = false;
        if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            while (a < b && !faultout) {
                if (Reads.compareIndices(array, a, b, 1, true) > 0) {
                    for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), 0.5, true, false);
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
            if (!faultout) blockInsertionSort(array, b, end);
        }
    }

    protected void milkNon2N(int[] array, int start, int end, int len) {
        int b = start + (len / 2);
        if (b < end) {
            if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            Highlights.clearAllMarks();
                if (end - b <= len / 8) grailMergeWithoutBuffer(array, start, b - start, end - b);
                else shellSort(array, start, end);
            }
        }
    }

    public void milkSortLen(int[] array, int start, int end, int lengthstart) {
        int len = lengthstart;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, 1, true) > 0) Writes.swap(array, index, index + 1, 1, true, false);
                } else milkPass(array, index, index + len);
                index += len;
            }
            if (index != end) milkNon2N(array, index, end, len);
            len *= 2;
        }
        if (len == end - start) milkPass(array, start, end);
        else milkNon2N(array, start, end, len);
    }

    // COOKIE
    protected void handleInsert(int[] array, int start, int end) {
        if (end - start <= 16) pdbinsertUnstable(array, start, end);
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

    protected void cookie(int[] array, int start, int length, int pd) {
        int blockLen = pow2lte((int) Math.sqrt(length));
        int endpoint = blockLen;
        while (endpoint + blockLen < length) endpoint += blockLen;
        int i;
        for (i = 0; i + blockLen <= endpoint; i += blockLen) if (start + i + blockLen > pd) handleInsert(array, start + i, start + i + blockLen);
        for (i = 0; i + 2 * blockLen <= endpoint; i += 2 * blockLen) manageSize(array, start + i, blockLen, start + endpoint);
        handleInsert(array, start + endpoint, start + length);
        milkPass(array, start + endpoint - blockLen, start + length);
        milkSortLen(array, start, start + length, 4 * blockLen);
    }

    public void cookieSort(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (par(array, start, end) <= (end - start) / 8) shellSort(array, start, end);
        else if (end - start <= 32) handleInsert(array, start, end);
        else {
            int pd = pdUnstableCNF(array, start, end);
            if (pd < end - 1) {
                int length = end - start;
                int effectivelen = 2;
                while (effectivelen <= length) effectivelen *= 2;
                effectivelen /= 2;
                cookie(array, start, effectivelen, pd);
                if (effectivelen != length) {
                    Writes.recursion();
                    cookieSort(array, start + effectivelen, end, depth + 1);
                    milkNon2N(array, start, end, effectivelen * 2);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        cookieSort(array, 0, currentLength, 0);
    }
}