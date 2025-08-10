package sorts.hybrid;


import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;
import utils.IndexedRotations;
import main.ArrayVisualizer;

public class AdvancedLogSort extends Sort {
	public AdvancedLogSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Advanced Log");
		this.setRunAllSortsName("Advanced Log Sort");
		this.setRunSortName("Advanced Logsort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int log(int v) {
		return 32-Integer.numberOfLeadingZeros(v-1);
	}

	private int[] productLog(int n) {
		int r = 1;
		while((r<<r)+r-1 < n) r++;
		int q = 0;
		while(1<<q < r) q++;
		return new int[] {r, 1<<q};
	}

	private void multiSwap(int[] array, int a, int b, int s) {
		while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}

	private int medOf3(int[] array, int a, int b, int c) {
    	int d;
    	if(Reads.compareIndices(array, a, b, 0.5, true) > 0) {
    		d = b; b = a;
    	} else
    		d = a;
    	if(Reads.compareIndices(array, b, c, 0.5, true) > 0) {
    		if(Reads.compareIndices(array, d, c, 0.5, true) > 0) {
        		return d;
        	}
    		return c;
    	}
    	return b;
	}

    private int ninther(int[] array, int a, int b) {
    	if(b-a<=9)
    		return a+(b-a)/2;
    	int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
    	int c = medOf3(array, a, a+eight, a+quart);
    	int d = medOf3(array, a+quart+eight, a+half, a+half+eight);
    	int e = medOf3(array, b-quart, b-eight, b-1);
    	int f = medOf3(array, c, d, e);
    	return f;
    }

     // Median of 3 ninthers
	 private int pseudomo27(int[] array, int a, int b) {
			if (b - a < 64) {
				 return this.ninther(array, a, b);
			} else {
				 int d = (b - a + 1) / 8;
				 int m0 = this.ninther(array, a, a + 2 * d);
				 int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
				 int m2 = this.ninther(array, a + 6 * d, b);
				 return this.medOf3(array, m0, m1, m2);
			}
	 }

	 // Ninther of 9 ninthers
	 private int pseudomo81(int[] array, int a, int b) {
			if (b - a < 256) {
				 return this.pseudomo27(array, a, b);
			} else {
				 int d = (b - a + 1) / 24;
				 int m0 = this.ninther(array, a, a + 2 * d);
				 int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
				 int m2 = this.ninther(array, a + 6 * d, a + 8 * d);
				 int m3 = this.ninther(array, a + 9 * d, a + 11 * d);
				 int m4 = this.ninther(array, a + 12 * d, a + 14 * d);
				 int m5 = this.ninther(array, a + 15 * d, a + 17 * d);
				 int m6 = this.ninther(array, a + 18 * d, a + 20 * d);
				 int m7 = this.ninther(array, a + 19 * d, a + 21 * d);
				 int m8 = this.ninther(array, a + 22 * d, b);
				 return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5), this.medOf3(array, m6, m7, m8));
			}
	 }

	 // Ninther of 9 medians of 3 ninthers
	 private int pseudomo243(int[] array, int a, int b) {
			if (b - a < 16384) {
				 return this.pseudomo81(array, a, b);
			} else {
				 int d = (b - a + 1) / 24;
				 int m0 = this.pseudomo27(array, a, a + 2 * d);
				 int m1 = this.pseudomo27(array, a + 3 * d, a + 5 * d);
				 int m2 = this.pseudomo27(array, a + 6 * d, a + 8 * d);
				 int m3 = this.pseudomo27(array, a + 9 * d, a + 11 * d);
				 int m4 = this.pseudomo27(array, a + 12 * d, a + 14 * d);
				 int m5 = this.pseudomo27(array, a + 15 * d, a + 17 * d);
				 int m6 = this.pseudomo27(array, a + 18 * d, a + 20 * d);
				 int m7 = this.pseudomo27(array, a + 19 * d, a + 21 * d);
				 int m8 = this.pseudomo27(array, a + 22 * d, b);
				 return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5), this.medOf3(array, m6, m7, m8));
			}
	 }

	 // get rank of r between [a,a+g...b)
    private int gaprank(int[] array, int a, int b, int g, int r) {
    	int re = 0;
    	while(a < b) {
    		if(a != r) {
    			if(Reads.compareIndices(array, a, r, 0.25, true) < 0) re++;
    		}
    		a += g;
    	}
    	return re;
    }

    // hopefully better "rank of 243s" median selector
    private int rankof243s(int[] array, int a, int b) {
    	// 2^(log(b-a)/2)
    	int s = 1;
    	while(s*s<b-a) s*=2;

    	// low n: return ninther
    	if((s/=2) < 2) return ninther(array, a, b);
    	int mid = (b-a-1)/(2*s)+1, e = (b-a) / 8, cm = a+(b-a)/2, cr = 0;

    	// select pmo243 with gapped rank closest to middle
    	for(int i=0; i<e; i+=s) {
    		int p = pseudomo243(array, a+i, b-e+i), r = gaprank(array, a, b, s, p);
    		if(Math.abs(cr-mid)>Math.abs(r-mid)) {
    			cm = p;
    			cr = r;
    		}
    	}
    	return cm;
    }

    private void encode(int[] array, int a, int b, int v) {
    	while(v>0) {
    		if(v%2==1) Writes.swap(array, a, b, 1, true, false);
    		v/=2; a++; b++;
    	}
    }

    private int get(int[] array, int a, int p, int l, int c, boolean b) {
    	int v = 0, i = 0;
    	while(l-->0) {
    		v |= (Reads.compareIndexValue(array, a+i, p, 0.1, true) < c ^ b ? 1 << i : 0);
    		i++;
    	}
    	return v;
    }

	private void blockcycle(int[] array, int a, int m, int b, int frag, int l, int w, int p, int c, boolean i) {
		if(frag!=b-1) multiSwap(array, a+frag*l, a+(b-1)*l, l);
		for(int k = 0; k < b - 1; k++) {
			int z = get(array, a+k*l, p, w, c, i);
			while(z != k) {
				multiSwap(array, a+k*l, a+z*l, l);
				z = get(array, a+k*l, p, w, c, i);
			}
			encode(array, a+k*l, m+k*l, k);
		}
	}

	private int rotatePart(int[] array, int a, int b, int p, int c) {
		if(a < b) {
			int m = a + (b - a) / 2;
			int l1 = rotatePart(array, a,   m, p, c);
			int l2 = rotatePart(array, m+1, b, p, c);
			IndexedRotations.juggling(array, a+l1, m+1, m+l2+1, 1, true, false);
			return l1 + l2;
		} else {
			return Reads.compareIndexValue(array, a, p, 0.5, true) < c ? 1 : 0;
		}
	}

    // advanced log partition: hybrid aeos-muku* (*not muku)
	// includes a tweaked form of aphiBP typing, giving it ~4n worst case writes at the cost of a guaranteed redistribution phase.
	// also still really buggy.
    private int partition(int[] array, int[] tmp, int a, int B, int p, int c, int s) {
    	int b = B - (B - a) % s;

		if(a == b) { // easy partition
			int tt = 0;
			for(int i = a; i < B; i++) {
	    		if(Reads.compareIndexValue(array, i, p, 0.5, true) < c) {
	    			Writes.write(array, i - tt, array[i], 2, true, false);
        			if(tt > 0) Writes.visualClear(array, i);
	    		} else {
	    			Writes.write(tmp, tt++, array[i], 2, true, true);
	    			Writes.visualClear(array, i);
	    		}
			}
			Writes.arraycopy(tmp, 0, array, B - tt, tt, 2, true, false);
			return B - tt;
		}

    	// get one block of buffer
    	int id = a, t = 0,  // aeos/muku vals
    		lb = 0, rb = 0, // muku block tally
    		lc = 0, rc = 0, // muku blockbuf count
    		li = 0, ri = 0, // muku block index
    		ii = -1;        // tracker index
    	while(id < b && t < s) {
    		if(Reads.compareIndexValue(array, id, p, 0.5, true) < c) {
    			if(lc < 0) lc++;
    			if(lc == 0) { // if block marked full, go to next
    				li = ++ii;
    				lb++;
    			}
    			int x = id - t;
    			Writes.write(array, x, array[id], 2, true, false);
    			if(id != x) Writes.visualClear(array, id);
    			if(++lc == s) { // if block full, mark as such
    				lc = -1;
    			}
    		} else {
    			Writes.write(tmp, t++, array[id], 2, true, true);
    			Writes.visualClear(array, id);
    		}
    		id++;
    	}

    	// muku typing
    	while(id < b) {
    		if(Reads.compareIndexValue(array, id, p, 0.5, true) < c) {
    			if(lc < 0) lc++;
    			if(lc == 0) { // if block marked full, go to next
    				li = ++ii;
    				lb++;
    			}
    			int x = a + li * s + lc++;
    			Writes.write(array, x, array[id], 1, true, false);
    			if(id != x) Writes.visualClear(array, id);
    			if(lc == s) { // if block full, mark as such
    				lc = -1;
    			}
    		} else {
    			if(rc < 0) rc++;
    			if(rc == 0) { // if block marked full, go to next
    				ri = ++ii;
    				rb++;
    			}
    			int x = a + ri * s + rc++;
    			Writes.write(array, x, array[id], 1, true, false);
    			if(id != x) Writes.visualClear(array, id);
    			if(rc == s) { // if block full, mark as such
    				rc = -1;
    			}
    		}
    		id++;
    	}
    	boolean lf = lc > 0, rf = rc > 0;
    	int lb_c = lb - (lf ? 1 : 0),
    		rb_c = rb - (rf ? 1 : 0); // corrected vars (block tally minus fragments)
    	int min  = Math.min(lb, rb);
		int M    = log(min);
    	if(min > 0) {
    		// tag blocks
    		for(int i = 0, j = 0, k = 0; i < min - 1; i++) {
    			while(j == ri || Reads.compareIndexValue(array, a+j*s+M, p, 0.5, true) >= c) j++;
    			while(k == li || Reads.compareIndexValue(array, a+k*s+M, p, 0.5, true) < c) k++;
    			encode(array, a+j++*s, a+k++*s, i);
    		}
    		int ca, cm, cf;
    		// sort blocks
    		if(lb < rb) {
    			for(int i = lb + rb - 1, j = 0; i >= 0; i--) {
    				if(i != li && (i == ri || Reads.compareIndexValue(array, a+i*s+M, p, 0.5, true) >= c)) {
    					multiSwap(array, a+i*s, a+(i+j)*s, s);
    					if(i + j == li) li = i;
    					if(i == ri) ri = i + j;
    				} else j++;
    			}
    			ca = a;
    			cm = a + lb * s;
    			cf = li;
    		} else {
    			for(int i = 0, j = 0; i < lb + rb; i++) {
    				if(i != ri && (i == li || Reads.compareIndexValue(array, a+i*s+M, p, 0.5, true) < c)) {
    					if(i == li) li = j;
    					if(j == ri) ri = i;
    					multiSwap(array, a+i*s, a+j++*s, s);
    				}
    			}
    			ca = a + lb * s;
    			cm = a;
    			cf = ri - lb;
    		}
    		// block cycle with fragment
			if(min > 1) blockcycle(array, ca, cm, min, cf, s, M, p, c, lb<rb);
    	}

		int mid,
			bm = lc < 0 ? lb * s : lb_c * s + lc,
			be = rc < 0 ? rb * s : rb_c * s + rc,
			fm = lc < 0 ? 0 : s - lc;

		// handle fragment
    	if(b < B) {
    		// in-place
    		int lo = rotatePart(array, b, B-1, p, c);
			if(rb == 0) { // missing full buffer
    			Writes.arraycopy(array, b, array, a + bm, lo, 1, true, false);
    			Writes.arraycopy(tmp, 0, array, a + bm + lo, t, 1, true, false);
			} else {
    			int sz = Math.min(lo, fm);

    			// copy section of fragment
    			Writes.arraycopy(array, b, array, a + bm, sz, 1, true, false);

    			// a
    			int vi = a + lb * s + be;
    			if(lo <= fm) {
    				Writes.arraycopy(array, a + lb * s, array, b + lo - be, be, 1, true, false);
    				Writes.arraycopy(tmp, 0, array, a + bm + lo, t, 1, true, false);
    			} else {
        			// this approach does the fewest writes.
    				// it looks like garbage and is nowhere near cache friendly,
    				// but it's the best i got.
        			Writes.arraycopy(array, b + sz, array, vi, lo - sz, 1, true, false);
        			Writes.arraycopy(tmp, 0, array, vi + lo - sz, t, 1, true, false);
        			IndexedRotations.juggling(array, a + lb * s, vi, b + lo, 1, true, false);
    			}
			}
			mid = a + bm + lo;
    	} else {
			Writes.arraycopy(array, a + lb * s, array, b - be, be, 1, true, false);
			Writes.arraycopy(tmp, 0, array, mid = a + bm, t, 1, true, false);
    	}
    	return mid;
    }

	public void advLogSort(int[] array, int[] tmp, int a, int b, int s, int depth) {
		Writes.recordDepth(depth++);
		int p, m;
		boolean bad = false;
		while(b - a > 32) {
			if(bad) {
				p = array[rankof243s(array, a, b)];
				bad = false;
			} else
				p = array[pseudomo81(array, a, b)];
			m = partition(array, tmp, a, b, p, 1, s);
			if(m == b) { // pivot is highest rank
				// repartition with different bias
				b = m = partition(array, tmp, a, b, p, 0, s);
				continue;
			}
			int left = m - a, right = b - m;
			bad = left >= 8 * right || right >= 8 * left;
			if(left > right) {
				Writes.recursion();
				advLogSort(array, tmp, m, b, s, depth);
				b = m;
			} else {
				Writes.recursion();
				advLogSort(array, tmp, a, a = m, s, depth);
			}
		}

		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		smallSort.customBinaryInsert(array, a, b, 0.25);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int l = productLog(length)[0];
		int[] tmp = Writes.createExternalArray(l);
		advLogSort(array, tmp, 0, length, l, 0);
	}
}