package sorts.templates;

import main.ArrayVisualizer;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps

public abstract class ShellSorting extends Sort {
    protected int[] OriginalGaps         = {2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    protected int[] PowTwoPlusOneGaps    = {2049, 1025, 513, 257, 129, 65, 33, 17, 9, 5, 3, 1};
    protected int[] PowTwoMinusOneGaps   = {4095, 2047, 1023, 511, 255, 127, 63, 31, 15, 7, 3, 1};
    protected int[] ThreeSmoothGaps      = {3888, 3456, 3072, 2916, 2592, 2304, 2187, 2048, 1944, 1728,
                                                  1536, 1458, 1296, 1152, 1024, 972, 864, 768, 729, 648, 576,
                                                  512, 486, 432, 384, 324, 288, 256, 243, 216, 192, 162, 144,
                                                  128, 108, 96, 81, 72, 64, 54, 48, 36, 32, 27, 24, 18, 16, 12,
                                                  9, 8, 6, 4, 3, 2, 1};
    protected int[] PowersOfThreeGaps    = {3280, 1093, 364, 121, 40, 13, 4, 1};
    protected int[] SedgewickIncerpiGaps = {1968, 861, 336, 112, 48, 21, 7, 3, 1};
    protected int[] SedgewickGaps        = {1073, 281, 77, 23, 8, 1};
    protected int[] OddEvenSedgewickGaps = {3905, 2161, 929, 505, 209, 109, 41, 19, 5, 1};
    protected int[] GonnetBaezaYatesGaps = {1861, 846, 384, 174, 79, 36, 16, 7, 3, 1};
    protected int[] TokudaGaps           = {2660, 1182, 525, 233, 103, 46, 20, 9, 4, 1};
    protected int[] CiuraGaps            = {1750, 701, 301, 132, 57, 23, 10, 4, 1};
    protected int[] ExtendedCiuraGaps    = {8861, 3938, 1750, 701, 301, 132, 57, 23, 10, 4, 1};

    protected ShellSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void shellSort(int[] array, int length) {
        int incs[] = ExtendedCiuraGaps;

        for (int k = 0; k < incs.length; k++) {
            if (incs == PowersOfThreeGaps) {
                if (incs[k] < length/3) {
                    for (int h = incs[k], i = h; i < length; i++) {
                        //ArrayVisualizer.setCurrentGap(incs[k]);

                        int v = array[i];
                        int j = i;

                        Highlights.markArray(1, j);
                        Highlights.markArray(2, j - h);

                        boolean change = false;

                        while (j >= h && Reads.compareValues(array[j - h], v) == 1)
                        {
                            Writes.write(array, j, array[j - h], 1, false, false);
                            change = true;
                            j -= h;

                            Highlights.markArray(1, j);

                            if (j - h >= 0) {
                                Highlights.markArray(2, j - h);
                            }
                            else {
                                Highlights.clearMark(2);
                            }
                        }
                        if (change) Writes.write(array, j, v, 1, true, false);
                    }
                }
            }
            else {
                if (incs[k] < length) {
                    for (int h = incs[k], i = h; i < length; i++) {
                        //ArrayVisualizer.setCurrentGap(incs[k]);

                        int v = array[i];
                        int j = i;

                        Highlights.markArray(1, j);
                        Highlights.markArray(2, j - h);

                        boolean change = false;

                        while (j >= h && Reads.compareValues(array[j - h], v) == 1)
                        {
                            Writes.write(array, j, array[j - h], 1, false, false);
                            change = true;
                            j -= h;

                            Highlights.markArray(1, j);

                            if (j - h >= 0) {
                                Highlights.markArray(2, j - h);
                            }
                            else {
                                Highlights.clearMark(2);
                            }
                        }
                        if (change) Writes.write(array, j, v, 1, true, false);
                    }
                }
            }
        }
    }

    protected void quickShellSort(int[] array, int lo, int hi) {
        int incs[] = {48, 21, 7, 3, 1};

        for (int k = 0; k < incs.length; k++) {
            for (int h = incs[k], i = h + lo; i < hi; i++)
            {
                int v = array[i];
                int j = i;

                boolean change = false;

                while (j >= h && Reads.compareValues(array[j-h], v) == 1)
                {
                    Highlights.markArray(1, j);

                    Writes.write(array, j, array[j - h], 1, true, false);
                    change = true;
                    j -= h;
                }
                if (change) Writes.write(array, j, v, 0.5, true, false);
            }
        }
    }
}