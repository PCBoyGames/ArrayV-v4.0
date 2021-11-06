package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/**
 * @author aphitorite
 * @author Kiriko-chan
 * @author yuji
 *
 */
public final class AdaptiveMedianMergeSort extends Sort {

    public AdaptiveMedianMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Median Merge");
        this.setRunAllSortsName("Adaptive Median Merge Sort");
        this.setRunSortName("Adaptive Median Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private InsertionSort insSort;
    
    private void medianOfThree(int[] array, int a, int b) {
        int m = a+(b-1-a)/2;
        
        if(Reads.compareIndices(array, a, m, 1, true) == 1)
            Writes.swap(array, a, m, 1, true, false);
        
        if(Reads.compareIndices(array, m, b-1, 1, true) == 1) {
            Writes.swap(array, m, b-1, 1, true, false);
            
            if(Reads.compareIndices(array, a, m, 1, true) == 1)
                return;
        }
        
        Writes.swap(array, a, m, 1, true, false);
    }
    
    //lite version
    private void medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;
        
        while(end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for(i = start; i+2*s <= end; i+=s) {
                this.insSort.customInsertSort(array, i, i+s, 0.25, false);
                Writes.swap(array, j++, i+s/2, 1, false, false);
                Highlights.markArray(2, j);
            }
            if(i < end) {
                this.insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i+(end-(ad ? 1 : 0)-i)/2, 1, false, false);
                Highlights.markArray(2, j);
                if((end-i)%2 == 0) ad = !ad;
            }
            end = j;
        }
    }
    
    public int partition(int[] array, int a, int b, int p) {
        int i = a - 1;
        int j = b;
        Highlights.markArray(3, p);
        
        while(true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while(i < j && Reads.compareIndices(array, i, p, 0, false) == -1);
            
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }
            while(j >= i && Reads.compareIndices(array, j, p, 0, false) == 1);
                
            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                Highlights.clearMark(3);
                return j;
            }
        }
    }
    
    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }
    
    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;

        return this.leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }
    
    private void multiSwap(int[] array, int a, int b, int len) {
        for(int i = 0; i < len; i++)
            Writes.swap(array, a+i, b+i, 1, true, false);
    }
    
    private void mergeWithBufRest(int[] array, int a, int m, int b, int p, int pLen) {
        int i = 0, j = m, k = a;
        
        while(i < pLen && j < b) {
            if(Reads.compareValues(array[p+i], array[j]) <= 0) 
                Writes.swap(array, k++, p+(i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        }
        while(i < pLen) Writes.swap(array, k++, p+(i++), 1, true, false);
    }

    private void mergeWithBuf(int[] array, int a, int m, int b, int p) {
        int l = m-a;
        this.multiSwap(array, p, a, l);
        this.mergeWithBufRest(array, a, m, b, p, l);
    }
    
    private void mergeWithBufBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.multiSwap(array, m, p, pLen);
        
        int i = pLen-1, j = m-1, k = b-1;
        
        while(i >= 0 && j >= a) {
            if(Reads.compareValues(array[p+i], array[j]) >= 0) 
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while(i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
    }
    
    public void smartMerge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0)
            return;
        a = this.leftBoundSearch(array, a, m, array[m]);
        b = this.rightBoundSearch(array, m, b, array[m - 1]);
        if(b - m < m - a) mergeWithBufBW(array, a, m, b, p);
        else mergeWithBuf(array, a, m, b, p);
    }
    
    public static int getMinLevel(int n) {
        while(n >= 32) n = (n+1)/2;
        return n;
    }
    
    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        
        if(a > b) {
            int temp = array[a];
            
            do Writes.write(array, a, array[--a], 0.25, true, false);
            while(a > b);
            
            Writes.write(array, b, temp, 0.25, true, false);
        }
    }
    
    protected void insertion(int[] array, int a, int b) {
        int i = a + 1;
        if(Reads.compareIndices(array, i-1, i++, 1, true) == 1) {
            while(i < b && Reads.compareIndices(array, i-1, i, 0.5, true) == 1) i++;
            Writes.reversal(array, a, i-1, 1, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i-1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for(; i < b; i++) {
            insertTo(array, i, rightBinSearch(array, a, i, array[i]));
        }
    }
    
    private void buildRuns(int[] array, int a, int b, int mRun) {
        int i = a+1, j = a;
        
        while(i < b) {
            if(Reads.compareIndices(array, i-1, i++, 1, true) == 1) {
                while(i < b && Reads.compareIndices(array, i-1, i, 1, true) == 1) i++;
                Writes.reversal(array, j, i-1, 1, true, false);
            }
            else while(i < b && Reads.compareIndices(array, i-1, i, 1, true) <= 0) i++;
            
            if(i < b) j = i - (i-j-1)%mRun - 1;
            
            while(i-j < mRun && i < b) {
                this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
    }
    
    public void mergeSort(int[] array, int a, int b, int p) {
        int length = b-a;
        if(length < 2) return;
        
        int i, j = getMinLevel(length);
        
        buildRuns(array, a, b, j);
        
        while(j < length) {
            for(i = a; i+2*j <= b; i+=2*j)
                this.smartMerge(array, i, i+j, i+2*j, p);
            if(i + j < b)
                this.smartMerge(array, i, i+j, b, p);

            j *= 2;
        }
    }
    
    public void medianMergeSort(int[] array, int a, int b) {
        this.insSort = new InsertionSort(arrayVisualizer);
        int start = a, end = b;
        boolean badPartition = false, mom = false;
        
        while(end - start > 16) {
            if(badPartition) {
                this.medianOfMedians(array, start, end, 5);
                mom = true;
            }
            else this.medianOfThree(array, start, end);
            
            int p = this.partition(array, start+1, end, start);
            Writes.swap(array, start, p, 1, true, false);
            int left  = p-start;
            int right = end-(p+1);
            badPartition = !mom && ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16));
            
            if(left <= right) {
                this.mergeSort(array, start, p, p+1);
                start = p+1;
            }
            else {
                this.mergeSort(array, p+1, end, start);
                end = p;
            }
        }
        insertion(array, start, end);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        medianMergeSort(array, 0, sortLength);

    }

}
