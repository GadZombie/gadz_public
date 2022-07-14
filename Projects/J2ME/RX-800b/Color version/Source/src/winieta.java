import java.lang.System;
import java.lang.Runnable;
import java.lang.InterruptedException;

import javax.microedition.lcdui.*;

public class winieta extends Canvas implements Runnable {

    private Image obrwinieta = null;

	public void paint(Graphics g) {
		g.setColor(0x00000000);
		g.fillRect(0,0,getWidth(),getHeight());
	    g.drawImage(obrwinieta, getWidth()/2, getHeight()/2, Graphics.VCENTER|Graphics.HCENTER);
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

