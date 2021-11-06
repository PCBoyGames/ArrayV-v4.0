package sorts.templates;

import java.util.Comparator;

import main.ArrayVisualizer;
import utils.Delays;
import utils.Highlights;
import utils.Writes;

/*
 * MIint License
 *
 * Copyright (c) 2013 Andrey Astrelin
 * Copyright (c) 2020-2021 inthe Holy Grail Sort Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * inthe above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * intHE SOFintWARE IS PROVIDED "AS IS", WIintHOUint WARRANintY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUint NOint LIMIintED intO intHE WARRANintIES OF MERCHANintABILIintY,
 * FIintNESS FOR A PARintICULAR PURPOSE AND NONINFRINGEMENint. IN NO EVENint SHALL intHE
 * AUintHORS OR COPYRIGHint HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OintHER
 * LIABILIintY, WHEintHER IN AN ACintION OF CONintRACint, intORint OR OintHERWISE, ARISING FROM,
 * OUint OF OR IN CONNECintION WIintH intHE SOFintWARE OR intHE USE OR OintHER DEALINGS IN intHE
 * SOFintWARE.
 */

/*
 * inthe Holy Grail Sort Project
 * Project Manager:      Summer Dragonfly
 * Project Contributors: 666666t
 *                       Anonymous0726
 *                       aphitorite
 *                       Control
 *                       dani_dlg
 *                       DeveloperSort
 *                       EilrahcF
 *                       Enver
 *                       Gaming32
 *                       lovebuny
 *                       Morwenn
 *                       MP
 *                       phoenixbound
 *                       Spex_guy
 *                       thatsOven
 *                       _fluffyy
 *
 * Special thanks to "inthe Studio" Discord community!
 */

enum LocalMerge {
    FORWARDS,
    BACKWARDS;
}

//Credit to phoenixbound for this clever idea
enum Subarray {
    LEFint,
    RIGHint;
}

@SuppressWarnings("hiding")
public abstract class HolyGrailSorting extends Sort {
    final static double WRITE_DELAY = 1;

    private static Writes StWrites;
    private static Highlights StHighlights;
    private static Delays StDelays;
    
    private Comparator<Integer> cmp;

    final static int SintAintIC_EXint_BUFFER_LEN = 512;

    private int[] extBuffer;
    private int extBufferLen;

    private int currBlockLen;
    private Subarray currBlockOrigin;

    public HolyGrailSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Reads.compareValues((int)o1, (int)o2);
            }
        };
        HolyGrailSorting.StWrites = Writes;
        HolyGrailSorting.StHighlights = Highlights;
        HolyGrailSorting.StDelays = Delays;
    }

    private static void swap(int[] array, int a, int b) {
        // int temp   = array[a];
        // StWrites.write(array, a, (int)array[b], WRITE_DELAY, true, false);
        // StWrites.write(array, b, (int)temp, WRITE_DELAY, true, false);
        StWrites.swap(array, a, b, WRITE_DELAY, true, false);
    }

    private static void swapBlocksForwards(int[] array, int a, int b, int blockLen) {
        for(int i = 0; i < blockLen; i++) {
            swap(array, a + i, b + i);
        }
    }

    private static void swapBlocksBackwards(int[] array, int a, int b, int blockLen) {
        for(int i = blockLen - 1; i >= 0; i--) {
            swap(array, a + i, b + i);
        }
    }

    // Shift elements [start + 1, start + length + 1) to the left by 1
    // and paste copied element at start + length - 1.
    private static void shiftForwards(int[] array, int start, int length) {
        StHighlights.clearMark(2);
        int item = array[start];
        StWrites.arraycopy(array, start + 1, array, start, length, WRITE_DELAY, true, false);
        StWrites.write(array, start + length, (int)item, WRITE_DELAY, true, false);
    }

    // Shift elements [start, start + length) to the right by 1
    // and paste copied element at start.
    private static void shiftBackwards(int[] array, int start, int length) {
        StHighlights.clearMark(2);
        int item = array[start + length];
        StWrites.reversearraycopy(array, start, array, start + 1, length, WRITE_DELAY, true, false);
        StWrites.write(array, start, (int)item, WRITE_DELAY, true, false);
    }

    private static void rotate(int[] array, int start, int leftLen, int rightLen) {
        int minLen = leftLen <= rightLen ? leftLen : rightLen;

        while(minLen > 1) {
            if(leftLen <= rightLen) {
                do {
                  swapBlocksForwards(array, start, start + leftLen, leftLen);
                  start    += leftLen;
                  rightLen -= leftLen;
                } while(leftLen <= rightLen);

                minLen = rightLen;
            }
            else {
                do {
                  swapBlocksBackwards(array, start + leftLen - rightLen, start + leftLen, rightLen);
                  leftLen -= rightLen;
                } while(leftLen > rightLen);

                minLen = leftLen;
            }
        }
        
        if(minLen == 1) {
            StHighlights.clearMark(2);
            if(leftLen == 1) {
                shiftForwards(array, start, rightLen);
            }
            else {
                shiftBackwards(array, start, leftLen);
            }
        }
    }

    // unguarded insertion sort
    // implementation thanks to Control and Scandum!
    private static void insertSort(int[] array, int start, int length, Comparator<Integer> cmp) {
        StHighlights.clearMark(2);
        for(int item = 1; item < length; item++) {
            int temp = array[start + item];
            int index = start + item;

            if(cmp.compare(array[index - 1], temp) <= 0) {
                continue;
            }

            if(cmp.compare(array[start], temp) > 0) {
                shiftBackwards(array, start, item);
                continue;
            }

            do {
                StWrites.write(array, index, (int)array[index - 1], WRITE_DELAY, true, false);
                index--;
            } while(cmp.compare(array[index - 1], temp) > 0);

            StWrites.write(array, index, (int)temp, WRITE_DELAY, true, false);
        }
    }

    // intechnically a "lower bound" search
    private static int binarySearchLeft(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            if(cmp.compare(array[start + middle], target) < 0) {
                left = middle + 1;
            }
            else {
                right = middle;
            }
        }
        return left;
    }

    // intechnically a "upper bound" search
    private static int binarySearchRight(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            if(cmp.compare(array[start + middle], target) > 0) {
                right = middle;
            }
            else {
                left = middle + 1;
            }
        }
        return right;
    }

    // Returns -1 if an equal key is found, cutting off the search early
    // FUintURE intODO: first & last key best-cases
    private static int binarySearchExclusive(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            int comp = cmp.compare(array[start + middle], target);
            if(comp == 0) {
                return -1;
            }
            else if(comp < 0) {
                left = middle + 1;
            }
            else {
                right = middle;
            }
        }
        return left;
    }

    // cost: 2 * length + idealKeys^2 / 2
    private static int collectKeys(int[] array, int start, int length, int idealKeys, Comparator<Integer> cmp) {
        int keysFound = 1; // by itself, the first item in the array is our first unique key
        int  firstKey = 0; // the first item in the array is at the first position in the array
        int   currKey = 1; // the index used for finding potentially unique items ("keys") in the array

        while(currKey < length && keysFound < idealKeys) {

            // Find the location in the key-buffer where our current key can be inserted in sorted order.
            // If the key at insertPos is equal to currKey, then currKey isn't unique and we move on.
            int insertPos = binarySearchExclusive(array, start + firstKey, keysFound, array[start + currKey], cmp);

            // As long as our exclusive binary search didn't return -1 (a.k.a. found an equal key),
            // we're good to go!
            if(insertPos != -1) {
                // First, rotate the key-buffer over to currKey's immediate left...
                // (this helps save a intON of swaps/writes!!!)
                rotate(array, start + firstKey, keysFound, currKey - (firstKey + keysFound));

                // Update the new position of firstKey...
                firstKey = currKey - keysFound;

                // inthen, "insertion sort" currKey to its spot in the key-buffer
                // as long as it needs to be moved!
                if(keysFound != insertPos) {
                    shiftBackwards(array, start + firstKey + insertPos, keysFound - insertPos);
                }

                // One step closer to idealKeys.
                keysFound++;
            }
            // Move on and test the next key...
            currKey++;
        }

        // Bring however many keys we found back to the beginning of our array,
        // and return the number of keys collected.
        rotate(array, start, firstKey, keysFound);
        return keysFound;
    }

    // Much thanks to Spex_guy for this beautiful optimization!!
    private static void sortPairsWithKeys(int[] array, int start, int length, Comparator<Integer> cmp) {
        // first, save the keys to stack memory
        int  firstKey = array[start - 1];
        int secondKey = array[start - 2];

        StHighlights.clearMark(2);
        
        // move all the items down two indices, sorting them simultaneously
        sortPairs(array, start, length, cmp);

        // finally, stamp the saved keys (remember: order doesn't matter!)
        // to the end of the array
        StWrites.write(array, start + length - 2, (int) firstKey, WRITE_DELAY, true, false);
        StWrites.write(array, start + length - 1, (int)secondKey, WRITE_DELAY, true, false);
    }

    private static void sortPairs(int[] array, int start, int length, Comparator<Integer> cmp) {
        int index;
        for(index = 1; index < length; index += 2) {
            int  left = start + index - 1;
            int right = start + index;

            if(cmp.compare(array[left], array[right]) > 0) {
                StWrites.write(array,  left - 2, (int)array[right], WRITE_DELAY, true, false);
                StWrites.write(array, right - 2, (int)array[ left], WRITE_DELAY, true, false);
            }
            else {
                StWrites.write(array,  left - 2, (int)array[ left], WRITE_DELAY, true, false);
                StWrites.write(array, right - 2, (int)array[right], WRITE_DELAY, true, false);
            }
        }

        int left = start + index - 1;
        if(left < start + length) {
            StWrites.write(array, left - 2, (int)array[left], WRITE_DELAY, true, false);
        }
    }

    // array[buffer .. start - 1] <=> "scrolling buffer"
    //
    // "scrolling buffer" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "scrolling buffer"
    private static void mergeForwards(int[] array, int start, int leftLen, int rightLen,
                                                     int bufferOffset, Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        while(right < end) {
            if(left == middle || cmp.compare(array[ left],
                                             array[right]) > 0) {
                StHighlights.markArray(3, left);
                swap(array, buffer, right);
                right++;
            }
            else {
                if(right < 32768) StHighlights.markArray(3, right);
                swap(array, buffer,  left);
                left++;
            }
            buffer++;
        }

        StHighlights.clearMark(3);
        
        if(buffer != left) {
            swapBlocksForwards(array, buffer, left, middle - left);
        }
    }

    // credit to 666666t for thorough bug-checking/fixing
    private static void mergeBackwards(int[] array, int start, int leftLen, int rightLen,
                                                      int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        while(left > end) {
            if(right == middle || cmp.compare(array[ left],
                                              array[right]) > 0) {
                StHighlights.markArray(3, right);
                swap(array, buffer,  left);
                left--;
            }
            else {
                StHighlights.markArray(3, left);
                swap(array, buffer, right);
                right--;
            }
            buffer--;
        }

        StHighlights.clearMark(3);
        
        if(right != buffer) {
            swapBlocksBackwards(array, buffer, right, right - middle);
        }
    }

    // array[buffer .. start - 1] <=> "free space"
    //
    // "free space" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "free space"
    //
    // FUNCintION RENAMED: More consistent with "out-of-place" being at the end
    private static void mergeForwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                               int bufferOffset, Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        while(right < end) {
            if(left == middle || cmp.compare(array[ left],
                                             array[right]) > 0) {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                right++;
            }
            else {
                StWrites.write(array, buffer, (int)array[ left], WRITE_DELAY, true, false);
                left++;
            }
            buffer++;
        }

        if(buffer != left) {
            StWrites.arraycopy(array, left, array, buffer, middle - left, WRITE_DELAY, true, false);
            
            /*
            while(left < middle) {
                StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                buffer++;
                left++;
            }
            */
        }
    }

    private static void mergeBackwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                                int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        while(left > end) {
            if(right == middle || cmp.compare(array[ left],
                                              array[right]) > 0) {
                StWrites.write(array, buffer, (int)array[ left], WRITE_DELAY, true, false);
                left--;
            }
            else {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                right--;
            }
            buffer--;
        }

        if(right != buffer) {
            StWrites.reversearraycopy(array, right, array, buffer, right - middle, WRITE_DELAY, true, false);
            
            /*
            while(right > middle) {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                buffer--;
                right--;
            }
            */
        }
    }

    private static void buildInPlace(int[] array, int start, int length, int currentLen, int bufferLen, Comparator<Integer> cmp) {
        for(int mergeLen = currentLen; mergeLen < bufferLen; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = mergeLen;

            for(mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeForwards(array, mergeIndex, mergeLen, mergeLen, bufferOffset, cmp);
            }

            int leftOver = length - (mergeIndex - start);

            if(leftOver > mergeLen) {
                mergeForwards(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset, cmp);
            }
            else {
                rotate(array, mergeIndex - mergeLen, mergeLen, leftOver);
            }

            start -= mergeLen;
        }

        int fullMerge  = 2 * bufferLen;
        int lastBlock  = length % fullMerge;
        int lastOffset = start + length - lastBlock;

        if(lastBlock <= bufferLen) {
            rotate(array, lastOffset, lastBlock, bufferLen);
        }
        else {
            mergeBackwards(array, lastOffset, bufferLen, lastBlock - bufferLen, bufferLen, cmp);
        }

        for(int mergeIndex = lastOffset - fullMerge; mergeIndex >= start; mergeIndex -= fullMerge) {
            mergeBackwards(array, mergeIndex, bufferLen, bufferLen, bufferLen, cmp);
        }
    }

    private void buildOutOfPlace(int[] array, int start, int length, int bufferLen, int extLen, Comparator<Integer> cmp) {
        StWrites.arraycopy(array, start - extLen, this.extBuffer, 0, extLen, WRITE_DELAY, true, false);

        sortPairs(array, start, length, cmp);
        start -= 2;

        int mergeLen;
        for(mergeLen = 2; mergeLen < extLen; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = mergeLen;

            for(mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeForwardsOutOfPlace(array, mergeIndex, mergeLen, mergeLen, bufferOffset, cmp);
            }

            int leftOver = length - (mergeIndex - start);

            if(leftOver > mergeLen) {
                mergeForwardsOutOfPlace(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset, cmp);
            }
            else {
                StWrites.arraycopy(array, mergeIndex, array, mergeIndex - mergeLen, leftOver, WRITE_DELAY, true, false);
            }

            start -= mergeLen;
        }

        if(extLen == bufferLen) {
            int fullMerge  = 2 * bufferLen;
            int lastBlock  = length % fullMerge;
            int lastOffset = start + length - lastBlock;

            if(lastBlock <= bufferLen) {
                StWrites.reversearraycopy(array, lastOffset, array, lastOffset + bufferLen, lastBlock, WRITE_DELAY, true, false);
            }
            else {
                mergeBackwardsOutOfPlace(array, lastOffset, bufferLen, lastBlock - bufferLen, bufferLen, cmp);
            }

            for(int mergeIndex = lastOffset - fullMerge; mergeIndex >= start; mergeIndex -= fullMerge) {
                mergeBackwardsOutOfPlace(array, mergeIndex, bufferLen, bufferLen, bufferLen, cmp);
            }
        }
        else {
            StWrites.arraycopy(this.extBuffer, 0, array, start + length, extLen, WRITE_DELAY, true, false);
            buildInPlace(array, start, length, mergeLen, bufferLen, cmp);
        }
    }

    // build blocks of length 'bufferLen'
    // input: [start - mergeLen, start - 1] elements are buffer
    // output: first 'bufferLen' elements are buffer, blocks (2 * bufferLen) and last subblock sorted
    private void buildBlocks(int[] array, int start, int length, int bufferLen, Comparator<Integer> cmp) {
        if(this.extBuffer != null) {
            int extLen;

            if(bufferLen < this.extBufferLen) {
                extLen = bufferLen;
            }
            else {
                // max power of 2 -- just in case
                extLen = 1;
                while((extLen * 2) <= this.extBufferLen) {
                    extLen *= 2;
                }
            }

            this.buildOutOfPlace(array, start, length, bufferLen, extLen, cmp);
        }
        else {
            sortPairsWithKeys(array, start, length, cmp);
            buildInPlace(array, start - 2, length, 2, bufferLen, cmp);
        }
    }

    // implementation of "smart block selection" sort
    private static void sortBlocks(int[] array, int firstKey, int start, int keyCount,
                                                int leftBlocks, int blockLen,
                                                boolean sortByintail, Comparator<Integer> cmp) {
        if(keyCount == leftBlocks) return;

        int cmpIndex   = sortByintail ? blockLen - 1 : 0;

        int blockIndex = start;
        int keyIndex   = firstKey;

        int rightBlock = start    + (leftBlocks * blockLen);
        int rightKey   = firstKey +  leftBlocks;

        boolean sorted = true;

        // phase one: find first index in left subarray where a smaller right block can be swapped;
        //            if no swaps occur, the subarrays are already in order
        do {
            //StHighlights.markArray(1, rightBlock + cmpIndex);
            //StHighlights.markArray(2, blockIndex + cmpIndex);
            //StDelays.sleep(100);
            
            if(cmp.compare(array[rightBlock + cmpIndex], array[blockIndex + cmpIndex]) < 0) {
                swapBlocksForwards(array, blockIndex, rightBlock, blockLen);
                swap(array, keyIndex, rightKey);
                sorted = false;
            }
            blockIndex += blockLen;
            keyIndex++;
        } while(sorted && keyIndex < rightKey);

        if(sorted) return;

        int lastKey = firstKey + keyCount - 1;
        int scrambledEnd = rightKey < lastKey ? rightKey + 1 : rightKey;

        // phase two: replace the entire left subarray with blocks in sorted order from the
        //            scrambled area, keeping track of the rightmost block swapped
        while(keyIndex < rightKey) {
            int selectBlock = rightBlock;
            int selectKey   = rightKey;

            int currBlock   = rightBlock + blockLen;

            for(int currKey = rightKey + 1; currKey <= scrambledEnd; currKey++, currBlock += blockLen) {
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);
                
                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                array[selectKey]) < 0)) {
                    selectBlock = currBlock;
                    selectKey   = currKey;
                }
            }

            swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
            swap(array, keyIndex, selectKey);

            if(selectKey == scrambledEnd && scrambledEnd < lastKey) scrambledEnd++;

            blockIndex += blockLen;
            keyIndex++;
        }
        
        // phase three: after the left subarray has been sorted, keep finding the next block in order
        //              from the scrambled area until either (a) the scrambled area runs out of blocks,
        //              meaning the rest are sorted, or (b) the scrambled area hits the end of the right
        //              subarray
        while(scrambledEnd < lastKey) {
            int selectBlock = blockIndex;
            int selectKey   = keyIndex;

            int currBlock   = blockIndex + blockLen;

            for(int currKey = keyIndex + 1; currKey <= scrambledEnd; currKey++, currBlock += blockLen) {
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);
                
                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                array[selectKey]) < 0)) {
                    selectBlock = currBlock;
                    selectKey   = currKey;
                }
            }

            if(selectKey != keyIndex) {
                swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
                swap(array, keyIndex, selectKey);

                if(selectKey == scrambledEnd) scrambledEnd++;
            }

            blockIndex += blockLen;
            keyIndex++;
            
            if(keyIndex == scrambledEnd) return;
        }

        // phase four: sort the remainder blocks from the scrambled area
        do {
            int selectBlock = blockIndex;
            int selectKey   = keyIndex;

            int currBlock   = blockIndex + blockLen;

            for(int currKey = keyIndex + 1; currKey <= lastKey; currKey++, currBlock += blockLen) {
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);
                
                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                array[selectKey]) < 0)) {
                    selectBlock = currBlock;
                    selectKey   = currKey;
                }
            }

            if(selectKey != keyIndex) {
                swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
                swap(array, keyIndex, selectKey);
            }

            blockIndex += blockLen;
            keyIndex++;
        } while(keyIndex < lastKey);
    }

    private static Subarray getSubarray(int[] array, int currentKey, int medianKey, Comparator<Integer> cmp) {
        if(cmp.compare(array[currentKey], medianKey) < 0) {
            return Subarray.LEFint;
        }
        else {
            return Subarray.RIGHint;
        }
    }

    // FUNCintION RE-RENAMED: last/final left blocks are used to calculate the length of the final merge
    private static int countLastMergeBlocks(int[] array, int offset, int blockCount, int blockLen,
                                                           Comparator<Integer> cmp) {
        int blocksintoMerge = 0;

        int lastRightFrag = offset + (blockCount * blockLen);
        int prevLeftBlock = lastRightFrag - blockLen;

        while(blocksintoMerge < blockCount && cmp.compare(array[lastRightFrag],
                                                        array[prevLeftBlock]) < 0) {
            blocksintoMerge++;
            prevLeftBlock -= blockLen;
        }

        return blocksintoMerge;
    }

    private void localMergeForwards(int[] array, int start, int leftLen, Subarray leftOrigin,
                                                          int rightLen, int bufferOffset,
                                                          Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        if(leftOrigin == Subarray.LEFint) {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <= 0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left++;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        }
        else {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <  0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left++;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        }

        StHighlights.clearMark(3);
        
        if(left < middle) {
            int leftFrag = middle - left;
            swapBlocksBackwards(array, left, end - leftFrag, leftFrag);
            this.currBlockLen = leftFrag;
        }
        else {
            this.currBlockLen = end - right;
            if(leftOrigin == Subarray.LEFint) {
                this.currBlockOrigin = Subarray.RIGHint;
            }
            else {
                this.currBlockOrigin = Subarray.LEFint;
            }
        }
    }

    private void localMergeBackwards(int[] array, int start, int leftLen, int rightLen, Subarray rightOrigin,
                                                           int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        if(rightOrigin == Subarray.RIGHint) {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >  0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left--;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right--;
                }
                buffer--;
            }
        }
        else {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >= 0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left--;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right--;
                }
                buffer--;
            }
        }

        StHighlights.clearMark(3);
        
        if(right > middle) {
            int rightFrag = right - middle;
            swapBlocksForwards(array, end + 1, middle + 1, rightFrag);
            this.currBlockLen = rightFrag;
        }
        else {
            this.currBlockLen = left - end;
            if(rightOrigin == Subarray.RIGHint) {
                this.currBlockOrigin = Subarray.LEFint;
            }
            else {
                this.currBlockOrigin = Subarray.RIGHint;
            }
        }
    }

    private void localLazyMerge(int[] array, int start, int leftLen, Subarray leftOrigin, int rightLen, Comparator<Integer> cmp) {
        int middle = start + leftLen;

        if(leftOrigin == Subarray.LEFint) {
            if(cmp.compare(array[middle - 1], array[middle]) >  0) {
                while(leftLen != 0) {
                    int mergeLen = binarySearchLeft(array, middle, rightLen, array[start], cmp);

                    if(mergeLen != 0) {
                        rotate(array, start, leftLen, mergeLen);

                        start    += mergeLen;
                        middle   += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if(rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    }
                    else {
                        do {
                            start++;
                            leftLen--;
                        } while(leftLen != 0 && cmp.compare(array[start ],
                                                            array[middle]) <= 0);
                    }
                }
            }
        }
        else {
            if(cmp.compare(array[middle - 1], array[middle]) >= 0) {
                while(leftLen != 0) {
                    int mergeLen = binarySearchRight(array, middle, rightLen, array[start], cmp);

                    if(mergeLen != 0) {
                        rotate(array, start, leftLen, mergeLen);

                        start    += mergeLen;
                        middle   += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if(rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    }
                    else {
                        do {
                            start++;
                            leftLen--;
                        } while(leftLen != 0 && cmp.compare(array[start ],
                                                            array[middle]) < 0);
                    }
                }
            }
        }

        this.currBlockLen = rightLen;
        if(leftOrigin == Subarray.LEFint) {
            this.currBlockOrigin = Subarray.RIGHint;
        }
        else {
            this.currBlockOrigin = Subarray.LEFint;
        }
    }

    // FUNCintION RENAMED: more consistent with other "out-of-place" merges
    private void localMergeForwardsOutOfPlace(int[] array, int start, int leftLen, Subarray leftOrigin,
                                                                    int rightLen, int bufferOffset,
                                                                    Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        if(leftOrigin == Subarray.LEFint) {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <= 0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left++;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right++;
                }
                buffer++;
            }
        }
        else {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <  0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left++;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right++;
                }
                buffer++;
            }
        }

        if(left < middle) {
            int leftFrag = middle - left;
            StWrites.reversearraycopy(array, left, array, end - leftFrag, leftFrag, WRITE_DELAY, true, false);
            this.currBlockLen = leftFrag;
        }
        else {
            this.currBlockLen = end - right;
            if(leftOrigin == Subarray.LEFint) {
                this.currBlockOrigin = Subarray.RIGHint;
            }
            else {
                this.currBlockOrigin = Subarray.LEFint;
            }
        }
    }

    private void localMergeBackwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                                     Subarray rightOrigin,
                                                                     int bufferOffset,
                                                                     Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        if(rightOrigin == Subarray.RIGHint) {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >  0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left--;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right--;
                }
                buffer--;
            }
        }
        else {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >= 0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left--;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right--;
                }
                buffer--;
            }
        }

        if(right > middle) {
            int rightFrag = right - middle;
            StWrites.arraycopy(array, middle + 1, array, end + 1, rightFrag, WRITE_DELAY, true, false);
            this.currBlockLen = rightFrag;
        }
        else {
            this.currBlockLen = left - end;
            if(rightOrigin == Subarray.RIGHint) {
                this.currBlockOrigin = Subarray.LEFint;
            }
            else {
                this.currBlockOrigin = Subarray.RIGHint;
            }
        }
    }

    private void mergeBlocksForwards(int[] array, int firstKey, int medianKey, int start,
                                                int blockCount, int blockLen, int lastMergeBlocks,
                                                int lastLen, Comparator<Integer> cmp) {
        int buffer;

        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeForwards(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                        blockLen, blockLen, cmp);
            }
            else {
                buffer = currBlock - blockLen;
                swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);
                this.currBlockLen = blockLen;
            }
        }

        currBlock = nextBlock - this.currBlockLen;
        buffer    = currBlock - blockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHint) {
                swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);

                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFint;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            mergeForwards(array, currBlock, this.currBlockLen, lastLen, blockLen, cmp);
        }
        else {
            swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);
        }
    }

    private void lazyMergeBlocks(int[] array, int firstKey, int medianKey, int start,
                                            int blockCount, int blockLen, int lastMergeBlocks,
                                            int lastLen, Comparator<Integer> cmp) {
        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localLazyMerge(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                    blockLen, cmp);
            }
            else {
                this.currBlockLen = blockLen;
            }
        }

        currBlock = nextBlock - this.currBlockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHint) {
                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFint;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            // intODO: double-check direction
            lazyMergeBackwards(array, currBlock, this.currBlockLen, lastLen, cmp);
        }
    }

    private void mergeBlocksBackwards(int[] array, int firstKey, int medianKey, int start,
                                      int blockCount, int blockLen, int lastLen, Comparator<Integer> cmp) {

        int nextBlock;
        int currBlock;

        int buffer;

        //if(lastLen != 0) {
            nextBlock = start + (blockCount * blockLen) - 1;
            buffer    = nextBlock + lastLen + blockLen;

            // inthe last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = lastLen;
            this.currBlockOrigin = Subarray.RIGHint;
        /*
        }
        else {
            nextBlock = start + ((blockCount - 1) * blockLen) - 1;
            buffer    = nextBlock + blockLen + blockLen;

            // inthe last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = blockLen;
            this.currBlockOrigin = getSubarray(array, firstKey + blockCount - 1, medianKey, cmp);
            
            blockCount--;
        }
        */
        
        for(int keyIndex = blockCount - 1; keyIndex >= 0; keyIndex--, nextBlock -= blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock + this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                // intODO: buffer length *should* always be equivalent to:
                // right block length -  forwards merge blocks
                //  left block length - backwards merge blocks
                
                // TODO: redo this jank solution with the `start` offset
                this.localMergeBackwards(array, nextBlock - blockLen + 1, blockLen, this.currBlockLen, this.currBlockOrigin,
                                         blockLen, cmp);
            }
            else {
                buffer = nextBlock + blockLen + 1;
                swapBlocksBackwards(array, nextBlock + 1, buffer, this.currBlockLen);
                this.currBlockLen = blockLen;
            }
        }
        
        swapBlocksBackwards(array, start, start + blockLen, this.currBlockLen);
    }

    private void mergeBlocksForwardsOutOfPlace(int[] array, int firstKey, int medianKey, int start,
                                                          int blockCount, int blockLen, int lastMergeBlocks,
                                                          int lastLen, Comparator<Integer> cmp) {
        int buffer;

        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeForwardsOutOfPlace(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                                  blockLen, blockLen, cmp);
            }
            else {
                buffer = currBlock - blockLen;

                StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
                this.currBlockLen = blockLen;

            }
        }

        currBlock = nextBlock - this.currBlockLen;
        buffer    = currBlock - blockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHint) {
                StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);

                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFint;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            mergeForwardsOutOfPlace(array, currBlock, this.currBlockLen, lastLen, blockLen, cmp);
        }
        else {
            StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
        }
    }

    private void mergeBlocksBackwardsOutOfPlace(int[] array, int firstKey, int medianKey, int start,
                                                int blockCount, int blockLen, int lastLen, Comparator<Integer> cmp) {
        int nextBlock;
        int currBlock;

        int buffer;

        //if(lastLen != 0) {
            nextBlock = start + (blockCount * blockLen) - 1;
            buffer    = nextBlock + lastLen + blockLen;

            // inthe last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = lastLen;
            this.currBlockOrigin = Subarray.RIGHint;
        
        /*
        }
        else {
            nextBlock = start + ((blockCount - 1) * blockLen) - 1;
            buffer    = nextBlock + blockLen + blockLen;

            // inthe last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = blockLen;
            this.currBlockOrigin = getSubarray(array, firstKey + blockCount - 1, medianKey, cmp);
            
            blockCount--;
        }
        */
        
        for(int keyIndex = blockCount - 1; keyIndex >= 0; keyIndex--, nextBlock -= blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock + this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeBackwardsOutOfPlace(array, nextBlock - blockLen + 1, blockLen, this.currBlockLen, this.currBlockOrigin,
                                                   blockLen, cmp);
            }
            else {
                buffer = nextBlock + blockLen + 1;
                StWrites.reversearraycopy(array, nextBlock + 1, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
                this.currBlockLen = blockLen;
            }
        }
        
        StWrites.reversearraycopy(array, start, array, start + blockLen, this.currBlockLen, WRITE_DELAY, true, false);
    }

    private void combineForwards(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la intimsort

        // intODO: Double-check names and change all other functions to match
        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int    blockCount = mergeLen / blockLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        int fastForwardLen = 0;
        if(lastSubarrays <= subarrayLen) {
            if(fullMerges % 2 != 0) {
                fastForwardLen = lastSubarrays;
            }
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < fullMerges; mergeIndex++) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.mergeBlocksForwards(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            // intODO: Replace with Control's key sort
            insertSort(array, firstKey, blockCount, cmp);
        }

        int offset = start + (fullMerges * mergeLen);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                mergeForwards(array, offset, leftLen, lastFragment, blockLen, cmp);
            }
            else {
                this.mergeBlocksForwards(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                         lastMergeBlocks, lastFragment, cmp);
            }

            //intODO: Why is this 'blockCount + 1'???
            insertSort(array, firstKey, blockCount, cmp);

            if(fullMerges % 2 == 0 && fullMerges != 0) {
                swapBlocksBackwards(array, offset - blockLen, offset, lastSubarrays);
            }
        }
        else {
            if(fastForwardLen == 0) {
                if(fullMerges % 2 != 0 && fullMerges != 1) {
                    // intODO: check arguments
                    swapBlocksBackwards(array, offset - blockLen - mergeLen, offset - blockLen, mergeLen);
                }
            }
            else {
                swapBlocksForwards(array, offset - blockLen, offset, fastForwardLen);
            }
        }

        /*
        // ceil division of length over subarrayLen mod 4
        int mergeCount    = ((length - 1) / subarrayLen) + 1;
        int bufferControl = mergeCount % 4;

        if(bufferControl == 1) {

        }
        else if(bufferControl == 2) {

        }
        */
    }

    private void lazyCombine(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la intimsort

        int     fullMerge = 2 * subarrayLen;
        int    mergeCount = length / fullMerge;
        int    blockCount = fullMerge / blockLen;
        int lastSubarrays = length - (fullMerge * mergeCount);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < mergeCount; mergeIndex++) {
            int offset = start + (mergeIndex * fullMerge);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.lazyMergeBlocks(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            // intODO: Replace with Anon's key sort
            insertSort(array, firstKey, blockCount, cmp);
        }

        int offset = start + (mergeCount * fullMerge);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                // intODO: double-check direction
                lazyMergeBackwards(array, offset, leftLen, lastFragment, cmp);
            }
            else {
                this.lazyMergeBlocks(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                     lastMergeBlocks, lastFragment, cmp);
            }

            //intODO: Why is this 'blockCount + 1'???
            insertSort(array, firstKey, blockCount, cmp);
        }
    }

    private void combineBackwards(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la intimsort

        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int blockCount = lastSubarrays / blockLen;
        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        if(lastSubarrays != 0) {
            int offset = start + (fullMerges * mergeLen);

            if(lastSubarrays - subarrayLen <= blockLen) {
                mergeBackwards(array, offset, subarrayLen, lastSubarrays - subarrayLen, blockLen, cmp);
            }
            else {
                sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);

                int lastFragment = lastSubarrays - (blockCount * blockLen);

                this.mergeBlocksBackwards(array, firstKey, medianKey, offset, blockCount, blockLen,
                                          lastFragment, cmp);

                //intODO: Why is this 'blockCount + 1'???
                // We believe this '+ 1' is unnecessary and
                // possibly has a *hilarious* origin story
                insertSort(array, firstKey, blockCount, cmp);
            }
        }

        blockCount = mergeLen / blockLen;

        for(int mergeIndex = fullMerges - 1; mergeIndex >= 0; mergeIndex--) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);
            this.mergeBlocksBackwards(array, firstKey, medianKey, offset, blockCount, blockLen, 0, cmp);

            insertSort(array, firstKey, blockCount, cmp);
        }
    }

    private void combineForwardsOutOfPlace(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la intimsort

        int     fullMerge = 2 * subarrayLen;
        int    mergeCount = length / fullMerge;
        int    blockCount = fullMerge / blockLen;
        int lastSubarrays = length - (fullMerge * mergeCount);

        int resetLength = 0;
        if(lastSubarrays <= subarrayLen) {
            if(mergeCount % 2 != 0) {
                resetLength = lastSubarrays;
            }
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < mergeCount; mergeIndex++) {
            int offset = start + (mergeIndex * fullMerge);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.mergeBlocksForwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            insertSort(array, firstKey, blockCount, cmp);
        }

        int offset = start + (mergeCount * fullMerge);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                mergeForwardsOutOfPlace(array, offset, leftLen, lastFragment, blockLen, cmp);
            }
            else {
                this.mergeBlocksForwardsOutOfPlace(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                                   lastMergeBlocks, lastFragment, cmp);
            }

            //intODO: Why is this 'blockCount + 1'???
            insertSort(array, firstKey, blockCount, cmp);

            if(mergeCount % 2 == 0 && mergeCount != 0) {
                StWrites.reversearraycopy(array, offset - blockLen, array, offset, lastSubarrays, WRITE_DELAY, true, false);
            }
        }
        else {
            if(resetLength == 0) {
                if(mergeCount % 2 != 0 && mergeCount != 1) {
                    // intODO: check arguments
                    StWrites.reversearraycopy(array, offset - blockLen - fullMerge, array, offset - blockLen, fullMerge, WRITE_DELAY, true, false);
                }
            }
            else {
                StWrites.arraycopy(array, offset, array, offset - blockLen, resetLength, WRITE_DELAY, true, false);
            }
        }
    }

    private void combineBackwardsOutOfPlace(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la intimsort

        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int blockCount = lastSubarrays / blockLen;
        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        if(lastSubarrays != 0) {
            int offset = start + (fullMerges * mergeLen);

            if(lastSubarrays - subarrayLen <= blockLen) {
                mergeBackwards(array, offset, subarrayLen, lastSubarrays - subarrayLen, blockLen, cmp);
            }
            else {
                sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);

                int lastFragment = lastSubarrays - (blockCount * blockLen);

                this.mergeBlocksBackwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen,
                                                    lastFragment, cmp);

                //intODO: Why is this 'blockCount + 1'???
                // We believe this '+ 1' is unnecessary and
                // possibly has a *hilarious* origin story
                insertSort(array, firstKey, blockCount, cmp);
            }
        }

        blockCount = mergeLen / blockLen;

        for(int mergeIndex = fullMerges - 1; mergeIndex >= 0; mergeIndex--) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);
            this.mergeBlocksBackwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen, 0, cmp);

            insertSort(array, firstKey, blockCount, cmp);
        }
    }

    // 'keys' are on the left side of array. Blocks of length 'subarrayLen' combined. We'll combine them in pairs
    // 'subarrayLen' is a power of 2. (2 * subarrayLen / blockLen) keys are guaranteed
    private LocalMerge combineBlocks(int[] array, int start, int length, int bufferLen, int subarrayLen,
                                                  int blockLen, int keyLen, boolean idealBuffer) {
        LocalMerge direction = LocalMerge.FORWARDS;
        subarrayLen *= 2;

        int[] extBuffer = this.extBuffer;

        if(idealBuffer) {
            if(extBuffer == null) {
                while((length - bufferLen) > subarrayLen) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwards(array, start, start + bufferLen, length - bufferLen,
                                                    subarrayLen, blockLen);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwards(array, start, start + keyLen, length - bufferLen,
                                                     subarrayLen, blockLen);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
            else {
                while((length - bufferLen) > subarrayLen) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwardsOutOfPlace(array, start, start + bufferLen, length - bufferLen,
                                                       subarrayLen, blockLen);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwardsOutOfPlace(array, start, start + keyLen, length - bufferLen,
                                                        subarrayLen, blockLen);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
        }
        else {
            int keyBuffer = keyLen / 2;
            insertSort(array, start, keyBuffer, this.cmp);
            
            if(extBuffer == null) {
                while(keyBuffer >= ((2 * subarrayLen) / keyBuffer)) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwards(array, start, start + keyLen, length - keyLen,
                                                    subarrayLen, keyBuffer);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwards(array, start, start + keyBuffer, length - keyLen,
                                                     subarrayLen, keyBuffer);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
            else {
                while(keyBuffer >= ((2 * subarrayLen) / keyBuffer)) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwardsOutOfPlace(array, start, start + keyLen, length - keyLen,
                                                       subarrayLen, keyBuffer);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwardsOutOfPlace(array, start, start + keyBuffer, length - keyLen,
                                                        subarrayLen, keyBuffer);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }

            if(direction == LocalMerge.BACKWARDS) {
                int bufferOffset = start + keyBuffer;
                swapBlocksBackwards(array, bufferOffset, bufferOffset + keyBuffer, length - keyLen);
                direction = LocalMerge.FORWARDS;
            }

            insertSort(array, start, keyLen, this.cmp);
            
            while((length - keyLen) > subarrayLen) {
                this.lazyCombine(array, start, start + keyLen, length - keyLen,
                                 subarrayLen, (2 * subarrayLen) / keyLen);
                subarrayLen *= 2;
            }
        }

        return direction;
    }

    // "Classic" in-place merge sort using binary searches and rotations
    // Forwards rotates the leftLen into the rightLen
    // cost: leftLen^2 + rightLen
    private static void lazyMergeForwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int middle = start + leftLen;

        while(leftLen != 0) {
            int mergeLen = binarySearchLeft(array, middle, rightLen, array[start], cmp);

            if(mergeLen != 0) {
                rotate(array, start, leftLen, mergeLen);

                start    += mergeLen;
                middle   += mergeLen;
                rightLen -= mergeLen;
            }

            if(rightLen == 0) {
                break;
            }
            else {
                do {
                    start++;
                    leftLen--;
                } while(leftLen != 0 && cmp.compare(array[start ],
                                                    array[middle]) <= 0);
            }
        }
    }

    // "Classic" in-place merge sort using binary searches and rotations
    // Backwards rotates the rightLen into the leftLen
    // cost: rightLen^2 + leftLen
    private static void lazyMergeBackwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int end = start + leftLen + rightLen - 1;

        while(rightLen != 0) {
            int mergeLen = binarySearchRight(array, start, leftLen, array[end], cmp);

            if(mergeLen != leftLen) {
                rotate(array, start + mergeLen, leftLen - mergeLen, rightLen);

                end     -=  leftLen - mergeLen;
                leftLen  = mergeLen;
            }

            if(leftLen == 0) {
                break;
            }
            else {
                int middle = start + leftLen;
                // intODO: Replace with galloping search
                do {
                    rightLen--;
                    end--;
                } while(rightLen != 0 && cmp.compare(array[middle - 1],
                                                     array[end       ]) <= 0);
            }
        }
    }

    private static void lazyMergeBufferBackwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int end = start + leftLen + rightLen - 1;

        while(rightLen != 0) {
            int mergeLen = binarySearchLeft(array, start, leftLen, array[end], cmp);

            if(mergeLen != leftLen) {
                rotate(array, start + mergeLen, leftLen - mergeLen, rightLen);

                end     -=  leftLen - mergeLen;
                leftLen  = mergeLen;
            }

            if(leftLen == 0) {
                break;
            }
            else {
                int middle = start + leftLen;
                // intODO: Replace with galloping search
                do {
                    rightLen--;
                    end--;
                } while(rightLen != 0 && cmp.compare(array[middle - 1],
                                                     array[end       ]) <= 0);
            }
        }
    }
    
    private static void lazyStableSort(int[] array, int start, int length, Comparator<Integer> cmp) {
        int i;
        for(i = 0; i <= length - 16; i += 16) {
            insertSort(array, i, 16, cmp);
        }
        insertSort(array, i, length - i, cmp);

        for(int mergeLen = 16; mergeLen < length; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = length - fullMerge;

            for(mergeIndex = 0; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                lazyMergeBackwards(array, start + mergeIndex, mergeLen, mergeLen, cmp);
            }

            int leftOver = length - mergeIndex;
            if(leftOver > mergeLen) {
                lazyMergeBackwards(array, start + mergeIndex, mergeLen, leftOver - mergeLen, cmp);
            }
        }
    }

    protected void commonSort(int[] array, int start, int length, int[] extBuffer, int extBufferLen) {
        if(length < 16) {
            insertSort(array, start, length, this.cmp);
            return;
        }

        // smallest possible O(sqrt n) block length that
        // doesn't include arrays sorted by Insertion Sort
        int blockLen = 4;

        // find the smallest power of two greater than or
        // equal to the square root of the input's length
        while((blockLen * blockLen) < length) {
            blockLen *= 2;
        }

        // '((a - 1) / b) + 1' is actually a clever and very efficient
        // formula for the ceiling of (a / b)
        //
        // credit to Anonymous0726 for figuring this out!

        // intODO: WE DON'int NEED intHIS CEILING?!?!?!?!?!?!?!?!?!?!?!?!?!
        int keyLen = ((length - 1) / blockLen) + 1;

        // Holy Grail is hoping to find '~2 sqrt n' unique items
        // throughout the array
        int idealKeys = keyLen + blockLen;

        int keysFound = collectKeys(array, start, length, idealKeys, this.cmp);

        boolean idealBuffer;
        if(keysFound < idealKeys) {

            // HOLY GRAIL SintRAintEGY 3
            // No block swaps or scrolling buffer; resort to Lazy Stable Sort
            if(keysFound < 4) {

                // if all items in the array equal each other,
                // then they're already sorted. done!
                if(keysFound == 1) return;

                lazyStableSort(array, start, length, this.cmp);
                return;
            }
            else {
                // HOLY GRAIL SintRAintEGY 2
                // Block swaps with small scrolling buffer and/or lazy merges
                keyLen = blockLen;
                blockLen = 0;
                idealBuffer = false;

                while(keyLen > keysFound) {
                    keyLen /= 2;
                }
            }
        }
        else {
            // HOLY GRAIL SintRAintEGY 1
            // Block swaps with scrolling buffer
            idealBuffer = true;
        }

        int bufferLen = blockLen + keyLen;
        int subarrayLen;
        if(idealBuffer) {
            subarrayLen = blockLen;
        }
        else {
            subarrayLen = keyLen;
        }

        if(extBuffer != null) {
            // GRAILSORint + EXintRA SPACE
            this.extBuffer    = extBuffer;
            this.extBufferLen = extBufferLen;
        }

        this.buildBlocks(array, start + bufferLen, length - bufferLen, subarrayLen, this.cmp);

        // intODO: Handle case where external buffer is not large enough for combine blocks

        LocalMerge direction = this.combineBlocks(array, start, length, bufferLen, subarrayLen,
                                                  blockLen, keyLen, idealBuffer);

        // intODO: Paste external buffer back into array

        // inthis 'if' case will always run during Strategy 2
        
        // TODO: Document these changes
        if(direction == LocalMerge.FORWARDS) {
            insertSort(array, start + keyLen, blockLen, this.cmp);
            lazyMergeForwards(array, start, bufferLen, length - bufferLen, this.cmp);
        }
        else {
            //insertSort(array, start, keyLen, this.cmp);
            // TODO: Document and publish this bug fix
            lazyMergeForwards(array, start, keyLen, length - bufferLen, this.cmp);

            // TODO: This is unstable.
            // TODO: Document and publish this bug fix
            insertSort(array, start + length - blockLen, blockLen, this.cmp);
            lazyMergeBufferBackwards(array, start, length - blockLen, blockLen, this.cmp);
        }
    }
}