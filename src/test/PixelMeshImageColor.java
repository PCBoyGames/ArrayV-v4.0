package test;

import java.io.PrintWriter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

class PixelMeshImageColor {

	public static int[][] getColorArray(BufferedImage image) {
		int width = image.getWidth(), height = image.getHeight();

		int[][] r = new int[width][height];

		for (int j = 0; j < width; j++)
			for (int i = 0; i < height; i++)
				r[j][i] = image.getRGB(j, i);

		return r;
	}

	public static int truncateColor(int c, int cbrt) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = c & 0xff;

		return (int)((r/255d)*(cbrt-1)+0.5)*cbrt*cbrt + (int)((g/255d)*(cbrt-1)+0.5)*cbrt + (int)((b/255d)*(cbrt-1)+0.5);
	}

	/*
	javac PixelMeshImageColor.java
	java PixelMeshImageColor file.jpg 0.5 max

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
		int length = sqrt*sqrt, cbrt = (int)Math.cbrt(length);
		int[] array = new int[length];

		double scaleX = (double)width/sqrt, scaleY = (double)height/sqrt;

		for (int i = 0, y = 0; y < sqrt; y++)
			for (int x = 0; x < sqrt; x++)
				array[i++] = truncateColor(img[(int)(scaleX * x)][(int)(scaleY * y)], cbrt);

		try {
			PrintWriter writer = new PrintWriter("pixel_mesh_color-"+args[0]+".txt", "UTF-8");
			for (int i : array)
				writer.print(i + " ");
			writer.close();
		}
		catch(Exception e) {}

		System.out.printf("Set length to %d to view image in Pixel Mesh.\n", length);
	}
}