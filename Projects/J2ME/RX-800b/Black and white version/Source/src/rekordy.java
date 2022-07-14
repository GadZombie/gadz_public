import java.lang.Runnable;
import javax.microedition.rms.*;
import java.io.*;
import javax.microedition.lcdui.*;

public class rekordy extends Canvas implements Runnable {
    static final byte iloscmiejsc = 20;
    public static long[] r_wynik = new long[iloscmiejsc];
    public static String[] r_imie = new String[iloscmiejsc];

	public static Font czcionkamala = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	public static Font czcionkanaglowek = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

	private int odmiejsca = 0;
	public int ostatniwynik=-1;
	private byte ilelinii;

    rekordy() {
		for (int a=0; a<=iloscmiejsc-1; a++)
		{
			r_wynik[a] = 20000 - a*970;
			r_imie[a] = "RX-800b";
		}

		ilelinii =(byte) ((getHeight()-14) / 10);

		wczytajrekordy();
    }

	public void wczytajrekordy() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("rx800bwyniki", true);
			if (recordStore.getNumRecords() > 0) {
					byte[] record = recordStore.getRecord(1);
					DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record, 0, record.length));
				for (int i = 0; i < iloscmiejsc; i++) {
					r_wynik[i] = istream.readLong();
					r_imie[i] = istream.readUTF();
				}
			} else {
                    ByteArrayOutputStream bstream = new ByteArrayOutputStream(17*iloscmiejsc);
                    DataOutputStream ostream = new DataOutputStream(bstream);
				for (int i = 0; i < iloscmiejsc; i++) {
                    ostream.writeLong(r_wynik[i]);
                    ostream.writeUTF(r_imie[i]);
				}
                    ostream.flush();
                    ostream.close();
                    byte[] record = bstream.toByteArray();
                    recordStore.addRecord(record, 0, record.length);
            }
        } catch(Exception e) {
        } finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                } catch(Exception e) {
                }
            }
        }

	}

	public void zapiszrekordy() {
                RecordStore recordStore = null;
                try {
                    recordStore = RecordStore.openRecordStore("rx800bwyniki", true);
                        ByteArrayOutputStream bstream = new ByteArrayOutputStream(17*iloscmiejsc);
                        DataOutputStream ostream = new DataOutputStream(bstream);
                    for (int j = 0; j < iloscmiejsc; j++) {
                        ostream.writeLong(r_wynik[j]);
                        ostream.writeUTF(r_imie[j]);
					}
                        ostream.flush();
                        ostream.close();
                        byte[] record = bstream.toByteArray();
                        recordStore.setRecord(1, record, 0, record.length);
                } catch(Exception e) {
                } finally {
                    if (recordStore != null) {
                        try {
                            recordStore.closeRecordStore();
                        } catch(Exception e) {
                        }
                    }
                }

	}

	public void dodajwynik(long pkt, String im) {
		String s;
		long i;
		boolean ok;

		r_wynik[iloscmiejsc-1] = pkt;
		r_imie[iloscmiejsc-1] = im;

		do {
			ok = true;
			for (int a=0; a<=iloscmiejsc-2; a++)
			{
				if (r_wynik[a]<r_wynik[a+1])
				{
					s = r_imie[a];
					r_imie[a] = r_imie[a+1];
					r_imie[a+1] = s;
					i = r_wynik[a];
					r_wynik[a] = r_wynik[a+1];
					r_wynik[a+1] = i;
					ok=false;
				}//if
			}//for
		} while (!ok);

		for (int a=0; a<=iloscmiejsc-1; a++)
			if ((r_wynik[a]==pkt) && (r_imie[a]==im))
			{
				ostatniwynik=a;
				odmiejsca = a-3;
				if (odmiejsca<0) odmiejsca=0;
				if (odmiejsca>iloscmiejsc-ilelinii) odmiejsca=iloscmiejsc-ilelinii;
				break;
			}

		zapiszrekordy();
	}

	public void paint(Graphics g) {
		g.setColor(0x00000000);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(0x00FFFFFF);
		g.setFont(czcionkanaglowek);
		g.drawString("LISTA NAJLEPSZYCH", 50,1,Graphics.TOP|Graphics.HCENTER);

		g.setFont(czcionkamala);
		for (int a=0; a<ilelinii; a++)
		{
			if (a+odmiejsca==ostatniwynik)
			{
				g.setColor(0x00FFFFFF);
				g.fillRect(0,11+a*10,101,11);
				g.setColor(0x00000000);
			} else g.setColor(0x00FFFFFF);
			g.drawString("" + (a+1+odmiejsca) + "." + r_imie[a+odmiejsca] , 0, 12+a*10,Graphics.TOP|Graphics.LEFT);
			g.drawString("" + r_wynik[a+odmiejsca]+"$", getWidth(), 12+a*10,Graphics.TOP|Graphics.RIGHT);
		}

	}

   protected void keyPressed(int keyCode)
   {
		int gameAction = getGameAction(keyCode);

		switch (keyCode)
		{
		case KEY_NUM2:
			if (odmiejsca>0) odmiejsca--;
			repaint();
		break;
		case KEY_NUM8:
			if (odmiejsca<iloscmiejsc-ilelinii) odmiejsca++;
			repaint();
		break;
		}

		switch (gameAction)
		{
		case UP:
			if (odmiejsca>0) odmiejsca--;
			repaint();
		break;
		case DOWN:
			if (odmiejsca<iloscmiejsc-ilelinii) odmiejsca++;
			repaint();
		break;
		default:
			return;
		}
      
   }

	public void run() {
		repaint();
	}


}//class
