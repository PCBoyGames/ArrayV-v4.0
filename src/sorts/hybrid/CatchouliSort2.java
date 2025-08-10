package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class CatchouliSort2 extends Sort {
    public CatchouliSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Catchouli II");
        this.setRunAllSortsName("Catchouli Sort II");
        this.setRunSortName("Cachesort II");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int[] LP = {1, 1, 3, 5, 9, 15, 25, 41, 67, 109, 177, 287, 465, 753, 1219, 1973,
    						   3193, 5167, 8361, 13529, 21891, 35421, 57313, 92735, 150049, 242785,
    						   392835, 635621, 1028457, 1664079, 2692537, 4356617, 7049155, 11405773,
    						   18454929, 29860703, 48315633, 78176337, 126491971, 204668309, 331160281,
    						   535828591, 866988873, 1402817465}; // recursive's too slow :(
    private static int maxNoPartition = 192;
	private static int minQSI = 16;

    // Cachesort: A different form of SPP with no bitbuffer indice shenanigans.
    // Works off of code from Cavernous, which works off of code from Unstable
    // Lovern. Also uses unreleased Leonardo Heap code

	private int log(int v) {
		return 32-Integer.numberOfLeadingZeros(v-1);
	}

	// not a true "last power of two less than", but better for the quickselect
	private int potlt(int n) {
		int r = 1;
		while(r<n/2) r *= 2;
		return r;
	}

	private int flrMed(int a, int b, int div) {
		return a + div * ((b - a) / (2 * div));
	}

	private void multiSwap(int[] array, int a, int b, int s) {
		while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}

	private class BitArray {
		private int[] array;
		private int pa, pb, w;
		private int cache, val;
		private boolean INCR;

		public int size;

		public BitArray(int[] array, int pa, int pb, int size, int w) {
			this.array = array;
			this.clearCache();
			this.pa = pa;
			this.pb = pb;
			this.size = size;
			this.w  = w;
		}

		private void clearCache() {
			cache = -1;
			val = 0;
			INCR = false;
		}

		private void flipBit(int a, int b) {
			Writes.swap(array, a, b, 0.5, true, false);
		}

		private boolean getBit(int a, int b) {
			return Reads.compareIndices(array, a, b, 0, false) > 0;
		}

		private void setBit(int a, int b, boolean bit) {
			if(this.getBit(a, b) ^ bit)
				this.flipBit(a, b);
		}

		public void set(int idx, int uInt) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			if(cache == idx) {
				if(INCR) this.clearCache();
				else {
					this.xor(idx, val^uInt);
					return;
				}
			} else if(!INCR) {
				cache = idx; val = uInt;
			}

			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
				this.setBit(i, j, (uInt & 1) == 1);

			if(uInt > 0) System.out.println("Warning: Word too large");
		}

		public void xor(int idx, int uInt) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			if(cache == idx) {
				if(INCR) this.incrByCache(false);
				else     val ^= uInt;
			}

			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>>= 1)
				if((uInt & 1) == 1) this.flipBit(i, j);

			if(uInt != 0) System.out.println("Warning: Word too large");
		}

		public int get(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			if(cache == idx) {
				if(INCR) {
					val = this.incrByCache(true);
					cache = idx;
					if(val >= 0) return val;
				} else return val;
			}

			int r = 0, s = idx*w;
			for(int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
				r |= (this.getBit(i, j) ? 1 : 0) << k;

			if(!INCR) {
				cache = idx; val = r;
			}
			return r;
		}

		// breaks down an O(b) operation into O(1) [amortized]
		private int incrByCache(boolean nRet) {
			if(val == 0) return -1;
			int s = cache*w, i1 = pa+s+w, i = pa+s, j=pb+s, v = 0, k = 0;
            boolean vn = this.val < 0;
			for(; i < i1; i++, j++, k++) {
				int valbit = val & 1;
				if(valbit == 1) this.flipBit(i, j);

				if(val == 0) break;

				if(nRet || valbit == 1) {
					boolean set = this.getBit(i, j);
					v += (set ? 1 : 0) << k;
					int carry = set ? 0 : valbit;
					val = (val >>> 1) + carry;
				} else
					val >>= 1;
			}
			for(; nRet && i < i1; i++, j++, k++) {
				v += (this.getBit(i, j) ? 1 : 0) << k;
			}
			if(val > 0 && !vn) System.out.println("Warning: Integer overflow");
			clearCache();
			return nRet ? v : -1;
		}

		public void incr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			if(!INCR) clearCache();

			if(cache != idx) {
				this.incrByCache(false);
				cache = idx;
				val = 0;
				INCR = true;
			}
			val++;
		}
	}

	private int findMin(int[] array, int p, int a, int b, int s) {
		int min = p;

		for(int i = a; i < b; i += s)
			if(Reads.compareIndices(array, i, min, 0.1, true) < 0)
				min = i;

		return min;
	}

	private void lazyHeap(int[] array, int a, int b) {
		int n = b - a;
		int s = (int)Math.sqrt(n-1)+1;

		int f = a+((n-1)%s+1);
		int fMin = this.findMin(array, a, a+1, f, 1);

		for(int j = f; j < b; j += s) {
			int min = this.findMin(array, j, j+1, j+s, 1);

			if(j != min) Writes.swap(array, j, min, 1, true, false);
		}

		for(int j = a; j < b;) {
			int min = this.findMin(array, fMin, f, b, s);

			if(min == fMin) {
				if(j != min) Writes.swap(array, j, min, 1, true, false);
				if(++j == f) f += s; //check for bounds if last block is < s

				fMin = this.findMin(array, j, j+1, f, 1);
			}
			else {
				if(j == fMin) fMin = this.findMin(array, j+1, j+2, f, 1);

				int nMin = this.findMin(array, j, min+1, min+s, 1);

				if(nMin == j) Writes.swap(array, j, min, 1, true, false);

				else {
					Highlights.clearMark(2);

					int t = array[j];
					Writes.write(array, j,    array[min],  0.5, true, false);
					Writes.write(array, min,  array[nMin], 0.5, true, false);
					Writes.write(array, nMin, t,           0.5, true, false);
				}

				if(++j == f) f += s;
			}
		}
	}

    private void baseSort(int[] array, int a, int b, int expectedDist) {
    	if(b - a > 2 * expectedDist) { // only up to 4x bucket comparisons max before fallback
    		lazyHeap(array, a, b);
    		return;
    	}
    	// higher move count but less comparison overhead
    	int ll = -1, lh = -1;
        for(int i = b - 1; b - i - 1 < i - a; i--) {
        	int ii = a + b - i - 1, jj = i;
        	if(Reads.compareIndices(array, ii, jj, 0.5, true) > 0) {
        		Writes.swap(array, ii, jj, 0.5, true, false);
        	}
        }
        for(int i = b - 1; b - i - 1 < i - a; i--) {
        	int ii = a + b - i - 1, jj = i, cl = ii, ch = jj;
        	int lc = b - (ll - a) - 1, hc = a + (b - lh - 1);
        	if(ll >= ii && ll < lc && Reads.compareIndices(array, ll, lc, 0.5, true) > 0) {
        		Writes.swap(array, ll, lc, 0.5, true, false);
        	}
        	if(lh <= jj && hc < lh && Reads.compareIndices(array, hc, lh, 0.5, true) > 0) {
        		Writes.swap(array, hc, lh, 0.5, true, false);
        	}
            for(int j = ii + 1, k = jj - 1; j <= k; j++, k--) {
                if(Reads.compareIndices(array, j, cl, 0.033, true) < 0) {
                    cl = j;
                }
                if(cl < k && Reads.compareIndices(array, k, ch, 0.033, true) > 0) {
                  	ch = k;
                }
            }
            if((ll = cl) > ii) Writes.swap(array, ll, ii, 2.5, true, false);
            if((lh = ch) < jj) Writes.swap(array, lh, jj, 2.5, true, false);
        }
    }

    // sort bucket for low uniques
    private void sortBucket(int[] array, int a, int b, int expectedDist) {
    	if(b - a > maxNoPartition) {
    		// ternary partition using a pseudomedian
        	int p = b - a > 8 * maxNoPartition ? rankof2187s(array, a, b) : pseudomo243(array, a, b);
        	int[] v = partition(array, a, b, array[p]);

        	// lazy heap both sides
        	baseSort(array, a, v[0], expectedDist);
        	baseSort(array, v[1] + 1, b, expectedDist);
    	} else {
    		baseSort(array, a, b, expectedDist);
    	}
    }

	private void insertRun(int[] array, int start, int end, boolean d) {
		boolean invert = d;
		int l, r, m, j, t;
		for(int i=start+1; i<end; i++) {
			if(invert ^ Reads.compareIndices(array, i-1, i, 0.01, true) <= 0) {
				continue;
			}
			if(invert ^ Reads.compareIndices(array, start, i, 0.01, true) > 0) {
				Writes.reversal(array, start, i-1, 0.5, true, false);
				invert = !invert;
				continue;
			}
			l = start + 1;
			r = i - 1;
			while(l < r) {
				m = l + (r - l) / 2;
				if(invert ^ Reads.compareIndices(array, m, i, 0.0625, true) > 0) {
					r = m;
				} else {
					l = m + 1;
				}
			}
			t = array[i];
			j = i - 1;
			while(j >= l) {
				Writes.write(array, j+1, array[j--], 0.5, true, false);
			}
			Writes.write(array, l, t, 0.5, true, false);
		}
		if(invert ^ d)
			Writes.reversal(array, start, end-1, 1, true, false);
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
    		return array[a+(b-a)/2];
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

	// AHHHHHHHH
    private int pseudomo2187(int[] array, int a, int b) {
    	if(b-a < 4*2187) {
    		return pseudomo243(array, a, b);
    	}
    	int d = (b-a+1)/78,
    		m0 = pseudomo81(array, a, a+2*d),
    		m1 = pseudomo81(array, a+3*d, a+5*d),
    		m2 = pseudomo81(array, a+6*d, a+8*d),
    		m3 = pseudomo81(array, a+9*d, a+11*d),
    		m4 = pseudomo81(array, a+12*d, a+14*d),
    		m5 = pseudomo81(array, a+15*d, a+17*d),
    		m6 = pseudomo81(array, a+18*d, a+20*d),
    		m7 = pseudomo81(array, a+19*d, a+21*d),
    	    m8 = pseudomo81(array, a+22*d, a+24*d),
    	    m9 = pseudomo81(array, a+25*d, a+27*d),
    	    m10 = pseudomo81(array, a+28*d, a+30*d),
    	    m11 = pseudomo81(array, a+31*d, a+33*d),
    	    m12 = pseudomo81(array, a+34*d, a+36*d),
    	    m13 = pseudomo81(array, a+37*d, a+39*d),
    	    m14 = pseudomo81(array, a+40*d, a+42*d),
    		m15 = pseudomo81(array, a+43*d, a+45*d),
    		m16 = pseudomo81(array, a+46*d, a+48*d),
    		m17 = pseudomo81(array, a+49*d, a+51*d),
    		m18 = pseudomo81(array, a+52*d, a+54*d),
    		m19 = pseudomo81(array, a+55*d, a+57*d),
    		m20 = pseudomo81(array, a+58*d, a+60*d),
    	    m21 = pseudomo81(array, a+61*d, a+63*d),
    	    m22 = pseudomo81(array, a+64*d, a+66*d),
    	    m23 = pseudomo81(array, a+67*d, a+69*d), // nice
    	    m24 = pseudomo81(array, a+70*d, a+72*d),
    	    m25 = pseudomo81(array, a+73*d, a+75*d),
    	    m26 = pseudomo81(array, a+76*d, b);
    	return medOf3(array,
        	medOf3(array,
        		medOf3(array, m0, m1, m2),
        		medOf3(array, m3, m4, m5),
        		medOf3(array, m6, m7, m8)
        	),
        	medOf3(array,
        		medOf3(array, m9, m10, m11),
        		medOf3(array, m12, m13, m14),
        		medOf3(array, m15, m16, m17)
    		),
			medOf3(array,
				medOf3(array, m18, m19, m20),
				medOf3(array, m21, m22, m23),
				medOf3(array, m24, m25, m26)
    		)
    	);
    }

    private int pseudomo6561(int[] array, int a, int b) {
    	if(b-a < 16*6561)
    		return pseudomo2187(array, a, b);
    	int d=(b-a+1)/6,
    		m0 = pseudomo2187(array, a, a+d),
    		m1 = pseudomo2187(array, a+2*d, a+3*d),
    		m2 = pseudomo2187(array, a+4*d, b);
    	return medOf3(array, m0, m1, m2);
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

    // hopefully better "rank of 2187s" median selector
    private int rankof2187s(int[] array, int a, int b) {
    	// 2^(log(b-a)/2)
    	int s = 1;
    	while(s*s<b-a) s*=2;

    	// low n: return ninther
    	if((s/=2) < 2) return ninther(array, a, b);
    	int mid = (b-a-1)/(2*s)+1, e = (b-a) / 8, cm = a+(b-a)/2, cr = 0;

    	// select pmo243 with gapped rank closest to middle
    	for(int i=0; i<e; i+=s) {
    		int p = pseudomo2187(array, a+i, b-e+i), r = gaprank(array, a, b, s, p);
    		if(Math.abs(cr-mid)>Math.abs(r-mid)) {
    			cm = p;
    			cr = r;
    		}
    	}
    	return cm;
    }

    private void xor(int[] array, int lo, int hi, int val, int log, boolean bit) {
        int o = Integer.bitCount(val);
        while (log-- > 0) {
            if ((val % 2 == 1) == bit) {
                Writes.swap(array, lo + o--, hi + log, 1, true, false);
            }
            val /= 2;
        }
    }

    private int get(int[] array, int block, int piv, int log, int bias, boolean bit) {
        int v = 0, s = 1;
        while (log-- > 0) {
            v |= (bit ^ Reads.compareIndexValue(array, block+log, piv, 1, true) > -bias) ? s : 0;
            s *= 2;
        }
        return v;
    }

	private void tailmerge(int[] array, int a, int m, int b, int t) {
		multiSwap(array, m, t, b-m);
		int l = m-1, r = t+b-m-1;
		while(l>=a && r>=t) {
			if(Reads.compareIndices(array, l, r, 0.5, true) > 0) {
				Writes.swap(array, --b, l--, 0.5, true, false);
			} else {
				Writes.swap(array, --b, r--, 0.5, true, false);
			}
		}
		while(r>=t)
			Writes.swap(array, --b, r--, 0.5, true, false);
	}

    private void blockMerge(int[] array, int t, int a, int m, int b, int p, int w, int piv, boolean invert, int bias) {
        if(Reads.compareIndices(array, m-1, m, 1, true) <= 0)
            return;
        int l = a, r = m, bufs[] = new int[] {a, m}, j = p, wc = 1, ml = log((b-a)/w-1)+1;
        for(int i = 0; i < w && (l < m || r < b); i++) {
            if(l < m && (r == b || Reads.compareValues(array[l], array[r]) <= 0)) {
                Writes.swap(array, t + i, l++, 1, true, false);
            } else {
                Writes.swap(array, t + i, r++, 1, true, false);
            }
        }
        while(l < m || r < b) {
            int idx = l - bufs[0] > 0 && (l - bufs[0] == w || Reads.compareIndices(array, bufs[0] + w - 1, bufs[1] + w - 1, 1, true) <= 0) ? 0 : 1;
            for(int c = 0; c < w; c++) {
                if(l < m && (r == b || Reads.compareValues(array[l], array[r]) <= 0)) {
                    Writes.swap(array, bufs[idx]++, l++, 1, true, false);
                } else {
                    Writes.swap(array, bufs[idx]++, r++, 1, true, false);
                }
            }
            xor(array, j, bufs[idx]-w, wc, ml, true);
            j += Integer.bitCount(wc++);
        }

        multiSwap(array, t, bufs[bufs[0] < m ? 0 : 1], w);

        int i = a, h = 0;
        for(j = p; i < b - w; i += w, h++) {
            int dst = get(array, i, piv, ml, bias, invert);
            while(h != dst) {
                multiSwap(array, i, a+dst*w, w);
                dst = get(array, i, piv, ml, bias, invert);
            }
            xor(array, j, i, h, ml, true);
            j += Integer.bitCount(h);
        }
        xor(array, j, i, h, ml, true);
    }

    private void blockmergehelp(int[] array, int a, int m, int b, int pb, int p, int piv, int c, int w, boolean v) {
    	int z = (b-m)%w;
    	blockMerge(array, pb, a, m, b-z, p, w, piv, v, c);
    	tailmerge(array, a, b-z, b, pb);
    }

    private int[] partition(int[] array, int a, int b, int p) {
    	b--;
    	int A, B;
    	int c = A = a, d = B = b, c1 = 0, d1 = 0, C = 0;
    	for(;;) {
    		// find next out-of-place element
    		while(a <= b && (C = Reads.compareIndexValue(array, a, p, 0.5, true)) <= 0) {
    			if(C == 0) { // swap to c if equal to pivot
    				Writes.swap(array, c++, a, 0.25, true, false);
    				c1++;
    			}
    			a++;
    		}
    		// find next out-of-place element
    		while(a <= b && (C = Reads.compareIndexValue(array, b, p, 0.5, true)) >= 0) {
    			if(C == 0) { // swap to d if equal to pivot
    				Writes.swap(array, d--, b, 0.25, true, false);
    				d1++;
    			}
    			b--;
    		}
    		if(a == b) b--;
    		if(a < b) {
    			// swap both elements
    			Writes.swap(array, a++, b--, 1, true, false);
    		} else {
    			if(b-c>=c1) // transport equals to middle left
	    			for(int i=c; c1-->0;)
	    				Writes.swap(array, b--, --i, 0.1, true, false);
    			else { // transport inequals to left
    				for(int i=A, j=c; j<=b;)
    					Writes.swap(array, i++, j++, 0.1, true, false);
    				b -= c1;
    			}
    			if(d-a>=d1) // transport equals to middle right
	    			for(int i=d; d1-->0;)
	    				Writes.swap(array, a++, ++i, 0.1, true, false);
    			else { // transport inequals to right
    				for(int i=B, j=d; j>=a;)
    					Writes.swap(array, i--, j--, 0.1, true, false);
    				a += d1;
    			}
    			return new int[] {b+1, a-1};
    		}
    	}
    }

    // iterative optimal multirank quickselect in O(p) aux
    private int[] quickselect(int[] array, int a, int b, int... r) {
    	int e = r.length;
    	assert e > 0 : "No ranks provided for quickselect";
    	int q[][] = new int[e][], p[] = new int[2*e];
    	int[] d = new int[e];
    	for(int i=0; i<e; i++) {
    		q[i] = new int[] {a, b};
    	}
    	boolean bad;
    	int c = 0, j = 2 * potlt(e), i = 0;
    	do {
    		if(i+j/2 < e && d[i+j/2] == 0) {
    			int v = i+j/2;
    			bad = false;
    			int ak = q[v][0], bk = q[v][1];
    			while(bk-ak > minQSI) {
    	    		int mp = array[bad ? rankof2187s(array, ak, bk) : pseudomo6561(array, ak, bk)];
    	    		int[] m = partition(array, ak, bk, mp);
    	    		bad = (m[0] - ak) * 8 <= bk - ak || (bk - m[1]) * 8 < bk - ak;
    	    		for(int k = 0; k < e; k++) {
    	    			if(k != v && d[k] == 0) {
    	    				// cavernous' tri-pivot quickselect broke because would set boundaries wrong
    	    				// this is likely the correct method, but it has not been thoroughly stresstested
    	    				if(m[0] <= r[k] && r[k] <= m[1]) {
    	    					p[2*k] = m[0]; p[2*k+1] = m[1] + 1; d[k] = 1; c++; continue;
    	    				} else if(r[k] < m[0] && m[0] < q[k][1])      q[k][1] = m[0];
  	    				  	  else if(r[k] > m[1] + 1 && m[1] >= q[k][0]) q[k][0] = m[1] + 1;
    	    			}
    	    		}
    	    		if(m[0] <= r[v] && r[v] <= m[1]) {
    	    			p[2*v] = m[0]; p[2*v+1] = m[1] + 1; d[v] = 1; c++; break;
    	    		} else if(r[v] < m[0]) bk = m[0];
    	    		else                   ak = m[0] + 1;
    			}
    			if(d[v] == 0) {
    				insertRun(array, ak, bk, false);
    		    	int m2, m1 = m2 = r[v];
    		    	do m1--; while(Reads.compareIndices(array, m1, r[v], 0.1, true) == 0);
    		    	   m1++;
    		    	do m2++; while(Reads.compareIndices(array, m2, r[v], 0.1, true) == 0);
    		    	p[2*v] = m1; p[2*v+1] = m2; d[v] = 1; c++;
    			}
    		}
    		i += j;
    		if(i >= e) {
    			i = 0; j /= 2;
    		}
    	} while(c < e);
    	return p;
    }

	private void sift(int[] array, int start, int root, int len, int tmp) {
		// find highest branch
		int j = root;
		while(2*j+1 < len) {
			 j = 2*j+1;
			 if (j+1 < len && Reads.compareValues(array[start + j], array[start + j + 1]) < 0) {
					j++;
			 }
		}

		// get first element in branch higher than tmp
		while(Reads.compareValueIndex(array, tmp, start + j, 0.25, true) > 0) {
			 j = (j-1)/2;
		}

		// insert tmp into heap
		while(j > root) {
			 int t2 = array[start+j];
			 Writes.write(array, start+j, tmp, 0.5, true, false);
			 tmp = t2;
			 j = (j-1)/2;
		}

		Writes.write(array, start+root, tmp, 0.5, true, false);
	}

	private void heap(int[] array, int start, int end) {
		// heapify
		int p = end - start;
		for(int j = (p - 1) / 2; j >= 0; --j) {
			 sift(array, start, j, p, array[start + j]);
		}
		for(int j = p - 1; j > 0; --j) {
			 // pick out root, re-sift using last element in heap
			 int t = array[start + j];
			 Writes.write(array, start + j, array[start], 1.0, true, false);
			 sift(array, start, 0, j, t);
		}
	}

    private int licd(int v, int x) {
    	if(v <= 0) return x;
    	int n = 0;
    	do {
    		int mc = LP[x - 1]; // size of first child
    		if(n == v) { // v found in first trail
    			return x;
    		}
    		if(++n + mc == v) { // v found in second trail
    			return x - 1;
    		}

    		if(v >= n + mc) {
    			n += mc; // close in on v
    			x--;
    		} else x -= 2;
    	} while(x > 0);
    	return 0; // within lowest node, 0
    }

    private void lSiftDown(int[] array, int a, int b, int hl, int tmp, boolean step) {
    	while(hl > 0) {
	    	int l = a + 1, ll = l, hc = LP[hl - 1], d = 2;
	    	if(l + hc < b && Reads.compareIndices(array, ll, l + hc, 0.25, true) < 0) {
	    		ll = l + hc;
	    		d--;
	    	}
	    	if(ll < b && Reads.compareValueIndex(array, tmp, ll, 0.5, true) < 0) {
	    		Writes.write(array, a, array[ll], 0.618d, true, false);
	    		a = ll; step = true; hl -= d;
	    	} else {
	    		break;
	    	}
    	}
		if(step) Writes.write(array, a, tmp, 1, true, false);
    }

    private void lHeap(int[] array, int a, int b) {
    	int m = 0;
    	while(LP[++m+1] < b - a);
		for(int i = b - 1; i >= a; i--) {
			int d = licd(i - a, m);
			if(d > 0)
				lSiftDown(array, i, b, d, array[i], false);
		}
    	for(int i = b - 1; i > a + m / 2; i--) {
    		int t = array[i];
    		Writes.write(array, i, array[a], 2.5, true, false);
    		lSiftDown(array, a, i, m, t, true);
    	}
    	Writes.reversal(array, a, a + m / 2, 0.5, true, false);
    }

    // binary search in pivots
    private int b(int[] array, int a, int b, int v, boolean r) {
    	while(a<b) {
    		int m = a+(b-a)/2;
    		if(Reads.compareIndices(array, m, v, 0.25, true) > (r ? 0 : -1)) b = m;
    		else a = m + 1;
    	}
    	return a;
    }

    // pivot count such that the bitbuffer can fit into both partitions
    // makes O(n / log n) pivots
    private int pivs(int v) {
    	int a=0, b=v;
    	while(a<b) {
    		int m=a+(b-a)/2;
    		if(m+(m+1)*log(v-m) > v) b = m;
    		else a = m + 1;
    	}
    	while(a+(a+1)*log(v-a) > v) a--; // just to be sure
    	return a;
    }

    private int findPivs(int[] array, int a, int b) {
    	int pc = pivs(b - a), j = a;
		// swap elements evenly distributed across [a...b)
    	for (int i=a; j < a + pc && i < b; i+=(b-a)/pc, j++) {
    		Writes.swap(array, i, j, 1, true, false);
    	}
    	heap(array, a, j);
    	return j;
    }

    // pache partition
    private void why(int[] array, int a, int b, int p1, int p2, int pa, int pb, boolean iv) {
        /**
         * @param p - location of swapspace for bitbuffer
         * @param m - size of swapspac
         * @param p1 - pivots start
         * @param p2 - pivots end
         * @param pc - pivots count
         * @param pa - bitbuffer location a
         * @param pb - bitbuffer location b
         * @param iv - whether to swap bitbuffer-related conditionals
         * @param lg - ceiling of log_2(b-a)
         * @param pc - maximum amount of pivots where the bitbuffer will
         *             fit into both sublists
         * @param a1 - range excluding pivots
         * @param cntpos - counters for bitbuffer, substituted for
         *                 positions for buckets after counting
         * @param max - maximum accessed bitbuffer index
         * @param B - current binary search location
         * @param v1 - cached cntpos[i], used for compareless clearing
         * @param v - v1, adjusted to be within [a1...b)
         * @param v2 - v, adjusted due to bitbuffer
         * @param V - temporary value
         * @param s - steps taken
         * @param x - bucket being looked at
         * @param y - cached pos[x]
         * @param t - copy of y, used to find out-of-place bucket elements
         * @param t1 - t, adjusted to [a1...b), and adjusted for bitbuffer
         * @param b1 - binary search, cached for optimization purposes
         * @param nv - new value to replace V with
         **/

    	int lg = log(b-a+1);
    	if(p2 - p1 < 2) {
    		if(a < b) heap(array, a, b);
    		return;
    	}

    	int max = -1;
    	BitArray cntpos = new BitArray(array, iv?pb:pa, iv?pa:pb, (p2-p1)+1, lg);

    	// count elements
    	for(int i=a; i<b; i++) {
    		int B = b(array, p1, p2, i, false) - p1;
    		cntpos.incr(B); max = Math.max(max, B);
    	}
    	Highlights.clearMark(3);

    	// set positions
    	for(int i=0, s=0; i<=max; i++) {
    		int cv = cntpos.get(i);
    		if(cv > 0)
    			cntpos.xor(i, cv ^ (s += cv));
    	}

    	// pache shared-space bucketsort
    	for(int i=0, j=a; i<max;) {
    		int v1 = cntpos.get(i), v = v1 + a - 1;

    		while(j <= v) {
    			// 1. get adjusted location of v in bitbuffers
    			// 2. binary search v in pivots
    			int x = b(array, p1, p2, v, false) - p1, V = array[v], s = 0;
    			while(x != i) {
    				int y = cntpos.get(x), t = y, t1 = 0, b1 = 0;

    				// look for first out-of-place element in bucket
    				while(t-->0 && (b1 = b(array, p1, p2, t1 = t + a, false) - p1) == x);

    				// failsafe
    				if(t<0) t++;

    				// put the correct element into v
    				int nv = array[t1];
    				Writes.write(array, t1, V, 0.5, true, false);
    				V = nv;

    				// no-comps set cntpos[x] to t using cached value and xor
    				cntpos.xor(x, y^t);
    				x = b1;
    				s++;
    			}

    			// put temporary variable back
    			if(s > 0) Writes.write(array, v, V, 0.5, true, false);
    			v--;
    		}

    		// free cntpos[i]
    		cntpos.xor(i, v1);

    		// binary search to next bucket
    		if(i < max - 1)
    			j = b(array, v1 + a, b, p1 + i, true);

    		// iterate until next unique pivot found
    		do i++; while(i < max && Reads.compareIndices(array, p1+i-1, p1+i, 1, true) == 0);
    	}

    	// clear alternate idx mark
    	Highlights.clearMark(3);

    	// free the remaining number
    	cntpos.set(max, 0);

    	int dist = (b - a) / (p2 - p1) + 1;

    	int j = a;
    	for(int i = p1; i < p2; i++) {
    		int j1 = b(array, a, b, i, true);
    		//               ! OOB ISSUE !
    		//!!! MIGHT BE IN THE ORIGINAL OPTILAZYHEAP !!!
    		sortBucket(array, j, j = j1, dist);
    	}
		sortBucket(array, j, b, dist);
    }


    // really slow but I don't have anything much faster
    private void remerge(int[] array, int a, int a1, int m, int t, boolean doHp) {
    	for(int i = a; i < a1; i++) {
    		Writes.swap(array, i, i + t - a, 2.5, true, false);
    	}

    	int l = t, le = t + a1 - a, r = a1, re = m;
    	while(l < le && r < re) {
    		if(Reads.compareIndices(array, l, r, 0.5, true) <= 0) {
    			Writes.swap(array, a++, l++, 0.5, true, false);
    		} else {
    			Writes.swap(array, a++, r++, 0.5, true, false);
    		}
    	}
    	while(l < le)
			Writes.swap(array, a++, l++, 0.5, true, false);

    	if(doHp) lHeap(array, t, le);
    }

    public void catchouli(int[] array, int a, int b) {
    	int l2 = log(b-a), a1 = a+(b-a)/3, b1 = b-(b-a)/3;

    	// quickselect 3 pivots
    	int[] p = quickselect(array, a, b, a1, b1);

    	if(p[1] >= b-1 || Reads.compareIndices(array, a, b-1, 0.1, true) == 0) return;

    	if(l2 > pivs(p[2]-p[1])) {
    		lHeap(array, a, b);
    		return;
    	}
    	int r = array[a1], r2 = array[b1];

    	int p1 = findPivs(array, a, p[0]), p2 = findPivs(array, p[1], p[2]),
    		p3 = findPivs(array, p[3], b);

    	int c1 = a, c2 = a1, c3 = b1,
    		c4 = flrMed(a, p[0], l2), c5 = flrMed(p[1], p[2], l2),
    		c6 = flrMed(p[3], b, l2);

    	// sort halves in each quarter
    	why(array, p1, c4, a,    p1, c2, c3, false);
    	why(array, p2, c5, p[1], p2, c1, c3, false);
    	why(array, p3, c6, p[3], p3, c1, c2, false);

    	why(array, c4, p[0], a,    p1, c2, c3, false);
    	why(array, c5, p[2], p[1], p2, c1, c3, false);
    	why(array, c6, b,    p[3], p3, c1, c2, false);

    	remerge(array, a,    p1, c4, c5, false);
    	blockmergehelp(array, a,    c4, p[0], c5, p[3], r2, 1, l2, false);

    	remerge(array, p[3], p3, c6, c5, false);
    	blockmergehelp(array, p[3], c6, b,    c5, a,    r,  0, l2, true);

    	lHeap(array, c5, c5 + Math.max(p1 - a, p3 - p[3]));

    	remerge(array, p[1], p2, c5, a, false);
    	blockmergehelp(array, p[1], c5, p[2], a,  p[3], r2, 1, l2, false);

    	lHeap(array, a, a + p2 - p[1]);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
    	catchouli(array, 0, sortLength);
    }
}
