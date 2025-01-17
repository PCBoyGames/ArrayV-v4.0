package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2019 Morwenn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class PoplarHeapSort extends Sort {
    public PoplarHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Poplar Heap");
        this.setRunAllSortsName("Poplar Heap Sort");
        this.setRunSortName("Poplar Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    ////////////////////////////////////////////////////////////
    // Generic helper functions
    ////////////////////////////////////////////////////////////

    // Returns 2^floor(log2(n)), assumes n > 0
    private static int hyperfloor(int n) {
        return (int) Math.pow(2, Math.floor(Math.log(n) / Math.log(2)));
    }

    // Insertion sort which doesn't check for empty sequences
    private void unchecked_insertion_sort(int[] array, int first, int last, boolean shuffle) {
        for (int cur = first + 1; cur != last; ++cur) {
            int sift = cur;
            int sift_1 = cur - 1;

            // Compare first so we can avoid 2 moves for
            // an element already positioned correctly
            if (Reads.compareValues(array[sift], array[sift_1]) == -1) {
                int tmp = array[sift];
                do {
                    Writes.write(array, sift, array[sift_1], shuffle ? 0 : 0.25, true, false);
                } while (--sift != first && Reads.compareValues(tmp, array[--sift_1]) == -1);
                Writes.write(array, sift, tmp, shuffle ? 0 : 0.25, true, false);
            }
        }
    }

    private void insertion_sort(int[] array, int first, int last, boolean shuffle) {
        if (first == last) return;
        unchecked_insertion_sort(array, first, last, shuffle);
    }

    ////////////////////////////////////////////////////////////
    // Poplar heap specific helper functions
    ////////////////////////////////////////////////////////////

    private void sift(int[] array, int first, int size, boolean shuffle) {
        if (size < 2) return;

        int root = first + (size - 1);
        int child_root1 = root - 1;
        int child_root2 = first + (size / 2 - 1);

        while (true) {
            int max_root = root;
            if (Reads.compareValues(array[max_root], array[child_root1]) == -1) {
                max_root = child_root1;
            }
            if (Reads.compareValues(array[max_root], array[child_root2]) == -1) {
                max_root = child_root2;
            }
            if (max_root == root) return;

            Writes.swap(array, root, max_root, shuffle ? 0 : 0.75, true, false);
            Highlights.clearMark(2);

            size /= 2;
            if (size < 2) return;

            root = max_root;
            child_root1 = root - 1;
            child_root2 = max_root - (size - size / 2);
        }
    }

    private void pop_heap_with_size(int[] array, int first, int last, int size) {
        int poplar_size = PoplarHeapSort.hyperfloor(size + 1) - 1;
        int last_root = last - 1;
        int bigger = last_root;
        int bigger_size = poplar_size;

        // Look for the bigger poplar root
        int it = first;
        while (true) {
            int root = it + poplar_size - 1;
            if (root == last_root) break;
            if (Reads.compareValues(array[bigger], array[root]) == -1) {
                bigger = root;
                bigger_size = poplar_size;
            }
            it = root + 1;

            size -= poplar_size;
            poplar_size = PoplarHeapSort.hyperfloor(size + 1) - 1;
        }

        // If a poplar root was bigger than the last one, exchange
        // them and sift
        if (bigger != last_root) {
            Writes.swap(array, bigger, last_root, 0.75, true, false);
            Highlights.clearMark(2);
            this.sift(array, bigger - (bigger_size - 1), bigger_size, false);
        }
    }

    private void make_heap(int[] array, int first, int last, boolean shuffle) {
        int size = last - first;
        if (size < 2) return;

        // A sorted collection is a valid poplar heap; whenever the heap
        // is small, using insertion sort should be faster, which is why
        // we start by constructing 15-element poplars instead of 1-element
        // ones as the base case
        int small_poplar_size = 15;
        if (size <= small_poplar_size) {
            this.unchecked_insertion_sort(array, first, last, shuffle);
            return;
        }

        // Determines the "level" of the poplars seen so far; the log2 of this
        // variable will be used to make the binary carry sequence
        int poplar_level = 1;

        int it = first;
        int next = it + small_poplar_size;
        while (true) {
            // Make a 15 element poplar
            this.unchecked_insertion_sort(array, it, next, shuffle);

            int poplar_size = small_poplar_size;

            // Bit trick iterate without actually having to compute log2(poplar_level)
            for (int i = (poplar_level & (0 - poplar_level)) >> 1; i != 0; i >>= 1) {
                it -= poplar_size;
                poplar_size = 2 * poplar_size + 1;
                this.sift(array, it, poplar_size, shuffle);
                ++next;
            }

            if ((last - next) <= small_poplar_size) {
                this.insertion_sort(array, next, last, shuffle);
                return;
            }

            it = next;
            next += small_poplar_size;
            ++poplar_level;
        }
    }

    private void sort_heap(int[] array, int first, int last) {
        int size = last - first;
        if (size < 2) return;

        do {
            this.pop_heap_with_size(array, first, last, size);
            --last;
            --size;
        } while (size > 1);
    }

    public void poplarHeapify(int[] array, int start, int length, boolean shuffle) {
        this.make_heap(array, start, length, shuffle);
    }

    public void heapSort(int[] array, int start, int end) {
        this.make_heap(array, start, end, false);
        this.sort_heap(array, start, end);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.heapSort(array, 0, length);
    }
}