package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.GrailSorting;

public class FeruSort extends GrailSorting {
    public FeruSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Feru");
        this.setRunAllSortsName("Feru Sort");
        this.setRunSortName("Ferusort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private static final int fraction = 16;

    private OptimizedMergeSort initial;

    private boolean ceilLogEven(int v) {
        int l = 0;
        while(v > 0) {
            v /= 2; l++;
        }
        return l % 2 == 0;
    }
    private int bufS, bufE;
    private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) {
            Writes.swap(array, a++, b++, 1, true, false);
        }
    }
    private int quMerge(int[] array, int start, int mid, int end) {
        Writes.arraycopy(array, start, array, bufS, mid-start, 1, true, false);
        int l = bufS, r = mid, t = start;
        while(l < bufS+(mid-start) && r < end) {
            if(Reads.compareValues(array[l], array[r]) <= 0) {
                Writes.write(array, t++, array[l++], 1, true, false);
            } else {
                Writes.write(array, t++, array[r++], 1, true, false);
            }
        }
        int q = t;
        while(l < bufS+(mid-start)) {
            Writes.write(array, t++, array[l++], 1, true, false);
        }
        return q;
    }
    private void oopRotate(int[] array, int start, int mid, int end) {
        if(end-mid > mid-start) {
            Writes.arraycopy(array, start, array, bufS, mid-start, 1, true, false);
            Writes.arraycopy(array, mid, array, start, end-mid, 1, true, false);
            Writes.arraycopy(array, bufS, array, start+(end-mid), mid-start, 1, true, false);
        } else {
            Writes.arraycopy(array, mid, array, bufS, end-mid, 1, true, false);
            Writes.arraycopy(array, start, array, start+(end-mid), mid-start, 1, true, false);
            Writes.arraycopy(array, bufS, array, start, end-mid, 1, true, false);
        }
    }
    private int binSearch(int[] array, int start, int end, int key, boolean isLeft) {
        while(start < end) {
            int mi = start + (end - start) / 2,
                comp = Reads.compareValues(array[mi], key);
            if(comp == 1 || (isLeft && comp == 0)) {
                end = mi;
            } else {
                start = mi + 1;
            }
        }
        return start;
    }
    private void feruMerge(int[] array, int start, int mid, int end) {
        // TODO: <b><i>REFACTOR THIS SHIT</i></b>
        int blkSz = bufE-bufS;
        if(end-start <= blkSz) {
            int l = start, r = mid, t = bufS;
            while(l < mid && r < end) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.write(array, t++, array[l++], 1, true, false);
                } else {
                    Writes.write(array, t++, array[r++], 1, true, false);
                }
            }
            while(l < mid) {
                Writes.write(array, t++, array[l++], 1, true, false);
            }
            while(r < end) {
                Writes.write(array, t++, array[r++], 1, true, false);
            }
            Writes.arraycopy(array, bufS, array, start, end-start, 1, true, false);
        } else if(mid-start <= blkSz) {
            Writes.arraycopy(array, start, array, bufS, mid-start, 1, true, false);
            int l = bufS, r = mid, t = start;
            while(l < bufS+(mid-start) && r < end) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.write(array, t++, array[l++], 1, true, false);
                } else {
                    Writes.write(array, t++, array[r++], 1, true, false);
                }
            }
            while(l < bufS+(mid-start)) {
                Writes.write(array, t++, array[l++], 1, true, false);
            }
        } else if(end-mid <= bufE-bufS) {
            Writes.arraycopy(array, mid, array, bufS, end-mid, 1, true, false);
            int l = mid-1, r = bufS+(end-mid)-1, t = end-1;
            while(l >= start && r >= bufS) {
                if(Reads.compareValues(array[l], array[r]) > 0) {
                    Writes.write(array, t--, array[l--], 1, true, false);
                } else {
                    Writes.write(array, t--, array[r--], 1, true, false);
                }
            }
            while(r >= bufS) {
                Writes.write(array, t--, array[r--], 1, true, false);
            }
        } else {
            int leftoverL = (mid-start)%blkSz,
                leftoverR = (end-mid)%blkSz;
            if(leftoverL != 0) {
                this.oopRotate(array, mid-leftoverL, mid, end-leftoverR);
            }
            mid -= leftoverL;
            end -= leftoverR+leftoverL;
            if(end-start <= 2*blkSz) {
                this.feruMerge(array, start, mid, end);
            } else {
                int blks = (end-start)/blkSz;
                for(int i=0; i<blks-1; i++) {
                    int min=i;
                    for(int j=i+1; j<blks; j++) {
                        if(Reads.compareValues(array[start+min*blkSz], array[start+j*blkSz]) > 0) {
                            min=j;
                        }
                    }
                    int w = start+i*blkSz;
                    if(min != i) {
                        this.multiSwap(array, w, start+min*blkSz, blkSz);
                    }
                }
                for(int i=1, d=start; i<blks; i++) {
                    int w = start+i*blkSz;
                    d = this.quMerge(array, d, w, w+blkSz);
                }
            }
            if(leftoverR != 0 || leftoverL != 0) {
                this.feruMerge(array, end, end+leftoverL, end+leftoverL+leftoverR);
                int b = this.binSearch(array, start, end, array[end], true),
                    b2 = this.binSearch(array, end, end+leftoverL+leftoverR, array[end-1], false);
                this.feruMerge(array, b, end, b2);
            }
        }
    }
    private void feruHalfMerge(int[] array, int[] tmp, int a, int b, int t, int c, int d) {
        while(c < d && a < b) {
            if(Reads.compareValues(tmp[a], array[c]) <= 0) {
                Writes.write(array, t++, tmp[a++], 1, true, false);
            } else {
                Writes.write(array, t++, array[c++], 1, true, false);
            }
        }
        while(a < b) {
            Writes.write(array, t++, tmp[a++], 1, true, false);
        }
    }
    private void feruMergeSort(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        if(start == mid)
             return;
        this.feruMergeSort(array, start, mid);
        this.feruMergeSort(array, mid, end);
        this.feruMerge(array, start, mid, end);
    }
    public void feru(int[] array, int start, int end) {
        this.initial = new OptimizedMergeSort(arrayVisualizer);
        this.initial.inserter = new InsertionSort(arrayVisualizer);
        if(end-start < fraction) {
            this.initial.inserter.customInsertSort(array, start, end, 0.25, false);
            return;
        }
        int size = (end-start)/fraction;
        int[] tmp = Writes.createExternalArray(size);
        boolean cle = ceilLogEven(end-start);
        initial.mergeSort(array, tmp, start, start + size, cle);
        if(!cle) {
            Writes.arraycopy(array, start, tmp, 0, size, 0, false, true);
        }
        this.bufS = start;
        this.bufE = start+size;
        this.feruMergeSort(array, start+size, end);
        this.feruHalfMerge(array, tmp, 0, size, start, start+size, end);
        Writes.deleteExternalArray(tmp);
    }
    @Override
    public void runSort(int[] array, int length, int buckets) {
        this.feru(array, 0, length);
    }
}