package sorts.hybrid;
import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import sorts.insert.InsertionSort;

final public class UnstableLogPartitionSort extends GrailSorting {
    public UnstableLogPartitionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Unstable Log Partition");
        this.setRunAllSortsName("Unstable Log Partition Sort");
        this.setRunSortName("Unstable Log Partition Sort");
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

    private class BitArray {
        private final int[] array;
        private final int pa, pb, w;

        public final int size;

        public BitArray(int[] array, int pa, int pb, int size, int w) {
            this.array = array;
            this.pa = pa;
            this.pb = pb;
            this.size = size;
            this.w  = w;
        }

        private void flipBit(int a, int b) {
            Writes.swap(array, a, b, 0.5, true, false);
        }
        private boolean getBit(int a, int b) {
            return Reads.compareIndices(array, a, b, 0, false) > 0;
        }

        public void xor(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
                if ((uInt & 1) == 1) flipBit(i, j);

            if (uInt > 0) System.out.printf("Warning: Word too large: %d\n", idx);
        }

        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int r = 0, s = idx*w;
            for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;
            return r;
        }

        public void incr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++) {
                this.flipBit(i, j);
                if (this.getBit(i, j)) return;
            }
            System.out.println("Warning: Integer overflow");
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

    private int[] partition(int[] array, int a, int b, int p) {
        b--;
        int c = a, d = b, c1 = 0, d1 = 0, C = 0;
        for (;;) {
            while (a <= b && (C = Reads.compareIndexValue(array, a, p, 0.5, true)) <= 0) {
                if (C == 0) {
                    Writes.swap(array, c++, a, 0.25, true, false);
                    c1++;
                }
                a++;
            }
            while (a <= b && (C = Reads.compareIndexValue(array, b, p, 0.5, true)) >= 0) {
                if (C == 0) {
                    Writes.swap(array, d--, b, 0.25, true, false);
                    d1++;
                }
                b--;
            }
            if (a == b) b--;
            if (a < b) {
                Writes.swap(array, a++, b--, 1, true, false);
            } else {
                for (int i=c; c1-->0;) {
                    Writes.swap(array, b--, --i, 0.1, true, false);
                }
                for (int i=d; d1-->0;) {
                    Writes.swap(array, a++, ++i, 0.1, true, false);
                }
                return new int[] {b+1, a-1};
            }
        }
    }

    private int quickselect(int[] array, int a, int b, int r) { // Accidentally implemented ternary first try
        InsertionSort i = new InsertionSort(arrayVisualizer);
        int j = 0;
        int[] m;
        while (b-a > 24) {
            int p = ninther(array, a, b);
            m = partition(array, a, b, array[p]);
            if (m[0] <= r && r <= m[1]) return j;
            else if (r < m[0]) b = m[0];
            else a = m[1] + 1;
            j++;
        }
        i.customInsertSort(array, a, b, 0.1, false);
        return j;
    }

    private void merge(int[] array, int a, int b, int p) {
        InsertionSort s = new InsertionSort(arrayVisualizer);
        int m = 16;
        for (int i=a; i<b; i+=m) {
            s.customInsertSort(array, i, Math.min(i+m, b), 0.1, false);
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

    private int b(int[] array, int a, int b, int v, boolean r) {
        while (a<b) {
            int m = a+(b-a)/2;
            if (Reads.compareIndices(array, m, v, 1, true) > (r ? 0 : -1)) b = m;
            else a = m + 1;
        }
        return a;
    }

    // Returns element, accounting for bitbuffer
    private int idx(int[] array, int v, int p, int a1, int z, boolean iv) {
        return v<a1+(2*(z+1)*z) && (Reads.compareIndices(array, v, p+v - a1, 1, true) > 0 ^ iv) ? p + v - a1 : v;
    }

    // Pseudo-Pache partitioning with peephole optimizations
    // -----------------------------------------------------
    // Primary optimization: One pivot instead of 2, as it
    // forces the sortspace to share space with the bitbuffer
    private void why(int[] array, int a, int b, int p, boolean iv) {
        int z = log(b-a), a1 = a+z;
        if (z+2*(z+1)*z>=b-a||z<2) { // Divide by zero, apparently
            InsertionSort s = new InsertionSort(arrayVisualizer);
            s.customInsertSort(array, a, b, 0.1, false);
            return;
        }
        // c: counts, p: positions
        BitArray c = new BitArray(array, iv?p:a1, iv?a1:p, z+1, z);
        BitArray ps = new BitArray(array, (iv?p:a1)+(z+1)*z, (iv?a1:p)+(z+1)*z, z+1, z);

        // swap elements evenly distributed across [a1...b)
        for (int i=a1, j=a; i<b; i+=(b-a)/z-1) Writes.swap(array, j++, i, 1, true, false);
        // sort keys
        merge(array, a, a1, a1);

        // count elements
        for (int i=a1; i<b; i++) {
            int j = b(array, a, a1, idx(array, i, p, a1, z, iv), false) - a;
            c.incr(j);
        }

        // set positions in ps bitbuffer
        for (int i=0, s=0; i<=z; i++) {
            int v = i==z?0:c.get(i);
            ps.xor(i, s);
            s += v;
        }

        // wacky in-place bucketsort
        for (int i=0; i<=z; i++) {
            int v1 = ps.get(i), v = v1+a1, w = c.get(i), w1 = w;
            while (w>0) {
                int v2 = idx(array, v, p, a1, z, iv);
                int x = b(array, a, a1, v2, false) - a;
                while (x != i) {
                    int y = ps.get(x), w2 = c.get(x), w3 = w2, t = y, t1, b2;

                    // find out-of-place element in target bucket
                    while ((b2 = b(array, a, a1, t1 = idx(array, t+a1, p, a1, z, iv), false) - a) == x) {
                        t++;
                        if (--w3<1) break;
                    }

                    // reset adjusted position element after hitting last element in bucket
                    if (w3<1) {
                        t1 = idx(array, t+a1, p, a1, z, iv);
                        w3 = 0;
                    } else w3--;

                    // swap numbers
                    Writes.swap(array, v2, t1, 1, true, false);

                    // increase bitbuffers accordingly
                    ps.xor(x, y^(++t));
                    c.xor(x, w2^w3);

                    // only readjust bucket element if within modified area
                    if (((v-a1)/z)%(z+1) == x)
                        v2 = idx(array, v, p, a1, z, iv);
                    x = b2;
                }
                v++;
                w--;
            }

            // clear element in both bitbuffers without extra comparisons
            c.xor(i, w1);
            ps.xor(i, v1);
        }

        // sort new buckets, find using binary search
        int i=a1;
        for (int j=a; j<a1; j++) {
            int i1 = b(array, a1, b, j, true);
            run(array, i, i = i1);
        }
        run(array, i, b);

        // merge keys back into buffer
        grailMergeWithoutBuffer(array, a, z, b-a-z);
    }

    private void run(int[] array, int a, int b) {
        int m1 = a+(b-a)/2, m2 = m1;

        // quickselect middle element
        int z = quickselect(array, a, b, m1);

        // don't interact if one unique
        if (z == 0 && Reads.compareIndices(array, a, b-1, 1, true) == 0) return;

        int p = array[m1];

        // find best boundaries
        do m1--; while (Reads.compareIndexValue(array, m1, p, 0.1, true) == 0);
           m1++;
        do m2++; while (Reads.compareIndexValue(array, m2, p, 0.1, true) == 0);
           m2--;

        int l = m1 - a, r = b - m2;
        if (l > r) {
            // merge right side, bucketsort left side
            merge(array, m2, b, a);
            why(array, a, m1, m1, false);
        } else {
            // merge left side, bucketsort right side
            merge(array, a, m1, m2);
            why(array, m2, b, a, true);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        run(array, 0, currentLength);
    }
}