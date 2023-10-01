package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// https://github.com/bhauth/shelfsort

public class ShelfSort extends Sort {
    public ShelfSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Shelf");
        this.setRunAllSortsName("Shelf Sort (Block Merge Sort)");
        this.setRunSortName("Shelf Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int SMALL_SORT = 4;
    private int[] indicesA, indicesB;

    public void smallSort(int[] array, int start) {
        int a = array[start    ],
            b = array[start + 1],
            c = array[start + 2],
            d = array[start + 3];

        int a2, b2, c2, d2, b3, c3;

        Highlights.markArray(1, start);
        Highlights.markArray(2, start + 1);
        if (Reads.compareValues(b, a) < 0) {
            a2 = b;
            b2 = a;
        } else {
            a2 = a;
            b2 = b;
        }

        Highlights.markArray(1, start + 2);
        Highlights.markArray(2, start + 3);
        if (Reads.compareValues(d, c) < 0) {
            c2 = d;
            d2 = c;
        } else {
            c2 = c;
            d2 = d;
        }
        Highlights.clearAllMarks();

        if (Reads.compareValues(b2, c2) < 0) {
            Writes.write(array, start    , a2, 1, true, false);
            Writes.write(array, start + 1, b2, 1, true, false);
            Writes.write(array, start + 2, c2, 1, true, false);
            Writes.write(array, start + 3, d2, 1, true, false);
            return;
        }

        if (Reads.compareValues(a2, c2) <= 0) {
            b3 = c2;
            Writes.write(array, start, a2, 1, true, false);
        } else {
            b3 = a2;
            Writes.write(array, start, c2, 1, true, false);
        }

        if (Reads.compareValues(b2, d2) <= 0) {
            c3 = b2;
            Writes.write(array, start + 3, d2, 1, true, false);
        } else {
            c3 = d2;
            Writes.write(array, start + 3, b2, 1, true, false);
        }

        if (Reads.compareValues(a2, d2) <= 0) {
            Writes.write(array, start + 1, b3, 1, true, false);
            Writes.write(array, start + 2, c3, 1, true, false);
        } else {
            Writes.write(array, start + 1, c3, 1, true, false);
            Writes.write(array, start + 2, b3, 1, true, false);
        }
    }

    public void mergePair(int[] array, int start1, int start2, int[] output, int outputStart, int n, boolean aux) {
        int i1 = n,
            i2 = n;

        for (int i = n * 2 + 1; i1 >= 0 && i2 >= 0; i--) {
            if (Reads.compareIndices(array, start2 + i2, start1 + i1, 1, true) < 0)
                 Writes.write(output, outputStart + i, array[start1 + i1--], 1, true, aux);
            else Writes.write(output, outputStart + i, array[start2 + i2--], 1, true, aux);
        }

        if (i1 >= 0) Writes.arraycopy(array, start1, output, outputStart, i1 + 1, 1, true, !aux);
        else         Writes.arraycopy(array, start2, output, outputStart, i2 + 1, 1, true, !aux);
    }

    private void blockMerge(int[] array, int start, int[] scratch, int indexStart, int blockCount1, int blockCount2, int blockSize) {
        int ii1              = blockCount1 - 1,
            ii2              = blockCount2 - 1,
            blockId1         = this.indicesA[indexStart + ii1],
            blockId2         = this.indicesA[indexStart + blockCount1 + ii2],
            p1               = start + blockId1 * blockSize,
            p2               = start + (blockCount1 + blockId2) * blockSize,
            outBlockCount    = blockCount1 + blockCount2 - 2,
            clearBlockId     = 0,
            nextClearBlockId = 0,
            i                = blockSize * 2 - 1,
            i1               = blockSize - 1,
            i2               = i1,
            outputStart      = 0;

        int[]   output    = scratch;
        boolean outputAux = true;

        if (Reads.compareIndices(
            array,
            p1 + (this.indicesA[indexStart] * blockSize) + blockSize - 1,
            start + (blockCount1 + this.indicesA[indexStart + blockCount1]) * blockSize,
            1, true
        ) <= 0) {
            for (i = 0; i < blockCount1; i++)
                Writes.write(this.indicesB, indexStart + i, this.indicesA[indexStart + i], 1, true, true);

            for (i = blockCount1; i < blockCount1 + blockCount2; i++)
                Writes.write(this.indicesB, indexStart + i, this.indicesA[indexStart + i] + blockCount1, 1, true, true);

            return;
        }

        while (true) {
            for (; i1 >= 0 && i2 >= 0 && i >= 0; i--) {
                if (Reads.compareIndices(array, p2 + i2, p1 + i1, 1, true) < 0)
                     Writes.write(output, outputStart + i, array[p1 + i1--], 1, true, outputAux);
                else Writes.write(output, outputStart + i, array[p2 + i2--], 1, true, outputAux);
            }

            if (i < 0) {
                outputStart = start + nextClearBlockId * blockSize;
                outputAux   = false;
                output      = array;

                outBlockCount--;
                Writes.write(this.indicesB, indexStart + outBlockCount, nextClearBlockId, 1, true, true);
                i = blockSize - 1;
            }

            if (i1 < 0) {
                nextClearBlockId = blockId1;

                if (--ii1 < 0) {
                    while (true) {
                        for (; i2 >= 0 && i >= 0; i2--, i--)
                            Writes.write(output, outputStart + i, array[p2 + i2], 1, true, outputAux);

                        if (i < 0) {
                            clearBlockId = nextClearBlockId;

                            outputStart = start + nextClearBlockId * blockSize;
                            outputAux   = false;
                            output      = array;

                            if (i2 >= 0)
                                Writes.write(this.indicesB, indexStart + outBlockCount, nextClearBlockId, 1, true, true);

                            outBlockCount--;
                            i = blockSize - 1;
                        }

                        if (i2 < 0) {
                            nextClearBlockId = blockCount1 + blockId2;

                            if (--ii2 < 0) break;

                            blockId2 = this.indicesA[indexStart + blockCount1 + ii2];
                            p2       = start + (blockCount1 + blockId2) * blockSize;
                            i2       = blockSize - 1;
                        }
                    }

                    break;
                }

                blockId1 = this.indicesA[indexStart + ii1];
                p1       = start + blockId1 * blockSize;
                i1       = blockSize - 1;
            }

            if (i2 < 0) {
                nextClearBlockId = blockCount1 + blockId2;

                if (--ii2 < 0) {
                    while (true) {
                        for (; i1 >= 0 && i >= 0; i1--, i--)
                            Writes.write(output, outputStart + i, array[p1 + i1], 1, true, outputAux);

                        if (i < 0) {
                            clearBlockId = nextClearBlockId;

                            outputStart = start + nextClearBlockId * blockSize;
                            outputAux   = false;
                            output      = array;

                            if (i1 >= 0)
                                Writes.write(this.indicesB, indexStart + outBlockCount, nextClearBlockId, 1, true, true);

                            outBlockCount--;
                            i = blockSize - 1;
                        }

                        if (i1 < 0) {
                            nextClearBlockId = blockId1;

                            if (--ii1 < 0) break;

                            blockId1 = this.indicesA[indexStart + ii1];
                            p1       = start + blockId1 * blockSize;
                            i1       = blockSize - 1;
                        }
                    }

                    break;
                }

                blockId2 = this.indicesA[indexStart + blockCount1 + ii2];
                p2       = start + (blockCount1 + blockId2) * blockSize;
                i2       = blockSize - 1;
            }
        }

        Writes.arraycopy(scratch, 0, output, outputStart, blockSize, 1, true, outputAux);
        Writes.arraycopy(scratch, blockSize, array, start + nextClearBlockId * blockSize, blockSize, 1, true, outputAux);
        int last = blockCount1 + blockCount2 - 1;
        Writes.write(this.indicesB, indexStart + last - 1,     clearBlockId, 1, true, true);
        Writes.write(this.indicesB, indexStart + last    , nextClearBlockId, 1, true, true);
    }

    private void finalBlockSorting(int[] array, int start, int[] scratch, int blocks, int blockSize) {
        for (int b = 0; b < blocks; b++) {
            int ix = this.indicesA[b];

            if (ix != b) {
                Writes.arraycopy(array, start + b * blockSize, scratch, 0, blockSize, 1, true, true);
                int emptyBlock = b;

                while (ix != b) {
                    Writes.arraycopy(array, start + ix * blockSize, array, start + emptyBlock * blockSize, blockSize, 1, true, false);
                    Writes.write(this.indicesA, emptyBlock, emptyBlock, 1, true, true);
                    emptyBlock = ix;
                    ix = this.indicesA[ix];
                }

                Writes.arraycopy(scratch, 0, array, start + emptyBlock * blockSize, blockSize, 1, true, false);
                Writes.write(this.indicesA, emptyBlock, emptyBlock, 1, true, true);
            }
        }
    }

    public void sort(int[] array, int start, int size) {
        int logSize = 0,
                  v = size;

        while ((v >>>= 1) != 0) logSize++;

        int scratchSize = 1 << (2 + (logSize + 1) / 2);

        for (int i = 0; i < size; i += SMALL_SORT)
            smallSort(array, start + i);

        int[] scratch = Writes.createExternalArray(scratchSize);
        this.indicesA = Writes.createExternalArray(scratchSize);
        this.indicesB = Writes.createExternalArray(scratchSize);

        int sortedZoneSize = SMALL_SORT, runLen;

        for (; sortedZoneSize < scratchSize / 2; sortedZoneSize *= 2) {
            runLen = sortedZoneSize;
            sortedZoneSize *= 2;

            for (int i = 0; i < size; i += sortedZoneSize * 2) {
                int p1 = start + i,
                    p2 = start + i + runLen,
                    p3 = p2 + runLen,
                    p4 = p3 + runLen;

                boolean less1 = Reads.compareIndices(array, p2 - 1, p2, 1, true) <= 0,
                        less2 = Reads.compareIndices(array, p4 - 1, p4, 1, true) <= 0;

                if (!less1)
                    mergePair(array, p1, p2, scratch, 0, runLen - 1, true);

                if (!less2)
                    mergePair(array, p3, p4, scratch, sortedZoneSize, runLen - 1, true);

                if (less1 || less2) {
                    if (!less1)
                        Writes.arraycopy(array, p3, scratch, sortedZoneSize, sortedZoneSize, 1, true, true);
                    else if (!less2)
                        Writes.arraycopy(array, p1, scratch, 0, sortedZoneSize, 1, true, true);
                    else if (Reads.compareIndices(array, p1 + sortedZoneSize - 1, p3, 1, true) <= 0)
                        continue;
                    else Writes.arraycopy(array, p1, scratch, 0, sortedZoneSize * 2, 1, true, true);
                }

                mergePair(scratch, 0, sortedZoneSize, array, p1, sortedZoneSize - 1, false);
            }
        }

        int blockSize    = scratchSize / 2,
            totalBlocks  = size / blockSize,
            blocksPerRun = sortedZoneSize / blockSize;

        for (int i = 0; i < totalBlocks; i += blocksPerRun)
            for (int j = 0; j < blocksPerRun; j++)
                Writes.write(this.indicesA, i + j, j, 0.25, true, true);

        while (sortedZoneSize <= (size / 2)) {
            runLen = sortedZoneSize;
            int blocks1 = sortedZoneSize / blockSize,
                blocks2 = blocks1;
            sortedZoneSize *= 2;

            for (int i = 0; i < totalBlocks; i += blocks1 + blocks2)
                this.blockMerge(array, start + i * blockSize, scratch, i, blocks1, blocks2, blockSize);

            for (int i = 0; i < this.indicesA.length; i++) {
                int tmp = this.indicesA[i];
                Writes.write(this.indicesA, i, this.indicesB[i], 1, true, true);
                Writes.write(this.indicesB, i, tmp,              1, true, true);
            }
        }

        Highlights.clearAllMarks();
        this.finalBlockSorting(array, start, scratch, sortedZoneSize / blockSize, blockSize);

        Writes.deleteExternalArrays(scratch, this.indicesA, this.indicesB);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
