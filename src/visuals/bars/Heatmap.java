package visuals.bars;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

public class Heatmap extends Visual {

    public Heatmap(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		int n = Renderer.getArrayLength();

        for (int i = 0, j = 0, k = 0; i < n; i++) {
			int width = (int) (Renderer.getXScale() * (i + 1)) - j;

			if (width == 0) continue;

			this.mainRender.setColor(getHeatColor(ArrayVisualizer.getHeatmap()[i]));

			int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(array[i]): array[i];
			int y = (int) (((Renderer.getViewSize() - 20)) - (val + 1) * Renderer.getYScale());

			this.mainRender.fillRect(j + 20, Renderer.getYOffset() + y, width, (int) ((val + 1) * Renderer.getYScale()));
			j += width;

			ArrayVisualizer.hmCool(i);
        }
		if (ArrayVisualizer.externalArraysEnabled()) {
			this.mainRender.setColor(Color.BLUE);
			this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
		}
    }
}