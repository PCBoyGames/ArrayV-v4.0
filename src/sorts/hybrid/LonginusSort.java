package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SNAP IT GOOD, MY TALINUVA! -
------------------------------

I'm making this port as close to direct as possible, and goodness me, is that hard and painful to do!

STILL WORK IN PROGRESS, MAY (DOES) CONTAIN BUGS

*/
public class LonginusSort extends Sort {
    public LonginusSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Longinus");
        this.setRunAllSortsName("Longinus Sort");
        this.setRunSortName("Longinus Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void longinus(int[] array, int currentLength) {
        // talinuva had to account for a special length case, and I added it in accordingly.
        // Otherwise, the sort would not have worked on these lengths.
        if (currentLength < 40) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsert(array, 0, currentLength, 0.5, false);
            // Returns essentially stop the block. Since this is within a void, return nothing.
            return;
        }
        // I kid you not, this is exactly how Snap! works in Java. This line is a nearly direct port.
        // That is, give or take a few blocks below the script variables that act as an init that I merged.
        // Only variable blocks in the main indent should init anyway, and this port respects that.
        // All other ints are not really there until they're used, but Snap! is weird, so here I'm using 0.
        int s = 0, b = 1, k = 1, p = currentLength - 1, c = 0, l = 0, m = 0, r = 0, i = currentLength - 1;
        // We don't have repeat until, but we do have while not!
        while (!(p == 0 || b * (b + 3) > currentLength)) {
            l = i - 1;
            r = i + b;
            while (!(l == r - 1)) {
                // Clever use of an equal-detecting Binary search.
                c = pair(array, p - 1, (int) Math.floor((l + r) / 2));
                if (c == 1) r = (int) Math.floor((l + r) / 2);
                else if (c == 0) l = r - 1;
                else l = (int) Math.floor((l + r) / 2);
            }
            if (c != 0) {
                if ((b + 6) * (b + 3) > currentLength) {
                    IndexedRotations.adaptable(array, p - 1, p, r, 0.5, true, false);
                    i--;
                } else if (r == i + b) {
                    IndexedRotations.adaptable(array, p - 1, i, i + b, 0.5, true, false);
                    i = p - 1;
                } else {
                    IndexedRotations.adaptable(array, p, i, i + b, 0.5, true, false);
                    IndexedRotations.adaptable(array, p - 1, p, p + (r - i), 0.5, true, false);
                    i = p - 1;
                }
                b++;
            }
            p--;
        }
        IndexedRotations.adaptable(array, i, i + b, currentLength, 0.5, true, false);
        if (Math.pow(2, b) <= currentLength) {
            // I'll do you one better.
            LazicciSortAdaRot lazy = new LazicciSortAdaRot(arrayVisualizer);
            lazy.lazicciStable(array, 0, currentLength);
            //plan(array, currentLength, b);
            return;
        // This is not that hard to trigger. Commented out for sanity reasons.
        } /*else if (b * (b + 3) <= currentLength) {
            arrayVisualizer.setExtraHeading(" / This is where I'd switch to my alt strategies...");
            arrayVisualizer.updateNow();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            arrayVisualizer.setExtraHeading(" / ...IF I HAD ANY!");
            arrayVisualizer.updateNow();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            arrayVisualizer.setExtraHeading("");
        }*/
        i = 0;
        p = 1;
        while (!(k == b || p == currentLength - b || (k + 2) * b > currentLength - k)) {
            l = i - 1;
            r = i + k;
            while (!(l == r - 1)) {
                c = pair(array, (int) Math.floor((l + r) / 2), p);
                if (c == 1) l = (int) Math.floor((l + r) / 2);
                else if (c == 0) l = r - 1;
                else r = (int) Math.floor((l + r) / 2);
            }
            if (c != 0) {
                if (k > b - 3 || r == i) IndexedRotations.adaptable(array, r, p, p + 1, 0.5, true, false);
                else {
                    IndexedRotations.adaptable(array, i, i + k, p, 0.5, true, false);
                    IndexedRotations.adaptable(array, (p + r) - (i + k), p, p + 1, 0.5, true, false);
                    i = p - k;
                }
                k++;
            }
            p++;
        }
        IndexedRotations.adaptable(array, 0, i, i + k, 0.5, true, false);
        phase2(array, currentLength, s, b, k, p, c, l, m, r, i);
    }

    protected int pair(int[] array, int left, int right) {
        // That's right, I don't have to drag those values around at all!
        // Why, you might ask? Well... We have index-based compares built into ArrayV.
        return -1 * Reads.compareIndices(array, left, right, 0.5, true);
    }

    // Now unused, but still ported.
    protected void plan(int[] array, int currentLength, int b) {
        // "Now this is unusual," you might say. "So you're telling me that variable I is an int here?"
        // So what? Yes, it is, get used to it! I converted a boolean to an int for the heck of it.
        // The pocket is only used for a single item here, so it's a single int.
        int l = currentLength - b, m = 0, r = 0, s = 0, i = 0, p = 0, c = 0, pocket = 0;
        while (!(b == 1)) {
            b--;
            m = 0;
            while (!(m == l || pair(array, m, l + b) == 0)) m++;
            if (m < l) {
                r = m + 1;
                s = 1;
                while (!(r == l)) {
                    if (pair(array, r, l + b) == 1) {
                        if (i == 1) {
                            pocket = array[r];
                            Writes.write(array, r, array[c], 0.5, true, false);
                            Writes.write(array, c, array[r - s], 0.5, true, false);
                            Writes.write(array, r - s, pocket, 0.5, true, false);
                            if (p == c) p = r;
                            c++;
                            if (r - m == s - 1) {
                                IndexedRotations.adaptable(array, c, p, r + 1, 0.5, true, false);
                                i = 0;
                            }
                        } else {
                            Writes.swap(array, r - s, r, 0.5, true, false);
                            if (r - m == s) m = r;
                        }
                    } else {
                        if (i == 1) {
                            if (r - p > p - c && p > c) {
                                Writes.swap(array, c, r, 0.5, true, false);
                                IndexedRotations.adaptable(array, c, c + 1, p, 0.5, true, false);
                            } else {
                                IndexedRotations.adaptable(array, p, r, r + 1, 0.5, true, false);
                                p++;
                            }
                            if (r - c > Math.sqrt(s)) {
                                IndexedRotations.adaptable(array, c, p, r + 1, 0.5, true, false);
                                if (m - r - s > c - m) {
                                    IndexedRotations.adaptable(array, m, c, r + 1, 0.5, true, false);
                                    m += r + 1 - c;
                                } else if (r + 1 - c > m - r - s) {
                                    blockSwap(array, r - s, (r - m) + (r - s) + 1, true, m - r - s);
                                    IndexedRotations.adaptable(array, c, (r - m) + (r - s) + 1, r + 1, 0.5, true, false);
                                } else {
                                    blockSwap(array, r - s, c, true, (r + 1) - c);
                                    IndexedRotations.adaptable(array, r - s, (r - s) + (r - c) + 1, m, 0.5, true, false);
                                }
                                i = 0;
                            }
                        } else {
                            // Really, though. That takes thought. I don't normally do it this way.
                            if (r - m == s);
                            else if (r - m <= Math.sqrt(s)) {
                                IndexedRotations.adaptable(array, m, r, r + 1, 0.5, true, false);
                                m++;
                            } else if (m - r - s <= Math.sqrt(s)) {
                                Writes.swap(array, r - s, r, 0.5, true, false);
                                IndexedRotations.adaptable(array, r - s, r - s + 1, m, 0.5, true, false);
                            } else {
                                i = 1;
                                c = r;
                                p = r;
                            }
                        }
                        s++;
                    }
                    r++;
                }
                if (i == 1) {
                    IndexedRotations.adaptable(array, c, p, r, 0.5, true, false);
                    IndexedRotations.adaptable(array, m, c, r, 0.5, true, false);
                    m += r - c;
                    i = 0;
                }
                while (!(r == l + b)) {
                    Writes.swap(array, r - s, r, 0.5, true, false);
                    if (r - m == s) m = r;
                    r++;
                }
                IndexedRotations.adaptable(array, r - s, m, r, 0.5, true, false);
                l -= s;
            }
        }
    }

    // This is a really short block. Probably could have been simply appended unless it was long before.
    protected void phase2(int[] array, int currentLength, int s, int b, int k, int p, int c, int l, int m, int r, int i) {
        itMakeBlock(array, (currentLength - b) - ((currentLength - k) % b), currentLength - b, currentLength, false);
        p = (currentLength - b) - ((currentLength - k) % b);
        while (!(p == k)) {
            itMakeBlock(array, p - b, p, p + b, false);
            // Once again, set as to change by.
            p -= b;
        }
        phase3(array, currentLength, s, b, k, p, c, l, m, r, i);
    }

    protected void itMakeBlock(int[] array, int l, int m, int r, boolean fwd) {
        // Surprise, my little Snap! There's a direct Java port for your if then else reporter block!
        int s = 0, b = 0, c = 0, i = 0, e = fwd ? m - l : r - m;
        for (s = 0; Math.pow(2, s + 1) <= (fwd ? r - m : m - l); s++) e -= (int) Math.floor((fwd ? r - m : m - l) / Math.pow(2, s + 1));
        if (e > s) {
            b = e - s;
            e = s;
        } else b = 0;
        for (c = 0; s >= 0; s--) {
            if (e == s && e > 0) {
                b++;
                e--;
            }
            for (i = 0; i < Math.pow(2, s); i++) {
                // Forced to cast to ints since Math.floor and Math.pow return doubles.
                if (fwd) mergeWithBuffer(array, (int) ((m - c) + Math.floor((i * (r - m)) / Math.pow(2, s))), (int) ((m - c) + Math.floor(((i + 0.5) * (r - m)) / Math.pow(2, s))), (int) ((m - c) + Math.floor(((i + 1) * (r - m)) / Math.pow(2, s))), b, true);
                else mergeWithBuffer(array, (int) ((m + c) - Math.floor(((i + 1) * (m - l)) / Math.pow(2, s))), (int) ((m + c) - Math.floor(((i + 0.5) * (m - l)) / Math.pow(2, s))), (int) ((m + c) - Math.floor((i * (m - l)) / Math.pow(2, s))), b, false);
            }
            c += b;
            b = (int) Math.floor((fwd ? r - m : m - l) / Math.pow(2, s));
        }
    }

    protected void phase3(int[] array, int currentLength, int s, int b, int k, int p, int c, int l, int m, int r, int i) {
        i = 0;
        m = 1;
        while (!(i * k >= Math.floor((currentLength - k) / b) - 2)) {
            if (m == 0) IndexedRotations.adaptable(array, k + ((i - 1) * k * b), k + ((((i - 1) * k) + 1) * b), k + (((i * k) + 1) * b), 0.5, true, false);
            else m = 0;
            while (!(m + 2 > k || k + (((i * k) + m + 3) * b) > currentLength - ((currentLength - k) % b))) {
                mergeWithBuffer(array, k + (((i * k) + m + 1) * b), k + (((i * k) + m + 2) * b), k + (((i * k) + m + 3) * b), b, true);
                m += 2;
            }
            s = 1;
            while (!(Math.pow(2, s) >= k || k + (((i * k) + Math.pow(2, s) + 1) * b) >= currentLength - ((currentLength - k) % b))) {
                if (s % 2 == 0) m = right(array, currentLength, s, b, k, p, c, l, m, r, i);
                else m = left(array, currentLength, s, b, k, p, c, l, m, r, i);
                s++;
            }
            i++;
        }
        phase4(array, currentLength, s, b, k, p, c, l, m, r, i);
    }

    protected void mergeWithBuffer(int[] array, int l, int m, int r, int s, boolean fwd) {
        // Fun Fact: These both are init as 0. I still init other not-currently-used as zero, but not these.
        // This single line is a very near direct port of three consecutive blocks.
        int b = 0, t = 0;
        if (fwd) {
            while (!(b == m - l || t == r - m || s == t)) {
                if (pair(array, m + t, l + b) == 1) {
                    Writes.swap(array, l - s + b + t, m + t, 0.5, true, false);
                    t++;
                } else {
                    Writes.swap(array, l - s + b + t, l + b, 0.5, true, false);
                    b++;
                }
            }
            if (s == t) {
                while (!(t == r - m)) {
                    Writes.swap(array, m - s + t, m + t, 0.5, true, false);
                    t++;
                }
                merge(array, l + b, m, m - s + t);
            } else {
                while (!(b == m - l)) {
                    Writes.swap(array, l - s + b + t, l + b, 0.5, true, false);
                    b++;
                }
                while (!(t == r - m)) {
                    Writes.swap(array, l - s + b + t, m + t, 0.5, true, false);
                    t++;
                }
            }
        } else {
            while (!(b == m - l || t == r - m || s == b)) {
                if (pair(array, r - 1 - t, m - 1 - b) == 1) {
                    Writes.swap(array, r + s - (b + t + 1), m - 1 - b, 0.5, true, false);
                    b++;
                } else {
                    Writes.swap(array, r + s - (b + t + 1), r - 1 - t, 0.5, true, false);
                    t++;
                }
            }
            if (s == b) {
                while (!(b == m - l)) {
                    if (s != 0) Writes.swap(array, m + s - (b + 1), m - 1 - b, 0.5, true, false);
                    b++;
                }
                merge(array, m + s - b, m, r - t);
            } else {
                while (!(t == r - m)) {
                    Writes.swap(array, r + s - (b + t + 1), r - 1 - t, 0.5, true, false);
                    t++;
                }
                while (!(b == m - l)) {
                    Writes.swap(array, r + s - (b + t + 1), m - 1 - b, 0.5, true, false);
                    b++;
                }
            }
        }
    }

    // I can't believe I have to port things like this. I mean, yes, I like doing ports of these algorithms.
    // Just... when the code is not text-based, it means I have to write all of this by hand! And it hurts!
    protected int right(int[] array, int currentLength, int s, int b, int k, int p, int c, int l, int m, int r, int i) {
        IndexedRotations.adaptable(array, k + (i * k * b), k + (i * k * b) + (m * b), k + (i * k * b) + ((m + 1) * b), 0.5, true, false);
        m = 0;
        while (!(m + Math.pow(2, s) >= k || k + ((1 + (i * k) + m + Math.pow(2, s)) * b) >= currentLength - ((currentLength - k) % b))) {
            l = m;
            r = (int) (m + Math.pow(2, s));
            while (!(l == r)) {
                if (r == m + Math.pow(2, s)) c = l;
                else {
                    // Converted to Math.max due to the nature of the if then else block in the code.
                    c = (int) Math.max(m + Math.pow(2, s), l);
                    p = c + 1;
                    while (!(p >= r)) {
                        if (pair(array, p, c) == 1) c = p;
                        p++;
                    }
                }
                if (k + (((i * k) + 1 + r) * b) < currentLength - ((currentLength - k) % b) && r < k && r < m + Math.pow(2, s + 1) && pair(array, k + (((i * k) + 1 + r) * b), k + (((i * k) + 1 + c) * b)) == 1) {
                    c = r;
                    r++;
                }
                if (c > l) {
                    blockSwap(array, k + (((i * k) + 1 + l) * b), k + (((i * k) + 1 + c) * b), true, b);
                    Writes.swap(array, c, l, 0.5, true, false);
                }
                l++;
            }
            l = 0;
            r = 0;
            // talinuva and I explained this one to each other, as Math.min has that power.
            c = (int) Math.min(Math.min(m + Math.pow(2, s + 1), k), (currentLength - k) / b - 1 - i * k);
            Writes.swap(array, k + (((i * k) + m) * b), k + (((i * k) + m + 1) * b), 0.5, true, false);
            l++;
            while (!(m == c)) {
                while (!(l == b || r == b)) {
                    p = pair(array, k + (((i * k) + m + 1) * b) + l, k + (((i * k) + m + 2) * b) + r);
                    if (p == 1 || (p == 0 && pair(array, m, m + 1) == 1)) {
                        Writes.swap(array, k + (((i * k) + m) * b) + l + r, k + (((i * k) + m + 1) * b) + l, 0.5, true, false);
                        l++;
                    } else {
                        Writes.swap(array, k + (((i * k) + m) * b) + l + r, k + (((i * k) + m + 2) * b) + r, 0.5, true, false);
                        r++;
                    }
                }
                m++;
                if (m == c - 1) {
                    while (!(r == b)) {
                        Writes.swap(array, k + (((i * k) + m) * b) + r, k + (((i * k) + m + 1) * b) + r, 0.5, true, false);
                        r++;
                    }
                    m++;
                } else if (r == b) {
                    r = l;
                    while (!(r == b)) {
                        Writes.swap(array, k + (((i * k) + m) * b) + r, k + (((i * k) + m + 1) * b) + r, 0.5, true, false);
                        r++;
                    }
                    Writes.swap(array, m, m - 1, 0.5, true, false);
                    r = 0;
                } else {
                    l = r;
                    r = 0;
                }
            }
            unweave(array, (int) ((c - 1) - ((c - 1) % Math.pow(2, s + 1))), c);
        }
        // Since this is meant to report a value, Java must return it.
        return m;
    }

    protected int left(int[] array, int currentLength, int s, int b, int k, int p, int c, int l, int m, int r, int i) {
        // Another one we talked about, except the previous form was buggy.
        c = Math.min(k, (int) Math.floor((currentLength - k) / b) - 1 - i * k);
        if (c % Math.pow(2, s + 1) <= Math.pow(2, s)) c -= c % Math.pow(2, s + 1);
        if (c > m) IndexedRotations.adaptable(array, k + (((i * k) + m) * b), k + (((i * k) + m + 1) * b), k + (((i * k) + c + 1) * b), 0.5, true, false);
        else IndexedRotations.adaptable(array, k + (((i * k) + c) * b), k + (((i * k) + m) * b), k + (((i * k) + m + 1) * b), 0.5, true, false);
        // This set to was in both of the if else cases from above. It's now isolated to appear only once.
        m = c;
        while (!(m == 0)) {
            r = m - 1;
            l = (int) ((m - 2) - ((m - 1) % Math.pow(2, s)));
            while (!(r == l)) {
                if ((l + 1) % Math.pow(2, s) == 0 && (l + 1) % Math.pow(2, s + 1) > 0) c = r;
                else {
                    c = (int) Math.min(r, (m - 2) - ((m - 1) % Math.pow(2, s)));
                    p = c - 1;
                    while (!(p <= l)) {
                        if (pair(array, c, p) == 1) c = p;
                        p--;
                    }
                }
                if ((l + 1) % Math.pow(2, s + 1) > 0 && pair(array, k + (((i * k) + c) * b) + b - 1, k + (((i * k) + l) * b) + b - 1) == 1) {
                    c = l;
                    l--;
                }
                if (c < r) {
                    blockSwap(array, k + (((i * k) + r) * b) + b - 1, k + (((i * k) + c) * b) + b - 1, false, b);
                    Writes.swap(array, c, r, 0.5, true, false);
                }
                r--;
            }
            l = b - 1;
            r = b - 1;
            c = m;
            m--;
            Writes.swap(array, k + (((i * k) + m) * b) + r, k + (((i * k) + m + 1) * b) + r, 0.5, true, false);
            r--;
            while (!(m % Math.pow(2, s + 1) == 0)) {
                while (!(l < 0 || r < 0)) {
                    p = pair(array, k + (((i * k) + m - 1) * b) + l, k + (((i * k) + m) * b) + r);
                    if (p == 1 || (p == 0 && pair(array, m - 1, m) == 1)) {
                        Writes.swap(array, k + (((i * k) + m) * b) + r, k + (((i * k) + m) * b) + l + r + 1, 0.5, true, false);
                        r--;
                    } else {
                        Writes.swap(array, k + (((i * k) + m - 1) * b) + l, k + (((i * k) + m) * b) + l + r + 1, 0.5, true, false);
                        l--;
                    }
                }
                m--;
                if (m % Math.pow(2, s + 1) == 0) {
                    while (!(l < 0)) {
                        Writes.swap(array, k + (((i * k) + m + 1) * b) + l, k + (((i * k) + m) * b) + l, 0.5, true, false);
                        l--;
                    }
                } else if (l < 0) {
                    l = r;
                    while (!(l < 0)) {
                        Writes.swap(array, k + (((i * k) + m + 1) * b) + l, k + (((i * k) + m) * b) + l, 0.5, true, false);
                        l--;
                    }
                    Writes.swap(array, m, m + 1, 0.5, true, false);
                    l = b - 1;
                } else {
                    r = l;
                    l = b - 1;
                }
            }
            unweave(array, m, c);
        }
        return m;
    }

    protected void phase4(int[] array, int currentLength, int s, int b, int k, int p, int c, int l, int m, int r, int i) {
        IndexedRotations.adaptable(array, k + ((((i - 1) * k) + m) * b), k + ((((i - 1) * k) + m + 1) * b), currentLength, 0.5, true, false);
        inner(array, currentLength - b, currentLength);
        if (currentLength - k >= (i * k + 2) * b) i++;
        merge(array, (currentLength - b) - (currentLength - k) % b, currentLength - b, currentLength);
        merge(array, k + ((i - 1) * k * b), (currentLength - b) - (currentLength - k) % b, currentLength);
        s = 0;
        while (!(k + (Math.pow(2, s) * k * b) >= currentLength)) {
            c = 0;
            while (!(k + (Math.pow(2, s) * k * b * (c + 1)) >= currentLength)) {
                depthMerge(array, (int) (k + (Math.pow(2, s) * k * b * c)), (int) (k + (Math.pow(2, s) * k * b * (c + 1))), (int) Math.min(currentLength, k + (Math.pow(2, s) * k * b * (c + 2))), 1, 0);
                c += 2;
            }
            s++;
        }
        merge(array, 0, k, currentLength);
    }

    // Revised code for iteration.
    protected void merge(int[] array, int l, int m, int r) {
        while (l < m && m < r) {
            // Honestly a good call, not making this variable unless the overall condition is true!
            int p = 0;
            if (r - m > m - l) {
                while (!(l == m || pair(array, m, l) == 1)) l++;
                if (l < m) {
                    p = m;
                    while (!(m == r || pair(array, m, l) < 1)) m++;
                    IndexedRotations.adaptable(array, l, p, m, 0.5, true, false);
                    l += m - p;
                }
            } else {
                while (!(m == r || pair(array, r - 1, m - 1) == 1)) r--;
                if (m < r) {
                    p = m;
                    while (!(m == l || pair(array, r - 1, m - 1) < 1)) m--;
                    IndexedRotations.adaptable(array, m, p, r, 0.5, true, false);
                    r += m - p;
                }
            }
        }
    }

    protected void blockSwap(int[] array, int a, int b, boolean right, int s) {
        if (right) {for (int i = 0; i < s; i++) Writes.swap(array, a + i, b + i, 0.5, true, false);}
        else for (int i = 0; i < s; i++) Writes.swap(array, a - i, b - i, 0.5, true, false);
    }

    // It's funny to think I started this idea with a barely optimized concept.
    // Kudos to talinuva for optimizing it like crazy!
    protected void unweave(int[] array, int s, int e) {
        int l = s;
        // An "empty" while. Usually not ideal in Snap! unless the boolean can change with no operations.
        // The problem is that even then, the while loop can suddenly be not empty, but heavy on a function.
        // Java happens to have these advantages; in this one, it can change using the increment operator.
        while (++s < e && pair(array, s - 1, s) == 1);
        if (s == e) return;
        int w = l - 1, r = s;
        while (s - w > 1) {
            if (pair(array, r, (w + s) >> 1) == 1) s = (w + s) >> 1;
            else w = (w + s) >> 1;
        }
        // And here's an empty for. This is easier to do in Snap!, but a special block must be made for it.
        // Lucky us, the block is very easy to make. Yet strangely enough, the Snap! verion doesn't use it.
        for (w = 1; w * 2 < e - l; w *= 2);
        w -= s - l;
        r -= s;
        l = 1;
        while (l < w && w + r < e - s) {
            if (pair(array, s + l + r, s) == 1) l++;
            else {
                IndexedRotations.adaptable(array, s, s + r, s + r + l, 0.5, true, false);
                s += l;
                w -= l;
                l = 0;
                r++;
            }
        }
        IndexedRotations.adaptable(array, s, s + r, (l < w) ? e : (s + r + l), 0.5, true, false);
    }

    // Replaced with an insertion sort combination upon request of talinuva.
    protected void inner(int[] array, int s, int e) {
        if (e - s <= 32) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsert(array, s, e, 0.5, false);
            return;
        }
        // These gaps are called Ciura(1636) F2.2344 (LDE Improved). And they are really overpowered.
        // They are the best Shell gaps we currently know through any means, extending best before them.
        // And I was even part of making it possible! Probably my peak when it comes to sorting algorithms.
        // More information at https://sortingalgos.miraheze.org/wiki/Shellsort.
        // There is an important caveat, however: Shell was designed for randomized cases.
        int[] gs = {1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156, 2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018};
        // This 1.73 optimization was first found by Lee (as 2), then reoptimized by Lord Control (as 1.73).
        for (int g = gs.length - 1; g >= 0; g--) if (gs[g] / 1.73 < e - s) shellPass(array, s, e, gs[g]);
    }

    // Also revised for tail recursion and an iteration replacement.
    protected void depthMerge(int[] array, int l, int m, int r, int d, int depth) {
        Writes.recordDepth(depth);
        if (l == m - 1 || m == r - 1 || d == 0) {
            merge(array, l, m, r);
        } else {
            while (l <= m - 1 && m <= r - 1) {
                int p = 0, b = 0, t = 0;
                if (r - m > m - l) {
                    p = (int) Math.floor((m + r) / 2);
                    if (pair(array, p, l) == 1) t = l;
                    else {
                        b = l;
                        t = m;
                        while (!(b == t - 1)) {
                            if (pair(array, p, (int) Math.floor((b + t) / 2)) == 1) t = (int) Math.floor((b + t) / 2);
                            else b = (int) Math.floor((b + t) / 2);
                        }
                    }
                    IndexedRotations.adaptable(array, t, m, p + 1, 0.5, true, false);
                    Writes.recursion();
                    depthMerge(array, l, t, t + p - m, d - 1, depth + 1);
                    l = t + p + 1 - m;
                    m = p + 1;
                } else {
                    p = (int) Math.floor((l + m) / 2);
                    if (pair(array, m, p) == 1) {
                        b = m;
                        t = r;
                        while (!(b == t - 1)) {
                            if (pair(array, (int) Math.floor((b + t) / 2), p) == 1) b = (int) Math.floor((b + t) / 2);
                            else t = (int) Math.floor((b + t) / 2);
                        }
                    } else t = m;
                    IndexedRotations.adaptable(array, p, m, t, 0.5, true, false);
                    Writes.recursion();
                    depthMerge(array, l, p, p + t - m, d - 1, depth + 1);
                    l = p + t + 1 - m;
                    m = t;
                }
            }
        }
    }

    protected void shellPass(int[] array, int s, int e, int g) {
        for (int h = g, i = h + s; i < e; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.5);
            for (; j >= h && j - h >= s && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.5, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        longinus(array, currentLength);
    }
}