package sorts.hybrid;

import sorts.insert.InsertionSort;
import sorts.templates.Sort;
import main.ArrayVisualizer;

public class PackederLogMergeSort extends Sort {
    public PackederLogMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Packeder Log Merge");
        this.setRunAllSortsName("Packeder Log Merge Sort");
        this.setRunSortName("Packeder Log Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int log(int v) {
        if (v == 0) return 0;
        return 31 - Integer.numberOfLeadingZeros(v);
    }

    private int log(int v, int base) {
        return (int) (Math.log(v) / Math.log(base));
    }

    private int w(int n) {
		int r = 1;
		while((r<<r)+r-1 < n) r++;
        return r;
    }

    private InsertionSort is;

    //median selection and sort helper code for aeosqsort by Anonymous0726

    private int medianOf3(int[] array, int... indices) {
		int tmp;
		if (Reads.compareIndices(array, indices[0], indices[1], 0.125, true) > 0) {
			tmp = indices[1];
			indices[1] = indices[0];
		} else tmp = indices[0];
		if (Reads.compareIndices(array, indices[1], indices[2], 0.125, true) > 0) {
			if (Reads.compareIndices(array, tmp, indices[2], 0.125, true) > 0) {
				return tmp;
			}
			return indices[2];
		}
		return indices[1];
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

    // median of 3
    private int m3(int[] array, int l0, int l1, int l2) {
        int t;
        if (Reads.compareIndices(array, l0, l1, 5, true) > 0) {
            t = l0; l0 = l1; l1 = t;
        }
        if (Reads.compareIndices(array, l1, l2, 5, true) > 0) {
            t = l1; l1 = l2; l2 = t;
            if (Reads.compareIndices(array, l0, l1, 5, true) > 0) {
                return l0;
            }
        }
        return l1;
    }

    // median of medians with customizable depth
    private int medianDepth(int[] array, int start, int end, int depth) {
        if (end-start < 9 || depth <= 0) {
            return m3(array, start, start+(end-start)/2, end);
        }
        int e = (end - start) / 8;
        int m0 = medianDepth(array, start, start + 2 * e, --depth);
        int m1 = medianDepth(array, start + 3 * e, start + 5 * e, depth);
        int m2 = medianDepth(array, start + 6 * e, end, depth);
        return m3(array, m0, m1, m2);
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        for (int i=0; i<s; i++) {
            Writes.swap(array,a+i,b+i,1,true,false);
        }
    }

    private void xor(int[] array, int lo, int hi, int val, int log, boolean bit, boolean packed) {
        int o = packed ? Integer.bitCount(val) : 0;
        while(log-- > 0) {
            if ((val % 2 == 1) == bit) {
                if (packed)
                    Writes.swap(array, lo+o--, hi + log, 1, true, false);
                else
                    Writes.swap(array, lo + log, hi + log, 1, true, false);
            }
            val /= 2;
        }
    }

    private int get(int[] array, int block, int piv, int log, int bias, boolean bit) {
        int v = 0, s = 1;
        while(log-- > 0) {
            v |= (bit ^ Reads.compareIndexValue(array, block+log, piv, 1, true) > -bias) ? s : 0;
            s *= 2;
        }
        return v;
    }

    private void merge(int[] array, int[] buf, int a, int m, int b, int t, boolean aux) {
        int l = a, r = m;
        while(l < m && r < b) {
            if (Reads.compareIndices(array, l, r, 1, true) <= 0) {
                Writes.write(buf, t++, array[l++], 1, true, aux);
            } else {
                Writes.write(buf, t++, array[r++], 1, true, aux);
            }
        }
        while(l < m && l < b)
            Writes.write(buf, t++, array[l++], 1, true, aux);
        while(r < b)
            Writes.write(buf, t++, array[r++], 1, true, aux);
    }

    private void merge(int[] array, int[] tmp, int a, int b, int o, boolean startaux) {
    	if (b - a <= 12) {
            is.customInsertSort(array, a, b, 0.5, startaux);
            return;
    	}
        for (int s=startaux?o:a, i=s, j=0; j<b-a; i+=8, j+=8) {
            is.customInsertSort(startaux?tmp:array, i, Math.min(i+8, s+b-a), 0.5, startaux);
        }
        boolean A = !startaux;
        for (int j=8; j<b-a; j*=2) {
            int t=A?o:a, f=A?a:o;
            for (int i=0; i<b-a; i+=2*j) {
                merge(A?array:tmp, A?tmp:array, f+i, f+i+j, Math.min(f+i+2*j, f+b-a), t+i, A);
            }
            A=!A;
        }
        if (!A) Writes.arraycopy(tmp, o, array, a, b-a, 1, true, false);
    }
    private void tailMerge(int[] array, int[] buf, int start, int mid, int end, boolean cpy) {
        if (cpy)
            Writes.arraycopy(array, mid, buf, 0, end-mid, 1, true, true);
        int l=mid-1, r=end-mid-1, t=end;
        while(l >= start && r >= 0) {
            t--;
            if (Reads.compareValues(array[l], buf[r]) > 0)
                Writes.write(array, t, array[l--], 1, true, false);
            else
                Writes.write(array, t, buf[r--], 1, true, false);
        }
        while(r >= 0)
            Writes.write(array, --t, buf[r--], 1, true, false);
    }

    private int[] partition(int[] array, int[] tmp, int a, int b, int p, int blk, int B) {
        int lb = 0, rb = 0, l = 0, r = 0, t = a;
        boolean chkop = true;
        for (int i=a; i<b; i++) {
            int cmp = Reads.compareIndexValue(array, i, p, 1, true);
            chkop = chkop && (!(B == 0 ^ cmp > -B) || cmp == 0);
            if (cmp > -B) {
                Writes.write(tmp, r++, array[i], 0.25, true, true);
                if (r == blk) {
                    Writes.arraycopy(array, t, array, i-l+1, l, 1, true, false);
                    Writes.arraycopy(tmp, 0, array, t, blk, 1, true, false);
                    rb++;
                    r = 0;
                    t += blk;
                }
            } else {
                Writes.write(array, t+l++, array[i], 0.25, true, true);
                if (l == blk) {
                    lb++;
                    l = 0;
                    t += blk;
                }
            }
        }
        int m = Math.min(lb, rb);
        if (m > 0) {
            int j, k, i, h = 0, M = log(m-1)+1;
            j = k = a;
            for (i = 0; i < m; i++) {
                while(Reads.compareValues(array[j+M], p) <= -B) j += blk;
                while(Reads.compareValues(array[k+M], p) > -B) k += blk;
                xor(array, j, k, i, M, lb < rb, false);
                j += blk;
                k += blk;
            }
            if (lb < rb) {
                for (j = t - blk, k = t; j >= a; j -= blk) {
                    if (Reads.compareValues(array[j+M], p) > -B) {
                        multiSwap(array, j, k -= blk, blk);
                    }
                }
                for (i = a; i < k - blk; i += blk, h++) {
                    int w = get(array, i, p, M, B, false);
                    while(h != w) { // index sort
                        multiSwap(array, a+w*blk, i, blk);
                        w = get(array, i, p, M, B, false);
                    }
                    xor(array, i, k+h*blk, h, M, lb < rb, false); // compareless clear the block tag
                }
                xor(array, i, k+h*blk, h, M, lb < rb, false);
            } else {
                for (j = k = a; j < t; j += blk) {
                    if (Reads.compareValues(array[j+M], p) <= -B) {
                        multiSwap(array, j, k, blk);
                        k += blk;
                    }
                }
                for (; k < t - blk; k += blk, h++) {
                    int w = get(array, k, p, M, B, false);
                    while(h != w) {
                        multiSwap(array, k+(w-h)*blk, k, blk);
                        w = get(array, k, p, M, B, false);
                    }
                    xor(array, k, a+h*blk, h, M, lb < rb, false); // compareless clear the block tag
                }
                xor(array, k, a+h*blk, h, M, lb < rb, false);
            }
        }
        Writes.arraycopy(tmp, 0, array, b-r, r, 1, true, false);
        Writes.arraycopy(array, t, tmp, 0, l, 1, true, false);
        if (l > 0)
            Writes.arraycopy(array, a+lb*blk, array, b-r-rb*blk, rb*blk, 1, true, false);
        Writes.arraycopy(tmp, 0, array, a+lb*blk, l, 1, true, false);
        return new int[] {a+l+lb*blk, chkop?1:0};
    }
    private int[] quickSelect(int[] array, int[] buf, int a, int b, int r) {
        int c = -1, M[], m;
        while(b-a >= 16) {
            int p = c == 1 ? medianOfMedians(array, a, (b-a)-(~(b-a)&1)) : medianDepth(array, a, b, log(b-a, 6));
           	M = partition(array, buf, a, b, array[p], buf.length, 0);
           	m = M[0];
           	if (M[1] == 1) {
           		if (m >= r)
           			return new int[] {-1, m, 0};
           		a = m;
           		continue;
           	}
            if (m == a || m == b) {
                return new int[] {a, b, c};
            }
            c = 0;
            if (m == b) {
                M = partition(array, buf, a, b, array[p], buf.length, 1);
                m = M[0];
                if (m <= r)
           			return new int[] {m, -1, 1};
                b = m;
                c = 1;
            }
            if (m > r)
                b = m;
            else
                a = m;
        }
        if (b - a < 16) {
            is.customInsertSort(array, a, b, 1, false);
            a = b;
        }
        return new int[] {a, b, c};
    }

    // thanks to aphitorite for this updated version
    private int onesRange(int val) {
    	int r = 0;
        for (int j = 0, k = val-1; k > 0; j++, k /= 2) {
            if (k%2 == 1) r += (k/2 + 1) << j;
            else         r += (k/2 - Integer.bitCount(k/2)) << j;
        }
        return r;
    }
    private boolean ratioBad(int a, int b, int l, int r, int blk) {
        return onesRange((r-l-1)/blk+1) > b-a;
    }
    private void blockMerge(int[] array, int[] buf, int a, int m, int b, int bit, int w, int piv, boolean invert, int bias) {
        if (Reads.compareIndices(array, m-1, m, 1, true) <= 0)
            return;
        int l = a, r = m, bufs[] = new int[] {a, m}, j = bit, wc = 1, ml = log((b-a)/w-1)+1;
        for (int i = 0; i < buf.length && (l < m || r < b); i++) {
            if (l < m && (r == b || Reads.compareValues(array[l], array[r]) <= 0)) {
                Writes.write(buf, i, array[l++], 1, true, true);
            } else {
                Writes.write(buf, i, array[r++], 1, true, true);
            }
        }
        while(l < m || r < b) {
            int idx = l - bufs[0] > 0 && (l - bufs[0] == w || Reads.compareIndices(array, bufs[0] + w - 1, bufs[1] + w - 1, 1, true) <= 0) ? 0 : 1;
            for (int c = 0; c < w; c++) {
                if (l < m && (r == b || Reads.compareValues(array[l], array[r]) <= 0)) {
                    Writes.write(array, bufs[idx]++, array[l++], 1, true, false);
                } else {
                    Writes.write(array, bufs[idx]++, array[r++], 1, true, false);
                }
            }
            xor(array, j, bufs[idx]-w, wc, ml, true, true);
            j += Integer.bitCount(wc++);
        }

        Writes.arraycopy(buf, 0, array, bufs[bufs[0] < m ? 0 : 1], w, 0.5, true, false);

        int i = a, h = 0;
        for (j = bit; i < b - w; i += w, h++) {
            int dst = get(array, i, piv, ml, bias, invert);
            while(h != dst) {
                multiSwap(array, i, a+dst*w, w);
                dst = get(array, i, piv, ml, bias, invert);
            }
            xor(array, j, i, h, ml, true, true);
            j += Integer.bitCount(h);
        }
        xor(array, j, i, h, ml, true, true);
    }
    private void sortHalf(int[] array, int[] buf, int a, int c, int bb, int blk, int piv, boolean invert, int bias) {
        int b = c - ((c - a) % blk);
        for (int i = a; i < b; i += buf.length) {
            merge(array, buf, i, Math.min(i+buf.length, b), 0, false);
        }
        for (int j = buf.length; j < b - a; j *= 2) {
            for (int i = a; i + j < b; i += 2 * j) {
                if (j > buf.length) {
                    blockMerge(array, buf, i, i + j, Math.min(i + 2 * j, b), bb, blk, piv, invert, bias);
                } else {
                    tailMerge(array, buf, i, i + j, Math.min(i + 2 * j, b), true);
                }
            }
        }
        merge(array, buf, b, c, 0, false);
        if (b < c)
            tailMerge(array, buf, a, b, c, true);
    }
    public void packedML(int[] array, int[] buf, int a, int b, int blk, boolean care) {
        int bp, p, pr[], piv;
        do {
            int m = medianDepth(array, a, b-1, log(b-a, 7));
            piv = array[m];
	        pr = partition(array, buf, a, b, piv, buf.length, 0);
	        p = pr[0];
	        if (pr[1] == 1)
	        	a = p;
	        if (p == b) {
	            p = b = partition(array, buf, a, b, piv, buf.length, 1)[0];
	            pr[1] = bp = 1;
	        } else bp = 0;
        } while(a < b && pr[1] == 1);
        if (a >= b)
        	return;
	    int[] p2 = new int[] {p, p};
	    if (care) {
	        if (ratioBad(a, p, p, b, blk) || ratioBad(p, b, a, p, blk)) {
	            int M = a + (b - a) / 2;
	            if (p > M) {
	                p2 = quickSelect(array, buf, a, p, M);
	            } else {
	                p2 = quickSelect(array, buf, p, b, M);
	            }
	            bp = p2[2];
	            piv = array[M];
	        }
	    } else {
	        if (ratioBad(p, b, a, p, blk)) {
	            p2[0] = -1;
	            packedML(array, buf, a, p, blk, false);
	        } else if (ratioBad(a, p, p, b, blk)) {
	            p2[1] = -1;
	            packedML(array, buf, p, b, blk, false);
	        }
	    }
        if (p2[0] >= 0)
        	sortHalf(array, buf, a, p2[0], p2[0], blk, piv, false, bp);
        if (p2[1] >= 0)
        	sortHalf(array, buf, p2[1], b, a, blk, piv, true, bp);
    }
    public void packedMicroLog(int[] array, int a, int b, boolean useLog, boolean careStack) {
        int lg = useLog ? log(b-a-1) + 1 : w(b-a);
        int[] buf = Writes.createExternalArray(lg);
        is = new InsertionSort(arrayVisualizer);
        packedML(array, buf, a, b, lg, careStack);
        Writes.deleteExternalArray(buf);
    }
    @Override
    public void runSort(int[] array, int nmemb, int bucketCount) {
        packedMicroLog(array, 0, nmemb, false, true);
    }
}