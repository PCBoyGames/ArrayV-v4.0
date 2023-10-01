package sorts.merge;

import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * Powersort implementation as described in the paper.
 *
 * Natural runs are extended to minRunLen if needed before we continue
 * merging.
 * Unless useMsbMergeType is false, node powers are computed using
 * a most-significant-bit trick;
 * otherwise a loop is used.
 * If onlyIncreasingRuns is true, only weakly increasing runs are picked up.
 *
 * @author Sebastian Wild (wild@uwaterloo.ca)
 * @license: MIT
 */
public class PowerSort extends Sort {
    private boolean useMsbMergeType; // if (minRunLen > 1 && (!useMsbMergeType || onlyIncreasingRuns))
                                     // returns false if (msb true and increasing false)
    private boolean onlyIncreasingRuns;

    private static int minRunLen = 0;
    private MergesAndRuns MergesAndRuns;
    private Insertionsort Insertionsort;

    public PowerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Power");
        this.setRunAllSortsName("Power Sort");
        this.setRunSortName("Powersort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Select a mode:\n0: No MSB, No only increasing runs\n1 (default): MSB, only increasing runs\n2: MSB, no only increasing runs", 1);
        this.MergesAndRuns = new MergesAndRuns();
        this.Insertionsort = new Insertionsort();
    }

    @Override
    public void runSort(int[] A, int right, int bucketCount) throws Exception {
        switch (bucketCount) {
            case 0:
                useMsbMergeType = false;
                onlyIncreasingRuns = false;
                break;
            default:
                useMsbMergeType = true;
                onlyIncreasingRuns = true;
                break;
            case 2:
                useMsbMergeType = true;
                onlyIncreasingRuns = false;
                minRunLen = 16;
                break;
        }
        if (useMsbMergeType) {
            if (onlyIncreasingRuns)
                powersortIncreasingOnlyMSB(A, 0, right - 1);
            else
                powersort(A, 0, right - 1);
        } else {
            powersortBitWise(A, 0, right - 1);
        }
    }

    private static int nodePower(int left, int right, int startA, int startB, int endB) {
        int n = (right - left + 1);
        long l = (long) startA + (long) startB - ((long) left << 1); // 2*middleA
        long r = (long) startB + (long) endB + 1 - ((long) left << 1); // 2*middleB
        int a = (int) ((l << 30) / n); // middleA / 2n
        int b = (int) ((r << 30) / n); // middleB / 2n
        return Integer.numberOfLeadingZeros(a ^ b);
    }

    private static int nodePowerBitwise(int left, int right, int startA, int startB, int endB) {
        assert right < (1 << 30); // otherwise nt2, l and r will overflow
        int n = right - left + 1;
        int l = startA - (left << 1) + startB;
        int r = startB - (left << 1) + endB + 1;
        // a and b are given by l/nt2 and r/nt2, both are in [0,1).
        // we have to find the number of common digits in the
        // binary representation in the fractional part.
        int nCommonBits = 0;
        boolean digitA = l >= n, digitB = r >= n;
        while (digitA == digitB) {
            ++nCommonBits;
            // if (digitA) { l -= n; r -= n; }
            l -= digitA ? n : 0;
            r -= digitA ? n : 0;
            l <<= 1;
            r <<= 1;
            digitA = l >= n;
            digitB = r >= n;
        }
        return nCommonBits + 1;
    }

    private static int NULL_INDEX = Integer.MIN_VALUE;

    public void powersort(int[] A, int left, int right) {
        int n = right - left + 1;
        int lgnPlus2 = log2(n) + 2;
        int[] leftRunStart = Writes.createExternalArray(lgnPlus2), leftRunEnd = Writes.createExternalArray(lgnPlus2);
        Arrays.fill(leftRunStart, NULL_INDEX);
        int top = 0;
        int[] buffer = Writes.createExternalArray(n);

        int startA = left, endA = MergesAndRuns.extendAndReverseRunRight(A, startA, right);
        // extend to minRunLen
        int lenA = endA - startA + 1;
        if (lenA < minRunLen) {
            endA = Math.min(right, startA + minRunLen - 1);
            Insertionsort.insertionsort(A, startA, endA, lenA);
        }
        while (endA < right) {
            int startB = endA + 1, endB = MergesAndRuns.extendAndReverseRunRight(A, startB, right);
            // extend to minRunLen
            int lenB = endB - startB + 1;
            if (lenB < minRunLen) {
                endB = Math.min(right, startB + minRunLen - 1);
                Insertionsort.insertionsort(A, startB, endB, lenB);
            }
            int k = nodePower(left, right, startA, startB, endB);
            assert k != top;
            for (int l = top; l > k; --l) {
                if (leftRunStart[l] == NULL_INDEX)
                    continue;
                MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, endA, buffer);
                startA = leftRunStart[l];
                Writes.write(leftRunStart, l, NULL_INDEX, 0.5, true, true);
            }
            // store left half of merge between A and B
            Writes.write(leftRunStart, k, startA, 0.5, true, true);
            Writes.write(leftRunEnd, k, endA, 0.5, true, true);
            top = k;
            startA = startB;
            endA = endB;
        }
        assert endA == right;
        for (int l = top; l > 0; --l) {
            if (leftRunStart[l] == NULL_INDEX)
                continue;
            MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, right, buffer);
        }
        Writes.deleteExternalArrays(leftRunStart, leftRunEnd, buffer);
    }

    public void powersortBitWise(int[] A, int left, int right) {
        int n = right - left + 1;
        int lgnPlus2 = log2(n) + 2;
        int[] leftRunStart = Writes.createExternalArray(lgnPlus2), leftRunEnd = Writes.createExternalArray(lgnPlus2);
        Arrays.fill(leftRunStart, NULL_INDEX);
        int top = 0;
        int[] buffer = Writes.createExternalArray(n);

        int startA = left, endA = MergesAndRuns.extendAndReverseRunRight(A, startA, right);
        while (endA < right) {
            int startB = endA + 1, endB = MergesAndRuns.extendAndReverseRunRight(A, startB, right);
            int k = nodePowerBitwise(left, right, startA, startB, endB);
            assert k != top;
            // clear left subtree bottom-up if needed
            for (int l = top; l > k; --l) {
                if (leftRunStart[l] == NULL_INDEX)
                    continue;
                MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, endA, buffer);
                startA = leftRunStart[l];
                Writes.write(leftRunStart, l, NULL_INDEX, 0.5, true, true);
            }
            // store left half of merge between A and B
            Writes.write(leftRunStart, k, startA, 0.5, true, true);
            Writes.write(leftRunEnd, k, endA, 0.5, true, true);
            top = k;
            startA = startB;
            endA = endB;
        }
        assert endA == right;
        for (int l = top; l > 0; --l) {
            if (leftRunStart[l] == NULL_INDEX)
                continue;
            MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, right, buffer);
        }
        Writes.deleteExternalArrays(leftRunStart, leftRunEnd, buffer);
    }

    public void powersortIncreasingOnlyMSB(int[] A, int left, int right) {
        int n = right - left + 1;
        int lgnPlus2 = log2(n) + 2;
        int[] leftRunStart = Writes.createExternalArray(lgnPlus2), leftRunEnd = Writes.createExternalArray(lgnPlus2);
        Arrays.fill(leftRunStart, NULL_INDEX);
        int top = 0;
        int[] buffer = Writes.createExternalArray(n);

        int startA = left, endA = MergesAndRuns.extendWeaklyIncreasingRunRight(A, startA, right);
        while (endA < right) {
            int startB = endA + 1, endB = MergesAndRuns.extendWeaklyIncreasingRunRight(A, startB, right);
            int k = nodePower(left, right, startA, startB, endB);
            assert k != top;
            // clear left subtree bottom-up if needed
            for (int l = top; l > k; --l) {
                if (leftRunStart[l] == NULL_INDEX)
                    continue;
                MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, endA, buffer);
                startA = leftRunStart[l];
                Writes.write(leftRunStart, l, NULL_INDEX, 0.5, true, true);
            }
            // store left half of merge between A and B
            Writes.write(leftRunStart, k, startA, 0.5, true, true);
            Writes.write(leftRunEnd, k, endA, 0.5, true, true);
            top = k;
            startA = startB;
            endA = endB;
        }
        assert endA == right;
        for (int l = top; l > 0; --l) {
            if (leftRunStart[l] == NULL_INDEX)
                continue;
            MergesAndRuns.mergeRuns(A, leftRunStart[l], leftRunEnd[l] + 1, right, buffer);
        }
        Writes.deleteExternalArrays(leftRunStart, leftRunEnd, buffer);
    }

    public static int log2(int n) {
        if (n == 0)
            throw new IllegalArgumentException("lg(0) undefined");
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    /**
     * Merging procedures and some related helpers
     *
     * @author Sebastian Wild (wild@uwaterloo.ca)
     */
    public class MergesAndRuns {

        /** turns on the counting of merge costs */
        public boolean COUNT_MERGE_COSTS = true;
        /** total merge costs of all merge calls */
        public long totalMergeCosts = 0;

        /**
         * Merges runs A[l..m-1] and A[m..r] in-place into A[l..r]
         * with Sedgewick's bitonic merge (Program 8.2 in Algorithms in C++)
         * using B as temporary storage.
         * B.length must be at least r+1.
         */
        public void mergeRuns(int[] A, int l, int m, int r, int[] B) {
            --m;// mismatch in convention with Sedgewick
            int i, j;
            assert B.length >= r + 1;
            if (COUNT_MERGE_COSTS)
                totalMergeCosts += (r - l + 1);
            for (i = m + 1; i > l; --i)
                Writes.write(B, i - 1, A[i - 1], 0.5, true, true);
            for (j = m; j < r; ++j)
                Writes.write(B, r + m - j, A[j + 1], 0.5, true, true);
            for (int k = l; k <= r; ++k)
                Writes.write(A, k, Reads.compareIndices(B, j, i, 0.5, true) < 0 ? B[j--] : B[i++], 0.5, true, false);
        }

        /**
         * Merges runs A[l..m-1] and A[m..r] in-place into A[l..r]
         * by copying the shorter run into temporary storage B and
         * merging back into A.
         * B.length must be at least min(m-l,r-m+1)
         */
        public void mergeRunsCopyHalf(int[] A, int l, int m, int r, int[] B) {
            int n1 = m - l, n2 = r - m + 1;
            if (COUNT_MERGE_COSTS)
                totalMergeCosts += (n1 + n2);
            assert B.length >= n1 || B.length >= n2;
            if (n1 <= n2) {
                Writes.arraycopy(A, l, B, 0, n1, 1, true, true);
                int i1 = 0, i2 = m, o = l;
                while (i1 < n1 && i2 <= r)
                    Writes.write(A, o++, Reads.compareIndices(B, i1, i2, 0.5, true) <= 0 ? B[i1++] : A[i2++], 0.5, true,
                            false);
                while (i1 < n1)
                    Writes.write(A, o++, B[i1++], 0.5, true, false);
            } else {
                System.arraycopy(A, m, B, 0, n2);
                int i1 = m - 1, i2 = n2 - 1, o = r;
                while (i1 >= l && i2 >= 0)
                    Writes.write(A, o--, Reads.compareIndices(A, i1, i2, 0.5, true) <= 0 ? B[i2--] : A[i1--], 0.5, true,
                            false);
                while (i2 >= 0)
                    Writes.write(A, o--, B[i2--], 0.5, true, false);
            }
        }

        /**
         * Reverse the specified range of the specified array.
         *
         * @param a  the array in which a range is to be reversed
         * @param lo the index of the first element in the range to be
         *           reversed
         * @param hi the index of the last element in the range to be
         *           reversed
         */
        public void reverseRange(int[] a, int lo, int hi) {
            while (lo < hi) {
                int t = a[lo];
                Writes.write(a, lo++, a[hi], 0.5, true, false);
                Writes.write(a, hi--, t, 0.5, true, false);
            }
        }

        public int extendWeaklyIncreasingRunLeft(int[] A, int i, int left) {
            while (i > left && Reads.compareIndices(A, i - 1, i, 0.5, true) <= 0)
                --i;
            return i;
        }

        public int extendWeaklyIncreasingRunRight(int[] A, int i, int right) {
            while (i < right && Reads.compareIndices(A, i + 1, i, 0.5, true) >= 0)
                ++i;
            return i;
        }

        public int extendStrictlyDecreasingRunLeft(int[] A, int i, int left) {
            while (i > left && Reads.compareIndices(A, i - 1, i, 0.5, true) > 0)
                --i;
            return i;
        }

        public int extendStrictlyDecreasingRunRight(int[] A, int i, int right) {
            while (i < right && Reads.compareIndices(A, i + 1, i, 0.5, true) < 0)
                ++i;
            return i;
        }

        public int extendAndReverseRunRight(int[] A, int i, int right) {
            assert i <= right;
            int j = i;
            if (j == right)
                return j;
            // Find end of run, and reverse range if descending
            if (Reads.compareIndices(A, j, ++j, 0.5, true) > 0) { // Strictly Descending
                while (j < right && Reads.compareIndices(A, j + 1, j, 0.5, true) < 0)
                    ++j;
                reverseRange(A, i, j);
            } else { // Weakly Ascending
                while (j < right && Reads.compareIndices(A, j + 1, j, 0.5, true) >= 0)
                    ++j;
            }
            return j;
        }

        public int extendAndReverseRunLeft(int[] A, int j, int left) {
            assert j >= left;
            int i = j;
            if (i == left)
                return i;
            // Find end of run, and reverse range if descending
            if (Reads.compareIndices(A, i, --i, 0.5, true) < 0) { // Strictly Descending
                while (i > left && Reads.compareIndices(A, i - 1, i, 0.5, true) > 0)
                    --i;
                reverseRange(A, i, j);
            } else { // Weakly Ascending
                while (i > left && Reads.compareIndices(A, i - 1, i, 0.5, true) <= 0)
                    --i;
            }
            return i;
        }
    }

    /**
     * Straight-insertion sort implementations
     *
     * @author Sebastian Wild (wild@uwaterloo.ca)
     */
    public class Insertionsort {

        /** Sort A[left..right] by straight-insertion sort (both endpoints inclusive) */
        public void insertionsort(int[] A, int left, int right) {
            for (int i = left + 1; i <= right; ++i) {
                int j = i - 1;
                int v = A[i];
                while (Reads.compareValues(v, A[j]) < 0) {
                    Writes.write(A, j + 1, A[j], 0.5, true, false);
                    --j;
                    if (j < left)
                        break;
                }
                Writes.write(A, j + 1, v, 0.5, true, false);
            }
        }

        /**
         * Sort A[left..right] by straight-insertion sort (both endpoints
         * inclusive), assuming the leftmost nPresorted elements form a weakly
         * increasing run
         */
        public void insertionsort(int[] A, int left, int right, int nPresorted) {
            assert right >= left;
            assert right - left + 1 >= nPresorted;
            for (int i = left + nPresorted; i <= right; ++i) {
                int j = i - 1;
                int v = A[i];
                while (Reads.compareValues(v, A[j]) < 0) {
                    Writes.write(A, j + 1, A[j], 0.5, true, false);
                    --j;
                    if (j < left)
                        break;
                }
                Writes.write(A, j + 1, v, 0.5, true, false);
            }
        }

        /**
         * Sort A[left..right] by straight-insertion sort (both endpoints
         * inclusive), assuming the rightmost nPresorted elements form a weakly
         * increasing run
         */
        public void insertionsortRight(int[] A, int left, int right, int nPresorted) {
            assert right >= left;
            assert right - left + 1 >= nPresorted;
            for (int i = right - nPresorted; i >= left; --i) {
                int j = i + 1;
                int v = A[i];
                while (Reads.compareValues(v, A[j]) > 0) {
                    Writes.write(A, j - 1, A[j], 0.5, true, false);
                    ++j;
                    if (j > right)
                        break;
                }
                Writes.write(A, j - 1, v, 0.5, true, false);
            }
        }

    }
}
