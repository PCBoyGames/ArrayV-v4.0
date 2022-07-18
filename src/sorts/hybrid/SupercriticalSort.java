package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SupercriticalSort extends Sort {
    public SupercriticalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Supercritical");
        this.setRunAllSortsName("Supercritical Sort");
        this.setRunSortName("Supercritical Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int sqrt(int v) {
        int l=1;
        while (l*l<v) l*=2;
        return l;
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    // code stolen from Distay's Stable
    private void tailMerge(int[] array, int[] tmp, int o, int a, int m, int b, int routine, boolean aux) {
        if (routine == 3) {
            int l = m, M;
            while (l < b) {
                M = l + (b - l) / 2;
                if (Reads.compareIndices(array, M, m-1, 1, true) < 0) {
                    l = M + 1;
                } else {
                    b = M;
                }
            }
        }
        if (routine < 1 || routine > 2) {
            Writes.arraycopy(array, m, tmp, o, b-m, 1, true, !aux);
        }
        int l = m-1, r = o+(b-m)-1;
        while (l >= a && r >= o) {
            Highlights.markArray(2, --b);
            if (Reads.compareIndexValue(array, l, tmp[r], 1, true) > (routine == 1 ? -1 : 0))
                Writes.write(array, b, array[l--], 0, true, aux);
            else
                Writes.write(array, b, tmp[r--], 0, true, aux);
        }
        while (r >= o)
            Writes.write(array, --b, tmp[r--], 1, true, aux);
        Highlights.clearMark(2);
    }
    private void merge(int[] array, int[] tmp, int a, int m, int b, int t, boolean aux) {
        int l = a, r = m;
        while (l < m && r < b) {
            if (!aux)
                Highlights.markArray(3, t);
            if (Reads.compareIndices(array, l, r, 1, true) <= 0)
                Writes.write(tmp, t++, array[l++], 0, true, aux);
            else
                Writes.write(tmp, t++, array[r++], 0, true, aux);
        }
        while (l < m)
            Writes.write(tmp, t++, array[l++], 1, true, aux);
        while (r < b)
            Writes.write(tmp, t++, array[r++], 1, true, aux);
        Highlights.clearMark(2);
        Highlights.clearMark(3);
    }
    private void headMerge(int[] array, int[] tmp, int l, int a, int m, int b, boolean aux) {
        int le=l+m-a, r = m;
        while (l < le && r < b) {
            Highlights.markArray(2, r);
            Highlights.markArray(3, l);
            if (Reads.compareValueIndex(array, tmp[l], r, 1, true) <= 0) {
                Writes.write(array, a++, tmp[l++], 0, true, aux);
            } else {
                Writes.write(array, a++, array[r++], 0, true, aux);
            }
        }
        while (l < le)
            Writes.write(array, a++, tmp[l++], 1, true, aux);
        Highlights.clearMark(2);
        Highlights.clearMark(3);
    }
    private void mergeRoutine(int[] array, int[] tmp, int o, int a, int b, int g, boolean aux) {
        int k = 0;
        if (b - a > 16) {
            k =      (           Reads.compareIndices(array, a+g-1,   a+g,   10, true) <= 0 ? 1 : 0) |
                     (a+3*g<b && Reads.compareIndices(array, a+3*g-1, a+3*g, 10, true) <= 0 ? 4 : 0);
               if (k > 0 || a+3*g>=b)
                   k |= (a+2*g<b && Reads.compareIndices(array, a+2*g-1, a+2*g, 10, true) <= 0 ? 2 : 0);
        }
        switch(k) {
            case 2:
                if (a+3*g >= b) {
                    Writes.arraycopy(array, a, tmp, o, g, 1, true, !aux);
                    headMerge(array, tmp, o, a, a+g, b, aux);
                    break;
                }
            case 0:
                if (a+2*g >= b)
                    tailMerge(array, tmp, o, a, a+g, b, 0, aux);
                else
                    merge(array, tmp, a, a+g, Math.min(a+2*g, b), o, !aux);
                if (a+3*g < b) {
                    merge(array, array, a+2*g, a+3*g, b, a, aux);
                    tailMerge(array, tmp, o, a, a+(b-a-2*g), b, 1, aux);
                } else if (a+2*g < b) {
                    headMerge(array, tmp, o, a, a+2*g, b, aux);
                }
                break;
            case 1:
                if (a+3*g < b) {
                    merge(array, tmp, a+2*g, a+3*g, b, o, !aux);
                    tailMerge(array, tmp, o, a, a+2*g, b, 2, aux);
                } else if (a+2*g < b) {
                    tailMerge(array, tmp, o, a, a+2*g, b, 0, aux);
                }
                break;
            case 3:
                if (a+3*g < b)
                    tailMerge(array, tmp, o, a, a+3*g, b, 3, aux);
                break;
            case 4:
                merge(array, tmp, a, a+g, Math.min(a+2*g, b), o, !aux);
                headMerge(array, tmp, o, a, a+2*g, b, aux);
                break;
            case 5:
                int l = a, r = a + 2 * g, M;
                while (l < r) {
                    M = l + (r - l) / 2;
                    if (Reads.compareIndices(array, a + 2 * g, M, 1, true) < 0) {
                        r = M;
                    } else {
                        l = M + 1;
                    }
                }
                if ((a+2*g)-l >= b-(a+2*g)) {
                    tailMerge(array, tmp, o, l, a+2*g, b, 3, aux);
                } else {
                    Writes.arraycopy(array, l, tmp, o, (2*g)-(l-a), 1, true, !aux);
                    headMerge(array, tmp, o, l, a+2*g, b, aux);
                }
                break;
            case 6:
                int lf = a, rf = a + g, MF;
                while (lf < rf) {
                    MF = lf + (rf - lf) / 2;
                    if (Reads.compareIndices(array, a + g, MF, 1, true) < 0) {
                        rf = MF;
                    } else {
                        lf = MF + 1;
                    }
                }
                Writes.arraycopy(array, lf, tmp, o, g-(lf-a), 1, true, !aux);
                headMerge(array, tmp, o, lf, a+g, b, aux);
                break;
        }
    }
    public void disStable(int[] array, int[] tmp, int o, int a, int b, boolean aux) {
        int i=1;
        for (; i<=(b-a)/4; i*=4) {
            for (int j=a; j+i<b; j+=4*i) {
                mergeRoutine(array, tmp, o, j, Math.min(j+4*i, b), i, aux);
            }
        }
        while (i<b-a) {
            for (int j=a; j+i<b; j+=2*i)
                tailMerge(array, tmp, o, j, j+i, Math.min(j+2*i, b), 3, aux);
            i*=2;
        }
    }
    private void blockMergeFW(int[] array, int[] idx, int s, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareIndices(array, m-1, m, 1, true) <= 0) {
            Writes.arraycopy(array, a, array, a-s, b-a, 1, true, false);
            return;
        }
        int l = a, r = m, lb = a-s, rb = m, lt = a-s, rt = m, ltc = 0, rtc = (m-lb)/s, c = 0, lc = 0, rc = 0;
        do {
            for (; lb < l && (l < m || r < b);) {
                if (l < m && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                    Writes.write(array, lb++, array[l++], 1, true, false);
                } else {
                    Writes.write(array, lb++, array[r++], 1, true, false);
                }
                if (++lc == s) {
                    Writes.write(idx, ltc++, c++, 1, true, true);
                    lt += s;
                    lc = 0;
                }
            }
            Writes.arraycopy(array, lb = lt, array, rb, lc, 1, true, false);
            rb += rc = lc; lc = 0;
            for (; rb < r && (l < m || r < b);) {
                if (l < m && (r >= b || Reads.compareIndices(array, l, r, 1, true) <= 0)) {
                    Writes.write(array, rb++, array[l++], 1, true, false);
                } else {
                    Writes.write(array, rb++, array[r++], 1, true, false);
                }
                if (++rc == s) {
                    Writes.write(idx, rtc++, c++, 1, true, true);
                    rt += s;
                    rc = 0;
                }
            }
            Writes.arraycopy(array, rb = rt, array, lb, rc, 1, true, false);
            lb += lc = rc; rc = 0;
        } while (l < m || r < b);
        if (l - lt > r - rt) {
            Writes.arraycopy(array, rb = rt - s, array, lt, s, 1, true, false);
            Writes.write(idx, ltc, idx[rtc-1], 1, true, true);
            rt = lt; lt += s;
        }
        for (int i=0; i<c; i++) {
            int n = idx[i];
            while (i != n) {
                int t=idx[n];
                multiSwap(array, a+(i-1)*s, a+(n-1)*s, s);
                Writes.write(idx, n, n, 1, true, false);
                n = t;
            }
        }
        Highlights.clearMark(2);
    }
    private void blockMergeBW(int[] array, int[] idx, int s, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareIndices(array, m-1, m, 1, true) <= 0) {
            Writes.arraycopy(array, a, array, a+s, b-a, 1, true, false);
            return;
        }
        int l = m - 1, r = b - 1, lb = l, rb = r + s, lt = l, rt = rb, ltc = (m-a)/s-1, pc = 0, rtc = (b-a)/s, tc = 0, c = rtc, lc = 0, rc = 0;
        do {
            for (; r < rb && (l >= a || r >= m);) {
                if (l >= a && (r < m || Reads.compareIndices(array, l, r, 1, true) > 0))
                    Writes.write(array, rb--, array[l--], 1, true, false);
                else
                    Writes.write(array, rb--, array[r--], 1, true, false);
                if (++rc == s) {
                    Writes.write(idx, --rtc, --c, 1, true, true);
                    tc++;
                    rt -= s;
                    rc = 0;
                }
            }
            Writes.arraycopy(array, rb+1, array, lb-rc+1, lc=rc, 1, true, false);
            lb -= lc; rb = rt; rc = 0;

            for (; l < lb && (l >= a || r >= m);) {
                if (l >= a && (r < m || Reads.compareIndices(array, l, r, 1, true) > 0))
                    Writes.write(array, lb--, array[l--], 1, true, false);
                else
                    Writes.write(array, lb--, array[r--], 1, true, false);
                if (++lc == s) {
                    pc=--c;
                    if (ltc > 0) Writes.write(idx, --ltc, c, 1, true, true);
                    tc++;
                    lt -= s;
                    lc = 0;
                }
            }
            Writes.arraycopy(array, lb+1, array, rb-lc+1, rc=lc, 1, true, false);
            rb -= rc; lb = lt; lc = 0;
        } while (l >= a || r >= m);
        if (lt - l <= rt - r) {
            Writes.arraycopy(array, lt+1, array, rt-s+1, s, 1, true, false);
            Writes.write(idx, rtc-1, pc, 1, true, true);
        }
        for (int i=0; i<tc; i++) {
            int n = idx[i];
            while (i != n) {
                int t=idx[n];
                multiSwap(array, a+(i+1)*s, a+(n+1)*s, s);
                Writes.write(idx, n, n, 1, true, false);
                n = t;
            }
        }
        Highlights.clearMark(2);
    }
    public void supercritical(int[] array, int a, int c) {
        int s=sqrt(c-a), b=c-(c%s), i, buf[] = Writes.createExternalArray(s), idx[] = Writes.createExternalArray((b-a)/s);
        a+=s;
        for (i=1; i<=s/2; i*=4) {
            for (int j=a; j+i<b; j+=4*i)
                mergeRoutine(array, buf, 0, j, Math.min(j+4*i, b), i, false);
        }
        for (; i<=s; i*=2)
            for (int j=a; j+i<b; j+=2*i)
                tailMerge(array, buf, 0, j, j+i, Math.min(j+2*i, b), 0, false);
        Writes.arraycopy(array, a-s, buf, 0, s, 1, true, false);
        boolean fw = true;
        int m = 0;
        for (; i<b-a; i*=2) {
            int j;
            if (fw) {
                for (j=a; j+i<b; j+=2*i) {
                    m = j;
                    blockMergeFW(array, idx, s, j, j+i, Math.min(j+2*i, b));
                }
                if (j<b) {
                    m = j;
                    Writes.arraycopy(array, j, array, j-s, b-j, 1, true, false);
                }
            } else {
                for (j=b; m>a; j=m+i) {
                    blockMergeBW(array, idx, s, Math.max(m-i, a)-s, Math.max(m, a)-s, j-s);
                    m -= 2 * i;
                }
                if (j>a) {
                    Writes.arraycopy(array, j-s, array, j, j-a+s, 1, true, false);
                }
            }
            fw=!fw;
        }
        if (fw) {
            disStable(buf, array, a-s, 0, s, true);
            headMerge(array, buf, 0, a-s, a, b, false);
        } else {
            disStable(buf, array, b-s, 0, s, true);
            tailMerge(array, buf, 0, a-s, b-s, b, 1, false);
        }
        if (b < c) {
            disStable(array, buf, 0, b, c, false);
            tailMerge(array, buf, 0, a-s, b, c, 3, false);
        }
        Writes.deleteExternalArrays(buf, idx);
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        supercritical(array, 0, sortLength);
    }
}
