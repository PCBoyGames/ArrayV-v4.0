package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

public class ItRotMerge extends Sort {
    public ItRotMerge(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Iterative Rotate Merge");
        this.setRunAllSortsName("Iterative Rotate Merge Sort");
        this.setRunSortName("Iterative Rotate Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void itRotMerSort(int[] array, int currentLength){
        int l = 0;
        while (1 << l <= currentLength) l++;
        for (int u = l; u >= 0; u--)
            for (int d = u; d <= l; d++)
                for (int i = 0; i < 1<<d; i++)
                    rotatePartition(array, (i * currentLength)>>d, ((2 * i + 1) * currentLength) >> (d + 1), ((i + 1) * currentLength) >> d, u==d);
    }

    protected void rotatePartition(int[] array, int l, int p, int r, boolean top){
        if (l == p || p == r || (top && pair(array, p-1, p) >= 0)) return;
        int m = (top)?p:findSplit(array,l,r);
        if (m == r) return;
        if (p + m > r + l) l = p + m - r - 1;
        else l--;
        r = Math.min(p,m);
        while (r-l>1){
            if (pair(array, p+m-1-(l+r)>>1, (l+r)>>1) == 1) r = (l+r)>>1;
            else l = (l+r)>>1;
        }
        IndexedRotations.adaptable(array, r, m, p + m - r, 0.5, true, false);
    }

    protected int findSplit(int[] array, int l, int r){
        if (r - l > 3 && pair(array, r - (int) Math.floor((r - l) / 3),l + (int) Math.floor((r - l) / 3)) == 1){
            l+=(int) Math.floor((r - l) / 3);
            r-=(int) Math.floor((r - l) / 3);
            while (r-l > 1){
                if (pair(array, (r + l) >> 1, l) == 1) r = (r + l)>>1;
                else l = (r + l) >> 1;
            }
            return r;
        } else {
            l++;
            while (l < r && pair(array, l, l-1) < 1) l++;
            return l;
        }
    }

    protected int pair(int[] array, int left, int right) {
        return -1 * Reads.compareIndices(array, left, right, 0.5, true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        itRotMerSort(array, currentLength);
    }
}