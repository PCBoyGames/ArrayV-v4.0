package sorts.templates;

import main.ArrayVisualizer;

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
 *
 */

public abstract class InsertionSorting extends Sort {
    protected InsertionSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void insertionSort(int[] array, int start, int end, double sleep, boolean auxwrite) {
        int pos;
        int current;

        for (int i = start; i < end; i++) {
            current = array[i];
            pos = i - 1;
            boolean change = false;

            while (pos >= start && Reads.compareValues(array[pos], current) > 0) {
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
                change = true;
            }
            if (change) Writes.write(array, pos + 1, current, sleep, true, auxwrite);
        }
    }
}
