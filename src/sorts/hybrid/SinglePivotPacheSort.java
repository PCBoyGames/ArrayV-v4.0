package sorts.hybrid;
import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;
import sorts.select.OptimizedLazyHeapSort;

final public class SinglePivotPacheSort extends Sort {
    public SinglePivotPacheSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Single-Pivot Pache");
        this.setRunAllSortsName("Single-Pivot Pache Sort");
        this.setRunSortName("Single-Pivot Pache Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static final boolean deterministic = false;
    private static final int maxNoPartition = 128;
    private OptimizedLazyHeapSort lh;

    // indeterministic: O(n log n) worst
    // deterministic: O(n^1.5) worst, if there's a fast enough
    // way to get (near) evenly-spaced pivots for the bucketing,
    // it could be on par with the indeterministic variant

    private int log(int v) {
        return 32-Integer.numberOfLeadingZeros(v-1);
    }

    // unnecessary variables and functions removed, op-cached
    private class BitArray {
        private final int[] array;
        private final int pa, pb, w;
        private int cache, val;
        private boolean INCR;

        public final int size;

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
            if (this.getBit(a, b) ^ bit)
                this.flipBit(a, b);
        }

        public void set(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            if (cache == idx) {
                if (INCR) this.clearCache();
                else {
                    this.xor(idx, val^uInt);
                    return;
                }
            } else if (!INCR) {
                cache = idx; val = uInt;
            }

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
                this.setBit(i, j, (uInt & 1) == 1);

            if (uInt > 0) System.out.println("Warning: Word too large");
        }

        public void xor(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            if (cache == idx) {
                if (INCR) this.incrByCache(false);
                else     val ^= uInt;
            }

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>>= 1)
                if ((uInt & 1) == 1) this.flipBit(i, j);

            if (uInt != 0) System.out.println("Warning: Word too large");
        }

        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            if (cache == idx) {
                if (INCR) {
                    val = this.incrByCache(true);
                    cache = idx;
                    if (val >= 0) return val;
                } else return val;
            }

            int r = 0, s = idx*w;
            for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;

            if (!INCR) {
                cache = idx; val = r;
            }
            return r;
        }

        // breaks down an O(b) operation into O(1) [amortized]
        private int incrByCache(boolean nRet) {
            if (val == 0) return -1;
            int s = cache*w, i1 = pa+s+w, i = pa+s, j=pb+s, v = 0, k = 0;
            for (; i < i1; i++, j++, k++) {
                int valbit = val & 1;
                if (valbit == 1) this.flipBit(i, j);

                if (val == 0) break;

                if (nRet || valbit == 1) {
                    boolean set = this.getBit(i, j);
                    v += (set ? 1 : 0) << k;
                    int carry = set ? 0 : valbit;
                    val = (val >>> 1) + carry;
                } else
                    val >>>= 1;
            }
            for (; nRet && i < i1; i++, j++, k++) {
                v += (this.getBit(i, j) ? 1 : 0) << k;
            }
            if (val > 0) System.out.println("Warning: Integer overflow");
            clearCache();
            return nRet ? v : -1;
        }

        public void incr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            if (!INCR) clearCache();

            if (cache != idx) {
                this.incrByCache(false);
                cache = idx;
                val = 0;
                INCR = true;
            }
            val++;
        }
    }

    private void sift(int[] array, int start, int root, int len, int tmp) {
        int j = root;
        while (2*j+1 < len) {
             j = 2*j+1;
             if (j+1 < len && Reads.compareValues(array[start + j], array[start + j + 1]) < 0) {
                    j++;
             }
        }
        while (Reads.compareValueIndex(array, tmp, start + j, 0.25, true) > 0) {
             j = (j-1)/2;
        }
        while (j > root) {
             int t2 = array[start+j];
             Writes.write(array, start+j, tmp, 0.5, true, false);
             tmp = t2;
             j = (j-1)/2;
        }
        Writes.write(array, start+root, tmp, 0.5, true, false);
    }

    private void heap(int[] array, int start, int end) {
        int p = end - start;
        for (int j = (p - 1) / 2; j >= 0; --j) {
             sift(array, start, j, p, array[start + j]);
        }
        for (int j = p - 1; j > 0; --j) {
             int t = array[start + j];
             Writes.write(array, start + j, array[start], 1.0, true, false);
             sift(array, start, 0, j, t);
        }
    }

    private void redistribute(int[] array, int key, int keys, int end) {
        int a = key, carry = 0, v = 0;
        while (keys > 0) {
            if (v++>0)
                if (carry == 1) {
                    Writes.swap(array, a, a + keys++, 1, true, false);
                    heap(array, a+1, a+keys);
                } else {
                    heap(array, a, a+keys);
                }
            int Z = b(array, a + keys, end, a, false);
            IndexedRotations.cycleReverse(array, a, a + keys, Z, 1, true, false);
            a += Z - (a + keys);
            int l = a, r = a + keys, le = a + keys / 2, t = a + (keys + 1) / 2;
            while (l < le && r < end) {
                if (Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, t++, l++, 1, true, false);
                } else {
                    Writes.swap(array, t++, r++, 1, true, false);
                }
            }
            carry = keys % 2;
            keys /= 2;
        }
    }

    private void insertRun(int[] array, int start, int end, boolean d) {
        boolean invert = d;
        int l, r, m, j, t;
        for (int i=start+1; i<end; i++) {
            if (invert ^ Reads.compareIndices(array, i-1, i, 0.01, true) <= 0) {
                continue;
            }
            if (invert ^ Reads.compareIndices(array, start, i, 0.01, true) > 0) {
                Writes.reversal(array, start, i-1, 0.5, true, false);
                invert = !invert;
                continue;
            }
            l = start + 1;
            r = i - 1;
            while (l < r) {
                m = l + (r - l) / 2;
                if (invert ^ Reads.compareIndices(array, m, i, 0.0625, true) > 0) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            t = array[i];
            j = i - 1;
            while (j >= l) {
                Writes.write(array, j+1, array[j--], 0.5, true, false);
            }
            Writes.write(array, l, t, 0.5, true, false);
        }
        if (invert ^ d)
            Writes.reversal(array, start, end-1, 1, true, false);
    }

    private int medOf3(int[] array, int a, int b, int c) {
        int d;
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            d = b; b = a;
        } else
            d = a;
        if (Reads.compareIndices(array, b, c, 0.5, true) > 0) {
            if (Reads.compareIndices(array, d, c, 0.5, true) > 0) {
                return d;
            }
            return c;
        }
        return b;
    }

    private int ninther(int[] array, int a, int b) {
        if (b-a<=9)
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

    private int gaprank(int[] array, int a, int b, int g, int r) {
        int re = 0;
        while (a < b) {
            if (a != r) {
                if (Reads.compareIndices(array, a, r, 1, true) < 0) re++;
            }
            a += g;
        }
        return re;
    }

    // hopefully better median selector
    private int median(int[] array, int a, int b) {
        int s = 1;
        while (s*s<b-a) s*=2;
        if ((s/=2) < 2) return medOf3(array, a, a+(b-a)/2, b-1);
        int mid = (b-a-1)/(2*s)+1, e = (b-a) / 8, cm = a+(b-a)/2, cr = 0;
        for (int i=0; i<e; i+=s) {
            int p = pseudomo81(array, a+i, b-e+i), r = gaprank(array, a, b, s, p);
            if (Math.abs(cr-mid)>Math.abs(r-mid)) {
                cm = p;
                cr = r;
            }
        }
        return cm;
    }

    private int[] partition(int[] array, int a, int b, int p) {
        b--;
        int A, B;
        int c = A = a, d = B = b, c1 = 0, d1 = 0, C = 0;
        for (;;) {
            // find next out-of-place element
            while (a <= b && (C = Reads.compareIndexValue(array, a, p, 0.5, true)) <= 0) {
                if (C == 0) { // swap to c if equal to pivot
                    Writes.swap(array, c++, a, 0.25, true, false);
                    c1++;
                }
                a++;
            }
            // find next out-of-place element
            while (a <= b && (C = Reads.compareIndexValue(array, b, p, 0.5, true)) >= 0) {
                if (C == 0) { // swap to d if equal to pivot
                    Writes.swap(array, d--, b, 0.25, true, false);
                    d1++;
                }
                b--;
            }
            if (a == b) b--;
            if (a < b) {
                // swap both elements
                Writes.swap(array, a++, b--, 1, true, false);
            } else {
                if (b-c>=c1) // transport equals to middle left
                    for (int i=c; c1-->0;)
                        Writes.swap(array, b--, --i, 0.1, true, false);
                else { // transport inequals to left
                    for (int i=A, j=c; j<=b;)
                        Writes.swap(array, i++, j++, 0.1, true, false);
                    b -= c1;
                }
                if (d-a>=d1) // transport equals to middle right
                    for (int i=d; d1-->0;)
                        Writes.swap(array, a++, ++i, 0.1, true, false);
                else { // transport inequals to right
                    for (int i=B, j=d; j>=a;)
                        Writes.swap(array, i--, j--, 0.1, true, false);
                    a += d1;
                }
                return new int[] {b+1, a-1};
            }
        }
    }

    // ternary quickselect, works way too well
    private int[] quickselect(int[] array, int a, int b, int r) {
        int j = 0;
        int[] m;
        boolean bad = false;
        while (b-a > 24) {
            int p = bad ? median(array, a, b) : pseudomo243(array, a, b);
            m = partition(array, a, b, array[p]);
            bad = (m[0] - a) * 8 <= b - a || (b - m[1]) * 8 < b - a;
            if (m[0] <= r && r <= m[1]) return new int[] {j, m[0], m[1]};
            else if (r < m[0]) b = m[0];
            else a = m[1] + 1;
            j++;
        }
        insertRun(array, a, b, false);
        return new int[] {j};
    }

    // ping-pong merge
    private void merge(int[] array, int a, int b, int p) {
        int m = 16;
        for (int i=a; i<b; i+=m) {
            insertRun(array, i, Math.min(i+m, b), false);
        }
        boolean q=false;
        for (; m<b-a; m*=2) {
            int t=q?p:a, f=q?a:p;
            for (int i=t; i<t+b-a; i+=2*m) {
                int l=i, le=Math.min(i+m, t+b-a), r=le, e=Math.min(r+m, t+b-a);
                while (l<le&&r<e) {
                    if (Reads.compareIndices(array, l, r, 1, true) <= 0) Writes.swap(array, l++, f++, 1, true, false);
                    else Writes.swap(array, r++, f++, 1, true, false);
                }
                while (l<le) Writes.swap(array, l++, f++, 1, true, false);
                while (r<e) Writes.swap(array, r++, f++, 1, true, false);
            }
            q=!q;
        }
        if (q) for (int i=a; i<b; i++) Writes.swap(array, i, p+i-a, 1, true, false);
    }

    // binary search in pivots
    private int b(int[] array, int a, int b, int v, boolean r) {
        while (a<b) {
            int m = a+(b-a)/2;
            if (Reads.compareIndices(array, m, v, 0.25, true) > (r ? 0 : -1)) b = m;
            else a = m + 1;
        }
        return a;
    }

    // returns element, accounting for bitbuffer
    private int idx(int[] array, int v, int p, int pc, int a1, int z, boolean iv, boolean pos) {
        boolean inrange = v<a1+((pos?2:1)*(pc+1)*z); // within used bitbuffers
        if (inrange) {
            // mark bitbuffer value to ensure visibility
            Highlights.markArray(3, p+v-a1);
            // return the lowest of the elements, inverted if needed
            return (Reads.compareIndices(array, v, p+v - a1, 1, true) > 0 ^ iv) ? p + v - a1 : v;
        } else
            Highlights.clearMark(3);
        return v;
    }

    // pivot count such that the bitbuffer can fit into both partitions (don't know its growth)
    private int pivs(int v, int bb) {
        int a=0, b=v;
        while (a<b) {
            int m=a+(b-a)/2;
            if (m+2*(m+1)*bb > v) b = m;
            else a = m + 1;
        }
        while (a+2*(a+1)*bb > v) a--; // just to be sure
        return a;
    }

    private boolean sorted(int[] array, int a, int b) {
        for (int i=a; i+1<b; i++) {
            if (Reads.compareIndices(array, i, i+1, 0.1, true) > 0)
                return false;
        }
        return true;
    }

    // pache partition
    private int[] why(int[] array, int a, int b, int p, int m, boolean iv) {
        /**
         * @param p - location of swapspace for bitbuffer
         * @param m - size of swapspace
         * @param iv - whether to swap bitbuffer-related conditionals
         * @param lg - ceiling of log_2(b-a)
         * @param pc - maximum amount of pivots where the bitbuffers will
         *             fit into both sublists
         * @param a1 - range excluding pivots
         * @param cnt - counters for bitbuffer
         * @param pos - positions for buckets
         * @param v1 - cached pos[i], used for compareless clearing
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
         * @param c - cached cnt[i] for incrementing j and compareless
         *            clearing
         **/

        int lg = log(b-a), pc = pivs(Math.min(b-a, m), lg);
        if (pc < 2) {
            if (a < b) heap(array, a, b);
            return new int[] {a, a};
        }

        if (deterministic) {
            // swap elements evenly distributed across [a...b)
            for (int i=a, j=a; i<b; i+=(b-a)/pc, j++) {
                Writes.swap(array, i, j, 1, true, false);
            }
        } else {
            // swap random elements across [a...b)
            Random r = new Random();
            int i=a, j=a;
            for (; i<b; i+=2*(b-a)/pc, j++) {
                Writes.swap(array, i, j, 1, true, false);
            }
            for (i=j; i-a<pc; i++) {
                Writes.swap(array, i, r.nextInt(b-i) + i, 1, true, false);
            }
        }
        int a1 = a + pc;
        // sort pivots
        merge(array, a, a1, a1);

        BitArray cnt = new BitArray(array, iv?p:a1, iv?a1:p, pc+1, lg);
        BitArray pos = new BitArray(array, (iv?p:a1)+(pc+1)*lg, (iv?a1:p)+(pc+1)*lg, pc+1, lg);

        // count elements
        for (int i=a1; i<b; i++) {
            cnt.incr(b(array, a, a1, idx(array, i, p, pc, a1, lg, iv, false), true) - a);
        }
        Highlights.clearMark(3);

        // set positions for later use
        for (int i=0, s=0; i<=pc; i++) {
            pos.xor(i, s += cnt.get(i));
        }

        // pache shared-space bucketsort
        for (int i=0, j=a1; i<pc; i++) {
            int v1 = pos.get(i), v = v1+a1-1;

            while (j <= v) {
                // 1. get adjusted location of v in bitbuffers
                // 2. binary search v in pivots
                int v2, x = b(array, a, a1, v2 = idx(array, v, p, pc, a1, lg, iv, true), true) - a, V = array[v2], s = 0;
                while (x != i) {
                    int y = pos.get(x), t = y, t1 = 0, b1 = 0;

                    // look for first out-of-place element in bucket
                    while (t-->0 && (b1 = b(array, a, a1, t1 = idx(array, t+a1, p, pc, a1, lg, iv, true), true) - a) == x);

                    // failsafe: it dies without this
                    if (t<1) b1 = b(array, a, a1, t1 = idx(array, (t=0)+a1, p, pc, a1, lg, iv, true), true) - a;

                    // put the correct element into v
                    int nv = array[t1];
                    Writes.write(array, t1, V, 0.5, true, false);
                    V = nv;

                    // no-comps set pos[x] to t using cached value and xor
                    pos.xor(x, y^t);

                    // only recheck table element if v is within pos[x]
                    if (((v - a1) / lg) - (pc + 1) == x) {
                        int tb = (v - a1) % lg;
                        v2 = (t & (1 << tb)) > 0 ? p + v - a1 : v;
                    }
                    x = b1;
                    s++;
                }
                if (s > 0) Writes.write(array, v2, V, 0.5, true, false);
                v--;
            }

            // increment
            int c = cnt.get(i);
            j += c;

            // compareless free i from both bitbuffers using cached pos[i] and cnt[i]
            pos.xor(i, v1);
            cnt.xor(i, c);
        }

        // clear alternate idx mark
        Highlights.clearMark(3);

        // free the remaining 2 numbers
        pos.set(pc, 0);
        cnt.set(pc, 0);

        // return boundaries of pivots
        return new int[] {a, a1};
    }

    // sort bucket for low uniques
    private void sortBucket(int[] array, int a, int b) {
        if (b - a > maxNoPartition) {
            // ternary partition using a pseudomedian
            int p = b - a > 8 * maxNoPartition ? median(array, a, b) : pseudomo81(array, a, b);
            int[] v = partition(array, a, b, array[p]);

            // lazy heap both sides
            lh.lazyHeap(array, a, v[0]);
            lh.lazyHeap(array, v[1] + 1, b);
        } else {
            lh.lazyHeap(array, a, b);
        }
    }

    public void run(int[] array, int a, int b) {
        lh = new OptimizedLazyHeapSort(arrayVisualizer);
        int m1 = a+(b-a)/2, m2 = m1;

        if (sorted(array, a, b)) return;

        // quickselect middle element
        int[] z = quickselect(array, a, b, m1);

        // don't interact if one unique
        if (Reads.compareIndices(array, a, b-1, 1, true) == 0) return;

        int p = array[m1], j;

        if (z.length == 3) {
            m1 = z[1];
            m2 = z[2] + 1;
        } else {
            // find best boundaries
            do m1--; while (m1 > a && Reads.compareIndexValue(array, m1, p, 0.1, true) == 0);
               m1++;
            do m2++; while (m2 < b && Reads.compareIndexValue(array, m2, p, 0.1, true) == 0);
        }

        // partition both sides
        int[] hi = why(array, m2, b, a, m2-a, true);
        int[] lo = why(array, a, m1, m1, b-m1, false);

        // sort buckets

        // sort left half
        j = lo[1];
        for (int i=lo[0]; i<lo[1]; i++) {
            // binary search for the start of the next bucket
            int j1 = b(array, j, m1, i, false);
            // and sort between the boundaries
            sortBucket(array, j, j = j1);
        }
        // sort last bucket
        sortBucket(array, j, m1);

        // sort right half
        j = hi[1];
        for (int i=hi[0]; i<hi[1]; i++) {
            // binary search for the start of the next bucket
            int j1 = b(array, j, b, i, false);
            // and sort between the boundaries
            sortBucket(array, j, j = j1);
        }
        // sort last bucket
        sortBucket(array, j, b);


        // merge the smallest set of pivots into its corresponding half,
        // using the biggest set as buffer
        int l = lo[1] - a < hi[1] - m2 ? a : m2,
            r = l == a ? lo[1] : hi[1], ll = r - l,
            e = l == a ? m1 : b,
            tm = l == a ? m2 : a,
            c = l == a ? hi[1] : lo[1],
            lb = tm;

        for (int i=l; i<r; i++) Writes.swap(array, tm+i-l, i, 1, true, false);

        while (tm < lb + ll && r < e) {
            if (Reads.compareIndices(array, tm, r, 0.5, true) <= 0) {
                Writes.swap(array, tm++, l, 0.5, true, false);
            } else {
                Writes.swap(array, r++, l, 0.5, true, false);
            }
            l++;
        }
        while (tm < lb + ll)
            Writes.swap(array, tm++, l++, 0.5, true, false);

        // heapsort the buffer
        heap(array, lb, lb+ll);

        // and merge it into its half
        redistribute(array, lb, c-lb, lb==m2?b:m1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        run(array, 0, currentLength);
    }
}