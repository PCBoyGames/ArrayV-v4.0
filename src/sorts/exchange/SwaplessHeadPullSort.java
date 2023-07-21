package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

U8y3zJTNmM2cVMy2zJLMlMyvQcy4zIHNhVnMts2MzJ4gzLbNg82QzZXMq0HMuM2EzYjMolfMuM2b
zYdBzLXMj8yqWcy2zJHNjMypzKIgzLfNm8yWRsy4zIzMs8yuUsy4zIfMscyiT8y4zYrMhM2FzZVN
zLfNncyeIMy3zYPMvcyzTcy3zJvMrUXMt8yAzLEhzLXMhcymzLogzLfMjsyjScy1zYrNk8ypIMy3
zJDMnc2WQcy4zILNhsylTMy2zIrMjs2VzK9SzLfNoMyKzJ1FzLfMk8ylQcy2zInNnc2NzKZEzLfM
ic2azJ5ZzLXMicyNzKogzLfMjMySzY1XzLfMi8y6Qcy1zJTNgsyuUsy2zL/MmE7Mt8yHzY5FzLXM
k0TMtsyLzL3MmCDMtMyOzL7MsVnMtsyRzKPMrE/MuMyEzYTMoVXMt82YzZfMvCDMtcyPzIjMmVTM
tMy/zY5IzLTMg8yQzZTMskHMtcyRzIDMnsycVMy3zZDMjs2ZIMy0zIjMu0XMuM2EzIXMqMy7Vsy1
zYvMoUXMtsyKzZxOzLTNks2bzK4gzLXNoMyvTcy1zYPNhsydWcy4zJDMhs2cIMy0zYTMpcyfT8y2
zYvMoc2HV8y0zJLMvcy5Tsy1zZvNl8yWIMy1zYrMl03MtsyOzLBFzLXMm82azKlOzLjMklTMuMyB
zYZBzLjNhM2ZTMy0zInNlSDMtcyUzZjNmknMtc2LzL7Mp07MuMybzLvMu1PMtsySzJfMn0HMuM2M
zJDMu07MtcyHzIzNnMyxScy0zZ3Ml1TMuMyAzLDMqlnMtcy9zZMgzLjMgMyszJZJzLjMh8yYU8y0
zZLNms2NIMy3zaDMu0LMt8yPzLDMo0XMuMyUzZLMoc2ZWcy1zInMp0/Mts2AzZ3Mnk7MtMyOzZDM
rETMts2QzZogzLTMgcy5Tcy4zYLNlVnMt82BzL/MuyDMts2BzIvNnMylQ8y4zYLMh8yjzJlPzLjM
vsywzLJOzLjMhsyLzLLMoFTMtcy/zYXMr1LMt8yQzKdPzLjMjsy8TMy2zYHMo8ylIcy1zZfMkc2N
IMy2zYPMn8ydVMy3zL/Nlcy7SMy4zL7MiMyxQcy4zYPMn8yuVMy1zJPNiM2UJ8y0zIrMlM2ZzLFT
zLfMjc2CzJ/MqyDMtcyazKPNjUjMt8yMzKnNjU/Mt8yRzZzNh1fMuMyBzZLNhSDMt82YzJPNiM2Z
Scy3zIvNoM2cIMy2zL7MnUXMtsyOzJDMosypTsy3zYvMmMyhRMy0zIXNjc2ORcy4zZ3Mv8yWRMy1
zJXMq8ykIMy2zI7Nnc2VVcy3zL/NksyeUMy1zITMg8ysIMy2zIHMgMydzKRIzLTMvcy6Rcy0zZvN
m8y6Usy4zZjNksyxzZxFzLfMksyrIMy3zIjMvs2NScy4zZvNhMyyzKVOzLjMgcyezKEgzLfMh8yr
zLlUzLjMjc2dzLxIzLbMlM2EzJ9FzLTMisyDzKHNhSDMtcyKzLJGzLXNl8yOzZXNmUnMuMyGzLnM
nVLMt8ybzJrNmsyeU8y3zIrNlVTMuMy9zILNh8yeIMy3zYDMkcyqUMy4zL3Mqc2HTMy4zL7Nis2Z
Qcy1zYvNnMywQ8y2zYHMkM2FRcy2zJLMjSHMt8yTzZfMncywCgogLSBUaGUgTWFkaG91c2UgQ0VP

*/
final public class SwaplessHeadPullSort extends Sort {
    public SwaplessHeadPullSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Head Pull");
        this.setRunAllSortsName("Swapless Head Pull Sort");
        this.setRunSortName("Swapless Head Pull Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i + 1 <= currentLength;) {
            if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) {
                Writes.insert(array, i, 0, 0.1, true, false);
                i = 1;
            } else i++;
        }
    }
}