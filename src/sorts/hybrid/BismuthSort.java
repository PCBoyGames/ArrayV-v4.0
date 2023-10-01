package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BismuthSort extends MadhouseTools {
    public BismuthSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bismuth [WIP]");
        this.setRunAllSortsName("Bismuth Sort");
        this.setRunSortName("Bismuth Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the segment size to be used for this sort:\n0: Automatic (default)", 0);
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i], j = i;
            boolean w = false;
            for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) == 1; j -= h) Writes.write(array, j, array[j - h], 0.1, w = true, false);
            if (w) Writes.write(array, j, v, 0.1, true, false);
        }
    }

    protected void shell(int[] array, int start, int end) {
        if (findRun(array, start, end, 0.1, true, false) >= end) return;
        Highlights.clearAllMarks();
        for (int gap = end - start; gap > 1; gap /= 2.3601) shellPass(array, start, end, gap);
        shellPass(array, start, end, 1);
    }

    protected void blocksert(int[] array, int start, int end) {
        if (start >= end) return;
        int i = findRun(array, start, end, 0.1, true, false), j;
        for (; i < end; i = j) {
            j = findRun(array, i, end, 0.1, true, false);
            if (i < end) grailMergeWithoutBuffer(array, start, i - start, j - i);
        }
    }

    protected void insertions(int[] array, int start, int end) {
        if (end - start > 32) shell(array, start, end);
        else blocksert(array, start, end);
    }

    protected void prepareSegments(int[] array, int start, int end, int bSize) {
        int i = start;
        for (; i + bSize <= end; i += bSize) insertions(array, i, i + bSize);
        insertions(array, i, end);
    }

    protected int fcomp(int[] array, int a, int b, int f) {
        int c = stableReturn(array[a]), d = stableReturn(array[b]);
        if (c < d) return -1;
        else if (c > d) return 1;
        else return 0;
    }

    protected void blockSelect(int[] array, int start, int end, int mid, int bSize) {
        for (int i = start, sel = i, right = mid, f = 0; i + bSize <= end; i += bSize, sel = i, f = 0) {
            if (i == mid) {
                for (; mid + 2 * bSize <= Math.min(right, end - bSize); mid += bSize) if (fcomp(array, mid, mid + bSize, f++) >= 0) break;
                mid += bSize;
            }
            for (int j = Math.max(i + bSize, mid); j <= Math.min(right, end - bSize); j += bSize) {
                int comp = fcomp(array, sel, j, f++);
                if (comp > 0) sel = j;
                else if (comp == 0 && bSize > 1 && fcomp(array, sel + bSize - 1, j + bSize - 1, f++) > 0) sel = j;
            }
            Reads.addComparisons(f);
            if (sel == right) right += bSize;
            if (sel != i) for (int j = 0; j < bSize; j++) Writes.swap(array, i + j, sel + j, 1, true, false);
            else {
                Highlights.clearMark(2);
                Highlights.markArray(1, i);
            }
        }
    }

    protected int toBuff(int[] array, int start, int end, int tStart) {
        int buffPos = 0;
        for (; start < end; start++) Writes.swap(array, start, tStart + buffPos++, 1, true, false);
        return buffPos;
    }

    protected boolean doesRotate(int[] array, int start, int mid, int end) {
        boolean did = false;
        if (Reads.compareIndices(array, start, end - 1, 1, true) >= 0) rotateIndexed(array, start, mid, end, 1, did = true, false);
        else if (Reads.compareIndices(array, start, end - 2, 1, true) >= 0) {
            grailMergeWithoutBuffer(array, start, mid - start, end - mid);
            did = true;
        }
        return did;
    }

    protected int buffer(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean dir, boolean skip) {
        int i = mid + bSize;
        if (skip) {
            for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
            if (i == mid + bSize) return buffer(array, start, mid, end, tStart, tEnd, bSize, true, false);
            int sEnd = minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true);
            if (doesRotate(array, start, mid, sEnd)) return i - 1;
            int buffLen = toBuff(array, start, mid, tStart), buffPos = 0, right = mid, left = start;
            while (buffPos < buffLen && left < right) {
                int target = minExponentialSearch(array, right, i, array[tStart + buffPos], true, 1, true);
                while (right < target) Writes.swap(array, left++, right++, 1, true, false);
                Writes.swap(array, tStart + buffPos++, left++, 1, true, false);
                if (right == sEnd) while (buffPos < buffLen) Writes.swap(array, tStart + buffPos++, left++, 1, true, false);
            }
        } else if (doesRotate(array, start, mid, end)) i = start;
        else {
            int buffLen = toBuff(array, dir ? start : mid, dir ? mid : end, tStart), buffPos = dir ? 0 : buffLen - 1, left = dir ? start : mid - 1, right = dir ? mid : end - 1;
            while ((dir && buffPos < buffLen) || (!dir && buffPos >= 0)) {
                if (Reads.compareIndices(array, dir ? tStart + buffPos : left, dir ? right : tStart + buffPos, 1, true) <= 0) Writes.swap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++: right--, 1, true, false);
                else Writes.swap(array, dir ? left++ : left--, dir ? right++ : right--, 1, true, false);
                if (right == end || left < start) while ((dir && buffPos < buffLen) || (!dir && buffPos >= 0)) Writes.swap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++ : right--, 1, true, false);
            }
            i = start;
        }
        return --i;
    }

    protected int noBuffer(int[] array, int start, int mid, int end, int tEnd, int bSize, boolean skip) {
        int sEnd, i = mid + bSize;
        if (skip) for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
        sEnd = skip ? minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true) : end;
        if (doesRotate(array, start, mid, sEnd)) return start;
        if (Math.min(mid - start, sEnd - mid) > 9) giveUpMerge(array, start, mid, sEnd, bSize, mid - start < sEnd - mid, false, false, 0);
        else grailMergeWithoutBuffer(array, start, mid - start, sEnd - mid);
        return sEnd;
    }

    protected int twoBlocks(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean useBuffers) {
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return start;
        int left = minExponentialSearch(array, start + 1, mid, array[mid], false, 1, true), right = maxExponentialSearch(array, mid, end - 1, array[mid - 1], true, 1, true);
        if (Math.min(mid - 1 - left, right - mid) > 8 && useBuffers) return buffer(array, left, mid, right + 1, tStart, tEnd, bSize, left - start > end - 1 - right, right == end - 1);
        else return noBuffer(array, left, mid, right + 1, tEnd, bSize, right == end - 1);
    }

    protected void merge(int[] array, int start, int end, int mid, int tStart, int bSize, boolean useBuffers) {
        if (mid >= end) return;
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return;
        blockSelect(array, start, end, mid, bSize);
        if (bSize == 2) for (int i = start; i + 1 < end; i++) if (Reads.compareIndices(array, i, i + 1, 1, true) > 0) Writes.swap(array, i, i + 1, 1, true, false);
        if (bSize <= 2) return;
        for (int i = start, p = start; i + 2 * bSize <= end; i += bSize) if (i + bSize >= p) p = twoBlocks(array, i, i + bSize, i + 2 * bSize, tStart, end, bSize, useBuffers);
    }

    protected void giveUpMerge(int[] array, int start, int mid, int end, int bSize, boolean side, boolean pre, boolean searches, int d) {
        Writes.recordDepth(d);
        if (pre) insertions(array, side ? start : mid, side ? mid : end);
        if (searches) start = minExponentialSearch(array, start, mid, array[mid], true, 1, true);
        if (searches) end = maxExponentialSearch(array, mid, end, array[mid - 1], false, 1, true);
        if (Math.min(mid - start, end - mid) <= 8) {
            grailMergeWithoutBuffer(array, start, mid - start, end - mid);
            return;
        }
        if (mid - start < end - mid != side) side = !side;
        int pos = side ? minExponentialSearch(array, mid, end, array[start], true, 1, true) : maxExponentialSearch(array, start, mid, array[end - 1], false, 1, true), s = side ? mid - start - 1 : end - mid - 1, g = (int) Math.sqrt(s + 1), b = end;
        rotateIndexed(array, side ? start : pos, mid, side ? pos : end, 1, true, false);
        mid = pos;
        if (side) start = pos - s;
        else end = pos + s;
        while (s > g + 1) {
            pos = side ? minExponentialSearch(array, mid, end, array[start + g], true, 1, true) : maxExponentialSearch(array, start, mid, array[end - g - 1], false, 1, true);
            rotateIndexed(array, side ? start + g : pos, mid, side ? pos : end - g, 1, true, false);
            if (((side && pos > mid) || (!side && pos < mid)) && Math.min(g, Math.abs(pos - mid)) > 8) {
                Writes.recursion();
                giveUpMerge(array, side ? start : pos, side ? start + g : end - g, side ? pos : end, bSize, side, false, false, d + 1);
            } else grailMergeWithoutBuffer(array, side ? start : pos, side ? g : end - pos - g, side ? pos - start - g : g);
            s -= g + 1;
            mid = pos;
            if (side) start = pos - s;
            else end = pos + s;
        }
        if (s > 0) grailMergeWithoutBuffer(array, start, side ? s : mid - start, side ? end - start - s : Math.min(s + 1, b - mid));
    }

    public void bismuthSort(int[] array, int start, int end, int getSize, boolean useBuffers) {
        if (getSize == 0) getSize = (int) Math.sqrt(end - start);
        if (getSize > end - start) getSize = end - start;
        String caseType = end - start < 33 ? "SMALL" : getSize == 1 ? "1A" : getSize == 2 ? "1B" : getSize < 11 ? "1C" : !useBuffers && getSize < 33 ? "1D" : !useBuffers && getSize > 32 ? "1E" : getSize < 33 ? "2A" : getSize > 32 ? "2B" : "UNKNOWN";
        arrayVisualizer.setExtraHeading(" / bSize = " + getSize + " (Case " + caseType + ")");
        int balance = (end - start) % getSize, deadEnd = end - balance;
        if (end - start < 33) insertions(array, start, end);
        if (end - start < 33 || patternDefeat(array, start, end, false, 1, true, false)) return;
        if (getSize < 11 || !useBuffers) insertions(array, start, start + getSize);
        prepareSegments(array, start + getSize, deadEnd, getSize);
        for (int cur = getSize * 2; cur <= 2 * (deadEnd - start); cur *= 2) {
            int s = getSize > 10 && useBuffers ? start + getSize : start;
            for (; s + cur <= deadEnd; s += cur) merge(array, s, s + cur, s + cur / 2, start, getSize, useBuffers);
            if (s + cur / 2 <= deadEnd) merge(array, s, deadEnd, s + cur / 2, start, getSize, useBuffers);
        }
        if (getSize > 10 && useBuffers) giveUpMerge(array, start, start + getSize, deadEnd, getSize, true, true, true, 0);
        if (balance > 0) giveUpMerge(array, start, deadEnd, deadEnd + balance, getSize, false, true, true, 0);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        bismuthSort(array, 0, currentLength, getSize, true);
    }
}