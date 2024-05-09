package sorts.quick;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Finally, an accurate way to get a near-perfect halving pivot!

*/
public class TernaryCountingDualPivotQuickSort extends MadhouseTools {
    public TernaryCountingDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Counting Dual Pivot Quick");
        this.setRunAllSortsName("Ternary Counting Dual Pivot Quick Sort");
        this.setRunSortName("Ternary Counting Dual Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if (a != b) Writes.swap(array, a, b, pause, mark, auxwrite);
    }

    protected int[] getPivots(int[] array, int start, int end) {
        // Counting
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = start; i < end; i++) {
            int idx = values.indexOf(stableReturn(array[i]));
            if (idx != -1) {
                int get = times.get(idx);
                times.set(idx, get + 1);
                Writes.auxWrites++;
            } else {
                values.add(stableReturn(array[i]));
                Writes.allocAmount++;
                Writes.auxWrites++;
                times.add(1);
                Writes.allocAmount++;
                Writes.auxWrites++;
            }
            Highlights.markArray(1, i);
            Delays.sleep(1);
        }

        // Shell
        int[] gs = {1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156, 2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018};
        for (int g = gs.length - 1; g >= 0; g--) {
            while (1.68 * gs[g] >= times.size()) g--;
            int gap = gs[g];
            for (int i = gap; i < times.size(); i++) {
                int j = i;
                int v = values.get(j);
                int t = times.get(j);
                boolean w = false;
                while (j >= gap && Reads.compareValues(v, values.get(j - gap)) < 0) {
                    Highlights.markArray(1, start + j);
                    Highlights.markArray(2, start + j - gap);
                    values.set(j, values.get(j - gap));
                    Writes.auxWrites++;
                    times.set(j, times.get(j - gap));
                    Writes.auxWrites++;
                    j -= gap;
                    w = true;
                    Delays.sleep(1);
                }
                if (w) {
                    Highlights.clearMark(2);
                    Highlights.markArray(1, start + j);
                    values.set(j, v);
                    Writes.auxWrites++;
                    times.set(j, t);
                    Writes.auxWrites++;
                    Delays.sleep(1);
                }
            }
        }

        // Pivot Selection
        int cnt = 0;
        int pos = 0;
        int pivot1 = Integer.MIN_VALUE;
        int pivot2 = Integer.MIN_VALUE;
        while (true) {
            cnt += times.get(pos);
            if (cnt >= (end - start) / 3 && pivot1 == Integer.MIN_VALUE) {
                // The pivots will be ternary this time, so decrementing is not exactly ideal this time.
                pivot1 = values.get(pos);
            }
            if (cnt >= (end - start) / 1.5 && pivot2 == Integer.MIN_VALUE) {
                int result = values.get(pos);
                Writes.allocAmount -= 2 * times.size();
                values.clear();
                times.clear();
                pivot2 = result;
                return new int[] {pivot1, pivot2};
            }
            pos++;
        }
    }

    protected int[] partition(int[] array, int a, int b, int piv1, int piv2) {
        int i1 = a, i = a, j = b, j1 = b;
		for (int k = i; k < j; k++) {
			int cmp1 = Reads.compareIndexValue(array, k, piv1, 1, true);
			if (cmp1 <= 0) {
				int t = array[k];
				Writes.write(array, k, array[i], 0.5, true, false);
				if (cmp1 == 0) {
					Writes.write(array, i, array[i1], 0.5, true, false);
					Writes.write(array, i1++, t, 0.5, true, false);
				} else Writes.write(array, i, t, 0.5, true, false);
				i++;
			} else {
				int cmp2 = Reads.compareIndexValue(array, k, piv2, 1, true);
				if (cmp2 >= 0) {
					while (--j > k) {
						int cmp = Reads.compareIndexValue(array, j, piv2, 1, true);
						if (cmp == 0) {
                            if (j1 - 1 != j) Writes.swap(array, --j1, j, 1, true, false);
                            else j1--;
                        }
						else if (cmp < 0) break;
					}
					Highlights.clearMark(2);
					int t = array[k];
					Writes.write(array, k, array[j], 0.5, true, false);
					if (cmp2 == 0) {
						Writes.write(array, j, array[--j1], 0.5, true, false);
						Writes.write(array, j1, t, 0.5, true, false);
					} else Writes.write(array, j, t, 0.5, true, false);
					cmp1 = Reads.compareIndexValue(array, k, piv1, 1, true);
					if (cmp1 <= 0) {
						t = array[k];
						Writes.write(array, k, array[i], 0.5, true, false);
						if (cmp1 == 0) {
							Writes.write(array, i, array[i1], 0.5, true, false);
							Writes.write(array, i1++, t, 0.5, true, false);
						}
						else Writes.write(array, i, t, 0.5, true, false);
						i++;
					}
				}
			}
		}
        int iS = i;
        if (i1 - a > i - i1) {
			i = a;
			while (i1 < iS) Writes.swap(array, i++, i1++, 1, true, false);
		} else {
            while (i1 > a) {
                if (i != i1) Writes.swap(array, --i, --i1, 1, true, false);
                else {
                    i--;
                    i1--;
                }
            }
        }
        int jS = j;
        if (b - j1 > j1 - j) {
			j = b;
			while (j1 > jS) Writes.swap(array, --j, --j1, 1, true, false);
		} else {
            while (j1 < b) {
                if (j != j1) Writes.swap(array, j++, j1++, 1, true, false);
                else {
                    j++;
                    j1++;
                }
            }
        }
        return new int[] {i, iS, jS, j};
    }

    public void countingPivotQuick(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start < 16) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsertUnstable(array, start, end, 1, false);
            return;
        }
        if (isSorted(array, start, end)) return;
        int[] pivots = getPivots(array, start, end);
        int[] result = partition(array, start, end, pivots[0], pivots[1]);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, start, result[0], depth + 1);
        Highlights.clearAllMarks();
        if (pivots[1] > pivots[0]) {
            Writes.recursion();
            countingPivotQuick(array, result[1], result[2], depth);
            Highlights.clearAllMarks();
        }
        Writes.recursion();
        countingPivotQuick(array, result[3], end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        countingPivotQuick(array, 0, currentLength, 0);
    }
}