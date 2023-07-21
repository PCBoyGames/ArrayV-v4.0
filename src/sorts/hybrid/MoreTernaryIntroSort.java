package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Credit to Ayako-chan.

 */

public final class MoreTernaryIntroSort extends Sort {

    private int middle;
    private int sizeThreshold = 32;

    public MoreTernaryIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("More Ternary Intro");
        this.setRunAllSortsName("More Ternary Introspective Sort");
        this.setRunSortName("More Ternary Introsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Start of Ternary insertion.

    public int ternary(int[] array, int start, int end, int key, double sleep) {
        while (start < end-1) {
            int third = (end - start + 1) / 3,
                midA = start + third,
                midB = end - third;
            if (Reads.compareValues(array[midA], key) == 1) {
                end = midA;
            } else if (Reads.compareValues(array[midB], key) == -1) {
                start = midB;
            } else {
                start = midA;
                end = midB;
            }
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }

    private void ternaryInsert(int[] array, int a, int b){
        for (int i = a+1; i < b; i++) {
            Writes.insert(array, i, ternary(array, 0, i, array[i], 1), 0.05, true, false);
        }
    }

    // End of Ternary insertion.

    //Start of Ternary Heap.

    private int heapSize;

    private static int leftBranch(int i) {
        return 3 * i + 1;
    }

    private static int middleBranch(int i) {
        return 3 * i + 2;
    }

    private static int rightBranch(int i) {
        return 3 * i + 3;
    }

    private void maxHeapify(int[] array, int i) {

        int leftChild = leftBranch(i);
        int rightChild = rightBranch(i);
        int middleChild = middleBranch(i);
        int largest;

        largest = leftChild <= heapSize && Reads.compareValues(array[leftChild], array[i]) > 0 ? leftChild : i;

        if (rightChild <= heapSize && Reads.compareValues(array[rightChild], array[largest]) > 0) {
            largest = rightChild;
        }

        if (middleChild <= heapSize && Reads.compareValues(array[middleChild], array[largest]) > 0) {
            largest = middleChild;
        }


        if (largest != i) {
            Writes.swap(array, i, largest, 1, true, false);
            this.maxHeapify(array, largest);
        }
    }

    private void buildMaxTernaryHeap(int[] array, int a, int b) {
        heapSize = b - 1;
        for (int i = b - 1  / 3; i >= a; i--)
            this.maxHeapify(array, i);
    }

    private void ternaryHeap(int[] array, int a, int b) {
        this.buildMaxTernaryHeap(array, a, b);

        for (int i = b - 1; i >= a; i--) {
            Writes.swap(array, a, i, 1, true, false); //add last element on array, i.e heap root

            heapSize = heapSize - 1; //shrink heap by 1
            this.maxHeapify(array, a);
        }
    }

    //End of Ternary Heap.

    public static int log2(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2)));
    }

    private int medianof3(int[] arr, int left, int mid, int right) {
        if (Reads.compareValues(arr[right], arr[left]) == -1) {
            Writes.swap(arr, left, right, 1, true, false);
        }
        if (Reads.compareValues(arr[mid], arr[left]) == -1) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if (Reads.compareValues(arr[right], arr[mid]) == -1) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        middle = mid;
        Highlights.markArray(3, mid);
        return arr[mid];
    }


    public int[] partition(int[] array, int a, int b, int piv) {
        // determines which elements do not need to be moved
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) >= 0) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) <= 0) break;
        }
        int i1 = a, i = a - 1, j = b, j1 = b;
        while (true) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) Writes.swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) Writes.swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i >= j) {
                if (i1 == b) return new int[] { a, b };
                if (j < i) j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) Writes.swap(array, i++, i1++, 1, true, false);
                } else while (i1 > a) Writes.swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) Writes.swap(array, --j, --j1, 1, true, false);
                } else while (j1 < b) Writes.swap(array, j++, j1++, 1, true, false);
                return new int[] { i, j };
            }
            // The patch is not needed here, because it never swaps when i == j.
            Writes.swap(array, i, j, 1, true, false);
            Highlights.clearMark(2);
        }
    }
    
    private void introsortLoop(int[] a, int lo, int hi, int depthLimit) {
        while (hi - lo > sizeThreshold) {
            if (depthLimit == 0) {
                Highlights.clearAllMarks();
                ternaryHeap(a, lo, hi);
                return;
            }
            depthLimit--;
            int[] p = partition(a, lo, hi, medianof3(a, lo, lo + ((hi - lo) / 2), hi - 1));
            if (hi - p[1] < p[0] - lo) {
                introsortLoop(a, p[1], hi, depthLimit);
                hi = p[0];
            } else {
                introsortLoop(a, lo, p[0], depthLimit);
                lo = p[1];
            }
        }
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        introsortLoop(array, a, b, 2 * log2(b - a));
        Highlights.clearAllMarks();
        ternaryInsert(array, a, b);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        quickSort(array, 0, currentLength);
    }
}
