import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.lang.Thread;
import java.util.Random;
import java.lang.Runtime;

public class gra extends MIDlet implements CommandListener {

	private ruch tredruch;
	private Thread myThread;
	private winieta wwinieta;
	private rekordy rrekordy;
	public static ustawienia uustawienia;

    public static Form oprogramieForm, wpisForm, opcjeForm;

	private static TextField imiegracza,g_etap;
	private static ChoiceGroup ch_kfg;
	private boolean[] wyn_kfg;

	private Command c_wyjscie,c_nowagra,c_zakonczgre,c_oprogramie,c_rekordy,c_ok,c_opcje;
	public static Command c_zabijsie;

	private long nowyrekord;

	public gra() {
		c_zakonczgre = new Command("Zakoñcz grê", Command.SCREEN, 2);
		c_zabijsie = new Command("Samobójstwo", Command.SCREEN, 2);

		c_ok = new Command("OK", Command.OK, 2);

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
			c_nowagra = new Command("Nowa gra", Command.SCREEN, 1);
			c_oprogramie = new Command("O programie", Command.SCREEN, 2);
			c_rekordy = new Command("Rekordy", Command.SCREEN, 2);
			c_opcje = new Command("Ustawienia", Command.SCREEN, 2);
			c_wyjscie = new Command("Wyjœcie", Command.EXIT, 1);
			wwinieta = new winieta();
			wwinieta.addCommand(c_nowagra);
			wwinieta.addCommand(c_oprogramie);
			wwinieta.addCommand(c_opcje);
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
		c_opcje = null;
		c_wyjscie = null;
		wwinieta = null;
	}//void

	protected void nowagra() {
		Runtime.getRuntime().gc();
		if (tredruch == null)
		{
			tredruch = new ruch();
//			tredruch.addCommand(c_zabijsie);
			tredruch.addCommand(c_zakonczgre);
		}
		if (myThread == null)
		{
			myThread = new Thread(tredruch);
		}

		Display.getDisplay(this).setCurrent(tredruch);
		tredruch.setCommandListener(this);
		try {
		    myThread.setPriority(Thread.MAX_PRIORITY);
			myThread.start();
		    myThread.setPriority(Thread.MAX_PRIORITY);
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
        oprogramieForm = new Form("RX-800b");
        oprogramieForm.append("Autor:\n Grzegorz \"GAD\" Drozd\n\n");
        oprogramieForm.append("(c)2003 Studio Komputerowe GAD\n\n");
        oprogramieForm.append("RX-800b (w skrócie RX) to prototypowy model robota do specjalnych zadañ. Steruj¹c nim wype³niaj misje. Uwa¿aj na stwory w jaskiniach, które mog¹ uszkodziæ RX.\n\n");
        oprogramieForm.addCommand(c_ok);

		Display.getDisplay(this).setCurrent(oprogramieForm);
		oprogramieForm.setCommandListener(this);
	}

	public void pokazopcje() {
        opcjeForm = new Form("Ustawienia");
		g_etap = new TextField("Misja pocz¹tkowa:", "1", 3, TextField.NUMERIC);
		g_etap.setString("" + uustawienia.etappoc);

		opcjeForm.append(g_etap);
		String[] kfgarray = { "Wibrator" };
		ch_kfg = new ChoiceGroup("", ChoiceGroup.MULTIPLE, kfgarray, null);
		wyn_kfg = new boolean[1];
		wyn_kfg[0] = uustawienia.wibrator;
		ch_kfg.setSelectedFlags(wyn_kfg);
		opcjeForm.append(ch_kfg);
        opcjeForm.addCommand(c_ok);

		Display.getDisplay(this).setCurrent(opcjeForm);
		opcjeForm.setCommandListener(this);
	}

	public void pokazrekordy() {
		Display.getDisplay(this).setCurrent(rrekordy);
		rrekordy.setCommandListener(this);
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
		if (c == c_opcje) {
			pokazopcje();
		} else 
		if (c == c_ok) {
			if (oprogramieForm!=null) oprogramieForm=null;
			if (s==wpisForm)
			{
				rrekordy.dodajwynik(nowyrekord, imiegracza.getString());
				pokazwiniete();
				pokazrekordy();
			} else
			if (s==opcjeForm)
			{
				String ss = g_etap.getString();
				uustawienia.etappoc = Integer.parseInt(ss);
				if (uustawienia.etappoc<1) uustawienia.etappoc=1;
				if (uustawienia.etappoc>uustawienia.etapmax) uustawienia.etappoc=uustawienia.etapmax;
				g_etap.setString("" + uustawienia.etappoc);
				ch_kfg.getSelectedFlags(wyn_kfg);
				uustawienia.wibrator = wyn_kfg[0];
				uustawienia.zapiszustawienia();
				imiegracza=null;
				if (opcjeForm!=null) opcjeForm=null;
				pokazwiniete();
			} else
			{
				Display.getDisplay(this).setCurrent(wwinieta);
				wwinieta.setCommandListener(this);
			}
		} else 
		if (c == c_zabijsie) {
			tredruch.zabijgracza();
		}
		if (c == c_zakonczgre) {
			boolean kon = tredruch.przerwij();
			//g_etap.setString("" + uustawienia.etappoc);
			if (tredruch.pkt>rrekordy.r_wynik[rrekordy.iloscmiejsc-1])
			{
				nowyrekord = tredruch.pkt;
				wpisForm = new Form("Nowy rekord!");
				imiegracza = new TextField("Podaj imiê:", "", 9, TextField.ANY);
				wpisForm.append(imiegracza);
				wpisForm.addCommand(c_ok);
				Display.getDisplay(this).setCurrent(wpisForm);
				wpisForm.setCommandListener(this);
			} else {
				pokazwiniete();
				if (kon) pokazrekordy();
			}
			uustawienia.zapiszustawienia();
			tredruch = null;
			myThread = null;
			Runtime.getRuntime().gc();
		} else 
		if (c == c_wyjscie) {
			destroyApp(false);
			notifyDestroyed();
		}//if
	}//void


} //class

