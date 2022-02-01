 package sorts.hybrid;

 import main.ArrayVisualizer;
 import sorts.templates.BogoSorting;
 public final class IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort extends BogoSorting {

   public IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);
     this.setSortListName("If Bread In French Is Pain Then \uD835\uDC70 \uD835\uDC76\uD835\uDC7E\uD835\uDC75 \uD835\uDC68 \uD835\uDC6D\uD835\uDC7C\uD835\uDC6A\uD835\uDC72\uD835\uDC70\uD835\uDC75\uD835\uDC6E \uD835\uDC69\uD835\uDC68\uD835\uDC72\uD835\uDC6C\uD835\uDC79\uD835\uDC80");
     this.setRunAllSortsName("If Bread In French Is Pain Then I Own A Fucking Bakery Sort");
     this.setRunSortName("IBIFIPTIOAFB-Sort");
     this.setCategory("Esoteric Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     setUnreasonablySlow(false);
     setUnreasonableLimit(0);
     setBogoSort(false);
   }

   public boolean comp(int[] array, int start, int end, boolean dir) {
       if(start != end && dir == (Reads.compareValues(array[start],array[end]) == 1)) {
           Writes.swap(array, start, end, 1, true, false);
           return true;
       }
       return false;
   }
   public void bitPass(int[] array, int start, int end, boolean dir) {
       if(start==end)
           return;
       int mid = (end-start)/2;
       for(int i=0; i<mid; i++) {
           this.comp(array, start+i,start+mid+i, dir);
       }
       if(mid==end-start)
           return;
       this.bitPass(array, start, start+mid, dir);
       this.bitPass(array, end-mid, end, dir);
   }
   // forgot to do the upper-bound power method, so this breaks
   public int circlePass(int[] array, int start, int end) {
       if(start==end)
           return 0;
       int mid = (end-start)/2, s = 0;
       for(int i=0; i<mid; i++) {
           if(this.comp(array, start+i,end-i-1, true))
               s++;
       }
       if(mid==end-start)
           return 0;
       s += this.circlePass(array, start, start+mid);
       s += this.circlePass(array, end-mid, end);
       return s;
   }
   public void circleSort(int[] array, int start, int end) {
       int s=1;
       while(s>0)
           s=this.circlePass(array, start, end);
   }
   // almost sorts an array
   public void horrorPass(int[] array, int start, int end) {
       for(int i=0; i<(end-start); i++) {
           this.bitPass(array, start, end, true);
           this.bitPass(array, start+i, end, false);
       }
   }
   // O(n^3) Surprisesort
   public void surpass(int[] array, int start, int end) {
       for(int i=start; i<end; i++) {
           for(int j=start; j<end; j++) {
               for(int k=0; k<end-start; k++) {
                   if(Reads.compareValues(array[i], array[j]) == -1) {
                       Writes.multiSwap(array, j, i, 0, true, false);
                   }
               }
           }
       }
   }
   // New passing method ("green herring sort", O(n^3)?)
   public void greenHerringPass(int[] array, int start, int end) {
       int h = (end-start)/2, a = start+h/2, b=end-h/2;
       if(a==start||b==end)
           return;

       this.horrorPass(array, start, a);
       this.surpass(array, start, a);

       this.greenHerringPass(array, a, end);

       this.circleSort(array, start, end);
   }

   // Passes the (demonic) array through the Bible, making it slightly more sorted.
   public void unholyPass(int[] array, int start, int end) {
       for(int i=start+1; i<end; i++) {
           for(int j=start; j<=i; j++) {
                   if(i<end && Reads.compareValues(array[i], array[j]) == -1) {
                       Writes.multiSwap(array, j, i-1, 0, true, false);
                       Writes.swap(array, i, j, 0, true, false);
                   }
               }
       }
   }
   private void rot(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
           for(int i=0; i<lenB; i++) {
            Writes.multiSwap(array, pos+lenA+lenB-1, pos, pause, mark, auxwrite);
        }
   }
   // Hybrid pass
   public void grossPass(int[] array, int start, int end, int len, boolean left) {
       int h = (end-start)/2, m=start+h;
       if(m==start)
           return;
       if(end-start < 32) {
           this.greenHerringPass(array, start, end);
           return;
       }
       this.grossPass(array, start, m, len, true);
       if(left)
           this.rot(array,start,h,h,0.1,true,false); // "Rotations.noob"
       this.grossPass(array, start, end-(m-start), len, false);
       this.circleSort(array, start, end);
       if(!left)
           this.rot(array,start,end-start,Math.min(len-end,end-start),0.1,true,false);
   }

   // Improved Terrorism (O(2^n)?)
   public void terrorismSort(int[] array, int start, int end) {
       if(start >= end)
           return;
       this.comp(array, start, end, true);
       this.terrorismSort(array, start+1, end);
       this.terrorismSort(array, start, end-1);
   }

   // Terrorism squared (+ Cuprise)
   public void terrorismSquared(int[] array, int start, int end) {
       if(start >= end)
           return;
       int m = start + ((end-start)/2);
       this.surpass(array, start, m);
       this.surpass(array, m, end);
       this.terrorismSort(array, start, end-1);
       Highlights.markArray(1, m);
       Delays.sleep(0);
       this.terrorismSquared(array, start, end-1);
       this.terrorismSquared(array, start+1, end);
   }

   // Hybrid hybrid pass
   public void GodSpeed(int[] array, int start, int end) {
       this.unholyPass(array, start, end);
       if(end-start < 64) {
           this.greenHerringPass(array, start, end);
           return;
       }
       int h = (end-start)/2, a = start+h/2, b=end-h/2;
       if(a==start||b==end)
           return;
       this.GodSpeed(array, start, b);
       this.GodSpeed(array, a, end);
       this.GodSpeed(array, start, b);

       this.grossPass(array, start, end, end, true);

       this.horrorPass(array, start, end);

       this.terrorismSquared(array, start, end);
   }

   @Override
   public void runSort(int[] array, int sortLength, int bucketCount) {
       for(int i=1; i<=sortLength; i++)
           this.GodSpeed(array, 0, i);
       this.surpass(array, 0, sortLength);
   }
 }