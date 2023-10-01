package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class DistaysStableSort extends Sort {
    public DistaysStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Distay's Stable");
        this.setRunAllSortsName("Distay's Stable Sort");
        this.setRunSortName("Distay's Stable Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void tailMerge(int[] array, int[] tmp, int a, int m, int b, int routine) {
        if (routine == 3) {
            int l = m, M;
            while (l < b) {
                M = l + (b - l) / 2;
                if (Reads.compareIndices(array, M, m-1, 1, true) < 0) {
                    l = M + 1;
                } else {
                    b = M;
                }
            }
        }
        if (routine == 0 || routine == 3) {
            Writes.arraycopy(array, m, tmp, 0, b-m, 1, true, true);
        }
        int l = m-1, r = b-m-1;
        while (l >= a && r >= 0) {
            Highlights.markArray(2, --b);
            if (Reads.compareIndexValue(array, l, tmp[r], 1, true) > (routine == 1 ? -1 : 0))
                Writes.write(array, b, array[l--], 0, true, false);
            else
                Writes.write(array, b, tmp[r--], 0, true, false);
        }
        while (r >= 0)
            Writes.write(array, --b, tmp[r--], 1, true, false);
    }
    private void merge(int[] array, int[] tmp, int a, int m, int b, int t, boolean aux) {
        int l = a, r = m;
        while (l < m && r < b) {
            if (!aux)
                Highlights.markArray(3, t);
            if (Reads.compareIndices(array, l, r, 1, true) <= 0)
                Writes.write(tmp, t++, array[l++], 0, true, aux);
            else
                Writes.write(tmp, t++, array[r++], 0, true, aux);
        }
        while (l < m)
            Writes.write(tmp, t++, array[l++], 1, true, aux);
        while (r < b)
            Writes.write(tmp, t++, array[r++], 1, true, aux);
    }
    private void headMerge(int[] array, int[] tmp, int a, int m, int b) {
        int l=0, le=m-a, r = m;
        while (l < le && r < b) {
            Highlights.markArray(2, r);
            if (Reads.compareValueIndex(array, tmp[l], r, 1, true) <= 0) {
                Writes.write(array, a++, tmp[l++], 0, true, false);
            } else {
                Writes.write(array, a++, array[r++], 0, true, false);
            }
        }
        while (l < le)
            Writes.write(array, a++, tmp[l++], 1, true, false);
    }
    private void mergeRoutine(int[] array, int[] tmp, int a, int b, int g) {
        int k = 0;
        if (b - a > 16)
            k = (           Reads.compareIndices(array, a+g-1,   a+g,   10, true) <= 0 ? 1 : 0) |
                (a+2*g<b && Reads.compareIndices(array, a+2*g-1, a+2*g, 10, true) <= 0 ? 2 : 0) |
                (a+3*g<b && Reads.compareIndices(array, a+3*g-1, a+3*g, 10, true) <= 0 ? 4 : 0);
        switch(k) {
            case 2:
                if (a+3*g >= b) {
                    Writes.arraycopy(array, a, tmp, 0, g, 1, true, false);
                    headMerge(array, tmp, a, a+g, b);
                    break;
                }
            case 0:
                merge(array, tmp, a, a+g, Math.min(a+2*g, b), 0, true);
                if (a+3*g < b) {
                    merge(array, array, a+2*g, a+3*g, b, a, false);
                    tailMerge(array, tmp, a, a+(b-a-2*g), b, 1);
                } else if (a+2*g < b) {
                    headMerge(array, tmp, a, a+2*g, b);
                } else {
                    Writes.arraycopy(tmp, 0, array, a, b-a, 1, true, false);
                }
                break;
            case 1:
                if (a+3*g < b) {
                    merge(array, tmp, a+2*g, a+3*g, b, 0, false);
                    tailMerge(array, tmp, a, a+2*g, b, 2);
                } else if (a+2*g < b) {
                    tailMerge(array, tmp, a, a+2*g, b, 0);
                }
            case 3:
                if (a+3*g < b)
                    tailMerge(array, tmp, a, a+3*g, b, 3);
                break;
            case 4:
                merge(array, tmp, a, a+g, Math.min(a+2*g, b), 0, true);
                headMerge(array, tmp, a, a+2*g, b);
                break;
            case 5:
                tailMerge(array, tmp, a, a+2*g, b, 3);
                break;
            case 6:
                int l = a, r = a + g, M;
                while (l < r) {
                    M = l + (r - l) / 2;
                    if (Reads.compareIndices(array, a + g, M, 1, true) <= 0) {
                        r = M;
                    } else {
                        l = M + 1;
                    }
                }
                Writes.arraycopy(array, l, tmp, 0, g-(l-a), 1, true, false);
                headMerge(array, tmp, l, a+g, b);
                break;
        }
    }

    public void stableSort(int[] array, int a, int b) {
        int[] t = Writes.createExternalArray((b-a)/2);
        int i=1;
        for (; i<=(b-a)/4; i*=4) {
            for (int j=a; j+i<b; j+=4*i) {
                mergeRoutine(array, t, j, Math.min(j+4*i, b), i);
            }
        }
        while (i<b-a) {
            for (int j=a; j+i<b; j+=2*i)
                tailMerge(array, t, j, j+i, Math.min(j+2*i, b), 3);
            i*=2;
        }
        Writes.deleteExternalArray(t);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.stableSort(array, 0, length);
    }
}