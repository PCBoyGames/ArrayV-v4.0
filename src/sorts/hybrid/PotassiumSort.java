package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSort;
import sorts.select.SmoothSort;
import sorts.templates.GrailSorting;

/**
 * @author aphitorite
 * @author EilrahcF
 *
 */
public final class PotassiumSort extends GrailSorting {

    /**
     * @param arrayVisualizer
     */
    public PotassiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Potassium");
        setRunAllSortsName("Potassium Sort");
        setRunSortName("Potassiumsort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(2048);
        setBogoSort(false);

    }

    int end2 = 0;
    int end = 0;
    int block = 0;

       public int avg(int a, int b) {
            return Math.abs(b - a)/2 + a;
        }

        public int avg31(int a, int b) {
            return Math.abs(b - a)/3 + a;
        }

        public int avg32(int a, int b) {
            return Math.abs(b - a)/3*2 + a;
        }

        // Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR pointers.
        private void quickSort(int[] a, int p, int r, int d) {
            Writes.recordDepth(d);

            if(d >= log2(this.end2)) {
                SmoothSort sort = new SmoothSort(this.arrayVisualizer);
                sort.smoothSort(a, p, r, true);
                return;
            }

            int length = r - p;
            if(length > this.end2/Math.sqrt(log2(this.end2))) {
                int pivot      = avg(p, r);
                int pLeft      = p;
                int pLeftL = avg31(pLeft, pivot);
                int pLeftR = avg32(pLeft, pivot);

                int pRight     = r;
                int pRightL = avg31(pivot, pRight);
                int pRightR = avg32(pivot, pRight);


                Writes.swap(a, 0, pLeft,     1, true, false);
                Writes.swap(a, 1, pLeftL,     1, true, false);
                Writes.swap(a, 2, pLeftR,     1, true, false);
                Writes.swap(a, 3, pivot,     1, true, false);
                Writes.swap(a, 4, pRightL,     1, true, false);
                Writes.swap(a, 5, pRightR,     1, true, false);
                Writes.swap(a, 6, pRight,     1, true, false);

                sort.insertionSort(a, 0, 7);

                Writes.swap(a, pLeft,     0,     1, true, false);
                Writes.swap(a, pLeftL,     1,    1, true, false);
                Writes.swap(a, pLeftR,     2,    1, true, false);
                Writes.swap(a, pivot,     3,    1, true, false);
                Writes.swap(a, pRightL, 4,    1, true, false);
                Writes.swap(a, pRightR, 5,    1, true, false);
                Writes.swap(a, pRight,     6,    1, true, false);

                int x = a[pivot];

                int i = p;
                int j = r;

                Highlights.markArray(3, pivot);

                while (i <= j) {
                    while (Reads.compareValues(a[i], x) == -1){
                        i++;
                        Highlights.markArray(1, i);
                        Delays.sleep(0.5);
                    }
                    while (Reads.compareValues(a[j], x) == 1){
                        j--;
                        Highlights.markArray(2, j);
                        Delays.sleep(0.5);
                    }

                    if (i <= j) {
                        // Follow the pivot and highlight it.
                        if(i == pivot) {
                            Highlights.markArray(3, j);
                        }
                        if(j == pivot) {
                            Highlights.markArray(3, i);
                        }

                        Writes.swap(a, i, j, 1, true, false);

                        i++;
                        j--;
                    }
                }

                //Writes.reversal(a, i, r, 0.5, true, false);


                if(i < r) { Writes.recursion(); this.quickSort(a, i, r, d+1); }
                if(p < j) { Writes.recursion(); this.quickSort(a, p, j, d+1); }


            } else { Highlights.clearAllMarks(); this.stableDiamondInit(a, p, r+1); }
        }

        public void pull(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
            for(int j = a; j < b; j++) {
                Writes.swap(array, j, j + 1, sleep, mark, aux);
            }
        }

        public static int log2(int n){ //stackoverflow version
            if(n <= 0) throw new IllegalArgumentException();
            return 31 - Integer.numberOfLeadingZeros(n);
        }


    BlockInsertionSort sort = new BlockInsertionSort(this.arrayVisualizer);


    private boolean isRangeSorted(int[] array, int start, int end, boolean mark, boolean markLast) {
        for (int i = end - 1; i > start; --i) {
            if (Reads.compareIndices(array, i - 1, i, 0.1, mark) > 0) {
                //Highlights.incrementFancyFinishPosition();
                if (markLast) Highlights.markArray(3, i + 1);
                return false;
            }
        }
        return true;
    }

    private void quadStooge(int[] array, int pos, int len, int d) {
        if(pos >= end) return;
        if(pos + len >= end) len = end - pos;

        Writes.recordDepth(d);
        if(isRangeSorted(array, pos, pos + len, true, false)) return;

        //if (len > block && this.Reads.compareValues(array[pos], array[pos + len - 1]) == 1) {
        //    Writes.swap(array, pos, pos + len - 1, 1, true, false);
        //}
        if (len <= block*2) {
            sort.insertionSort(array, pos, pos+len);
            return;
        }
        this.Delays.sleep(0.5D);

        this.Highlights.markArray(1, pos);
        this.Highlights.markArray(2, pos + len - 1);

        int len1 = len / 2;
        int len2 = (len + 1) / 2;
        int len3 = (len1 + 1) / 2 + (len2 + 1) / 2;


        Writes.recursion(5);
        quadStooge(array, pos, len1, d + 1);
        quadStooge(array, pos + len1, len2, d + 1);
        quadStooge(array, pos + len1 / 2, len3, d + 1);
        quadStooge(array, pos + len1, len2, d + 1);
        quadStooge(array, pos, len1, d + 1);
        if (len > 3) {
            Writes.recursion();
            quadStooge(array, pos + len1 / 2, len3, d + 1);
        }
    }

    public void stableDiamond(int[] array, int start, int end) {

        if(isRangeSorted(array, start, end, true, false)) return;

            for(int i = block; i < end - start; i *= 2) {
                for(int j = start; j < end; j += i) {
                quadStooge(array, j, i, 0);}}
        if(isRangeSorted(array, start, end, true, false)) return;
        quadStooge(array, start, end, 0);
    }

    public void stableDiamondInit(int[] array, int start, int end) {
        int len = end - start + 1;

        this.end = end;
        block = (int)Math.floor(Math.sqrt(end-start + 1));
        if(block*block == len) {
            stableDiamond(array, start, end);
        } else {
            int n = len - block*block + 1;

            stableDiamond(array, start + n, end);
            sort.insertionSort(array, start, start + n);

            this.grailMergeWithoutBuffer(array, start, n, len);
        }

        sort.insertionSort(array, end-(int)Math.sqrt(len), end);

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.end2 = sortLength;
        if(sortLength > 16) {
            sort.insertionSort(array, 0, 7);
            this.quickSort(array, 7, sortLength - 1, 0);
            //sort2.insertionSort(array, 8, currentLength);
            this.grailMergeWithoutBuffer(array, 0, 7, sortLength - 1);
        } else sort.insertionSort(array, 0, sortLength);
    }
}