package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

import java.util.Random;

/*
Copyright (c) 2023 thatsOven

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

public final class SambaSort extends BogoSorting {
    public SambaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Samba");
        this.setRunAllSortsName("Samba Sort");
        this.setRunSortName("Samba Sort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4);
        this.setBogoSort(true);
    }

    private final boolean WORST_CASE_MOV_AMT = false;

    private Random rng;

    public void move(int[] array, int from, int to) {
        if (from == to) return;

        if (from < to) {
            Writes.reversal(array, from, to, 0.5, true, false);
            Writes.reversal(array, from, to - 1, 0.5, true, false);
        } else {
            Writes.reversal(array, to, from, 0.5, true, false);
            Writes.reversal(array, to + 1, from, 0.5, true, false);
        }
    }

    private void moveBad(int[] array, int from, int to) {
        if (from == to) return;

        if (from < to) {
            for (int i = from; i + 1 < to; i++) {
                move(array, i, to);
                move(array, to, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                move(array, i, to);
                move(array, to, i - 1);
            }
        }
    }

    public long factorial(int n) {
        long f = 1;
        for (int i = 2; i <= n && f > 0; i++)
            f *= i;

        if (f <= 0) return factorial(n - 1);
        else        return f;
    }

    private long getMoveAmt(int n) {
        return WORST_CASE_MOV_AMT ? factorial(n) + 1 : (this.rng.nextLong() % factorial(n)) + 1;
    }

    private boolean isSorted(int[] array, int n) {
        return isArraySorted(array, n) && this.rng.nextLong() % factorial(n) == 0;
    }

    public void sort(int[] array, int a, int b) {
        this.rng = new Random();
        int n = b - a;

        while (true) {
            boolean c = false;
            int p = this.rng.nextInt(n - 1) + a; // pick random item in the array
            long m = this.getMoveAmt(n); // pick random amount of moves (at least 1)
            for (long i = 0; i < m;) {
                int nP;
                if (this.rng.nextBoolean()) { // pick random direction for each move
                    if (p < b - 1) nP = this.rng.nextInt(b - p) + p;
                    else {
                        c = true;
                        continue;
                    }
                } else if (p <= a) {
                    c = true;
                    continue;
                } else nP = this.rng.nextInt(p - a) + a;

                if (p == nP) {
                    c = true;
                    continue;
                };

                moveBad(array, p, nP); // move random amount of steps in each direction (at least 1)
                p = nP;
                i++;
            }

            m = this.getMoveAmt(n); // pick random amount of moves (at least 1)
            for (long i = 0; i < m;) {
                int x = this.rng.nextInt(n - 1) + a,
                    y = this.rng.nextInt(n - 1) + a;

                // swap random indices
                if (x != i) Writes.swap(array, x, y, 0.25, true, false);
                else {
                    c = true;
                    continue;
                }

                i++;
            }

            if (this.isSorted(array, n) && !c) break; // had equal or invalid results in the rng? too bad.
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);
    }
}
