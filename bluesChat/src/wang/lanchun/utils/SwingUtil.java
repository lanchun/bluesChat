package wang.lanchun.utils;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingUtil {
	public static void run(final JFrame f,final int width,final int height){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				f.setSize(width, height);
				f.setVisible(true);
			}
			
		});
	}

}
