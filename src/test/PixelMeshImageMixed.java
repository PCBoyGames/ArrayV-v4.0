package test;

import java.io.PrintWriter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

class PixelMeshImageMixed {
	public static int[][] getColorArray(BufferedImage image) {
		int width = image.getWidth(), height = image.getHeight();

		int[][] r = new int[width][height];

		for (int j = 0; j < width; j++)
			for (int i = 0; i < height; i++)
				r[j][i] = image.getRGB(j, i);

		return r;
	}

	public static int residuals(int c, int w) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = c & 0xff;

		// normalize vals
		float cr = r / 255f, cg = g / 255f, cb = b / 255f;

		// get working luminance
		float wl = ((cr > cg && cr > cb) ? cr - (cg > cb ? cb : cg) :
					(cg > cb) ? cg - (cr > cb ? cb : cr) :
								cb - (cr > cb ? cg : cr));

		// get working sum
		float wd = ((cr > cg && cr > cb) ? cr + (cg > cb ? cb : cg) :
					(cg > cb) ? cg + (cr > cb ? cb : cr) :
								cb + (cr > cb ? cg : cr));

		// saturation formula
		float s = wl >= 0.5 ? wl / (2f - wl) : wl / wd;
		int res = (int)(255 / ((1f-s) * 255 + 1));
		return ((((255 - r) * res) / 255) << 16) | ((((255 - g) * res) / 255) << 8) | (((256 - 5) * res) / 255);
	}

	public static int truncColor(int c, int w, int rs) {
		// residuals subtracted
		int r = ((c >> 16) & 0xff) - ((rs >> 16) & 0xff);
		int g = ((c >> 8) & 0xff) - ((rs >> 8) & 0xff);
		int b = (c & 0xff) - (rs & 0xff);

		// normalized vals
		float cr = Math.max(r / 255f, 0f), cg = Math.max(g / 255f, 0f), cb = Math.max(b / 255f, 0f);
		// get hue in one monolith
		float h = (cr > cg && cr > cb ? (cg - cb) / (cr - (cg > cb ? cb : cg)) : cg > cb ? 2f + (cb - cr) / (cg - (cr > cb ? cb : cr)) : 4f + (cr - cg) / (cb - (cr > cg ? cg : cr))) / 6f;
		if (h < 0) h += 1f;

		// get luminance
		double l = Math.sqrt((0.299 * cr * cr) + (0.587 * cg * cg) + (0.114 * cb * cb));

		// boundscheck black, boundscheck white, set hue and luminance
		return l < 1d / (double)w ? -1 : 1d - l < 1d / (double)w ? w * w + 1 : ((int)(h * w)) + (int)(l * w + 1) * w;
	}

	/*
	javac PixelMeshImageMixed.java
	java PixelMeshImageMixed file.jpg 0.5 max

	*/
	public static void main(String[] args) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File(args[0]));
		}
		catch(Exception e) {
			System.out.println("File not found");
			return;
		}

		int[][] img = getColorArray(image);
		int width = img.length, height = img[0].length;

		int sqrt = (args.length > 2 && args[2].equals("max")) ? Math.max(width, height) : Math.min(width, height);
		    sqrt = (int)((args.length < 2 ? 1d : Double.parseDouble(args[1])) * sqrt);
		int length = sqrt*sqrt;
		int[] array = new int[length];

		double scaleX = (double)width/sqrt, scaleY = (double)height/sqrt;

		for (int i = 0, y = 0, r = 0; y < sqrt; y++)
			for (int x = 0; x < sqrt; x++) {
				// decay residual
				r = (r & 0xFEFEFE) >> 1;
				// get residual for current pixel, append it to running res
				int cr = residuals(img[(int)(scaleX * x)][(int)(scaleY * y)], sqrt);
				r += cr;
				// get raw truncated color for bounds checking
				int stc = truncColor(img[(int)(scaleX * x)][(int)(scaleY * y)], sqrt, 0);
				// get sum of residual rgb vals
				int sr = ((cr & 0xff) + ((cr >> 8) & 0xFF) + ((cr >> 16) & 0xff));
				int dr = Math.abs((sr / 3) - (cr % 256)), dg = Math.abs((sr / 3) - ((cr >> 8) & 0xFF)), db = Math.abs((sr / 3) - ((cr >> 16) & 0xFF));
				// "too grey" check
				if (Math.min(dr, Math.min(dg, db)) < 5) {
					int hotswap = (0x0A0A05 ^ (0x0F0F0F * (x % 2))) * (sr / 192);
					if (r <= hotswap) r += hotswap;
					else r = hotswap;
				}
				// push
				array[i++] = stc < 0 ? -1 : stc >= sqrt * sqrt ? sqrt * sqrt : truncColor(img[(int)(scaleX * x)][(int)(scaleY * y)], sqrt, r);
			}

		try {
			PrintWriter writer = new PrintWriter("pixel_mesh_color-.txt", "UTF-8");
			for (int i : array)
				writer.print(i + " ");
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		System.out.printf("Set length to %d to view image in Pixel Mesh.\n", length);
	}
}