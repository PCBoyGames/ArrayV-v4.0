package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

surrender

 */

public class VoidSortGooflang extends Sort {
    public VoidSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Void");
        this.setRunAllSortsName("Gooflang's Void Sort");
        this.setRunSortName("Gooflang's Voidsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(3);
        this.setBogoSort(false);
    }

    public long arrow(long a, long m, long n) {
        if (n == 0) {
            return 1;
        } else {
            if (m == 1) return a ^ n;
            return arrow(a, m-1, arrow(a, m, n-1));
        }
    }

    public void nOmegaSwap(int O, int[] array, int a, int b, int d) {
        if (a != b) {
            Writes.recordDepth(d++);
            int temp = array[a];
            if (O == 1) {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.swap(array, a, b, 0.1, true, false);
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaSwap(O - 1, array, a, b, d);
                }
            }
            if (array[b] != temp) Writes.swap(array, a, b, 0.1, true, false);
        }
    }

    public void nOmegaMultiSwap(int O, int[] array, int a, int b, int d) {
        if (a != b) {
            int temp = array[a];
            Writes.recordDepth(d++);
            if (O == 1) {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        for (int j = a; j < b; j++) {
                            nOmegaSwap(arrayVisualizer.getCurrentLength(), array, j, j + 1, 0);
                        }
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        for (int j = a; j > b; j--) {
                            nOmegaSwap(arrayVisualizer.getCurrentLength(), array, j, j - 1, 0);
                        }
                    }
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaMultiSwap(O - 1, array, a, b, d);
                }
            }
            while (array[b] != temp) {
                if (b - a > 0) {
                    for (int j = a; j < b; j++) {
                        nOmegaSwap(arrayVisualizer.getCurrentLength(), array, j, j + 1, 0);
                    }
                } else {
                    for (int j = a; j > b; j--) {
                        nOmegaSwap(arrayVisualizer.getCurrentLength(), array, j, j - 1, 0);
                    }
                }
            }
        }
    }

    public void nOmegaPush(int O, int[] array, int a, int b, int d) {
        if (a != b) {
            int temp = array[a];
            Writes.recordDepth(d++);
            if (O == 1) {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        for (int j = a; j < b-a; j++) {
                            nOmegaMultiSwap(arrayVisualizer.getCurrentLength(), array, b, a, 0);
                        }
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        for (int j = a; j > b-a; j--) {
                            nOmegaMultiSwap(arrayVisualizer.getCurrentLength(), array, a, b, 0);
                        }
                    }
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaPush(O - 1, array, a, b, d);
                }
            }
            while (array[b] != temp) {
                if (b - a > 0) {
                    for (int j = a; j < b-a; j++) {
                        nOmegaMultiSwap(arrayVisualizer.getCurrentLength(), array, b, a, 0);
                    }
                } else {
                    for (int j = a; j > b-a; j--) {
                        nOmegaMultiSwap(arrayVisualizer.getCurrentLength(), array, a, b, 0);
                    }
                }
            }
        }
    }

    public void nOmegaReversal(int O, int[] array, int a, int b, int d) {
        if (a < b) {
            Writes.recordDepth(d++);
            int temp = array[a];
            if (O == 1) {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    for (int j = a; j < b; j++) {
                        nOmegaPush(arrayVisualizer.getCurrentLength(), array, j, b-j, 0);
                        nOmegaPush(arrayVisualizer.getCurrentLength(), array, b-j-1, j, 0);
                    }
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaReversal(O - 1, array, a, b, d);
                }
            }
            while (array[b] != temp) {
                for (int i = a; i < b; i++) {
                    nOmegaPush(arrayVisualizer.getCurrentLength(), array, i, b-i, 0);
                    nOmegaPush(arrayVisualizer.getCurrentLength(), array, b-i-1, i, 0);
                }
            }
        }
    }

    private void goofVoid(int O, int[] array, int a, int b, int d, long ord) {
        Writes.recordDepth(d++);
        do nOmegaSwap(arrayVisualizer.getCurrentLength(), array, a, b, 0); while (Reads.compareValues(array[a], array[b]) > 0);
        if (O == 1) {
            for (long i = 0; i <= ord; i++) {
                for (int j = 1; j < b-a; j++) {
                    Writes.recursion();
                    goofVoid(O, array, a+j, b, d, arrow(ord, 2, ord));
                    Writes.recursion();
                    goofVoid(O, array, a, b-j, d, arrow(ord, 2, ord));
                    Writes.recursion();
                    goofVoid(O, array, a+j, b, d, arrow(ord, 2, ord));
                    if (ord > 0) {
                        nOmegaReversal(arrayVisualizer.getCurrentLength(), array, a, b, 0);
                        Writes.recursion();
                        goofVoid(O, array, a, b, d, ord-1);
                    }
                }
            }
        } else {
            for (long i = 0; i <= ord; i++) {
                for (int j = 1; j < b-a; j++) {
                    Writes.recursion();
                    goofVoid(O, array, a+j, b, d, arrow(ord, 2, ord));
                    Writes.recursion();
                    goofVoid(O, array, a, b-j, d, arrow(ord, 2, ord));
                    Writes.recursion();
                    goofVoid(O, array, a+j, b, d, arrow(ord, 2, ord));
                    if (ord > 0) {
                        nOmegaReversal(arrayVisualizer.getCurrentLength(), array, a, b, 0);
                        Writes.recursion();
                        goofVoid(O-1, array, a, b, d, ord-1);
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        goofVoid(currentLength, array, 0, currentLength-1, 0, (long) currentLength);
    }
}
