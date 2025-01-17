package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SmoothSort extends Sort {
    public SmoothSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Smooth");
        this.setRunAllSortsName("Smooth Sort");
        this.setRunSortName("Smoothsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // SMOOTH SORT - Provided here:
    // https://stackoverflow.com/questions/1390832/how-to-sort-nearly-sorted-array-in-the-fastest-time-possible-java/28352545#28352545

    static int LP[] = {1, 1, 3, 5, 9, 15, 25, 41, 67, 109,
            177, 287, 465, 753, 1219, 1973, 3193, 5167, 8361, 13529, 21891,
            35421, 57313, 92735, 150049, 242785, 392835, 635621, 1028457,
            1664079, 2692537, 4356617, 7049155, 11405773, 18454929, 29860703,
            48315633, 78176337, 126491971, 204668309, 331160281, 535828591,
            866988873};
            // the next number is > 31 bits.

    private void sift(int[] A, int pshift, int head, boolean shuffle)
    {
        // we do not use Floyd's improvements to the heapsort sift, because we
        // are not doing what heapsort does - always moving nodes from near
        // the bottom of the tree to the root.

        int val = A[head];

        while (pshift > 1)
        {
            int rt = head - 1;
            int lf = head - 1 - LP[pshift - 2];

            Highlights.markArray(2, rt);
            Highlights.markArray(3, lf);

            Delays.sleep(shuffle ? 0 : 0.325);

            if (Reads.compareValues(val, A[lf]) >= 0 && Reads.compareValues(val, A[rt]) >= 0)
                break;

            if (Reads.compareValues(A[lf], A[rt]) >= 0) {
                Writes.write(A, head, A[lf], shuffle ? 0 : 0.65, true, false);
                head = lf;
                pshift -= 1;
            }
            else {
                Writes.write(A, head, A[rt], shuffle ? 0 : 0.65, true, false);
                head = rt;
                pshift -= 2;
            }
        }
        Writes.write(A, head, val, shuffle ? 0 : 0.65, true, false);

        Highlights.clearMark(2);
        Highlights.clearMark(3);
    }

    private void trinkle(int[] A, int p, int pshift, int head, boolean isTrusty, boolean shuffle)
    {
        int val = A[head];

        while (p != 1)
        {
            int stepson = head - LP[pshift];

            if (Reads.compareValues(A[stepson], val) <= 0)
                break; // current node is greater than head. sift.

            // no need to check this if we know the current node is trusty,
            // because we just checked the head (which is val, in the first
            // iteration)
            if (!isTrusty && pshift > 1) {
                int rt = head - 1;
                int lf = head - 1 - LP[pshift - 2];
                Highlights.markArray(2, rt);
                Highlights.markArray(3, lf);

                Delays.sleep(shuffle ? 0 : 0.325);

                if (Reads.compareValues(A[rt], A[stepson]) >= 0 ||
                    Reads.compareValues(A[lf], A[stepson]) >= 0)
                    break;
            }
            Writes.write(A, head, A[stepson], shuffle ? 0 : 0.65, true, false);

            Highlights.clearMark(2);
            Highlights.clearMark(3);

            head = stepson;
            //int trail = Integer.numberOfTrailingZeros(p & ~1);
            int trail = Integer.numberOfTrailingZeros(p & ~1);
            p >>= trail;
            pshift += trail;
            isTrusty = false;
        }

        if (!isTrusty) {
            Writes.write(A, head, val, shuffle ? 0 : 0.65, true, false);
            this.sift(A, pshift, head, shuffle);
        }
    }

    public void smoothSort(int[] A, int lo, int hi, boolean fullSort, boolean shuffle)
    {
        int head = lo; // the offset of the first element of the prefix into m

        // These variables need a little explaining. If our string of heaps
        // is of length 38, then the heaps will be of size 25+9+3+1, which are
        // Leonardo numbers 6, 4, 2, 1.
        // Turning this into a binary number, we get b01010110 = 0x56. We represent
        // this number as a pair of numbers by right-shifting all the zeros and
        // storing the mantissa and exponent as "p" and "pshift".
        // This is handy, because the exponent is the index into L[] giving the
        // size of the rightmost heap, and because we can instantly find out if
        // the rightmost two heaps are consecutive Leonardo numbers by checking
        // (p&3)==3

        int p = 1; // the bitmap of the current standard concatenation >> pshift
        int pshift = 1;

        while (head < hi)
        {
            if ((p & 3) == 3) {
                // Add 1 by merging the first two blocks into a larger one.
                // The next Leonardo number is one bigger.
                this.sift(A, pshift, head, shuffle);
                p >>= 2;
                pshift += 2;
            }
            else {
                // adding a new block of length 1
                if (LP[pshift - 1] >= hi - head) {
                    // this block is its size.
                    this.trinkle(A, p, pshift, head, false, shuffle);
                } else {
                    // this block will get merged. Just make it trusty.
                    this.sift(A, pshift, head, shuffle);
                }

                if (pshift == 1) {
                    // LP[1] is being used, so we add use LP[0]
                    p <<= 1;
                    pshift--;
                } else {
                    // shift out to position 1, add LP[1]
                    p <<= (pshift - 1);
                    pshift = 1;
                }
            }
            p |= 1;
            head++;
        }

        if (fullSort) {
            this.trinkle(A, p, pshift, head, false, shuffle);

            while (pshift != 1 || p != 1)
            {
                if (pshift <= 1) {
                    // block of length 1. No fiddling needed
                    int trail = Integer.numberOfTrailingZeros(p & ~1);
                    p >>= trail;
                pshift += trail;
                }
                else {
                    p <<= 2;
                    p ^= 7;
                    pshift -= 2;

                    // This block gets broken into three bits. The rightmost bit is a
                    // block of length 1. The left hand part is split into two, a block
                    // of length LP[pshift+1] and one of LP[pshift].  Both these two
                    // are appropriately heapified, but the root nodes are not
                    // necessarily in order. We therefore semitrinkle both of them

                    this.trinkle(A, p >> 1, pshift + 1, head - LP[pshift] - 1, true, shuffle);
                    this.trinkle(A, p, pshift, head - 1, true, shuffle);
                }
                head--;
            }
        }
    }

    public void smoothHeapify(int[] array, int length, boolean shuffle) {
        this.smoothSort(array, 0, length - 1, false, shuffle);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.smoothSort(array, 0, currentLength - 1, true, false);
    }
}