package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SmoothSort2 extends Sort {

    public SmoothSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Smooth II");
        this.setRunAllSortsName("Smooth Sort");
        this.setRunSortName("Smoothsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static final int LP[] = { 1, 1, 3, 5, 9, 15, 25, 41, 67, 109, 177, 287, 465, 753, 1219, 1973, 3193, 5167, 8361,
            13529, 21891, 35421, 57313, 92735, 150049, 242785, 392835, 635621, 1028457, 1664079, 2692537, 4356617,
            7049155, 11405773, 18454929, 29860703, 48315633, 78176337, 126491971, 204668309, 331160281, 535828591,
            866988873, 1402817465 };// the next number is > INT32_MAX.

    /*
    void sift(int[] array, int order, int head) {
        if (order < 2) return;
        // Improved sift function to use two comparisons per step, not three
        while (order > 1) {
            int rt = head - 1;
            int lf = rt - LP[order-2];
            Highlights.markArray(2, rt);
            Highlights.markArray(3, lf);
            Delays.sleep(0.325);
            if (Reads.compareValues(array[lf], array[rt]) < 0) {
                // sift into right child
                if (Reads.compareValues(array[rt], array[head]) > 0)
                    Writes.swap(array, head, rt, 0.65, true, false);
                else break;
                head = rt;
                order -= 2;
            } else {
                // sift into left child
                if (Reads.compareValues(array[lf], array[head]) > 0)
                    Writes.swap(array, head, lf, 0.65, true, false);
                else break;
                head = lf;
                order -= 1;
            }
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
    }
    */

    void sift(int[] array, int order, int head) {
        if (order < 2) return;
        // Improved sift function to use two comparisons per step, not three
        int val = array[head];
        while (order > 1) {
            int rt = head - 1;
            int lf = rt - LP[order-2];
            Highlights.markArray(2, rt);
            Highlights.markArray(3, lf);
            Delays.sleep(0.325);
            if (Reads.compareValues(array[lf], array[rt]) < 0) {
                // sift into right child
                if (Reads.compareValues(array[rt], val) > 0)
                    Writes.write(array, head, array[rt], 0.65, true, false);
                else break;
                head = rt;
                order -= 2;
            } else {
                // sift into left child
                if (Reads.compareValues(array[lf], val) > 0)
                    Writes.write(array, head, array[lf], 0.65, true, false);
                else break;
                head = lf;
                order -= 1;
            }
        }
        Writes.write(array, head, val, 0.65, true, false);
        Highlights.clearMark(2);
        Highlights.clearMark(3);
    }

    void trinkle(int[] array, long heaps, int head) {
        if (heaps == 0) return;
        int val = array[head];
        while (heaps != 0) {
            int order = Long.numberOfTrailingZeros(heaps);

            // leftmost heap has no stepson, just sift
            if ((heaps >> order) == 1) {
                Writes.write(array, head, val, 0.65, true, false);
                sift(array, order, head);
                return;
            }

            int stepson = head - LP[order];

            if (Reads.compareValues(val, array[stepson]) < 0) {
                if (order > 1) {
                    // check stepson against children
                    int rt = head - 1;
                    int lf = rt - LP[order-2];
                    Highlights.markArray(2, rt);
                    Highlights.markArray(3, lf);
                    Delays.sleep(0.325);
                    if (Reads.compareValues(array[lf], array[rt]) < 0) {
                        // compare head, stepson, and right child
                        if (Reads.compareValues(array[stepson], array[rt]) < 0) {
                            // by transitive property, A[head] < A[stepson] < A[rt], thus A[head] < A[rt]
                            // sift into right child
                            Writes.write(array, head, array[rt], 0, true, false);
                            Writes.write(array, rt, val, 0.65, true, false);
                            sift(array, order-2, rt);
                            return;
                        }
                    } else {
                        // compare head, stepson, and left child
                        if (Reads.compareValues(array[stepson], array[lf]) < 0) {
                            // by transitive property, A[head] < A[stepson] < A[lf], thus A[head] < A[lf]
                            // sift into left child
                            Writes.write(array, head, array[lf], 0, true, false);
                            Writes.write(array, lf, val, 0.65, true, false);
                            sift(array, order-1, lf);
                            return;
                        }
                    }
                }

                // move to next root on left
                Writes.write(array, head, array[stepson], 0.65, true, false);
                head = stepson;
                Highlights.clearMark(2);
                Highlights.clearMark(3);
                heaps &= ~(1L << order);
            } else {
                // sift into current tree
                Writes.write(array, head, val, 0.65, true, false);
                sift(array, order, head);
                return;
            }
        }
        Writes.write(array, head, val, 0.65, true, false);
    }

    // Semitrinkle assumes the current heap is fully valid, but the root may need to be moved left
    void semitrinkle(int[] array, long heaps, int head) {
        int val = array[head];
        boolean change = false;
        while (true) {
            int order = Long.numberOfTrailingZeros(heaps);

            // leftmost heap has no stepson, just return
            if ((heaps >> order) == 1)
                break;

            int stepson = head - LP[order];

            if (Reads.compareValueIndex(array, val, stepson, 0.325, true) >= 0) break;

            Writes.write(array, head, array[stepson], 0.65, change = true, false);
            if (order == 0 && (heaps & 2) != 0) {
                // next one is order 1
                heaps &= ~1L;
                head = stepson;
            } else {
                // The change boolean is not necessary here, because it is always true on entry.
                Writes.write(array, stepson, val, 0.65, true, false);
                trinkle(array, heaps & ~(1L << order), stepson);
                return;
            }
        }
        if (change) Writes.write(array, head, val, 0.65, true, false);
    }

    void sort(int[] A, int l, int r, boolean fullSort) {
        long heaps = 0; // bitmap of complete Leonardo heap orders

        // Start by building the forest of heaps
        for (int head = l; head < r; head++) {
            int order = Long.numberOfTrailingZeros(heaps);

            if (((heaps >> order) & 3) == 3) {
                // Rightmost heaps are of consecutive order, so merge them
                heaps += 1L << order;
                sift(A, order+2, head);
            } else {
                // adding a new block of length 1
                if ((heaps & 2) != 0) {
                    // L(1) already present, so this must be L(0) sibling
                    heaps |= 1L;
                } else {
                    // L(1) takes priority over L(0)
                    heaps |= 1L << 1;
                }
            }
        }
        if (!fullSort) return;
        // Sort the root nodes
        for (int head = l, order = 63; head < r; order--) {
            if ((heaps & (1L << order)) != 0) {
                long forest = heaps & ~((1L << order) - 1);
                head += LP[order];
                semitrinkle(A, forest, head-1);
            }
        }

        // Now extract one element at a time
        for (int head = r-1; head > l; head--) {
            int order = Long.numberOfTrailingZeros(heaps);

            if (order < 2) {
                // L(0) and L(1) are trivial
                heaps = (heaps - 1L) & ~1L;
            } else {
                int rt = head - 1, lf = rt - LP[order-2];

                // Ensure roots are in sorted order
                heaps -= 1L << (order-1);
                semitrinkle(A, heaps, lf);
                heaps += 1L << (order-2);
                semitrinkle(A, heaps, rt);

            }
        }
    }

    public void smoothHeapify(int[] array, int start, int end) {
        this.sort(array, start, end, false);
    }

    public void smoothSort(int[] array, int start, int end) {
        this.sort(array, start, end, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        smoothSort(array, 0, sortLength);

    }

}
