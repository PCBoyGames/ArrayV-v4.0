package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.select.MaxHeapSort;

import java.util.Random;

/*
Copyright (c) 2023 thatsOven

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

/*
 * Unique Map Sort
 * Unstably sorts u distinct items (with u < n / (2 * log2 n) in
 * O(n log n) comparisons, O(n) moves, and O(1) space
 *
 * The sort can be modified to make O(n log u) comparisons by
 * removing the bit array (for example, with O(u) external space)
 *
 * Could be made stable with multi-way stable partitioning.
 */

/*
 * Autonomous Unique Map Sort
 * Unique Map Sort, but can sort on its own
 * O(n log n) comparisons, O(n) moves (i think?), and O(log n) space (worst case)
 */

public class AutoUMapSort extends Sort {
    public AutoUMapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Auto. Unique Map");
        this.setRunAllSortsName("Autonomous Unique Map Sort");
        this.setRunSortName("Autonomous Unique Map Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static final int SMALL_SORT = 256;

    private Random rng;

    private int log2(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    private class BitArray {
        private final int[] array;
        private final int pa, pb, w;

        public final int size, length;

        public BitArray(int[] array, int pa, int pb, int size, int w) {
            this.array  = array;
            this.pa     = pa;
            this.pb     = pb;
            this.size   = size;
            this.w      = w;
            this.length = size * w;
        }

        private void flipBit(int a, int b) {
            Writes.swap(array, a, b, 0.5, true, false);
        }

        private boolean getBit(int a, int b) {
            return Reads.compareIndices(array, a, b, 0.1, true) > 0;
        }

        private void setBit(int a, int b, boolean bit) {
            if (this.getBit(a, b) ^ bit)
                this.flipBit(a, b);
        }

        public void free() {
            int i1 = pa + length;
            for (int i = pa, j = pb; i < i1; i++, j++)
                this.setBit(i, j, false);
        }

        public void set(int idx, int val) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx * w, i1 = pa + s + w;
            for (int i = pa + s, j = pb + s; i < i1; i++, j++, val >>= 1)
                this.setBit(i, j, (val & 1) == 1);

            if (val > 0) System.out.println("Warning: Word too large");
        }

        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int r = 0, s = idx * w;
            for (int k = 0, i = pa + s, j = pb + s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;
            return r;
        }

        public void incr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx * w, i1 = pa + s + w;
            for (int i = pa + s, j = pb + s; i < i1; i++, j++) {
                this.flipBit(i, j);
                if (this.getBit(i, j)) return;
            }

            System.out.println("Warning: Integer overflow");
        }

        public void decr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx * w, i1 = pa + s + w;
            for (int i = pa + s, j = pb + s; i < i1; i++, j++) {
                this.flipBit(i, j);
                if (!this.getBit(i, j)) return;
            }

            System.out.println("Warning: Integer underflow");
        }
    }

    private void blockSwapFW(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 0.5, true, false);
    }

    private void blockSwapBW(int[] array, int a, int b, int len) {
        for (int i = len - 1; i >= 0; i--)
            Writes.swap(array, a + i, b + i, 0.5, true, false);
    }

    private void insertToLeft(int[] array, int from, int to) {
        Highlights.clearAllMarks();

        int tmp = array[from];
        for (int i = from - 1; i >= to; i--)
            Writes.write(array, i + 1, array[i], 0.25, true, false);
        Writes.write(array, to, tmp, 0.25, true, false);
    }

    private void insertToRight(int[] array, int from, int to) {
        Highlights.clearAllMarks();

        int tmp = array[from];
        for (int i = from; i < to; i++)
            Writes.write(array, i, array[i + 1], 0.25, true, false);
        Writes.write(array, to, tmp, 0.25, true, false);
    }

    public void rotate(int[] array, int a, int m, int b) {
        int rl = b - m,
            ll = m - a;

        while (rl > 1 && ll > 1) {
            if (rl < ll) {
                blockSwapFW(array, a, m, rl);
                a  += rl;
                ll -= rl;
            } else {
                b  -= ll;
                rl -= ll;
                blockSwapBW(array, a, b, ll);
            }
        }

        if      (rl == 1) insertToLeft( array, m, a);
        else if (ll == 1) insertToRight(array, a, b - 1);
    }

    private int binarySearch(int[] array, int a, int b, int value, boolean left) {
        Highlights.clearAllMarks();

        while (a < b) {
            int m = a + (b - a) / 2;

            int cmp = Reads.compareIndexValue(array, m, value, 0.25, true);
            if (left ? cmp >= 0 : cmp > 0)
                 b = m;
            else a = m + 1;
        }

        return a;
    }

    public int findKeys(int[] array, int a, int b, int q) {
        int n = 1,
            p = b - 1;

        for (int i = p; i > a && n < q; i--) {
            int l = binarySearch(array, p, p + n, array[i - 1], true) - p;
            if (l == n || Reads.compareIndices(array, i - 1, p + l, 1, true) < 0) {
                rotate(array, i, p, p + n++);
                p = i - 1;
                insertToRight(array, i - 1, p + l);
            }
        }

        rotate(array, p, p + n, b);
        return n;
    }

    private void mergeInPlaceBW(int[] array, int a, int m, int b) {
        int s = b - 1,
            l = m - 1;

        while (s > l && l >= a) {
            if (Reads.compareIndices(array, l, s, 0, false) > 0) {
                int p = this.binarySearch(array, a, l, array[s], false);
                rotate(array, p, l + 1, s + 1);
                s -= l + 1 - p;
                l = p - 1;
            } else s--;
        }
    }

    private int medianOf3(int[] array, int[] indices) {
		// small length cases

		// maybe an error would be better but w/e
		if (indices.length == 0) return -1;

		// median of 1 or 2 elements can just be the first
		if (indices.length < 3) return indices[0];

		// 3 element case (common)
		// only first 3 elements are considered if given an array of 4+ indices
		if (Reads.compareIndices(array, indices[0], indices[1], 0.5, true) <= 0) {
			if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) <= 0)
				return indices[1];
			if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) < 0)
				return indices[2];
			return indices[0];
		}
		if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) >= 0) {
			return indices[1];
		}
		if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) <= 0) {
			return indices[0];
		}
		return indices[2];
	}

	private int medianOf9(int[] array, int start, int end) {
		// anti-overflow with good rounding
		int  length = end - start;
		int    half =  length / 2;
		int quarter =    half / 2;
		int  eighth = quarter / 2;

		int[] elements0 = {start, start + eighth, start + quarter};
		int med0 = medianOf3(array, elements0);

		int[] elements1 = {start + quarter + eighth, start + half, start + half + eighth};
		int med1 = medianOf3(array, elements1);

		int[] elements2 = {start + half + quarter, start + half + quarter + eighth, end - 1};
		int med2 = medianOf3(array, elements2);

		return medianOf3(array, new int[] {med0, med1, med2});
	}

	private int mOMHelper(int[] array, int start, int length) {
		if (length == 1) return start;

		int[] meds = new int[3];
		int third = length / 3;
		meds[0] = mOMHelper(array, start, third);
		meds[1] = mOMHelper(array, start + third, third);
		meds[2] = mOMHelper(array, start + 2 * third, third);

		return medianOf3(array, meds);
	}

	private int medianOfMedians(int[] array, int start, int length) {
		if (length == 1) return start;

		int[] meds = new int[3];

		int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)));
		if (nearPower == length)
			return mOMHelper(array, start, length);

		nearPower /= 3;
		// uncommon but can happen with numbers slightly smaller than 2*3^k
		// (e.g., 17 < 18 or 47 < 54)
		if (2*nearPower >= length) nearPower /= 3;

		meds[0] = mOMHelper(array, start, nearPower);
		meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
		meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

		return medianOf3(array, meds);
	}

    private int partition(int[] array, int a, int b) {
        int i = a,
            j = b;

        while (true) {
            while (++i <  j && Reads.compareIndices(array, i, a, 0.25, true) < 0);
            while (--j >= i && Reads.compareIndices(array, j, a, 0.25, true) > 0);

            if (i < j) Writes.swap(array, i, j, 0.5, true, false);
            else {
                Writes.swap(array, a, j, 1, true, false);
                return j;
            }
        }
    }

    private void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0, false) < 0)
                insertToLeft(array, i, binarySearch(array, a, i, array[i], false));
    }

    private void quickSelect(int[] array, int a, int b, boolean badPartition, int k, int k0) {
		int a1 = a,
            b1 = b;

        while (b - a > 32) {
			int p;

			if (badPartition) {
				int n = b - a;
				n -= ~n & 1; // even lengths bad
				p = this.medianOfMedians(array, a, n);
				badPartition = false;
			} else p = this.medianOf9(array, a, b);

            Writes.swap(array, p, a, 0.5, true, false);
			int m = this.partition(array, a, b);

            if      (m > k0 && m < b1)     b1 = m;
            else if (m < k0 && m + 1 > a1) a1 = m + 1;
            else if (m == k0)              a1 = b1;

            if (m == k) break;

            int l = m - a,
                r = b - m;

            if (r < l) badPartition = r * 8 < l;
            else       badPartition = l * 8 < r;

            if (k < m) b = m;
            else       a = m + 1;
		}

        if (b - a <= 32) insertSort(array, a, b);

        while (b1 - a1 > 32) {
			int p;

			if (badPartition) {
				int n = b1 - a1;
				n -= ~n & 1; // even lengths bad
				p = this.medianOfMedians(array, a1, n);
				badPartition = false;
			} else p = this.medianOf9(array, a1, b1);

            Writes.swap(array, p, a1, 0.5, true, false);
			int m = this.partition(array, a1, b1);

            if (m == k0) return;

            int l = m - a1,
                r = b1 - m;

            if (r < l) badPartition = r * 8 < l;
            else       badPartition = l * 8 < r;

            if (k0 < m) b1 = m;
            else        a1 = m + 1;
		}

        if (b1 - a1 <= 32) insertSort(array, a1, b1);
	}

    private void sort(int[] array, int a, int b, int g, int f, BitArray bits) {
        MaxHeapSort heapSorter = new MaxHeapSort(arrayVisualizer);

        int n = b - a;

        if (n <= g) {
            heapSorter.customHeapSort(array, a, b, 0.5);
            return;
        }

        int u      = findKeys(array, a, b, f),
            b0     = b,
            bufPos = b - u;
        b = bufPos;

        for (int i = a; i < b; i++) {
            Highlights.markArray(3, i);
            Delays.sleep(1);
            bits.incr(binarySearch(array, bufPos, bufPos + u, array[i], true) - bufPos);
        }
        Highlights.clearMark(3);

        for (int i = 1, sum = bits.get(0); i < u + 1; i++) {
            sum += bits.get(i);
            bits.set(i, sum);
        }

        for (int i = 0, j = 0; i < u; i++) {
            Highlights.markArray(3, bufPos + i);
            Delays.sleep(1);
            int c = bits.get(i);

            while (j < c) {
                int p = binarySearch(array, bufPos + i, bufPos + u, array[a + j], true) - bufPos;

                if (p == i) Writes.swap(array, a + j, a + (--c), 1, true, false);
                else {
                    bits.decr(p);
                    Writes.swap(array, a + j, a + bits.get(p), 1, true, false);
                }
            }

            j = binarySearch(array, a + j, b, array[bufPos + i], false) - a;
        }

        bits.free();

        // unique map sort would skip this step, as it assumes it's sorting
        // a fixed amount of uniques
        if (u == f) {
            int j = a;
            for (int i = 0; i < u; i++) {
                Highlights.markArray(3, bufPos + i);
                int j1 = binarySearch(array, j, b, array[bufPos + i], false);

                if (j1 - j > g) this.sort(array, j, j1, g, f, bits);
                else            heapSorter.customHeapSort(array, j, j1, 0.5);

                j = j1;
            }

            if (b - j > g) this.sort(array, j, b, g, f, bits);
            else           heapSorter.customHeapSort(array, j, b, 0.5);
        }

        mergeInPlaceBW(array, a, b, b0);
    }

    private void shuffle(int[] array, int a, int b){
        for (int i = a; i < b; ++i)
            Writes.swap(array, i, this.rng.nextInt(b - i) + i, 0.25, true, false);
    }

    public void sort(int[] array, int a, int b) {
        MaxHeapSort heapSorter = new MaxHeapSort(arrayVisualizer);

        int n = b - a;

        if (n <= SMALL_SORT) {
            heapSorter.customHeapSort(array, a, b, 0.5);
            return;
        }

        this.rng = new Random();

        int l    = log2(n) + 1,
            g    = Math.max(n / l, SMALL_SORT),
            f    = n / (l * l),
            bLen = (f + 1) * l,
            s    = a + bLen,
            b0   = b - bLen;

        quickSelect(array, a, b, false, s, b0);

        heapSorter.customHeapSort(array, a, s, 0.5);
        heapSorter.customHeapSort(array, b0, b, 0.5);

        BitArray bits = new BitArray(array, a, b0, f + 1, l);

        shuffle(array, s, b0);
        this.sort(array, s, b0, g, f, bits);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
