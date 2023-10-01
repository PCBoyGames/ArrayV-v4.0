package sorts.merge;


import main.ArrayVisualizer;
import sorts.templates.Sort;

// Distray, 5
public class InPlaceMergeSortV extends Sort {
	public InPlaceMergeSortV(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("In-Place Merge V");
		this.setRunAllSortsName("In-Place Merge Sort V");
		this.setRunSortName("In-Place Merge Sort V");
		this.setCategory("Merge Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	// aphi, In-Place Merge Sort III
	private int binSearchR(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndices(array, m, k, 1, true) > 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}
	private int binSearchL(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndices(array, m, k, 1, true) >= 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}
	private int pos(int a, int l, int m, int v) {
		return v>=l?m+v-l:a+v;
	}
	private void multiSwap(int[] array, int a, int l, int m, int p1, int p2, int s) {
		for (int i=0; i<s; i++) {
			int p = pos(a, l, m, p1+i),
				q = pos(a, l, m, p2+i);
			Writes.swap(array, p, q, 0.5, true, false);
		}
	}
	/*
	public void rotateHe(int[] array, int a, int l, int m, int p, int l1, int l2) {
		while (l1 > 1 && l2 > 1) {
			if (l1 > l2) {
				multiSwap(array, a, l, m, p, p+l1, l2);
				l1 -= l2; p += l2;
			} else {
				multiSwapRev(array, a, l, m, p, p+l2, l1);
				l2 -= l1;
			}
		}
					// this gave me issues for so long until i noticed
		if (l2 == 1)		 insert(array, a, l, m, p+l1, p);
		else if (l1 == 1) insert(array, a, l, m, p, p+l2);
	}
	*/

	public void rotateNe(int[] array, int a, int l, int m, int p, int l1, int l2) {
		int r, k;
		while (l1 > 0 && l2 > 0) {
			if (l1 > l2) {
				r = l1 % l2;
				for (int i = 0; i < l2; i++) {
					int t = array[pos(a, l, m, p+i+l1)];
					for (int j = l2; j <= l1 - r; j += l2) {
						k = p + i + l1 - j;
						Writes.write(array, pos(a, l, m, k+l2), array[pos(a, l, m, k)], 0.5, true, false);
					}
					Writes.write(array, pos(a, l, m, p+i+r), t, 0.5, true, false);
				}
				l1 %= l2;
			} else {
				r = l2 % l1;
				for (int i = 0; i < l1; i++) {
					int t = array[pos(a, l, m, p+i)];
					for (int j = l1; j <= l2 - r; j += l1) {
						k = p + i + j;
						Writes.write(array, pos(a, l, m, k-l1), array[pos(a, l, m, k)], 0.5, true, false);
					}
					Writes.write(array, pos(a, l, m, p+i+l2-r), t, 0.5, true, false);
				}
				p += l2 - r;
				l2 %= l1;
			}
		}
	}


    public void rotate(int[] array, int a, int l, int m, int p, int l1, int l2) {
    	if (l1 < 1 || l2 < 1) return;
    	if (l1 % l2 == 0 || l2 % l1 == 0) {
    		rotateNe(array, a, l, m, p, l1, l2);
    		return;
    	}

    	int A = p, B = p + l1 - 1, C = B + 1, D = B + l2, t;
    	int PA, PB, PC, PD;

    	while (A < B && C < D) {
    		PA = pos(a, l, m, A++); PB = pos(a, l, m, B--);
    		PC = pos(a, l, m, C++); PD = pos(a, l, m, D--);
    		// ABCD -> CADB
    		t = array[PB];
    		Writes.write(array, PB, array[PA], 0.5, true, false);
    		Writes.write(array, PA, array[PC], 0.5, true, false);
    		Writes.write(array, PC, array[PD], 0.5, true, false);
    		Writes.write(array, PD, t, 0.5, true, false);
    	}

    	while (A < B) {
    		PA = pos(a, l, m, A++); PB = pos(a, l, m, B--);
    		PD = pos(a, l, m, D--);
    		// ABD -> DAB
    		t = array[PB];
    		Writes.write(array, PB, array[PA], 0.5, true, false);
    		Writes.write(array, PA, array[PD], 0.5, true, false);
    		Writes.write(array, PD, t, 0.5, true, false);
    	}

    	while (C < D) {
    		PA = pos(a, l, m, A++); PC = pos(a, l, m, C++);
    		PD = pos(a, l, m, D--);
    		// ACD -> CDA
    		t = array[PC];
    		Writes.write(array, PC, array[PD], 0.5, true, false);
    		Writes.write(array, PD, array[PA], 0.5, true, false);
    		Writes.write(array, PA, t, 0.5, true, false);
    	}

    	while (A < D) {
    		PA = pos(a, l, m, A++); PD = pos(a, l, m, D--);
    		// REVERSE
    		Writes.swap(array, PA, PD, 0.5, true, false);
    	}
    }
	public void inPlaceMerge5(int[] array, int a, int l, int m, int r) {
		if (l <= 0 || r <= 0 || Reads.compareIndices(array, a+l-1, m, 1, true) <= 0) // avoid unnecessary merging with sorted check
			return;
		if (l == 1 && r == 1) {
			Writes.swap(array, a, m, 1, true, false);
			return;
		}
		int ta = a;
		int tm = m, b = m + r;

		a = binSearchR(array, a, a+l-1, m); // get first element greater than array[m]
		l -= a - ta;

		m = binSearchL(array, m, b, a); // get first element greater than array[a]

		// loop (break if already greater)
		for (;l>0;) {
			int w = m - tm;
			// break away if current block too large
			if (w > l) {
				break;
			}
			// search for next m after merge
			int n = binSearchL(array, m, b, Math.min(a + w, a + l - 1));
			// swap in new block
			multiSwap(array, a, l, tm, 0, l, w);
			// merge
			inPlaceMerge5(array, tm, w, m, n - m);
			// apply iteration to relevant vars
			a += w; l -= w; m = n;
		}
		// merge part separately
		inPlaceMerge5(array, a, l, m, b - m);
		// rotate it back into place
		rotate(array, a, l, tm, 0, l, m-tm);
	}

	public void IPM5(int[] array, int start, int end) {
		for (int i=1; i<end-start; i*=2) {
			for (int j=start; j<end; j+=2*i) {
				if (j+i>=end)
					break;
				if (j+2*i<end)
					inPlaceMerge5(array,j,i,j+i,i);
				else
					inPlaceMerge5(array,j,i,j+i,end-(j+i));
			}
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		IPM5(array, 0, length);
	}
}