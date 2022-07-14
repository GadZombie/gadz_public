import java.lang.System;
import java.lang.Runnable;
import java.lang.InterruptedException;

import javax.microedition.lcdui.*;

public class winieta extends Canvas implements Runnable {

    private Image obrwinieta = null;

	public void paint(Graphics g) {
	    g.drawImage(obrwinieta, 0, 0, Graphics.TOP|Graphics.LEFT);
	}

	protected winieta() {
		try {
			obrwinieta = Image.createImage("/winieta.png");
		}
		catch (java.io.IOException e)	{	}
	}

	public void run() {
		repaint();
	}

}

