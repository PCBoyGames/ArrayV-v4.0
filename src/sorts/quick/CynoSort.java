package sorts.quick;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class CynoSort extends Sort {

    public CynoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cyno");
        this.setRunAllSortsName("Cyno Sort");
        this.setRunSortName("Cynosort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class Partition implements Comparable<Partition> {
        public int a, b;
        public int divisor;

        public Partition(int a, int b, int divisor) {
            this.a = a;
            this.b = b;
            this.divisor = divisor;
        }

        public int length() {
            return this.b - this.a;
        }

        @Override
        public int compareTo(Partition y) {
            int len0 = this.length(), len1 = y.length();
            if (len0 < len1)
                return 1;
            if (len0 > len1)
                return -1;
            return 0;
        }
    }

    int threshold = 32;

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    void consumePartition(int[] array, PriorityQueue<Partition> queue, int a, int b, int divisor) {
        if (b - a > threshold)
            queue.offer(new Partition(a, b, divisor));
        else
            insertSort(array, a, b);
    }

    void innerSort(int[] array, int left, int right) {
        if (right - left <= this.threshold) {
            insertSort(array, left, right);
            return;
        }
        PriorityQueue<Partition> queue = new PriorityQueue<>((right - left - 1) / this.threshold + 1);
        queue.offer(new Partition(left, right, 3));
        while (queue.size() > 0) {
            Partition part = queue.poll();
            int a = part.a, b = part.b, n = part.length(), d = part.divisor;
            int piv1, piv2, s = n / d;
            if (Reads.compareValues(array[a+s], array[b-1-s]) > 0) {
                piv1 = array[b-1-s];
                piv2 = array[a+s];
            } else {
                piv1 = array[a+s];
                piv2 = array[b-1-s];
            }

            int i1 = a, i = a, j = b, j1 = b;

            for (int k = i; k < j; k++) {
                int cmp1 = Reads.compareIndexValue(array, k, piv1, 0.5, true);

                if (cmp1 <= 0) {
                    int t = array[k];

                    Writes.write(array, k, array[i], 0.25, true, false);
                    if (cmp1 == 0) {
                        Writes.write(array, i, array[i1], 0.25, true, false);
                        Writes.write(array, i1++, t, 0.25, true, false);
                    } else Writes.write(array, i, t, 0.25, true, false);
                    i++;
                }
                else {
                    int cmp2 = Reads.compareIndexValue(array, k, piv2, 0.5, true);

                    if (cmp2 >= 0) {
                        while (--j > k) {
                            int cmp = Reads.compareIndexValue(array, j, piv2, 0.5, true);
                            if (cmp == 0) Writes.swap(array, --j1, j, 0.5, true, false);
                            else if (cmp < 0) break;
                        }
                        Highlights.clearMark(2);

                        int t = array[k];

                        Writes.write(array, k, array[j], 0.25, true, false);
                        if (cmp2 == 0) {
                            Writes.write(array, j, array[--j1], 0.25, true, false);
                            Writes.write(array, j1, t, 0.25, true, false);
                        } else Writes.write(array, j, t, 0.25, true, false);

                        cmp1 = Reads.compareIndexValue(array, k, piv1, 0.5, true);

                        if (cmp1 <= 0) {
                            t = array[k];

                            Writes.write(array, k, array[i], 0.25, true, false);
                            if (cmp1 == 0) {
                                Writes.write(array, i, array[i1], 0.25, true, false);
                                Writes.write(array, i1++, t, 0.25, true, false);
                            } else Writes.write(array, i, t, 0.25, true, false);
                            i++;
                        }
                    }
                }
            }
            if (i1 == b) continue;

            if (Math.min(i-a, Math.min(j-i, b-j)) <= threshold) d++;
            consumePartition(array, queue, i, j, d);

            if (i1 - a > i - i1) {
                int i2 = i;
                i = a;
                while (i1 < i2) Writes.swap(array, i++, i1++, 1, true, false);
            } else while (i1 > a) Writes.swap(array, --i, --i1, 1, true, false);

            consumePartition(array, queue, a, i, d);

            if (b - j1 > j1 - j) {
                int j2 = j;
                j = b;
                while (j1 > j2) Writes.swap(array, --j, --j1, 1, true, false);
            } else while (j1 < b) Writes.swap(array, j++, j1++, 1, true, false);

            consumePartition(array, queue, j, b, d);
        }
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        innerSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
