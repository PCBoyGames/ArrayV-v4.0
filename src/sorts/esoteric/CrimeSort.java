package sorts.esoteric;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.templates.BogoSorting;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

"If you can't do the time, don't do the crime"
Thankfully you can do the time.

 */

final public class CrimeSort extends BogoSorting {

    private volatile int next = 0;

    public CrimeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Crime");
        //this.setRunAllID("Time Sort");
        this.setRunAllSortsName("Crime Sort, Mul 10");
        this.setRunSortName("Crimesort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false); // *Does not* use buckets! "magnitude" is only a multiplier.
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1); //See threads.RunDistributionSort for details
        this.setBogoSort(false);
        this.setQuestion("Enter delay per number in milliseconds:", 10);
    }

    public int avg(int a, int b) {
        return (a+b)/2;
    }

    public void interp(int[] array, int length) {
        while (!isArraySorted(array, length)) {
            for (int i = 1; i < length - 1; i+=2) {
                Writes.write(array, i, avg(array[i-1], array[i+1]), 1, false, true);
            }
            for (int i = 2; i < length - 1; i+=2) {
                Writes.write(array, i, avg(array[i-1], array[i+1]), 1, false, true);
            }
        }
    }

    private synchronized void report(int[] array, int a) {
        Writes.write(array, next, a, 0, true, false);
        next++;
    }

    @Override
    public void runSort(int[] array, int sortLength, int magnitude) throws Exception {
        final int A = magnitude;
        next = 0;

        ArrayList<Thread> threads = new ArrayList<>();

        final int[] tmp = Writes.createExternalArray(sortLength);

        for (int i = 0; i < sortLength; i++) {
            Writes.write(tmp, i, array[i], 0.25, true, true);
        }

        double temp = Delays.getDisplayedDelay();
        Delays.updateDelayForTimeSort(magnitude);

        for (int i = 0; i < sortLength; i++) {
            final int index = i;
            threads.add(new Thread("TimeSort-" + i) {
                @Override
                public void run() {
                    int a = tmp[index];

                    try {
                        Thread.sleep(a*A);
                        Writes.addTime(A);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IllegalArgumentException ex) {
                        JErrorPane.invokeErrorMessage(ex);
                    }
                    CrimeSort.this.report(array, a);
                }
            });
        }

        for (Thread t : threads)
            t.start();

        try {
            Thread.sleep(sortLength * A);
        }
        catch (InterruptedException e) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (IllegalArgumentException ex) {
            JErrorPane.invokeErrorMessage(ex);
        }

        Delays.setCurrentDelay(temp);
        Writes.setTime(sortLength * A);

        interp(array, sortLength);

        Writes.deleteExternalArray(tmp);
    }
}