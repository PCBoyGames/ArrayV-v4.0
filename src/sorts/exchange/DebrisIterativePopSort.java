package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class DebrisIterativePopSort extends Sort {
    public DebrisIterativePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Debris Iterative Pop");
        this.setRunAllSortsName("Debris Iterative Pop Sort");
        this.setRunSortName("Debris Iterative Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void bubble(int[] array, int s, int e, int dir) {
        int i = s;
        int start = s;
        int end = s;
        int first = s + 1;
        int last = e - 1;
        int nextlast = e - 1;
        boolean firstfound = false;
        boolean anyrev = true;
        while (anyrev) {
            anyrev = false;
            firstfound = false;
            if (first > s) i = first - 1;
            else i = s;
            while (i < last) {
                start = i;
                while (Reads.compareIndices(array, i, i + 1, 0.025, true) == dir && i < last) {
                    if (!firstfound) {
                        first = i;
                        firstfound = true;
                    }
                    nextlast = i + 1;
                    i++;
                }
                end = i;
                if (start != end) {
                    if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
                    else Writes.reversal(array, start, end, 0.075, true, false);
                    anyrev = true;
                }
                i++;
            }
            if (nextlast + 1 < e - 1) last = nextlast + 1;
            else last = e - 1;
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        int dir = -1;
        while (len < currentLength) {
            index = 0;
            dir = -1;
            while (index + len <= currentLength) {
                bubble(array, index, index + len, dir);
                index += len;
                dir *= -1;
            }
            if (index != currentLength) bubble(array, index, currentLength, dir);
            len *= 2;
        }
        bubble(array, 0, currentLength, 1);
    }
}