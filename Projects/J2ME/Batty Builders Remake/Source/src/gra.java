import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.lang.Thread;
import java.util.Random;

public class gra extends MIDlet implements CommandListener {

	private ruch tredruch;
	private Thread myThread;
	private winieta wwinieta;
	private rekordy rrekordy;

    public static Form oprogramieForm, wpisForm;
	private static TextField imiegracza;

	private Command c_wyjscie,c_nowagra,c_zakonczgre,c_oprogramie,c_rekordy,c_ok,c_ok2;

	private long nowyrekord;

	public gra() {
		c_nowagra = new Command("Nowa gra", Command.SCREEN, 1);
		c_oprogramie = new Command("O programie", Command.SCREEN, 2);
		c_rekordy = new Command("Rekordy", Command.SCREEN, 2);
		c_wyjscie = new Command("Wyjœcie", Command.SCREEN, 3);

		c_zakonczgre = new Command("Zakoñcz grê", Command.SCREEN, 2);

		c_ok = new Command("OK", Command.SCREEN, 2);
		c_ok2 = new Command("OK", Command.SCREEN, 2);

        oprogramieForm = new Form("Batty Builders");
        oprogramieForm.append("Autor:\n Grzegorz \"GAD\" Drozd\n\n");
        oprogramieForm.append("(c)2003 Studio Komputerowe GAD\n\n");
        oprogramieForm.append("Na podstawie gry \"Batty Builders\", napisanej przez M.D.Caballero (c) English Software Company 1983 (ATARI)\n\n");
        oprogramieForm.append("Uk³adaj mur z cegie³, poruszaj¹c siê budowniczym. £ap ceg³ê klawiszem FIRE i rzucaj w górê w odpowiednie miejsce.\n");
        oprogramieForm.addCommand(c_ok);

		rrekordy = new rekordy();
        rrekordy.addCommand(c_ok);

        wpisForm = new Form("Nowy rekord!");
		imiegracza = new TextField("Podaj imiê:", "", 9, TextField.ANY);
		wpisForm.append(imiegracza);
	}

    public static Random random = new Random();

	protected void startApp() {
		pokazwiniete();
	}

	protected void pokazwiniete() {
		if (wwinieta == null)
		{
			wwinieta = new winieta();
			wwinieta.addCommand(c_nowagra);
			wwinieta.addCommand(c_oprogramie);
			wwinieta.addCommand(c_rekordy);
			wwinieta.addCommand(c_wyjscie);
		}
		Display.getDisplay(this).setCurrent(wwinieta);
		wwinieta.setCommandListener(this);
	}

	protected void nowagra() {
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
		Display.getDisplay(this).setCurrent(oprogramieForm);
		oprogramieForm.setCommandListener(this);
	}

	public void pokazrekordy() {
		Display.getDisplay(this).setCurrent(rrekordy);
		rrekordy.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable s) {
		if (c == c_nowagra) {
			wwinieta = null;
			nowagra();
		} else 
		if (c == c_oprogramie) {
			pokazoprogramie();
		} else 
		if (c == c_rekordy) {
			pokazrekordy();
		} else 
		if (c == c_ok) {
			Display.getDisplay(this).setCurrent(wwinieta);
			wwinieta.setCommandListener(this);
			rrekordy.ostatniwynik=-1;
		} else 
		if (c == c_ok2) {
	        wpisForm.removeCommand(c_ok2);
			rrekordy.dodajwynik(nowyrekord, imiegracza.getString());
			pokazwiniete();
			pokazrekordy();
		} else 
		if (c == c_zakonczgre) {
			boolean kon = tredruch.przerwij();
			if (tredruch.pkt>rrekordy.r_wynik[rrekordy.iloscmiejsc-1])
			{
				nowyrekord = tredruch.pkt;
		        wpisForm.addCommand(c_ok2);
				Display.getDisplay(this).setCurrent(wpisForm);
				wpisForm.setCommandListener(this);
			} else {
				pokazwiniete();
				if (kon) pokazrekordy();
			}
			tredruch = null;
			myThread = null;
		} else 
		if (c == c_wyjscie) {
			destroyApp(false);
			notifyDestroyed();
		}//if
	}//void


} //class

