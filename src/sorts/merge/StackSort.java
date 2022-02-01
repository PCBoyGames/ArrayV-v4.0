package sorts.merge;

import java.util.ArrayList;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Statistics;


final public class StackSort extends Sort {
    public StackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stack");
        this.setRunAllSortsName("Stack Sort");
        this.setRunSortName("Stacksort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    // Credit to Stentor for the original algorithm

    private ArrayList<Stack<Integer>> buildStacks(int[] array, int start, int end) {
        ArrayList<Stack<Integer>> stacksBuilt = new ArrayList<>();
        int zero = Integer.MIN_VALUE, zeroed = 0;
        while(zeroed < end-start) {
            Stack<Integer> currentStack = new Stack<>();
            Statistics.addStat("Stack");
            for(int j=start; j<end; j++) {
                if(array[j] != zero) {
                    if(currentStack.empty() || Reads.compareValues(currentStack.peek(), array[j]) < 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        Writes.write(array, j, zero, 1, false, false);
                        zeroed++;
                    }
                }
            }
            stacksBuilt.add(currentStack);
        }
        return stacksBuilt;
    }

    private int mergeWithStack(int[] array, int start, int end, Stack<Integer> stack) {
        int l = end-1, sz = stack.size(), t = end + sz - 1;
        while(l >= start && !stack.empty()) {
            if(Reads.compareValues(array[l], stack.peek()) > 0) {
                Writes.write(array, t--, array[l--], 0.5, true, false);
            } else {
                Writes.changeAllocAmount(-1);
                Writes.write(array, t--, stack.pop(), 0.5, true, false);
            }
        }
        while(!stack.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, t--, stack.pop(), 0.5, true, false);
        }
        return end + sz;
    }

    private void mergeStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
        Stack<Integer> first = stacks.remove(0);
        int ptr = start, n = start + first.size() - 1;
        while(!first.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, n--, first.pop(), 0.5, true, false);
            ptr++;
        }
        while(stacks.size() > 0) {
            ptr = mergeWithStack(array, start, ptr, stacks.remove(0));
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Statistics.putStat("Stack");
        ArrayList<Stack<Integer>> stacks = buildStacks(array, 0, length);
        mergeStacks(array, 0, length, stacks);
    }
}