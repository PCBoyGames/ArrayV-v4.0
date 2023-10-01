package sorts.merge;

import java.lang.Math;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
thanks to aphitorite, who created the "lazyMerge" and "inPlaceMerge2" functions
*/

public class BufMergeSort extends Sort {
    public BufMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buf Merge");
        this.setRunAllSortsName("thatsOven's In-Place Buffered Merge Sort");
        this.setRunSortName("thatsOven's In-Place Buffered Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int ceilNearestPowerOfTwo(int n) {
        int v = n;
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        int x = v >> 1;
        return x;
    }

    public int binarySearch(int[] array, int start, int end, int value, boolean left) {
		int lo = start, hi = end;

		while (lo < hi) {
			int mid = lo + ((hi - lo) / 2);
			Highlights.markArray(4, mid);

			Delays.sleep(1);
			if (Reads.compareValues(value, array[mid]) < (left ? 1 : 0)) {
				hi = mid;
			}
			else {
				lo = mid + 1;
			}
		}

		Highlights.clearMark(4);
		return lo;
	}

    public int floorLog(int value) {
        int result = -1;
        for (int i = 1; i < value; ++result, i <<= 1);
        return result;
    }

    public boolean binarySearchThreshold(int a, int b) {
        if (a > b) {
            return a/b > this.floorLog(a);
        } else {
            return b/a > this.floorLog(b);
        }
    }

    public void lazyMerge(int[] array, int a, int m, int b, int bufferPos) {
        if (b-m<1) return;
        int j = bufferPos;
        for (int i = m; i < b; i++, j++) {
            Writes.swap(array, i, j, 1, true, false);
        }
        j--;
        int i = m-1;
        int index = b-1;
        while (i >= a && j >= bufferPos) {
            if (this.binarySearchThreshold(i-a+1, index-i+1)) {
                if (Reads.compareValues(array[i], array[j]) > 0) {
                    int k = this.binarySearch(array, a, i+1, array[j], false);
                    while (i >= k) {
                        Writes.swap(array, i, index, 1, true, false);
                        i--;
                        index--;
                    }
                }
                Writes.swap(array, j, index, 1, true, false);
                j--;
                index--;
            } else {
                if (Reads.compareValues(array[i], array[j]) > 0) {
                    Writes.swap(array, i, index, 1, true, false);
                    i--;
                    index--;
                } else {
                    Writes.swap(array, j, index, 1, true, false);
                    j--;
                    index--;
                }
            }
        }
        while (j >= bufferPos) {
            Writes.swap(array, j, index, 1, true, false);
            j--;
            index--;
        }
    }

    public void rotate(int[] array, int start, int split, int end) {
        int temp;
        while ((split < end) && (split > start)) {
            if ((end-split)<(split-start)) {
                for (int i = split; i < end; i++) {
                    Writes.swap(array, i-(end-split), i, 1, true, false);
                }
                temp = end;
                end = split;
                split = (split-(temp-split));
            }
            else{
                for (int i = start; i < split; i++) {
                    Writes.swap(array, i, i+(split-start), 1, true, false);
                }
                temp = start;
                start = split;
                split = (split+(split-temp));
            }
        }
    }

    public void inPlaceMerge2(int[] array, int start, int mid, int end) {
        int i = start, m = mid, k = mid, q;
        while (m < end) {
            if (array[m-1] <= array[m])
                return;
            while (i < m-1 && array[i] <= array[m]) i++;
            Writes.swap(array, i++, k++, 1, true, false);
            while (i < m) {
                while (i < m && k < end && array[m] > array[k])
                    Writes.swap(array, i++, k++, 1, true, false);
                if (i >= m) break;
                else if (k >= end) {
                    rotate(array, i, m, end);
                    return;
                }
                else if (k-m >= m-i) {
                    rotate(array, i, m, k);
                    break;
                }
                q = m;
                while (i < m && q < k && array[q] <= array[k])
                    Writes.swap(array, i++, q++, 1, true, false);
                rotate(array, m, q, k);
            }
            m = k;
        }
    }

    public int mergeRight(int[] arr, int start, int mid, int end, int bufferpoint) {
        int i = bufferpoint;
        int j = start;
        int k = mid;
        while (j < mid && k < end) {
            if (Reads.compareValues(arr[j], arr[k]) < 0) {
                Writes.swap(arr, i, j, 1, true, false);
                j++;
            } else {
                Writes.swap(arr, i, k, 1, true, false);
                k++;
            }
            i++;
        }
        while (k < end) {
            Writes.swap(arr, i, k, 1, true, false);
            i++;
            k++;
        }
        while (j < mid) {
            Writes.swap(arr, j, i, 1, true, false);
            i++;
            j++;
        }
        return i;
    }

    public int mergeLeft(int[] arr, int end, int mid, int start, int bufferpoint) {
        int i = bufferpoint;
        int j = mid;
        int k = end;
        while (j > start && k > mid) {
            if (Reads.compareValues(arr[j], arr[k]) > 0) {
                Writes.swap(arr, i, j, 1, true, false);
                j--;
            } else {
                Writes.swap(arr, i, k, 1, true, false);
                k--;
            }
            i--;
        }
        while (k > mid) {
            Writes.swap(arr, i, k, 1, true, false);
            i--;
            k--;
        }
        while (j > start) {
            Writes.swap(arr, i, j, 1, true, false);
            i--;
            j--;
        }
        return i;
    }

    public void mergeWithoutBuffer(int[] arr, int start, int mid, int end, int sqrtn) {
        this.lazyMerge(arr, start+sqrtn, mid, mid+sqrtn, start);
        for (int i = mid+sqrtn; i < end; i += sqrtn) {
            if (i+sqrtn < end) {
                this.lazyMerge(arr, start+sqrtn, i, i+sqrtn, start);
            } else {
                this.lazyMerge(arr, start+sqrtn, i, end, start);
            }
        }
    }

    public void bufferedMergeSort(int[] arr, int start, int end) {
        int n = end-start;
        if (n <= 1) return;
        for (int i = start; i < end-1; i += 2) {
            if (Reads.compareValues(arr[i], arr[i+1]) > 0) {
                Writes.swap(arr, i, i+1, 1, true, false);
            }
        }
        boolean mergedleft = false;
        int step = 2;
        int bufferpoint = start;
        while (step <= n) {
            bufferpoint = start;
            for (int i = start+step; i < end; i += (step*2)) {
                if (i + (step*2) < end) {
                    bufferpoint = this.mergeRight(arr, i, i+step, i+(step*2), bufferpoint);
                }
            }
            step *= 2;
            if (step > n) break;
            bufferpoint = end-1;
            for (int i = end-step; i > start; i -= (step*2)) {
                if (i - (step*2) >= start) {
                    bufferpoint = this.mergeLeft(arr, i-1, i-step-1, i-(step*2)-1, bufferpoint);
                }
            }
            step *= 2;
            if (step > n) {
                mergedleft = true;
                break;
            }
        }
        step /= 4;
        int sqrtn = (int)(Math.sqrt(n));
        if (!mergedleft) {
            this.bufferedMergeSort(arr, end-step, end);
            this.mergeWithoutBuffer(arr, start, end-step, end, sqrtn);
        } else {
            this.bufferedMergeSort(arr, start, start+step);
            this.mergeWithoutBuffer(arr, start, start+step, end, sqrtn);
        }
        this.bufMergeRun(arr, start, start+sqrtn);
        this.inPlaceMerge2(arr, start, start+sqrtn, end);
    }

    public void runNonPowTwo(int[] arr, int start,int end, int currentLength, int reducedLength) {
        if (currentLength <= 1) {
            return;
        }
        this.bufferedMergeSort(arr, start, start+reducedLength);
        this.bufMergeRun(arr, start+reducedLength, end);
        int sqrtn = (int)(Math.sqrt(currentLength));
        for (int i = start+reducedLength; i < end; i += sqrtn) {
            if (i+sqrtn < end) {
                this.lazyMerge(arr, start+sqrtn, i, i+sqrtn, start);
            } else {
                this.lazyMerge(arr, start+sqrtn, i, end, start);
            }
        }
        this.bufMergeRun(arr, start, start+sqrtn);
        this.inPlaceMerge2(arr, start, start+sqrtn, end);
    }

    public void bufMergeRun(int[] array, int start, int end) {
        int currentLength = end-start;
        int reducedLength = this.ceilNearestPowerOfTwo(currentLength);
        if (reducedLength*2 != currentLength) {
            this.runNonPowTwo(array, start, end, currentLength, reducedLength);
        } else {
            this.bufferedMergeSort(array, start, end);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.bufMergeRun(array, 0, currentLength);
    }
}