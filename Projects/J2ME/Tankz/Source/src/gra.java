import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.lang.Thread;
import java.util.Random;

public class gra extends MIDlet implements CommandListener {

	private ruch tredruch;
	private Thread myThread;
	private winieta wwinieta;
	private rekordy rrekordy;
	public static ustawienia uustawienia;

    public static Form oprogramieForm, wpisForm;

	private static TextField imiegracza;
	private static Gauge g_etap;
	private static ChoiceGroup ch_rodzkloc;
	private boolean[] wyn_rodzkloc;

	private int wpisgracza;

	public static Command c_wyjscie,c_nowagra,c_zakonczgre,c_oprogramie,c_rekordy,c_ok;

	private long nowyrekord;

	public gra() {
		c_zakonczgre = new Command("Zakoñcz grê", Command.SCREEN, 2);
		c_ok = new Command("OK", Command.SCREEN, 2);

		rrekordy = new rekordy();
        rrekordy.addCommand(c_ok);

		uustawienia = new ustawienia();

	}

    public static Random random = new Random();

	protected void startApp() {
		pokazwiniete();
	}

	protected void pokazwiniete() {
		if (wwinieta == null)
		{
			wwinieta = new winieta();
			c_nowagra = new Command("Nowa gra", Command.SCREEN, 1);
			c_oprogramie = new Command("O programie", Command.SCREEN, 2);
			c_rekordy = new Command("Rekordy", Command.SCREEN, 2);
			c_wyjscie = new Command("Wyjœcie", Command.SCREEN, 3);
			wwinieta.addCommand(c_nowagra);
			wwinieta.addCommand(c_oprogramie);
			wwinieta.addCommand(c_rekordy);
			wwinieta.addCommand(c_wyjscie);
		}
		Display.getDisplay(this).setCurrent(wwinieta);
		wwinieta.setCommandListener(this);
	}

	protected void schowajwiniete() {
		c_nowagra = null;
		c_oprogramie = null;
		c_rekordy = null;
		c_wyjscie = null;
		wwinieta = null;
	}//void

	protected void nowagra() {
		Runtime.getRuntime().gc();
		if (tredruch == null)
		{
			tredruch = new ruch();
			tredruch.addCommand(c_zakonczgre);
		}
		if (myThread == null)
		{
			myThread = new Thread(tredruch);
		}

		Display.getDisplay(this).setCurrent(tredruch);
		tredruch.setCommandListener(this);
		try {
			myThread.start();
		} catch (Error e) {
			destroyApp(false);
			notifyDestroyed();
		}//catch
	}

	public void pauseApp() { }

	public void destroyApp(boolean unconditional) {
		Display.getDisplay(this).setCurrent(null);
	}

	public void pokazoprogramie() {
		if (oprogramieForm == null)
		{
	        oprogramieForm = new Form("TANKZ");
	        oprogramieForm.append("Autor:\n Grzegorz \"GAD\" Drozd\n\n");
	        oprogramieForm.append("(c)2003 Studio Komputerowe GAD\n\n");
	        oprogramieForm.addCommand(c_ok);
		}
		Display.getDisplay(this).setCurrent(oprogramieForm);
		oprogramieForm.setCommandListener(this);
	}

	public void pokazrekordy() {
		Display.getDisplay(this).setCurrent(rrekordy);
		rrekordy.setCommandListener(this);
	}

	boolean kon;

	public void pzakonczgre() {
		kon = tredruch.przerwij();
		wpisgracza=-1;
		if (wpisForm==null)
		{
	        wpisForm = new Form("Rekord!Podaj imiê");
			imiegracza = new TextField("", "", 9, TextField.ANY);
			wpisForm.append(imiegracza);
		}
		kolejnywpis();
	}

	public void kolejnywpis() {
		wpisgracza++;
		if ((wpisgracza<tredruch.ilgracz) && (uustawienia.graczrodz[wpisgracza]==0) &&
			(tredruch.graczkasasuma[wpisgracza]>rrekordy.r_wynik[rrekordy.iloscmiejsc-1]))
			{
				nowyrekord = tredruch.graczkasasuma[wpisgracza];
				imiegracza.setString("");
				imiegracza.setLabel("Gracz "+(wpisgracza+1)+":");
		        wpisForm.addCommand(c_ok);
				Display.getDisplay(this).setCurrent(wpisForm);
				wpisForm.setCommandListener(this);
			} else {
				imiegracza=null;
				wpisForm=null;
				pokazwiniete();
				if (kon) pokazrekordy();
				tredruch = null;
				myThread = null;
			}
	}

	public void commandAction(Command c, Displayable s) {
		if (c == c_nowagra) {
			schowajwiniete();
			nowagra();
		} else 
		if (c == c_oprogramie) {
			pokazoprogramie();
		} else 
		if (c == c_rekordy) {
			pokazrekordy();
		} else 
		if (c == c_ok) {
			if (s==wpisForm)
			{
		        wpisForm.removeCommand(c_ok);
				rrekordy.dodajwynik(nowyrekord, imiegracza.getString());
				kolejnywpis();
			} else {
		        oprogramieForm = null;
				Display.getDisplay(this).setCurrent(wwinieta);
				wwinieta.setCommandListener(this);
				rrekordy.ostatniwynik=-1;
			}
		} else 
		if (c == c_zakonczgre) {
			pzakonczgre();
		} else 
		if (c == c_wyjscie) {
			destroyApp(false);
			notifyDestroyed();
		}//if
	}//void


} //class

