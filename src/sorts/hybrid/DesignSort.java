package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;

public class DesignSort extends Sort {
    public DesignSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Design");
        this.setRunSortName("Designsort");
        this.setRunAllSortsName("Design Sort");
        this.setCategory("Hybrid Sorts");
        this.setBogoSort(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setComparisonBased(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
    }

    static final int grow = 3;

    private int r0S, r0E, r1Sz;
    private BinaryInsertionSort inserter;

    private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    private void rotate(int[] array, int loc, int lenA, int lenB) {
        if(lenA <= 0 || lenB <= 0)
            return;
        if(lenA >= lenB) {
            this.multiSwap(array, loc, loc+lenA, lenB);
            loc += lenB;
            int sPos = loc + lenA - 2*lenB;
            int i=sPos;
            for(; i >= loc; i -= lenB) {
                this.multiSwap(array, i, i+lenB, lenB);
            }
            int leftOver = (i + lenB) - loc;
            this.rotate(array, loc, leftOver, lenB);
        } else {
            int i = loc+lenB;
            for(; i>=loc+lenA; i-=lenA) {
                this.multiSwap(array, loc, i, lenA);
            }
            int leftOver = i - loc;
            this.rotate(array, loc, lenA, leftOver);
        }
    }
    public int mergeBinSearch(int[] array, int start, int end, int key, boolean isLeft) {
        int l = -1, r = end-start;
        while(l < r - 1) {
            int mi = l + (r - l) / 2,
                comp = Reads.compareValues(array[start + mi], key);
            if(comp == 1 || (isLeft && comp == 0)) {
                r = mi;
            } else {
                l = mi;
            }
        }
        return r;
    }
    public int exponentialRight(int[] array, int start, int end, int key, double sleep) {
        int prev = 1;
        int length = end-start;
        while(prev < length && Reads.compareValues(array[prev+start], key) == -1) {
            prev *= grow;
            Highlights.markArray(1, prev+start);
            Delays.sleep(sleep);
        }
        if(prev > length) {
            prev = length;
        }
        return (prev/grow)+start;
    }
    public int exponentialLeft(int[] array, int start, int end, int key, double sleep) {
        int prev = 1;
        int length = end-start;
        while(prev < length && Reads.compareValues(array[end-prev], key) == 1) {
            prev *= grow;
            if(end-prev>=start)
                Highlights.markArray(1, end-prev);
            Delays.sleep(sleep);
        }
        if(prev > length) {
            prev = length;
        }
        return end-(prev/grow);
    }
    public int oopBinSearch(int[] array, int start, int end, int key, boolean isLeft) {
        int s = (int) Math.sqrt(end-start);
        if(!isLeft && Reads.compareValues(array[start+s], key) > 0) {
            return exponentialRight(array, start, end, key, 0.125);
        } else if(isLeft && Reads.compareValues(array[end-s], key) <= 0) {
            return exponentialLeft(array, start, end, key, 0.125);
        }
        while(start < end) {
            int mi = start + (end - start) / 2,
                comp = Reads.compareValues(array[mi], key);
            if(comp == 1 || (isLeft && comp == 0)) {
                end = mi;
            } else {
                start = mi + 1;
            }
        }
        return start;
    }
    private void mergeNoBuf(int[] array, int start, int mid, int end) {
        if(start == mid || mid == end)
            return;
        if(Reads.compareValues(array[mid-1], array[mid]) < 0) {
            return;
        }
        if(Reads.compareValues(array[start], array[end-1]) == 1) {
            this.rotate(array, start, mid-start, end-mid);
            return;
        }
        if(mid - start < end - mid) {
            int l = start, r = mid;
            while(l < r && r < end) {
                int search = this.mergeBinSearch(array, r, end, array[l], true);
                if(search > 0) {
                    this.rotate(array, l, r-l, search);
                    l += search + 1;
                    r += search;
                } else {
                    l++;
                }
            }
        } else {
            int l = mid, r = end;
            while(l < r) {
                int search = start + this.mergeBinSearch(array, start, l, array[r-1], false);
                if(search < l) {
                    this.rotate(array, search, l-search, r-l);
                    r -= l-search+1;
                    l = search;
                } else {
                    r--;
                }
                if(l <= start)
                    return;
            }
        }
    }
    private int findSortedRun(int[] array, int start, int end, boolean fw) {
        if(fw) {
            while(start < end - 1) {
                Highlights.markArray(1, start);
                Delays.sleep(1);
                if(Reads.compareValues(array[start], array[start+1]) > 0)
                    return start+1;
                start++;
            }
            return end;
        } else {
            end--;
            while(end > start + 1) {
                Highlights.markArray(1, end);
                Delays.sleep(1);
                if(Reads.compareValues(array[end-1], array[end]) > 0)
                    return end;
                end--;
            }
            return start;
        }
    }
    private void mergeAd(int[] array, int start, int mid, int end) {
        if(end-start == 0)
            return;
        else if(end-start == 1) {
            if(Reads.compareValues(array[start], array[end]) == 1) {
                Writes.swap(array, start, end, 1, true, false);
            }
            return;
        } else if(mid-start <= r0E-r0S) {
            this.multiSwap(array, r0S, start, mid-start);
            int l = r0S, lE = l + mid-start, r = mid, t = start;
            while(r < end && l < lE) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, t++, l++, 1, true, false);
                } else {
                    Writes.swap(array, t++, r++, 1, true, false);
                }
            }
            while(l < lE) {
                Writes.swap(array, t++, l++, 1, true, false);
            }
        } else if(end-mid <= r0E-r0S) {
            this.multiSwap(array, r0S, mid, end-mid);
            int l = mid-1, r = r0S+end-mid-1, t = end-1;
            while(r >= r0S && l >= start) {
                if(Reads.compareValues(array[l], array[r]) > 0) {
                    Writes.swap(array, t--, l--, 1, true, false);
                } else {
                    Writes.swap(array, t--, r--, 1, true, false);
                }
            }
            while(r >= r0S) {
                Writes.swap(array, t--, r--, 1, true, false);
            }
        } else {
            if(Reads.compareValues(array[mid-1], array[mid]) < 0) {
                return;
            }
            if(Reads.compareValues(array[start], array[end-1]) == 1) {
                this.rotate(array, start, mid-start, end-mid);
                return;
            }
            start = oopBinSearch(array, start, mid, array[mid], false);
            end = oopBinSearch(array, mid, end, array[mid-1], true);

            if(mid-start < end-mid) {
                int[] temp = Writes.createExternalArray(mid-start);
                Writes.arraycopy(array, start, temp, 0, mid-start, 1, true, true);
                int l = 0, lE = mid-start, r = mid, t = start;
                while(r < end && l < lE) {
                    if(Reads.compareValues(temp[l], array[r]) <= 0) {
                        Writes.write(array, t++, temp[l++], 1, true, false);
                    } else {
                        Writes.write(array, t++, array[r++], 1, true, false);
                    }
                }
                while(l < lE) {
                    Writes.write(array, t++, temp[l++], 1, true, false);
                }
                Writes.deleteExternalArray(temp);
            } else {
                int[] temp = Writes.createExternalArray(end-mid);
                Writes.arraycopy(array, mid, temp, 0, end-mid, 1, true, true);
                int l = mid-1, r = end-mid-1, t = end-1;
                while(r >= 0 && l >= start) {
                    if(Reads.compareValues(array[l], temp[r]) > 0) {
                        Writes.write(array, t--, array[l--], 1, true, false);
                    } else {
                        Writes.write(array, t--, temp[r--], 1, true, false);
                    }
                }
                while(r >= 0) {
                    Writes.write(array, t--, temp[r--], 1, true, false);
                }
                Writes.deleteExternalArray(temp);
            }
        }
    }
    private void designMerge(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        if(start == mid)
            return;
        if(end-start < Math.min(r0E-r0S, r1Sz)) {
            this.inserter.customBinaryInsert(array, start, end, 0.1);
             return;
        }
        this.designMerge(array, start, mid);
        this.designMerge(array, mid, end);
        this.mergeAd(array, start, mid, end);
    }
    private void fallbackMerge(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        if(start == mid)
            return;
        if(end-start < 64) {
            this.inserter.customBinaryInsert(array, start, end, 0.1);
            return;
        }
        this.fallbackMerge(array, start, mid);
        this.fallbackMerge(array, mid, end);
        this.mergeNoBuf(array, start, mid, end);
    }
    public void design(int[] array, int start, int end) {
        int run0 = findSortedRun(array, start, end, true);
        if(run0 == end)
            return;
        r0S = start;
        r0E = run0;
        int run1 = findSortedRun(array, run0, end, false);
        r1Sz = end - run1;
        this.designMerge(array, run0, run1);
        if(run0 > start+1 && run0 != run1) {
            this.fallbackMerge(array, start, Math.min(start + (run1-run0)/2, run0));
        }
        this.mergeNoBuf(array, start, run0, run1);
        this.mergeNoBuf(array, start, run1, end);
    }
    @Override
    public void runSort(int[] array, int len, int bucket) {
        this.inserter = new BinaryInsertionSort(arrayVisualizer);
        this.design(array, 0, len);
    }
}
