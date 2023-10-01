package utils;

import java.util.Random;

import main.ArrayVisualizer;
// Distay's Search Set

public class Searches {
    private static ArrayVisualizer arrv;
    private static Writes Writes;
    private static Highlights Highlights;
    private static Reads Reads;
    private static Delays Delays;
    public static enum Search {
        LINEAR(0),
        BINARY(1),
        EXPOLINEAR(2),
        EXPOBINARY(3),
        PUREEXPO(4),
        FIB(5),
        TRISEARCH(6),
        TERNARY(7),
        RANDOM(8),
        EXTREMERANDOM(9),
        SQUARE(10),
        SLOPE(11);
        public int type;
        Search(int type) {
            this.type = type;
        }
    }
    public Searches(ArrayVisualizer A) {
        arrv = A;
        Writes = A.getWrites();
        Highlights = A.getHighlights();
        Reads = A.getReads();
        Delays = A.getDelays();
    }

    public static void push(int[] array, int search, int end, int key, boolean aux) {
        int i=end-1;
        for (; i>=search&&i>=0; i--) {
            Writes.write(array, i+1, array[i], 1, true, aux);
        }
        Writes.write(array, i+1, key, 1, true, aux);
    }

    public static void shift(int[] array, int from, int to, double sleep) {
        if (from == to)
            return;
        int k = array[from];
        if (from < to) {
            Writes.arraycopy(array, from+1, array, from, to-from, sleep/2d, true, false);
        } else {
            Writes.arraycopy(array, to, array, to+1, from-to, sleep/2d, true, false);
        }
        Writes.write(array, to, k, sleep, true, false);
    }

    public static void revpush(int[] array, int start, int search, int length, int key) {
        int i=start+1;
        for (; i<search&&i<length; i++) {
            Writes.write(array, i-1, array[i], 1, true, false);
        }
        Writes.write(array, i-1, key, 1, true, false);
    }

    /**
     * Average linear search.
     * Finishes in O(n) max.
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @param Whether to search left or right
     * @return Where the number should go.
     */
    public static int linear(int[] array, int start, int end, int key, boolean goLeft, double sleep) {
        if (goLeft) {
            while (end >= start) {
                if (Reads.compareValues(array[end], key) == -1)
                    break;
                Highlights.markArray(1, end--);
                Delays.sleep(sleep);
            }
            return end+1;
        } else {
            while (end >= start) {
                if (Reads.compareValues(array[start], key) == 1)
                    break;
                Highlights.markArray(1, start++);
                Delays.sleep(sleep);
            }
            return start;
        }
    }
    /**
     * The binary search method.
     * Finishes in O(log n) constant.
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @return Where the number should go.
     */
    public static int binary(int[] array, int start, int end, int key, double sleep) {
        while (start < end) {
            int mid = start + ((end - start) / 2);
            if (Reads.compareValues(array[mid], key) == -1) {
                start = mid + 1;
            } else {
                end = mid;
            }
            Highlights.markArray(1, mid);
            Delays.sleep(sleep);
        }
        return start;
    }
    /**
     * The exponential search.
     * Finishes in O({@code log}<sub>{@code gap}</sub> {@code i}).
     * The gap is 2 by default.
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @return The range between the last success and first failure.
     */
    public static Range exponential(int[] array, int start, int end, int key, double sleep) {
        int prev = 1;
        int gap = 2;
        int length = end-start;
        while (prev < length && Reads.compareValues(array[prev+start], key) == -1) {
            prev *= gap;
            Highlights.markArray(1, prev+start);
            Delays.sleep(sleep);
        }
        if (prev > length) {
            prev = length;
        }
        return new Range((prev/gap)+start, prev+start);
    }
    /**
     * The exponential search method, going from the end.
     * Finishes in O({@code log}<sub>{@code gap}</sub> {@code i}).
     * The gap is 2 by default.
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @return The range between the last success and first failure.
     */
    public static Range reverseExponential(int[] array, int start, int end, int key, double sleep) {
        int prev = 1;
        int gap = 2;
        int length = end-start;
        while (prev < length && Reads.compareValues(array[end-prev], key) == 1) {
            prev *= gap;
            if (end-prev>=start)
                Highlights.markArray(1, end-prev);
            Delays.sleep(sleep);
        }
        if (prev > length) {
            prev = length;
        }
        return new Range(end-prev, end-(prev/gap));
    }
    /**
     * An improvement to linear search, using exponential search first.
     * Finishes in O({@code (log i) + n}).
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @param Whether to use reverse exponential search or not
     * @param How much sleep
     * @return Where the number should go
     */
    public static int exponentialLinear(int[] array, int start, int end, int key, boolean goRight, double sleep) {
        Range rng;
        if (goRight)
            rng = exponential(array, start, end, key, sleep);
        else
            rng = reverseExponential(array, start, end, key, sleep);
        return linear(array, rng.start, rng.end, key, !goRight, sleep);
    }
    /**
     * An exponential-bounded variant of binary search, only applicable in very specific situations.
     * Finishes in O({@code log}<sup>{@code 2}</sup> {@code i}).
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @param Whether to use reverse exponential search or not
     * @param How much sleep
     * @return Where the number should go
     */
    public static int exponentialBinary(int[] array, int start, int end, int key, boolean goRight, double sleep) {
        Range rng;
        if (goRight)
            rng = exponential(array, start, end, key, sleep);
        else
            rng = reverseExponential(array, start, end, key, sleep);
        return binary(array, rng.start, rng.end, key, sleep);
    }
    /**
     * An binsearch-esque bidirectional variant of exponential search that
     * outputs an actual search location, instead of a range to search in.
     *
     * Finishes in O({@code log}<sup>{@code 2}</sup> {@code n}).
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @param The starting direction of the recursive expsearch
     * @param How much sleep per search
     * @return Where the number should go
     */
    public static int exponentialIterative(int[] array, int start, int end, int key, boolean direction, double sleep) {
        start--;
        while (start < end - 1) {
            Range rng;
            if (direction) {
                rng = reverseExponential(array, start, end, key, sleep);
            } else {
                rng = exponential(array, start, end, key, sleep);
            }
            start = rng.start;
            end = rng.end;
            if (start>=0)
                Highlights.markArray(1, start);
            Highlights.markArray(2, end);
            Delays.sleep(sleep*4d);
            direction=!direction;
        }
        return end;
    }
    public static int exponentialIterative(int[] array, int start, int end, int key, double sleep) {
        int mid = start + ((end - start) / 2);
        return exponentialIterative(array, start, end, key, Reads.compareValues(key, array[mid]) >= 0, sleep);
    }

    /**
     * The fibonacci search in Fibonacci Insertion Sort.
     * Unsure about finish time.
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @return Where the number should go
     */
    public static int fibonacci(int[] array, int start, int end, int item) {
        int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }

        int offset = start - 1;

        while (fibM > 1) {

            int i = Math.min(offset + fibM2, end);

            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);

            if (Reads.compareValues(array[i], item) <= 0) {
                fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
                fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
            Delays.sleep(0.6);
        }
        int position = ++offset;
        if (Reads.compareValues(array[position], item) <= 0) {
            ++position;
        }
        return position;
    }

    /**
     * The binary search method, but idealized for Insertion Sort.
     * Finishes in O({@code log n}).
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @return Where the number should go.
     */
    public static int trisearch(int[] array, int start, int end, int key, double sleep) {
        while (start < end) {
            int mid = start + ((end - start) / 2);
            Highlights.markArray(1, start);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, end);
            Delays.sleep(sleep);
            if (Reads.compareValues(array[start], key) == 1) {
                break;
            } else if (Reads.compareValues(array[end], key) == -1) {
                Highlights.clearAllMarks();
                return end;
            } else if (Reads.compareValues(array[mid], key) == -1) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        Highlights.clearAllMarks();
        return start;
    }

    /**
     * Distay's take on ternary searching.
     * Best case of O({@code log}<sub>{@code 3}</sub> {@code n}), average of O({@code log}<sup>{@code 2}</sup> {@code n}).
     * @param The array to search in.
     * @param The start of the range
     * @param The end of the range
     * @param The number to search for
     * @param How much sleep per iteration
     * @return Where the number should go.
     */
    public static int ternary(int[] array, int start, int end, int key, double sleep) {
        while (start < end-1) {
            int third = (end - start + 1) / 3,
                midA = start + third,
                midB = end - third;
            if (Reads.compareValues(array[midA], key) == 1) {
                end = midA;
            } else if (Reads.compareValues(array[midB], key) == -1) {
                start = midB;
            } else {
                start = midA;
                end = midB;
            }
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }


    /**
     * Random linear-esque search.
     * Finishes in O({@code kn}) average, can finish in O(infinity).
     */
    public static int extremeRandom(int[] array, int start, int end, int key, double sleep) {
        if (end-start == 0)
            return start;
        if (Reads.compareValues(array[end-1], key) == -1) {
            return end;
        }
        if (Reads.compareValues(array[start], key) == 1) {
            return start;
        }
        Random z = new Random();
        int search;
        do {
            search = z.nextInt(end-start) + start;
            Highlights.markArray(1, search);
            Delays.sleep(sleep);
        } while ((search>start && Reads.compareValues(array[search-1], key) == 1)
             || Reads.compareValues(array[search], key) == -1);
        return search;
    }

    /**
     * Random binary-esque search.
     * Finishes in O(log^3 n) average, can finish in O(infinity).
     */
    public static int random(int[] array, int start, int end, int key, double sleep) {
        Random z = new Random();
        while (start < end-1) {
            int r1 = z.nextInt(end-start) + start,
                r2 = z.nextInt(end-start) + start;
            if (r2 < r1) {
                int t=r2;
                r2  = r1;
                r1  =  t;
            }
            if (Reads.compareValues(array[r1], key) <= 0) {
                start = r1;
            }
            if (Reads.compareValues(array[r2], key) >= 0) {
                end = r2;
            }
            Highlights.markArray(2, r1);
            Highlights.markArray(3, r2);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }

    /**
     * FurtherOptimized Squaresearches.
     *
     * @param array
     * @param start
     * @param end
     * @param key
     * @param sleep
     * @return no idea
     */
    public static int square(int[] array, int start, int end, int key, double sleep) {
        boolean low = true;
        while (start < end) {
            int squareMid;
            if (low) {
                squareMid = (int) Math.sqrt(start*end);
            } else {
                squareMid = (int) (end - (Math.sqrt(start*end) - start));
            }
            Highlights.markArray(1, start);
            Highlights.markArray(2, squareMid);
            Highlights.markArray(3, end);
            Delays.sleep(sleep);
            if (Reads.compareValues(array[squareMid], key) == -1) {
                end = squareMid;
                low = false;
            } else {
                start = squareMid + 1;
                low = true;
            }
        }
        return start;
    }

    /**
     * A proposal for an O({@code log n}) average comparisons searching algorithm.
     * (O({@code log}<sup>{@code 2}</sup> {@code n}) worst, O({@code 0}) best)
     * @param array
     * @param start
     * @param end
     * @param key
     * @param sleep
     * @return location
     */
    public static int slope(int[] array, int start, int end, int key, double sleep) {
        if (end-start == 0)
            return start;
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int i=start; i<end; i++) {
            int v = array[i];
            if (arrv.doingStabilityCheck()) {
                v = arrv.getStabilityValue(v);
            }
            if (max < v) {
                max = v;
            }
            if (min > v) {
                min = v;
            }
        }
        double approxRate = (max-min) / (double)(end - start);
        int approxIndex = (int) ((key - min) / approxRate) + start;
        if (approxIndex < start)
            return start;
        if (approxIndex > end)
            return end;
        //approxIndex = Math.min(Math.max(approxIndex, start), end);
        if (Reads.compareValues(array[approxIndex], key) >= 0) {
            return exponentialIterative(array, start, approxIndex, key, true, sleep);
        } else {
            return exponentialIterative(array, approxIndex, end, key, false, sleep);
        }
    }

    public static int search(int[] array, int start, int end, int key, Search search, double sleep) {
        switch(search) {
            case LINEAR:
                return linear(array, start, end, key, true, sleep);
            case BINARY:
                return binary(array, start, end, key, sleep);
            case EXPOLINEAR:
                return exponentialLinear(array, start, end, key, true, sleep);
            case EXPOBINARY:
                return exponentialBinary(array, start, end, key, false, sleep);
            case PUREEXPO:
                return exponentialIterative(array, start, end, key, sleep);
            case FIB:
            default:
                return fibonacci(array, start, end, key);
            case TRISEARCH:
                return trisearch(array, start, end, key, sleep);
            case TERNARY:
                return ternary(array, start, end, key, sleep);
            case RANDOM:
                return random(array, start, end, key, sleep);
            case EXTREMERANDOM:
                return extremeRandom(array, start, end, key, sleep);
            case SQUARE:
                return square(array, start, end, key, sleep);
            case SLOPE:
                return slope(array, start, end, key, sleep);
        }
    }
}