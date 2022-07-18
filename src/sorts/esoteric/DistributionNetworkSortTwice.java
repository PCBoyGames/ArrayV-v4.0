package sorts.esoteric;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.BogoSorting;
import utils.PerlinNoise;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class DistributionNetworkSortTwice extends BogoSorting {

    boolean changes = false;
    boolean changesthis = false;
    int type;

    public DistributionNetworkSortTwice(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Distribution as Network");
        this.setRunAllSortsName("Distribution as Network Sort");
        this.setRunSortName("Distribution as Networksort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter distribution:\n1: Sine Wave\n2: Cosine Wave\n3: Perlin Noise\n4: Perlin Noise Curve\n5: Bell Curve\n6: Ruler\n7: Blancmange Curve\n8: Sum of Divisors\n9: Fly Straight, Dammit!\n10: Decreasing Random\n11: Modulo Function\n12: Product of Digits\n13: Euler-Totient Function\n14: TWPK's FOUR\n(Default is 1)", 1);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int i = len - 1;
        int p = 1;
        int j = len - 1;
        while (j >= 0 && i >= p) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected void networksort(int[] array, int[] indexnetwork, int start, int length) {
        for (int i = 1; i < length; i += 2) {
            Highlights.markArray(3, start + i - 1);
            Highlights.markArray(4, start + i);
            pairsort(array, start + indexnetwork[i - 1], start + indexnetwork[i]);
        }
    }

    protected void pairsort(int[] array, int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        if (Reads.compareIndices(array, i, j, 0.025, true) > 0) {
            Writes.swap(array, i, j, 0.025, true, false);
            changes = true;
            changesthis = true;
        }
    }

    // There's a lot to do here, but scaling isn't one of them.
    // The linear inversions already take care of that.
    // I'm also going to mark all the aux writes manually.
    // In many of these cases, I kind of have to, otherwise it won't work.
    protected void initializeNetwork(int[] array, int currentLen, int type) {
        Random random = new Random();

        // Sine Wave
        if (type == 1) {
            int n = currentLen - 1;
            double c = 2 * Math.PI / n;
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = (int) (n * (Math.sin(c * i) + 1) / 2);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Cosine Wave
        if (type == 2) {
            int n = currentLen - 1;
            double c = 2 * Math.PI / n;
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = (int) (n * (Math.cos(c * i) + 1) / 2);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Perlin Noise
        if (type == 3) {
            int[] perlinNoise = new int[currentLen];
            float step = 1f / currentLen;
            float randomStart = (float) (random.nextInt(currentLen));
            int octave = (int) (Math.log(currentLen) / Math.log(2));
            for (int i = 0; i < currentLen; i++) {
                int value = (int) (PerlinNoise.returnFracBrownNoise(randomStart, octave) * currentLen);
                perlinNoise[i] = value;
                randomStart += step;
            }
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = Math.min(perlinNoise[i], currentLen - 1);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Perlin Noise Curve
        if (type == 4) {
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                int value = 0 - (int) (PerlinNoise.returnNoise((float) i / currentLen) * currentLen);
                array[i] = Math.min(value, currentLen - 1);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Bell Curve
        if (type == 5) {
            double step = 8d / currentLen;
            double position = -4;
            int constant = 1264;
            double factor = currentLen / 512d;
            for (int i = 0; i < currentLen; i++) {
                double square = Math.pow(position, 2);
                double negativeSquare = 0 - square;
                double halfNegSquare = negativeSquare / 2d;
                double numerator = constant * factor * Math.pow(Math.E, halfNegSquare);
                double doublePi = 2 * Math.PI;
                double denominator = Math.sqrt(doublePi);
                Highlights.markArray(1, i);
                array[i] = (int) (numerator / denominator);
                Writes.auxWrites++;
                Delays.sleep(0.1);
                position += step;
            }
        }

        // Ruler
        if (type == 6) {
            int step = Math.max(1, currentLen / 256);
            int floorLog2 = (int) (Math.log(currentLen / step) / Math.log(2));
            int lowest;
            for (lowest = step; 2 * lowest <= currentLen / 4; lowest *= 2);
            boolean[] digits = new boolean[floorLog2 + 2];
            int i, j;
            for (i = 0; i + step <= currentLen; i += step) {
                for (j = 0; digits[j]; j++);
                digits[j] = true;
                for (int k = 0; k < step; k++) {
                    Highlights.markArray(1, i + k);
                    int value = currentLen / 2 - Math.min((1 << j) * step, lowest);
                    array[i + k] = value;
                    Writes.auxWrites++;
                    Delays.sleep(0.1);
                }
                for (int k = 0; k < j; k++) digits[k] = false;
            }
            for (j = 0; digits[j]; j++);
            digits[j] = true;
            while (i < currentLen) {
                Highlights.markArray(1, i);
                int value = Math.max(currentLen / 2 - (1 << j) * step, currentLen / 4);
                array[i++] = value;
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Blancmange Curve (Safe Ass)
        if (type == 7) {
            int floorLog2 = (int) (Math.log(currentLen) / Math.log(2));
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                int value = (int) (currentLen * curveSum(floorLog2, (double) i / currentLen));
                array[i] = value;
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Sum of Divisors
        if (type == 8) {
            Highlights.markArray(1, 0);
            array[0] = 0;
            Writes.auxWrites++;
            Delays.sleep(0.1);
            if (currentLen > 1) {
                Highlights.markArray(1, 1);
                array[1] = 1;
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
            for (int i = 2; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = sumDivisors(i);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Fly Straight, Dammit!
        if (type == 9) {
            int[] fsd = new int[currentLen > 1 ? currentLen : 1];
            double max;
            max = fsd[0] = fsd[1] = 1;
            for (int i = 2; i < currentLen; i++) {
                int g = gcd(fsd[i - 1], i);
                fsd[i] = fsd[i - 1] / g + (g == 1 ? i + 1 : 0);
                if (fsd[i] > max) max = fsd[i];
            }
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = (int) (fsd[i]);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Decreasing Random
        if (type == 10) {
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                int r = random.nextInt(currentLen - i) + i;
                array[i] = r;
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Modulo Function
        if (type == 11) {
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = 2 * (currentLen % (i + 1));
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }

        // Product of Digits
        if (type == 12) {
            int n = currentLen;
            for (int j = 0; j < n; j++) {
                Highlights.markArray(1, j);
                array[j] = 1;
                Writes.auxWrites++;
                Delays.sleep(0.1);
                for (int i = j; i > 0; i /= 10) {
                    if (i % 10 > 0) {
                        array[j] *= i % 10;
                        Writes.auxWrites++;
                        Delays.sleep(0.1);
                    }
                }
            }
        }

        // Euler-Totient Function
        if (type == 13) {
            int[] minPrimeFactors = new int[currentLen];
            List<Integer> primes = new ArrayList<Integer>();
            Highlights.markArray(1, 0);
            array[0] = 0;
            Writes.auxWrites++;
            Delays.sleep(0.1);
            if (currentLen > 1) {
                Highlights.markArray(1, 1);
                array[1] = 1;
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
            for (int i = 2; i < currentLen; i++) {
                if (minPrimeFactors[i] == 0) {
                    primes.add(i);
                    minPrimeFactors[i] = i;
                    Highlights.markArray(1, i);
                    array[i] = i - 1;
                    Writes.auxWrites++;
                    Delays.sleep(0.1);
                }
                for (int prime : primes) {
                    if (i * prime >= currentLen) break;
                    boolean last = prime == minPrimeFactors[i];
                    minPrimeFactors[i * prime] = prime;
                    Highlights.markArray(1, i * prime);
                    array[i * prime] = array[i] * (last ? prime : prime - 1);
                    Writes.auxWrites++;
                    Delays.sleep(0.1);
                    if (last) break;
                }
            }
        }

        // TWPK's FOUR
        if (type == 14) {
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                array[i] = runFOUR(i);
                Writes.auxWrites++;
                Delays.sleep(0.1);
            }
        }
    }

    protected double curveSum(int n, double x) {
        double sum = 0;
        while (n >= 0)
            sum += curve(n--, x);
        return sum;
    }

    protected double curve(int n, double x) {
        return triangleWave((1 << n) * x) / (1 << n);
    }

    protected double triangleWave(double x) {
        return Math.abs(x - (int) (x + 0.5));
    }

    protected int sumDivisors(int n) {
        int sum = n + 1;
        for (int i = 2; i <= (int) Math.sqrt(n); i++) {
            if (n % i == 0) {
                if (i == n / i) sum += i;
                else sum += i + n / i;
            }
        }
        return sum;
    }

    protected int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    protected int runFOUR(int a) {
        if (a == 0) return 2;
        int[] letters = {4, 3, 3, 5, 4, 4, 3, 5, 5, 4};
        int steps = 1;
        int cur = 0;
        int last = a;
        while (last != 4) {
            for (int i = last; i > 0; i /= 10) cur += letters[i % 10];
            steps++;
            last = cur;
            cur = 0;
        }
        return steps;
    }

    protected void linearInvert(int[] array, int currentLen) {
        int[] tmp = new int[currentLen];
        tableinvert(array, tmp, currentLen);
        Highlights.clearAllMarks();
        Writes.arraycopy(tmp, 0, array, 0, currentLen, 0.1, true, true);
    }

    protected boolean stableComp(int[] array, int[] table, int a, int b) {
        int comp = Reads.compareIndices(array, table[a], table[b], 0, false);
        return comp > 0 || (comp == 0 && Reads.compareOriginalIndices(table, a, b, 0, false) > 0);
    }

    protected void medianOfThree(int[] array, int[] table, int a, int b) {
        int m = a + (b - 1 - a) / 2;
        if (stableComp(array, table, a, m)) Writes.swap(table, a, m, 0, true, true);
        if (stableComp(array, table, m, b - 1)) {
            Writes.swap(table, m, b - 1, 0, false, true);
            if (stableComp(array, table, a, m)) return;
        }
        Writes.swap(table, a, m, 0, false, true);
    }

    protected int partition(int[] array, int[] table, int a, int b, int p) {
        int i = a - 1, j = b;
        Highlights.markArray(3, p);
        while (true) {
            do i++; while (i < j && !stableComp(array, table, i, p));
            do j--; while (j >= i && stableComp(array, table, j, p));
            if (i < j) Writes.swap(table, i, j, 0, false, true);
            else return j;
        }
    }

    protected void quickSort(int[] array, int[] table, int a, int b) {
        if (b - a < 3) {
            if (b - a == 2 && stableComp(array, table, a, a + 1)) Writes.swap(table, a, a + 1, 0, false, true);
            return;
        }
        medianOfThree(array, table, a, b);
        int p = partition(array, table, a + 1, b, a);
        Writes.swap(table, a, p, 0, false, true);
        quickSort(array, table, a, p);
        quickSort(array, table, p + 1, b);
    }

    protected void tableinvert(int[] array, int[] table, int currentLength) {
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0, false, true);
        quickSort(array, table, 0, currentLength);
    }

    protected void prepareIndexes(int[] array, int length) {
        initializeNetwork(array, length, type);
        linearInvert(array, length);
        linearInvert(array, length);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        if (answer > 14) return 14;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int input) {
        type = input;
        delay = 0.1;
        int indexeslen = par(array, currentLength);
        int lastlen = currentLength;
        int[] indexes = Writes.createExternalArray(indexeslen);
        boolean lenchange = true;
        int firstpos = 0;
        int nextlast = currentLength;
        int lastpos = currentLength;
        boolean firstfound = false;
        while (lastlen > 1) {
            changes = false;
            Highlights.clearAllMarks();
            if (lenchange && indexeslen > 1) prepareIndexes(indexes, indexeslen);
            else if (lenchange) break;
            lenchange = false;
            firstfound = false;
            Highlights.clearAllMarks();
            for (int i = firstpos > 0 ? firstpos - 1 : 0; i + indexeslen <= (lastpos + 1 < currentLength ? lastpos + 1 : currentLength); i++) {
                changesthis = false;
                networksort(array, indexes, i, indexeslen);
                if (changes && !firstfound) {
                    firstpos = i;
                    firstfound = true;
                }
                if (changesthis) nextlast = i + indexeslen;
            }
            Highlights.clearAllMarks();
            lastpos = nextlast;
            if (!changes) {
                if (!isArraySorted(array, currentLength)) {
                    firstpos = 0;
                    lastpos = currentLength;
                    lastlen = indexeslen;
                    indexeslen = par(array, currentLength);
                    while (indexeslen >= lastlen && indexeslen >= 2) indexeslen--;
                    lenchange = true;
                    Writes.deleteExternalArray(indexes);
                    indexes = Writes.createExternalArray(indexeslen);
                } else break;
            }
        }
        Highlights.clearAllMarks();
        InsertionSort clean = new InsertionSort(arrayVisualizer);
        clean.customInsertSort(array, 0, currentLength, 10, false);
    }
}