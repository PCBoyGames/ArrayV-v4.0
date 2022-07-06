package sorts.golf;

import main.ArrayVisualizer;
import sorts.insert.BranchlessBinaryInsertionSort;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;

public class ZeroMergeSort extends GrailSorting {
    public ZeroMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Zero");
        this.setRunAllSortsName("Zero Sort");
        this.setRunSortName("Zerosort");
        this.setCategory("Golf Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int buf, bufsz;
    private static final int minBinsert = 8;
    private BranchlessBinaryInsertionSort binserter;

    protected void grailRotate(int[] array, int pos, int lena, int lenb) {
        IndexedRotations.neon(array, pos, pos+lena, pos+lena+lenb, 1, true, false);
    }

    // fw function only, gonna make bw function soon
    private void zeromerge(int[] array, int start, int mid, int end) {
        int l = start, r = mid;
        int lBuf = buf, rBuf = mid, lSz = bufsz, rSz = 0;
        while (l < mid || r < end) {
            while (l < mid && r < end && lSz > 0) {
                if (Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, lBuf++, l++, 1.25, true, false);
                } else {
                    Writes.swap(array, lBuf++, r++, 1.25, true, false);
                    lSz--;
                    rSz++;
                }
            }
            if (lSz > 0) {
                while (l < mid) {
                    Writes.swap(array, lBuf++, l++, 1.25, true, false);
                }
                while (r < end && lSz > 0) {
                    Writes.swap(array, lBuf++, r++, 1.25, true, false);
                    lSz--;
                    rSz++;
                }
            }
            while (l < mid && r < end && rSz > 0) {
                if (Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, rBuf++, l++, 1.25, true, false);
                    rSz--;
                    lSz++;
                } else {
                    Writes.swap(array, rBuf++, r++, 1.25, true, false);
                }
            }
            if (rSz > 0) {
                while (l < mid && rSz > 0) {
                    Writes.swap(array, rBuf++, l++, 1.25, true, false);
                    rSz--;
                    lSz++;
                }
                while (r < end) {
                    Writes.swap(array, rBuf++, r++, 1.25, true, false);
                }
            }
            IndexedRotations.neon(array, lBuf, mid, rBuf+rSz, 2.5, true, false);
            l += rBuf-mid+rSz;
            lBuf += rBuf-mid+rSz;
            mid = r = rBuf+rSz;
            rSz=0;
        }
        buf = end-bufsz;
    }

    private void binsertruns(int[] array, int start, int end) {
        int m=Math.max(2*minBinsert, bufsz);
        for (int i=start; i<end; i+=m) {
            binserter.binaryInsertSort(array, i, Math.min(i+m, end), 0.025, 0.05);
        }
    }

    public void runZero(int[] array, int start, int end) {
        buf = start;
        bufsz = (int) Math.pow(end-start, 0.5d);
        binserter = new BranchlessBinaryInsertionSort(arrayVisualizer);
        if (bufsz < 4) {
            binserter.binaryInsertSort(array, start, end, 0.5, 0.5);
            return;
        }
        binsertruns(array, buf+bufsz, end);
        for (int j=Math.max(bufsz, 2*minBinsert); j<end-start; j*=2) {
            for (int i=buf+bufsz; i<end; i+=2*j) {
                if (i+j > end)
                    break;
                if (i+2*j > end) {
                    zeromerge(array, i, i+j, end);
                } else {
                    zeromerge(array, i, i+j, i+2*j);
                }
            }
            IndexedRotations.neon(array, start, buf, buf+bufsz, 1, true, false);
            buf=start;
        }
        binserter.binaryInsertSort(array, buf, buf+bufsz, 0.25, 0.05);
        grailMergeWithoutBuffer(array, buf, bufsz, end-(buf+bufsz));
    }

    @Override
    public void runSort(int[] array, int len, int buck) {
        runZero(array, 0, len);
    }
}