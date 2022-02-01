package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.exchange.OptimizedStoogeSortStudio;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.GrailSorting;

final public class SodiumSort extends GrailSorting {
    public SodiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sodium");
        this.setRunAllSortsName("Sodium Sort");
        this.setRunSortName("Sodiumsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int threshold;
    private OptimizedStoogeSortStudio stg;
    private BinaryInsertionSort bruh;

    private int logSqr(int j) {
        int log = (int) (Math.log(j) / Math.log(5));
        return log*log;
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) {
            Writes.swap(array, a+s, b+s, 1, true, false);
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
                    Writes.swap(array, t++, l++, 0.5, true, false);
                } else {
                    Writes.swap(array, t++, r++, 0.5, true, false);
                }
            }
            while(l < le) {
                Writes.swap(array, t++, l++, 0.5, true, false);
            }
            return r==m;
        } else {
            this.multiSwap(array, q, m, e-m);
            int l = m-1, r = q+e-m-1, t=e-1;
            while(l >= s && r >= q) {
                if(Reads.compareValues(array[l], array[r]) > 0) {
                    Writes.swap(array, t--, l--, 0.5, true, false);
                } else {
                    Writes.swap(array, t--, r--, 0.5, true, false);
                }
            }
            while(r >= q) {
                Writes.swap(array, t--, r--, 0.5, true, false);
            }
            return l==m-1;
        }
    }

    public void siftDown(int[] array, int root, int dist, int start, double sleep) {
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compareValues(array[start + leaf - 1], array[start + leaf]) == -1) {
                leaf++;
            }
            Highlights.markArray(1, start + root - 1);
            Highlights.markArray(2, start + leaf - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(array[start + root - 1], array[start + leaf - 1]) == -1) {
                Writes.swap(array, start + root - 1, start + leaf - 1, 0, true, false);
                root = leaf;
            }
            else break;
        }
    }

    public void heapify(int[] arr, int low, int high, double sleep) {
        int length = high - low;
        for (int i = length / 2; i >= 1; i--) {
            siftDown(arr, i, length, low, sleep);
        }
    }

    private void heapDownToMid(int[] array, int start, int end) {
        this.heapify(array, start, end, 0.125);

        for(int i=end-1; i>start+(end-start)/2; i--) {
            Writes.swap(array, i, start, 1, true, false);
            this.siftDown(array, 1, i-start-1, start, 0.5);
        }
    }

    private void stoogeOpt(int[] array, int start, int end) {
        if(end-start > 1)
            this.stg.stoogeSort(array, start, start+1, end+1, false);
        else if(end-start == 1)
            if(Reads.compareValues(array[start], array[end]) == 1)
                Writes.swap(array, start, end, 1, true, false);
    }

    private boolean stoogeblock(int[] array, int start, int end, int buff, int blocksize) {
        if(end-start <= 2*blocksize) {
            return !merge(array, buff, start, start+blocksize, end);
        } else if(end-start >= 3*blocksize) {
            int third = ((end-start + 1) / (3 * blocksize)) * blocksize;
            boolean left = this.stoogeblock(array, start, end-third, buff, blocksize),
                    right = this.stoogeblock(array, start+third, end, buff, blocksize);
            return left || right;
        } else {
            this.bruh.customBinaryInsert(array, start, end+1, 0.25);
            return true;
        }
    }

    private void sodStooge(int[] array, int start, int end) {
        int wantedKeys = logSqr(end-start);
        if(wantedKeys < 2) {
            this.stoogeOpt(array, start, end);
        } else {
            int foundKeys = this.grailFindKeys(array, start, end-start+1, wantedKeys);
            if(foundKeys < 2) {
                this.stoogeOpt(array, start, end);
                return;
            }
            for(int i=start+foundKeys; i<end; i+=foundKeys) {
                this.sodStooge(array, i, Math.min(i+foundKeys, end));
            }
            int len = Math.floorDiv(end-start, foundKeys) * foundKeys;
            this.stoogeblock(array, start+foundKeys, start+len, start, foundKeys);
            this.merge(array, start, start+foundKeys, start+len, end);
            this.bruh.customBinaryInsert(array, start, start+foundKeys, 0.25);
            this.grailMergeWithoutBuffer(array, start, foundKeys, end-start-foundKeys+1);
        }
    }

    private void sodiumSort(int[] array, int start, int end) {
        if(end-start <= threshold) {
            this.sodStooge(array, start, end);
            return;
        }
        int medianSize = (int) Math.pow(end-start, 0.4d);
        this.heapDownToMid(array, start, start + medianSize);

        int piv = array[start], p = start, r = end;
        while(p <= r) {
            while(Reads.compareValues(array[p], piv) < 0) {
                 p++;
                 Highlights.markArray(1, p);
                Delays.sleep(0.25);
            }
            while(Reads.compareValues(array[r], piv) > 0) {
                r--;
                Highlights.markArray(2, r);
                Delays.sleep(0.25);
            }
            if(p <= r) {
                Writes.swap(array, p++, r--, 0.5, true, false);
            }
        }
        if(p > start) this.sodiumSort(array, p, end);
        if(r < end) this.sodiumSort(array, start, r);
    }
    public void initSodium(int[] array, int start, int end) {
        this.threshold = (int) Math.pow(end-start, 0.67d);
        this.stg = new OptimizedStoogeSortStudio(arrayVisualizer);
        this.bruh = new BinaryInsertionSort(arrayVisualizer);
        this.sodiumSort(array, start, end-1);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.initSodium(array, 0, length);
    }
}