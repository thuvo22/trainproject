package train_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//Importing all OpenCV files
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import nu.pattern.OpenCV;

public class TrainProject {
	public static final String EXCEL_SRC = "C:\\Users\\Thu\\Downloads\\sample 1.xlsx";
	public static final String LOGO_SRC = "C:\\Users\\Thu\\Downloads\\result\\logo\\logo.png";
	public static final String DESTINATION_SRC = "C:\\Users\\Thu\\Downloads\\result";
	public static final String PHOTO_SRC = "C:\\Users\\Thu\\Downloads\\photos";
	public static final String FINAL_DES_SRC = "C:\\Users\\Thu\\Downloads\\trademark\\";

	public static final int COOR_Y_COMMENT_SMALL = 2800;
	public static final int COOR_X_COMMENT_SMALL = 1200;
	public static final int COOR_Y_COMMENT = 2650;
	public static final int COOR_X_COMMENT = 800;
	public static void writeOnPhoto(String des, String comment, String price, String brand, String fileName) {
		// Loading the OpenCV core library
		OpenCV.loadShared();

		// Reading the contents of the image
		// from local computer directory
		String src = "C:\\Users\\Thu\\Downloads\\photos\\" + fileName + ".jpg";

		// Creating a Mat object
		Mat image = Imgcodecs.imread(src);

		// Text to be added
		String text = fileName;
		String priceText = "Price: $" + price;
		String commentText = comment;
		// Points from where text should be added
		Point imageIDPoint = new Point(90, 240);

		Point pricePoint = new Point(3050, 240);

		Point commentPoint = new Point(1250, 2850);

		Point brandPoint = new Point(1400, 240);
		if (price.length() == 3) {
			pricePoint = new Point(2700, 240);
			brandPoint = new Point(1300, 240);
		} else if (price.length() == 2) {
			pricePoint = new Point(2950, 240);
			brandPoint = new Point(1400, 240);
		}

		// Color of the text
		Scalar color = new Scalar(0, 0, 255);

		// Fonttype of the text to be added
		int fontType = Imgproc.FONT_ITALIC;
		double fontSizeBrand = 3.8;
		// Fontsize of the text to be added
		double fontSize = 6;
		double fontCommentSize = 6;
		// Thickness of the lines in px
		int thickness = 15;
		if (commentText.length() <= 45) {
			fontCommentSize = 4.2;
			thickness = 12;
			commentPoint = new Point(COOR_X_COMMENT_SMALL, COOR_Y_COMMENT_SMALL);

		} else if (commentText.length() > 45) {
			StringBuilder sb = new StringBuilder(commentText);

			int i = 0;
			while ((i = sb.indexOf(" ", i + 45)) != -1) {
				sb.replace(i, i + 1, "\n");
			}
			commentText = sb.toString();
			System.err.println(commentText);
			fontCommentSize = 3;
			thickness = 10;
			commentPoint = new Point(COOR_X_COMMENT, COOR_Y_COMMENT);
		}
		// Adding text to the image using putText method
		// image ID
		Imgproc.putText(image, text, imageIDPoint, fontType, fontSize, color, thickness);
		// price
		Imgproc.putText(image, priceText, pricePoint, fontType, fontSize, color, thickness);
		// comment

		if (!commentText.contains("\n")) {
			Imgproc.putText(image, commentText, commentPoint, fontType, fontCommentSize, color, thickness);
		} else {
			String[] arr = commentText.split("\n");
			int line = 100;
			for (int i = 0; i < arr.length; i++) {
				Imgproc.putText(image, arr[i], commentPoint, fontType, fontCommentSize, color, thickness);
				commentPoint = new Point(COOR_X_COMMENT, COOR_Y_COMMENT + (line*(i+1)));
			}
			
		}
		// brand
		Imgproc.putText(image, brand, brandPoint, fontType, fontSizeBrand, color, thickness);
		// Displaying the Image after adding the Text
		Imgcodecs imageCodecs = new Imgcodecs();
		imageCodecs.imwrite(DESTINATION_SRC + "\\" + fileName + ".jpg", image);

		// Waiting for a key event to delay
//		HighGui.waitKey();
	}

	public static String ReadCellData(int vRow, int vColumn) {
		String value = null; // variable for storing the cell value
		Workbook wb = null; // initialize Workbook null
		try {
			// reading data from a file in the form of bytes
			FileInputStream fis = new FileInputStream(EXCEL_SRC);
			// constructs an XSSFWorkbook object, by buffering the whole stream into the
			// memory
			wb = new XSSFWorkbook(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Sheet sheet = wb.getSheetAt(0); // getting the XSSFSheet object at given index
		Row row = sheet.getRow(vRow); // returns the logical row
		if (row == null)
			return "";
		Cell cell = row.getCell(vColumn); // getting the cell representing the given column
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			value = String.valueOf(cell.getNumericCellValue()).split("\\.")[0];
			break;
		}
		// getting cell value
		return value; // returns the cell value
	}

	public static int countRow() {
		int res = 0;
		try {
			File file = new File(EXCEL_SRC); // creating a new file instance
			FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
			// creating Workbook instance that refers to .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
			Iterator<Row> itr = sheet.iterator(); // iterating over excel file
			while (itr.hasNext()) {

				Row row = itr.next();
				res++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void addTradeMark() {
		Test test = new Test();
		File watermarkImageFile = new File(LOGO_SRC);
		File folder = new File(DESTINATION_SRC);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				File destImageFile = new File(FINAL_DES_SRC + i + ".jpg");
				test.addImageWatermark(watermarkImageFile, listOfFiles[i], destImageFile);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

//      //extract data from cvs
		Map<String, Text> imgAndDescription = new HashMap<>();
		int numRow = countRow();
		// reading the value of 2nd row and 2nd column
		for (int i = 3; i <= numRow; i++) {
			String imageNum = ReadCellData(i, 26);
			String comment = ReadCellData(i, 14);
			String description = ReadCellData(i, 13);
			String price = ReadCellData(i, 20);
			String brand = ReadCellData(i, 12);
			Text text = new Text(description, comment, price, brand);
			imgAndDescription.put(imageNum, text);

		}

		// get all file names in a folder
		File folder = new File(PHOTO_SRC);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fullName = listOfFiles[i].getName();
				String fileNameWithOutExt = FilenameUtils.removeExtension(fullName);
				if (imgAndDescription.containsKey(fileNameWithOutExt)) {
					writeOnPhoto(imgAndDescription.get(fileNameWithOutExt).getDescription(),
							imgAndDescription.get(fileNameWithOutExt).getComment(),
							imgAndDescription.get(fileNameWithOutExt).getPrice(),
							imgAndDescription.get(fileNameWithOutExt).getBrand(), fileNameWithOutExt);
				}
				System.err.println("fullName: " + fileNameWithOutExt);

			}
		}
		addTradeMark();
	}
}
