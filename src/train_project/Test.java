package train_project;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//from  ww  w .j a v a  2  s  .c  o  m
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

public class Test {
	static void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {

		try {
			BufferedImage sourceImage = ImageIO.read(sourceImageFile);
			BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

			// initializes necessary graphic properties
			Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
			g2d.setComposite(alphaChannel);

			// calculates the coordinate where the image is painted
			int topLeftX = 0;
			int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight());

			// paints the image watermark
			g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

			ImageIO.write(sourceImage, "png", destImageFile);
			g2d.dispose();

			System.out.println("The image watermark is added to the image.");

		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

	public static void main(String[] args) throws IOException {
		File watermarkImageFile = new File("C:\\Users\\Thu\\Downloads\\result\\logo.png");
		File folder = new File("C:\\Users\\Thu\\Downloads\\result");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()) {
				File destImageFile = new File("C:\\Users\\Thu\\Downloads\\result\\"+i+".jpg");
				addImageWatermark(watermarkImageFile, listOfFiles[i], destImageFile);
			}
		}
		
	}

}
