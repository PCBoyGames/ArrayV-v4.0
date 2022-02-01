package sorts.hybrid;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;

final public class CaesiumSort extends GrailSorting {
    public CaesiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Caesium");
        this.setRunAllSortsName("Caesium Sort");
        this.setRunSortName("Caesiumsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int logCub(int j) {
        double log = Math.log10(j);
        return (int) (log*log*log);
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) {
            Writes.swap(array, a+s, b+s, 0.05, true, false);
        }
    }

    private boolean merge(int[] array, int q, int s, int m, int e) {
        if(s >= m || m >= e)
            return false;
        if(e-m > m-s) {
            this.multiSwap(array, q, s, m-s);
            int l = q, le = q+m-s, r = m, t = s;
            while(l < le && r < e) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, t++, l++, 0.05, true, false);
                } else {
                    Writes.swap(array, t++, r++, 0.05, true, false);
                }
            }
            while(l < le) {
                Writes.swap(array, t++, l++, 0.05, true, false);
            }
            return r==m;
        } else {
            this.multiSwap(array, q, m, e-m);
            int l = m-1, r = q+e-m-1, t=e-1;
            while(l >= s && r >= q) {
                if(Reads.compareValues(array[l], array[r]) > 0) {
                    Writes.swap(array, t--, l--, 0.05, true, false);
                } else {
                    Writes.swap(array, t--, r--, 0.05, true, false);
                }
            }
            while(r >= q) {
                Writes.swap(array, t--, r--, 0.05, true, false);
            }
            return l==m-1;
        }
    }

    private boolean halfstooge(int[] array, int start, int end) {
        if(end-start == 1) {
            boolean change = Reads.compareIndices(array, start, end, 0.01, true) == 1;
            if(change)
                Writes.swap(array, start, end, 0.01, true, false);
            return change;
        } else if(end-start > 1) {
            int t = (end - start + 1) / 3;
            boolean l = halfstooge(array, start, end-t),
                    r = halfstooge(array, start+t, end);
            return l||r;
        }
        return false;
    }

    private boolean stoogeblock(int[] array, int bs, int be, int buff, ArrayList<Integer> boundaries) {
        if(be-bs == 2) {
            return !merge(array, buff, boundaries.get(bs), boundaries.get(bs+1), boundaries.get(be));
        } else if(be-bs > 2) {
            int third = ((be-bs) / 3);
            boolean left = this.stoogeblock(array, bs, be-third, buff, boundaries),
                    right = this.stoogeblock(array, bs+third, be, buff, boundaries);
            return left || right;
        }
        return false;
    }

    private void runCaesium(int[] array, int start, int end) {
        int size = logCub(end-start);
        ArrayList<Integer> sizes = new ArrayList<>();
        for(int i=start; i<end;) {
            int keys = this.grailFindKeys(array, i, end-i, size);
            if(i == start && keys < 4 && size >= 4) {
                this.grailLazyStableSort(array, start, end-start);
                return;
            }
            Writes.arrayListAdd(sizes, i);
            i += keys;
        }
        Writes.arrayListAdd(sizes, end);

        this.stoogeblock(array, 1, sizes.size()-1, start, sizes);
        Writes.arrayListClear(sizes);

        this.halfstooge(array, start, sizes.get(1)-1);
        this.grailMergeWithoutBuffer(array, start, sizes.get(1)-start, end-sizes.get(1));
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.runCaesium(array, 0, length);
    }
}