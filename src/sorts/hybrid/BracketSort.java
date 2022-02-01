package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BracketSort extends Sort {
    final static int NULL_VALUE = -1;

    int[] array;
    int length;

    int[] bracket;
    int[] origins;

    int bracketEnd, rootIndex;
    boolean isPow2Len;

    public BracketSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bracket");
        this.setRunAllSortsName("Bracket Sort");
        this.setRunSortName("Bracketsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean isPow2(int x) {
        return (x & (x - 1)) == 0;
    }

    protected void initBracket() {
        Writes.arraycopy(array, 0, bracket, 0, length, 1, true, true);

        int i, j, k, l;
        for (i = 0, j = 1, k = length, l = 0; j < length; i += 2, j += 2, k++, l++) {
            if (Reads.compareIndices(bracket, i, j, 0.5, true) > 0) {
                Writes.write(bracket, k, bracket[j], 0.5, false, true);
                Writes.write(origins, l, j, 0.5, false, true);
            } else {
                Writes.write(bracket, k, bracket[i], 0.5, false, true);
                Writes.write(origins, l, i, 0.5, false, true);
            }
        }

        int m, n;
        for (m = 0, n = 1; j < k; i += 2, j += 2, k++, l++, m += 2, n += 2) {
            if (Reads.compareIndices(bracket, i, j, 0.5, true) > 0) {
                Writes.write(bracket, k, bracket[j], 0.5, false, true);
                Writes.write(origins, l, origins[n], 0.5, false, true);
            } else {
                Writes.write(bracket, k, bracket[i], 0.5, false, true);
                Writes.write(origins, l, origins[m], 0.5, false, true);
            }
        }
    }

    protected void copyMinAndSift(int i) {
        int offset = 0;
        int level = 0;
        int root = origins[rootIndex];

        Writes.write(array, i, bracket[bracketEnd], 0.5, true, false);
        Writes.write(bracket, root, NULL_VALUE, 0.5, true, true);

        int other = (root & 1) == 0 ? root + 1 : root - 1;
        while (bracket[offset + other] == NULL_VALUE) {
            offset += length >> level;
            level++;
            root >>= 1;
            other = (root & 1) == 0 ? root + 1 : root - 1;
            Writes.write(bracket, offset + root, NULL_VALUE, 0.5, true, true);
        }

        int otherValue = bracket[offset + other];
        int otherOrigin = offset > 0 ? origins[offset + other - length] : other;
        offset += length >> level;
        level++;
        root >>= 1;
        other = (root & 1) == 0 ? root + 1 : root - 1;
        Writes.write(bracket, offset + root, otherValue, 0.5, true, true);
        Writes.write(origins, offset + root - length, otherOrigin, 0.5, false, true);

        while (offset < bracketEnd - 1) {
            int nextOffset = offset + (length >> level);
            int nextRoot = root >> 1;
            if (bracket[offset + other] == NULL_VALUE) {
                Writes.write(bracket, nextOffset + nextRoot, bracket[offset + root], 0.5, true, true);
                Writes.write(origins, nextOffset + nextRoot - length, origins[offset + root - length], 0.5, true, true);
            } else {
                if (Reads.compareIndices(bracket, offset + other, offset + root, 0.5, true) > 0) {
                    Writes.write(bracket, nextOffset + nextRoot, bracket[offset + root], 1, true, true);
                    Writes.write(origins, nextOffset + nextRoot - length, origins[offset + root - length], 0.5, true, true);
                } else {
                    Writes.write(bracket, nextOffset + nextRoot, bracket[offset + other], 1, true, true);
                    Writes.write(origins, nextOffset + nextRoot - length, origins[offset + other - length], 0.5, true, true);
                }
            }
            offset = nextOffset;
            level++;
            root = nextRoot;
            other = (nextRoot & 1) == 0 ? nextRoot + 1 : nextRoot - 1;
        }
    }

    protected String formatDebugBracketLen8() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("(%1$s-%2$s) (%3$s-%4$s) (%5$s-%6$s) (%7$s-%8$s)\n", bracket[0] + 1, bracket[1] + 1, bracket[2] + 1, bracket[3] + 1, bracket[4] + 1, bracket[5] + 1, bracket[6] + 1, bracket[7] + 1));
        builder.append(" |       |     |   |\n");
        builder.append(String.format("(%1$s-------%2$s)   (%3$s---%4$s)\n", bracket[8] + 1, bracket[9] + 1, bracket[10] + 1, bracket[11] + 1));
        builder.append(" |             |\n");
        builder.append(String.format("(%1$s-------------%2$s)\n", bracket[12] + 1, bracket[13] + 1));
        builder.append(" |\n");
        builder.append(String.format(" %1$s", bracket[14] + 1));
        return builder.toString();
    }

    protected void printDebugBracketLen8() {
        System.out.println(formatDebugBracketLen8());
        System.out.println();
    }

    protected void printDebugBracketLen8ANSI() {
        System.out.print(formatDebugBracketLen8());
        System.out.print("\u001b[2D\u001b6A");
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        bracket = Writes.createExternalArray(2 * currentLength - 1);
        origins = Writes.createExternalArray(currentLength - 1);
        this.array = array;
        this.length = currentLength;

        initBracket();
        bracketEnd = length * 2 - 2;
        rootIndex = bracketEnd - length;
        isPow2Len = isPow2(length);

        // printDebugBracketLen8();

        for (int i = 0; i < currentLength - 1; i++) {
            copyMinAndSift(i);
            // printDebugBracketLen8();
        }
        Writes.write(array, currentLength - 1, bracket[bracketEnd], 1, true, false);

        //Writes.deleteExternalArrays(bracket, origins);
    }
}
