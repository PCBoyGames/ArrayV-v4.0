package visuals.dots;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ScatterChords extends Visual {
    public ScatterChords(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        int offset = 20 + (int) (Renderer.getXScale()/2);
        int dotS = (int) (Renderer.getDotDimensions());
        this.mainRender.setStroke(ArrayVisualizer.getCustomStroke(4));

        for (int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? Color.WHITE : getIntColor(array[i], Renderer.getArrayLength()));
            else if (ArrayVisualizer.colorEnabled()) {
                int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getIndexValue(array[i]): array[i];
                this.mainRender.setColor(getIntColor(val, ArrayVisualizer.getCurrentLength()));
            } else if (ArrayVisualizer.colorCoding && Highlights.hasColor(array, i)) this.mainRender.setColor(Highlights.colorAt(array, i));
            else this.mainRender.setColor(Color.WHITE);

            int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(array[i]): array[i];
            int y = (int) (((Renderer.getViewSize() - 20)) - (val + 1) * Renderer.getYScale());
            int relY = (int) (((Renderer.getViewSize() - 20)) - (i + 1) * Renderer.getYScale());

            this.mainRender.fillRect(j + offset, Renderer.getYOffset() + y, (int) (dotS * 1.5), (int) (dotS * 1.5));
            this.mainRender.drawLine(j + offset + (int) (dotS * 3 / 4), Renderer.getYOffset() + y + (int) (dotS * 3 / 4), j + offset + (int) (dotS * 3 / 4), Renderer.getYOffset() + relY + (int) (dotS * 3 / 4));

            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
            j += width;
        }

        for (int i = 0, j = 0; i < Renderer.getArrayLength(); i++) {
            if (Highlights.containsPosition(i)) {
                this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? getIntColor(array[i], Renderer.getArrayLength(), 0.25f, 1) : Color.RED);
                int val = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(array[i]): array[i];
                int y = (int) (((Renderer.getViewSize() - 20)) - (val + 1) * Renderer.getYScale());
                this.mainRender.fillRect(j + offset - (3*dotS), Renderer.getYOffset() + y - (3*dotS), 6*dotS, 6*dotS);
                this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? getIntColor(array[i], Renderer.getArrayLength()) : Color.WHITE);
                this.mainRender.fillRect(j + offset - (int)(1.5*dotS), Renderer.getYOffset() + y - (int)(1.5*dotS), 3*dotS, 3*dotS);
            }

            int width = (int) (Renderer.getXScale() * (i + 1)) - j;
            j += width;
        }
        if (ArrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, Renderer.getYOffset() + Renderer.getViewSize() - 20, ArrayVisualizer.currentWidth(), 1);
        }
    }
}