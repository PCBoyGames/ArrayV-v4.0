package sorts.misc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeonLessInsert;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray, PCBoy and stentor

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Distray
 * @author PCBoy
 * @author stentor
 *
 */
public final class PDSafeStalinSort extends Sort {

    public PDSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Safe Stalin");
        this.setRunAllSortsName("Pattern-Defeating Safe Stalin Sort");
        this.setRunSortName("Pattern-Defeating Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    int firstlen;
    int secondlen;

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b) dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else dir = true;
        if (dir) while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0) i++;
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected ArrayList<Stack<Integer>> buildStacks(int[] array, BitSet bits, int a, int b) {
        ArrayList<Stack<Integer>> stacksBuilt = new ArrayList<>();
        int zeroed = 0;
        while (zeroed < b - a) {
            Stack<Integer> currentStack = new Stack<>();
            for (int j = a; j < b; j++)
                if (!bits.get(j - a))
                    if (currentStack.empty() || Reads.compareValues(currentStack.peek(), array[j]) <= 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        bits.set(j - a);
                        zeroed++;
                    }
            stacksBuilt.add(currentStack);
        }
        bits.clear();
        return stacksBuilt;
    }

    protected void reciteStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
        int ptr = start;
        int stackdone = 0;
        while (stacks.size() > 0) {
            Stack<Integer> first = stacks.remove(0);
            if (stackdone == 0) firstlen = first.size();
            if (stackdone == 1) secondlen = first.size();
            int n = ptr + first.size() - 1;
            while (!first.empty()) {
                Writes.changeAllocAmount(-1);
                Writes.write(array, n--, first.pop(), 1, true, false);
                ptr++;
            }
            stackdone++;
        }
    }

    protected int stepDown(int[] array, int n) {
        int steps = 1;
        int finals = n - 2;
        while (Reads.compareIndices(array, secondlen - 1, finals, 1, true) <= 0) {
            finals--;
            steps++;
        }
        Highlights.clearAllMarks();
        return steps;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if(patternDefeat(array, 0, length)) return;
        BlockInsertionSortNeonLessInsert two = new BlockInsertionSortNeonLessInsert(arrayVisualizer);
        int n = length;
        boolean check = false;
        BitSet bits = new BitSet(length);
        while (!check && n > 1) {
            ArrayList<Stack<Integer>> stacks = buildStacks(array, bits, 0, n);
            int size = stacks.size();
            reciteStacks(array, 0, n, stacks);
            if (size > 2) {
                IndexedRotations.neon(array, 0, firstlen, n, 1, true, false);
                if (!check) n -= stepDown(array, n);
            } else {
                if (size == 2) two.insertionSort(array, 0, n);
                check = true;
            }
        }
    }
}