package wang.lanchun.utils;
import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;
/**
 * 图片压缩处理
 * @author 崔素强
 */
public class ImgCompress {
	private Image img;
	private int width;
	private int height;
	public static final int WIDTH = 200;
	public static final int HEIGHT = 200;
	
	public static void main(String[] args){
		ImgCompress imgCom = new ImgCompress("D:\\temp\\1.jpg");
		imgCom.resizeFix(WIDTH, HEIGHT);
	}
	
	/**
	 * 构造函数
	 */
	public ImgCompress(String fileName){
		System.out.println(fileName);
		File file = new File(fileName);// 读入文件
		System.out.println("sdf"+file.getAbsolutePath());
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}      
		// 构造Image对象
		width = img.getWidth(null);    // 得到源图宽
		height = img.getHeight(null);  // 得到源图长
	}
	/**
	 * 按照宽度还是高度进行压缩
	 * @param w int 最大宽度
	 * @param h int 最大高度
	 */
	public String resizeFix(int w, int h) {
		String descFileName;
		if (width / height > w / h) {
			descFileName = resizeByWidth(w);
		} else {
			descFileName = resizeByHeight(h);
		}
		return descFileName;
	}
	/**
	 * 以宽度为基准，等比例放缩图片
	 * @param w int 新宽度
	 */
	public String resizeByWidth(int w) {
		int h = (int) (height * w / width);
		return resize(w, h);
	}
	/**
	 * 以高度为基准，等比例缩放图片
	 * @param h int 新高度
	 */
	public String resizeByHeight(int h) {
		int w = (int) (width * h / height);
		return resize(w, h);
	}
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 */
	public String resize(int w, int h){
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB ); 
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		File destFile = new File("D:\\temp\\"+UUID.randomUUID()+".jpg");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		// 输出到文件流
		// 可以正常实现bmp、png、gif转jpg
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		try {
			encoder.encode(image);
			out.close();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		// JPEG编码
		return destFile.getAbsolutePath();
	}
}