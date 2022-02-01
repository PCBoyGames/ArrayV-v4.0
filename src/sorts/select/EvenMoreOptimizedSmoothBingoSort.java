package sorts.select;

import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// #4 of Distray's Pop The Top Lineup
final public class EvenMoreOptimizedSmoothBingoSort extends Sort {
    public EvenMoreOptimizedSmoothBingoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Even More Optimized Smooth Bingo");
        this.setRunAllSortsName("Even More Optimized Smooth Bingo Sort");
        this.setRunSortName("Even More Optimized Smooth Bingo Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int seek = 0;
        Stack<Integer> equals = new Stack<>();
        for(int i=length-1; i>seek;) {
            int equal;
            if(equals.empty()) {
                equal = 0;
            } else {
                Writes.changeAllocAmount(-1);
                equal = equals.pop();
            }
            for(int j=seek+1; j<=i; j++) {
                int c = Reads.compareIndices(array, j, seek, 0.1, true);
                if(c >= 0) {
                    if(j != ++seek)
                        Writes.swap(array, j, seek, 0.5, true, false);
                    if(c == 0) {
                        equal++;
                    } else {
                        Writes.changeAuxWrites(1);
                        Writes.changeAllocAmount(1);
                        equals.push(equal);
                        equal = 0;
                    }
                }
            }
            if(seek >= i) {
                Writes.clearAllocAmount();
                return;
            }
            do {
                Writes.swap(array, seek--, i--, 1, true, false);
            } while(equal-- > 0);
            if(seek < 0) seek = 0;
        }
    }
}