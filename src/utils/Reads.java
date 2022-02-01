package utils;

import java.util.ArrayList;
import java.math.BigInteger;
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

final public class Reads {
    private volatile BigInteger comparisons;
    public volatile ArrayList<Integer> networkIndices;

    private ArrayVisualizer ArrayVisualizer;

    private Delays Delays;
    private Highlights Highlights;
    private Timer Timer;

    public Reads(ArrayVisualizer arrayVisualizer) {
        this.ArrayVisualizer = arrayVisualizer;

        this.resetStatistics();
        this.networkIndices = new ArrayList<>();

        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Timer = ArrayVisualizer.getTimer();

        ArrayVisualizer.getNumberFormat();
    }

    public void resetStatistics() {
        this.comparisons = BigInteger.ZERO;
    }

    public void addComparison() {
        this.comparisons = this.comparisons.add(BigInteger.ONE);
    }

    public void addComparisons(int value) {
        this.comparisons = this.comparisons.add(BigInteger.valueOf((long)value));
    }

    public void addComparisons(long value) {
        this.comparisons = this.comparisons.add(BigInteger.valueOf(value));
    }

    public void addComparisons(BigInteger value) {
        this.comparisons = this.comparisons.add(value);
    }

    public String getStats() {
        if(this.comparisons.signum() == -1) {
            return "\u03a9 Comparisons";
        }
        else {
            if(this.comparisons.equals(BigInteger.ONE)) return this.comparisons + " Comparison";
            else                      return String.format("%,d",this.comparisons) + " Comparisons";
        }
    }

    public BigInteger getComparisons() {
        return this.comparisons;
    }

    public void setComparisons(long value) {
        this.comparisons = BigInteger.valueOf(value);
    }

    public void setComparisons(BigInteger value) {
        this.comparisons = value;
    }

    public int compareValues(int left, int right) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        this.addComparison();

        if (ArrayVisualizer.doingStabilityCheck()) {
            left  = ArrayVisualizer.getStabilityValue(left);
            right = ArrayVisualizer.getStabilityValue(right);
        }

        int cmpVal = 0;

        Timer.startLap("Compare");

        if(left > right)      cmpVal =  1;
        else if(left < right) cmpVal = -1;
        else                  cmpVal =  0;

        Timer.stopLap();

        if (!ArrayVisualizer.useAntiQSort()) {
            if (ArrayVisualizer.reversedComparator()) {
                return -cmpVal;
            }
            return cmpVal;
        }
        else {
            return ArrayVisualizer.antiqCompare(left, right);
        }
    }

    public int compareOriginalValues(int left, int right) {
        if (ArrayVisualizer.sortCanceled()) throw new StopSort();
        this.addComparison();

        int cmpVal = 0;

        Timer.startLap("Compare");

        if(left > right)      cmpVal =  1;
        else if(left < right) cmpVal = -1;
        else                  cmpVal =  0;

        Timer.stopLap();

        return cmpVal;
    }

    public int compareIndices(int[] array, int left, int right, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(sleep);
        }
        if (ArrayVisualizer.generateSortingNetworks()) {
            networkIndices.add(left);
            networkIndices.add(right);
        }
        return this.compareValues(array[left], array[right]);
    }

    public int compareOriginalIndices(int[] array, int left, int right, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(array[left], array[right]);
    }

    public int compareIndexValue(int[] array, int index, int value, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareValues(array[index], value);
    }

    public int compareOriginalIndexValue(int[] array, int index, int value, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(array[index], value);
    }

    public int compareValueIndex(int[] array, int value, int index, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareValues(value, array[index]);
    }

    public int compareOriginalValueIndex(int[] array, int value, int index, double sleep, boolean mark) {
        if(mark) {
            Highlights.markArray(1, index);
            Delays.sleep(sleep);
        }
        return this.compareOriginalValues(value, array[index]);
    }

    public int analyzeMax(int[] array, int length, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for(int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val > max) max = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return max;
    }

    public int analyzeMax(int[] array, int start, int end, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for(int i = start; i < end; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val > max) max = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return max;
    }

    public int analyzeRawMax(int[] array, int start, int end, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for(int i = start; i < end; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];

            Timer.startLap("Analysis");

            if(val > max) max = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return max;
    }

    public int analyzeMin(int[] array, int length, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int min = 0;

        for(int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val < min) min = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return min;
    }

    public int analyzeMin(int[] array, int start, int end, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int min = 0;

        for(int i = start; i < end; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val < min) min = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return min;
    }

    public int analyzeRawMin(int[] array, int start, int end, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int min = 0;

        for(int i = start; i < end; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];

            Timer.startLap("Analysis");

            if(val < min) min = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return min;
    }

    public int analyzeMaxLog(int[] array, int length, int base, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for(int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val > max) max = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeMaxCeilingLog(int[] array, int length, int base, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        int max = 0;

        for(int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if(val > max) max = val;

            Timer.stopLap();

            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();

        return (int) (Math.log(max) / Math.log(base));
    }

    public int analyzeBit(int[] array, int length) {
        ArrayVisualizer.toggleAnalysis(true);
        ArrayVisualizer.updateNow();

        // Find highest bit of highest value
        int max = 0;

        for(int i = 0; i < length; i++) {
            if (ArrayVisualizer.sortCanceled()) throw new StopSort();

            int val = array[i];
            if(ArrayVisualizer.doingStabilityCheck())
                val = ArrayVisualizer.getStabilityValue(val);

            Timer.startLap("Analysis");

            if (val > max) max = val;

            Timer.stopLap();

            Highlights.markArray(1, i);
            Delays.sleep(0.75);
        }

        int analysis;

        Timer.startLap();

        analysis = 31 - Integer.numberOfLeadingZeros(max);

        Timer.stopLap();

        ArrayVisualizer.toggleAnalysis(false);
        ArrayVisualizer.updateNow();
        return analysis;
    }

    public int getDigit(int a, int power, int radix) {
        if(ArrayVisualizer.doingStabilityCheck())
            a = ArrayVisualizer.getStabilityValue(a);

        int digit;
        Timer.startLap();
        digit = (int) (a / Math.pow(radix, power)) % radix;
        Timer.stopLap();
        return digit;
    }

    public boolean getBit(int n, int k) {
        if(ArrayVisualizer.doingStabilityCheck())
            n = ArrayVisualizer.getStabilityValue(n);

        // Find boolean value of bit k in n
        boolean result;
        Timer.startLap();
        result = ((n >> k) & 1) == 1;
        Timer.stopLap();
        return result;
    }
}
