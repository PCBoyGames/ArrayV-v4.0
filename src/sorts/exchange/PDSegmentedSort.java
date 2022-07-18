package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;
import utils.Rotations;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDSegmentedSort extends GrailSorting {

    int segmentcount;
    boolean initlimit;
    int initend;
    int lastcheck;

    public PDSegmentedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Segmented");
        this.setRunAllSortsName("Pattern-Defeating Segmented Sort");
        this.setRunSortName("Pattern-Defeating Segmented Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void handleReversal(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth);
        Statistics.addStat("Rotation");
        IndexedRotations.holyGriesMills(array, a, a + ((b - a) / 2) + 1, b + 1, 0.1, true, false);
        if ((b - a) / 2 >= 1) {
            Writes.recursion();
            handleReversal(array, a, a + ((b - a) / 2), depth + 1);
            Writes.recursion();
            handleReversal(array, a + ((b - a) / 2) + 1, b, depth + 1);
        }
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Statistics.addStat("Rotation");
        if (lenA % lenB == 0 || lenB % lenA == 0) Rotations.holyGriesMills(array, pos, lenA, lenB, 0.1, true, false);
        else Rotations.cycleReverse(array, pos, lenA, lenB, 0.1, true, false);
    }

    protected void rotateReversal(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth);
        int reallen = b - a + 1;
        int len = 2;
        for (; len <= reallen; len *= 2);
        len /= 2;
        handleReversal(array, a, a + len - 1, depth);
        if (len < reallen) {
            Writes.recursion();
            rotateReversal(array, a + len, b, depth + 1);
            grailRotate(array, a, len, reallen - len);
        }
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b) return i;
        int cmp = Reads.compareIndices(array, i - 1, i++, 0.1, true);
        while (cmp == 0 && i < b) cmp = Reads.compareIndices(array, i - 1, i++, 0.1, true);
        if (cmp == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.1, true) >= 0) i++;
            rotateReversal(array, a, i - 1, 0);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 0.1, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    protected boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected boolean firstSegments(int[] array, int end) {
        lastcheck = segmentcount;
        segmentcount = 0;
        int last = 0;
        boolean anyswaps = false;
        for (int i = 0; i < end; i++) {
            int a = i;
            while (Reads.compareIndices(array, i, i + 1, 0.01, true) <= 0 && i + 1 < end) i++;
            int b1 = i;
            int b2 = i + 1;
            if (i + 1 < end) {
                if (Reads.compareIndices(array, a, b2, 0.01, true) > 0) {
                    i++;
                    while (Reads.compareIndices(array, i, i + 1, 0.01, true) <= 0 && i + 1 < end) i++;
                    int c1 = i;
                    Highlights.clearMark(2);
                    grailRotate(array, a, b2 - a, c1 - b1);
                    i = a - 1;
                    anyswaps = true;
                    last = c1;
                } else {
                    i = b1;
                    segmentcount++;
                }
            }
        }
        if (initlimit) initend = last;
        else if (segmentcount == lastcheck) initlimit = true;
        return anyswaps;
    }

    protected void checkSegments(int[] array, int end) {
        segmentcount = 0;
        for (int i = 0; i + 1 < end && segmentcount < 2; i++) if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) segmentcount++;
    }

    protected int secondSegments(int[] array, int start, int end) {
        segmentcount = 0;
        int trackthis = 0;
        int getend = start + 1;
        boolean foundend = false;
        for (int i = start; i < end && trackthis < 2; i++) {
            int a = i;
            while (Reads.compareIndices(array, i, i + 1, 0.01, true) <= 0 && i + 1 < end) i++;
            int b1 = i;
            int b2 = i + 1;
            if (i + 1 < end) {
                if (Reads.compareIndices(array, a, b2, 0.01, true) > 0) {
                    i++;
                    while (Reads.compareIndices(array, i, i + 1, 0.01, true) <= 0 && i + 1 < end) i++;
                    int c1 = i;
                    Highlights.clearMark(2);
                    grailRotate(array, a, b2 - a, c1 - b1);
                    i = a - 1;
                    trackthis = 0;
                } else {
                    i = b1;
                    segmentcount++;
                    trackthis++;
                    if (!foundend) {
                        foundend = true;
                        getend = b2;
                    }
                }
            }
        }
        return getend;
    }

    protected int nextStart(int[] array, int start, int sel, int end) {
        int i = start + 1;
        while (Reads.compareIndices(array, i, sel, 0.1, true) <= 0 && i < end) i++;
        return i;
    }

    protected void blocksert(int[] array, int a, int b) {
        int i, j, len;
        i = findRun(array, a, b);
        while (i < b) {
            j = findRun(array, i, b);
            len = j - i;
            grailMergeWithoutBuffer(array, a, i - a, len);
            i = j;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Statistics.putStat("Rotation");
        boolean two = false;
        int twoi = 0;
        if (patternDefeat(array, 0, currentLength)) return;
        initlimit = false;
        initend = currentLength;
        lastcheck = currentLength;
        while (firstSegments(array, initend));
        checkSegments(array, currentLength);
        for (int start = 1; start < currentLength;) {
            if (segmentcount == 1) {
                two = true;
                twoi = start;
            }
            if (segmentcount <= 1) break;
            Highlights.markArray(3, start);
            int sel = secondSegments(array, start, currentLength);
            start = nextStart(array, start, sel, currentLength);
        }
        if (two) {
            Highlights.clearAllMarks();
            blocksert(array, twoi, currentLength);
        }
    }
}