package sorts.misc;

import java.util.ArrayList;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.templates.Sort;
import utils.IndexedRotations;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH STENTOR AND DISTRAY

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedSafeStalinSort extends Sort {

    int firstlen;
    int secondlen;

    public OptimizedSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Safe Stalin");
        this.setRunAllSortsName("Optimized Safe Stalin Sort");
        this.setRunSortName("Optimized Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    protected ArrayList<Stack<Integer>> buildStacks(int[] array, int start, int end) {
        ArrayList<Stack<Integer>> stacksBuilt = new ArrayList<>();
        int zero = Integer.MIN_VALUE, zeroed = 0;
        while(zeroed < end-start) {
            Stack<Integer> currentStack = new Stack<>();
            Statistics.addStat("Stack");
            for(int j=start; j<end; j++) {
                if(array[j] != zero) {
                    if(currentStack.empty() || Reads.compareValues(currentStack.peek(), array[j]) <= 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        Writes.write(array, j, zero, 0, false, false);
                        zeroed++;
                    }
                }
            }
            stacksBuilt.add(currentStack);
        }
        return stacksBuilt;
    }

    protected void reciteStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
        int ptr = start;
        int stackdone = 0;
        while(stacks.size() > 0) {
            Stack<Integer> first = stacks.remove(0);
            if (stackdone == 0) firstlen = first.size();
            if (stackdone == 1) secondlen = first.size();
            int n = ptr + first.size() - 1;
            while(!first.empty()) {
                Writes.changeAllocAmount(-1);
                Writes.write(array, n--, first.pop(), 1, true, false);
                ptr++;
            }
            stackdone++;
        }
    }

    protected int stepDown(int[] array, int end) {
        int steps = 1;
        int finals = end - 2;
        while (Reads.compareIndices(array, secondlen - 1, finals, 1, true) <= 0) {
            finals--;
            steps++;
        }
        Highlights.clearAllMarks();
        return steps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        BlockInsertionSortNeon two = new BlockInsertionSortNeon(arrayVisualizer);
        int workinglength = currentLength;
        Statistics.putStat("Stack");
        boolean check = false;
        while (!check && workinglength > 1) {
            ArrayList<Stack<Integer>> stacks = buildStacks(array, 0, workinglength);
            int size = stacks.size();
            reciteStacks(array, 0, workinglength, stacks);
            if (size > 2) {
                IndexedRotations.neon(array, 0, firstlen, workinglength, 1, true, false);
                if (!check) workinglength -= stepDown(array, workinglength);
                Statistics.resetStat("Stack");
            } else {
                Statistics.resetStat("Stack");
                if (size == 2) two.insertionSort(array, 0, workinglength);
                check = true;
            }
        }
    }
}