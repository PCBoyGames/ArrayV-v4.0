package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

CURRENTLY INCOMPLETE

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- IIAU DID A THING IN ROBLOX -
------------------------------

I may need help fixing this. I know several things here aren't right.
I left some comments behind just in case.

*/
final public class PartitionHeapMergeSort extends Sort {

    PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);

    // These variables were originally part of individual files.
    // I have instead decided to define them for the entire class.

    // merge.lua
    int mergeStartBlockSize = 16;

    // init.lua
    int minRun = 32;
    int rotateThreshold = 4;

    public PartitionHeapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Partition Heap Merge");
        this.setRunAllSortsName("Partition Heap Merge Sort");
        this.setRunSortName("Partition Heap Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected boolean callback(int a, int b) {
        Reads.addComparison();
        return a < b;
    }

    protected void swap(int[] array, int a, int b) {
        if (a != b) Writes.swap(array, a - 1, b - 1, 0.5, true, false);
    }

    // binary.lua
    // I have made it an Insertion this time!

    protected int binarySearch(int[] array, int num, int lo, int hi) {
        while (true) {
            if (lo > hi) {
                Highlights.clearAllMarks();
                return hi + 1;
            }
            int mid = (int) Math.floor((lo + hi) / 2);
            Highlights.markArray(1, lo - 1);
            Highlights.markArray(2, mid - 1);
            Highlights.markArray(3, hi - 1);
            Delays.sleep(1);
            if (!callback(stablereturn(num), stablereturn(array[mid - 1]))) lo = mid + 1;
            else hi = mid - 1;
        }
    }

    protected int binarySearchR(int[] array, int num, int lo, int hi) {
        while (true) {
            if (lo > hi) {
                Highlights.clearAllMarks();
                return hi + 1;
            }
            int mid = (int) Math.floor((lo + hi) / 2);
            Highlights.markArray(1, lo - 1);
            Highlights.markArray(2, mid - 1);
            Highlights.markArray(3, hi - 1);
            Delays.sleep(1);
            if (!callback(stablereturn(array[mid - 1]), stablereturn(num))) hi = mid - 1;
            else lo = mid + 1;
        }
    }

    protected void binaryInsertion(int[] array, int lo, int hi, int stpt) {
        if (stpt != -1) {
            for (int i = stpt; i <= hi; i++) {
                if (!callback(stablereturn(array[i - 1]), stablereturn(array[i - 2]))) continue;
                int idx = binarySearch(array, stablereturn(array[i - 1]), lo, i - 1);
                Writes.insert(array, i - 1, idx - 1, 0.5, true, false);
            }
        }
        else binsert.pdbinsertUnstable(array, lo - 1, hi, 0.5, false);
    }

    // heapsort.lua
    // Recursive Heapsort...? Why though...?
    // Can someone make this iterative?
    // Just to save me (and the stack) from the pain?

    protected void heapify(int[] array, int n, int start, int max, int depth) {
        Writes.recordDepth(depth);
        int i = start - 1 + (n - start + 1) * 2;
        int j = i + 1;
        if (i > max) return;
        int vp = stablereturn(array[n - 1]);
        int vi = stablereturn(array[i - 1]);
        int vj = stablereturn(array[j - 1]);
        int child_max = i;
        if (j <= max && callback(vi, vj)) child_max = j;
        if (callback(vp, stablereturn(array[child_max - 1]))) {
            swap(array, n, child_max);
            Writes.recursion();
            heapify(array, child_max, start, max, depth + 1);
        }
    }

    protected void heapSort(int start, int last, int[] array) {
        for (int i = start + (int) Math.floor((last - start) / 2); i >= start; i--) heapify(array, i, start, last, 0);
        for (int i = last; i >= start; i--) {
            heapify(array, start, start, i, 0);
            swap(array, i, start);
        }
    }

    // merge.lua
    // I had to do some rather hacky things to fix this.

    protected void mergeAtoB(int[] arr, int mergeToIdx, int mergeFromIdx, int staA, int staB, int endIdx, int bufferSize) {
        bufferSize = bufferSize != -1 ? bufferSize : staB - staA;
        int ptrA = staA, ptrB = staB;
        int i = staA;
        while (true) {
            boolean ptrAOver = ptrA - staA >= bufferSize;
            boolean ptrBOver = ptrB > endIdx || ptrB >= staB + bufferSize;
            if (ptrAOver && ptrBOver) break;
            int idx;
            if (ptrAOver) idx = ptrB++;
            else if (ptrBOver) idx = ptrA++;
            else {
                if (!callback(arr[ptrB - 1], arr[ptrA - 1])) idx = ptrA++;
                else idx = ptrB++;
            }
            swap(arr, mergeToIdx + i - mergeFromIdx, idx);
            i++;
        }
    }

    protected void mergeSort(int[] array, int sidx, int eidx, int bidx) {
        int reglen = eidx - sidx + 1;
        for (int i = 1; i <= Math.ceil(reglen/mergeStartBlockSize) + 1; i++) binaryInsertion(array, sidx + (i - 1) * mergeStartBlockSize, Math.min(sidx + i * mergeStartBlockSize - 1, eidx), -1);
        if (reglen > mergeStartBlockSize) {
            int mergeFromIdx = sidx;
            int mergeToIdx = bidx;
            int regionSize = mergeStartBlockSize;
            while (reglen > regionSize) {
                int i = 1;
                while (true) {
                    int staA = (i - 1) * 2 * regionSize;
                    int staB = staA + regionSize;
                    if (staB > reglen) {
                        if (reglen - staA > 0) rotateRegion(array, mergeFromIdx + staA, mergeToIdx + staA, reglen - staA);
                        break;
                    }
                    mergeAtoB(array, mergeToIdx, mergeFromIdx, mergeFromIdx + staA, mergeFromIdx + staB, mergeFromIdx + reglen - 1, -1);
                    i++;
                }
                regionSize *= 2;
                int tempPtr = mergeFromIdx;
                mergeFromIdx = mergeToIdx;
                mergeToIdx = tempPtr;
            }
            if (mergeFromIdx == bidx) rotateRegion(array, bidx, sidx, reglen);
        }
    }

    // quick.lua

    protected int quickPartition(int[] arr, int pivot, int low, int high) {
        while (true) {
            while (!callback(pivot, arr[low - 1])) {
                low++;
                if (low >= high) return high;
            }
            while (!callback(arr[high - 1], pivot)) {
                high--;
                if (high <= low) return low;
            }
            if (low >= high) return high;
            swap(arr, low, high);
        }
    }

    // rotate.lua
    // Per request, I replaced TriRevs with Trinity and Holy Gries-Mills rotations.
    // Adaptable depends on modulo operations to determine which one to use.
    // Keep in mind Holy Gries-Mills performs best on multiples of x:1 or 1:x ratios.

    protected void rotateRegion(int[] array, int i, int j, int size) {
        for (int k = 0; k <= size - 1; k++) swap(array, i + k, j + k);
    }

    protected void rotateLeft(int[] array, int sidx, int eidx, int num) {
        Writes.reversal(array, sidx - 1, sidx + num - 2, 0.5, true, false);
        //Writes.reversal(array, sidx + num - 1, eidx - 1, 0.5, true, false);
        //Writes.reversal(array, sidx - 1, eidx - 1, 0.5, true, false);
        IndexedRotations.adaptable(array, sidx - 1, sidx + num - 1, eidx, 0.5, true, false);
    }

    // init.lua
    // I know there are problems here. Why would there not be?

    protected int min(int[] array, int lo, int hi) {
        int min = stablereturn(array[lo - 1]);
        if (hi - lo <= 1) return min;
        for (int i = lo + 1; i <= hi; i++) if (callback(stablereturn(array[i - 1]), min)) min = stablereturn(array[i - 1]);
        return min;
    }

    protected int rotateTo(int[] array, int minVal, int fromLo, int rotAmt, int toLo, int toHi) {
        int rotidx = binarySearch(array, minVal, toLo, toHi);
        rotateLeft(array, fromLo, rotidx - 1, rotAmt);
        return rotidx;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (currentLength <= minRun) {
            binaryInsertion(array, 1, currentLength, -1);
            return;
        }
        int thirds = (int) Math.floor(currentLength / 3);
        mergeSort(array, 1, thirds, thirds + 1);
        mergeSort(array, currentLength - thirds + 1, currentLength, thirds + 1);
        mergeAtoB(array, thirds + 1, 1, 1, currentLength - thirds + 1, currentLength, thirds);
        int diff = currentLength - thirds * 3;
        if (diff > 0) binaryInsertion(array, thirds + 1, currentLength, currentLength - diff + 1);
        int currsz = thirds;
        int ptrloc = 1;
        while (true) {
            System.err.println("prtloc = " + ptrloc);
            int currEnd = ptrloc + currsz - 1;
            if (currsz <= rotateThreshold) {
                for (int i = 0; i <= currsz - 1; i++) rotateTo(array, stablereturn(array[currEnd - i - 1]), currEnd - i, 1, currEnd + 1 - i, currentLength);
                break;
            }
            int pivpos = (int) Math.floor((currentLength + (ptrloc + currsz)) / 2);
            int blocksz = currentLength - currEnd;
            if (blocksz == 0) {
                heapSort(ptrloc, currEnd, array);
                return;
            }
            int pivot = stablereturn(array[pivpos - 1]);
            int splitloc = quickPartition(array, pivot, ptrloc, ptrloc + currsz - 1);
            int bufsz = splitloc - ptrloc;
            if (bufsz == 0) {
                int min = min(array, ptrloc, currEnd);
                int newEnd = rotateTo(array, min, ptrloc, currsz, currEnd + 1, currentLength);
                ptrloc = newEnd - currsz;
            } else {
                int maxsz = (int) Math.floor(currsz / 2);
                boolean stationary = false;
                if (bufsz > maxsz) {
                    splitloc = ptrloc + maxsz;
                    bufsz = splitloc - ptrloc;
                    stationary = true;
                }
                int startBuf = ptrloc + bufsz;
                int chunksz = pivpos - currEnd;
                if (chunksz < bufsz / 2) {
                    heapSort(splitloc, currentLength, array);
                    currsz = splitloc - ptrloc;
                    continue;
                }
                if (bufsz <= mergeStartBlockSize) binaryInsertion(array, ptrloc, splitloc - 1, -1);
                else heapSort(ptrloc, splitloc - 1, array);
                if (chunksz < splitloc - ptrloc) chunksz = splitloc - ptrloc;
                mergeAtoB(array, currEnd - bufsz + 1, currEnd + 1, currEnd + 1, ptrloc, startBuf - 1, chunksz);
                currsz -= bufsz;
                if (!stationary) {
                    rotateLeft(array, ptrloc, pivpos - 1, currsz);
                    ptrloc = pivpos - currsz;
                }
            }
        }
    }
}