package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryDoubleInsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/**
 * Miepurge Sort: Min Heap Merge Sort, taking O(n+k) aux,
 * and slightly less time when finding mins compared to BaseNMerge
 */

final public class MiepurgeSort extends Sort {
    public MiepurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Miepurge");
        this.setRunAllSortsName("Miepurge Sort (K-Way Heap Merge Sort)");
        this.setRunSortName("Miepurge Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setQuestion("Set the miepurge base of the sort:", 16);
        this.setBogoSort(false);
    }
    public BinaryDoubleInsertionSort smallSort;
    public int threshold;
    private int tabComp(int[] array, int[] table, int a, int b) {
        return Reads.compareValues(array[table[a]], array[table[b]]);
    }
    private void sift(int[] array, int root, int[] table) {
        while(root <= table.length / 2) {
            int leaf = root * 2;
            if(leaf < table.length && tabComp(array, table, leaf-1, leaf) == 1)
                leaf++;
            if(tabComp(array, table, root-1, leaf-1) == 1) {
                Highlights.markArray(1, root-1);
                Highlights.markArray(2, leaf-1);
                Writes.swap(table, root-1, leaf-1, 5, true, true);
                root = leaf;
            } else break;
        }
    }
    private void heapifyTable(int[] array, int[] table) {
        for(int i = table.length / 2; i > 0; i--)
            sift(array, i, table);
    }
    private int[] remove(int[] arr, int index) {
        int[] nw = Writes.createExternalArray(arr.length - 1);
        for(int i=0, j=0; i<arr.length; i++) {
            if(i == index)
                continue;
            Writes.write(nw, j++, arr[i], 0, false, true);
        }
        Writes.deleteExternalArray(arr);
        return nw;
    }
    private int heapIndex(int[] array, int[] ptrs, int[] table, int index) {
        int i = 0;
        while(i < ptrs.length & ptrs[i] <= table[index])
            i++;
        return i;
    }
    private void kWayMerge(int[] array, int... ptrs) {
        int[] tmp = Writes.createExternalArray(ptrs[ptrs.length - 1] - ptrs[0]),
              table = Writes.createExternalArray(ptrs.length - 1);
        int k = 0;
        Writes.arraycopy(ptrs, 0, table, 0, table.length, 1, true, true);
        this.heapifyTable(array, table);
        while(table.length > 1) {
            int now = heapIndex(array, ptrs, table, 0),
                secondMin = table.length > 2 ?
                (tabComp(array, table, 1, 2) == 1 ? 2 : 1)
                : 1;
            do {
                if(table[0] < ptrs[now]) {
                    Highlights.markArray(3, table[0]);
                    Writes.write(tmp, k++, array[table[0]++], 1, true, true);
                }
            } while(table[0] < ptrs[now] && tabComp(array, table, 0, secondMin) <= 0);
            if(table[0] >= ptrs[now]) {
                Writes.swap(table, 0, table.length-1, 5, true, true);
                table = remove(table, table.length-1);
            }
            this.sift(array, 1, table);
        }
        int lastKey = heapIndex(array, ptrs, table, 0);
        while(table[0] < ptrs[lastKey]) {
            Highlights.markArray(1, table[0]);
            Writes.write(tmp, k++, array[table[0]++], 1, true, true);
        }
        Writes.deleteExternalArray(table);
        Writes.arraycopy(tmp, 0, array, ptrs[0], tmp.length, 1, true, false);
        Writes.deleteExternalArray(tmp);
    }
    public void miepurge(int[] array, int start, int end, int base) {
        int[] aux = new int[base + 1];
        int step = (end - start + 1) / base;
        if(end-start < base) {
            MaxHeapSort mhs = new MaxHeapSort(arrayVisualizer);
            mhs.customHeapSort(array, start, end, 0.1);
            return;
        }
        if(step == 0 || end-start == base) {
            this.smallSort.customDoubleInsert(array, start, end, 0.25);
            return;
        }
        for(int i = 0; i < base; i++) {
            aux[i] = start + step * i;
        }
        aux[base] = end;

        for(int i=0; i<base; i++) {
            if(base < 24 && step > threshold)
                this.miepurge(array, aux[i], aux[i+1], base);
            else
                this.smallSort.customDoubleInsert(array, aux[i], aux[i+1], 0.25);
        }
        this.kWayMerge(array, aux);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.smallSort = new BinaryDoubleInsertionSort(arrayVisualizer);
        threshold = (int) Math.pow(length, 21 / 28d); // ?????
        this.setRunAllSortsName("Miepurge Sort (K-Way Heap Merge Sort), Base " + bucketCount);
        this.miepurge(array, 0, length, bucketCount);
    }
}