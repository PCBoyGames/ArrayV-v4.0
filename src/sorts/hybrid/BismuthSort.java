package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

CHANGELOG:

Bismuth 0.WIP2A (08/09/2025)
 - Add colors and a related toggle. This is alongside adding color highlighting to the ArrayV fork.
   + Toggling colors on and off is useful in sort instances where the other sort is without colors.
 - Improve Shell's sequences to rely on coprimality.
 - Switch 1D and 1E to buffered self-recursion as needed.
 - Improve nudge behavior to not call recursion. This required a rewrite in the give-up behavior.

Bismuth 0.WIP2 (05/31/2025)
 - Switch redistribution to a nudge instead of shifting the buffer.
 - Improve case 1B to use Jeren behaviors instead of Bubble.
 - Switch 1D and 1E to self-recursion rather than forcing redistribution.
 - Merge several functions together to reduce code size.
 - Remove the pattern defeat. This was a small overhead, and removal improved random patterns in practice.
   + This led to what is now Natural Bismuth, which has a slightly different behavior, but uses PDs.

Bismuth 0.WIP1 (10/18/2023)
 - Initial version.

*/
/**An unstable and adaptive block merge sort.<p>
 * To use this algorithm in another, use {@code bismuthSort()} from a reference instance.
 * @version Bismuth 0.WIP2A
 * @author PCBoy
*/
public class BismuthSort extends MadhouseTools {

    public boolean colors = true;

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

    // Since colors are a toggle, override the color functions to allow for toggling
    public void colorRange(String colorName, java.awt.Color color, int start, int end) {
        if (!colors) return;
        Highlights.defineColor(colorName, color);
        for (int i = start; i < end; i++) Highlights.colorCode(colorName, i);
    }

    public void colorSwap(int[] array, int a, int b) {
        if (colors) Highlights.swapColors(a, b);
        Writes.swap(array, a, b, 1, true, false);
    }

    protected void insertions(int[] array, int start, int end) {
        if (end - start < 33) {
            if (end - start > 1) colorRange("blocksert", java.awt.Color.CYAN, start, end);
            int i = findRun(array, start, end, 0.1, true, false), j;
            for (; i < end; i = j) {
                j = findRun(array, i, end, 0.1, true, false);
                if (i < end) grailMergeWithoutBuffer(array, start, i - start, j - i);
            }
        } else {
            colorRange("shell", java.awt.Color.MAGENTA, start, end);
            if (findRun(array, start, end, 0.1, true, false) >= end) return;
            Highlights.clearAllMarks();
            for (int gap = end - start; gap >= 1; ) {
                for (int h = gap, i = h + start; i < end; i++) {
                    int v = array[i], j = i;
                    boolean w = false;
                    for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) > 0; j -= h) Writes.write(array, j, array[j - h], 1, w = true, false);
                    if (w) Writes.write(array, j, v, 1, true, false);
                }
                if (gap == 1) break;
                int newG = (int) Math.max(1, gap / 2.3601);
                while (newG > 1 && !coprime(gap, newG)) newG--;
                gap = newG;
            }
        }
    }

    // Custom comparison function so case 1A and 1B can be displayed faster on long arrays
    protected int fcomp(int[] array, int a, int b, int f) {
        int c = stableReturn(array[a]), d = stableReturn(array[b]);
        if (c < d) return -1;
        else if (c > d) return 1;
        else return 0;
    }

    protected void blockSelect(int[] array, int start, int end, int mid, int bSize) {
        for (int i = start, sel = i, right = mid, f = 0; i + bSize <= end; i += bSize, sel = i, f = 0) {
            if (i == mid) {
                for (; mid + 2 * bSize <= Math.min(right, end - bSize); mid += bSize) {
                    int cmp = fcomp(array, mid, mid + bSize, f++);
                    if (cmp > 0) break;
                    else if (cmp == 0 && bSize > 1 && fcomp(array, mid + bSize - 1, mid + 2 * bSize - 1, f++) > 0) break;
                }
                mid += bSize;
            }
            for (int j = Math.max(i + bSize, mid); j <= Math.min(right, end - bSize); j += bSize) {
                int comp = fcomp(array, sel, j, f++);
                if (comp > 0) sel = j;
                else if (comp == 0 && bSize > 1 && fcomp(array, sel + bSize - 1, j + bSize - 1, f++) > 0) sel = j;
            }
            Reads.addComparisons(f);
            if (sel == right) right += bSize;
            if (sel == i) {
                Highlights.clearMark(2);
                Highlights.markArray(1, i);
                colorRange("selected", java.awt.Color.ORANGE, i, i + bSize);
            } else for (int j = 0; j < bSize; j++) {
                colorRange("selected", java.awt.Color.ORANGE, i + j, i + j + 1);
                colorRange("selOpen", java.awt.Color.LIGHT_GRAY, sel + j, sel + j + 1);
                Writes.swap(array, i + j, sel + j, 1, true, false);
            }
        }
    }

    protected int toBuff(int[] array, int start, int end, int tStart) {
        int buffPos = 0;
        while (start < end) colorSwap(array, start++, tStart + buffPos++);
        return buffPos;
    }

    protected boolean doesRotate(int[] array, int start, int mid, int end) {
        boolean did = false;
        if (Reads.compareIndices(array, start, end - 1, 1, true) >= 0) {
            colorRange("doesRotate", new java.awt.Color(255, 255, 127), start, end);
            rotateIndexed(array, start, mid, end, 1, did = true, false);
        } else if (end - 1 > mid && Reads.compareIndices(array, start, end - 2, 1, true) >= 0) {
            colorRange("doesRotate", new java.awt.Color(255, 255, 127), start, end);
            rotateIndexed(array, start, mid, end - 1, 1, did = true, false);
            Writes.insert(array, end - 1, maxExponentialSearch(array, start, end - 1, array[end - 1], false, 1, true), 1, true, false);
        }
        return did;
    }

    protected int buffer(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean dir, boolean skip) {
        int i = mid + bSize;
        if (skip) {
            for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
            if (i == mid + bSize) return buffer(array, start, mid, end, tStart, tEnd, bSize, true, false);
            int sEnd = minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true);
            if (doesRotate(array, start, mid, sEnd)) return --i;
            colorRange("longBufferMerge", java.awt.Color.PINK, start, sEnd);
            for (int buffLen = toBuff(array, start, mid, tStart), buffPos = 0, right = mid, left = start; buffPos < buffLen && left < right; ) {
                int target = minExponentialSearch(array, right, i, array[tStart + buffPos], true, 1, true);
                while (right < target) colorSwap(array, left++, right++);
                colorSwap(array, tStart + buffPos++, left++);
                if (right == sEnd) while (buffPos < buffLen) colorSwap(array, tStart + buffPos++, left++);
            }
        } else if (!doesRotate(array, start, mid, end)) {
            colorRange("bufferMerge", java.awt.Color.GREEN, start, end);
            for (int buffLen = toBuff(array, dir ? start : mid, dir ? mid : end, tStart), buffPos = dir ? 0 : buffLen - 1, left = dir ? start : mid - 1, right = dir ? mid : end - 1; (dir && buffPos < buffLen) || (!dir && buffPos >= 0); ) {
                if (Reads.compareIndices(array, dir ? tStart + buffPos : left, dir ? right : tStart + buffPos, 1, true) <= 0) colorSwap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++: right--);
                else colorSwap(array, dir ? left++ : left--, dir ? right++ : right--);
                if (right == end || left < start) while ((dir && buffPos < buffLen) || (!dir && buffPos >= 0)) colorSwap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++ : right--);
            }
        }
        return --i;
    }

    protected int twoBlocks(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean useBuffers, int depth) {
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return start;
        int left = minExponentialSearch(array, start + 1, mid, array[mid], false, 1, true), right = maxExponentialSearch(array, mid, end - 1, array[mid - 1], true, 1, true);
        if (Math.min(mid - 1 - left, right - mid) < 9 || !useBuffers) {
            int i = mid + bSize;
            if (right == end - 1) for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
            int sEnd = right == end - 1 ? minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true) : right;
            colorRange("small", java.awt.Color.getHSBColor(0.8f, 1f, 1f), left, sEnd);
            if (doesRotate(array, left, mid, sEnd)) return sEnd;
            if (Math.min(mid - left, sEnd - mid) > 9) giveUpFair(array, left, mid, sEnd, true, false, false, depth);
            else grailMergeWithoutBuffer(array, left, mid - left, sEnd - mid);
            return sEnd;
        } else return buffer(array, left, mid, right + 1, tStart, tEnd, bSize, left - start > end - 1 - right, right == end - 1);
    }

    protected void merge(int[] array, int start, int end, int mid, int tStart, int bSize, boolean useBuffers, int depth) {
        if (mid >= end) return;
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return;
        blockSelect(array, start, end, mid, bSize);
        if (bSize == 2) {
            colorRange("jeren", java.awt.Color.BLUE, start, start + 1);
            for (int i = start + 1; i + 1 < end; i += (i - start) % 2 == 0 ? 1 : 2) {
                if (colors) Highlights.colorCode("jeren", i, i + 1);
                if (Reads.compareIndices(array, i, i + 1, 1, true) > 0) {
                    int left = i++;
                    for (; i + 1 < end; i++) {
                        if (colors) Highlights.colorCode("jeren", i + 1);
                        if (Reads.compareIndices(array, left, i + 1, 1, true) <= 0) break;
                    }
                    Highlights.clearMark(2);
                    Writes.insert(array, left, i, 1, true, false);
                }
            }
            if (colors) Highlights.colorCode("jeren", end - 1);
        }
        if (bSize < 3) return;
        for (int i = start, p = start; i + 2 * bSize <= end; i += bSize) if (i + bSize >= p) p = twoBlocks(array, i, i + bSize, i + 2 * bSize, tStart, end, bSize, useBuffers, depth);
    }

    protected void nudge(int[] array, int start, int mid, int end, int depth) {
        Writes.recordDepth(depth);
        colorRange("nudgeL", java.awt.Color.getHSBColor(0f, 0.5f, 1f), start, mid);
        colorRange("nudgeR", java.awt.Color.getHSBColor(0.333f, 0.5f, 1f), mid, end);
        boolean dir = mid - start < end - mid;
        int sizeOver = Math.min(mid - start, end - mid), left = dir ? start + sizeOver : end - sizeOver - 1, right = dir ? start + 2 * sizeOver : end - 2 * sizeOver - 1, pos = 0;
        colorRange("nudgeM", java.awt.Color.getHSBColor(0.667f, 0.5f, 1f), dir ? left : right + 1, dir ? right : left);
        while (right < end && right >= start && pos < sizeOver) {
            int target = dir ? minExponentialSearch(array, right, end, array[start + pos], true, 1, true) : maxExponentialSearch(array, start, right + 1, array[end - pos - 1], false, 1, true);
            while ((dir && right < target) || (!dir && right >= target)) colorSwap(array, dir ? left++ : left--, dir ? right++ : right--);
            colorSwap(array, dir ? start + pos++ : end - pos++ - 1, dir ? left++ : left--);
        }
        while (pos < sizeOver) colorSwap(array, dir ? start + pos++ : left--, dir ? left++ : end - pos++ - 1);
        insertions(array, dir ? start : mid, dir ? mid : end);
    }

    public void giveUpFair(int[] array, int start, int mid, int end, boolean side, boolean searches, boolean pre, int depth) {
        Writes.recordDepth(depth);
        while (true) {
            if (pre) insertions(array, side ? start : mid, side ? mid : end);
            if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return;
            if (searches) start = minExponentialSearch(array, start, mid, array[mid], false, 1, true);
            if (searches) end = maxExponentialSearch(array, mid, end, array[mid - 1], true, 1, true);
            if (doesRotate(array, start, mid, end)) return;
            else if (Math.min(mid - start, end - mid) < 10) {
                colorRange("small", java.awt.Color.getHSBColor(0.8f, 1f, 1f), start, end);
                grailMergeWithoutBuffer(array, start, mid - start, end - mid);
                return;
            } else if ((double) ((double) (mid - start) / (double) (end - mid)) > 0.66667 && (double) ((double) (mid - start) / (double) (end - mid)) < 1.5) {
                Writes.recursion();
                bismuthSort(array, start, end, 0, true, true, depth + 1);
                return;
            } else {
                nudge(array, start, mid, end, depth);
                pre = false;
                searches = true;
            }
        }
    }

    public void bismuthSort(int[] array, int start, int end, int getSize, boolean useBuffers, boolean silentCase, int depth) {
        Writes.recordDepth(depth);
        Highlights.retainColorMarks(true);
        getSize = getSize == 0 ? (int) Math.sqrt(end - start) : Math.min(getSize, end - start);
        String caseType = silentCase ? "" : end - start < 33 ? "SMALL" : getSize == 1 ? "1A" : getSize == 2 ? "1B" : getSize < 11 ? "1C" : !useBuffers && getSize < 33 ? "1D" : !useBuffers && getSize > 32 ? "1E" : getSize < 33 ? "2A" : getSize > 32 ? "2B" : "UNKNOWN";
        if (!caseType.equals("")) arrayVisualizer.setExtraHeading(" / bSize = " + getSize + " (Case " + caseType + ")");
        int balance = (end - start) % getSize, deadEnd = end - balance, i = getSize < 11 || !useBuffers ? start : start + getSize;
        if (end - start < 33) {
            insertions(array, start, end);
            return;
        }
        if (getSize > 10 && useBuffers) colorRange(depth > 0 ? "subBuffer" : "buffer", depth > 0 ? java.awt.Color.GRAY : java.awt.Color.DARK_GRAY, start, start + getSize);
        for (; i + getSize <= deadEnd; i += getSize) insertions(array, i, i + getSize);
        for (int cur = getSize * 2; cur <= 2 * (deadEnd - start); cur *= 2) {
            int s = getSize > 10 && useBuffers ? start + getSize : start;
            for (; s + cur <= deadEnd; s += cur) merge(array, s, s + cur, s + cur / 2, start, getSize, useBuffers, depth);
            if (s + cur / 2 <= deadEnd) merge(array, s, deadEnd, s + cur / 2, start, getSize, useBuffers, depth);
        }
        if (getSize > 10 && useBuffers) giveUpFair(array, start, start + getSize, deadEnd, true, true, true, depth);
        if (balance > 0) giveUpFair(array, start, deadEnd, end, false, true, true, depth);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        bismuthSort(array, 0, currentLength, getSize, true, false, 0);
    }
}