package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;



final public class RoughSort extends Sort {
    public RoughSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rough");
        this.setRunAllSortsName("Rough Sort");
        this.setRunSortName("Roughsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int sym(int j, int o) {
        j &= 0x3FFF;
        int z = 1, h = 1;
        while(h < j) {
            z++;
            if((z & (z << o)) == 0)
                h++;
        }
        return z;
    }
    private int findConverge(int[] array, int offset, int index, int length, int c) {
        int m = (~index >>> 1), l = index, z=0;
        while(m != l) {
            int n = m % length;
            if(n != index && (n > index ^ Reads.compareIndices(array, n+offset, index+offset, 0.1, true) < 0)) {
                Writes.swap(array, n+offset, index+offset, 0.1, true, false);
                z++;
            }
            l = m;
            m = ~m >>> c;
        }
        return z;
    }
    private int findSym(int[] array, int offset, int index, int length, int c) {
        int m = (~index >>> c), l = index, z=0;
        while(m > l) {
            int n = m % length;
            if(n != index && (n > index ^ Reads.compareIndices(array, n+offset, index+offset, 0.1, true) < 0)) {
                Writes.swap(array, n+offset, index+offset, 0.1, true, false);
                z++;
            }
            l = m;
            m = sym(m, (c>>3)+2) - 1;
        }
        return z;
    }
    private int sift(int[] array, int start, int end) {
        int l = 1, z=0;
        while(1<<l < end-start)
            l++;
        for(int i=start; i<end; i++) {
            for(int k=1; k<l; k++) {
                int a, b, c;
                if((c = findConverge(array, start, i-start, end-start, k)) == 0) {
                    for(int j=start; j<i; j++) {
                        if((a = findSym(array, start, j-start, end-start, k)) == 0) {
                            if((b = findConverge(array, start, j-start, end-start, k)) == 0)
                                return z;
                        } else
                            b = findConverge(array, start, j-start, end-start, k);
                        z += a + b;
                    }
                    break;
                }
                z += c;
            }
        }
        return z;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int k=0;
        for(int j=0; j<length; j++) {
            for(int i=0; i<length; i++) {
                k+=sift(array, 0, length-i);
                Writes.swap(array, 0, length-i-1, 1, true, false);
            }
            if(k==0)
                break;
            k=0;
        }
        SmoothSort s = new SmoothSort(arrayVisualizer);
        s.smoothSort(array, 0, length-1, true);
    }
}