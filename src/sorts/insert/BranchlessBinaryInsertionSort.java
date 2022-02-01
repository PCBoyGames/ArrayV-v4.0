package sorts.insert;

import java.util.Comparator;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BranchlessBinaryInsertionSort extends Sort {
    public BranchlessBinaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Branchless Binary Insert");
        this.setRunAllSortsName("Branchless Binary Insertion Sort");
        this.setRunSortName("Branchless Binary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int sign(int v) {
        return (v>>31) | -(-v>>31);
    }

    private int binsearch(int[] array, int start, int end, int key, double slp, Comparator<Integer> bcmp) {
        while(end-start > 0) {
            int l = end - start,
                c = bcmp.compare(key, array[start+l/2]); // branchless compare here
            Highlights.markArray(1, start+l/2);
            Delays.sleep(slp);
            start += ((l / 2) + 1) * sign(c+1);
            end -= ((l + 1) / 2) * -sign(c-1);
        }
        Highlights.clearAllMarks();
        return start;
    }

    public void binaryInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start + 1; i < end; i++) {
            int num = array[i];
            int src = binsearch(array, start, i, num, compSleep, Reads::compareValues);

            int j = i - 1;

            while (j >= src)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            if(src < i)
                Writes.write(array, src, num, writeSleep, true, false);

            Highlights.clearAllMarks();
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.binaryInsertSort(array, 0, currentLength, 5, 0.05);
    }
}