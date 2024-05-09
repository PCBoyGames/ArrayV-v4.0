package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class OddEvenPairwiseSortParallel extends Sort {

    public OddEvenPairwiseSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Odd-Even Pairwise (Parallel)");
        this.setRunAllSortsName("Parallel Odd-Even Pairwise Sorting Network");
        this.setRunSortName("Parallel Odd-Even Pairwise Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }
    
    private int[] array;
    private double delay = 0.5;
    
    private class SortThread extends Thread {
        private int start, stop, gap;
        SortThread(int start, int stop, int gap) {
            this.start = start;
            this.stop = stop;
            this.gap = gap;
        }
        public void run() {
            OddEvenPairwiseSortParallel.this.pairwiserecursive2(this.start, this.stop, this.gap);
        }
    }

    private class MergeThread extends Thread {
        private int start, stop, gap, depth;
        MergeThread(int start, int stop, int gap, int depth) {
            this.start = start;
            this.stop = stop;
            this.gap = gap;
            this.depth = depth;
        }
        public void run() {
            OddEvenPairwiseSortParallel.this.pairwisemerge2(this.start, this.stop, this.gap, this.depth);
        }
    }
    
    private void pairwisemerge2(int start, int end, int gap, int depth) {
        if (start >= end - gap) return;
        if ((end - start)/gap <= depth) return;
        MergeThread left = new MergeThread(start, end, gap, 2*depth);
        MergeThread right = new MergeThread(start+(gap*depth), end, gap, 2*depth);
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } 
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int a = start+(gap*depth);
        while (a < end) {
            if (Reads.compareIndices(array, a - (gap * (depth - 1)), a, delay, true) > 0) {
                Writes.swap(array, a - (gap * (depth - 1)), a, delay, true, false);
            }
            a += (gap * depth);
        }
    }
    
    private void pairwiserecursive2(int start, int end, int gap) {
        if (start == end - gap) return;
        int b = start + gap;
        while (b < end) {
            if (Reads.compareIndices(array, b - gap, b, delay, true) > 0)
                Writes.swap(array, b - gap, b, delay, true, false);
            b += (2 * gap);
        }
        SortThread left, right;
        if (((end - start) / gap) % 2 == 0) {
            left = new SortThread(start, end, gap * 2);
            right = new SortThread(start + gap, end + gap, gap * 2);
        } else {
            left = new SortThread(start, end + gap, gap * 2);
            right = new SortThread(start + gap, end, gap * 2);
        }
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } 
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.pairwisemerge2(start, end, gap, 2);
    }
    
    public void customSort(int[] array, int start, int end) {
        this.array = array;
        this.pairwiserecursive2(start, end, 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
