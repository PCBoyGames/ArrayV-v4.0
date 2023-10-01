package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import java.io.*;
import java.util.Random;

/*
 *
MIT License

Copyright (c) 2022-2023 aphitorite

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

public class BadAppleSort extends Sort {
    public BadAppleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad Apple");
        this.setRunAllSortsName("Bad Apple Sort");
        this.setRunSortName("Bad Apple Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) throws IOException {
        int N = 32768;
        int[] temp = Writes.createExternalArray(N);

        Random r = new Random(0); // seeded shuffle (do not change)

        for (int i = 0; i < N; i++) {
            int j = r.nextInt(i+1);
            Writes.write(temp, i, temp[j], 0, false, true);
            Writes.write(temp, j, i, 0, false, true);
        }

        String path = System.getProperty("user.dir") + "\\BadAppleSort";
        try (BufferedInputStream inFile = new BufferedInputStream(new FileInputStream(path))) {
            byte[] bytes = new byte[N/8];

            int alloc = 6726656; // total amount of space in ints used
            Writes.changeAllocAmount(alloc);

            int br = 0;

            while (br != -1) {
                br = inFile.read(bytes, 0, bytes.length);

                for (int i = 0; i < currentLength; i++) {
                    int idx = (int)(N * (double)i/currentLength);
                    int q   = (bytes[idx >> 3] >> (idx & 7)) & 1;

                    Writes.write(array, i, q * (int)(temp[idx] * (double)currentLength/N), 0.015, true, false);
                }
            }
            Writes.changeAllocAmount(-alloc);
        }
        Writes.deleteExternalArray(temp);
    }
}
