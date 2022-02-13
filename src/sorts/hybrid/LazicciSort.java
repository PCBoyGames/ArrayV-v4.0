package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;

final public class LazicciSort extends GrailSorting {
    public LazicciSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazicci Stable");
        this.setRunAllSortsName("Lazicci Stable Sort");
        this.setRunSortName("Lazicci Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int runs(int len) {
        int j = 1, l = 0;
        while(j < len) {
            j*=10;
            l++;
        }
        return l*l;
    }

    // n^0.5 (log n iterations)
    private int sqrt(int len) {
        int l = 0, h = len;
        while(l < h) {
            int m = l+(h-l)/2;
            if(m*m > len)
                h = m;
            else
                l = m + 1;
        }
        return l;
    }

    // n^0.66 (log^2 n? iterations)
    private int fcrt(int len) {
        int l = 0, h = len;
        while(l < h) {
            int m = l+(h-l)/2;
            if(m*sqrt(m) > len)
                h = m;
            else
                l = m + 1;
        }
        return l;
    }

    @Override
    protected void grailRotate(int[] array, int pos, int len1, int len2) {
        Rotations.cycleReverse(array, pos, len1, len2, 0.5, true, false);
    }

    // taken from PDIPop
    private void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
        else Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    private int findRun(int[] array, int start, int max) {
        if(start >= max - 1)
            return start+1;
        int c = Reads.compareIndices(array, start, ++start, 0.1, true),
            s = start-1,
            d = c;
        boolean stableRev = false;
        if(c == 0) {
            stableRev = true;
            c = -1;
        }
        while(start < max - 1 && (d == c || d == 0)) {
            d = Reads.compareIndices(array, start, start+1, 0.1, true);
            if(d == 0)
                stableRev = true;
            start++;
        }
        if(c == 1) {
            if(stableRev) {
                stableSegmentReversal(array, s, start-1);
            } else {
                Writes.reversal(array, s, start-1, 1, true, false);
            }
        }
        return start;
    }

    private int findSortedRunBW(int[] array, int start, int end) {
        do end--;
        while(end > start && Reads.compareValues(array[end-1], array[end]) <= 0);
        return Math.max(start, end);
    }

    private int mergeRuns(int[] array, int start, int end) {
        int z = runs(end-start),
            x = fcrt(end-start),
            s = start,
            r = start,
            d = 0;
        while(r < end) {
            int y = findRun(array, r, end);
            if(y >= r + x) {
                s = r = y;
                d = 0;
                continue;
            }
            if(d > 0) {
                grailMergeWithoutBuffer(array, s, r-s, y-r);
            }
            if(d + 1 == z) {
                s = y;
            }
            r = y;
            d = (d + 1) % z;
        }
        return s;
    }
    private void blockBack(int[] array, int start, int run, int end) {
        while(run > start) {
            int r = findSortedRunBW(array, start, run);
            grailMergeWithoutBuffer(array, r, run-r, end-run);
            run = r;
        }
    }
    public void lazicciStable(int[] array, int start, int end) {
        blockBack(array, start, mergeRuns(array, start, end), end);
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        blockBack(array, 0, mergeRuns(array, 0, currentLength), currentLength);
    }
}