package sorts.esoteric;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import main.ArrayVisualizer;
import sorts.templates.BestForNSorting;
import sorts.templates.Parallelize;
import sorts.templates.Sort;
import utils.ImplQueue;
import utils.Statistics;

/*******************************************
 *          The Epsilon Committee          *
 * --------------------------------------- *
 * The mind is like an iceberg, it floats  *
 * with one-seventh of its bulk above      *
 * water                                   *
 * ======================================= *
 * Author: Distray                         *
 *******************************************/
public class pcboygamesisyournewgodSort extends BestForNSorting implements Parallelize {
	public pcboygamesisyournewgodSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("PCBGIYNG");
		this.setRunAllSortsName("&pcboygamesisyournewgod_checkoutpcbsam_lookatarrayv_itscool_itsalwaysbeingupdated_itsgoingtooverthrowtheusgovernment_dontbeslipstick_westillneedtomaketriplefactorial_pleasenevermentionworstworstinfrontofnaoan_theepsilondimensionhasbeeninlimboforforever_ordinalhierarchybasesortcouldbeworthygiventherightordinals_weneedfileformatdiversityrightaway_tecapprovalcouldbecomeajokewithhowoftennonworthysortsareslidingthrough_westillneedtoimplementsuperdeterminismintoanalgorithm_agonybubbleisonlyintheepsiloncommitteebecauseofitsfilesize_tecslanderwillhappenin3months_letsfaceitepsicourts1e1introductionwillneverhappenbeforehksilksong_thebigocalculatorindistrrayvwillnevercompletelydisappearanddistraywillhatethatfactfortherestofhislife_distraywhenheseesreadablecodethatfollowsconvention_stillyettounderstandwhynaoanissoopposedtotheconceptofavcintheepsiloncommittee_threewillforeverbegreaterthanorequaltofour_undefinedbestrank_areyoutellingmeyouknowmymaker_sortingalgorithmmadhousesus_pingpongittookme100y_naoansplitwillforeverexistinthecodeoftecbotregardlessofhowunnecessarilycomplexitis_ittakestwopeopletoaddaranktoarankingsystem_replitkeepalivesaredyingfasterthanyouthink_tosolveyourlifeproblemsjustaddk_dbjsonsarefunctionallyequivalenttosqlite3_intercomcommandishonestlyquiteincredible_shiyaleshouldnothavebeenmutuallyadjacenttonullbuthereweare_slashcommandsarestilloneofmanyinstancesofdiscordsexecutivewrath_potassiumwastechnicallythetruemaker_examplerolehearteyes_restinpeacememermemings_eilrahcfisarguablythemostsanepersoninthecommittee_ultradeterminismwillforeverremainajokeandshouldnotbeactuallyimplemented_justlikenewcokeorcokeiitherewillbesomethingalongthelinesofpsi0omegalargeomegasmallwithrespecttobuchholzpsicommitteethatshutsdownduetojustbeingawatereddownoverhypedepsiloncommittee_lorevoicewillforeverbeoverlooked_pcboyshealmmememannermemingscrossovermoreambitiousthaninfinitywar_stanilealgorithmswillnevercatchonbecausetheyaddapracticaluseevenifthecomplexitydips_gammagammasort2willkillusall Sort");
		this.setRunSortName("&pcboygamesisyournewgod Sort");
		this.setCategory("Epsilon Committee Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
		this.setPathogenic(true); // this is an absolute monster.
		this.setPathogenName("TEC-25 \"Iceberg\"");
	}
    private static pcboygamesisyournewgodSort self = null;
    private static HashMap<String, String> maps = new HashMap<>();

	private static enum BIAS {
		GE(0),
		G(1),
		DONTCARE(2);
		public int type;
		BIAS(int type) {
			this.type = type;
		}
	}
	private static double SINGUCOUNTING_EXP = 1d / 6d;
	// pcboy, i beg of you, *please* check on your laptops. it's been eight months since the last post in the G.A.T.E. thread.
	private static int[] GATE_GAPS = {12267594, 5380524, 2359879, 1035035, 453963, 199107, 87328, 38303, 16803, 7382, 3283, 1636, 701, 301, 132, 57, 23, 10, 4, 1};
	public int gmin, gmax; // not the same as Powset's min/max.

    private static String[] icebergStrings = new String[] {
		"&pcboygames_is_your_new_god",
		"&check_out_pcbsam",
		"&look_at_arrayv",
		"&its_cool",
		"&its_always_being_updated",
		"&its_going_to_overthrow_the_us_government",
		"&dont_be_slipstick",
		"&we_still_need_to_make_triple_factorial",
		"&please_never_mention_worstworst_in_front_of_naoan",
		"&the_epsilon_dimension_has_been_in_limbo_for_forever",
		"&ordinal_hierarchy_base_sort_could_be_worthy_given_the_right_ordinals",
		"&we_need_file_format_diversity_right_away",
		"&tec_approval_could_become_a_joke_with_how_often_nonworthy_sorts_are_sliding_through",
		"&we_still_need_to_implement_superdeterminism_into_an_algorithm",
		"&agony_bubble_is_only_in_the_epsilon_committee_because_of_its_filesize",
		"&tec_slander_will_happen_in_3_months",
		"&lets_face_it_epsicourt_s1e1_introduction_will_never_happen_before_hk_silksong",
		"&the_big_o_calculator_in_distrrayv_will_never_completely_disappear_and_distray_will_hate_that_fact_for_the_rest_of_his_life",
		"&distray_when_he_sees_readable_code_that_follows_convention",
		"&still_yet_to_understand_why_naoan_is_so_opposed_to_the_concept_of_avc_in_the_epsilon_committee",
		"&three_will_forever_be_greater_than_or_equal_to_four",
		"&undefined_best_rank",
		"&are_you_telling_me_you_know_my_maker",
		"&sorting_algorithm_madhouse_sus",
		"&ping_pong_it_took_me_100y",
		"&naoan_split_will_forever_exist_in_the_code_of_tecbot_regardless_of_how_unnecessarily_complex_it_is",
		"&it_takes_two_people_to_add_a_rank_to_a_ranking_system",
		"&replit_keepalives_are_dying_faster_than_you_think",
		"&to_solve_your_life_problems_just_add_k",
		"&db_jsons_are_functionally_equivalent_to_sqlite3",
		"&intercom_command_is_honestly_quite_incredible",
		"&shiyale_should_not_have_been_mutually_adjacent_to_null_but_here_we_are",
		"&slash_commands_are_still_one_of_many_instances_of_discords_executive_wrath",
		"&potassium_was_technically_the_true_maker",
		"&example_role_hearteyes",
		"&rest_in_peace_memermemings",
		"&eilrahcf_is_arguably_the_most_sane_person_in_the_committee",
		"&ultradeterminism_will_forever_remain_a_joke_and_should_not_be_actually_implemented",
		"&just_like_new_coke_or_coke_ii_there_will_be_something_along_the_lines_of_psi0_omegalarge_omegasmall_with_respect_to_buchholz_psi_committee_that_shuts_down_due_to_just_being_a_watered_down_overhyped_epsilon_committee",
		"&lore_voice_will_forever_be_overlooked",
		"&pcboy_shealm_mememannermemings_crossover_more_ambitious_than_infinity_war",
		"&stanile_algorithms_will_never_catch_on_because_they_add_a_practical_use_even_if_the_complexity_dips",
		"&gammagamma_sort_2_will_kill_us_all"
    };

    private static String iceberg(String output, boolean cached) {
    	String v = icebergStrings[randInt(0, icebergStrings.length)];
    	if(cached) {
        	if(output == null) return null;
    		if(maps.containsKey(output))
    			return maps.get(output);
    		maps.put(output, v);
    	}
    	// meant to constantly change.
    	return v;
    }

    private static Sort[] wrapsCompare, wrapsDistr;
    public static class Epsilonite extends Sort {
    	private Sort wrapped;
    	public Epsilonite(ArrayVisualizer arrayVisualizer) {
    		super(arrayVisualizer);
    	}
    	public Epsilonite(ArrayVisualizer arrayVisualizer, int index, boolean compare) {
    		super(arrayVisualizer);
    		if(compare)
    			wrapped = wrapsCompare[index];
    		else
    			wrapped = wrapsDistr[index];
    		instantiate();
    	}
    	private void instantiate() {
			Object v;
    		for(Field i : wrapped.getClass().getDeclaredFields()) {
				try {
					v = i.get(wrapped);
	    			i.set(this, v);
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
    		}
    	}
    	public String getSortListName() {
    		return iceberg(wrapped.getSortListName(), true); // collections.sort gets fussy otherwise
    	}
    	public String getRunAllSortsName() {
    		return iceberg(null, false);
    	}
    	public String getRunSortName() {
    		return iceberg(null, false);
    	}
    	public String getCategory() {
    		return iceberg(null, false);
    	}
    	public String getQuestion() {
    		return null;
    	}
    	@Override
    	public void runSort(int[] array, int length, int buckets) {
			pcbgiyng(array, 0, length, -1);
    	}
    }

    private void infect() {
    	ArrayList<Sort> sorts0 = arrayVisualizer.getSortAnalyzer().comparisonSorts,
    					sorts1 = arrayVisualizer.getSortAnalyzer().distributionSorts;
    	wrapsCompare = new Sort[sorts0.size()];
    	wrapsDistr = new Sort[sorts1.size()];

    	for(int i=0; i<sorts0.size(); i++) {
    		Epsilonite s = new Epsilonite(arrayVisualizer);
    		s.wrapped = sorts0.get(i);
    		s.instantiate();
    		sorts0.set(i, s);
    	}
    	for(int i=0; i<sorts1.size(); i++) {
    		Epsilonite s = new Epsilonite(arrayVisualizer);
    		s.wrapped = sorts1.get(i);
    		s.instantiate();
    		sorts1.set(i, s);
    	}
    	arrayVisualizer.getSortAnalyzer().sortSorts();
        arrayVisualizer.refreshSorts();
    	for(int i=0; i<sorts0.size(); i++)
    		wrapsCompare[i] = ((Epsilonite) sorts0.get(i)).wrapped;
    	for(int i=0; i<sorts1.size(); i++)
    		wrapsDistr[i] = ((Epsilonite) sorts1.get(i)).wrapped;
    }


	// GENERAL MATH FUNCTIONS
	private int fact(int v) {
		int n = v;
		while(v-->1)n*=v;
		return n;
	}

	private int log(int v) {
		return 32-Integer.numberOfLeadingZeros(v-1);
	}

	private int sqrtrnd(int v) {
		int p=0;
		while(++p*p<v);
		return p;
	}


	// GENERALIZED PARTITION COMPARATORS (bias = 0: ge, bias = 1: g, bias = 2: dontcare)
	private int compareValuesTo(int aV, int bV, int pivV, BIAS bias) {
		int ac = Reads.compareValues(aV, pivV),
			bc = Reads.compareValues(bV, pivV);
		Statistics.addStat("Pivot Comparison");
		return ac == bc ? 0 :
			bias == BIAS.DONTCARE ? (ac > bc ? 1 : -1) :
			(ac >= bias.type && bc < bias.type ? 1 :
			 ac < bias.type && bc >= bias.type ? -1 : 0);
	}

	private int compareIndicesTo(int[] array, int aI, int bI, int pivV, BIAS bias, double sleep) {
		Highlights.markArray(1, aI);
		Highlights.markArray(2, bI);
		Delays.sleep(sleep);
		return compareValuesTo(array[aI], array[bI], pivV, bias);
	}

	private int compareIndexValueTo(int[] array, int aI, int bV, int pivV, BIAS bias, double sleep) {
		Highlights.markArray(1, aI);
		Delays.sleep(sleep);
		return compareValuesTo(array[aI], bV, pivV, bias);
	}

    // BOGO COMPONENTS
    private static int randInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    private boolean isRangePartitioned(int[] array, int start, int pivot, int end) {
        for (int i = start; i < pivot; i++) {
            if (Reads.compareIndices(array, i, pivot, 0.001, true) > 0)
                return false;
        }
        for (int i = pivot + 1; i < end; i++) {
            if (Reads.compareIndices(array, pivot, i, 0.001, true) > 0)
                return false;
        }
        return true;
    }

    private boolean isRangeSorted(int[] array, int start, int end) {
        for (int i = start; i < end-1; i++) {
            if (Reads.compareIndices(array, i, i+1, 0.001, true) > 0)
                return false;
        }
        return true;
    }

    private boolean isMaxSorted(int[] array, int start, int end) {
        return isRangePartitioned(array, start, end-1, end);
    }

    private void bogoSwap(int[] array, int a, int b, boolean aux) {
    	for (int i = a; i < b - 1; i++) {
    		Writes.swap(array, i, randInt(i, b), 0.001, true, aux);
    	}
    }

    // PDSBOGO PATTERN DEFEAT
    private int sig(int a, int b, int d) {
    	return ((a + b) + d * Math.abs(a - b)) / 2;
    }

    private int findRun(int[] array, int start, int end) {
    	if(start >= end - 1)
    		return start + 1;
    	int cmp = -Reads.compareIndices(array, start++, start, 1, true) | 1,
    		k = start - 1, d;
    	do {
    		d = Reads.compareIndices(array, start++, start, 1, true);
    	} while(start < end && d != cmp);
    	int m = (start - k) / 2,
    		q = sig(k, start-1, -cmp);
    	for(int i=0; i<m; i++) {
    		Writes.swap(array, k+i, q+cmp*i, 1, true, false);
    	}
    	return start;
    }

    // PATTERN-DEFEATING SAFE BOGO SORT
    public void pdsBogo(int[] array, int a, int b) {
		int p = this.findRun(array, a, b) - 1;

		while (p < b - 1) {
			Writes.swap(array, p, randInt(p, b), 1, true, false);
			int cmp = p > 0 ? Reads.compareValues(array[p-1], array[p]) : Reads.compareValues(array[p], array[p+1]);
			if (cmp == 0) cmp = -1;
			if (cmp == 1 && p > 0) p--;
			else if (cmp < 0) {
				do {
					p++;
				} while (p < b && Reads.compareValues(array[p-1], array[p]) <= 0);
				p--;
			}
		}
    }

    // COMMON MADHOUSE COMPONENTS
    private int stableReturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    private boolean hasNoDupes(int[] array, int min, int max, int a, int b) {
        int size = max - min + 1;
        int[] holes = new int[size];
        for (int x = a; x < b; x++) {
            if (holes[stableReturn(array[x]) - min] == 1) return false;
            else holes[stableReturn(array[x]) - min] = 1;
        }
        return true;
    }

    private boolean isAnagram(int[] input, int[] letters, int min, int a, int b) {
        boolean anagram = true;
        int n = b - a;
        int[] test = Writes.createExternalArray(n);
        Writes.arraycopy(input, a, test, 0, n, 0, false, true);
        for (int i = a; i < b; i++) {
            int select = 0;
            boolean any = false;
            for (int j = 0; j < n; j++) {
                if (Reads.compareValues(letters[i], test[j]) == 0) {
                    select = j;
                    any = true;
                    break;
                }
            }
            if (any) Writes.write(test, select, min - 1, 0, false, true);
            else {
                anagram = false;
                break;
            }
        }
        Writes.deleteExternalArray(test);
        return anagram;
    }

    private int maxExponentialSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        int i = 1;
        int cmp = -1;
        if (end - i >= start) cmp = Reads.compareValues(value, array[end - i]);
        while (end - i >= start && (cmp < 0 || (left && cmp == 0))) {
            if (mark) {
                Highlights.markArray(1, end - i / 2);
                Highlights.markArray(2, end - i);
            }
            Delays.sleep(delay);
            i *= 2;
            if (end - i >= start) cmp = Reads.compareValues(value, array[end - i]);
        }
        int a1 = Math.max(start, end - i + 1);
        int b1 = end - i / 2;
        return left ? binSearchLD(array, a1, b1, value) : binSearchRD(array, a1, b1, value);
    }

    private int minExponentialSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        int i = 1;
        int cmp = Reads.compareValues(value, array[start - 1 + i]);
        while (start - 1 + i < end && (cmp > 0 || (!left && cmp == 0))) {
            if (mark) {
                Highlights.markArray(1, start + i / 2);
                Highlights.markArray(2, start - 1 + i);
            }
            Delays.sleep(delay);
            i *= 2;
            if (start - 1 + i < end) cmp = Reads.compareValues(value, array[start - 1 + i]);
        }
        int a1 = start + i / 2;
        int b1 = Math.min(end, start - 1 + i);
        return left ? binSearchLD(array, a1, b1, value) : binSearchRD(array, a1, b1, value);
    }

    private int centerBiasSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        if (end - start < 3) return left ? binSearchLD(array, start, end, value) : binSearchRD(array, start, end, value);
        int m = start + (end - start) / 2;
        int c = Reads.compareValues(value, array[m]);
        if (mark) {
            Highlights.markArray(1, start);
            Highlights.markArray(2, m);
            Highlights.markArray(3, end);
        }
        Delays.sleep(delay);
        if (mark) Highlights.clearMark(3);
        if (c == 0) {
            if (left) return maxExponentialSearch(array, start, m + 1, value, left, delay, mark);
            else return minExponentialSearch(array, m, end, value, left, delay, mark);
        } else if (c < 0) return maxExponentialSearch(array, start, m, value, left, delay, mark);
        else return minExponentialSearch(array, m + 1, end, value, left, delay, mark);
    }

    // CALCIUM-57 MERGE
    private int b(int[] array, int l, int r, int k) {
    	while(l<r) {
    		int m=(l&r)+((l^r)>>1);
    		if(Reads.compareValues(array[m], k) < 0) {
    			r=m;
    		} else {
    			l=m+1;
    		}
    	}
    	return l;
    }

	private void calcMerge(int[] array, int s, int m, int e) {
		if (s != m && m != e && Reads.compareValues(array[m-1], array[m]) <= 0)
			return;
		if ((e-s <= 16 || s == m || m == e) && s < e) {
			cloakOpti(array, s, e);
			return;
		} else if(s >= e)
			return;
		int rz = e-m, r = m, z = s+(m-s)-rz, l = z;
		while (rz > 0) {
			if (Reads.compareValues(array[l], array[r]) == 1) {
				multiSwapSt(array, l, r, rz);
			} else {
				l++;
				rz--;
			}
		}
		this.calcium57(array, m, e);
		if (z > s) {
			int c = b(array, z, e, array[z-1]);
			for (int i = z; i < c; i += z-s) {
				this.calcMerge(array, i - (z - s), i, Math.min(i + z - s, c));
			}
		}
	}

	// CALCIUM-57 SORT (needed for calcium merge to function)
	private void calcium57(int[] array, int s, int e) {
		int m = (e - s + 1) /3;
		if (m < 1)
			return;
		this.calcium57(array, s + m, e);
		this.calcium57(array, s, e - m);
		int z = e - s - 2 * m; // middle size
		this.calcMerge(array, s + Math.min(m, z), e - m, e);
	}

    // DEEP POP COMPONENTS
    private void bubbleSort(int[] array, int start, int end, boolean right) {
    	int swap = end, comp = right ? 1 : -1;
    	while(swap > start) {
    		int lastSwap = start;
    		for(int i=start; i<swap-1; i++) {
    			if(Reads.compareValues(array[i], array[i+1]) == comp) {
    				Writes.swap(array, i, i+1, 0.025, true, false);
    				lastSwap = i+1;
    			}
    		}
    		swap = lastSwap;
    	}
    }

    private void bubblePop(int[] array, int start, int end, boolean right) {
    	int swap = end, comp = right ? 1 : -1;
    	while(swap > start) {
    		int lastSwap = start;
    		for(int i=start; i<swap-1; i++) {
    			if(Reads.compareValues(array[i], array[i+1]) == comp) {
    				Writes.swap(array, i, i+1, 0.025, true, false);
    				lastSwap = i+1;
    			} else if(lastSwap > start)
    				break;
    		}
    		swap = lastSwap;
    	}
    }

    // DEEP POPSORT
    private void dPop(int[] array, int start, int end, int order, boolean invert) {
    	if(start >= end)
    		return;
    	if(end-start <= 4 || order < 1) {
    		this.bubbleSort(array, start, end, !invert);
    		return;
    	}
    	int quarter = (end - start + 1) / 4, half = (end - start + 1) / 2;
    	if(order == 1) {
    		this.dPop(array, start, start + quarter, order, !invert);
    		this.dPop(array, start + quarter, start + half, order, invert);
    		this.dPop(array, start + half, end - quarter, order, !invert);
    		this.dPop(array, end - quarter, end, order, invert);
    		this.dPop(array, start, start + half, order, !invert);
    		this.dPop(array, start + half, end, order, invert);
    		this.bubblePop(array, start, end, !invert);
    	} else {
    		this.dPop(array, start, start+quarter, order, invert);
    		this.dPop(array, start+quarter, start+half, order, !invert);
    		this.dPop(array, start+half, end-quarter, order, invert);
    		this.dPop(array, end-quarter, end, order, !invert);
    		this.dPop(array, start, start+half, order, invert);
    		this.dPop(array, start+half, end, order, !invert);
    		this.dPop(array, start, end, order-1, invert);
    	}
    }



    // BLOCK SHELL COMPONENTS
    private int gappedBinary(int[] A, int P, int l, int K, int G, boolean i) {
    	int L=-1, R=l, C, M;
    	while(L<R-1) {
    		M = L + ((R - L) >> 1);
    		C = Reads.compareValues(A[P+M*G], A[K]);
    		if(C == 1 || (i && C == 0)) {
    			R = M;
    		} else {
    			L = M;
    		}
    	}
    	return R;
    }
    private void GSFW(int[] A, int P, int L, int G) {
    	int t=A[P];
    	for(int i=0; i<L; i++) {
    		Writes.write(A, P+i*G, A[P+(i+1)*G], 1, true, false);
    	}
    	Writes.write(A, P+L*G, t, 1, true, false);
    }
    private void GSBW(int[] A, int P, int L, int G) {
    	int t=A[P+L*G];
    	for(int i=L; i>0; i--) {
    		Writes.write(A, P+i*G, A[P+(i-1)*G], 1, true, false);
    	}
    	Writes.write(A, P, t, 1, true, false);
    }
    private void GMSFW(int[] A, int lA, int lB, int L, int G) {
    	for(int i=0; i<L; i++) {
    		Writes.swap(A, lA+i*G, lB+i*G, 1, true, false);
    	}
    }
    private void GMSBW(int[] A, int lA, int lB, int L, int G) {
    	for(int i=0; i<L; i++) {
    		Writes.swap(A, lA+i*G, lB+i*G, 1, true, false);
    	}
    }
    private void rotate(int[] A, int P, int L, int R, int G) {
    	while(L > 1 && R > 1) {
    		if(L <= R) {
    			GMSFW(A, P, P + L * G, L, G);
    			P += L * G;
    			R -= L;
    		} else {
    			GMSBW(A, P + (L - R) * G, P + L * G, R, G);
    			L -= R;
    		}
    	}
    	if(L>0&&R>0) {
	    	if(L==1)
	    		GSFW(A,P,R,G);
	    	else if(R==1)
	    		GSBW(A,P,L,G);
    	}
    }
    private void lazyMerge(int[] array, int P, int L0, int L1, int G) {
    	int S;
    	if(L0 < L1) {
    		while(L0 != 0) {
    			S = gappedBinary(array, P + L0 * G, L1, P, G, true);
    			if(S != 0) {
    				rotate(array, P, L0, S, G);
    				P += S * G;
    				L1 -= S;
    			}
    			if(L1 == 0)
    				break;
    			do {
    				P += G;
    				L0--;
    			} while(L0 != 0 && Reads.compareValues(array[P], array[P+L0*G]) <= 0);
    		}
    	} else {
    		while(L1 != 0) {
    			S = gappedBinary(array, P, L0, P + (L0+L1-1) * G, G, false);
    			if(S != L0) {
    				rotate(array, P+S*G, L0-S, L1, G);
    				L0 = S;
    			}
    			if(L0 == 0)
    				break;
    			do {
    				L1--;
    			} while(L1 != 0 && Reads.compareValues(array[P+(L0-1)*G], array[P+(L0+L1-1)*G]) <= 0);
    		}
    	}
    }

    private void gappedReverse(int[] array, int start, int end, int gap) {
    	while(start <= end - gap) {
    		Writes.swap(array, start, end, 1, true, false);
    		start+=gap; end-=gap;
    	}
    }

    private int getGappedRun(int[] array, int start, int end, int gap) {
    	int t = start,
    		iD = -Reads.compareValues(array[start], array[start+gap]),
    		len = 1;
    	if(end-start<gap)
    		return 1;
    	if(iD == 0) iD = 1;
    	do {
    		len++;
    		start+=gap;
    	} while(start <= end - gap && Reads.compareValues(array[start], array[start+gap]) != iD);
    	if(iD == -1) {
    		gappedReverse(array, t, start, gap);
    	}
    	return len;
    }

	public void shellPass(int[] array, int start, int end, int gap) {
		if(end-start < gap)
			return;
		int[] starts = new int[gap], lens = new int[gap], ends = new int[gap];
		for(int i=0; i<gap; i++) {
			starts[i] = start+i;
			lens[i] = 0;
			ends[i] = (end-(end%gap))+i;
			if(ends[i] >= end) {
				ends[i]-=gap;
			}
		}
		boolean done;
		do {
			done = true;
			for(int i=0; i<gap; i++) {
				int v=starts[i]+lens[i]*gap;
				if(v > ends[i] || ends[i] == -1)
					continue;
				done=false;
				int r=getGappedRun(array, v, ends[i], gap);
				lazyMerge(array, starts[i], lens[i], r, gap);
				Writes.write(lens, i, lens[i]+r, 0, false, true);
			}

		} while(!done);
	}

    // IPM5 COMPONENTS
	private int binSearchR(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndices(array, m, k, 1, true) > 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}

	private int binSearchL(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndices(array, m, k, 1, true) >= 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}

	private int binSearchLD(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndexValue(array, m, k, 1, true) >= 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}

	private int binSearchRD(int[] array, int a, int b, int k) {
		while (a < b) {
			int m = a + (b - a) / 2;
			if (Reads.compareIndexValue(array, m, k, 1, true) > 0) {
				b = m;
			} else {
				a = m + 1;
			}
		}
		return a;
	}

	private int pos(int a, int l, int m, int v) {
		return v >= l ? m + v - l : a + v;
	}

	private void multiSwap(int[] array, int a, int l, int m, int p1, int p2, int s) {
		for (int i = 0; i < s; i++) {
			int p = pos(a, l, m, p1+i),
				q = pos(a, l, m, p2+i);
			Writes.swap(array, p, q, 0.5, true, false);
		}
	}

	private void multiSwapSt(int[] array, int a, int b, int s) {
		for(int i = 0; i < s; i++) {
			Writes.swap(array, a + i, b + i, 0.5, true, false);
		}
	}

	public void rotateNe(int[] array, int a, int l, int m, int p, int l1, int l2, double sleep, boolean mark, boolean aux) {
		int r, k;
		while(l1 > 0 && l2 > 0) {
			if(l1 > l2) {
				r = l1 % l2;
				for(int i = 0; i < l2; i++) {
					int t = array[pos(a, l, m, p+i+l1)];
					for(int j = l2; j <= l1 - r; j += l2) {
						k = p + i + l1 - j;
						Writes.write(array, pos(a, l, m, k+l2), array[pos(a, l, m, k)], sleep, mark, aux);
					}
					Writes.write(array, pos(a, l, m, p+i+r), t, sleep, mark, aux);
				}
				l1 %= l2;
			} else {
				r = l2 % l1;
				for(int i = 0; i < l1; i++) {
					int t = array[pos(a, l, m, p+i)];
					for(int j = l1; j <= l2 - r; j += l1) {
						k = p + i + j;
						Writes.write(array, pos(a, l, m, k-l1), array[pos(a, l, m, k)], sleep, mark, aux);
					}
					Writes.write(array, pos(a, l, m, p+i+l2-r), t, sleep, mark, aux);
				}
				p += l2 - r;
				l2 %= l1;
			}
		}
	}
	private void insert(int[] array, int from, int to, double sleep, boolean mark, boolean aux) {
		rotateNe(array, 0, 0, 0, Math.min(from, to), from<to?1:from-to, from>to?1:to-from, sleep, mark, aux);
	}

    public void rotate(int[] array, int a, int l, int m, int p, int l1, int l2, double sleep, boolean mark, boolean aux) {
    	if (l1 < 1 || l2 < 1) return;
    	if (l1 % l2 == 0 || l2 % l1 == 0) {
    		rotateNe(array, a, l, m, p, l1, l2, sleep, mark, aux);
    		return;
    	}

    	int A = p, B = p + l1 - 1, C = B + 1, D = B + l2, t;
    	int PA, PB, PC, PD;

    	while (A < B && C < D) {
    		PA = pos(a, l, m, A++); PB = pos(a, l, m, B--);
    		PC = pos(a, l, m, C++); PD = pos(a, l, m, D--);
    		// ABCD -> CADB
    		t = array[PB];
    		Writes.write(array, PB, array[PA], sleep, mark, aux);
    		Writes.write(array, PA, array[PC], sleep, mark, aux);
    		Writes.write(array, PC, array[PD], sleep, mark, aux);
    		Writes.write(array, PD, t, sleep, mark, aux);
    	}

    	while (A < B) {
    		PA = pos(a, l, m, A++); PB = pos(a, l, m, B--);
    		PD = pos(a, l, m, D--);
    		// ABD -> DAB
    		t = array[PB];
    		Writes.write(array, PB, array[PA], sleep, mark, aux);
    		Writes.write(array, PA, array[PD], sleep, mark, aux);
    		Writes.write(array, PD, t, sleep, mark, aux);
    	}

    	while (C < D) {
    		PA = pos(a, l, m, A++); PC = pos(a, l, m, C++);
    		PD = pos(a, l, m, D--);
    		// ACD -> CDA
    		t = array[PC];
    		Writes.write(array, PC, array[PD], sleep, mark, aux);
    		Writes.write(array, PD, array[PA], sleep, mark, aux);
    		Writes.write(array, PA, t, sleep, mark, aux);
    	}

    	while (A < D) {
    		PA = pos(a, l, m, A++); PD = pos(a, l, m, D--);
    		// REVERSE
    		Writes.swap(array, PA, PD, sleep, mark, aux);
    	}
    }

    private void rotateIdx(int[] array, int a, int m, int b, double sleep, boolean mark, boolean aux) {
    	rotate(array, a, m - a, m, 0, m - a, b - m, sleep, mark, aux);
    }

	private int inPlaceMergeV(int[] array, int a, int l, int m, int r) {
		if (l <= 0 || r <= 0 || Reads.compareIndices(array, a + l - 1, m, 1, true) <= 0) // avoid unnecessary merging with sorted check
			return 1;
		if (l == 1 && r == 1) {
			Writes.swap(array, a, m, 1, true, false);
			return 1;
		}
		int ta = a;
		int tm = m, b = m + r;

		a = binSearchR(array, a, a+l-1, m); // get first element greater than array[m]
		l -= a - ta;

		m = binSearchL(array, m+1, b, a); // get first element greater than array[a]

		// loop (break if already greater)
		for (; l > 0; ) {
			int w = m - tm;
			// break away if current block too large
			if (w > l) {
				break;
			}

			// search for next m after merge
			int n = binSearchL(array, m, b, Math.min(a + w, a + l - 1));
			// swap in new block
			multiSwap(array, a, l, tm, 0, l, w);
			// merge
			inPlaceMergeV(array, tm, w, m, n - m);
			// apply iteration to relevant vars
			a += w; l -= w; m = n;
		}
		// merge final part separately
		inPlaceMergeV(array, a, l, m, b - m);
		// rotate it back into place
		rotate(array, a, l, tm, 0, l, m-tm, 0.5, true, false);
		return 0;
	}

    // FURTHER RANDOM SHELL COMPONENTS
    private void shellPassFR(int[] array, int a, int b, int bounding) {
    	int n = b - a;
        for (int h = randInt(1, bounding + 1), i = h; i < n; i++) {
            int v = array[a + i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            while (j >= h && Reads.compareValues(array[a + j - h], v) == 1) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                Writes.write(array, a + j, array[a + j - h], 0.25, true, false);
                j -= h;
                w = true;
            }
            if (w) Writes.write(array, a + j, v, 0.25, true, false);
            h = randInt(1, i < bounding ? i + 1 : bounding + 1);
        }
    }

    private int nextBound(int[] array, int currentLength, int bounding) {
        int gap = bounding;
        boolean passing = true;
        for (; passing && gap > Math.cbrt(currentLength); gap--) for (int i = 0; i + gap < currentLength && passing; i++) if (Reads.compareIndices(array, i, i + gap, 0.005, true) > 0) passing = false;
        return gap;
    }

    // FURTHER RANDOM SHELL SORT
    public void frShell(int[] array, int a, int b) {
    	int n = b - a;
        int bounding = n - 1;
        while (bounding > Math.cbrt(n)) {
            shellPassFR(array, a, b, bounding > 1 ? bounding : 1);
            Highlights.clearAllMarks();
            bounding = nextBound(array, n, bounding);
        }
        shellPassFR(array, a, b, 1);
    }

    // ISSA COMPONENTS
    private void push(int[] array, int a, int b) {
		while (Reads.compareValues(array[a], array[a+1]) > 0 && a < b - 1) {
			Writes.swap(array, a, ++a, 0.05, true, false);
		}
    }
	private void issaPass(int[] array, int a, int b) {
		int i = a, j = a, k = a;
		while (i < b && j < b) {
			if (Reads.compareValues(array[i], array[i+1]) > 0) {
				push(array, i, b);
			}
			if (Reads.compareValues(array[j], array[j+1]) > 0) {
				Writes.swap(array, j, ++j, 0.05, true, false);
			}
			if (Reads.compareValues(array[i], array[j]) < 0) {
				i = j;
			} else {
				i++;
			}
			if (Reads.compareValues(array[k], array[k+1]) > 0) {
				if (k < i) {
					push(array, k++, b);
				} else {
					k = a;
				}
			}
		}
	}

	// ISSASORT
	public void issaSort(int[] array, int a, int b) {
		while(!isRangeSorted(array, a, b)) {
			issaPass(array, a, b);
		}
	}

	// BEST-FOR-N STOOGE COMPONENTS
    protected void bestNStooge(int[] array, int s, int l, int depth) {
        Writes.recordDepth(depth);
        if (l <= 128) {
            Statistics.addStat("Network Use");
            initNetwork(array, s, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 2, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 4, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 2, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 4, l / 2);
        } else {
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
        }
    }

    // BEST-FOR-N STOOGE SORT
    public void bfnStooge(int[] array, int start, int length, int base, int depth, double del) {
        delay = del;
        Statistics.putStat("Network Use");
        Writes.recordDepth(depth);
        if (length <= 64) {
            Statistics.addStat("Network Use");
            initNetwork(array, start, length);
        } else {
            int effectivelen = base;
            while (effectivelen <= length) effectivelen *= 2;
            effectivelen /= 2;
            bestNStooge(array, start, effectivelen, depth);
            if (effectivelen != length) {
                Writes.recursion();
                bfnStooge(array, start + effectivelen, length - effectivelen, base, depth + 1, del);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
                bestNStooge(array, start, effectivelen, depth);
                bestNStooge(array, start + length - effectivelen, effectivelen, depth);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
            }
        }
    }

    // SINGULARITY-COUNTING PIVOT SELECTION
    protected long getPivotSnCnt(int[] array, int start, int end) {
    	/**
    	 * Gets an amount of runs tied to the length (~ n^(1/6)) through Safestalin,
    	 * merges them with BFNStooge or IPM5 depending on final length, then
    	 * selects them using Counting.
    	 **/

    	// Stalin run collection
    	int runsWanted = (int)(Math.ceil(Math.pow(end - start, SINGUCOUNTING_EXP))),
    		tolerableLength = Math.min(32 * runsWanted, (end - start) / 2),
    		runsGot = 0, sleft = start, sfirst = -1;
    	while (runsGot++ < runsWanted && sleft < end) {
    		for (int sright = sleft + 1; sright < end; sright++) {
    			if (Reads.compareIndices(array, sleft, sright, 0.05, true) <= 0) {
    				Writes.swap(array, ++sleft, sright, 0.05, true, false);
    			}
    		}
    		sleft++;
    		if (sfirst < 0) sfirst = sleft;
    	}

    	// Sanity checks
    	if (sfirst >= end) {
    		return -1L;
    	}
    	if (--runsGot < runsWanted) {
    		gamma(array, start, end, false);
    		return -1L;
    	}
    	if(sleft - start >= tolerableLength) {
    		sleft = sfirst; runsGot = 1;
    	}
    	// Merging
    	if (runsGot > 1) {
    		if (sleft - start > tolerableLength) {
    			// Best-for-N Stooge
    			bfnStooge(array, start, sleft - start, 64, 0, 0.25);
    		} else {
    			// Natural IPM5
    			while (runsGot > 1) {
    				int runEnd = findRun(array, sfirst, sleft);
    				inPlaceMergeV(array, start, sfirst - start, sfirst, runEnd - sfirst);
    				sfirst = runEnd;
    				runsGot--;
    				for (; runEnd < sleft;) {
    					int runFirst = findRun(array, runEnd, sleft);
    					if(runFirst < sleft) {
    						int runNextEnd = findRun(array, runFirst, sleft);
    	    				inPlaceMergeV(array, runEnd, runFirst - runEnd, runFirst, runNextEnd - runFirst);
    	    				runsGot--; runEnd = runNextEnd;
    					} else break;
    				}
    			}
    		}
    	}

    	// Pseudo-Counting
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> times = new ArrayList<>();
        values.add(stableReturn(array[start]));
        Writes.allocAmount++; Writes.auxWrites++;
        times.add(1);
        Writes.allocAmount++; Writes.auxWrites++;
        for (int i = start + 1; i < sleft; i++) {
        	int last = times.size() - 1;
            if (Reads.compareIndices(array, i - 1, i, 0.5, true) == 0) {
                int get = times.get(last);
                times.set(last, get + 1);
                Writes.auxWrites++;
            } else {
                values.add(stableReturn(array[i]));
                Writes.allocAmount++; Writes.auxWrites++;
                times.add(1);
                Writes.allocAmount++; Writes.auxWrites++;
            }
            Highlights.markArray(1, i);
            Delays.sleep(1);
        }

        // Pivot Selection
        int cnt = 0;
        int pos = 0;
        while (true) {
            cnt += times.get(pos);
            if (cnt >= (sleft - start) / 2) {
                // If not perfect, determine from the sides what the closest split of uniques is.
                // The highest unique could in theory take value up more than half of the items.
                // So we don't use it if it's not needed.
                // Unaffected by all one unique value (checked alongside range sortedness).
                // This resolves certain side bias, too.
                if (pos > 0 && cnt > (sleft - start) / 2 && Math.abs(cnt - times.get(pos) - (sleft - start) / 2) < Math.abs(cnt - (sleft - start) / 2)) pos--;
                int result = values.get(pos);
                Writes.allocAmount -= 2 * times.size();
                values.clear();
                times.clear();
                return (result & 0xFFFFFFFFL) | ((long)(sleft) << 32);
            } else pos++;
        }
    }


    // CLOAK COMPONENTS
    private int cPull(int[] array, int a, int b, int t) {
    	int s = 0;
    	--b;
    	while(b - t > a) {
    		int l = 2 * t + 1, l2 = 2 * t + 2;

    		int l3 = l2 > b - a || Reads.compareIndices(array, b - l, b - l2, 1, true) > 0 ? l : l2;

    		if(l3 <= b - a && Reads.compareIndices(array, b - t, b - l3, 1, true) < 0) {
    			s = 1;
    			Writes.swap(array, b - l3, b - t, 1, true, false);
    		}
    		t = l3;
    	}
    	return s;
    }

    // OPTIMIZED CLOAKSORT (CALCIUM-57 MERGE NEW SMALLSORT)
    public void cloakOpti(int[] array, int a, int b) {
    	while(b > a) {
    		int t = 0;
    		while(!isMaxSorted(array, a, b)) {
    			int v = array[b - 1];
    			while(t <= (b - a) / 2 && Reads.compareIndexValue(array, b-1, v, 0.5, true) <= 0)
    				t = (t + 1) * (1 - cPull(array, a, b, t));
    		}
    		b--;
    	}
    }

    // CLOAK SORT II *WITH GPART COMPARATORS*
    private int cPullGP(int[] array, int a, int b, int piv, int t) {
    	int s = 0;
    	--b;
    	while(b - t > a) {
    		int l = 2 * t + 1, l2 = 2 * t + 2;

    		int l3 = l2 > b - a || compareIndicesTo(array, b - l, b - l2, piv, BIAS.G, 0.5d) > 0 ? l : l2;

    		if(l3 <= b - a && compareIndicesTo(array, b - t, b - l3, piv, BIAS.G, 0.5d) < 0) {
    			s = 1;
    			Writes.swap(array, b - l3, b - t, 1, true, false);
    		}
    		t = l3;
    	}
    	return s;
    }
    public void cloakGP(int[] array, int a, int b, int piv) {
    	while(b > a) {
    		int t = 0;
    		while(t <= (b - a) / 2)
    			t = (t + 1) * (1 - cPullGP(array, a, b, piv, t));
    		b--;
    	}
	}

    // ASTERACEAE SORT *WITH GPART COMPARATORS*
    public void asteraceaeGP(int[] array, int a, int b, int piv) {
    	int n = b - a;
        int i = a + 1;
        int firstswap = a + 2;
        boolean anyswaps = true;
        boolean lastswap = false;
        while (anyswaps) {
            if (firstswap - 1 == a) {
                i = a + 1;
            } else {
                i = firstswap - 1;
            }
            anyswaps = false;
            lastswap = false;
            while (i + 1 <= b) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.1);
                if (compareValuesTo(array[i - 1], array[i], piv, BIAS.G) > 0) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    i++;
                    if (!anyswaps) {
                        firstswap = i - 1;
                    }
                    anyswaps = true;
                    lastswap = true;
                } else {
                    if (lastswap) {
                        i += (int) Math.floor(Math.sqrt(n));
                    } else {
                        i++;
                    }
                    lastswap = false;
                }
            }
        }
    }

    // PACKWATCH SORT
    public void packwatch(int[] array, int a, int b) {
	   int n = 2, m = b - a;

	   int[] swaps = Writes.createExternalArray(2 * m);

	   while(true) {
		   while(true) {
			   for(int i=0; i<m; i++) {
				   Writes.write(swaps, 2*i, randInt(0, n), 0.05, true, true);
				   Writes.write(swaps, 2*i+1, randInt(0, n), 0.05, true, true);
				   Highlights.markArray(1, swaps[2*i]);
				   Highlights.markArray(2, swaps[2*i+1]);
			   }

			   boolean sorted = true;
			   for(int i=0; i<n-1; i++) {
				   for(int j=0; j<m; j++) {
					   int inda =     i == swaps[2*j] ? swaps[2*j+1] :     i == swaps[2*j+1] ? swaps[2*j] : i,
						   indb = (i+1) == swaps[2*j] ? swaps[2*j+1] : (i+1) == swaps[2*j+1] ? swaps[2*j] : (i+1);
					   sorted = sorted && Reads.compareIndices(array, inda + a, indb + a, 0.005, true) <= 0;
				   }
			   }
			   if(sorted) {
				   Writes.swap(array, randInt(a, b), randInt(a, b), 1, true, false);
				   break;
			   } else {
				   for(int j=0; j<2*m; j+=2) {
					   Writes.swap(array, swaps[j]+a, swaps[j+1]+a, 1, true, false);
				   }
			   }
		   }
		   if(isRangeSorted(array, a, b)) {
			   boolean unique = true, sorted = true;
			   for(int i = 0; i < 2 * m - 1; i++) {
				   int c = Reads.compareOriginalIndices(swaps, i, i+1, 0, false);
				   if(c == 0) unique = false;
				   if(c > 0) sorted = false;
			   }
			   if(n == m && sorted) break;

			   if(unique) {
				   n++;
				   continue;
			   }
		   }
		   n = 2;
	   }
	   Writes.deleteExternalArray(swaps);
	}

    // GNOT A GNOBLIN COMPONENTS
    public void gnomeSingle(int[] array, int s, int a) {
 	   int d = 0;
 	   while(a>s && Reads.compareIndices(array, a, --a, 0.05, true) < 0) {
 		   Writes.swap(array, a, a+1, 0.05, true, true); d++;
 	   }
 	   if(d<gmin) gmin=d;
 	   if(d>gmax) gmax=d;
    }
    protected Void gnomeSingle(Object... vals) {
 	   assert vals.length == 3;
 	   run("gnomeSingle", vals);
 	   return null;
    }

    // GNOT A GNOBLIN SORT
    public void gnotAGnoblin(int[] array, int a, int b) {
    	int n = b - a;
    	ArrayList<int[]> p = new ArrayList<>();
    	int fc = fact(n);
    	Func[] pool = new Func[fc];
    	for(int i=fc; i>0; i--) {
    		bogoSwap(array, a, b, true);
    		p.add(Writes.copyOfRangeArray(array, a, b));
    	}
    	int[][] f = p.toArray(new int[0][]);
    	p.clear();
      gnoblin:
    	while(!isRangeSorted(array, a, b)) {
    		for (int i = 1; i < n; i++) {
			   	gmin=n; gmax=0;
			   	for (int j = 0; j < fc; j++) {
			   		pool[j] = new Func(f[j], 0, i).setConsumer(this::gnomeSingle);
			   		pool[j].start();
			   	}
			   	for (int j = 0; j < fc; j++) {
			   		try {
	    				pool[j].join();
			   		} catch(InterruptedException e) {
	    				Thread.currentThread().interrupt();
	    			}
			   	}
			   	if(gmin != gmax) {
			   		for (int j = 0; j < fc; j++) {
			   			bogoSwap(f[j], 0, n, true);
			   		}
			   		continue gnoblin;
			   	}
    		}
    		int m = 0, mi = 0;
    		for (int i = 0; i < fc; i++) {
    			int qa = randInt(0, n),
    				qb = randInt(0, n),
    				c = Math.abs(qb - qa);
    			if(c>m) {m=c; mi=i;}
    			Writes.multiSwap(f[i], qa, qb, 0.01, true, true);
    		}
    		Writes.arraycopy(f[mi], 0, array, a, n, 1, true, false);
    	}
	   Writes.deleteExternalArrays(f);
    }

    // JUGGLING PIVOT PARTITION (clause: pivot must exist in list[l:r])
	public int jPart(int[] array, int l, int m, int r, int piv, int d) {
		int a = m, b = r;
        for(int i = l; i < m; i++) {
        	if(Reads.compareIndexValue(array, i, piv, 0.5, true) == 0) {
        		Writes.multiSwap(array, i, m, 0.25, true, false);
        		break;
        	}
        }
        while(a < b) {
        	while(a < b && Reads.compareIndices(array, a, b, 0.25, true) <= 0) {
        		b--;
        	}
        	if(a < b) {
	    		Writes.swap(array, a, b, 1, true, false);
        	}
        	while(a == b || (a < b && Reads.compareIndices(array, a, b, 0.25, true) <= 0)) {
        		a++;
        	}
        	if(a < b) {
	    		Writes.swap(array, a, b, 1, true, false);
        	} else {
        		pcboyGamesIsYourNewGod(array, a, r, --d);
        		return b;
        	}
        }
        return -1; // actual deadcode
    }

    // SINGULARITY PARTITION (clause: must handle most of the recursion :sadge:)
	public int singularityDown(int[] array, int l, int m, int r, int d) {
		while(m-- > l) {
			int p = m + 1, pe = m;
			int piv = array[m];
			for(; p < r; p++) {
				if(Reads.compareIndexValue(array, p, piv, 0.5, true) <= 0) {
					Writes.swap(array, pe++, p, 0.5, true, false);
				}
			}
			pcboyGamesIsYourNewGod(array, pe, r, d - 1);
			r = pe;
		}
        return r;
    }

	// ECOLO SORT *WITH GPART COMPARATORS*
	private void ecoloGP(int[] array, int a, int b, int piv) {
        int left = a + 1;
        int right = b;
        int way = 1;
        int i = a + 1;
        while (left <= right) {
            if (way == 1) {
                i = left;
            } else {
                i = right;
            }
            while ((way == 1 && i < right) || (way == -1 && i > left)) {
                if (compareIndicesTo(array, left - 1, i - 1, piv, BIAS.G, 0.05) > 0) {
                    Writes.swap(array, left - 1, i - 1, 0.05, true, false);
                }
                if (compareIndicesTo(array, i - 1, right - 1, piv, BIAS.G, 0.05) > 0) {
                    Writes.swap(array, i - 1, right - 1, 0.05, true, false);
                }
                i += way;
            }
            left++;
            right--;
            way *= -1;
        }
	}

	// STANILE REVERSAL
    public void stableRev(int[] array, int a, int b, double sleep) {
    	if(b - a == 1) {
    		if(Reads.compareIndices(array, a, b, sleep, true) != 0)
    			Writes.swap(array, a, b, 0, true, false);
    		return;
    	}
    	if(a >= b) return;
    	int m = b - (b - a) / 2;
    	LinkedList<ImplQueue<Integer>> headloc = new LinkedList<>()
    	                             , tailloc = new LinkedList<>();
    	headloc.add(new ImplQueue<>());
    	headloc.getFirst().add(a);
    	Writes.changeAllocAmount(1);
    	Writes.changeAuxWrites(1);
    	for(int i = a + 1; i <= m; i++) {
    		int l = 0, r = headloc.size();
    		boolean eq = false;
    		z:
    		while(l < r) {
    			int M = l + (r - l) / 2;
    			switch(Reads.compareIndices(array, headloc.get(M).peek(0), i, sleep/4d, true)) {
    				case 1:
    					r = M;
    					break;
    				case 0:
    					l = M;
    					eq = true;
    					break z;
    				case -1:
    					l = M + 1;
    					break;
    			}
    		}
    		if(!eq)
    			headloc.add(l, new ImplQueue<>());
    		headloc.get(l).add(i);
        	Writes.changeAllocAmount(1);
        	Writes.changeAuxWrites(1);
    	}
    	z:
    	for(int i = b; i > m; i--) {
    		int l = 0, r = tailloc.size();
    		boolean eq = false;
    		y:
    		while(l < r) {
    			int M = l + (r - l) / 2;
    			switch(Reads.compareIndices(array, tailloc.get(M).peek(0), i, sleep/4d, true)) {
    				case 1:
    					r = M;
    					break;
    				case 0:
    					l = M;
    					eq = true;
    					break y;
    				case -1:
    					l = M + 1;
    					break;
    			}
    		}

    		int l2 = 0, r2 = headloc.size();
    		y:
    		while(l2 < r2) {
    			int M = l2 + (r2 - l2) / 2;
    			switch(Reads.compareIndices(array, headloc.get(M).peek(0), i, sleep/4d, true)) {
    				case 1:
    					r2 = M;
    					break;
    				case 0:
    					int j = eq ? tailloc.get(l).shift() : i;
    					Writes.swap(array, headloc.get(M).shift(), j, sleep, true, false);
    			    	Writes.changeAllocAmount(eq?-2:-1);
    					if(headloc.get(M).isEmpty()) headloc.remove(M);
    					if(eq && tailloc.get(l).isEmpty()) {
    						tailloc.remove(l);
    						continue z;
    					} else if(!eq) continue z;
    					break y;
    				case -1:
    					l2 = M + 1;
    					break;
    			}
    		}
    		if(!eq)
    			tailloc.add(l, new ImplQueue<>());
    		tailloc.get(l).add(i);
	    	Writes.changeAllocAmount(1);
	    	Writes.changeAuxWrites(1);
    	}
    	while(!headloc.isEmpty()) {
    		ImplQueue<Integer> p = headloc.removeFirst();
    		while(!p.isEmpty()) {
		    	Writes.changeAllocAmount(p.size()==1?-1:-2);
    			if(p.size() > 1)
    				Writes.swap(array, p.shift(), p.pop(), sleep, true, false);
    			else
    				p.pop();
    		}
    	}
    	while(!tailloc.isEmpty()) {
    		ImplQueue<Integer> p = tailloc.removeFirst();
    		while(!p.isEmpty()) {
		    	Writes.changeAllocAmount(p.size()==1?-1:-2);
    			if(p.size() > 1)
    				Writes.swap(array, p.shift(), p.pop(), sleep, true, false);
    			else
    				p.pop();
    		}
    	}
    	Writes.reversal(array, a, b, sleep, true, false);
    }

    // CALIFORNIUM SORT *STANILE, WITH GPART COMPARATORS*
	private void californiumGP(int[] array, int a, int b, int piv, int d, int d2) {
		if(b - a == 2) {
			if(compareIndicesTo(array, a, a+1, piv, BIAS.G, 0.5d) > 0)
				Writes.swap(array, a, a+1, 1, true, false);
		}
		Writes.recordDepth(d++);
		if(--d2 < 1) {
			// TINY GNOME
			for(int i=a; ++i<b;) {
				if(compareIndicesTo(array, i - 1, i, piv, BIAS.G, 0.05d) > 0) {
					Writes.swap(array, i-1, i, 0.1d, true, false);
					i=a;
				}
			}
			return;
		}
		if(a >= b - 2)
			return;
		LinkedList<Integer> recurse = new LinkedList<>(), // stack to recursively Californium on
		                    maxstack = new LinkedList<>(); // list of sqrt(n) maximum items (dupes not included)
		int i = b, // boundary
		    j, k, m, me = 0, b0 = b; // temp
		boolean shd = false;
		bndchk:
		while(i > a + 1) {
			Writes.changeAllocAmount(-maxstack.size()+1);
			recurse.addFirst(i = b0);
			Writes.changeAuxWrites(1);
			maxstack.clear();
			for(k = sqrtrnd(i - a); k-- > 0;) {
				shd = !maxstack.isEmpty() && compareIndexValueTo(array, a, maxstack.getFirst(), piv, BIAS.G, 0.01) >= 0;
				me = 0;
				for(j = (m = a) + 1; j < i; j++) {
					int p = Reads.compareIndices(array, m, j, 0.01, true);
					if(p == 0) me++;
					if(p < 0 || shd)
						if(maxstack.isEmpty() || compareIndexValueTo(array, j, maxstack.getFirst(), piv, BIAS.G, 0.01) < 0) {
							m = j;
							shd = false;
							me = 0;
						}
				}
				if(shd)
					break bndchk;
				maxstack.addFirst(array[m]);
				Writes.changeAllocAmount(1);
				Writes.changeAuxWrites(1);
				k -= me;
			}
			cnt:
			for(j = b0 = a; j < i; j++) {
				for(int v : maxstack) {
					if(compareIndexValueTo(array, j, v, piv, BIAS.G, 0.01) == 0) {
						stableRev(array, b0, j - 1, 0.1);
						continue cnt;
					}
				}
				stableRev(array, a, b0 - 1, 0.001);
				stableRev(array, b0++, j, 0.001);
			}
		}
		if(me == b - a - 1)
			return;
		j = a;
		for(k = 0; k < recurse.size(); k++) {
			Writes.recursion();
			californiumGP(array, j, j = recurse.get(k), piv, d, d2);
		}
		Writes.changeAllocAmount(-recurse.size());
	}


	// GAMMA SORT COMPONENTS
	// [RANGED GETPERMUTATIONS WIP!]
	private void getpermutations(int[] array, int depth, int a, int b, ArrayList<int[]> p, boolean aux) {
		int n = b - a;
		if (depth >= n - 1) {
			p.add(Writes.copyOfRangeArray(array, a, b));
			return;
		}

       	for (int i = b - 1; i > a + depth; --i) {
       		getpermutations(array, depth+1, a, b, p, aux);

           	if ((n - depth) % 2 == 0) {
               	Writes.swap(array, a + depth, i, this.delay, true, aux);
           	} else {
               	Writes.swap(array, a + depth, b - 1, this.delay, true, aux);
           	}
       	}
       	getpermutations(array, depth + 1, a, b, p, aux);
	}

	private void wtfBogo(int[] array, int a, int b, boolean aux) {
	   	if (a < b) {
		   	this.wtfBogo(array, a, b - 1, aux);
		   	this.bogoSwap(array, a, b, aux);
		   	this.wtfBogo(array, a + 1, b, aux);
	   	}
   	}

   	private boolean allMatch(int[][] arrays, int n) {
	   	boolean f = true;
	   	for (int i = 1; i < arrays.length; i++) {
		   	for (int j = 0; j < n; j++) {
			   	if (Reads.compareValues(arrays[i - 1][j], arrays[i][j]) != 0) {
				   	this.wtfBogo(arrays[i - 1], 0, n, true);
				   	gamma(arrays[i], 0, n - 1, true);
				   	f = false;
			   	}
		   	}
	   	}
	   	return f;
   	}

   	private boolean isBogoBogoRangeSorted(int[] array, int a, int b, boolean aux) {
	   	this.wtfBogo(array, a, b, aux);
	   	gamma(array, a, b - 1, aux);
	   	return Reads.compareIndices(array, b - 2, b - 1, 1, true) <= 0;
   	}

   	// GAMMA SORT [RANGED VER. WIP!]
   	public void gamma(int[] array, int a, int b, boolean aux) {
   		int n = b - a;
	   	if (n < 2) return; // catch bad eggs

	   	ArrayList<int[]> p = new ArrayList<>();
	   	getpermutations(array, 0, a, b, p, aux);
	   	int[][] perms = p.toArray(new int[0][]);
	   	while (!isBogoBogoRangeSorted(array, a, b, aux)) {
		   	do {
			   	for(int[] i : perms)
				   this.wtfBogo(i, 0, n, true);
		   	} while (!allMatch(perms, n));
		   Writes.arraycopy(perms[0], 0, array, a, n, 1, true, aux);
	   	}
	   	Writes.deleteExternalArrays(perms);
   	}

   	// [vɯβu]SORT COMPONENTS
	private void omegaPush(int[] array, int start, int end) {
    	for (int i = 0; i<end - start - 1; i++) {
    		Writes.multiSwap(array, end - 1, start, 0.01, true, false);
    	}
    }

    private void omegaPushBW(int[] array, int start, int end) {
    	for (int i = 0; i < end - start - 1; i++) {
    		Writes.multiSwap(array, start, end-1, 0.01, true, false);
    	}
    }

    private void omegaPush(int[] array, int start, int end, int k) {
    	if (k == 0) {
    		omegaPush(array, start, end);
    	} else
	    	for(int i=0; i<end-start-1; i++) {
	    		omegaPushBW(array, start, end, k-1);
	    	}
    }

    private void omegaPushBW(int[] array, int start, int end, int k) {
    	if (k == 0) {
    		omegaPushBW(array, start, end);
    	} else
	    	for(int i=0; i<end-start-1; i++) {
	    		omegaPush(array, start, end, k-1);
	    	}
    }

    private void omegaSwap(int[] array, int start, int end, int r) {
    	if (start >= end)
    		return;
    	Writes.recordDepth(r++);
    	this.omegaPush(array, start, end+1, end-start);
    	this.omegaPushBW(array, start, end, end-start);
    	this.omegaSwap(array, start+1, end-1, r);
    	this.omegaSwap(array, start+1, end-1, r);
    }

    private void omegaOmegaPush1(int[] array, int start, int end, int depth) {
    	depth++;
    	for (int j=end-1; j>=start; j--) {
    		omegaSwap(array, j, end-1, depth);
    	}
    }

    private void omegaOmegaPushBW1(int[] array, int start, int end, int depth) {
    	depth++;
    	for(int j=start+1; j<end; j++) {
    		omegaSwap(array, start, j, depth);
    	}
    }

    private void omegaOmegaPush(int[] array, int start, int end, int depth) {
    	depth++;
    	for(int i=start; i<end-1; i++) {
    		omegaOmegaPushBW1(array, start, end, depth);
    	}
    }

    private void omegaOmegaPushBW(int[] array, int start, int end, int depth) {
    	depth++;
    	for(int i=start; i<end-1; i++) {
    		omegaOmegaPush1(array, start, end, depth);
    	}
    }

    private void omegaOmegaSwap(int[] array, int start, int end, int r) {
    	if(start >= end)
    		return;
    	Writes.recordDepth(r++);
    	this.omegaOmegaPush(array, start, end+1, r);
    	this.omegaOmegaPushBW(array, start, end, r);
    	this.omegaOmegaSwap(array, start+1, end-1, r);
    	this.omegaOmegaSwap(array, start+1, end-1, r);
    }

    private void omegaOmegaOmegaPushBW(int[] array, int start, int end, int depth) {
    	depth++;
    	for(int j=start+1; j<end; j++) {
    		omegaOmegaSwap(array, start, j, depth);
    	}
    }

    private void what_why(int[] array, int start, int end, int d) {
    	Writes.recordDepth(d++);
    	int m=(end-start)/2;
    	if(m==0)
    		return;
    	for(int i=0;i<m;i++) {
    		omegaOmegaOmegaPushBW(array, start, end, d);
    	}
    	what_why(array, start, start+m, d);
    	what_why(array, start+m, end, d);
    }

    private void wotateOwO(int[] array, int pos, int range, int amount, int d) {
    	this.what_why(array, pos, pos+range-amount, d);
    	this.what_why(array, pos+range-amount, pos+range, d);
    	this.what_why(array, pos, pos+range, d);
    }

	private int[] generateNegligiblyUnchangingList(int[] array) { // O((m^2)^^2) avg.
		int[][] lists = new int[array.length][];
	  regenLists:
		for(int ID=0; ID<lists.length;) {
			lists[ID] = Writes.createExternalArray(array.length);
			for(int i=0; i<array.length; i++) {
				int r;
			  rejectSample:
				for(;;) {
					r = randInt(0, array.length);
					for(int j=0; j<i; j++) {
						if(Reads.compareOriginalValueIndex(lists[ID], r, j, 0.01, true) == 0)
							continue rejectSample;
					}
					break;
				}
				Writes.write(lists[ID], i, r, 0.01, true, true);
			}
			if(ID > 0) {
				for(int i=0; i<array.length; i++) {
					if(Reads.compareOriginalValues(lists[ID-1][i], lists[ID][i]) != 0) {
						for(int ii=0; ii<=ID; ii++) {
							Writes.deleteExternalArray(lists[ID]);
						}
						ID=0; continue regenLists;
					}
				}
			}
			ID++;
		}
		for(int ID=1; ID<lists.length; ID++) {
			Writes.deleteExternalArray(lists[ID]);
		}
		return lists[0];
	}

	private int randomSearchInTable(int[] array, int a, int b, int k) {
		if(Reads.compareOriginalIndices(array, b-1, k, 0.1, true) <= 0)
			return b;
		int l = a, r = b;
		while(l < r) {
			int m = randInt(a, b);
			if(m <= l || m > r) continue;
			int c = Reads.compareOriginalIndices(array, m, k, 0.1, true);
			if(c <= 0) {
				l = m + 1;
			}
			if(c >= 0) {
				r = m;
			}
		}
		return l;
	}

	private void insertTable(int[] table, int[] array, int a, int b) {
		for(int i=a+1; i<b; i++) {
			int l = randomSearchInTable(table, a, i, table[i]);
			wotateOwO(table, a+l, i-l, 1, 1);
			wotateOwO(array, a+l, i-l, 1, 1);
		}
	}

	private boolean isArrayProbablySorted(int[] array, int a, int b) {
		return isRangeSorted(array, a, b) && randInt(0, array.length) == 0;
	}

   	// FIXED [vɯβu]SORT
	public void vubuPhon(int[] array, int a, int b) {
	  doProbableCheck:
		while(!isArrayProbablySorted(array, a, b)) {
			Writes.recursion();
			int[] indice = generateNegligiblyUnchangingList(array);
			// if this works as intended, the chance of this passing is 1/((m/n)^n)
			for(int i=a; i<b; i++) {
				if(Reads.compareOriginalIndexValue(indice, i, a, 0.01, true) < 0 ||
				   Reads.compareOriginalIndexValue(indice, i, b, 0.01, true) >= 0) {
					Writes.deleteExternalArray(indice);
					continue doProbableCheck;
				}
			}
			insertTable(indice, array, a, b);
			Writes.deleteExternalArray(indice);
		}
	}

	// COSPO SORT COMPONENTS
    private void sortexternal(int[] array, int[] correct, int a, int b) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = a; i < b; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int mi = min;
        int size = max - mi + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = a; x < b; x++) {
            Highlights.markArray(1, x);
            Writes.write(holes, array[x] - mi, holes[array[x] - mi] + 1, 1, false, true);
        }
        Highlights.clearMark(2);
        int j = 0;
        for (int count = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++) {
                Writes.write(correct, j, count + mi, 1, false, true);
                Highlights.markArray(1, j + 1);
                j++;
            }
        }
        Writes.deleteExternalArray(holes);
    }

    // COSPO SORT
	public void cospo(int[] array, int a, int b) {
        int[] correct = Writes.createExternalArray(b - a);
        sortexternal(array, correct, a, b);

        int spot1 = 0;
        int spot2 = 0;
        int verifyL = a;
        int verifyR = b - 1;
        while (Reads.compareValues(array[verifyL], correct[verifyL]) == 0) {
            Highlights.markArray(1, verifyL);
            Delays.sleep(delay);
            verifyL++;
            if (verifyL == verifyR) break;
        }
        if (verifyL != verifyR) {
            while (Reads.compareValues(array[verifyR], correct[verifyR]) == 0) {
                Highlights.markArray(1, verifyR);
                Delays.sleep(delay);
                verifyR--;
            }
        }
        while (verifyL < verifyR) {
            Highlights.clearAllMarks();
            boolean spot1found = false;
            while (!spot1found) {
                spot1 = randInt(verifyL, verifyR + 1);
                Highlights.markArray(1, spot1);
                Delays.sleep(0);
                spot1found = Reads.compareValues(array[spot1], correct[spot1]) != 0;
            }
            boolean spot2found = false;
            while (!spot2found) {
                spot2 = randInt(verifyL, verifyR + 1);
                Highlights.markArray(1, spot2);
                Delays.sleep(0);
                if (spot1 != spot2) {
                    spot2found = Reads.compareValues(array[spot2], correct[spot2]) != 0;
                }
            }
            Writes.swap(array, spot1, spot2, delay, true, false);
            Highlights.clearAllMarks();
            while (Reads.compareValues(array[verifyL], correct[verifyL]) == 0) {
                Highlights.markArray(1, verifyL);
                Delays.sleep(delay);
                verifyL++;
                if (verifyL == verifyR) break;
            }
            if (verifyL != verifyR) {
                while (Reads.compareValues(array[verifyR], correct[verifyR]) == 0) {
                    Highlights.markArray(1, verifyR);
                    Delays.sleep(delay);
                    verifyR--;
                }
            }
        }
	}

	// POWER SET SORT
	public void powSetSort(int[] array, int a, int b) {
		int n = b - a;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = a; i < b; i++) {
            if (stableReturn(array[i]) < min) min = stableReturn(array[i]);
            if (stableReturn(array[i]) > max) max = stableReturn(array[i]);
        }
        boolean equals = !hasNoDupes(array, min, max, a, b);
        int[] init = Writes.createExternalArray(n);
        Writes.arraycopy(array, a, init, 0, n, 0, false, true);
        for (int i = a; i < b; i++) Writes.write(array, i, min, 0.1, true, false);
        boolean finalized = isAnagram(array, init, min, a, b);
        while (!finalized) {
            if (equals) {
                boolean goback = false;
                for (int i = b - 1; i > a; i--) {
                    if (Reads.compareValues(array[i], max) >= 0) Writes.write(array, i, min, 0.1, goback = true, false);
                    else {
                        Writes.write(array, i, array[i] + 1, 0.1, true, false);
                        break;
                    }
                }
                if (goback) for (int i = a; i + 1 < b; i++) if (Reads.compareValues(array[i], array[i + 1]) > 0) while (i + 1 < b) Writes.write(array, i + 1, array[i++], 0.1, true, false);
            } else {
                boolean loop = true;
                while (loop) {
                    boolean goback = false;
                    for (int i = b - 1; i > a; i--) {
                        if (Reads.compareValues(array[i], max) >= 0) Writes.write(array, i, min, 0.1, goback = true, false);
                        else {
                            Writes.write(array, i, array[i] + 1, 0.1, true, false);
                            break;
                        }
                    }
                    if (goback) {
                        for (int i = a; i + 1 < b; i++) {
                            if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                                while (i + 1 < b) {
                                    if (array[i] + 1 <= max) Writes.write(array, i + 1, array[i++] + 1, 0.1, true, false);
                                    else {
                                        Writes.write(array, i + 1, max + 1, 0.1, true, false);
                                        while (array[i + 1] - array[i] == 1) i--;
                                        Writes.write(array, i, array[i] + 1, 0.1, true, false);
                                        while (i + 1 < b) Writes.write(array, i + 1, array[i++] + 1, 0.1, true, false);
                                    }
                                }
                            }
                        }
                    }
                    if (Reads.compareValues(array[b - 1], max) <= 0) loop = false;
                }
            }
            finalized = isAnagram(array, init, min, a, b);
        }
	}

	// BISMUTH COMPONENTS
    private void ins(int[] array, int start, int end) {
        if (end - start > 32) frShell(array, start, end);
        else shellPass(array, start, end, 1);
    }

    private void prepareSegments(int[] array, int start, int end, int bSize) {
        int i = start;
        for (; i + bSize <= end; i += bSize) ins(array, i, i + bSize);
        ins(array, i, end);
    }

    private int fcomp(int[] array, int a, int b, int f) {
        int c = stableReturn(array[a]), d = stableReturn(array[b]);
        if (c < d) return -1;
        else if (c > d) return 1;
        else return 0;
    }

    private void blockSelect(int[] array, int start, int end, int mid, int bSize) {
        for (int i = start, sel = i, right = mid, f = 0; i + bSize <= end; i += bSize, sel = i, f = 0) {
            if (i == mid) {
                for (; mid + 2 * bSize <= Math.min(right, end - bSize); mid += bSize) {
                    int cmp = fcomp(array, mid, mid + bSize, f++);
                    if (cmp > 0) break;
                    else if (cmp == 0 && bSize > 1) if (fcomp(array, mid + bSize - 1, mid + 2 * bSize - 1, f++) > 0) break;
                }
                mid += bSize;
            }
            for (int j = Math.max(i + bSize, mid); j <= Math.min(right, end - bSize); j += bSize) {
                int comp = fcomp(array, sel, j, f++);
                if (comp > 0) sel = j;
                else if (comp == 0 && bSize > 1 && fcomp(array, sel + bSize - 1, j + bSize - 1, f++) > 0) sel = j;
            }
            Reads.addComparisons(f);
            if (sel == right) right += bSize;
            if (sel != i) for (int j = 0; j < bSize; j++) Writes.swap(array, i + j, sel + j, 1, true, false);
            else {
                Highlights.clearMark(2);
                Highlights.markArray(1, i);
            }
        }
    }

    private int toBuff(int[] array, int start, int end, int tStart) {
        int buffPos = 0;
        for (; start < end; start++) Writes.swap(array, start, tStart + buffPos++, 1, true, false);
        return buffPos;
    }

    private boolean doesRotate(int[] array, int start, int mid, int end) {
        boolean did = false;
        if (Reads.compareIndices(array, start, end - 1, 1, true) >= 0) rotateIdx(array, start, mid, end, 1, did = true, false);
        else if (Reads.compareIndices(array, start, end - 2, 1, true) >= 0) {
            rotateIdx(array, start, mid, end - 1, 1, did = true, false);
            insert(array, end - 1, maxExponentialSearch(array, start, end - 1, array[end - 1], false, 1, true), 1, true, false);
        }
        return did;
    }

    private int buffer(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean dir, boolean skip) {
        int i = mid + bSize;
        if (skip) {
            for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
            if (i == mid + bSize) return buffer(array, start, mid, end, tStart, tEnd, bSize, true, false);
            int sEnd = minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true);
            if (doesRotate(array, start, mid, sEnd)) return i - 1;
            int buffLen = toBuff(array, start, mid, tStart), buffPos = 0, right = mid, left = start;
            while (buffPos < buffLen && left < right) {
                int target = minExponentialSearch(array, right, i, array[tStart + buffPos], true, 1, true);
                while (right < target) Writes.swap(array, left++, right++, 1, true, false);
                Writes.swap(array, tStart + buffPos++, left++, 1, true, false);
                if (right == sEnd) while (buffPos < buffLen) Writes.swap(array, tStart + buffPos++, left++, 1, true, false);
            }
        } else if (doesRotate(array, start, mid, end)) i = start;
        else {
            int buffLen = toBuff(array, dir ? start : mid, dir ? mid : end, tStart), buffPos = dir ? 0 : buffLen - 1, left = dir ? start : mid - 1, right = dir ? mid : end - 1;
            while ((dir && buffPos < buffLen) || (!dir && buffPos >= 0)) {
                if (Reads.compareIndices(array, dir ? tStart + buffPos : left, dir ? right : tStart + buffPos, 1, true) <= 0) Writes.swap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++: right--, 1, true, false);
                else Writes.swap(array, dir ? left++ : left--, dir ? right++ : right--, 1, true, false);
                if (right == end || left < start) while ((dir && buffPos < buffLen) || (!dir && buffPos >= 0)) Writes.swap(array, dir ? tStart + buffPos++ : tStart + buffPos--, dir ? left++ : right--, 1, true, false);
            }
            i = start;
        }
        return --i;
    }

    private int noBuffer(int[] array, int start, int mid, int end, int tEnd, int bSize, boolean skip) {
        int sEnd, i = mid + bSize;
        if (skip) for (; i < tEnd; i += bSize) if (Reads.compareIndices(array, i - 1, mid - 1, 1, true) > 0) break;
        sEnd = skip ? minExponentialSearch(array, i - bSize, i, array[mid - 1], true, 1, true) : end;
        if (doesRotate(array, start, mid, sEnd)) return start;
        if (Math.min(mid - start, sEnd - mid) > 9) giveUpMergeOneRotate(array, start, mid, sEnd, bSize, mid - start < sEnd - mid, false, false, 0);
        else lazyMerge(array, start, mid - start, sEnd - mid, 1);
        return sEnd;
    }

    private int twoBlocks(int[] array, int start, int mid, int end, int tStart, int tEnd, int bSize, boolean useBuffers) {
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return start;
        int left = minExponentialSearch(array, start + 1, mid, array[mid], false, 1, true), right = maxExponentialSearch(array, mid, end - 1, array[mid - 1], true, 1, true);
        if (Math.min(mid - 1 - left, right - mid) > 8 && useBuffers) return buffer(array, left, mid, right + 1, tStart, tEnd, bSize, left - start > end - 1 - right, right == end - 1);
        else return noBuffer(array, left, mid, right + 1, tEnd, bSize, right == end - 1);
    }

    private void merge(int[] array, int start, int end, int mid, int tStart, int bSize, boolean useBuffers) {
        if (mid >= end) return;
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return;
        blockSelect(array, start, end, mid, bSize);
        if (bSize == 2) for (int i = start; i + 1 < end; i++) if (Reads.compareIndices(array, i, i + 1, 1, true) > 0) Writes.swap(array, i, i + 1, 1, true, false);
        if (bSize <= 2) return;
        for (int i = start, p = start; i + 2 * bSize <= end; i += bSize) if (i + bSize >= p) p = twoBlocks(array, i, i + bSize, i + 2 * bSize, tStart, end, bSize, useBuffers);
    }

    private void giveUpMerge(int[] array, int start, int mid, int end, int bSize, boolean side, boolean pre, boolean searches, int d) {
        Writes.recordDepth(d);
        if (pre) ins(array, side ? start : mid, side ? mid : end);
        if (searches) start = minExponentialSearch(array, start, mid, array[mid], true, 1, true);
        if (searches) end = maxExponentialSearch(array, mid, end, array[mid - 1], false, 1, true);
        if (Math.min(mid - start, end - mid) <= 8) {
            lazyMerge(array, start, mid - start, end - mid, 1);
            return;
        }
        if (mid - start < end - mid != side) side = !side;
        int pos = side ? minExponentialSearch(array, mid, end, array[start], true, 1, true) : maxExponentialSearch(array, start, mid, array[end - 1], false, 1, true), s = side ? mid - start - 1 : end - mid - 1, g = (int) Math.sqrt(s + 1), b = end;
        rotateIdx(array, side ? start : pos, mid, side ? pos : end, 1, true, false);
        mid = pos;
        if (side) start = pos - s;
        else end = pos + s;
        while (s > g + 1 && Math.min(mid - start, end - mid) > 8) {
            if (doesRotate(array, start, mid, end)) return;
            pos = side ? minExponentialSearch(array, mid, end, array[start + g], true, 1, true) : maxExponentialSearch(array, start, mid, array[end - g - 1], false, 1, true);
            rotateIdx(array, side ? start + g : pos, mid, side ? pos : end - g, 1, true, false);
            if (((side && pos > mid) || (!side && pos < mid)) && Math.abs(pos - mid) > 8) {
                Writes.recursion();
                giveUpMerge(array, side ? start : pos, side ? start + g : end - g, side ? pos : end, bSize, side, false, false, d + 1);
            } else lazyMerge(array, side ? start : pos, side ? g : end - pos - g, side ? pos - start - g : g, 1);
            s -= g + 1;
            mid = pos;
            if (side) start = pos - s;
            else end = pos + s;
            if (mid - start < end - mid != side) {
                side = !side;
                s = side ? mid - start : end - mid;
            }
        }
        if (s > 0) lazyMerge(array, start, side ? s : mid - start, side ? end - start - s : Math.min(s + 1, b - mid), 1);
    }

    private void giveUpMergeOneRotate(int[] array, int start, int mid, int end, int bSize, boolean side, boolean pre, boolean searches, int d) {
        Writes.recordDepth(d);
        if (pre) ins(array, side ? start : mid, side ? mid : end);
        if (searches) start = minExponentialSearch(array, start, mid, array[mid], true, 1, true);
        if (searches) end = maxExponentialSearch(array, mid, end, array[mid - 1], false, 1, true);
        if (Math.min(mid - start, end - mid) <= 8) {
            lazyMerge(array, start, mid - start, end - mid, 1);
            return;
        }
        if (mid - start < end - mid != side) side = !side;
        int pos = side ? minExponentialSearch(array, mid, end, array[start], true, 1, true) : maxExponentialSearch(array, start, mid, array[end - 1], false, 1, true), s = side ? mid - start - 1 : end - mid - 1, g = side ? (mid - start) / 2 : (end - mid) / 2;
        rotateIdx(array, side ? start : pos, mid, side ? pos : end, 1, true, false);
        mid = pos;
        if (side) start = pos - s;
        else end = pos + s;
        if (doesRotate(array, start, mid, end)) return;
        pos = side ? centerBiasSearch(array, mid, end, array[start + g], true, 1, true) : centerBiasSearch(array, start, mid, array[end - g - 1], false, 1, true);
        rotateIdx(array, side ? start + g : pos, mid, side ? pos : end - g, 1, true, false);
        giveUpMerge(array, side ? start : pos, side ? start + g : end - g, side ? pos : end, bSize, side, false, false, d);
        giveUpMerge(array, side ? pos - (s - g) + 1 : start, pos, side ? end : pos + s - g, bSize, side, false, false, d);
    }

    // BISMUTH SORT
    public void bismuthSort(int[] array, int start, int end, int getSize, boolean useBuffers) {
        if (getSize == 0) getSize = (int) Math.sqrt(end - start);
        if (getSize > end - start) getSize = end - start;
        int balance = (end - start) % getSize, deadEnd = end - balance;
        if (end - start < 33) ins(array, start, end);
        if (end - start < 33 || findRun(array, start, end) >= end) return;
        if (getSize < 11 || !useBuffers) ins(array, start, start + getSize);
        prepareSegments(array, start + getSize, deadEnd, getSize);
        for (int cur = getSize * 2; cur <= 2 * (deadEnd - start); cur *= 2) {
            int s = getSize > 10 && useBuffers ? start + getSize : start;
            for (; s + cur <= deadEnd; s += cur) merge(array, s, s + cur, s + cur / 2, start, getSize, useBuffers);
            if (s + cur / 2 <= deadEnd) merge(array, s, deadEnd, s + cur / 2, start, getSize, useBuffers);
        }
        if (getSize > 10 && useBuffers) giveUpMergeOneRotate(array, start, start + getSize, deadEnd, getSize, true, true, true, 0);
        if (balance > 0) giveUpMergeOneRotate(array, start, deadEnd, deadEnd + balance, getSize, false, true, true, 0);
    }

    // BLOCK SHELL SORT (w/ GATE gaps)
    public void blockShellGate(int[] array, int a, int b, int stop) {
    	if(b - a >= 3 * GATE_GAPS[0]) {
    		int n = GATE_GAPS[0];
    		while(3 * n <= b - a) n *= 3;
    		for(; n > GATE_GAPS[0]; n /= 2.3601) shellPass(array, a, b, n);
    	}
    	for(int i = 0; i < GATE_GAPS.length && GATE_GAPS[i] > stop; i++) shellPass(array, a, b, GATE_GAPS[i]);
    }


    // COOKIE COMPONENTS
    private int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }

    private int par(int[] array, int a, int b) {
        boolean[] max = new boolean[b - a];
        int maximum = stableReturn(array[a]);
        for (int i = 1; i < b - a; i++) {
            if (stableReturn(array[a + i]) > maximum) {
                maximum = stableReturn(array[a + i]);
                max[i] = true;
            }
        }
        int p = 1;
        for (int j = b - a - 1, i = b - a - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stableReturn(array[a + j]);
            while (maximum <= stableReturn(array[a + i]) && i >= p) i--;
            if (stableReturn(array[a + j]) > stableReturn(array[a + i]) && p < i - j) p = i - j;
        }
        return p;
    }

    private int pdUnstableNF(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp != 0) different = true;
            reverse++;
            if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 1, true, false);
            else Writes.reversal(array, start, reverse, 1, true, false);
        }
        return reverse;
    }

    private int pdUnstableCNF(int[] array, int start, int end) {
        int firstreverse = start;
        boolean first = true;
        int reverse = start;
        int newstart = start;
        while (reverse + 1 < end) {
            boolean different = false;
            int cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            while (cmp >= 0 && reverse + 1 < end) {
                if (cmp != 0) different = true;
                reverse++;
                if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 1, true);
            }
            if (reverse > newstart + 1 && different) {
                if (reverse < newstart + 3) Writes.swap(array, newstart, reverse, 1, true, false);
                else Writes.reversal(array, newstart, reverse, 1, true, false);
            }
            if (first) firstreverse = reverse;
            first = false;
            reverse++;
            newstart = reverse;
        }
        return firstreverse;
    }

	private int shellPassC(int[] array, int start, int end, int gap, int lastgap) {
        if (gap >= lastgap) return lastgap;
        if (gap == lastgap - 1 && gap != 1) return lastgap;
		if(end-start < gap)
			return gap;
		int[] starts = new int[gap], lens = new int[gap], ends = new int[gap];
		for(int i=0; i<gap; i++) {
			starts[i] = start+i;
			lens[i] = 0;
			ends[i] = (end-(end%gap))+i;
			if(ends[i] >= end) {
				ends[i]-=gap;
			}
		}
		boolean done;
		do {
			done = true;
			for(int i=0; i<gap; i++) {
				int v=starts[i]+lens[i]*gap;
				if(v > ends[i] || ends[i] == -1)
					continue;
				done=false;
				int r=getGappedRun(array, v, ends[i], gap);
				lazyMerge(array, starts[i], lens[i], r, gap);
				Writes.write(lens, i, lens[i]+r, 0, false, true);
			}

		} while(!done);
		return gap;
	}

    // BLOCK SHELL (w/ par func)
    private void blockShellPar(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        int pd = pdUnstableNF(array, a, b);
        if (pd + 1 < b) {
            Highlights.clearAllMarks();
            double truediv = 3;
            int lastpar = b - a;
            int lastgap = b - a;
            while (true) {
                int par = par(array, a, b);
                if (par >= lastpar) par = lastpar - (int) truediv;
                if (par / (int) truediv <= 1) {
                    shellPass(array, a, b, 1);
                    break;
                }
                lastgap = shellPassC(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), lastgap);
                if (lastpar - par <= Math.sqrt(lastpar)) truediv *= 1.5;
                lastpar = par;
            }
        }
    }

    // MILK
    protected void milkPass(int[] array, int start, int end) {
        int b = start + ((end - start) / 2);
        int lasta = start;
        int consecutive = 0;
        boolean faultout = false;
        if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            for (int a = start; a < b && !faultout; a++) {
                if (Reads.compareIndices(array, a, b, 1, true) > 0) {
                    for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), 0.5, true, false);
                    if (a - lasta < 3) {
                        consecutive++;
                        if (consecutive == 8) {
                            blockShellPar(array, a, end);
                            faultout = true;
                        }
                    }
                    lasta = a;
                } else if (a - lasta > 1) consecutive = 0;
            }
            if (!faultout) shellPass(array, b, end, 1);
        }
    }

    protected void milkNon2N(int[] array, int start, int end, int len) {
        int b = start + (len / 2);
        if (b < end) {
            if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            Highlights.clearAllMarks();
                if (end - b <= len / 8) lazyMerge(array, start, b - start, end - b, 1);
                else blockShellPar(array, start, end);
            }
        }
    }

    public void milkSortLen(int[] array, int start, int end, int lengthstart) {
        int len = lengthstart;
        int index = start;
        for (; len < end - start; len *= 2) {
            index = start;
            for (; index + len <= end; index += len) {
                if (len == 2) {if (Reads.compareIndices(array, index, index + 1, 1, true) > 0) Writes.swap(array, index, index + 1, 1, true, false);}
                else milkPass(array, index, index + len);
            }
            if (index != end) milkNon2N(array, index, end, len);
        }
        if (len == end - start) milkPass(array, start, end);
        else milkNon2N(array, start, end, len);
    }

    private void manageSize(int[] array, int start, int length, int bufferbegin) {
        if (Reads.compareIndices(array, start + length - 1, start + length, 1, true) > 0) {
            for (int i = 0; i < length; i++) Writes.swap(array, start + i, bufferbegin + i, 1, true, false);
            int left = 0;
            int right = 0;
            int balance = start;
            while (left < length && right < length) {
                if (Reads.compareIndices(array, start + length + left, bufferbegin + right, 1, true) <= 0) {
                    if (start + length + left != balance) Writes.swap(array, start + length + left, balance, 1, true, false);
                    left++;
                } else {
                    Writes.swap(array, bufferbegin + right, balance, 1, true, false);
                    right++;
                }
                balance++;
                if (left >= length) {
                    while (right < length) {
                        Writes.swap(array, bufferbegin + right, balance, 1, true, false);
                        right++;
                        balance++;
                    }
                }
            }
        }
    }

    private void cookie(int[] array, int start, int length, int pd, boolean tainted) {
        int blockLen = pow2lte((int) Math.sqrt(length));
        int endpoint = blockLen;
        while (endpoint + blockLen < length) endpoint += blockLen;
        for (int i = 0; i + blockLen <= endpoint; i += blockLen) {
        	if (start + i + blockLen > pd) {
        		if(tainted) packwatch(array, start + i, start + i + blockLen);
        		else ins(array, start + i, start + i + blockLen);
        	}
        }
        for (int i = 0; i + 2 * blockLen <= endpoint; i += 2 * blockLen) manageSize(array, start + i, blockLen, start + endpoint);
		if(tainted) packwatch(array, start + endpoint, start + length);
		else ins(array, start + endpoint, start + length);
        milkPass(array, start + endpoint - blockLen, start + length);
        milkSortLen(array, start, start + length, 4 * blockLen);
    }

    // COOKIE SORT
    public void cookieSort(int[] array, int start, int end, int depth, boolean tainted) {
        Writes.recordDepth(depth);
        if (par(array, start, end) <= (end - start) / 8) {
        	if(tainted) gnotAGnoblin(array, start, end);
        	else blockShellPar(array, start, end);
        } else if (end - start <= 32) {
        	if(tainted) packwatch(array, start, end);
        	else ins(array, start, end);
        } else {
            int pd = pdUnstableCNF(array, start, end);
            if (pd < end - 1) {
                int length = end - start;
                int effectivelen = 2;
                while (effectivelen <= length) effectivelen *= 2;
                effectivelen /= 2;
                cookie(array, start, effectivelen, pd, tainted);
                if (effectivelen != length) {
                    Writes.recursion();
                    cookieSort(array, start + effectivelen, end, depth + 1, tainted);
                    milkNon2N(array, start, end, effectivelen * 2);
                }
            }
        }
    }

    // STRANGE SORT
    public void strange(int[] array, int a, int b, int base) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int offset = a + 1; offset != b; offset++) {
                int mult = 1;
                int bound = 1;
                while (offset + mult <= b) {
                    if (Reads.compareIndices(array, (int) (offset + mult / base) - 1, (int) (offset + mult) - 1, 0.1, true) > 0) {
                        Writes.swap(array, (int) (offset + mult / base) - 1, (int) (offset + mult) - 1, 0.1, anyswaps = true, false);
                        if (mult == 1 / base) {
                            bound *= base;
                            mult = bound;
                        } else mult /= base;
                    } else {
                        bound *= base;
                        mult = bound;
                    }
                }
            }
        }
    }

    // SAFE ASS SORT COMPONENTS
    private int networksort(int[] array, int[] indexnetwork, int start, int length) {
    	int results = 0;
        for (int i = 1; i < length; i += 2) {
            Highlights.markArray(3, start + i - 1);
            Highlights.markArray(4, start + i);
            results |= pairsort(array, start + indexnetwork[i - 1], start + indexnetwork[i]);
        }
        return results;
    }

    private int pairsort(int[] array, int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        if (Reads.compareIndices(array, i, j, 0.025, true) > 0) {
            Writes.swap(array, i, j, 0.025, true, false);
            return 3;
        }
        return  0;
    }

    protected void initializeCurve(int[] array, int currentLen) {
        int floorLog2 = (int) (Math.log(currentLen) / Math.log(2));
        for (int i = 0; i < currentLen; i++) {
            int value = (int) (currentLen * curveSum(floorLog2, (double) i / currentLen));
            Writes.write(array, i, value, 0.1, true, true);
        }
    }

    protected double curveSum(int n, double x) {
        double sum = 0;
        while (n >= 0) sum += curve(n--, x);
        return sum;
    }

    protected double curve(int n, double x) {
        return triangleWave((1 << n) * x) / (1 << n);
    }

    protected double triangleWave(double x) {
        return Math.abs(x - (int) (x + 0.5));
    }

    protected void linearInvert(int[] array, int currentLen) {
        int[] tmp = new int[currentLen];
        tableinvert(array, tmp, currentLen);
        Highlights.clearAllMarks();
        Writes.arraycopy(tmp, 0, array, 0, currentLen, 0.1, true, true);
    }

    protected void siftDown(int[] array, int[] keys, int r, int len, int a, int t) {
        int j = r;
        while (2*j + 1 < len) {
            j = 2*j + 1;
            if (j+1 < len) {
                int cmp = Reads.compareOriginalIndices(array, a+keys[j+1], a+keys[j], 0, true);
                if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j+1], keys[j]) > 0)) j++;
            }
        }
        for (int cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true);
            cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0);
            j = (j-1)/2,
            cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true));
        for (int t2; j > r; j = (j-1)/2) {
            t2 = keys[j];
            Highlights.markArray(3, j);
            Writes.write(keys, j, t, 0, false, true);
            t = t2;
        }
        Highlights.markArray(3, r);
        Writes.write(keys, r, t, 0, false, true);
    }

    protected void tableSort(int[] array, int[] keys, int a, int b) {
        int len = b-a;
        for (int i = (len-1)/2; i >= 0; i--) this.siftDown(array, keys, i, len, a, keys[i]);
        for (int i = len-1; i > 0; i--) {
            int t = keys[i];
            Highlights.markArray(3, i);
            Writes.write(keys, i, keys[0], 0, false, true);
            this.siftDown(array, keys, 0, i, a, t);
        }
        Highlights.clearAllMarks();
    }

    protected void tableinvert(int[] array, int[] table, int currentLength) {
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0, false, true);
        tableSort(array, table, 0, currentLength);
    }

    protected void prepareIndexes(int[] array, int length, int pos) {
        initializeCurve(array, length);
        linearInvert(array, length);
        linearInvert(array, length);
        for(int i = 0; i < length; i++) {
        	Writes.write(array, i, array[i] + pos, 0, false, true);
        }
    }

    // SAFE ASS SORT
    public void safeAss(int[] array, int a, int b) {
    	int n = b - a;
        int indexeslen = par(array, a, b);
        int lastlen = n;
        int[] indexes = Writes.createExternalArray(indexeslen);
        boolean lenchange = true;
        int firstpos = a;
        int nextlast = b;
        int lastpos = b;
        int results = 0;
        boolean firstfound = false;
        while (lastlen > 1) {
        	results &= ~1;
            Highlights.clearAllMarks();
            if (lenchange) prepareIndexes(indexes, indexeslen, a);
            lenchange = false;
            firstfound = false;
            Highlights.clearAllMarks();
            for (int i = firstpos > a ? firstpos - 1 : a; i + indexeslen <= (lastpos + 1 < b ? lastpos + 1 : b); i++) {
                results &= ~2;
                results = networksort(array, indexes, i, indexeslen);
                if ((results & 1) > 0 && !firstfound) {
                    firstpos = i;
                    firstfound = true;
                }
                if ((results & 2) > 0) nextlast = i + indexeslen;
            }
            Highlights.clearAllMarks();
            lastpos = nextlast;
            if ((results & 1) == 0) {
                if (!isRangeSorted(array, a, b)) {
                    firstpos = a;
                    lastpos = b;
                    lastlen = indexeslen;
                    indexeslen = par(array, a, b);
                    if (indexeslen == lastlen && indexeslen > 2) indexeslen--;
                    lenchange = true;
                    Writes.deleteExternalArray(indexes);
                    indexes = Writes.createExternalArray(indexeslen);
                } else break;
            }
        }
        Highlights.clearAllMarks();
        shellPass(array, a, b, 1);
    }

   	// ~THE ALGORITHM~
    public void pcboyGamesIsYourNewGod(int[] array, int a, int b, int d) {
    	int n = b - a;

    	if (d == 0 || n < 16) {
    		// only comes into play when running out of depth.

    		// efficient depthsorts only come into play when it runs out
    		// with sufficiently large n. that's where Bismuth and Cookie
    		// get used.
    		if(n >= 1048576) {
    			bismuthSort(array, a, b, 0, true);
    		} else if(n >= 131072) {
    			cookieSort(array, a, b, 0, true);
    		} else if(n >= 16384) {
    			strange(array, a, b, 16);
    		} else if(n >= 2048) {
    			issaSort(array, a, b);
    		} else if(n >= 256) {
    			pdsBogo(array, a, b);
    		} else if(n >= 32) {
    			cospo(array, a, b);
    		} else if(n >= 4) {
    			dPop(array, a, b, n, false);
    		} else {
    			vubuPhon(array, a, b);
    		}
    		return;
    	}
    	boolean finalShell = d < 0;
    	if (finalShell) {
    		d = (log(b - a) * 2) / 5;
    	}

    	long pinfo = getPivotSnCnt(array, a, b);

    	if(pinfo == -1L) return;

    	int pmed = (int)pinfo, pleft = (int)(pinfo >> 32), pval;

    	if (n >= 524288) {
    		pval = ~jPart(array, a, pleft, b - 1, pmed, d);
    		pleft--;
    	} else if (n >= 65536) {
    		pval = -singularityDown(array, a, pleft, b, d);
    		pleft = a;
    	} else {
    		if (n >= 8192) {
        		ecoloGP(array, pleft, b, pmed);
        	} else if (n >= 1024) {
        		asteraceaeGP(array, pleft, b, pmed);
        	} else if (n >= 128) {
        		californiumGP(array, pleft, b, pmed, 0, log(n) / 2);
        	} else {
        		cloakGP(array, pleft, b, pmed);
        	}
    		pval = binSearchLD(array, pleft, b, pmed);
    	}
    	if (pval < 0) { // already recursed right
    		pcboyGamesIsYourNewGod(array, pleft, ~pval, d - 1);
    	} else {
    		if (pval - pleft <= b - pval) {
        		pcboyGamesIsYourNewGod(array, pleft, pval, d - 1);
        		pcboyGamesIsYourNewGod(array, pval, b, d - 1);
    		} else {
        		pcboyGamesIsYourNewGod(array, pval, b, d - 1);
        		pcboyGamesIsYourNewGod(array, pleft, pval, d - 1);
    		}
    	}

    	if (finalShell) {
    		powSetSort(array, a, pleft);
    		blockShellGate(array, a, b, 160);
    		safeAss(array, a, b);
    	} else {
    		calcMerge(array, a, pleft, b);
    	}
    }

    public static void pcbgiyng(int[] array, int a, int b, int d) {
    	Statistics.putStat("Pivot Comparison");
    	self.pcboyGamesIsYourNewGod(array, a, b, d);
    }

	public void runSort(int[] array, int currentLength, int bucketCount) {
		if(self != null)
			return;
    	infect();
    	self = this;
    	pcbgiyng(array, 0, currentLength, -1);
	}
}