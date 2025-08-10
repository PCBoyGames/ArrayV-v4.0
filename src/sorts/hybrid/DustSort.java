/**
 * v0.1.0
 *
 * Special thanks to:
 *  aphitorite and Control for help with the key collection algorithm
 *  Amari for inspiration (Helium Sort)
 *  Morwenn for useful information regarding presortedness measures
 *
 * --------
 *
 * Adaptive block merge sort satisfying the following constraints:
 *  O(1) space
 *  stable
 *  O(n log n) worst case data moves
 *  1 n log n + O(n) worst case comparisons
*/

package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Rotations;
import java.lang.Math;

public class DustSort extends Sort {
	public DustSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Dust");
		this.setRunAllSortsName("Dustsort");
		this.setRunSortName("Dustsort");
		this.setCategory("Hybrid Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	static int    MIN_RUN = 16,
						RATIO_BIN_MERGE = 6,
						MAX_APPEND = 3,
						MIN_KEYS = 12;

	boolean less(int[] arr, int i, int j, double sleep, boolean invert) {
		return Reads.compareIndices(arr, i, j, sleep, true) < (invert ? 1 : 0);
	}

	int blockLen(int n) {
		int k = 1;
		while (k < n / k)
			k *= 2;
		return k;
	}

	void reverse(int[] arr, int l, int r, double sleep) {
		if (l + 1 >= r) return;

		while (l + 1 < r) Writes.swap(arr, l++, --r, sleep, true, false);
		Writes.changeReversals(1);
	}

	void rotate(int[] arr, int s, int n1, int n2, double sleep) {
		Rotations.holyGriesMills(arr, s, n1, n2, sleep, true, false);
	}

	void blockSwap(int[] arr, int i, int j, int cnt, double sleep) {
		if (i == j) return;

		while (cnt-- > 0)
			Writes.swap(arr, i++, j++, sleep, true, false);
	}

	int binarySearch(int[] arr, int s, int n, int val, boolean upper) {
		int si = s;

		while (n > 0) {
			int d = n / 2;
			if (less(arr, s + d, val, 0, upper)) s += n - d;
			n = d;
		}

		return s - si;
	}

	int blockSwapLength(int[] arr, int s1, int n1, int s2, int n2, boolean invert) {
		int n = Math.min(n1, n2),
			s2i = s2,
			r1 = s1 + n1 - 1;

		while (n > 0) {
			int d = n / 2;
			if (less(arr, s2 + d, r1 - d, 0, invert)) {
				s2 += n - d;
				r1 -= n - d;
			}
			n = d;
		}

		return s2 - s2i;
	}

	void insertSort(int[] arr, int l, int i, int r) {
		for (; i < r; ++i) {
			int idx = this.binarySearch(arr, l, i - l, i, true);
			this.rotate(arr, l + idx, i - l - idx, 1, 0.5);
		}
	}

	int nextNonDescRunBound(int[] arr, int l, int r) {
		while (++l < r && !less(arr, l, l - 1, 1, false));
		return l;
	}

	int nextRunBound(int[] arr, int s, int n) {
		int i = this.nextNonDescRunBound(arr, s, s + n);
		if (i >= s + n || i > s + 1 && less(arr, s, i - 1, 1, false)) return i;

		int l = i;
		this.reverse(arr, s, l, 1);

		while (++i < s + n) {
			if (less(arr, i, i - 1, 1, false)) {
				this.reverse(arr, l, i, 1);
				l = i;
			} else if (less(arr, i - 1, i, 0, false)) break;
		}

		this.reverse(arr, l, i, 1);
		this.reverse(arr, s, i, 1);
		return this.nextNonDescRunBound(arr, i - 1, s + n);
	}

	void sortSmallRuns(int[] arr, int l, int i, int r) {
		i = Math.max(i, l + 1);

		while (i < r) {
			l += (i - l - 1) / MIN_RUN * MIN_RUN;

			int tmpR = Math.min(r, l + MIN_RUN);
			insertSort(arr, l, i, tmpR);

			l = tmpR;
			i = this.nextRunBound(arr, l, r - l);
		}
	}

	int mergeRight(int[] arr, int s1, int n1, int s2, int n2, int dst) {
		int i1 = 0,
			i2 = 0;

		while (i1 < n1 && i2 < n2) {
			int src = less(arr, s2 + i2, s1 + i1, 0, false) ? s2 + i2++ : s1 + i1++;
			Writes.swap(arr, dst++, src, 1, true, false);
		}

		this.blockSwap(arr, dst, i1 == n1 ? s2 + i2 : s1 + i1, n1 - i1 + n2 - i2, 1);
		return i1 + i2;
	}

	void binMergeRight(int[] arr, int s1, int n1, int s2, int n2, int dst) {
		int unit = (n1 + n2 - 1) / n1,
			bin = (n2 + unit - 1) % unit,
			i1 = 0,
			i2 = 0;

		int k = this.binarySearch(arr, s2, bin + 1, s1, false);

		while (i2 < n2) {
			if (i2 < k) {
				Writes.swap(arr, dst++, s2 + i2++, 0.5, true, false);
				continue;
			}

			if (i2 <= bin) {
				Writes.swap(arr, dst++, s1 + i1++, 0.5, true, false);
				if (i1 == n1) break;
			} else bin += unit;

			k = bin + 1;
			if (!less(arr, s2 + bin, s1 + i1, 0.5, false))
				k = i2 + this.binarySearch(arr, s2 + i2, bin - i2, s1 + i1, false);
		}

		this.blockSwap(arr, dst, i1 == n1 ? s2 + i2 : s1 + i1, n1 - i1 + n2 - i2, 1);
	}

	void mergeLeft(int[] arr, int s1, int n1, int s2, int n2, int dst) {
		int rDst = dst + n1 + n2;

		while (n1 > 0 && n2 > 0) {
			int src = less(arr, s2 + n2 - 1, s1 + n1 - 1, 0, false) ? s1 + --n1 : s2 + --n2;
			Writes.swap(arr, --rDst, src, 1, true, false);
		}

		this.blockSwap(arr, dst, n1 == 0 ? s2 : s1, n1 + n2, 1);
	}

	void binMergeLeft(int[] arr, int s1, int n1, int s2, int n2, int dst, boolean invert) {
		int unit = (n1 + n2 - 1) / n2,
			bin = n1 - 1 - (n1 - 1) % unit,
			rDst = dst + n1 + n2;

		int k = bin + this.binarySearch(arr, s1 + bin, n1 - bin, s2 + n2 - 1, !invert);

		while (n1 > 0) {
			if (n1 > k) {
				Writes.swap(arr, --rDst, s1 + --n1, 0.5, true, false);
				continue;
			}

			if (n1 != bin) {
				Writes.swap(arr, --rDst, s2 + --n2, 0.5, true, false);
				if (n2 == 0) break;
			} else bin -= unit;

			k = bin;
			if (!less(arr, s2 + n2 - 1, s1 + bin, 0.5, invert))
				k += 1 + this.binarySearch(arr, s1 + bin + 1, n1 - bin - 1, s2 + n2 - 1, !invert);
		}

		this.blockSwap(arr, dst, n1 == 0 ? s2 : s1, n1 + n2, 1);
	}

	boolean bufferedMerge(int[] arr, Buffer buf, int s, int n1, int n2) {
		int m = s + n1;

		if (n1 == 0 || n2 == 0 || !less(arr, m, m - 1, 0, false)) return true;

		// check if runs are reverse ordered
		if (less(arr, s + n1 + n2 - 1, s, 1, false)) {
			this.rotate(arr, s, n1, n2, 1);
			return true;
		}

		int rad = this.blockSwapLength(arr, s, n1, m, n2, false);

		// can't optimize moves
		if (rad > buf.len) {
			if (Math.max(n1, n2) - rad > buf.len) return false;

			// same branch is guaranteed to not pass on recursing
			this.blockSwap(arr, m - rad, m, rad, 1);
			return this.bufferedMerge(arr, buf, s, n1 - rad, rad)
				&& this.bufferedMerge(arr, buf, m, rad, n2 - rad);
		}

		this.startBufferedMerge(arr, buf, m - rad, rad);

		if (rad < (n1 - rad) / RATIO_BIN_MERGE) {
			this.binMergeLeft(arr, s, n1 - rad, m, rad, s, false);
		} else
			this.mergeLeft(arr, s, n1 - rad, m, rad, s);

		if (rad < (n2 - rad) / RATIO_BIN_MERGE) {
			this.binMergeRight(arr, buf.pos, rad, m + rad, n2 - rad, m);
		} else
			this.mergeRight(arr, buf.pos, rad, m + rad, n2 - rad, m);

		return true;
	}

	void inPlaceMerge(int[] arr, int s, int n1, int n2) {
		if (n1 == 0 || n2 == 0 || !less(arr, s + n1, s + n1 - 1, 0, false)) return;

		// reduce right run bounds; otherwise, special case sorting won't work
		n2 = 1 + this.binarySearch(arr, s + n1 + 1, n2 - 1, s + n1 - 1, false);
		int u = n1;

		while (true) {
			if (n1 <= u || n2 <= u) {
				int min = Math.min(n1, n2),
					max = Math.max(n1, n2);
				if (min == 0 || min * 2 < max / min) break;

				do {
					u /= 2;
				} while (min <= u);
			}

			int rad = this.blockSwapLength(arr, s, n1, s + n1, n2, false);
			this.blockSwap(arr, s + n1 - rad, s + n1, rad, 1);

			if (n2 < n1) {
				this.lazyMerge(arr, s + n1, rad, n2 - rad, false);
				n1 -= rad;
				n2 = rad;
			} else {
				this.lazyMerge(arr, s, n1 - rad, rad, false);
				s += n1;
				n1 = rad;
				n2 -= rad;
			}
		}

		this.lazyMerge(arr, s, n1, n2, false);
	}

	void lazyMerge(int[] arr, int s, int n1, int n2, boolean invert) {
		if (n2 <= n1) {
			while (n2 > 0) {
				int aCut = this.binarySearch(arr, s, n1, s + n1 + n2 - 1, !invert);
				this.rotate(arr, s + aCut, n1 - aCut, n2, 1);

				n1 = aCut;
				if (n1 == 0) break;

				n2 = this.binarySearch(arr, s + n1, n2, s + n1 - 1, invert);
			}
		} else {
			while (n1 > 0) {
				int bAdv = this.binarySearch(arr, s + n1, n2, s, invert);
				this.rotate(arr, s, n1, bAdv, 1);

				n2 -= bAdv;
				if (n2 == 0) break;

				int aAdv = this.binarySearch(arr, s + bAdv, n1, s + bAdv + n1, !invert);
				s += aAdv + bAdv;
				n1 -= aAdv;
			}
		}
	}

	int blockSelect(int[] arr, Blocks blocks, int a, int cnt) {
		int mid = 0;
		while (mid < a && !less(arr, blocks.end(a), blocks.end(mid), 0, false)) ++mid;

		Writes.swap(arr, blocks.key(mid), blocks.key(a), 1, true, false);
		this.blockSwap(arr, blocks.start(mid), blocks.start(a), blocks.len, 1);

		int aMin = a,
			bMin = a + 1;

		for (int i = mid + 1; i < bMin; ++i) {
			if (bMin < cnt && less(arr, blocks.end(bMin), blocks.end(aMin), 0, false)) {
				Writes.swap(arr, blocks.key(bMin), blocks.key(i), 1, true, false);
				this.blockSwap(arr, blocks.start(bMin), blocks.start(i), blocks.len, 1);

				if (aMin == i) aMin = bMin;
				++bMin;
			} else {
				Writes.swap(arr, blocks.key(aMin), blocks.key(i), 1, true, false);
				this.blockSwap(arr, blocks.start(aMin), blocks.start(i), blocks.len, 1);

				aMin = Math.max(a, i + 1);
				for (int j = aMin + 1; j < bMin; ++j) {
					if (less(arr, blocks.key(j), blocks.key(aMin), 0, false)) aMin = j;
				}
			}
		}

		return mid;
	}

	int localMerge(int[] arr, Buffer buf, int s, int n1, int n2, boolean invert) {
		if (!less(arr, s + n1, s + n1 - 1, 1, invert)) return 0;

		int rad = this.blockSwapLength(arr, s, n1, s + n1, n2 - 1, invert);
		this.startBufferedMerge(arr, buf, s + n1 - rad, rad);

		if (rad < (n1 - rad) / RATIO_BIN_MERGE) {
			this.binMergeLeft(arr, s, n1 - rad, s + n1, rad, s, invert);
		}
		else if (invert)    this.mergeLeft(arr, s + n1, rad, s, n1 - rad, s);
		else                this.mergeLeft(arr, s, n1 - rad, s + n1, rad, s);

		if (invert) return this.mergeRight(arr, s + n1 + rad, n2 - rad, buf.pos, rad, s + n1);
		else        return this.mergeRight(arr, buf.pos, rad, s + n1 + rad, n2 - rad, s + n1);
	}

	int blockMerge(int[] arr, Buffer buf, int s, int n1, int n2, int blockLen) {
		int blockCnt = (n1 + n2) / blockLen;

		Blocks blocks = new Blocks(buf.pos + buf.len, s, blockLen);
		int mid = this.blockSelect(arr, blocks, n1 / blockLen, blockCnt);

		boolean wasB = mid == 0;
		int fragPos = s;

		for (int i = mid; i < blockCnt; ++i) {
			boolean isA = i != mid && less(arr, blocks.key(i), blocks.key(mid), 0, false);

			if (isA)
				this.rotate(arr, blocks.key(mid), i - mid++, 1, 0);

			if (isA ^ wasB) continue;
			wasB ^= true;

			int pos = blocks.start(i);
			fragPos = pos + this.localMerge(arr, buf, fragPos, pos - fragPos, blockLen, isA);
		}

		return wasB ? blocks.start(blockCnt) : fragPos;
	}

	void blockMergeInPlace(int[] arr, Buffer keys, int s, int n1, int n2, int blockLen) {
		int blockCnt = (n1 + n2) / blockLen;

		Blocks blocks = new Blocks(keys.pos, s, blockLen);
		int mid = this.blockSelect(arr, blocks, n1 / blockLen, blockCnt);

		boolean wasB = mid == 0;
		int fragPos = s;

		for (int i = mid; i < blockCnt; ++i) {
			boolean isA = i != mid && less(arr, blocks.key(i), blocks.key(mid), 0, false);

			if (isA)
				this.rotate(arr, blocks.key(mid), i - mid++, 1, 0);

			if (isA ^ wasB) continue;
			wasB ^= true;

			int pos = blocks.start(i),
				head = this.binarySearch(arr, pos, blockLen - 1, pos - 1, isA);

			this.lazyMerge(arr, fragPos, pos - fragPos, head, isA);
			fragPos = pos + head;
		}
	}

	void inPlaceMergeSort(int[] arr, int l, int r, int run, int maxRun) {
		int li = l;

		for (; run < maxRun; run *= 2) {
			for (l = li; l + 2 * run <= r; l += 2 * run)
				this.inPlaceMerge(arr, l, run, run);

			if (l + run < r)
				this.inPlaceMerge(arr, l, run, r - (l + run));
		}
	}

	void blockMergeSort(int[] arr, Buffer buf, int s, int run) {
		int l = s,
			r = buf.pos,
			n = r - s,
			blockLen = 1;

		// == 2 << log2((buf.len + 2) / 3)
		while (blockLen * 2 <= buf.len - blockLen + 2) blockLen *= 2;

		int keys = buf.len - blockLen + 1;
		buf.len = blockLen - 1;

		if (buf.len < buf.unsorted)
			this.sortBuffer(arr, buf);

		// buffered block merges
		while (run < n && Math.min(n, 2 * run) / blockLen <= keys) {
			for (l = s; l + 2 * run <= r; l += 2 * run)
				if (!this.bufferedMerge(arr, buf, l, run, run))
					this.blockMerge(arr, buf, l, run, run, blockLen);

			int len = r - l;

			if (len > run && !this.bufferedMerge(arr, buf, l, run, len - run)) {
				int fragPos = this.blockMerge(arr, buf, l, run, len - run, blockLen);
				this.bufferedMerge(arr, buf, fragPos, r - fragPos - len % blockLen, len % blockLen);
			}

			run *= 2;
		}

		this.sortBuffer(arr, buf);
		buf.len += keys;

		// in place block merges
		while (run < n) {
			while (Math.min(n, 2 * run) / blockLen > buf.len)
				blockLen *= 2;

			for (l = s; l + 2 * run <= r; l += 2 * run)
				this.blockMergeInPlace(arr, buf, l, run, run, blockLen);

			int len = r - l;

			if (len > run + blockLen)
				this.blockMergeInPlace(arr, buf, l, run, len - run, blockLen);
			this.inPlaceMerge(arr, l, len - len % blockLen, len % blockLen);

			run *= 2;
		}
	}

	void makeInitialBuffer(int[] arr, Buffer buf, int l, int i) {
		while (i-- > l && buf.len < MIN_KEYS) {
			int idx = this.binarySearch(arr, buf.pos, buf.len, i, false);

			if (idx == buf.len || less(arr, i, buf.pos + idx, 1, false)) {
				this.shiftBuffer(arr, buf, i + 1);
				this.rotate(arr, i, 1, idx, 1);

				buf.pos = i;
				++buf.len;
			}
		}
	}

	void mergeUniqueKeys(int[] arr, Buffer buf, int l, int r, int ideal) {
		int b = this.binarySearch(arr, buf.pos, buf.len, r - 1, true);

		while (r > l && b > 0 && buf.len < ideal) {
			if (less(arr, r - 1, buf.pos + b - 1, 1, false)) {
				--b;
			} else if (less(arr, buf.pos + b - 1, --r, 0, false)) {
				this.shiftBuffer(arr, buf, r + 1);
				this.rotate(arr, r, 1, b++, 0.5);

				buf.pos = r;
				++buf.len;
			}
		}

		while (r-- > l && buf.len < ideal) {
			if (less(arr, r, buf.pos, 1, false)) {
				this.shiftBuffer(arr, buf, r + 1);

				buf.pos = r;
				++buf.len;
			}
		}
	}

	void simpleSort(int[] arr, int s, int n, int head, int tail, int blockLen) {
		this.sortSmallRuns(arr, s, s + head, s + n - tail);
		this.inPlaceMergeSort(arr, s, s + n - tail, MIN_RUN, n - tail);

		if (tail > 0) {
			this.sortSmallRuns(arr, s, s + n - tail, s + n);
			this.inPlaceMergeSort(arr, s, s + n, MIN_RUN, n);
		}
	}

	void sort(int[] arr, int s, int n) {
		int head = this.nextRunBound(arr, s, n);
		if (head >= n) return;

		int blockLen = this.blockLen(n + 1);

		// O(n) append case
		if (head + blockLen * MAX_APPEND >= s + n) {
			this.simpleSort(arr, s, n, head - s, 0, blockLen);
			return;
		}

		Buffer buf = new Buffer(s + n, 0);
		this.makeInitialBuffer(arr, buf, head, s + n);

		// O(n log n) low distincts case
		if (buf.len < MIN_KEYS) {
			this.shiftBuffer(arr, buf, s + n - buf.len);
			this.simpleSort(arr, s, n, head - s, s + n - head, blockLen);
			return;
		}

		// O(n log n) prepend + low distincts case
		if (buf.pos <= head + blockLen * MAX_APPEND) {
			int tail = s + n - buf.pos;
			this.shiftBuffer(arr, buf, s + n - buf.len);
			this.simpleSort(arr, s, n, head - s, tail, blockLen);
			return;
		}

		int l = s,
			m = buf.pos,
			r = buf.pos + buf.len,
			run = MIN_RUN,
			ideal = blockLen + (n + 1) / blockLen - 2;

		this.sortSmallRuns(arr, s, head, buf.pos);

		// collect keys
		while (m > s) {
			int len = (m - s - 1) % run + 1;
			this.mergeUniqueKeys(arr, buf, m - len, m, ideal);
			m -= len;

			if (buf.len == ideal) break;
			if (buf.len < run) continue;

			for (l = s; l + 2 * run <= m; l += 2 * run)
				this.bufferedMerge(arr, buf, l, run, run);

			this.sortBuffer(arr, buf);
			run *= 2;
		}

		l = buf.pos - (buf.pos - s) % run;
		this.shiftBuffer(arr, buf, s + n - buf.len);
		r -= buf.len;

		int frag = 0,
			prev = run;

		// snap blocks to powers of 2
		while (l < r) {
			m = this.nextNonDescRunBound(arr, l, r);

			if (run > MIN_RUN && m - l <= run / 2 && prev + m - l <= run) {
				run /= 2;
				frag %= run;
			}

			prev = m - l;
			this.bufferedMerge(arr, buf, l - frag, frag, Math.min(run - frag, prev));
			frag = (frag + prev) % run;
			l = m;
		}

		this.sortSmallRuns(arr, s, r, buf.pos);
		this.blockMergeSort(arr, buf, s, MIN_RUN);
		this.inPlaceMerge(arr, s, buf.pos - s, buf.len);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.sort(array, 0, length);
	}

	class Buffer {
		int pos, len, unsorted;

		Buffer(int pos, int len) {
			this.pos = pos;
			this.len = len;
			this.unsorted = 0;
		}
	}

	void shiftBuffer(int[] arr, Buffer buf, int dst) {
		if (dst <= buf.pos) this.rotate(arr, dst, buf.pos - dst, buf.len, 1);
		else                this.rotate(arr, buf.pos, buf.len, dst - buf.pos, 1);
		buf.pos = dst;
	}

	void startBufferedMerge(int[] arr, Buffer buf, int dst, int cnt) {
		buf.unsorted = Math.max(buf.unsorted, cnt);
		this.blockSwap(arr, buf.pos, dst, cnt, 1);
	}

	void sortBuffer(int[] arr, Buffer buf) {
		this.insertSort(arr, buf.pos, buf.pos + 1, buf.pos + buf.unsorted);
		buf.unsorted = 0;
	}

	class Blocks {
		int s, len, keys;

		Blocks(int keys, int s, int len) {
			this.keys = keys;
			this.s = s;
			this.len = len;
		}

		int start(int i) {
			return this.s + this.len * i;
		}

		int end(int i) {
			return this.start(i) + this.len - 1;
		}

		int key(int i) {
			return this.keys + i;
		}
	}
}
