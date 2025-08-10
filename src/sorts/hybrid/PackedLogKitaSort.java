package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;

public class PackedLogKitaSort extends Sort {
	public PackedLogKitaSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Packed Log Kita");
		this.setRunAllSortsName("Packed Log Kita Sort");
		this.setRunSortName("Packed Log Kitasort");
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

    private int logb(int v, int base) {
        return (int) (Math.log(v) / Math.log(base));
    }

	// first power of two greater than or equal to W(n), because I want to play it safe
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
    		return array[a+(b-a)/2];
    	int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
    	int c = medOf3(array, a, a+eight, a+quart);
    	int d = medOf3(array, a+quart+eight, a+half, a+half+eight);
    	int e = medOf3(array, b-quart, b-eight, b-1);
    	int f = medOf3(array, c, d, e);
    	return f;
    }

    // median of medians with customizable depth
    private int medianDepth(int[] array, int start, int end, int depth) {
        if(end-start < 9 || depth <= 0) {
            return medOf3(array, start, start+(end-start)/2, end);
        }
        int e = (end - start) / 8;
        int m0 = medianDepth(array, start, start + 2 * e, --depth);
        int m1 = medianDepth(array, start + 3 * e, start + 5 * e, depth);
        int m2 = medianDepth(array, start + 6 * e, end, depth);
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
    		int p = medianDepth(array, a+i, b-e+i-1, 4), r = gaprank(array, a, b, s, p);
    		if(Math.abs(cr-mid)>Math.abs(r-mid)) {
    			cm = p;
    			cr = r;
    		}
    	}
    	return cm;
    }

	private void blockcycle(int[] array, int a, int m, int b, int l, int w, int p, int c, boolean i) {
		for(int k = 0; k < b - 1; k++) {
			int z = get(array, a+k*l, p, w, c, i);
			while(z != k) {
				multiSwap(array, a+k*l, a+z*l, l);
				z = get(array, a+k*l, p, w, c, i);
			}
			encode(array, a+k*l, m+k*l, k);
		}
		encode(array, a+(b-1)*l, m+(b-1)*l, b-1);
	}

	private int partitionEasy(int[] array, int[] tmp, int a, int b, int p, int c) {
		int j = 0;

		for(int i = a; i < b; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(0.25);

			if(Reads.compareIndexValue(array, i, p, 0.5, true) < c)
				Writes.write(array, a++, array[i], 0.25, true, false);
			else
				Writes.write(tmp, j++, array[i], 0.25, false, true);
		}
		Writes.arraycopy(tmp, 0, array, a, j, 0.5, true, false);

		return a;
	}

	// log partition with +1 blocksize technique applied
    private int partition(int[] array, int[] tmp, int a, int b, int p, int c) {
    	int blk = tmp.length + 1;
    	if(b-a < blk) return partitionEasy(array, tmp, a, b, p, c);
    	int l = 0, r = 0, t = a, lb = 0, rb = 0;
    	// type blocks
    	for(int i=a; i<b; i++) {
    		if(Reads.compareIndexValue(array, i, p, 0.5, true) < c) {
    			// build low block using swapspace in main list
    			Writes.write(array, t+l++, array[i], 0.25, true, false);
    			if(l == blk) {
    				l = 0;
    				t += blk;
    				lb++;
    			}
    		} else {
    			if(r == blk - 1) {
    				// shift incomplete low block over, copy over complete high block
    				int t2 = array[i];
    				Writes.arraycopy(array, t, array, t+blk, l, 0.25, true, false);
    				Writes.arraycopy(tmp, 0, array, t, r, 0.25, true, false);
    				Writes.write(array, t+r, t2, 0.25, true, false);
    				t += blk;
    				r = 0;
    				rb++;
    			} else {
    				// save element to build high block
    				Writes.write(tmp, r++, array[i], 0.25, true, true);
    			}
    		}
    	}
		// sort blocks
    	int min = Math.min(lb, rb);
    	if(min > 0) {
    		int M = log(min);
    		// tag blocks with indices
    		for(int i=0, j=0, k=0; i<min; i++) {
    			while(Reads.compareIndexValue(array, a+j*blk+M, p, 0.5, true) >= c) j++;
    			while(Reads.compareIndexValue(array, a+k*blk+M, p, 0.5, true) < c) k++;
    			encode(array, a+j++*blk, a+k++*blk, i);
    		}
    		if(lb < rb) {
    			for(int i=lb+rb-1, j=0; i>=0; i--) {
    				if(Reads.compareIndexValue(array, a+i*blk+M, p, 0.5, true) >= c)
    					multiSwap(array, a+i*blk, a+(i+j)*blk, blk);
    				else j++;
    			}
    			// indexsort blocks
    			blockcycle(array, a, a+lb*blk, lb, blk, M, p, c, lb<rb);
    		} else {
    			for(int i=0, j=0; i<lb+rb; i++) {
    				if(Reads.compareIndexValue(array, a+i*blk+M, p, 0.5, true) < c)
    					multiSwap(array, a+i*blk, a+j++*blk, blk);
    			}
    			// indexsort blocks
    			blockcycle(array, a+lb*blk, a, rb, blk, M, p, c, lb<rb);
    		}
    	}
    	// redistribute fragment
    	Writes.arraycopy(tmp, 0, array, b-r, r, 1, true, false);
    	if(l > 0) {
    		Writes.arraycopy(array, t, tmp, 0, l, 0.5, true, true);
    		Writes.arraycopy(array, a+lb*blk, array, a+lb*blk+l, rb*blk, 0.5, true, false);
    		Writes.arraycopy(tmp, 0, array, a+lb*blk, l, 0.5, true, false);
    	}
    	return a+l+lb*blk;
    }

    // logselect function
    private int[] quickselect(int[] array, int[] tmp, int a, int b, int r) {
    	boolean bad = false;
    	while(b - a > 20) {
    		// select good enough median
    		int m = array[bad ? rankof243s(array, a, b) : medianDepth(array, a, b-1, 4)];
    		// partition using either bias, whichever one yields results
    		int p = partition(array, tmp, a, b, m, 0);
    		if(p == a) p = partition(array, tmp, a, b, m, 1);
    		if(p == b) {
    			// return boundary if no uniques
    			return new int[] {a, b};
    		}
    		// bad ratio is 6:1 instead of 8:1
    		bad = 6*(p-a)<b-a||6*(b-p)<b-a;
    		if(p <= r) a = p;
    		else b = p;
    	}
    	// binary insert and find boundaries on small n
    	BinaryInsertionSort i = new BinaryInsertionSort(arrayVisualizer);
    	i.customBinaryInsert(array, a, b, 0.5);
    	int m1 = a, m2 = b-1;
    	do m1--; while(Reads.compareIndices(array, m1, r, 0.1, true) == 0);
    	do m2++; while(Reads.compareIndices(array, m2, r, 0.1, true) == 0);
    	return new int[] {m1+1, m2};
    }

    private void merge(int[] array, int[] tmp, int a, int m, int b, int t, boolean aux) {
    	int l = a, r = m;
    	while(l < m && r < b) {
    		if(Reads.compareIndices(array, l, r, 0.5, true) <= 0) {
    			Writes.write(tmp, t++, array[l++], 0.5, true, aux);
    		} else {
    			Writes.write(tmp, t++, array[r++], 0.5, true, aux);
    		}
    	}
    	while(l < m)
			Writes.write(tmp, t++, array[l++], 0.5, true, aux);
    	while(r < b)
			Writes.write(tmp, t++, array[r++], 0.5, true, aux);
    }

    private void tailmerge(int[] array, int[] tmp, int a, int m, int b) {
    	Writes.arraycopy(array, m, tmp, 0, b-m, 1, true, true);
    	int l = m-1, r = b-m-1;
    	while(l >= a && r >= 0) {
    		if(Reads.compareIndexValue(array, l, tmp[r], 0.5, true) > 0) {
    			Writes.write(array, --b, array[l--], 0.5, true, false);
    		} else {
    			Writes.write(array, --b, tmp[r--], 0.5, true, false);
    		}
    	}
    	while(r >= 0)
			Writes.write(array, --b, tmp[r--], 0.5, true, false);
    }

    private void encode(int[] array, int a, int b, int v) {
    	while(v>0) {
    		if(v%2==1) Writes.swap(array, a, b, 1, true, false);
    		v/=2; a++; b++;
    	}
    }

    // Alternate alias is "dissonant encode"
    private void packedxor(int[] array, int a, int p, int v, int lo, int m) {
    	// pos:swap:val:localoffset:maxval
    	int op = 0, lv = v + lo;
    	for(int i = 0; 1 << i <= v; i++) {
    		int count = (lv & (-2 << i)) / 2 + (lv % (1 << i)); // unsafe and jank as hell, but good enough
    		if(((v >>> i) & 1) == 1) {
    			Writes.swap(array, a + i, p + count + op, 1, true, false);
    		}
    		op += (m & (-2 << i)) / 2 + ((m & (1 << i)) > 0 ? ((m % (1 << i)) + 1) : 0);
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

    private int getAndFree(int[] array, int a, int x, int lo, int m, int p, int w, int c, boolean b) {
    	int v = get(array, a, p, w, c, b);
    	packedxor(array, a, x, v, lo, m);
    	return v;
    }
    private boolean ratioBad(int a, int b, int l, int r, int blk) {
    	int max = (r - l) / blk, bitcount = 0;
    	for(int i = 0, v; (v = (max & (-2 << i)) / 2 + ((max & (1 << i)) > 0 ? (max % (1 << i) + 1) : 0)) > 0; i++, bitcount += v);
        return bitcount > b - a;
    }

    // a kitamerge based off of the properties of a linked list
    private void kitamerge(int[] array, int[] tmp, int x, int bi, int i, int ib, int j, int jb, int bj, int p, int c, int w, int w1, boolean y) {
    	// lt: left buffer tags, rt: right buffer tags, ft: block connecting to tag 0, sb: location of second aux block,
    	// l: left pointer, ln: next left index, lth: head of left buffer tags, ltt: tail of left buffer tags,
    	// lc: relative size of left buffer, ld: progress to finishing left block, r: right pointer,
    	// mb: amount of blocks before j, rn: next right index, rth: head of right buffer tags,
    	// rtt: tail of right buffer tags, rc: relative size of right buffer, rd: progress to finishing right block,
    	// cc: block counter, bb: current buffer location, bt: current tag of buffer, ls: last buffer location,
    	// fbt: first buffer tag, tc: tag count
    	// total: 6 elements of array space allocated, 23 variables defined in-sort, 35 variables + arguments
    	int[] lt = new int[3], rt = new int[] {ib, 0, 0};
    	int l = i, ln = getAndFree(array, l, x, bi, bj, p, w1, c, y), lth = 0, ltt = 1, lc = 0, ld = 0, r = j, mb = ib,
    		rn = getAndFree(array, r, x, bi + ib, bj, p, w1, c, y), rth = 0, rtt = 1, rc = 0, rd = 0, cc = 0,
    		bb, bt = 0, ls = -1, fbt = -1, tc = 0, ft = -1, sb;
    	Writes.changeAllocAmount(6);
		Writes.changeAuxWrites(1);

		// merge 2 blocks into buffer
    	for(; cc<2*w; cc++) {
    		// put lower element into tmp[cc]
    		if(jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
    			Writes.write(tmp, cc, array[l++], 0.5, true, true);
    			lc++;
    			// if block complete, go to next block according to linkedlist
    			if(++ld == w) {
    				if(--ib==0) continue;
    				ld = 0;
    				lt[ltt++%3] = ln;
        			Writes.changeAuxWrites(1);
    				l = i + ln * w;
    				ln = getAndFree(array, l, x, bi, bj, p, w1, c, y);
    			}
    		} else {
    			Writes.write(tmp, cc, array[r++], 0.5, true, true);
    			rc++;
    			// if block complete, go to next block according to linkedlist
    			if(++rd == w) {
    				if(--jb==0) continue;
    				rd = 0;
    				rt[rtt++%3] = rn + mb;
        			Writes.changeAuxWrites(1);
    				r = j + rn * w;
    				rn = getAndFree(array, r, x, bi + mb, bj, p, w1, c, y);
    			}
    		}
    	}

    	// block merging routine
    	do {
    		// merge as many blocks into left buffer as possible
    		while(lc >= rc && (ib > 0 || jb > 0)) {
    			// shift out first buffer tag in lt
    			bt = lt[lth++%3];
    			bb = i + bt * w;
    			for(cc=0; cc<w; cc++) {
    	    		// put lower element into array[bb+cc]
    				if(jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
    	    			Writes.write(array, bb+cc, array[l++], 0.5, true, false);
    	    			lc++;
    	    			// if block complete, go to next block according to linkedlist
    	    			if(++ld == w) {
    	    				if(--ib==0) continue;
    	    				ld = 0;
    	    				lt[ltt++%3] = ln;
    	        			Writes.changeAuxWrites(1);
    	    				l = i + ln * w;
    	    				ln = getAndFree(array, l, x, bi, bj, p, w1, c, y);
    	    			}
    	    		} else {
    	    			Writes.write(array, bb+cc, array[r++], 0.5, true, true);
    	    			rc++;
    	    			// if block complete, go to next block according to linkedlist
    	    			if(++rd == w) {
    	    				if(--jb==0) continue;
    	    				rd = 0;
    	    				rt[rtt++%3] = rn + mb;
    	        			Writes.changeAuxWrites(1);
    	    				r = j + rn * w;
    	    				rn = getAndFree(array, r, x, bi + mb, bj, p, w1, c, y);
    	    			}
    	    		}
    			}
    			// left has one block less of buffer
    			lc -= w;
    			if(tc++>0) {
    				if(bt == 0) {
    					// track connecting blocks
    					ft = ls;
    				} else {
        				// tag last block made with current buffer tag
        				packedxor(array, ls, x, bt, bi, bj);
    				}
    			} else {
    				// first made buffer tag gets saved for later use
    				fbt = bt;
    			}
    			ls = bb;
    		}
    		// merge as many blocks into right buffer as possible
    		while(lc <= rc && (ib > 0 || jb > 0)) {
    			// shift out first buffer tag in rt
    			bt = rt[rth++%3];
    			bb = i + bt * w;
    			for(cc=0; cc<w; cc++) {
    	    		// put lower element into array[bb+cc]
    				if(jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
    	    			Writes.write(array, bb+cc, array[l++], 0.5, true, false);
    	    			lc++;
    	    			// if block complete, go to next block according to linkedlist
    	    			if(++ld == w) {
    	    				if(--ib==0) continue;
    	    				ld = 0;
    	    				lt[ltt++%3] = ln;
    	        			Writes.changeAuxWrites(1);
    	    				l = i + ln * w;
    	    				ln = getAndFree(array, l, x, bi, bj, p, w1, c, y);
    	    			}
    	    		} else {
    	    			Writes.write(array, bb+cc, array[r++], 0.5, true, true);
    	    			rc++;
    	    			// if block complete, go to next block according to linkedlist
    	    			if(++rd == w) {
    	    				if(--jb==0) continue;
    	    				rd = 0;
    	    				rt[rtt++%3] = rn + mb;
    	        			Writes.changeAuxWrites(1);
    	    				r = j + rn * w;
    	    				rn = getAndFree(array, r, x, bi + mb, bj, p, w1, c, y);
    	    			}
    	    		}
    			}
    			// right has one block less of buffer
    			rc -= w;
    			if(tc++>0) {
    				// tag last block made with current buffer tag
    				packedxor(array, ls, x, bt, bi, bj);
    			} else {
    				// first made buffer tag gets saved for later use
    				fbt = bt;
    			}
    			ls = bb;
    		}
    	} while(ib > 0 || jb > 0);

    	// re-encode connecting blocks with target position tags,
    	// copy 0 block to target position
    	if(ltt-lth > 0) {
    		// change fbt accordingly
    		if(fbt == 0) fbt = lt[lth%3];
    		if(ft >= 0)
				packedxor(array, ft, x, lt[lth%3], bi, bj);
    		Writes.arraycopy(array, i, array, i+lt[lth%3]*w, w, 1, true, false);
    		sb = rtt-rth > 0 ? rt[rth%3] : lt[++lth%3];
    	} else {
    		// change fbt accordingly
    		if(fbt == 0) fbt = rt[rth%3];
    		if(ft >= 0)
				packedxor(array, ft, x, rt[rth%3], bi, bj);
    		Writes.arraycopy(array, i, array, i+rt[rth%3]*w, w, 1, true, false);
	    	sb = rt[++rth%3];
    	}
    	// copy buffer blocks to start
    	Writes.arraycopy(tmp, 0, array, i, w, 0.5, true, false);
    	Writes.arraycopy(tmp, w, array, i+sb*w, w, 0.5, true, false);
    	// encode accordingly
		packedxor(array, i, x, sb, bi, bj);
		packedxor(array, i + sb * w, x, fbt, bi, bj);
    	Writes.deleteExternalArrays(lt, rt);
    }

    // the only O(n) solution I have, and it's O(n/log n) space :sadge:
    private void stacktranscode(int[] array, int a, int a1, int x, int m, int bc, int w, int w1, int p, int c, boolean y) {
    	if(bc == m) return;
		int k = get(array, a1, p, w1, c, y);
		packedxor(array, a1, x, k, 0, m);
		stacktranscode(array, a, a + k * w, x, m, bc + 1, w, w1, p, c, y);
		packedxor(array, a1, x, bc, 0, m);
    }

    // indexsort with bitbuffer
    private void indexll(int[] array, int a, int b, int m, int x, int w, int w1, int p, int c, boolean y) {
    	// traverse linkedlist, transcode to index order
    	stacktranscode(array, a, a, x, m, 0, w, w1, p, c, y);
    	int i = a + w, i1 = 1;
    	for(; i < b; i += w, i1++) {
    		int j = get(array, i, p, w1, c, y);
    		if(j == 0) continue; // pre-encoded check
    		while(j != i1) {
    			int k = get(array, a+j*w, p, w1, c, y);
    			// clear bitbuffer using last index and swap block
    			packedxor(array, i, x, j, 0, m);
    			multiSwap(array, i, a + j * w, w);
    			j = k;
    		}
			packedxor(array, i, x, i1, 0, m);
    	}
    }

    private void kitaHalf(int[] array, int[] tmp, int a, int b, int x, int w, int p, int c, boolean iv) {
    	int B = b, s = tmp.length / 2, si = (s < 12 ? s : 8);
    	b -= (b - a) % s;
    	BinaryInsertionSort bi = new BinaryInsertionSort(arrayVisualizer);
    	for(int i = a; i < b; i += si) {
    		// binary insert small n
        	bi.customBinaryInsert(array, i, Math.min(i+si, b), 0.5);
    	}
    	int j = si;
    	for(; j <= tmp.length / 4; j *= 4) {
    		for(int i = a; i + j < b; i += 4 * j) {
    			// ping-pong merge groups of 4
    			merge(array, tmp, i, i+j, Math.min(i+2*j, b), 0, true);
    			merge(array, tmp, Math.min(i+2*j, b), Math.min(i+3*j, b), Math.min(i+4*j, b), 2*j, true);
    			merge(tmp, array, 0, Math.min(2*j, b-i), Math.min(4*j, b-i), i, false);
    		}
    	}
    	for(; j <= tmp.length; j *= 2) {
    		for(int i = a; i + j < b; i += 2 * j) {
    			// tailmerge pairs
    			tailmerge(array, tmp, i, i+j, Math.min(i+2*j, b));
    		}
    	}
    	int blks = (b-a)/s;
    	// encode linkedlist indices
    	for(int i = a, ii = j / s, ij = 0; i < b; i += j, ij += ii) {
    		for(int i1 = i, j1 = 1; i1 + s < b && j1 < ii; i1 += s, j1++) {
    			packedxor(array, i1, x, j1, ij, blks);
    		}
    	}
    	for(; j < b - a; j *= 2) {
    		for(int i = a; i + j < b; i += 2 * j) {
    			// kitamerge pairs
    			kitamerge(array, tmp, x, (i - a) / s, i, j/s, i+j, Math.min(j, b-i-j)/s, blks, p, c, s, log(j/s), iv);
    		}
    	}

    	indexll(array, a, b, blks, x, s, w, p, c, iv);
    	if(b < B) {
    		// merge remaining fragment
    		bi.customBinaryInsert(array, b, B, 0.5);
    		tailmerge(array, tmp, a, b, B);
    	}
    }
    private void packedLK(int[] array, int[] buf, int a, int b, int blk) {
        int p, piv;
        do {
            int m = medianDepth(array, a, b-1, logb(b-a, 7));
            piv = array[m];
	        p = partition(array, buf, a, b, piv, 1);
	        if(p == b) {
	            p = partition(array, buf, a, b, piv, 0);
		        if(p == a) return;
		        b = p;
	        } else break;
        } while(a < b);
        if(a >= b)
        	return;
	    int[] p2 = new int[] {p, p};
        if(ratioBad(a, p, p, b, blk) || ratioBad(p, b, a, p, blk)) {
            int M = a + (b - a) / 2;
            if(p > M) {
                p2 = quickselect(array, buf, a, p, M);
            } else {
                p2 = quickselect(array, buf, p, b, M);
            }
            piv = array[M];
        }
        if(p2[0] >= 0)
        	kitaHalf(array, buf, a, p2[0], p2[0], blk, piv, 1, true);
        if(p2[1] >= 0)
        	kitaHalf(array, buf, p2[1], b, a, blk, piv, 0, false);
    }

    public void packedLogkita(int[] array, int a, int b) {
    	int lg = productLog(b-a)[1];
    	int[] aux = Writes.createExternalArray(2*lg);
        packedLK(array, aux, a, b, lg);
    }

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		packedLogkita(array, 0, sortLength);
	}
}