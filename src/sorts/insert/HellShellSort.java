package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SHELL SHELL SHELL SHELL SH -
------------------------------

*/
public class HellShellSort extends Sort {

    // const SMALL_SORT_SIZE: usize = 32;
    int smallSortSize = 32;

    public HellShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Hell Shell");
        this.setRunAllSortsName("Hell Shell Sort");
        this.setRunSortName("Hell Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /*fn insertion_subarray_sort<T: Copy + PartialOrd + std::fmt::Debug>(
        arr: &mut [T],
        start: usize,
        jump: usize,
    ) {
    // sorts a subarray using insertion sort
        for i in (start..arr.len()).step_by(jump) {
            let num: T = arr[i];
            let mut tmp = i;
            while tmp >= jump && num < arr[tmp - jump] {
                arr[tmp] = arr[tmp - jump];
                tmp -= jump;
            }
            arr[tmp] = num;
        }
    }*/
    protected void insertionSubarray(int[] array, int start, int end, int jump) {
        for (int i = start; i < end; i += jump) {
            int num = array[i];
            int tmp = i;
            boolean changes = false;
            while (tmp >= jump && Reads.compareValues(num, array[tmp - jump]) < 0) {
                Writes.write(array, tmp, array[tmp - jump], 2, changes = true, false);
                tmp -= jump;
            }
            if (changes) Writes.write(array, tmp, num, 2, true, false);
        }
    }

    /*fn shell_shell_sort_recursive<T: Copy + PartialOrd + std::fmt::Debug>(
        arr: &mut [T],
        start: usize,
        jump: usize,
    ) {
        let mut inner_jump = (arr.len() - start) / jump;
        if inner_jump < SMALL_SORT_SIZE {
            insertion_subarray_sort(arr, start, jump);
            return;
        }
        inner_jump /= 2;
        while inner_jump > 1 {
            for i in (start..(inner_jump * jump)).step_by(jump) {
                shell_shell_sort_recursive(arr, i, jump * inner_jump);
            }
            inner_jump /= 2;
        }
        insertion_subarray_sort(arr, start, jump);
    }*/
    protected void hellShellRec(int[] array, int start, int end, int jump, int depth) {
        Writes.recordDepth(depth);
        int innerJump = (int) (end - start - start) / jump;
        if (innerJump < smallSortSize) {
            insertionSubarray(array, start, end, jump);
            return;
        }
        innerJump /= 2;
        while (innerJump > 1) {
            for (int i = start; i < innerJump * jump; i += jump) {
                Writes.recursion();
                hellShellRec(array, i, end, jump * innerJump, depth + 1);
            }
            innerJump /= 2;
        }
        insertionSubarray(array, start, end, jump);
    }

    /*
    fn shell_shell_sort<T: Copy + PartialOrd + std::fmt::Debug>(arr: &mut [T]) {
        shell_shell_sort_recursive(arr, 0, 1)
    }
    */
    @Override
    public void runSort(int[] array, int currentLength, int base) {
        hellShellRec(array, 0, currentLength, 1, 0);
    }
}