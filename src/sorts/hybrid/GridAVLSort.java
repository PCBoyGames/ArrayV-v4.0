package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class GridAVLSort extends Sort {
    public GridAVLSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gridded GAVL Tree");
        this.setRunAllSortsName("Gridded Gapped AVL Tree Sort");
        this.setRunSortName("Gridded Gapped AVL Treesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private SupercriticalSort sup;
    // private ImprovedOnlinePDMSort pdm;
    private int treeCap(int v) {
        int c = 31 - Integer.numberOfLeadingZeros(v);
        return ((c + 2) << c) - 1;
    }
    private int p2(int v) {
        return 1 << (31 - Integer.numberOfLeadingZeros(v));
    }
    private class GridIGAVL {
        private int g, k, e, tree[], size[], mainswap[], opos;
        public GridIGAVL(int[] array, int[] tree, int[] size, int out) {
            opos = out;
            mainswap = array;
            this.tree = tree;
            this.size = size;
            g = 2; k = 0;
            e = 0;
            Writes.write(tree, 0, array[out], 0.25, false, true);
        }
        private void exchout(int a, int b) {
            for(int i = a; i < b; i++) {
                if(size[i] > 0) {
                    sup.disStable(tree, mainswap, opos, g*i, g*i+size[i]+1, true);
                    //if(pdm.merge(tree, mainswap, opos, g*i, g*i+size[i]+1, -1, 0, true) < 0)
                    //    Writes.reversal(tree, g*i, g*i+size[i], 0.25, true, true);
                    Writes.write(size, i, ~size[i], 0.25, false, true);
                }
            }
            for(int i = a, j = opos; i < b; i++) {
                int s = size[i] < 0 ? ~size[i] : size[i];
                Writes.arraycopy(tree, g*i, mainswap, j, s+1, 0.1, true, false);
                for(int k=g*i, l=s; l-->=0; k++)
                    Writes.visualClear(tree, k);
                j += s+1;
            }
        }
        private void exchin(int a, int b) {
            for(int i = a, j = opos; i < b; i++) {
                int s = size[i] < 0 ? ~size[i] : size[i];
                Writes.arraycopy(mainswap, j, tree, g*i, s+1, 0.25, true, true);
                j += s + 1;
            }
        }
        private void dbl() {
            for(int i = 0; i <= k; i++) {
                if(size[i] > 0) {
                    sup.disStable(tree, mainswap, opos, g*i, g*i+size[i]+1, true);
                    //if(pdm.merge(tree, mainswap, opos, g*i, g*i+size[i]+1, -1, 0, true) < 0)
                    //    Writes.reversal(tree, g*i, g*i+size[i], 0.25, true, true);
                    Writes.write(size, i, ~size[i], 0.25, false, true);
                }
            }
            for(int i=(k+e)*(g+1), j=k; j>=0; j--) {
                int s = size[j];
                for(int l=s<0?~s:s; l>=0; l--) {
                    int p = tree[l+j*g];
                    Writes.visualClear(tree, l+j*g);
                    Writes.write(tree, i, p, 0.5, true, true);
                    i -= g + 1;
                }
            }
            for(int i = 0; i <= k; i++) {
                Writes.write(size, i, 0, 0.25, false, true);
            }
            k += e;
            g++; e = 0;
        }
        private int min(int a, int b) {
            int mg = size[a];
            if(mg < 0) mg = ~mg;
            for(int i = a + 1; i < b; i++) {
                int s = size[i] < 0 ? ~size[i] : size[i];
                mg = Math.min(mg, s);
            }
            return mg;
        }
        private void inc(int a, int b) {
            for(int i = a; i < b; i++) {
                if(size[i] < 0)
                    Writes.write(size, i, size[i] - 1, 1, true, true);
                else
                    Writes.write(size, i, size[i] + 1, 1, true, true);
            }
        }
        private void dec(int a, int b) {
            for(int i = a; i < b; i++) {
                if(size[i] < 0)
                    Writes.write(size, i, size[i] + 1, 1, true, true);
                else
                    Writes.write(size, i, size[i] - 1, 1, true, true);
            }
        }
        private void rebalance(int l) {
            for(int i = 0; i < g - 2; i++) {
                int p = 1 << i;
                l &= ~p;
                int m0 = min(l, l+p), m1 = min(l+p, l+2*p);
                if(m0 - m1 > 1) {
                    exchout(l, l+2*p);
                    dec(l, l|p);
                    inc(l|p, l+2*p);
                    exchin(l, l+2*p);
                } else if(m1 - m0 > 1) {
                    exchout(l, l+2*p);
                    inc(l, l|p);
                    dec(l|p, l+2*p);
                    exchin(l, l+2*p);
                } else if(m0 - m1 == 1 || m1 - m0 == 1) break;
            }
        }
        public void push(int val) {
            if(e>k) dbl();
            e++;
            int l = 0, m, r = k;
            while(l < r) {
                m = r - (r - l) / 2;
                if(Reads.compareIndexValue(tree, g*m, val, 0.05, true) <= 0) {
                    l = m;
                } else {
                    r = m - 1;
                }
            }
            int s = size[l] < 0 ? ~size[l] : size[l];
            Writes.write(tree, s+1+g*l, val, 0.5, true, true);
            Writes.write(size, l, s+1, 0, false, true);
            rebalance(l);
        }
    }
    public void gavl(int[] array, int a, int b) {
        int[] tree = Writes.createExternalArray(treeCap((b-a+1)/2));
        int[] size = Writes.createExternalArray(p2((b-a+1)/2));
        sup = new SupercriticalSort(arrayVisualizer);
        //   pdm = new ImprovedOnlinePDMSort(arrayVisualizer);
        GridIGAVL t = new GridIGAVL(array, tree, size, a);
        for(int i = a + 1; i < b; i++)
            t.push(array[i]);
        t.exchout(0, t.k+1);
        Writes.deleteExternalArrays(tree, size);
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        gavl(array, 0, sortLength);
    }
}