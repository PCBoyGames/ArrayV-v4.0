package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;

final public class LogKitaSortImplicit extends Sort {
    public LogKitaSortImplicit(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Log Kita (Implicit)");
        this.setRunAllSortsName("Implicit Log Kita Sort");
        this.setRunSortName("Implicit Log Kitasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int log(int v) {
        return 32-Integer.numberOfLeadingZeros(v-1);
    }

    // first power of two greater than or equal to W(n), because I want to play it safe
    private int[] productLog(int n) {
        int r = 1;
        while ((r<<r)+r-1 < n) r++;
        int q = 0;
        while (1<<q < r) q++;
        return new int[] {r, 1<<q};
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    private int medOf3(int[] array, int a, int b, int c) {
        int d;
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            d = b; b = a;
        } else
            d = a;
        if (Reads.compareIndices(array, b, c, 0.5, true) > 0) {
            if (Reads.compareIndices(array, d, c, 0.5, true) > 0) {
                return d;
            }
            return c;
        }
        return b;
    }

    private int ninther(int[] array, int a, int b) {
        if (b-a<=9)
            return array[a+(b-a)/2];
        int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
        int c = medOf3(array, a, a+eight, a+quart);
        int d = medOf3(array, a+quart+eight, a+half, a+half+eight);
        int e = medOf3(array, b-quart, b-eight, b-1);
        int f = medOf3(array, c, d, e);
        return f;
    }

     // Median of 3 ninthers
     private int pseudomo27(int[] array, int a, int b) {
            if (b - a < 64) {
                 return this.ninther(array, a, b);
            } else {
                 int d = (b - a + 1) / 8;
                 int m0 = this.ninther(array, a, a + 2 * d);
                 int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
                 int m2 = this.ninther(array, a + 6 * d, b);
                 return this.medOf3(array, m0, m1, m2);
            }
     }

     // Ninther of 9 ninthers
     private int pseudomo81(int[] array, int a, int b) {
            if (b - a < 256) {
                 return this.pseudomo27(array, a, b);
            } else {
                 int d = (b - a + 1) / 24;
                 int m0 = this.ninther(array, a, a + 2 * d);
                 int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
                 int m2 = this.ninther(array, a + 6 * d, a + 8 * d);
                 int m3 = this.ninther(array, a + 9 * d, a + 11 * d);
                 int m4 = this.ninther(array, a + 12 * d, a + 14 * d);
                 int m5 = this.ninther(array, a + 15 * d, a + 17 * d);
                 int m6 = this.ninther(array, a + 18 * d, a + 20 * d);
                 int m7 = this.ninther(array, a + 19 * d, a + 21 * d);
                 int m8 = this.ninther(array, a + 22 * d, b);
                 return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5), this.medOf3(array, m6, m7, m8));
            }
     }

     // Ninther of 9 medians of 3 ninthers
     private int pseudomo243(int[] array, int a, int b) {
            if (b - a < 16384) {
                 return this.pseudomo81(array, a, b);
            } else {
                 int d = (b - a + 1) / 24;
                 int m0 = this.pseudomo27(array, a, a + 2 * d);
                 int m1 = this.pseudomo27(array, a + 3 * d, a + 5 * d);
                 int m2 = this.pseudomo27(array, a + 6 * d, a + 8 * d);
                 int m3 = this.pseudomo27(array, a + 9 * d, a + 11 * d);
                 int m4 = this.pseudomo27(array, a + 12 * d, a + 14 * d);
                 int m5 = this.pseudomo27(array, a + 15 * d, a + 17 * d);
                 int m6 = this.pseudomo27(array, a + 18 * d, a + 20 * d);
                 int m7 = this.pseudomo27(array, a + 19 * d, a + 21 * d);
                 int m8 = this.pseudomo27(array, a + 22 * d, b);
                 return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5), this.medOf3(array, m6, m7, m8));
            }
     }

     // get rank of r between [a,a+g...b)
    private int gaprank(int[] array, int a, int b, int g, int r) {
        int re = 0;
        while (a < b) {
            if (a != r) {
                if (Reads.compareIndices(array, a, r, 0.25, true) < 0) re++;
            }
            a += g;
        }
        return re;
    }

    // hopefully better "rank of 243s" median selector
    private int rankof243s(int[] array, int a, int b) {
        // 2^(log(b-a)/2)
        int s = 1;
        while (s*s<b-a) s*=2;

        // low n: return ninther
        if ((s/=2) < 2) return ninther(array, a, b);
        int mid = (b-a-1)/(2*s)+1, e = (b-a) / 8, cm = a+(b-a)/2, cr = 0;

        // select pmo243 with gapped rank closest to middle
        for (int i=0; i<e; i+=s) {
            int p = pseudomo243(array, a+i, b-e+i), r = gaprank(array, a, b, s, p);
            if (Math.abs(cr-mid)>Math.abs(r-mid)) {
                cm = p;
                cr = r;
            }
        }
        return cm;
    }

    private void encode(int[] array, int a, int b, int v) {
        while (v>0) {
            if (v%2==1) Writes.swap(array, a, b, 1, true, false);
            v/=2; a++; b++;
        }
    }

    private void recode(int[] array, int a, int x, int w, int from, int f, int to, int t) {
        int F = x+from*w, T = x+to*w, tmp, i = 0;
        while (Math.min(f, t) > 0) {
            if ((f & t) % 2 == 1) {
                tmp = array[F + i];
                Writes.write(array, F + i, array[a + i], 0.33, true, false);
                Writes.write(array, a + i, array[T + i], 0.33, true, false);
                Writes.write(array, T + i, tmp, 0.33, true, false);
            } else if (f % 2 == 1) {
                Writes.swap(array, a + i, F + i, 1, true, false);
            } else if (t % 2 == 1) {
                Writes.swap(array, a + i, T + i, 1, true, false);
            }
            f /= 2; t /= 2; i++;
        }
        while (f > 0) {
            if (f % 2 == 1)
                Writes.swap(array, a + i, F + i, 1, true, false);
            f /= 2; i++;
        }
        while (t > 0) {
            if (t % 2 == 1)
                Writes.swap(array, a + i, T + i, 1, true, false);
            t /= 2; i++;
        }
    }

    private int get(int[] array, int a, int p, int l, int c, boolean b) {
        int v = 0, i = 0;
        while (l-->0) {
            v |= (Reads.compareIndexValue(array, a+i, p, 0.1, true) < c ^ b ? 1 << i : 0);
            i++;
        }
        return v;
    }

    private int getAndFree(int[] array, int a, int x, int p, int l, int w, int c, boolean b) {
        int v = get(array, a, p, w, c, b);
        encode(array, a, x+v*l, v);
        return v;
    }

    private int partitionEasy(int[] array, int[] tmp, int a, int b, int p, int c) {
        int j = 0;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (Reads.compareIndexValue(array, i, p, 0.5, true) < c)
                Writes.write(array, a++, array[i], 0.25, true, false);
            else
                Writes.write(tmp, j++, array[i], 0.25, false, true);
        }
        Writes.arraycopy(tmp, 0, array, a, j, 0.5, true, false);

        return a;
    }

    private void blockcycle(int[] array, int a, int m, int b, int l, int w, int p, int c, boolean i) {
        for (int k = 0; k < b - 1; k++) {
            int z = get(array, a+k*l, p, w, c, i);
            while (z != k) {
                multiSwap(array, a+k*l, a+z*l, l);
                z = get(array, a+k*l, p, w, c, i);
            }
            encode(array, a+k*l, m+k*l, k);
        }
        encode(array, a+(b-1)*l, m+(b-1)*l, b-1);
    }

    // log partition with +1 blocksize technique applied
    private int partition(int[] array, int[] tmp, int a, int b, int p, int c) {
        final int blk = tmp.length + 1;
        if (b-a < blk) return partitionEasy(array, tmp, a, b, p, c);
        int l = 0, r = 0, t = a, lb = 0, rb = 0;
        // type blocks
        for (int i=a; i<b; i++) {
            if (Reads.compareIndexValue(array, i, p, 0.5, true) < c) {
                // build low block using swapspace in main list
                Writes.write(array, t+l++, array[i], 0.25, true, false);
                if (l == blk) {
                    l = 0;
                    t += blk;
                    lb++;
                }
            } else {
                if (r == blk - 1) {
                    // shift incomplete low block over, copy over complete high block
                    int t2 = array[i];
                    Writes.arraycopy(array, t, array, t+blk, l, 0.25, true, false);
                    Writes.arraycopy(tmp, 0, array, t, r, 0.25, true, false);
                    Writes.write(array, t+r, t2, 0.25, true, false);
                    t += blk;
                    r = 0;
                    rb++;
                } else {
                    // save element to build high block
                    Writes.write(tmp, r++, array[i], 0.25, true, true);
                }
            }
        }
        // sort blocks
        int min = Math.min(lb, rb);
        if (min > 0) {
            int M = log(min);
            // tag blocks with indices
            for (int i=0, j=0, k=0; i<min; i++) {
                while (Reads.compareIndexValue(array, a+j*blk+M, p, 0.5, true) >= c) j++;
                while (Reads.compareIndexValue(array, a+k*blk+M, p, 0.5, true) < c) k++;
                encode(array, a+j++*blk, a+k++*blk, i);
            }
            if (lb < rb) {
                for (int i=lb+rb-1, j=0; i>=0; i--) {
                    if (Reads.compareIndexValue(array, a+i*blk+M, p, 0.5, true) >= c)
                        multiSwap(array, a+i*blk, a+(i+j)*blk, blk);
                    else j++;
                }
                // indexsort blocks
                blockcycle(array, a, a+lb*blk, lb, blk, M, p, c, lb<rb);
            } else {
                for (int i=0, j=0; i<lb+rb; i++) {
                    if (Reads.compareIndexValue(array, a+i*blk+M, p, 0.5, true) < c)
                        multiSwap(array, a+i*blk, a+j++*blk, blk);
                }
                // indexsort blocks
                blockcycle(array, a+lb*blk, a, rb, blk, M, p, c, lb<rb);
            }
        }
        // redistribute fragment
        Writes.arraycopy(tmp, 0, array, b-r, r, 1, true, false);
        if (l > 0) {
            Writes.arraycopy(array, t, tmp, 0, l, 0.5, true, true);
            Writes.arraycopy(array, a+lb*blk, array, a+lb*blk+l, rb*blk, 0.5, true, false);
            Writes.arraycopy(tmp, 0, array, a+lb*blk, l, 0.5, true, false);
        }
        return a+l+lb*blk;
    }

    // logselect function
    private int[] quickselect(int[] array, int[] tmp, int a, int b, int r) {
        boolean bad = false;
        while (b - a > 20) {
            // select good enough median
            int m = array[bad ? rankof243s(array, a, b) : pseudomo243(array, a, b)];
            // partition using either bias, whichever one yields results
            int p = partition(array, tmp, a, b, m, 0);
            if (p == a) p = partition(array, tmp, a, b, m, 1);
            if (p == b) {
                // return boundary if no uniques
                return new int[] {a, b};
            }
            // bad ratio is 6:1 instead of 8:1
            bad = 6*(p-a)<b-a||6*(b-p)<b-a;
            if (p <= r) a = p;
            else b = p;
        }
        // binary insert and find boundaries on small n
        BinaryInsertionSort i = new BinaryInsertionSort(arrayVisualizer);
        i.customBinaryInsert(array, a, b, 0.5);
        int m1 = r, m2 = r;
        do m1--; while (Reads.compareIndices(array, m1, r, 0.1, true) == 0);
           m1++;
        do m2++; while (Reads.compareIndices(array, m2, r, 0.1, true) == 0);
        return new int[] {m1, m2};
    }

    private void merge(int[] array, int[] tmp, int a, int m, int b, int t, boolean aux) {
        int l = a, r = m;
        while (l < m && r < b) {
            if (Reads.compareIndices(array, l, r, 0.5, true) <= 0) {
                Writes.write(tmp, t++, array[l++], 0.5, true, aux);
            } else {
                Writes.write(tmp, t++, array[r++], 0.5, true, aux);
            }
        }
        while (l < m)
            Writes.write(tmp, t++, array[l++], 0.5, true, aux);
        while (r < b)
            Writes.write(tmp, t++, array[r++], 0.5, true, aux);
    }

    private void tailmerge(int[] array, int[] tmp, int a, int m, int b) {
        Writes.arraycopy(array, m, tmp, 0, b-m, 1, true, true);
        int l = m-1, r = b-m-1;
        while (l >= a && r >= 0) {
            if (Reads.compareIndexValue(array, l, tmp[r], 0.5, true) > 0) {
                Writes.write(array, --b, array[l--], 0.5, true, false);
            } else {
                Writes.write(array, --b, tmp[r--], 0.5, true, false);
            }
        }
        while (r >= 0)
            Writes.write(array, --b, tmp[r--], 0.5, true, false);
    }

    // indexsort with bitbuffer
    private void indexll(int[] array, int a, int b, int x, int w, int w1, int p, int c, boolean y) {
        // traverse linkedlist, transcode to index order
        for (int i = a, j = 0; j < (b-a)/w; j++) {
            int k = get(array, i, p, w1, c, y);
            recode(array, i, x, w, k, k, (i-a)/w, j);
            i = a + k * w;
        }
        int i = a+w, i1 = 1;
        for (; i < b - w; i += w, i1++) {
            int j = get(array, i, p, w1, c, y);
            if (j == 0) continue; // pre-encoded check
            int m = i-a;
            while (j != i1) {
                int k = get(array, a+j*w, p, w1, c, y);
                // clear bitbuffer using last index and swap block
                encode(array, i, x+m, j);
                multiSwap(array, i, a+(m=j*w), w);
                j = k;
            }
            encode(array, i, x+m, i1);
        }
        for (int j = i, k = w1; k-->0; j++) {
            if (Reads.compareIndexValue(array, j, p, 1, true) < 0 ^ y) {
                encode(array, i, x+i-a, i1);
                break;
            }
        }
    }

    // a kitamerge based off of the properties of a linked list
    private void kitamerge(int[] array, int[] tmp, int x, int i, int ib, int j, int jb, int p, int c, int w, int w1, boolean y) {
        // lt: left buffer tags, rt: right buffer tags, ft: block connecting to tag 0, sb: location of second aux block,
        // l: left pointer, ln: next left index, ltc: amount of left buffer tags, lc: relative size of left buffer,
        // ld: progress to finishing left block, r: right pointer, mb: amount of blocks before j, rn: next right index,
        // rtc: amount of right buffer tags, rc: relative size of right buffer, rd: progress to finishing right block,
        // cc: block counter, bb: current buffer location, bt: current tag of buffer, ls: last buffer location,
        // fbt: first buffer tag, tc: tag count
        // total: 6 elements of array space allocated, 21 variables defined in-sort, 33 variables + arguments
        int[] lt = new int[3], rt = new int[] {ib, 0, 0};
        int l = i, ln = getAndFree(array, l, x, p, w, w1, c, y), ltc = 1, lc = 0, ld = 0, r = j, mb = ib,
            rn = getAndFree(array, r, x+j-i, p, w, w1, c, y), rtc = 1, rc = 0, rd = 0, cc = 0,
            bb, bt = 0, ls = -1, fbt = -1, tc = 0, ft = -1, sb;
        Writes.changeAllocAmount(6);
        Writes.changeAuxWrites(1);

        // merge 2 blocks into buffer
        for (; cc<2*w; cc++) {
            // put lower element into tmp[cc]
            if (jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
                Writes.write(tmp, cc, array[l++], 0.5, true, true);
                lc++;
                // if block complete, go to next block according to linkedlist
                if (++ld == w) {
                    if (--ib==0) continue;
                    ld = 0;
                    lt[ltc++] = ln;
                    Writes.changeAuxWrites(1);
                    l = i + ln * w;
                    ln = getAndFree(array, l, x, p, w, w1, c, y);
                }
            } else {
                Writes.write(tmp, cc, array[r++], 0.5, true, true);
                rc++;
                // if block complete, go to next block according to linkedlist
                if (++rd == w) {
                    if (--jb==0) continue;
                    rd = 0;
                    rt[rtc++] = rn + mb;
                    Writes.changeAuxWrites(1);
                    r = j + rn * w;
                    rn = getAndFree(array, r, x+j-i, p, w, w1, c, y);
                }
            }
        }

        // block merging routine
        do {
            // merge as many blocks into left buffer as possible
            while (lc >= rc && (ib > 0 || jb > 0)) {
                // shift out first buffer tag in lt
                bt = lt[0];
                lt[0] = lt[1];
                lt[1] = lt[2];
                Writes.changeAuxWrites(2);
                ltc--;
                bb = i + bt * w;
                for (cc=0; cc<w; cc++) {
                    // put lower element into array[bb+cc]
                    if (jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
                        Writes.write(array, bb+cc, array[l++], 0.5, true, false);
                        lc++;
                        // if block complete, go to next block according to linkedlist
                        if (++ld == w) {
                            if (--ib==0) continue;
                            ld = 0;
                            lt[ltc++] = ln;
                            Writes.changeAuxWrites(1);
                            l = i + ln * w;
                            ln = getAndFree(array, l, x, p, w, w1, c, y);
                        }
                    } else {
                        Writes.write(array, bb+cc, array[r++], 0.5, true, true);
                        rc++;
                        // if block complete, go to next block according to linkedlist
                        if (++rd == w) {
                            if (--jb==0) continue;
                            rd = 0;
                            rt[rtc++] = rn + mb;
                            Writes.changeAuxWrites(1);
                            r = j + rn * w;
                            rn = getAndFree(array, r, x+j-i, p, w, w1, c, y);
                        }
                    }
                }
                // left has one block less of buffer
                lc -= w;
                if (tc++>0) {
                    if (bt == 0) {
                        // track connecting blocks
                        ft = ls;
                    } else {
                        // tag last block made with current buffer tag
                        encode(array, ls, x+bt*w, bt);
                    }
                } else {
                    // first made buffer tag gets saved for later use
                    fbt = bt;
                }
                ls = bb;
            }
            // merge as many blocks into right buffer as possible
            while (lc <= rc && (ib > 0 || jb > 0)) {
                // shift out first buffer tag in rt
                bt = rt[0];
                rt[0] = rt[1];
                rt[1] = rt[2];
                Writes.changeAuxWrites(2);
                rtc--;
                bb = i + bt * w;
                for (cc=0; cc<w; cc++) {
                    // put lower element into array[bb+cc]
                    if (jb == 0 || (ib > 0 && Reads.compareIndices(array, l, r, 0.5, true) <= 0)) {
                        Writes.write(array, bb+cc, array[l++], 0.5, true, false);
                        lc++;
                        // if block complete, go to next block according to linkedlist
                        if (++ld == w) {
                            if (--ib==0) continue;
                            ld = 0;
                            lt[ltc++] = ln;
                            Writes.changeAuxWrites(1);
                            l = i + ln * w;
                            ln = getAndFree(array, l, x, p, w, w1, c, y);
                        }
                    } else {
                        Writes.write(array, bb+cc, array[r++], 0.5, true, true);
                        rc++;
                        // if block complete, go to next block according to linkedlist
                        if (++rd == w) {
                            if (--jb==0) continue;
                            rd = 0;
                            rt[rtc++] = rn + mb;
                            Writes.changeAuxWrites(1);
                            r = j + rn * w;
                            rn = getAndFree(array, r, x+j-i, p, w, w1, c, y);
                        }
                    }
                }
                // right has one block less of buffer
                rc -= w;
                if (tc++>0) {
                    // tag last block made with current buffer tag
                    encode(array, ls, x+bt*w, bt);
                } else {
                    // first made buffer tag gets saved for later use
                    fbt = bt;
                }
                ls = bb;
            }
        } while (ib > 0 || jb > 0);

        // re-encode connecting blocks with target position tags,
        // copy 0 block to target position
        if (ltc > 0) {
            // change fbt accordingly
            if (fbt == 0) fbt = lt[0];
            if (ft >= 0)
                encode(array, ft, x+lt[0]*w, lt[0]);
            Writes.arraycopy(array, i, array, i+lt[0]*w, w, 1, true, false);
            sb = rtc > 0 ? rt[0] : lt[1];
        } else {
            // change fbt accordingly
            if (fbt == 0) fbt = rt[0];
            if (ft >= 0)
                encode(array, ft, x+rt[0]*w, rt[0]);
            Writes.arraycopy(array, i, array, i+rt[0]*w, w, 1, true, false);
            sb = rt[1];
        }
        // copy buffer blocks to start
        Writes.arraycopy(tmp, 0, array, i, w, 0.5, true, false);
        Writes.arraycopy(tmp, w, array, i+sb*w, w, 0.5, true, false);
        // encode accordingly
        encode(array, i, x+sb*w, sb);
        encode(array, i+sb*w, x+fbt*w, fbt);
        Writes.deleteExternalArrays(lt, rt);
    }

    private void kita(int[] array, int[] tmp, int a, int b, int x, int w, int p, int c, boolean iv) {
        int B = b, s = tmp.length / 2;
        b -= (b - a) % s;
        BinaryInsertionSort bi = new BinaryInsertionSort(arrayVisualizer);
        for (int i = a; i < b; i += 16) {
            // binary insert small n
            bi.customBinaryInsert(array, i, Math.min(i+16, b), 0.5);
        }
        int j = 16;
        for (; j <= tmp.length / 4; j *= 4) {
            for (int i = a; i + j < b; i += 4 * j) {
                // ping-pong merge groups of 4
                merge(array, tmp, i, i+j, Math.min(i+2*j, b), 0, true);
                merge(array, tmp, Math.min(i+2*j, b), Math.min(i+3*j, b), Math.min(i+4*j, b), 2*j, true);
                merge(tmp, array, 0, Math.min(2*j, b-i), Math.min(4*j, b-i), i, false);
            }
        }
        for (; j <= tmp.length; j *= 2) {
            for (int i = a; i + j < b; i += 2 * j) {
                // tailmerge pairs
                tailmerge(array, tmp, i, i+j, Math.min(i+2*j, b));
            }
        }
        // encode linkedlist indices
        for (int i = a; i < b; i += j) {
            if (i+s<b) encode(array, i, x+s+i-a, 1);
            if (i+2*s<b) encode(array, i+s, x+2*s+i-a, 2);
            if (i+3*s<b) encode(array, i+2*s, x+3*s+i-a, 3);
        }
        for (; j < b - a; j *= 2) {
            for (int i = a; i + j < b; i += 2 * j) {
                // kitamerge pairs
                kitamerge(array, tmp, x+i-a, i, j/s, i+j, Math.min(j, b-i-j)/s, p, c, s, w, iv);
            }
        }
        // sort blocks
        indexll(array, a, b, x, s, w, p, c, iv);
        if (b < B) {
            // merge remaining fragment
            bi.customBinaryInsert(array, b, B, 0.5);
            tailmerge(array, tmp, a, b, B);
        }
    }

    public void logkita(int[] array, int a, int b) {
        int[] plgs = productLog(b-a);
        int lg2 = plgs[1], lg = plgs[0];
        int[] aux = Writes.createExternalArray(2*lg2);
        int m = a+(b-a)/2;
        // quickselect middle
        int[] bnds = quickselect(array, aux, a, b, m);
        // block merge both halves
        kita(array, aux, a, bnds[0], bnds[0], lg-1, array[m], 0, true);
        kita(array, aux, bnds[1], b, a, lg-1, array[m], 1, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        logkita(array, 0, sortLength);
    }
}