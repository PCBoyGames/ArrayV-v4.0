package visuals.bars;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

public class BarGraph extends Visual {

    public BarGraph(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        for (int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? Color.WHITE : getIntColor(array[i], Renderer.getArrayLength()));

            else if (ArrayVisualizer.colorEnabled()) {
                int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getIndexValue(array[i]): array[i];
                this.mainRender.setColor(getIntColor(val, ArrayVisualizer.getCurrentLength()));
            }
            else this.mainRender.setColor(Color.WHITE);

            int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(array[i]): array[i];
            int y = (int) (((Renderer.getViewSize() - 20)) - (val + 1) * Renderer.getYScale());

            this.mainRender.fillRect(j + 20, Renderer.getYOffset() + y, width, (int) ((val + 1) * Renderer.getYScale()));
            j += width;
        }
        int length = Math.min(Renderer.getArrayLength(), ArrayVisualizer.getCurrentLength());

        boolean mark = false;
        for (int i = 0, j = 0; i < length; i++) {
            this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? getIntColor(array[i], length, 0.25f, 1) : ArrayVisualizer.getHighlightColor());
            mark = mark || Highlights.containsPosition(i);

            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (mark) {
                int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(array[i]): array[i];
                int y = (int) (((Renderer.getViewSize() - 20)) - (val + 1) * Renderer.getYScale());

                this.mainRender.fillRect(j + 20, Renderer.getYOffset() + y, Math.max(width, 2), (int) ((val + 1) * Renderer.getYScale()));
                mark = false;
            }
            j += width;
        }
        if (ArrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
        }
    }
}