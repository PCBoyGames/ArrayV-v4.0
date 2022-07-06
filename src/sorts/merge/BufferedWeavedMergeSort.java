package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

public class BufferedWeavedMergeSort extends Sort {

    public BufferedWeavedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buffered Weaved Merge");
        this.setRunAllSortsName("Buffered Weaved Merge Sort");
        this.setRunSortName("Buffered Weaved Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void mergepp(int[] array, int buf, int a, int b, int g) {
        boolean A = false;
        int n = (b - a - 1) / g + 1;
        for(int i = 1; i < n; i *= 2) {
            A=!A;
            int f = A ? g : 1, t = A ? 1 : g, p = A ? a : buf, q = A ? buf : a;
            for(int j = 0; j < n; j += 2 * i) {
                int h = j, l = j, m = Math.min(j + i, n), r = m, e = Math.min(j + 2 * i, n);
                while(l < m && r < e) {
                    int c = p+l*f, d = p+r*f;
                    if(Reads.compareIndices(array, c, d, 1, true) <= 0) {
                        Writes.swap(array, c, q+t*h++, 1, true, false);
                        l++;
                    } else {
                        Writes.swap(array, d, q+t*h++, 1, true, false);
                        r++;
                    }
                }
                while(l < m)
                    Writes.swap(array, p+l++*f, q+t*h++, 1, true, false);
                while(r < e)
                    Writes.swap(array, p+r++*f, q+t*h++, 1, true, false);
            }
        }
        if(A)
            for(int i = 0; i < n; i++)
                Writes.swap(array, buf+i, a+i*g, 1, true, false);
    }

    private void weave(int[] array, int buf, int a, int b, int g) {
        int[] ptrs = new int[2*g], f = new int[g];
        for(int i = 0; i < g; i++) {
            ptrs[2*i] = a + i;
            ptrs[2*i+1] = a + g + i;
        }
        Writes.changeAllocAmount(3*g);
        int bsz = a - buf;
        while(buf < b - bsz) {
            for(int i = 0; i < g; i++) {
                boolean ldone = ptrs[2*i] >= b, rdone = ptrs[2*i+1] >= b;
                if(ldone && rdone) continue;
                int m = Math.min(ptrs[2*i], ptrs[2*i+1]), M = Math.max(ptrs[2*i], ptrs[2*i+1]);
                if(buf >= m && !(m == ptrs[2*i] ? rdone : ldone)) {
                    int q = Math.abs(f[i]) * g;
                    int d = M - g, e = d, fc = 0;
                    while(e >= m + q) {
                        Writes.swap(array, d, e, 1, true, false);
                        e -= 2 * g;
                        d -= g;
                        fc++;
                    }
                    if(f[i] == 0) {
                        boolean p = (ptrs[2*i]+(ldone?g:0)) < (ptrs[2*i+1]+(rdone?g:0));
                        Writes.write(ptrs, p ? 2*i : 2*i+1, d + g, 1, true, true);
                        Writes.write(f, i, fc * (p ? -1 : 1), 1, true, true);
                    } else {
                        int k = d;
                        for(int j = m + q; j > m; k -= g) { // todo: cycle reverse for gaps
                            Writes.swap(array, j -= g, k, 1, true, false);
                        }
                        Writes.write(f, i, f[i] < 0 ? f[i] - fc : f[i] + fc, 1, true, true);
                        Writes.write(ptrs, f[i] < 0 ? 2*i : 2*i+1, k + g, 1, true, true);
                    }
                }

                if(!ldone && (rdone || Reads.compareIndices(array, ptrs[2*i], ptrs[2*i+1], 1, true) <= 0)) {
                    Writes.swap(array, buf, ptrs[2*i], 1, true, false);
                    if(f[i] < 0) {
                        ptrs[2*i] += (-1 / f[i] + 1) * g;
                        Writes.write(f, i, f[i] + 1, 1, true, true);
                    } else {
                        ptrs[2*i] += 2 * g;
                    }
                } else {
                    Writes.swap(array, buf, ptrs[2*i+1], 1, true, false);
                    if(f[i] > 0) {
                        ptrs[2*i+1] += (1 / f[i] + 1) * g;
                        Writes.write(f, i, f[i] - 1, 1, true, true);
                    } else {
                        ptrs[2*i+1] += 2 * g;
                    }
                }
                buf++;
            }
        }
        Writes.changeAllocAmount(-3*g);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int s = 1;
        while(s*s < length) s *= 2;
        InPlaceMergeSortIV p = new InPlaceMergeSortIV(arrayVisualizer);
        if(length < 64) {
            p.IPM4(array, 0, length);
            return;
        }
        for(int i = s; i < 2 * s; i++) {
            mergepp(array, 0, i, length, s);
        }
        for(int i = s / 2; i > 0; i /= 2) {
            weave(array, 0, s, length, i);
            IndexedRotations.cycleReverse(array, 0, length-s, length, 1, true, false);
        }
        runSort(array, s, bucketCount);
        p.inPlaceMerge4(array, 0, s, length);
    }
}
