package sorts.hybrid;

import sorts.templates.Sort;
import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;


// - [Control/Jay 2022, impl. aphitorite 2022.] -
public class MicroKitaSort extends Sort {
	public MicroKitaSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Microkita");
		this.setRunAllSortsName("Micro-Kita Sort");
		this.setRunSortName("Microkitasort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	/*
	 * Microkita Sort: A kitasort golfed down to 2 sqrt(n) space.
	 * - Uses linkedlist space optimization used to make Logkita single-pivot
	 ***** Reduces index overhead from O(sqrt n log n) + O(1) moves to O(sqrt n) + O(1) moves,
	 ***** but is more tricky to modify after the fact.
	 * - Uses half-space optimization used for Half-Ecta
	 ***** Costs O(sqrt n log n) comparisons
	 */

    private static int minbin = 16;
    private void merge(int[] array, int[] buf, int start, int mid, int end) {
    	Writes.arraycopy(array, mid, buf, 0, end - mid, 1, true, true);
    	int l = mid - 1, r = end - mid - 1, t = end;
    	while (l >= start && r >= 0) {
    		t--;
    		if (Reads.compareValues(array[l], buf[r]) > 0)
    			Writes.write(array, t, array[l--], 1, true, false);
    		else
    			Writes.write(array, t, buf[r--], 1, true, false);
    	}
    	while (r >= 0)
			Writes.write(array, --t, buf[r--], 1, true, false);
    }

    // linked list edition
    private void inc(int[] val, int[] tval, int[] tag, int[] offs, int[] toffs, int w, int indice) {
    	int nt = tag[tval[indice] + toffs[indice]];
    	if (val[indice] >= 0 && (++val[indice] - offs[indice]) % w == 0)
    		if (nt == 0)
    			val[indice] = -1;
    		else
    			val[indice] = offs[indice] + (tval[indice] = nt) * w;
    }

    private void blockmerge(int[] array, int[] buf, int[] tags, int w, int s, int a, int m, int b) {
    	int wa = (a - s) / w, wm = (m - s) / w, lf = -1, ls = 0, ft = -1, c = 0, cidx, lv, bi, free;
    	int[] offs = {a, m}, toffs = {wa, wm},
    		  cnts = {0, 0},
    		  ptrs = {a, m}, bufs = {a, m},
    		  tptrs = {0, 0}, tbufs = {0, 0};

    	for (; c < w; c++) {
			Writes.write(buf, c, array[ptrs[cidx = ptrs[1] < 0 || Reads.compareIndices(array, ptrs[0], ptrs[1], 0.5, true) <= 0 ? 0 : 1]], 0.5, true, true);
			inc(ptrs, tptrs, tags, offs, toffs, w, cidx); cnts[cidx]++;
    	}

    	while (ptrs[0] >= 0 || ptrs[1] >= 0) {
            lv = cnts[0] > 0 && (ptrs[0] < 0 || ptrs[1] < 0 || cnts[0] == w || Reads.compareIndices(array, bufs[0] + w - 1, bufs[1] + w - 1, 1, true) <= 0) ? 0 : 1;
            bi = tbufs[lv] + (wm - wa) * lv;

    		for (c = 0; c++ < w;) {
	    		Writes.write(array, bufs[lv], array[ptrs[cidx = ptrs[0] >= 0 && (ptrs[1] < 0 || Reads.compareIndices(array, ptrs[0], ptrs[1], 0.5, true) <= 0) ? 0 : 1]], 0.5, true, false);
	    		inc(ptrs, tptrs, tags, offs, toffs, w, cidx); cnts[cidx]++;
    			inc(bufs, tbufs, tags, offs, toffs, w, lv);
    		}

    		cnts[lv] -= w;

			if (ft < 0)
				ft = bi;
			else if (bi == 0)
				lf = ls;
			else
	    		Writes.write(tags, ls + wa, bi, 1, true, true);
			ls = bi;
    	}

    	lv = cnts[0] > 0 ? 0 : 1; free = tbufs[lv] + toffs[lv];

    	Writes.arraycopy(array, a, array, s + free * w, w, 0.5, true, false);
    	Writes.write(tags, ls + wa, 0, 1, true, true);

    	if (lf >= 0)
    		Writes.write(tags, lf + wa, free - wa, 1, true, true);
    	else
    		ft = free - wa;

		Writes.write(tags, free, tags[wa], 1, true, true);

        Writes.arraycopy(buf, 0, array, a, w, 0.5, true, false);
    	Writes.write(tags, wa, ft, 1, true, true);
    }

    private void index(int[] array, int[] tags, int w, int a, int b) {
    	int i = 0, j = 0, i1, t;
    	do {
    		t = tags[i];
    		Highlights.markArray(3, a + i * w);
    		Writes.write(tags, i, j++, 2.5, true, true);
    		i = t;
    	} while (i != 0);
    	for (i = a + w, i1 = 1; i < b - w; i += w, i1++) {
			t = tags[i1];
    		while (Reads.compareOriginalValues(t, i1) != 0) {
    			for (j = 0; j < w; j++)
    				Writes.swap(array, i + j, a + j + t * w, 1, true, false);
    			j = tags[t];
    			Writes.write(tags, t, t, 1, true, true);
    			t = j;
    		}
    	}
    }

    public void microkita(int[] array, int a, int c) {
    	BinaryInsertionSort small = new BinaryInsertionSort(arrayVisualizer);
    	if (c - a <= 32) {
    		small.customBinaryInsert(array, a, c, 0.5);
    		return;
    	}
    	int sl = (32 - Integer.numberOfLeadingZeros(c - a - 1)) / 2, s = 1 << sl,
    		b = c - (c - a) % s, k = (b - a) / s,
    		buf[] = Writes.createExternalArray(s), tags[] = Writes.createExternalArray(k);

    	for (int i = a; i < b; i += minbin) {
    		int j = Math.min(i + minbin, b);
    		small.customBinaryInsert(array, i, j, 0.5);
    	}
    	for (int i = 0; i < k - 1; i++) Writes.write(tags, i, (i + 1) % Math.max(2, 2*minbin/s), 0, true, true);
    	for (int j = minbin; j < b - a; j *= 2) {
    		for (int l = a; l + j < b; l += 2 * j) {
    			int m = l + j, r = Math.min(l + 2 * j, b);
    			if (j > s)
    				blockmerge(array, buf, tags, s, a, l, m, r);
    			else
    				merge(array, buf, l, m, r);
    		}
    	}
    	index(array, tags, s, a, b);
    	Writes.deleteExternalArray(tags);
    	if (b < c) {
    		microkita(array, b, c);
    		merge(array, buf, a, b, c);
    	}
    	Writes.deleteExternalArray(buf);
    }

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		microkita(array, 0, length);
	}
}