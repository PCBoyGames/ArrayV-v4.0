package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

// https://unownfo.s-ul.eu/sbOh9Z56
// thing

public class OptimizedPartitionHeapMergeSort extends Sort {
    public OptimizedPartitionHeapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Partition Heap Merge");
        this.setRunAllSortsName("Optimized Partition Heap Merge Sort");
        this.setRunSortName("Optimized Partition Heap Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int medOf3(int[] array, int a, int b, int c) {
        int d = a;
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            d = b; b = a;
        }
        if (Reads.compareIndices(array, b, c, 0.5, true) > 0) {
            if (Reads.compareIndices(array, d, c, 0.5, true) > 0) {
                return d;
            }
            return c;
        }
        return b;
    }

    private int giveNinther(int[] array, int a, int b) {
        if (b-a<=9)
            return array[a];
        int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
        int c = medOf3(array, a, a+eight, a+quart);
        int d = medOf3(array, a+quart+eight, a+half, a+half+eight);
        int e = medOf3(array, b-quart, b-eight, b-1);
        int f = medOf3(array, c, d, e), fe = array[f];
        Writes.write(array, f, array[a], 1, true, false);
        return fe;
    }

    public int partition(int[] a, int p, int r, int cmp) {
        int x = giveNinther(a, p, r);

        boolean eq = false;
        int i = p, c = 0;
        int j = r-1;

        while (i <= j) {
            while (i <= j && (c = Reads.compareIndexValue(a, j, x, 1, true)) >= cmp) {
                j--;
                if (cmp == 0) eq=eq&&c==0;
            }
            if (i <= j && cmp == 1) eq=eq&&c==0;
            if (i < j) {
                Writes.write(a, i++, a[j], 1, true, false);
            } else {
                break;
            }
            while (i < j && (c = Reads.compareIndexValue(a, i, x, 1, true)) < cmp) {
                i++;
                if (cmp == 1) eq=eq&&c==0;
            }
            if (i < j && cmp == 0) eq=eq&&c==0;
            if (i < j) {
                Writes.write(a, j--, a[i], 1, true, false);
            } else {
                break;
            }
        }
        Writes.write(a, i, x, 1, true, false);
        return eq ? -1 : i;
    }

    private void sift(int[] array, int start, int root, int len, int tmp) {
        int j = root;
        while (2*j+1 < len) {
            j=2*j+1;
            if (j+1 < len && Reads.compareValues(array[start+j], array[start+j+1]) < 0) {
                j++;
            }
        }
        while (Reads.compareValueIndex(array, tmp, start+j, 0.25, true) > 0) {
            j=(j-1)/2;
        }
        for (int t2; j>root; j=(j-1)/2) {
            t2=array[start+j];
            Writes.write(array, start+j, tmp, 0.5, true, false);
            tmp=t2;
        }
        Writes.write(array, start+root, tmp, 0.5, true, false);
    }

    private void heap(int[] array, int start, int end) {
        int p=end-start;
        for (int j=(p-1)/2; j>=0; j--) {
            this.sift(array, start, j, p, array[start+j]);
        }
        for (int j=p-1; j>0; j--) {
            int t=array[start+j];
            Writes.write(array, start+j, array[start], 1, true, false);
            this.sift(array, start, 0, j, t);
        }
    }

    private int mergelowrite(int[] array, int a, int m, int b, int t, int R) {
        int l = R==1?t:a, r = m, s = R==1?a:t;
        while (l < (R==1?t+m-a:m) && r < b) {
            if (Reads.compareIndices(array, l, r, 1, true) <= 0) {
                Writes.swap(array, l++, s++, 0, true, false);
            } else {
                Writes.swap(array, r++, s++, 0, true, false);
            }
        }
        while (l < (R==1?t+m-a:m))
            Writes.swap(array, l++, s++, 1, true, false);
        if (R == 0) while (r < b)
            Writes.swap(array, r++, s++, 1, true, false);
        return s;
    }

    private void mergetail(int[] array, int t, int a, int m, int b, int R) {
        if (R == 0) {
            for (int i = m, j = t; i < b;)
                Writes.swap(array, i++, j++, 1, true, false);
        }
        int l = m - 1, r = t + (b - m) - 1;
        while (l >= a && r >= t) {
            if (Reads.compareIndices(array, l, r, 1, true) > 0) {
                Writes.swap(array, --b, l--, 0, true, false);
            } else {
                Writes.swap(array, --b, r--, 0, true, false);
            }
        }
        while (r >= t)
            Writes.swap(array, --b, r--, 1, true, false);
    }

    private void mergesort(int[] array, int a, int b, int t) {
        int minrun = 16;
        for (int i = a; i < b; i+=minrun) {
            heap(array, i, Math.min(i+minrun, b));
        }
        int j = minrun;
        for (; j <= (b - a) / 4; j *= 4) {
            for (int i = a; i + j < b; i += 4 * j) {
                mergelowrite(array, i, i+j, Math.min(i+2*j, b), t, 0);
                if (i+2*j>=b) {
                    for (int k = i; k < b; k++)
                        Writes.swap(array, k, t+k-i, 1, true, false);
                } else if (i+3*j>=b) {
                    mergelowrite(array, i, i+2*j, b, t, 1);
                } else {
                    mergelowrite(array, i+2*j, i+3*j, Math.min(i+4*j, b), i, 0);
                    mergetail(array, t, i, i+Math.min(2*j, (b-i)-2*j), Math.min(i+4*j, b), 1);
                }
            }
        }
        for (; j < b - a; j *= 2) {
            for (int i = a; i + j < b; i += 2 * j) {
                mergetail(array, t, i, i+j, Math.min(i+2*j, b), 0);
            }
        }
    }

    private int binsearch(int[] array, int a, int b, int k) {
        while (a < b) {
            int m = a + (b - a) / 2;
            if (Reads.compareIndices(array, m, k, 1, true) < 0) {
                a = m + 1;
            } else {
                b = m;
            }
        }
        return a;
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    private void ecta(int[] array, int a, int m, int b, int s, int t) {
        int l = a, r = m, lb = a, rb = m;

        for (int i = 0; i < 2 * s; i++) {
            if (Reads.compareIndices(array, l, r, 1, true) <= 0) {
                Writes.swap(array, l++, t+i, 0, true, false);
            } else {
                Writes.swap(array, r++, t+i, 0, true, false);
            }
        }
        while (l < m || r < b) {
            while (l-lb <= r-rb && (l < m || r < b)) {
                for (int i = 0; i < s; i++) {
                    if (l < m && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                        Writes.swap(array, l++, rb++, 0, true, false);
                    } else {
                        Writes.swap(array, r++, rb++, 0, true, false);
                    }
                }
            }
            while (l-lb >= r-rb && (l < m || r < b)) {
                for (int i = 0; i < s; i++) {
                    if (l < m && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                        Writes.swap(array, l++, lb++, 0, true, false);
                    } else {
                        Writes.swap(array, r++, lb++, 0, true, false);
                    }
                }
            }
        }
        while (lb < m) {
            multiSwap(array, lb, t, s);
            lb += s; t += s;
        }
        while (rb < b) {
            multiSwap(array, rb, t, s);
            rb += s; t += s;
        }

        for (int i = a; i < b - s; i += s) {
            int d = i;
            for (int j = i + s; j < b; j += s) {
                int c = Reads.compareIndices(array, d, j, 0.1, true);
                if (c > 0 || (c == 0 && Reads.compareIndices(array, d+s-1, j+s-1, 0.1, true) > 0)) {
                    d = j;
                }
            }
            multiSwap(array, d, i, s);
        }
    }

    private void supercritical(int[] array, int a, int m, int b, int s) {
        int lc = 0, rc = 0, lb = a, re = m - s, rb = re, l = a, r = m;
        while (l < re || r < b) {
            for (; lb < l && (l < re || r < b);) {
                if (l < re && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                    Writes.swap(array, l++, lb++, 1, true, false);
                } else {
                    Writes.swap(array, r++, lb++, 1, true, false);
                }
                lc = (lc + 1) % s;
            }
            multiSwap(array, rb, lb-=lc, rc=lc);
            lc = 0; rb += rc;
            for (; rb < r && (l < re || r < b);) {
                if (l < re && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                    Writes.swap(array, l++, rb++, 1, true, false);
                } else {
                    Writes.swap(array, r++, rb++, 1, true, false);
                }
                rc = (rc + 1) % s;
            }
            multiSwap(array, lb, rb-=rc, lc=rc);
            rc = 0; lb += lc;
        }

        if (l-lb>r-rb) {
            multiSwap(array, lb, rb-s, s);
        }

        for (int i = a; i < b - 2 * s; i += s) {
            int d = i;
            for (int j = i + s; j < b - s; j += s) {
                int c = Reads.compareIndices(array, d, j, 0.1, true);
                if (c > 0 || (c == 0 && Reads.compareIndices(array, d+s-1, j+s-1, 0.1, true) > 0)) {
                    d = j;
                }
            }
            multiSwap(array, d, i, s);
        }
    }

    private void sort(int[] array, int a, int b) {
        int c = a+(b-a)/3, d = b-(b-a)/3;
        mergesort(array, c, d, d);
        mergesort(array, a, c, d);
        mergetail(array, a, c, d, b, 1);

        int l = a, r = c;
        while (l < r) {
            int p = partition(array, l, r, 0);
            if (r - l > 1)
                // Remove once block merge is implemented
                while (p-l > 8 && r-p < 2*Math.sqrt(r-l)) p = partition(array, l, p, 0);
            if (p==a||p==b-1)
                p = partition(array, l, r, 1);
            if (p - l < 4 || r - p < 2) { // equal, small enough left chunk, or too small right chunk
                if (p >= 0) {
                    while (r - l > 32) {
                        heap(array, l, l+(r-l)/2);
                        mergelowrite(array, r-(r-l)/2, r, b, l, 1);
                        r -= (r-l)/2;
                    }
                       heap(array, l, r);
                }
                while (l < r) {
                    int s = binsearch(array, r, b, l);
                    IndexedRotations.cycleReverse(array, l, r, s, 1, true, false);
                    l = s - (r - l); r = s;
                    do {
                        l++;
                    } while (l < r && Reads.compareIndices(array, l, r, 1, true) <= 0);
                }
                return;
            }

            int segs = (p - l - 1) / (r - p) + 1, rs = r - p;
            if (segs == 1) {
                heap(array, l, p);
                int m = mergelowrite(array, r-(p-l), r, b, l, 1);
                for (int i = l, j = r-(p-l); j < m;)
                    Writes.swap(array, i++, j++, 1, true, false);
                l = m - (r - p); r = m;
            } else if (segs == 2) {
                heap(array, l, l+rs);
                heap(array, l+rs, p);
                int M = mergelowrite(array, r-rs, r, b, l, 1);
                int m = l+rs, n = p, o = r-rs;
                while (m < n) {
                    if (m == l) {
                        for (int k = o, j = n; j > m;)
                            Writes.swap(array, --k, --j, 1, true, false);
                        m = o - (n - m);
                        n = o;
                    }
                    if (Reads.compareIndices(array, m, o, 1, true) <= 0) {
                        Writes.swap(array, m++, l++, 1, true, false);
                    } else {
                        Writes.swap(array, o++, l++, 1, true, false);
                    }
                }
                while (l + rs < Math.max(o, M)) {
                    Writes.swap(array, l, l++ + rs, 1, true, false);
                }
                r = l + rs;
            } else {
                int sr = rs & ~1;
                int v = ((p-l)%sr);
                heap(array, p-v, p);
                for (int i = l; i < p-v; i += 2 * sr) {
                    heap(array, i, Math.min(i+2*sr, p-v));
                }
                for (int j = 2 * sr; j < p-l-v; j *= 2) {
                    for (int i = l; i+j < p-v; i += 2 * j) {
                        ecta(array, i, i+j, Math.min(i+2*j, p-v), sr/2, r-rs);
                    }
                }

                int sl = (v > 0 ? Math.max(binsearch(array, r, b, l+v-1), binsearch(array, r, b, p-1)) : binsearch(array, r, b, p-1)) - (rs - sr);
                int slc = p + ((sl - p - 1) / sr + 1) * sr;

                IndexedRotations.cycleReverse(array, l, p-v, p, 1, true, false);
                if (sr < rs) {
                    IndexedRotations.holyGriesMills(array, r-1, r, slc+1, 1, true, false);
                }
                supercritical(array, l+v, p+sr, slc, sr);
                multiSwap(array, slc-sr, l, v);
                mergelowrite(array, l, l+v, slc-sr, slc-sr, 1);
                if (sl < slc) // I need a way to push this back to the search area when it's done, this doesn't work as well as it should
                    IndexedRotations.cycleReverse(array, sl-rs, slc-sr, slc+(rs-sr), 1, true, false);
                l = sl - rs; r = sl;
            }
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);
    }
}