package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

// The worst in-place block merge, using half the array for 2 different buffers and key storage
final public class UnholyHalfSort extends Sort {
    public UnholyHalfSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unholy Half");
        this.setRunAllSortsName("Unholy Half Sort");
        this.setRunSortName("Unholy Halfsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private InsertionSort inserter;
    private KotaSort kitkat;
    private GrailSort jesus;
    private int blockSize;
    private int keyLoc, kotaLoc, halfLoc;
    private int qsqrt(int s) {
        int a=1;
        while(s/a >= (a*=2));
        return a/2;
    }
    private int rightFMerge(int[] array, int start, int mid, int end) {
        int low = mid - 1, hSz = (end - mid), high = kotaLoc + hSz - 1;
        this.multiSwap(array, kotaLoc, mid, hSz);
        int to = end-1, fragSz = 0;
        boolean lowHit = false, highHit = false;
        while(low >= start && high >= kotaLoc) {
            if(Reads.compareValues(array[low], array[high]) == 1) {
                if(!highHit) {
                    fragSz++;
                }
                Writes.swap(array, to--, low--, 1, true, false);
                lowHit = true;
            } else {
                if(!lowHit) {
                    fragSz++;
                }
                Writes.swap(array, to--, high--, 1, true, false);
                highHit = true;
            }
        }
        while(high >= kotaLoc) {
            Writes.swap(array, to--, high--, 1, true, false);
        }
        return fragSz;
    }
    private void multiSwap(int[] array, int a, int b, int size) {
        while(size-- > 0)
            Writes.swap(array, a++, b++, 1, true, false);
    }
    private int revExpS(int[] array, int a, int b, int k) {
        int g = 1;
        while(g < b-a && Reads.compareValues(array[b-g], k) > 0) {
            g *= 2;
            if(b-g>a)
                Highlights.markArray(1, b-g);
            Delays.sleep(2);
        }
        if(g > b-a) {
            g = b-a;
            Highlights.markArray(1, a);
        }
        return b-(g/2);
    }
    private void InPlaceMerge(int[] array, int start, int mid, int end, int buff) {
        int l = mid-1, r = end-1, t = buff-1;
        while(l >= start && r >= mid) {
            if(Reads.compareValues(array[l], array[r]) == 1) {
                Writes.swap(array, t--, l--, 1, true, false);
            } else {
                Writes.swap(array, t--, r--, 1, true, false);
            }
        }
        while(l >= start) {
            Writes.swap(array, t--, l--, 1, true, false);
        }
        while(r >= mid) {
            Writes.swap(array, t--, r--, 1, true, false);
        }
    }
    private void FrontMerge(int[] array, int start, int end) {
        int quart = (end - start) / 4,
            half = (end - start)  / 2;
        if(quart < 1) {
            inserter.customInsertSort(array, start, end, 0.5, false);
            return;
        }
        
        FrontMerge(array, start, start + quart);
        FrontMerge(array, start + quart, end - half);
        FrontMerge(array, end - half, end - quart);
        FrontMerge(array, end - quart, end);
        
        InPlaceMerge(array, start, start + quart, end - half, end + half);
        InPlaceMerge(array, end - half, end - quart, end, end + (end - start));
        InPlaceMerge(array, end, end + half, end + (end - start), end);
    }
    private void BackMerge(int[] array, int start, int end) {
        int quart = (end - start) / 4,
            half = (end - start)  / 2;
        if(quart < 1) {
            inserter.customInsertSort(array, start, end, 0.5, false);
            return;
        }
        BackMerge(array, end - quart, end);
        BackMerge(array, end - half, end - quart);
        BackMerge(array, start + quart, end - half);
        BackMerge(array, start, start + quart);
        
        InPlaceMerge(array, start, start + quart, end - half, start - half);
        InPlaceMerge(array, end - half, end - quart, end, start);
        InPlaceMerge(array, start - (end - start), start - half, start, end);
    }
    protected void mergeRight(int[] array, int start, int mid, int end, boolean useKota) {
        int buf = useKota ? kotaLoc : halfLoc;
        this.multiSwap(array, mid, buf, end-mid);
        int left = mid-1, right = buf+(end-mid)-1, to = end-1;
        while(left >= start && right >= buf) {
            if(Reads.compareValues(array[left], array[right])==1) {
                Writes.swap(array,left--,to--,1,true,false);
            } else {
                Writes.swap(array,right--,to--,1,true,false);
            }
        }
        while(right >= buf) {
            Writes.swap(array,right--,to--,1,true,false);
        }
        if(!useKota) {
            this.inserter.customInsertSort(array, halfLoc, halfLoc + (end-mid), 1, false);
        }
    }
    protected void mergeLeft(int[] array, int start, int mid, int end, boolean useKota) {
        int buf = useKota ? kotaLoc : halfLoc;
        this.multiSwap(array, start, buf, mid-start);
        int left = buf, m = buf+(mid-start), right = mid, to = start;
        while(left < m && right < end) {
            if(Reads.compareValues(array[left], array[right])<=0) {
                Writes.swap(array,left++,to++,1,true,false);
            } else {
                Writes.swap(array,right++,to++,1,true,false);
            }
        }
        while(left < m) {
            Writes.swap(array,left++,to++,1,true,false);
        }
        if(!useKota) {
            this.inserter.customInsertSort(array, halfLoc, halfLoc + (end-mid), 1, false);
        }
    }
    private void blockStupidCycle(int[] array, int start, int mid, int end) {
        int b = keyLoc;
        for(int i=start; i<end; i+=blockSize) {
            Writes.swap(array, i, b++, 1, true, false);
        }
        
        for(int i=keyLoc; i<b; i++) {
            int lower;
            do {
                lower = 0;
                for(int k = i+1; k < b; k++) {
                    if(Reads.compareValues(array[k], array[i]) == -1) {
                        lower++;
                    }
                }
                Writes.swap(array, i + lower, i, 1, true, false);
                this.multiSwap(array, start + (i-keyLoc) * blockSize, start + ((i-keyLoc) + lower) * blockSize, blockSize);
            } while(lower > 0);
        }
        
        b = keyLoc;
        for(int i=start; i<end; i+=blockSize) {
            Writes.swap(array, i, b++, 1, true, false);
        }
        // this.inserter.customInsertSort(array, keyLoc, b, 1, false);
        int f = start;
        for(int i=start+blockSize; i<end; i+=blockSize) {
            int s = revExpS(array, i, i+blockSize, array[i-1]);
            f = s - this.rightFMerge(array, f, i, s);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        blockSize = qsqrt(currentLength);
        inserter = new InsertionSort(arrayVisualizer);
        kitkat = new KotaSort(arrayVisualizer);
        kitkat.bufLen = 2*blockSize;
        kitkat.blockLen = blockSize;
        jesus = new GrailSort(arrayVisualizer);
        kotaLoc = currentLength - 2*blockSize;
        kitkat.bufPos = kotaLoc;
        halfLoc = (2*blockSize) + currentLength / 2;
        BackMerge(array, halfLoc, kotaLoc);
        int k = jesus.grailFindKeys(array, 0, (2*blockSize) + currentLength / 2, 2*blockSize);
        IndexedRotations.cycleReverse(array, 0, k, (currentLength / 2) + 2*blockSize, 1, true, false);
        keyLoc = (currentLength / 2) + ((2*blockSize) - k);
        for(int i=0; i<currentLength / 2; i+=blockSize*2) {
            FrontMerge(array, i, Math.min(i + 2 * blockSize, currentLength / 2));
        }
        int goodKeys = (keyLoc - 1) / blockSize + 1;
        inserter.customInsertSort(array, keyLoc, 2*blockSize + keyLoc, 0.25, false);
        for(int j=2*blockSize; j<currentLength/2; j*=2) {
            for(int i=0; i<keyLoc; i+=2*j) {
                int m=i+2*j;
                if(m > currentLength/2) {
                    this.mergeRight(array, i, i+j, currentLength / 2, false);
                } else {
                    this.blockStupidCycle(array, i, i+j, m);
                }
            }
        }
        inserter.customInsertSort(array, keyLoc, goodKeys + keyLoc, 0.25, false);
        this.mergeLeft(array, keyLoc, keyLoc + k, kotaLoc, true);
        kitkat.blockMergeBW(array, 0, keyLoc, kotaLoc, false);
        FrontMerge(array, 0, k);
        inserter.customInsertSort(array, k, 2*k, 0.25, false);
        jesus.grailMergeWithoutBuffer(array, 0, k, currentLength - k);
    }
}
