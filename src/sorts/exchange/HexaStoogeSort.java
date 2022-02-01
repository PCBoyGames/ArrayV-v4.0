package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;


// Code refactored from: https://en.wikipedia.org/wiki/Stooge_sort
final public class HexaStoogeSort extends Sort {
    public HexaStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hexa Stooge");
        this.setRunAllSortsName("Hexa Stooge Sort");
        this.setRunSortName("Hexastoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void stoogeSort(int[] A, int i, int j) {
        int len = j-i+1;
        Delays.sleep(0.0025);
        Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if(len == 2) {
            if (Reads.compareValues(A[i], A[j]) == 1) {
                Writes.swap(A, i, j, 0.005, true, false);
            }
        } else if(len == 3) {
            stoogeSort(A, i, j-1);
            stoogeSort(A, i+1, j);
            stoogeSort(A, i, j-1);
        } else if(len == 4) {
            int half1 = len / 2, half2 = (len+1) / 2,
                    half3 = (half1 + 1) / 2 + (half2 + 1) / 2;

            stoogeSort(A, i, i + half1);
            stoogeSort(A, i + half1, i+half1+half2);
            stoogeSort(A, i + half1 / 2, i+(half1/2)+half3);
            stoogeSort(A, i + half1, i+half1+half2);
            stoogeSort(A, i, i + half1);
            stoogeSort(A, i + half1 / 2, i+(half1/2)+half3);
        } else if(len == 5) {
            int f=len/5;
            this.stoogeSort(A, i, i+2*f);
            this.stoogeSort(A, j-2*f, j);
            this.stoogeSort(A, i+f, j-f);
            this.stoogeSort(A, i, j-2*f);
            this.stoogeSort(A, i+2*f, j);
        } else if(len > 5) {
            int s = len/6;
            this.stoogeSort(A, i, j-3*s);
            this.stoogeSort(A, i+3*s, j);
            this.stoogeSort(A, i+s, j-2*s);
            this.stoogeSort(A, i+2*s, j-s);
            this.stoogeSort(A, i, i+3*s);
            this.stoogeSort(A, j-3*s, j);
            this.stoogeSort(A, i+2*s, j-2*s);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1);
    }
}