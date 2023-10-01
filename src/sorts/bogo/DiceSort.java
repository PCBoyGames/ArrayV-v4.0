package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import java.util.Set;
import java.util.HashSet;

/*
 *
MIT License
Copyright (c) 2020 fungamer2 and thatsOven
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

public class DiceSort extends BogoSorting {

    int min;
    int max;

    public DiceSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dice");
        this.setRunAllSortsName("Dice Sort");
        this.setRunSortName("Dicesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(true);
    }

    public int factorial(int num) {
    	int fact = 1;
    	for (int i = 2; i <= num; i++) {
    	    fact *= i;
        }
        return fact;
    }

    public int rollDie() {
    	return (int)(Math.random() * 6) + 1;
    }

    //Generate permutations using heap's algorithm
    public int genPerms(int[] array, int[][] output, int size, int length, int i) {
    	if (size == 1) {
    	    Writes.arraycopy(array, 0, output[i++], 0, length, 0.0001, true, true);
            return i;
        }
        for (int j = 0; j < size; j++) {
        	i = genPerms(array, output, size - 1, length, i);
            if (size % 2 == 1) {
            	Writes.swap(array, 0, size - 1, 0.0001, true, false);
            } else {
            	Writes.swap(array, j, size - 1, 0.0001, true, false);
            }
        }
        return i;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[][] perms = new int[factorial(length)][length];
        genPerms(array, perms, length, length, 0);
        boolean sorted = false;
        while (!sorted) {
        	int num = Math.min(rollDie(), perms.length);
        	int[][] samples = new int[num][length];
            Set<Integer> indices = new HashSet<Integer>(num);
            Writes.startLap();
            for (int i = 0; i < num; i++) {
            	int gen;
            	do {
            	    gen = (int) (Math.random() * perms.length);
                } while (indices.contains(gen));
                indices.add(gen);
            }
            Writes.stopLap();
            int i = 0;
            for (int index : indices) {
            	Writes.arraycopy(perms[index], 0, samples[i], 0, length, 0, false, true);
            	i++;
            }
            for (i = 0; i < samples.length; i++) {
            	Writes.arraycopy(samples[i], 0, array, 0, length, 0.0001, true, false);
                if (isArraySorted(array, length)) {
                	sorted = true;
                    break;
                }
            }
        }
    }
}
