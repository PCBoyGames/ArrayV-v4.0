package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BestForNSorting;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- FUNGAMER2'S SCRATCH VISUAL -
------------------------------

*/
final public class ZigZagSort extends BestForNSorting {
    public ZigZagSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Zig-Zag");
        this.setRunAllSortsName("Zig-Zag Sort");
        this.setRunSortName("Zig-Zag Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected int compare(int[] array, int l, int r) {
        return Reads.compareValues(array[l - 1], array[r - 1]);
    }
    
    protected void swap(int[] array, int l, int r) {
        Writes.swap(array, l - 1, r - 1, 0.5, true, false);
    }
    
    protected void compswap(int[] array, int l, int r, int dir) {
        Highlights.markArray(1, l - 1);
        Highlights.markArray(2, r - 1);
        if (compare(array, l, r) == dir) swap(array, l, r);
    }
    
    protected void attentuate(int[] array, int start, int mid, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start + 1 < 8) smallsort(array, start, end);
        else {
            halver(array, start, mid);
            halver(array, mid + 1, end);
            halver(array, (int) Math.floor((start + mid) / 2) + 1, (int) Math.floor((mid + end) / 2));
            Writes.recursion();
            attentuate(array, (int) Math.floor((start + mid) / 2) + 1, mid, (int) Math.floor((mid + end) / 2), depth + 1);
            halver(array, (int) Math.floor((start + mid) / 2) + 1, mid);
            halver(array, mid + 1, (int) Math.floor((mid + end) / 2));
            halver(array, (int) Math.floor(((Math.floor((start + mid) / 2) + 1) + mid) / 2) + 1, (int) Math.floor((Math.floor((mid + end) / 2) + mid) / 2));
            Writes.recursion();
            attentuate(array, (int) Math.floor(((Math.floor((start + mid) / 2) + 1) + mid) / 2) + 1, mid, (int) Math.floor((Math.floor((mid + end) / 2) + mid) / 2), depth + 1);
        }
    }
    
    protected void halver(int[] array, int start, int end) {
        if (start < end) {
            if (end - start == 1) compswap(array, start, end, 1);
            else {
                if (end - start == 2) {
                    compswap(array, start, end - 1, 1);
                    compswap(array, start, end, 1);
                } else {
                    if (end - start == 3) {
                        compswap(array, start, end - 1, 1);
                        compswap(array, start + 1, end, 1);
                        compswap(array, start, end, 1);
                        compswap(array, start + 1, end - 1, 1);
                    } else {
                        int k = start;
                        while (k + (int) Math.ceil((end - start) / 2) <= end) {
                            compswap(array, k, k + (int) Math.ceil((end - start) / 2), 1);
                            k++;
                        }
                        k = start + 1;
                        while (k + (int) Math.ceil((end - start) / 2) - 1 <= end) {
                            compswap(array, k, k + (int) Math.ceil((end - start) / 2) - 1, 1);
                            k++;
                        }
                        k = start;
                        while(k + (int) Math.ceil((end - start) / 2) + 1 <= end) {
                            compswap(array, k, k + (int) Math.ceil((end - start) / 2) - 1, 1);
                            k++;
                        }
                    }
                }
            }
        }
    }
    
    protected void reduce(int[] array, int start, int end) {
        if (end - start + 1 < 8) smallsort(array, start, end);
        else {
            halver(array, start, end);
            attentuate(array, start, (int) Math.floor((start + end) / 2), end, 0);
        }
    }
    
    protected void smallsort(int[] array, int start, int end) {
        if (start < end) initNetwork(array, start - 1, end - start + 1);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int size = currentLength;
        while (size >= 2) {
            int i = 1;
            while (i + size - 1 <= currentLength) {
                reduce(array, i, i + size - 1);
                i += size;
            }
            i = 1;
            while (i + size - 1 <= currentLength) {
                int j = i;
                while (j + (int) Math.ceil(size / 2) <= i + size - 1) {
                    swap(array, j, j + (int) Math.ceil(size / 2));
                    j++;
                }
                reduce(array, i, i + size - 1);
                i += (int) Math.floor(size / 2);
            }
            i = currentLength - size + 1;
            while (i >= 1) {
                int j = i;
                while (j + (int) Math.ceil(size / 2) <= i + size - 1) {
                    swap(array, j, j + (int) Math.ceil(size / 2));
                    j++;
                }
                reduce(array, i, i + size - 1);
                i -= (int) Math.floor(size / 2);
            }
            size = (int) Math.floor(size / 2);
        } 
    }
}