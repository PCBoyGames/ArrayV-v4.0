package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class JamSort extends MadhouseTools {
    public JamSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Jam");
        this.setRunAllSortsName("Jam Sort");
        this.setRunSortName("Jamsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:", 64);
    }

    protected boolean cs(int[] array, double a, double b) {
        if ((int) a != (int) b) {
            if (Reads.compareIndices(array, (int) a, (int) b, 0.1, true) > 0) {
                Writes.swap(array, (int) a, (int) b, 0.1, true, false);
                return true;
            }
        }
        return false;
    }

    protected void spread(int[] array, int start, int end, int over, int depth) {
        Writes.recordDepth(depth);
        int l = end - start;
        if (l < over) {
            if (l < 0) return;
            for (int a = start, b = end - 1; a < b;) {
                int c = 1;
                for (int i = a; i < b; i++) {
                    if (cs(array, i, i + 1)) c = 1;
                    else c++;
                }
                b -= c;
                c = 1;
                for (int i = b; i > a; i--) {
                    if (cs(array, i - 1, i)) c = 1;
                    else c++;
                }
                a += c;
            }
            return;
        }
        // TODO: Manage into bidirectional something or another.
        // start + (1.0 * i / (over - 1)) * (l - 1), where 0 <= i < over.

        for (int i = 0; i < over - 1; i++) for (int j = i; j >= 0; j--) if (!cs(array, start + (1.0 * j / (over - 1)) * (l - 1), start + (1.0 * (j + 1) / (over - 1)) * (l - 1))) break;

        if (depth % 2 == 1 && end - 1 >= start) {
            Writes.recursion();
            spread(array, minSorted(array, start, end - 1, 0, true), maxSorted(array, start, end - 1, 0, true), over, depth + 1);
        }
        if (start + 1 <= end) {
            Writes.recursion();
            spread(array, minSorted(array, start + 1, end, 0, true), maxSorted(array, start + 1, end, 0, true), over, depth + 1);
        }
        if (end - 1 >= start) {
            Writes.recursion();
            spread(array, minSorted(array, start, end - 1, 0, true), maxSorted(array, start, end - 1, 0, true), over, depth + 1);
        }
        if (depth % 2 == 0 && start + 1 <= end) {
            Writes.recursion();
            spread(array, minSorted(array, start + 1, end, 0, true), maxSorted(array, start + 1, end, 0, true), over, depth + 1);
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        arrayVisualizer.setExtraHeading(" / SPAN: " + base);
        spread(array, minSorted(array, 0, currentLength, 0, true), maxSorted(array, 0, currentLength, 0, true), Math.min(base, currentLength), 0);
    }
}