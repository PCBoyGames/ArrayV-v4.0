package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

There is some explaining I need to do, and it's not so easy to do without
making it seem like a long story. You might say this is the lore behind the
algorithm.

It was October 21, 2021. Having noticed an idea that Potassium, one of my
friends over on the Musicombo Discord, had mentioned making a 'truly
random' Shellsort, I had to remind the CEO that what I made back in August
might as well be as arbitrary as it gets. The CEO, of course, dissented.
Him, I quote, "Mr. Clark, if you're so intent on making random seem like a
good thing, yet you also make Bogo variants, then couldn't you also try
doing something 'more random than Randomized Shell' and make that good,
too?" He was right. I had no idea where to start in terms of making it seem
good, but he was more right than I would ever be. The discussion ended
there, with me, my arms on the desk, folded, and my head down resting on
them. I almost mumbled, "How would that even work?"

The next day, I met the Madhouse CEO again. I was seeking some sort of
compromise. Having reminisced the events of the day before, I had time to
ponder it, and then I said, "Sir, I have an idea." He glanced at me and
asked, "And that is?" I suddenly felt a sense of uncertainty, as if I knew
I would get turned down. The words were on the tip of my tongue, but I
couldn't say them. And just before my hesitance took me over, I had no
choice. The proposal was set when I spoke, "I can't think of an
optimization for it." The CEO realized what I meant. He knew I had an
implementation in mind, but it wouldn't perform the way I wanted. He calmly
smiled and assured me, "I have an idea, too. There might be a way to make
it O(N^2) at best." I realized what the CEO meant. I knew he had an
optimization in mind.

It was December 29, 2021. By then, I had forgotten that we even had the
idea for this algorithm. As I was working on other hobbies, the CEO
entered the room with his notebooks in his hand. I hear my name called,
"Mr. Clark!" I look to my right and quickly notice the CEO with his
documents and folders, meaning he has something intriguing for me to
implement. He asks, "I'm sure you recall the Shellsort thing?" "No," I
said, slightly stammering. "What about it?" As he presented his idea, I
started to understand what he meant by an O(N^2) best case. Even with only
the pseudo-code he had written down in his journal, I knew where he was
going with his optimization.

I didn't think about them at first, but I had several concerns with that
code. First of all, if we were at a particular point in disparity and the
Rouge check bailed out too early, then items believed to be sorted could,
in fact, be out of order in pairs of two. Secondly, even though the Rogue
check decreases the chances of it happening, I started to wonder if any
Shell pass could make no change to the array. It turns out that's possible,
but I couldn't do much about it when I thought of it that way, and I didn't
want to, considering that would simply be a case of the randomizer being
terrible at its job. And lastly, we don't even need to go through every
single disparity down to one. I was tempted to add additional code for an
Insertsort to it but remembered that Shell gaps of 1 would do the exact
same thing.

The day turned to December 30, 2021. By then, I had fixed most of the
problems but still needed to find a safe exit to the Insertsort. I didn't
want the Insert threshold to be too high and didn't want it too low. Now my
struggle was to find a good median between them. I consulted the CEO once
more and asked for a fair point. He suggested CBRT(N), one of the exits I
had in mind. The length I was testing, 512 numbers, resulted in an exit of
8. This was perfect, and I thanked him for his help.

The algorithm was complete. It was an effort between the CEO and me that
feels like a perfectly vivid memory, even if I hardly remember it at all.
It was a blessing to have him remind me of the entire fun year we've had
together just in time for New Year's Eve.

From the CEO and myself, we wish you a Happy New Year 2022, and may it
better us all for a brighter future.

 - PCBoy

*/
final public class FurtherRandomShellSort extends BogoSorting {
    public FurtherRandomShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Further Randomized Shell");
        this.setRunAllSortsName("Further Randomized Shell Sort");
        this.setRunSortName("Further Random Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void shellPass(int[] array, int currentLength, int bounding) {
        for (int h = randInt(1, bounding + 1), i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            while (j >= h && Reads.compareValues(array[j - h], v) == 1) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                Writes.write(array, j, array[j - h], 0.25, true, false);
                j -= h;
                w = true;
            }
            if (w) {
                Writes.write(array, j, v, 0.25, true, false);
            }
            h = randInt(1, i < bounding ? i + 1 : bounding + 1);
        }
    }

    private int nextBound(int[] array, int currentLength, int bounding) {
        int gap = bounding;
        boolean passing = true;
        while (passing && gap > Math.cbrt(currentLength)) {
            for (int i = 0; i + gap < currentLength && passing; i++) {
                if (Reads.compareIndices(array, i, i + gap, 0.005, true) > 0) {
                    passing = false;
                }
            }
            gap--;
        }
        return gap;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bounding = currentLength - 1;
        while (bounding > Math.cbrt(currentLength)) {
            shellPass(array, currentLength, bounding > 1 ? bounding : 1);
            Highlights.clearAllMarks();
            bounding = nextBound(array, currentLength, bounding);
        }
        shellPass(array, currentLength, 1);
    }
}