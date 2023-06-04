package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class VubuPhoneticSort extends BogoSorting {
    public VubuPhoneticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("[v\u026F\u03B2u]");
        this.setRunAllSortsName("[v\u026F\u03B2u] Sort");
          this.setRunSortName("[v\u026F\u03B2u]sort");
          this.setCategory("Bogo Sorts");
          this.setComparisonBased(true);
          this.setBucketSort(false);
          this.setRadixSort(false);
          this.setUnreasonablySlow(false);
          this.setUnreasonableLimit(0);
          this.setBogoSort(false);
    }

    // salvaged from Vutuuwu
    public void omegaPush(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, end-1, start, 0.01, true, false);
        }
    }
    public void omegaPushBW(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, start, end-1, 0.01, true, false);
        }
    }
    public void omegaPush(int[] array, int start, int end, int k) {
        if(k==0) {
            omegaPush(array, start, end);
        } else
            for(int i=0; i<end-start-1; i++) {
                omegaPushBW(array, start, end, k-1);
            }
    }
    public void omegaPushBW(int[] array, int start, int end, int k) {
        if(k==0) {
            omegaPushBW(array, start, end);
        } else
            for(int i=0; i<end-start-1; i++) {
                omegaPush(array, start, end, k-1);
            }
    }
    // O(2n-1 * (2^(n/2 + 1)))?
    private void omegaSwap(int[] array, int start, int end, int r) {
        if(start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaPush(array, start, end+1, end-start);
        this.omegaPushBW(array, start, end, end-start);
        this.omegaSwap(array, start+1, end-1, r);
        this.omegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaPush1(int[] array, int start, int end, int depth) { // Clamber-esque push, because I want it to be the worst possible.
        depth++;
        for(int j=end-1; j>=start; j--) {
            omegaSwap(array, j, end-1, depth);
        }
    }
    private void omegaOmegaPushBW1(int[] array, int start, int end, int depth) {
        depth++;
        for(int j=start+1; j<end; j++) {
            omegaSwap(array, start, j, depth);
        }
    }
    private void omegaOmegaPush(int[] array, int start, int end, int depth) {
        depth++;
        for(int i=start; i<end-1; i++) {
            omegaOmegaPushBW1(array, start, end, depth);
        }
    }
    private void omegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for(int i=start; i<end-1; i++) {
            omegaOmegaPush1(array, start, end, depth);
        }
    }
    private void omegaOmegaSwap(int[] array, int start, int end, int r) {
        if(start >= end)
            return;
        Writes.recordDepth(r++);
        this.omegaOmegaPush(array, start, end+1, r);
        this.omegaOmegaPushBW(array, start, end, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
        this.omegaOmegaSwap(array, start+1, end-1, r);
    }
    private void omegaOmegaOmegaPushBW(int[] array, int start, int end, int depth) {
        depth++;
        for(int j=start+1; j<end; j++) {
            omegaOmegaSwap(array, start, j, depth);
        }
    }
    private void what_why(int[] array, int start, int end, int d) {
        Writes.recordDepth(d++);
        int m=(end-start)/2;
        if(m==0)
            return;
        for(int i=0;i<m;i++) {
            omegaOmegaOmegaPushBW(array, start, end, d);
        }
        what_why(array, start, start+m, d);
        what_why(array, start+m, end, d);
    }

    private void wotateOwO(int[] array, int range, int amount, int d) {
        this.what_why(array, 0, range-amount, d);
        this.what_why(array, range-amount, range, d);
        this.what_why(array, 0, range, d);
    }

    // O((m^2)^^2)
    private int[] generateNegligiblyUnchangingList(int[] array) {
        int[][] lists = new int[array.length][];
      regenLists:
        for(int ID=0; ID<lists.length;) {
            lists[ID] = Writes.createExternalArray(array.length);
            for(int i=0; i<array.length; i++) {
                int r;
              rejectSample:
                for(;;) {
                    r = randInt(0, array.length);
                    for(int j=0; j<i; j++) {
                        if(Reads.compareOriginalValueIndex(lists[ID], r, j, 0.01, true) == 0)
                            continue rejectSample;
                    }
                    break;
                }
                Writes.write(lists[ID], i, r, 0.01, true, true);
            }
            if(ID > 0) {
                for(int i=0; i<array.length; i++) {
                    if(Reads.compareOriginalValues(lists[ID-1][i], lists[ID][i]) != 0) {
                        for(int ii=0; ii<=ID; ii++) {
                            Writes.deleteExternalArray(lists[ID]);
                        }
                        ID=0; continue regenLists;
                    }
                }
            }
            ID++;
        }
        for(int ID=1; ID<lists.length; ID++) {
            Writes.deleteExternalArray(lists[ID]);
        }
        return lists[0];
    }

    private int randomSearchInTable(int[] array, int a, int b, int k) {
        if(Reads.compareOriginalIndices(array, b-1, k, 0.1, true) <= 0)
            return b;
        int l = a, r = b;
        while(l < r) {
            int m = randInt(a, b);
            if(m <= l || m > r) continue;
            int c = Reads.compareOriginalIndices(array, m, k, 0.1, true);
            if(c <= 0) {
                l = m + 1;
            }
            if(c >= 0) {
                r = m;
            }
        }
        return l;
    }

    private void insertTable(int[] table, int[] array, int a, int b) {
        for(int i=a+1; i<b; i++) {
            int l = randomSearchInTable(table, a, i, table[i]);
            wotateOwO(table, l, 1, 1);
            wotateOwO(array, l, 1, 1);
        }
    }

    private boolean isArrayProbablySorted(int[] array, int a, int b) {
        return isRangeSorted(array, a, b) && randInt(0, array.length) == 0;
    }

    public void vɯβu(int[] array, int a, int b) {
      doProbableCheck:
        while(!isArrayProbablySorted(array, a, b)) {
            Writes.recursion();
            int[] indice = generateNegligiblyUnchangingList(array);
            // if this works as intended, the chance of this passing is 1/((m-n)^n)
            for(int i=a; i<b; i++) {
                if(Reads.compareOriginalIndexValue(indice, i, a, 0.01, true) < 0 ||
                   Reads.compareOriginalIndexValue(indice, i, b, 0.01, true) >= 0) {
                    Writes.deleteExternalArray(indice);
                    continue doProbableCheck;
                }
            }
            insertTable(indice, array, a, b);
            Writes.deleteExternalArray(indice);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        vɯβu(array, 0, sortLength);
    }
}
