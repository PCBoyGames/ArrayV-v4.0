package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.select.MaxHeapSort;
import sorts.select.OptimizedLazyHeapSort;
import sorts.templates.Sort;

public class CavernousSort extends Sort {

    public CavernousSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cavernous");
        this.setRunAllSortsName("Cavernous Sort");
        this.setRunSortName("Cavernoussort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Cavernoussort: A rewrite of Unstable Lovern to try to get it to work
    // Just like before, it's an experiment, the only goal is to have an O(n log n)
    // *average* complexity and O(n) moves

    private int csqrt(int n) {
        int a = 0, b = n;
        while (a<b) {
            int m = a+(b-a)/2;
            if (m*m>=n) b = m;
            else   a = m + 1;
        }
        return a;
    }

    private int log(int v) {
        return 32-Integer.numberOfLeadingZeros(v-1);
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    private class BitArray {
        private int[] array;
        private int pa, pb, w;

        public int size, length;

        public BitArray(int[] array, int pa, int pb, int size, int w) {
            this.array = array;
            this.pa = pa;
            this.pb = pb;
            this.size = size;
            this.w  = w;
            this.length = size*w;
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

        public void free() {
            int i1 = pa+length;
            for (int i = pa, j = pb; i < i1; i++, j++)
                this.setBit(i, j, false);
        }

        public void insert(int from, int idx, int uInt, boolean uXor) {
            assert (idx >= 0 && idx < size && from > 0 && from < size - 1) : "BitArray index out of bounds";
            int p = this.get(from);
            for (int i=from, q = 0;;) {
                if (uXor) this.xor(i+1, p^q);
                else     this.set(i+1, p);
                q = p;
                if (--i>=idx) p = this.get(i);
                else break;
            }
            this.xor(idx, p ^ uInt);
        }

        public void set(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
                this.setBit(i, j, (uInt & 1) == 1);

            if (uInt > 0) System.out.println("Warning: Word too large");
        }

        public void xor(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
                if ((uInt & 1) == 1) this.flipBit(i, j);

            if (uInt > 0) System.out.println("Warning: Word too large");
        }

        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int r = 0, s = idx*w;
            for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;
            return r;
        }
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

     // get rank of r between [a,a+g...b)
    private int gaprank(int[] array, int a, int b, int g, int r) {
        int re = 0;
        while (a < b) {
            if (a != r) {
                if (Reads.compareIndices(array, a, r, 0.25, true) < 0) re++;
            }
            a += g;
        }
        return re;
    }

    // hopefully better "rank of 243s" median selector
    private int rankof243s(int[] array, int a, int b) {
        // 2^(log(b-a)/2)
        int s = 1;
        while (s*s<b-a) s*=2;

        // low n: return ninther
        if ((s/=2) < 2) return ninther(array, a, b);
        int mid = (b-a-1)/(2*s)+1, e = (b-a) / 8, cm = a+(b-a)/2, cr = 0;

        // select pmo243 with gapped rank closest to middle
        for (int i=0; i<e; i+=s) {
            int p = pseudomo243(array, a+i, b-e+i), r = gaprank(array, a, b, s, p);
            if (Math.abs(cr-mid)>Math.abs(r-mid)) {
                cm = p;
                cr = r;
            }
        }
        return cm;
    }

    private void encode(int[] array, int a, int b, int v) {
        while (v>0) {
            if (v%2==1) Writes.swap(array, a, b, 1, true, false);
            v/=2; a++; b++;
        }
    }

    private int get(int[] array, int a, int p, int l, int c, boolean b) {
        int v = 0, i = 0;
        while (l-->0) {
            v |= (Reads.compareIndexValue(array, a+i, p, 0.1, true) < c ^ b ? 1 << i : 0);
            i++;
        }
        return v;
    }

    private void blockcycle(int[] array, int a, int m, int b, int l, int w, int p, int c, boolean i) {
        for (int k = 0; k < b - 1; k++) {
            int z = get(array, a+k*l, p, w, c, i);
            while (z != k) {
                multiSwap(array, a+k*l, a+z*l, l);
                z = get(array, a+k*l, p, w, c, i);
            }
            encode(array, a+k*l, m+k*l, k);
        }
        encode(array, a+(b-1)*l, m+(b-1)*l, b-1);
    }

    private void tailmerge(int[] array, int a, int m, int b, int t) {
        multiSwap(array, m, t, b-m);
        int l = m-1, r = t+b-m-1;
        while (l>=a && r>=t) {
            if (Reads.compareIndices(array, l, r, 0.5, true) > 0) {
                Writes.swap(array, --b, l--, 0.5, true, false);
            } else {
                Writes.swap(array, --b, r--, 0.5, true, false);
            }
        }
        while (r>=t)
            Writes.swap(array, --b, r--, 0.5, true, false);
    }

    private void blockmerge(int[] array, int a, int m, int b, int pb, int p, int piv, int C, int w, boolean v) {
        int l = a, r = m, lb = a, rb = m, tc = 0;
        for (int c=0; c<2*w; c++) {
            if (r>=b||(l<m&&Reads.compareIndices(array, l, r, 0.5, true)<=0)) {
                Writes.swap(array, pb+c, l++, 1, true, true);
            } else {
                Writes.swap(array, pb+c, r++, 1, true, true);
            }
        }
        while (l<m||r<b) {
            while (l-lb>=r-rb&&(l<m||r<b)) {
                int t = lb;
                for (int c=0; c<w; c++) {
                    if (r>=b||(l<m&&Reads.compareIndices(array, l, r, 0.5, true)<=0)) {
                        Writes.swap(array, lb++, l++, 1, true, false);
                    } else {
                        Writes.swap(array, lb++, r++, 1, true, false);
                    }
                }
                encode(array, t, p+tc*w, tc++);
            }
            while (l-lb<=r-rb&&(l<m||r<b)) {
                int t = rb;
                for (int c=0; c<w; c++) {
                    if (r>=b||(l<m&&Reads.compareIndices(array, l, r, 0.5, true)<=0)) {
                        Writes.swap(array, rb++, l++, 1, true, false);
                    } else {
                        Writes.swap(array, rb++, r++, 1, true, false);
                    }
                }
                encode(array, t, p+tc*w, tc++);
            }
        }
        int t = a;
        while (lb<l) {
            multiSwap(array, t, lb, w);
            t += w; lb += w;
        }
        while (rb<r) {
            multiSwap(array, t, rb, w);
            t += w; rb += w;
        }
        multiSwap(array, pb, a, 2*w);
        blockcycle(array, a+2*w, p, tc, w, w, piv, C, v);
    }

    private void blockmergehelp(int[] array, int a, int m, int b, int pb, int p, int piv, int c, int w, boolean v) {
        int z = (b-m)%w;
        blockmerge(array, a, m, b-z, pb, p, piv, c, w, v);
        tailmerge(array, a, b-z, b, pb);
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
    private int[] quickselectS(int[] array, int a, int b, int r) {
        int[] m;
        boolean bad = false;
        while (b-a > 24) {
            // pick median based on last partition's balance
            int p = bad ? rankof243s(array, a, b) : pseudomo243(array, a, b);
            // ternary partition
            m = partition(array, a, b, array[p]);
            // imbalance 8:1
            bad = (m[0] - a) * 8 <= b - a || (b - m[1]) * 8 < b - a;

            if (m[0] <= r && r <= m[1]) return new int[] {m[0], m[1] + 1}; // r in m
            else if (r < m[0])          b = m[0];                          // r < m
            else                       a = m[1] + 1;                      // m < r
        }
        BinaryInsertionSort i = new BinaryInsertionSort(arrayVisualizer);
        i.customBinaryInsert(array, a, b, 0.5);
        int m1 = r, m2 = r;
        do m1--; while (Reads.compareIndices(array, m1, r, 0.1, true) == 0);
           m1++;
        do m2++; while (Reads.compareIndices(array, m2, r, 0.1, true) == 0);
        return new int[] {m1, m2};
    }

    private int[] triquickselect(int[] array, int a, int b, int r1, int r2, int r3) {
        int a1 = a, b1 = b, a2 = a, b2 = b, m[], l = 0;
        int[] q = new int[6];
        boolean bad = false;
        for (;;) {
            // pick median based on last partition's balance
            int p = bad ? rankof243s(array, a, b) : pseudomo243(array, a, b);
            // ternary partition
            m = partition(array, a, b, array[p]);
            // imbalance 8:1
            bad = (m[0] - a) * 8 <= b - a || (b - m[1]) * 8 < b - a;
            if (l % 2 == 0 && m[0] <= r1 && r1 <= m[1]) {
                q[0] = m[0]; q[1] = m[1] + 1; l |= 1;
            } else if (m[1] <= r1 && m[1] + 1 > a1) a1 = m[1] + 1;
            else if (r1 < m[0] && m[0] < b1) b1 = m[0];

            if (l / 2 == 0 && m[0] <= r3 && r3 <= m[1]) {
                q[4] = m[0]; q[5] = m[1] + 1; l |= 2;
            } else if (m[1] <= r3 && m[1] + 1 > a2) a2 = m[1] + 1;
            else if (r3 < m[0] && m[0] < b2) b2 = m[0];

            if (m[0] <= r2 && r2 <= m[1]) {
                q[2] = m[0]; q[3] = m[1] + 1; break;
            } else if (r2 < m[0]) {
                b = m[0];
            } else {
                a = m[1] + 1;
            }
        }
        bad = false;
        if (l % 2 == 0)
            for (;;) {
                int p = bad ? rankof243s(array, a1, b1) : pseudomo243(array, a1, b1);
                // ternary partition
                m = partition(array, a1, b1, array[p]);
                // imbalance 8:1
                bad = (m[0] - a1) * 8 <= b1 - a1 || (b1 - m[1]) * 8 < b1 - a1;

                if (l / 2 == 0 && m[0] <= r3 && r3 <= m[1]) {
                    q[4] = m[0]; q[5] = m[1] + 1; l |= 2;
                } else if (m[1] <= r3 && m[1] + 1 > a2) a2 = m[1] + 1;
                else if (r3 < m[0] && m[0] < b2) b2 = m[0];

                if (m[0] <= r1 && r1 <= m[1]) {
                    q[0] = m[0]; q[1] = m[1] + 1; break;
                } else if (r1 < m[0]) b1 = m[0];
                else a1 = m[0] + 1;
            }
        bad = false;
        if (l / 2 == 0)
            for (;;) {
                int p = bad ? rankof243s(array, a2, b2) : pseudomo243(array, a2, b2);
                // ternary partition
                m = partition(array, a2, b2, array[p]);
                // imbalance 8:1
                bad = (m[0] - a2) * 8 <= b2 - a2 || (b2 - m[1]) * 8 < b2 - a2;

                if (m[0] <= r3 && r3 <= m[1]) {
                    q[4] = m[0]; q[5] = m[1] + 1; break;
                } else if (r3 < m[0]) b2 = m[0];
                else a2 = m[1] + 1;
            }
        return q;
    }

    private void min(int[] array, int a, int b) {
        int m = a;
        for (int i = a + 1; i < b; i++) {
            if (Reads.compareIndices(array, i, m, 0.1, true) < 0)
                m = i;
        }
        Writes.swap(array, a, m, 1, true, false);
    }

    private int min(int[] array, int a, int b, int g, int p) {
        int m = a;
        boolean c = Reads.compareIndexValue(array, a, p, 0.05, true) > 0;
        for (int i = a + g; i < b; i += g) {
            boolean d = Reads.compareIndexValue(array, i, p, 0.05, true) > 0;
            if (!c || (d && Reads.compareIndices(array, i, m, 0.05, true) < 0)) {
                m = i;
                c = d;
            }
        }
        return m;
    }

    private void lazyheap(int[] array, int a, int b, int k, int p) {
        int s = csqrt(b-a);
        for (int i=a; i<b; i+=s) {
            min(array, i, Math.min(i+s, b));
        }
        for (int i=a; i<b; i++) {
            int t = array[k], u = min(array, a, b, s, p);
            if (i<b-1) {
                int v = min(array, u+1, Math.min(u+s, b), 1, p);
                Writes.write(array, k++, array[u], 0.033, true, false);
                Writes.write(array, u, array[v], 0.033, true, false);
                Writes.write(array, v, t, 0.033, true, false);
            } else
                Writes.swap(array, u, k, 0.05, true, false);
        }
    }

    private void lazyheap(int[] array, int a, int b) {
        OptimizedLazyHeapSort s = new OptimizedLazyHeapSort(arrayVisualizer);
        s.lazyHeap(array, a, b);
    }

    // binary search left in uniques, values rightbound
    private int binSearchZ(int[] array, int a, int b, int v) {
        int l = a;
        while (a < b) {
            int m = b - (b - a) / 2;
            if (Reads.compareIndices(array, m, v, 0.5, true) > 0) {
                b = m - 1;
            } else {
                a = m;
            }
        }
        if (Reads.compareIndices(array, a, v, 0.5, true) > 0)
            return a - l - 1;
        return a - l;
    }

    // binary search for first free in bucket
    private int binSearchB(int[] array, int a, int b, int p) {
        while (a < b) {
            int m = a + (b - a) / 2;
            if (Reads.compareIndexValue(array, m, p, 0.5, true) > 0) {
                a = m + 1;
            } else {
                b = m;
            }
        }
        return a;
    }

    private void grid(int[] array, BitArray idx, int a, int b, int c, int p, int w) {
        if (b - a < 2 * w) {
            lazyheap(array, a, b);
            return;
        }
        // quickselect middle element
        quickselectS(array, a, a+2*w, a+w);
        // select min in first block
        min(array, a, a+w);

        // swap blocks into buffer areas
        multiSwap(array, c+1, a+1, w-1);
        multiSwap(array, c+2*w+1, a+w+1, w-1);
        // swap heads into list
        Writes.swap(array, a+w, a+1, 1, true, false);
        idx.xor(1, 1);
        int s = 2, small = 0;
        for (int i=a+2*w; i<b; i++) {
            // search for head containing z
            int z1 = binSearchZ(array, a, a+s-1, i), z = idx.get(Math.max(z1, 0));
            if (z1 < 0) small++;
            int a1 = c + z * 2 * w, b1 = a1 + 2 * w;
            // search for first free in bucket
            int q = binSearchB(array, a1 + 1, b1 - 1, p);
            // swap into bucket
            Writes.swap(array, i, q, 1, true, false);
            if (++q >= b1) {
                // quickselect middle element
                int sr = w;
                boolean lowest = small > 0 && z1 < 1;
                if (lowest) {
                    System.out.println("lowest case");
                    sr = small+1;
                    small = 0;
                }
                quickselectS(array, a1+1, b1, a1+sr);
                if (lowest) {
                    // find minimum of smaller values
                    min(array, a1+1, a1+sr);
                    // swap in head of new block
                    Writes.swap(array, a1+1, a+s, 1, true, false);
                    Writes.swap(array, a1+sr-1, a1+1, 1, true, false);
                    // swap block into new area
                    multiSwap(array, a1+sr, c+s*2*w+1, 2*w-sr);
                    sr--; // so I don't have to have to share so much code
                } else {
                    // insert head of new block into list
                    Writes.swap(array, a1+sr, a+s, 1, true, false);
                    // swap new block into first free
                    multiSwap(array, a1+sr+1, c+s*2*w+1, (2*w-sr)-1);
                }
                Writes.multiSwap(array, a+s, a+(lowest?0:z1+1), 1, true, false);
                // insert into bitbuffer (as far as I can tell, there's
                // definitely enough space to do a gavl bitbuffer, but why)
                idx.insert(s, Math.max(z1, 0)+1, s++, true);
            }
        }
        for (int i=s-1, k=b; i>=0; i--) {
            // pop index of last block
            int z = idx.get(i);
            int a1 = c + z * 2 * w, b1 = a1 + 2 * w;
            // search for end of bucket
            int q = binSearchB(array, a1 + 1, b1 - 1, p);
            k -= q - a1;
            // swap head into place
            Writes.swap(array, a+i, k, 1, true, false);
            // lazy heap the bucket
            lazyheap(array, a1+1, q, k+1, p);
            if (i == 0 && small > 0) {
                Writes.multiSwap(array, k, k+small, 1, true, false);
            }
            idx.xor(i, z);
        }
    }

    // this is the most ridiculous function I have made in terms of args:
    // main array, start, end, gridsort pivot, grid swap start, grid swap end,
    // bitbuffer pa start, bitbuffer pa end, bitbuffer pb start, bitbuffer pb end,
    // logmerge aux start, logmerge aux end, log n, size of blocks, logmerge invert
    private void gridhelp(int[] array, int a, int b, int pi1, int pi2, int c, int d, int q, int r, int s, int t, int o, int p, int S, int w, int C, boolean v) {
        int e = (d - c + 1) / 2, f = (r-q)/S, g = (t-s)/S, h=Math.min(f, g);
        if (h < (b - a) / w / 2 || 6*e < b - a || p - o < b - a) {
            System.out.printf("heap fallback: %d & %d, %d & %d, %d & %d\n", h, (b-a)/w/2, 6*e, b-a, p-o, b-a);
            MaxHeapSort mh = new MaxHeapSort(arrayVisualizer);
            mh.customHeapSort(array, a, b, 0.5);
            return;
        }
        BitArray idx = new BitArray(array, q, s, h, S);
        for (int i = a; i < b; i += e) {
            grid(array, idx, i, Math.min(i+e, b), c, pi1, w);
        }
        idx.free();
        for (; e < b - a; e *= 2) {
            for (int i = a; i + e < b; i += 2 * e) {
                int j = Math.min(e, b - i - e);
                if (j < d - c) {
                    tailmerge(array, i, i + e, i + e + j, c);
                } else {
                    blockmergehelp(array, i, i + e, i + e + j, c, p, pi2, C, w, v);
                }
            }
        }
    }

    public void cavernous(int[] array, int a, int b) {
        int l2 = log(b-a), a1 = a+(b-a)/4, m = a+(b-a)/2, b1 = b-(b-a)/4;
        if (4*l2*l2>=a1-a) {
            System.out.println("heap fallback");
            MaxHeapSort mh = new MaxHeapSort(arrayVisualizer);
            mh.customHeapSort(array, a, b, 0.5);
            return;
        }
        // quickselect 3 pivots
        int[] p = triquickselect(array, a, b, a1, m, b1);
        if (p[1] >= b-1 || Reads.compareIndices(array, a, b-1, 0.1, true) == 0) return;
        int r = array[a1], r1 = array[m], r2 = array[b1];

        // sort three quarters of the list with grid sort
        // [    S    E][E   q2   E][E q3/bbl E][E q4/bbh  ]
        // [    S    E][E S1/bbl E][E   q3   E][E q4/bbh  ]
        // [    S    E][E S1/bbl E][E S2/bbh E][E   q4    ]
        gridhelp(array, p[1], p[2], r, r1, a, p[1], p[2], p[4], p[4], b, p[2], b, l2, l2*l2, 0, false);
        gridhelp(array, p[3], p[4], r, r1, a, p[1], p[1], p[2], p[4], b, a, p[3], l2, l2*l2, 1, true);
        gridhelp(array, p[5], b, r, r2, a, p[1], p[1], p[2], p[2], p[5], a, p[5], l2, l2*l2, 1, true);

        // sort first quarter on stack
        // [    S4   E][E   S1   E][E   S2   E][E   S3    ]
        cavernous(array, a, p[1]+1);
        // sorted
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        cavernous(array, 0, sortLength);
    }

}
