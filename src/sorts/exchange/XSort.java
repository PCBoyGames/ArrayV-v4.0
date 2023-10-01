package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class XSort extends Sort {
    public XSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("X Pattern");
        this.setRunAllSortsName("X Pattern Sort");
        this.setRunSortName("X Pattern Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
        this.setQuestion("Enter the X gap for this sort:", 1);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int gap = currentLength;
        boolean testpass = false;
        while (!testpass) {
            boolean anyswaps = false;
            for (int i = 1; (i - 1) + gap < currentLength; i++) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.001, true) > 0) {
                    Writes.swap(array, i - 1, (i - 1) + gap, 0.001, anyswaps = true, false);
                    int xleft = i + 1;
                    int xright = i + gap - 1;
                    if (gap != 1) {
                        for (int r = 0; r < gap - 1; r += base) {
                            if (Reads.compareIndices(array, xleft - 1, xright - 1, 0.001, true) > 0) Writes.swap(array, xleft - 1, xright - 1, 0.001, true, false);
                            xleft += base;
                            xright -= base;
                        }
                    }
                }
            }
            if (!anyswaps) {
                if (gap == 1) testpass = true;
                else gap--;
            }
        }
    }
}