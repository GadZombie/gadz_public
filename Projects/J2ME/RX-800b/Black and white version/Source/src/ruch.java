import java.lang.System;
import java.lang.Runnable;
import java.lang.InterruptedException;
import java.lang.String;
import java.lang.Character;
import javax.microedition.lcdui.*;
import java.io.InputStream;
import java.lang.Runtime;

/* import com.siemens.mp.game.Vibrator; */

public class ruch extends Canvas implements Runnable {

	public boolean finito;

    private Image ekrangry = null;
    private int   height;
    private int   width;
        
	private boolean dziala, przerysuj, skonczonerysowanie, moznawyjsc, poprawwyjscia;
	private int copokazuje,czasdokoncapokazywania;

 	private final Image[]    obr,obrcyfr,obrniesm,obrgora;
	private Image misjaobr;
	public static Font czcionkamala = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	public static Font czcionkamalagruba = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
	public static Font czcionkagruba = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

	public int		graczx,graczy,
						graczani,graczobr,
						aniobrazkow,
						niesmiertelnosc,
						klaw,klawpam;

	public int		ekranx,ekrany,
					ekranox,ekranoy,
						rozmx,rozmy;

	public long		pkt,spkt;
	public int		zycia,
						misja,misjajeszcze,smisjajeszcze,
						etap;

	private boolean	koniecgry,
							graczzyje,
							zmienetap;

	private boolean[] dzwiek;

	private boolean jestcmdsamoboj=false; //stwierdza, czy na ekranie jest komenda samobojstwa

	ustawienia uustawienia = gra.uustawienia;

	private int[][] mapa,mapacien;
	private static int[] dookolax = { 1, 0,-1, 0,-1, 1, 1,-1 };
	private static int[] dookolay = { 0, 1, 0,-1,-1, 1,-1, 1 };
	private static int[] numerobrazka = {
													0,		//0 - puste
													1,		//1 - ziemia
													2,		//2 - murek
													3,		//3 - gracz
													4,		//4 - kamien
													44,		//5 - rubin
													6,		//6 - bomba
													7,		//7 - bomba wybuchajaca
													8,		//8 - bum1 
													9,		//9 - bum2
													10,	//10- bum3
													4,		//11- kamien spada
													44,		//12- rubin spada
													6,		//13- bomba spada
													20,	//14- stal
													21,	//15- stworek z oczami, idzie w prawo
													21,	//16- stworek z oczami, idzie w dol
													23,	//17- stworek z oczami, idzie w lewo
													23,	//18- stworek z oczami, idzie w gore
													25,	//19- wylegarnia stworka z oczami
													26,	//20- stworek oko
													28,	//21- buteleczka z niesmiertelnoscia
													29,	//22- wyjscie
													31,	//23- kolczatka pozioma w prawo
													31,	//24- kolczatka pozioma w lewo
													33,	//25- kolczatka pionowa w gore
													33,	//26- kolczatka pionowa w dol
													35,	//27- maszyna przerabiajaca stwory na rubiny
													28,	//28- buteleczka z niesmiertelnoscia spada
													38,	//29- ciagnik w prawo
													39 	//30- ciagnik w lewo
														};


   protected void keyPressed(int keyCode)
   {
		int gameAction = getGameAction(keyCode);
	
		switch (keyCode)
		{
		case KEY_NUM2:
			klaw=1;
		break;
		case KEY_NUM8:
			klaw=2;
		break;
		case KEY_NUM4:
			klaw=3;
		break;
		case KEY_NUM6:
			klaw=4;
		break;
		case KEY_NUM5:
			klaw=5;
		break;
		default:
			//klaw=99;
		break;
		}

		switch (gameAction)
		{
		case UP:
			klaw=1;
		break;
		case DOWN:
			klaw=2;
		break;
		case LEFT:
			klaw=3;
		break;
		case RIGHT:
			klaw=4;
		break;
		case FIRE:
			klaw=5;
		break;
		default:
			//klaw=99;
		break;
		}

		klawpam=klaw;      
   }

	protected void keyReleased(int keyCode)
	{
		int gameAction = getGameAction(keyCode);
   
		switch (gameAction)
		{
		case UP:
		case DOWN:
		case LEFT:
		case RIGHT:
		case FIRE:
			klaw = 0;
		}

		switch (keyCode)
		{
		case KEY_NUM2:
		case KEY_NUM4:
		case KEY_NUM6:
		case KEY_NUM8:
		case KEY_NUM5:
			klaw = 0;
		}

		//if (klaw==99) klaw=0;

	}

	public void zbierzrubin()
	{
		if ((misja==1) && (misjajeszcze<=0)) pkt+=15;
		pkt+=15;
		if (misja==1) misjajeszcze--;
	}

	public void zbierzniesm()
	{
		pkt+=5;
		niesmiertelnosc+=50;
	}

	void sprawdznacowlazl()
	{
		if (mapa[graczy][graczx]==5) zbierzrubin(); else
		if (mapa[graczy][graczx]==21) zbierzniesm(); else
		if (mapa[graczy][graczx]==22) 
		{
			zmienetap=true;
			czasdokoncapokazywania=1;
			pkt+=1000;
		}
	}

   public void klawisze()
   {
	   int bb=0;
	   if ((klaw==0) && (klawpam!=0))
	   {
		   klaw=klawpam;
		   klawpam=0;
		   bb=1;
	   } else if (klaw!=0)
	   {
		   klawpam=0;
	   }
      switch (klaw)
      {
		case 1://gora
		  if ((mapa[graczy-1][graczx]==0) || (mapa[graczy-1][graczx]==1) || (mapa[graczy-1][graczx]==5) || (mapa[graczy-1][graczx]==21) || ((moznawyjsc) && (mapa[graczy-1][graczx]==22)))
			graczy--;
		    sprawdznacowlazl();
			graczani++; if (graczani>=2) graczani=0;
			graczobr = 12;
		break;
		case 2://dol
		  if ((mapa[graczy+1][graczx]==0) || (mapa[graczy+1][graczx]==1) || (mapa[graczy+1][graczx]==5) || (mapa[graczy+1][graczx]==21) || ((moznawyjsc) && (mapa[graczy+1][graczx]==22)))
			graczy++;
		    sprawdznacowlazl();
			graczani++; if (graczani>=2) graczani=0;
			graczobr = 14;
		break;
		case 3://lewo
		  if ((mapa[graczy][graczx-1]!=0) && (mapa[graczy][graczx-1]!=128) && (mapa[graczy][graczx-1]!=5) && (mapa[graczy][graczx-1]!=1)) graczobr = 42;
			else graczobr = 18;

		  if ((mapa[graczy][graczx-1]==0) || (mapa[graczy][graczx-1]==1) || (mapa[graczy][graczx-1]==5) || (mapa[graczy][graczx-1]==21) || ((moznawyjsc) && (mapa[graczy][graczx-1]==22)))
		  {	
		    graczx--;
		    sprawdznacowlazl();
		  }
		  else
		  if (((mapa[graczy][graczx-1]==4) || (mapa[graczy][graczx-1]==6)) && (mapa[graczy+1][graczx-1]!=0) && (graczx>=2) && (mapa[graczy][graczx-2]==0))
		  {
			mapa[graczy][graczx-2]=mapa[graczy][graczx-1]+128;
			graczx--;
		  }
			graczani++; if (graczani>=2) graczani=0;
		break;
		case 4://prawo
		  if ((mapa[graczy][graczx+1]!=0) && (mapa[graczy][graczx+1]!=128) && (mapa[graczy][graczx+1]!=5) && (mapa[graczy][graczx+1]!=1)) graczobr = 40;
			  else graczobr = 16;

		  if ((mapa[graczy][graczx+1]==0) || (mapa[graczy][graczx+1]==1) || (mapa[graczy][graczx+1]==5) || (mapa[graczy][graczx+1]==21) || ((moznawyjsc) && (mapa[graczy][graczx+1]==22)))
		  {
			graczx++;
		    sprawdznacowlazl();
		  }
		  else
		  if (((mapa[graczy][graczx+1]==4) || (mapa[graczy][graczx+1]==6)) && (mapa[graczy+1][graczx+1]!=0) && (graczx<=rozmx-3) && (mapa[graczy][graczx+2]==0))
		  {
			mapa[graczy][graczx+2]=mapa[graczy][graczx+1]+128;
			graczx++;
		  } 
			graczani++; if (graczani>=2) graczani=0;
		break;
      }
	  if (bb==1)
	  {
		  klaw=0;
	  }

   }

    public void czyscekran(Graphics g) {
		g.setColor(0x00FFFFFF);
	    g.fillRect(0, 0, getWidth(), getHeight());
	}

    public void pisz(Graphics g, String tekst, int xx, int yy) {
		for (int a=0; a<=tekst.length()-1; a++)
			if (((int) tekst.charAt(a) -48>=0) && ((int) tekst.charAt(a) -48<=9))
				g.drawImage(obrcyfr[ (int) tekst.charAt(a) -48], xx+a*5, yy, Graphics.TOP|Graphics.LEFT);
	}

    public void piszp(Graphics g, String tekst, int xx, int yy) {
		pisz(g,tekst,xx-tekst.length()*5,yy);
	}

    public String l2t(long l, int dl) {
		String tekst;
		tekst = String.valueOf(l);
		if (dl>0)
		{
			while (tekst.length()<dl)
			{
				tekst = "0" + tekst;
			}
		}
		return tekst;
	}
	
	Graphics ekrangraf;

    public void rysujmape() {
		ekrangraf = ekrangry.getGraphics();
		ekrangraf.setColor(0x00000000);
		ekrangraf.drawRect(0,0,(rozmx-2)*12+3,(rozmy-2)*12+3);
		ekrangraf.setColor(0x00FFFFFF);
		ekrangraf.drawRect(1,1,(rozmx-2)*12+1,(rozmy-2)*12+1);
		for (int y=0; y<=rozmy-3; y++)
			for (int x=0; x<=rozmx-3; x++)
				if (mapa[y+1][x+1]==4)
				ekrangraf.drawImage(obr[numerobrazka[mapa[y+1][x+1]]+1], 2+x*12,2+y*12, Graphics.TOP|Graphics.LEFT);
			else
				ekrangraf.drawImage(obr[numerobrazka[mapa[y+1][x+1]]], 2+x*12,2+y*12, Graphics.TOP|Graphics.LEFT);
	}

    public void rysujnamapie(int co, int x, int y) {
		int co2;
		if (co>=128) co-=128;
		ekrangraf = ekrangry.getGraphics();
		if (((co>=15) && (co<=18)) || (co==20) || ((co>=23) && (co<=26)))
		{
			co2=numerobrazka[co]+aniobrazkow;
		} else
		if ((co==4) || (co==11))
		{
			co2=numerobrazka[co]+Math.abs(gra.random.nextInt() % 2);
		} else
			co2=numerobrazka[co];
		ekrangraf.drawImage(obr[co2], x*12-10,y*12-10, Graphics.TOP|Graphics.LEFT);
	}

    public void rysujnamapieobrazek(int co, int x, int y) {
		ekrangraf = ekrangry.getGraphics();
		ekrangraf.drawImage(obr[co], x*12-10,y*12-10, Graphics.TOP|Graphics.LEFT);
	}

    public void paint(Graphics g) {
	skonczonerysowanie = false;
	switch (copokazuje)
	{
	case 2: //rysowanie napisu "wczytywanie"
		g.setColor(0x00000000);
		g.fillRect(0,0,width,height);
		g.setColor(0x00FFFFFF);
		g.setFont(czcionkamalagruba);
		g.drawString("£ADOWANIE...",width>>1,(height>>1)-5, Graphics.TOP|Graphics.HCENTER);
	break;
	case 1: //rysowanie poczatku etapu
		g.setColor(0x00000000);
		g.fillRect(0,0,width,height);
		g.setColor(0x00FFFFFF);
		g.setFont(czcionkagruba);
		g.drawString("MISJA "+etap,width>>1,2, Graphics.TOP|Graphics.HCENTER);
		g.drawString("Twoje zadanie:",width>>1,17, Graphics.TOP|Graphics.HCENTER);
		g.setFont(czcionkamala);
		String s1="",s2="";
		switch (misja)
		{
			case 1:
				s1="Zbierz " + misjajeszcze;
				s2="rubinów.";
			break;
			case 2:
				s1="Zniszcz " + misjajeszcze;
				s2="bomb.";
			break;
			case 3:
				s1="Zabij " + misjajeszcze;
				s2="stworów.";
			break;
		}
		g.drawString(s1,width>>1,32, Graphics.TOP|Graphics.HCENTER);
		g.drawString(s2,width>>1,42, Graphics.TOP|Graphics.HCENTER);

		g.drawString("Robotów: "+(zycia+1),width>>1,height, Graphics.BOTTOM|Graphics.HCENTER);
	break;
	default://rysowanie gry

		if (przerysuj) //calosc
		{
			g.setColor(0x00000000);
			g.fillRect(0,0,width,height);

			przerysuj = false;
			spkt = pkt+1;
			smisjajeszcze = misjajeszcze+1;
			g.drawImage(misjaobr,width-16,0,Graphics.TOP|Graphics.RIGHT);

			g.drawImage(obrgora[0],width-52,0,Graphics.TOP|Graphics.RIGHT);
			pisz(g, l2t(zycia+1,1),width-51,0);
			g.drawImage(obrgora[1],width-39,0,Graphics.TOP|Graphics.RIGHT);
			pisz(g, l2t(etap,3), width-38,0);
		} 

		g.setClip(0,6,width,height-6);
		g.drawImage(ekrangry,-ekranx,-ekrany,Graphics.TOP|Graphics.LEFT);
		if ((niesmiertelnosc>10) || ((niesmiertelnosc>0) && (niesmiertelnosc % 2==1)))
			g.drawImage(obrniesm[aniobrazkow],-ekranx+graczx*12-14,-ekrany+graczy*12-14,Graphics.TOP|Graphics.LEFT);
		g.setClip(0,0,width,height);

		//wyniki
		if (pkt!=spkt)
		{
			pisz(g, l2t(pkt,6), 1,0);
			spkt=pkt;
		}
		if ((misjajeszcze!=smisjajeszcze) || (misjajeszcze<=0))
		{
			if ((misjajeszcze>0) || ((misjajeszcze<=0) && (aniobrazkow==0)))
				pisz(g, l2t(Math.abs(misjajeszcze),3), width-15,0);
			else if ((misjajeszcze<=0) && (aniobrazkow==1))
			{
				g.setColor(0x00000000);
				g.fillRect(width-15,0,14,5);
			}
			smisjajeszcze=misjajeszcze;
		}


		if (koniecgry)
		{
			g.setColor(0x00FFFFFF);
			g.setFont(czcionkagruba);
			g.drawString("  KONIEC GRY  ", 50,34, Graphics.TOP | Graphics.HCENTER);
		} else 
		if (!graczzyje)
		{
			g.setColor(0x00FFFFFF);
			g.setFont(czcionkamala);
			g.drawString(" RX zniszczony! ", width>>1,height-3, Graphics.BOTTOM | Graphics.HCENTER);
		}


	break;
	}//switch
	skonczonerysowanie = true;
	}//void


	//wczytywanie etapu
	int odczytajnastepnadana(InputStream is)
	{
		byte[] tabl = new byte[2];
		int c,d;

        try {
			c = is.read();
			tabl[0]=(byte) c;
			c = is.read();
			tabl[1]=(byte) c;
        } catch (java.io.IOException ex) { }

		d = (tabl[1] << 8) + tabl[0];
		return d;
	}

	void wczytajetap(int etp) {
		InputStream is = null;
	    try {
			is = getClass().getResourceAsStream("/etapy/etap."+etp);
			if (is != null) {
				int xx=0,yy=0,c;

		        try {
					rozmx = odczytajnastepnadana(is);
					rozmy = odczytajnastepnadana(is);
					misja = odczytajnastepnadana(is);
					misjajeszcze = odczytajnastepnadana(is);

					Runtime.getRuntime().gc();
					mapa = new int[rozmy][rozmx];
					mapacien = new int[rozmy][rozmx];
			        ekrangry = Image.createImage( (rozmx-2)*12+4, (rozmy-2)*12+4 );

					//odczyt danych etapu
		            while ((c = is.read()) != -1) {
						mapa[yy][xx]=c;
						if (c==3)
						{
							graczx=xx;
							graczy=yy;
						}
						xx++;
						if (xx>=rozmx)
						{
							xx=0;
							if (yy<rozmy-1) yy++;
						}
		            }
		        } catch (java.io.IOException ex) { }

				is.close();
				switch (misja)
				{
					case 1:
					misjaobr = Image.createImage("/misjrub.png");
					break;
					case 2:
					misjaobr = Image.createImage("/misjbom.png");
					break;
					case 3:
					misjaobr = Image.createImage("/misjstw.png");
					break;				
				}
			} else {
				System.out.println("Nie mozna wczytac pliku!");
				return;
			}
	    } catch (java.io.IOException ex) {
			return;
	    }
		Runtime.getRuntime().gc();
	}

	void nowyetap() {
		Runtime.getRuntime().gc();
		copokazuje=2;
		repaint();
		serviceRepaints();
		czasdokoncapokazywania=50;
		zmienetap=false;
		spkt=pkt-1;
		niesmiertelnosc = 0;
		aniobrazkow = 0;
		moznawyjsc = false;
		poprawwyjscia = false;

		wczytajetap(etap);
		for (int y=0; y<=rozmy-1; y++)
		{
			mapa[y][0]=14;
			mapa[y][rozmx-1]=14;
		}
		for (int x=0; x<=rozmx-1; x++)
		{
			mapa[0][x]=14;
			mapa[rozmy-1][x]=14;
		}


		if ((rozmx-2)*12<=width) ekranox=(width>>1)-(rozmx-2)*6;
							else ekranox=-1;
		if ((rozmy-2)*12<=(height-6)) ekranoy=6+((height-6)>>1)-(rozmy-2)*6;
							else ekranoy=-1;
	
		
		if (ekranox<0)
		{
			ekranx=graczx*12-(width-12)/2;
			if (ekranx>rozmx*12-(width+20)) ekranx=rozmx*12-(width+20);
			if (ekranx<0) ekranx=0;
		} else ekranx=-ekranox;
		if (ekranoy<0)
		{
			ekrany=graczy*12-(height-12)/2+3;
			if (ekrany>rozmy*12-(height+20)) ekrany=rozmy*12-(height+20);
			if (ekrany<-6) ekrany=-6;
		} else ekrany=-ekranoy;

		Runtime.getRuntime().gc();
		rysujmape();

		for (int y=1; y<=rozmy-2; y++)
			for (int x=1; x<=rozmx-2; x++)
				mapacien[y][x]=mapa[y][x];

		przerysuj = true;
		dziala = true;
		graczzyje = true;
		copokazuje=1;
		repaint();
		Runtime.getRuntime().gc();
		klaw=0;
		klawpam=0;
	}

	void zakonczetap() {
		mapa = null;
		mapacien = null;
        ekrangry = null;
		misjaobr = null;
		Runtime.getRuntime().gc();
	}

	protected ruch() {
		Runtime.getRuntime().gc();
		finito = false;
		copokazuje=2;

        height = getHeight();
        width = getWidth();

		obr = new Image[45];
		obrcyfr = new Image[10];
		obrniesm = new Image[2];
		obrgora = new Image[2];

		dzwiek = new boolean[2];
		pkt = 0;
		zycia = 4;
		etap = uustawienia.etappoc;
		koniecgry=false;
	}

	void wczytujwszystko() {
		try
		{
			obrcyfr[0] = Image.createImage("/0.png");
			obrcyfr[1] = Image.createImage("/1.png");
			obrcyfr[2] = Image.createImage("/2.png");
			obrcyfr[3] = Image.createImage("/3.png");
			obrcyfr[4] = Image.createImage("/4.png");
			obrcyfr[5] = Image.createImage("/5.png");
			obrcyfr[6] = Image.createImage("/6.png");
			obrcyfr[7] = Image.createImage("/7.png");
			obrcyfr[8] = Image.createImage("/8.png");
			obrcyfr[9] = Image.createImage("/9.png");

			obr[0] = Image.createImage("/obrpust.png");
			obr[1] = Image.createImage("/obrziem.png");
			obr[2] = Image.createImage("/obrmur.png");
			obr[3] = Image.createImage("/obrgracz1.png");
			obr[4] = Image.createImage("/obrkam.png");
			obr[5] = Image.createImage("/obrkam2.png");
			obr[6] = Image.createImage("/obrbomba.png");
			obr[7] = Image.createImage("/obrbomba2.png");
			obr[8] = Image.createImage("/obrbum1.png");
			obr[9] = Image.createImage("/obrbum2.png");
			obr[10] = Image.createImage("/obrbum3.png");
			obr[11] = Image.createImage("/obrgracz2.png");
			obr[12] = Image.createImage("/obrgraczg1.png");
			obr[13] = Image.createImage("/obrgraczg2.png");
			obr[14] = Image.createImage("/obrgraczd1.png");
			obr[15] = Image.createImage("/obrgraczd2.png");
			obr[16] = Image.createImage("/obrgraczp1.png");
			obr[17] = Image.createImage("/obrgraczp2.png");
			obr[18] = Image.createImage("/obrgraczl1.png");
			obr[19] = Image.createImage("/obrgraczl2.png");
			obr[20] = Image.createImage("/obrstal.png");
			obr[21] = Image.createImage("/obrstwp1.png");
			obr[22] = Image.createImage("/obrstwp2.png");
			obr[23] = Image.createImage("/obrstwl1.png");
			obr[24] = Image.createImage("/obrstwl2.png");
			obr[25] = Image.createImage("/obrstws.png");
			obr[26] = Image.createImage("/obrstwb1.png");
			obr[27] = Image.createImage("/obrstwb2.png");
			obr[28] = Image.createImage("/obrniesm.png");
			obr[29] = Image.createImage("/obrwyjz.png");
			obr[30] = Image.createImage("/obrwyjo.png");
			obr[31] = Image.createImage("/obrkolpoz1.png");
			obr[32] = Image.createImage("/obrkolpoz2.png");
			obr[33] = Image.createImage("/obrkolpio1.png");
			obr[34] = Image.createImage("/obrkolpio2.png");
			obr[35] = Image.createImage("/obrmaszrub.png");
			obr[36] = Image.createImage("/obrgracz1o.png");
			obr[37] = Image.createImage("/obrgracz2o.png");
			obr[38] = Image.createImage("/obrciagp.png");
			obr[39] = Image.createImage("/obrciagl.png");
			obr[40] = Image.createImage("/obrgraczpp1.png");
			obr[41] = Image.createImage("/obrgraczpp2.png");
			obr[42] = Image.createImage("/obrgraczlp1.png");
			obr[43] = Image.createImage("/obrgraczlp2.png");
			obr[44] = Image.createImage("/obrzeb.png");

			obrniesm[0] = Image.createImage("/obrniesm1.png");
			obrniesm[1] = Image.createImage("/obrniesm2.png");

			obrgora[0] = Image.createImage("/gorazyc.png");
			obrgora[1] = Image.createImage("/goramis.png");
		}
		catch (java.io.IOException e)	{	}
	}

	void wybuch(int x,int y) {
		for (int b=0; b<=7; b++)
		{
			if ((misja==3) && (((mapa[y+dookolay[b]][x+dookolax[b]]>=15) && (mapa[y+dookolay[b]][x+dookolax[b]]<=18)) || ((mapa[y+dookolay[b]][x+dookolax[b]]>=15+128) && (mapa[y+dookolay[b]][x+dookolax[b]]<=18+128)) || (mapa[y+dookolay[b]][x+dookolax[b]]==20) || (mapa[y+dookolay[b]][x+dookolax[b]]==20+128)))
			{
				if (misjajeszcze<=0) pkt+=30;
				misjajeszcze--;
				pkt+=30;
			}
			if (mapa[y+dookolay[b]][x+dookolax[b]]==3) graczzyje=false;
			if ((mapa[y+dookolay[b]][x+dookolax[b]]==6) || (mapa[y+dookolay[b]][x+dookolax[b]]==13) ||
				(mapa[y+dookolay[b]][x+dookolax[b]]==6+128) || (mapa[y+dookolay[b]][x+dookolax[b]]==13+128))
				mapa[y+dookolay[b]][x+dookolax[b]]=7+128;
			else if ((mapa[y+dookolay[b]][x+dookolax[b]]!=14) && (mapa[y+dookolay[b]][x+dookolax[b]]!=7) && (mapa[y+dookolay[b]][x+dookolax[b]]!=7+128))
					mapa[y+dookolay[b]][x+dookolax[b]]=8+128;
		}
		if (mapa[y][x]==3) graczzyje=false;
		else
		if ((misja==2) && ((mapa[y][x]==6) || (mapa[y][x]==7) || (mapa[y][x]==6+128) || (mapa[y][x]==7+128)  || (mapa[y][x]==13) || (mapa[y][x]==13+128)))
		{
			if (misjajeszcze<=0) pkt+=10;
			misjajeszcze--;
			pkt+=10;
		} else
		if ((misja==3) && (((mapa[y][x]>=15) && (mapa[y][x]<=18)) || ((mapa[y][x]>=15+128) && (mapa[y][x]<=18+128)) || (mapa[y][x]==20) || (mapa[y][x]==20+128)))
		{
			if (misjajeszcze<=0) pkt+=30;
			misjajeszcze--;
			pkt+=30;
		}
		mapa[y][x] = 8+128;
		dzwiek[1]=true;
	}

	void sprawdzczymisjawypelniona() {
		if ((!moznawyjsc) && (misjajeszcze==0))
		{
			pkt+=500;
			moznawyjsc=true;
			poprawwyjscia=true;
		}
	}

	void przesuwekranu() { 	//przesuwanie ekranu
		int odl;
		if (ekranox<0)
		{
			odl=-(ekranx-(graczx*12-(width-12)/2-12))/(width/24);
			if (odl>12) odl=12;
			if (odl<-12) odl=-12;
			if (odl!=0) ekranx = ekranx + odl;
			if (ekranx>rozmx*12-(width+20)) ekranx=rozmx*12-(width+20);
			if (ekranx<0) ekranx=0;
		} else ekranx=-ekranox;
		if (ekranoy<0)
		{
			odl=-(ekrany-(graczy*12-(height-12)/2-12))/((height-7)/24);
			if (odl>12) odl=12;
			if (odl<-12) odl=-12;
			if (odl!=0) ekrany = ekrany + odl;
			if (ekrany>rozmy*12-(height+20)) ekrany=rozmy*12-(height+20);
			if (ekrany<-6) ekrany=-6;
		} else ekrany=-ekranoy;

	}

	void petlagry() {
		int b;
		przesuwekranu();
		//zmiany roznorakie
		aniobrazkow++; 
		if (aniobrazkow>=2) aniobrazkow=0;

		//ruch mapy
		if (graczzyje)
		{
			mapa[graczy][graczx]=128;
			graczobr = 3;
			klawisze();
			mapa[graczy][graczx]=3;
		} else {
			if (((klaw!=0) && (czasdokoncapokazywania>=3)) || (czasdokoncapokazywania>=150) || (zycia==0))
				zmienetap=true;

			if (czasdokoncapokazywania>=10)
			for (b=0; b<40; b++)
			{
				int qx=Math.abs(gra.random.nextInt() % rozmx);
				int qy=Math.abs(gra.random.nextInt() % rozmy);
				if ((mapa[qy][qx]>0) && (mapa[qy][qx]!=14) && (mapa[qy][qx]!=2))
					mapa[qy][qx]=8;
			}//for
		}//else

		for (int y=1; y<=rozmy-2; y++)
			for (int x=1; x<=rozmx-2; x++)
			switch (mapa[y][x])
			{
				case 0: //puste
				case 1: //ziemia
				case 2: //murek
				case 14: //stal
				case 3: //gracz
				case 22: //wyjscie
				break; //zeby dalej juz nie sprawdzalo niezmienianych rzeczy

				case 4: //kamien
				case 5: //rubin
				case 6: //bomba
				case 21: //buteleczka z niesm.
					if ((mapa[y+1][x]==0) || (mapa[y+1][x]==128))
					{
						mapa[y+1][x]=mapa[y][x]+128+7;
						mapa[y][x]=128;
					} else
					if ((mapa[y+1][x]==2) || (mapa[y+1][x]==4) || (mapa[y+1][x]==5) || (mapa[y+1][x]==6) || (mapa[y+1][x]==7) || (mapa[y+1][x]==21))
					{
						b = (Math.abs(gra.random.nextInt() % 2))*2-1;
						if ((mapa[y+1][x+b]==0) && (mapa[y][x+b]==0))
						{
							mapa[y][x+b]=mapa[y][x]+128+7;
							mapa[y][x]=128;
						} else
						if ((mapa[y+1][x-b]==0) && (mapa[y][x-b]==0))
						{
							mapa[y][x-b]=mapa[y][x]+128+7;
							mapa[y][x]=128;
						}
					}
				break;
				case 11: //kamien spadajacy
				case 12: //rubin spadajacy
					if (mapa[y+1][x]==0)
					{
						mapa[y+1][x]=mapa[y][x]+128;
						mapa[y][x]=128;
					} else
					if ((mapa[y+1][x]==2) || (mapa[y+1][x]==4) || (mapa[y+1][x]==5))
					{
						dzwiek[0]=true;
						b = (Math.abs(gra.random.nextInt() % 2))*2-1;
						if ((mapa[y+1][x+b]==0) && (mapa[y][x+b]==0))
						{
							mapa[y][x+b]=mapa[y][x]+128;
							mapa[y][x]=128;
						} else
						if ((mapa[y+1][x-b]==0) && (mapa[y][x-b]==0))
						{
							mapa[y][x-b]=mapa[y][x]+128;
							mapa[y][x]=128;
						} else
							mapa[y][x]=mapa[y][x]-7+128;
					} else
					{
						dzwiek[0]=true;
						mapa[y][x]=mapa[y][x]-7+128;
						if ((mapa[y+1][x]==6) || (mapa[y+1][x]==6+128))
							mapa[y+1][x]=7+128;
						if ((mapa[y+1][x]==21) || (mapa[y+1][x]==21+128))
							mapa[y+1][x]=8+128;
						if (
							((mapa[y+1][x]>=15) && (mapa[y+1][x]<=18)) ||
							((mapa[y+1][x]>=15+128) && (mapa[y+1][x]<=18+128)) ||
							((mapa[y+1][x]==20) || (mapa[y+1][x]==20+128)) ||
							((mapa[y+1][x]==3) && (niesmiertelnosc==0))
							)
							wybuch(x,y+1);
					}
				break;
				case 13: //bomba spadajaca
					if (mapa[y+1][x]==0)
					{
						mapa[y+1][x]=mapa[y][x]+128;
						mapa[y][x]=128;
					} else
					if (mapa[y+1][x]==1)
					{
						mapa[y][x]=6+128;
					} else 
					{
						if (
							((mapa[y+1][x]>=2) && (mapa[y+1][x]<=7)) ||
							((mapa[y+1][x]>=11) && (mapa[y+1][x]<=14)) ||
							((mapa[y+1][x]>=2+128) && (mapa[y+1][x]<=7+128)) ||
							((mapa[y+1][x]>=11+128) && (mapa[y+1][x]<=14+128)) ||
							((mapa[y+1][x]>=15) && (mapa[y+1][x]<=19)) ||
							((mapa[y+1][x]>=15+128) && (mapa[y+1][x]<=19+128)) ||
							((mapa[y+1][x]==21) || (mapa[y+1][x]==21+128)) ||
							((mapa[y+1][x]==20) || (mapa[y+1][x]==20+128)) ||
							((mapa[y+1][x]>=22) && (mapa[y+1][x]<=29)) ||
							((mapa[y+1][x]>=23+128) && (mapa[y+1][x]<=29+128))
							)
								mapa[y][x]=7+128;
						if (
							((mapa[y+1][x]>=15) && (mapa[y+1][x]<=18)) ||
							((mapa[y+1][x]>=15+128) && (mapa[y+1][x]<=18+128)) ||
							((mapa[y+1][x]==20) || (mapa[y+1][x]==20+128))
							)
								wybuch(x,y+1);
					}
				break;
				case 28: //buteleczka z niesm. spadajaca
					if (mapa[y+1][x]==0)
					{
						mapa[y+1][x]=mapa[y][x]+128;
						mapa[y][x]=128;
					} else
					if (mapa[y+1][x]==1)
					{
						mapa[y][x]=21+128;
					} else 
					{
						mapa[y][x]=8+128;
						if ((mapa[y+1][x]==21) || (mapa[y+1][x]==21+128))
						{
							mapa[y+1][x]=8+128;
						}
						dzwiek[0]=true;
					}
				break;
				case 7: //wybuchajaca bomba
					wybuch(x,y);
				break;
				case 8: //wybuch 1
					mapa[y][x]=9+128;
				break;
				case 9: //wybuch 2
					mapa[y][x]=10+128;
				break;
				case 10: //wybuch 3
					mapa[y][x]=128;
				break;
				case 15: //stworek z oczami, idzie w prawo
					mapa[y][x]=128;
					if ((mapa[y+1][x]==27) || (mapa[y-1][x]==27) || (mapa[y][x+1]==27) || (mapa[y][x-1]==27)) mapa[y][x]=5+128; 
					else
					if (mapa[y+1][x]==0)
					{
						mapa[y+1][x]=16+128;
					} else 
						if ((mapa[y+1][x]==3) && (niesmiertelnosc==0)) wybuch(x,y+1);
					else 
					if ((mapa[y][x+1]==0) && (mapa[y+1][x]!=0))
					{
						mapa[y][x+1]=15+128;
					} else 
						if ((mapa[y][x+1]==3) && (niesmiertelnosc==0)) wybuch(x+1,y);
					else
					if (mapa[y][x+1]!=0)
					{
						mapa[y][x]=18+128;
					}
				break;
				case 16: //stworek z oczami, idzie w dol
					mapa[y][x]=128;
					if ((mapa[y+1][x]==27) || (mapa[y-1][x]==27) || (mapa[y][x+1]==27) || (mapa[y][x-1]==27)) mapa[y][x]=5+128; 
					else
					if (mapa[y][x-1]==0)
					{
						mapa[y][x-1]=17+128;
					} else 
						if ((mapa[y][x-1]==3) && (niesmiertelnosc==0)) wybuch(x-1,y);
					else 
					if ((mapa[y+1][x]==0) && (mapa[y][x-1]!=0))
					{
						mapa[y+1][x]=16+128;
					} else 
						if ((mapa[y+1][x]==3) && (niesmiertelnosc==0)) wybuch(x,y+1);
					else
					if (mapa[y+1][x]!=0)
					{
						mapa[y][x]=15+128;
					}
				break;
				case 17: //stworek z oczami, idzie w lewo
					mapa[y][x]=128;
					if ((mapa[y+1][x]==27) || (mapa[y-1][x]==27) || (mapa[y][x+1]==27) || (mapa[y][x-1]==27)) mapa[y][x]=5+128; 
					else
					if (mapa[y-1][x]==0)
					{
						mapa[y-1][x]=18+128;
					} else 
						if ((mapa[y-1][x]==3) && (niesmiertelnosc==0)) wybuch(x,y-1);
					else 
					if ((mapa[y][x-1]==0) && (mapa[y-1][x]!=0))
					{
						mapa[y][x-1]=17+128;
					} else 
						if ((mapa[y][x-1]==3) && (niesmiertelnosc==0)) wybuch(x-1,y);
					else
					if (mapa[y][x-1]!=0)
					{
						mapa[y][x]=16+128;
					}
				break;
				case 18: //stworek z oczami, idzie w gore
					mapa[y][x]=128;
					if ((mapa[y+1][x]==27) || (mapa[y-1][x]==27) || (mapa[y][x+1]==27) || (mapa[y][x-1]==27)) mapa[y][x]=5+128; 
					else
					if (mapa[y][x+1]==0)
					{
						mapa[y][x+1]=15+128;
					} else 
						if ((mapa[y][x+1]==3) && (niesmiertelnosc==0)) wybuch(x+1,y);
					else 
					if ((mapa[y-1][x]==0) && (mapa[y][x+1]!=0))
					{
						mapa[y-1][x]=18+128;
					} else 
						if ((mapa[y-1][x]==3) && (niesmiertelnosc==0)) wybuch(x,y-1);
					else
					if (mapa[y-1][x]!=0)
					{
						mapa[y][x]=17+128;
					}
				break;
				case 19: //wylegarnia stworka z oczami
					if (Math.abs(gra.random.nextInt() % 40)==0)
					{
						b=Math.abs(gra.random.nextInt() % 4);
						if ((b==0) && (mapa[y][x+1]==0)) mapa[y][x+1]=128+16;
						else if ((b==1) && (mapa[y+1][x]==0)) mapa[y+1][x]=128+17;
						else if ((b==2) && (mapa[y][x-1]==0)) mapa[y][x-1]=128+18;
						else if ((b==3) && (mapa[y-1][x]==0)) mapa[y-1][x]=128+15;
					}
				break;
				case 20: //stworek oko
					if ((mapa[y+1][x]==27) || (mapa[y-1][x]==27) || (mapa[y][x+1]==27) || (mapa[y][x-1]==27)) mapa[y][x]=5+128; 
					else
					{
						b=Math.abs(gra.random.nextInt() % 13);
						if (b<=3) //idzie za graczem
						{
							mapa[y][x]=128;
							if ((x<graczx) && (mapa[y][x+1]==0))
							{
								mapa[y][x+1]=20+128;
							} else
							if ((x<graczx) && (mapa[y][x+1]==3) && (niesmiertelnosc==0))
							{
								wybuch(x+1,y);
							} else
							if ((x>graczx) && (mapa[y][x-1]==0))
							{
								mapa[y][x-1]=20+128;
							} else
							if ((x>graczx) && (mapa[y][x-1]==3) && (niesmiertelnosc==0))
							{
								wybuch(x-1,y);
							} else
							if ((y<graczy) && (mapa[y+1][x]==0))
							{
								mapa[y+1][x]=20+128;
							} else
							if ((y<graczy) && (mapa[y+1][x]==3) && (niesmiertelnosc==0))
							{
								wybuch(x,y+1);
							} else
							if ((y>graczy) && (mapa[y-1][x]==0))
							{
								mapa[y-1][x]=20+128;
							} else
							if ((y>graczy) && (mapa[y-1][x]==3) && (niesmiertelnosc==0))
							{
								wybuch(x,y-1);
							} else
								mapa[y][x]=20+128;
						} else
						if (b<=9) //idzie losowo
						{
							mapa[y][x]=128;
							b=Math.abs(gra.random.nextInt() % 4);
							if ((b==0) && (mapa[y][x+1]==0))
							{
								mapa[y][x+1]=20+128;
							} else
							if ((b==1) && (mapa[y+1][x]==0))
							{
								mapa[y+1][x]=20+128;
							} else
							if ((b==2) && (mapa[y][x-1]==0))
							{
								mapa[y][x-1]=20+128;
							} else
							if (mapa[y-1][x]==0)
							{
								mapa[y-1][x]=20+128;
							} else
							mapa[y][x]=20+128;
						} else
							mapa[y][x]=20+128;
					}//else
				break;
				case 23: //kolczatka pozioma idzie w prawo
					if (mapa[y][x+1]==0)
					{
						mapa[y][x]=128;
						mapa[y][x+1]=23+128;
					} else
					if (mapa[y][x+1]==3)
					{
						wybuch(x+1,y);
					} else
					if ((mapa[y][x+1]==6) || (mapa[y][x+1]==6+128))
					{
						mapa[y][x+1]=7;
					} else
					{
						mapa[y][x]=24+128;
					}
				break;
				case 24: //kolczatka pozioma idzie w lewo
					if (mapa[y][x-1]==0)
					{
						mapa[y][x]=128;
						mapa[y][x-1]=24+128;
					} else
					if (mapa[y][x-1]==3)
					{
						wybuch(x-1,y);
					} else
					if ((mapa[y][x-1]==6) || (mapa[y][x-1]==6+128))
					{
						mapa[y][x-1]=7;
					} else
					{
						mapa[y][x]=23+128;
					}
				break;
				case 25: //kolczatka pionowa idzie w gore
					if (mapa[y-1][x]==0)
					{
						mapa[y][x]=128;
						mapa[y-1][x]=25+128;
					} else
					if (mapa[y-1][x]==3)
					{
						wybuch(x-1,y);
					} else
					if ((mapa[y-1][x]==6) || (mapa[y-1][x]==6+128))
					{
						mapa[y-1][x]=7;
					} else
					{
						mapa[y][x]=26+128;
					}
				break;
				case 26: //kolczatka pionowa idzie w dol
					if (mapa[y+1][x]==0)
					{
						mapa[y][x]=128;
						mapa[y+1][x]=26+128;
					} else
					if (mapa[y+1][x]==3)
					{
						wybuch(x+1,y);
					} else
					if ((mapa[y+1][x]==6) || (mapa[y+1][x]==6+128))
					{
						mapa[y+1][x]=7;
					} else
					{
						mapa[y][x]=25+128;
					}
				break;
				case 27: //27- maszyna przerabiajaca stwory i kamienie na rubiny, a bomby na kamienie
					/*for (b=0; b<=7; b++)
					{
						if (
							((mapa[y+dookolay[b]][x+dookolax[b]]>=15) && (mapa[y+dookolay[b]][x+dookolax[b]]<=18)) ||
							((mapa[y+dookolay[b]][x+dookolax[b]]>=15+128) && (mapa[y+dookolay[b]][x+dookolax[b]]<=18+128)) ||
							(mapa[y+dookolay[b]][x+dookolax[b]]==20) || (mapa[y+dookolay[b]][x+dookolax[b]]==20+128)
							)
							mapa[y+dookolay[b]][x+dookolax[b]]=5+128;
					}*/
					if (mapa[y-1][x]==4)
					{
						b=Math.abs(gra.random.nextInt() % 3);
						for (int c=0; c<=2; c++)
						{
							b++;
							if (b>=3) b=0;
							if (mapa[y+dookolay[b]][x+dookolax[b]]==0)
							{
								mapa[y+dookolay[b]][x+dookolax[b]]=5+128;
								mapa[y-1][x]=128;
								break;
							}
						}
					} else
					if (mapa[y-1][x]==6)
					{
						b=Math.abs(gra.random.nextInt() % 3);
						for (int c=0; c<=2; c++)
						{
							b++;
							if (b>=3) b=0;
							if (mapa[y+dookolay[b]][x+dookolax[b]]==0)
							{
								mapa[y+dookolay[b]][x+dookolax[b]]=4+128;
								mapa[y-1][x]=128;
								break;
							}
						}
					}
				break;
				case 29: //29- ciagnik w prawo
					if ((mapa[y-1][x+1]==0) &&
						(
						((mapa[y-1][x]>=4) && (mapa[y-1][x]<=7)) || ((mapa[y-1][x]>=4+128) && (mapa[y-1][x]<=7+128)) ||
						(mapa[y-1][x]==21) || (mapa[y-1][x]==21+128)
						))
					{
						mapa[y-1][x+1]=mapa[y-1][x];
						mapa[y-1][x]=128;
					}
				break;
				case 30: //30- ciagnik w lewo
					if ((mapa[y-1][x-1]==0) &&
						(
						((mapa[y-1][x]>=4) && (mapa[y-1][x]<=7)) || ((mapa[y-1][x]>=4+128) && (mapa[y-1][x]<=7+128)) ||
						(mapa[y-1][x]==21) || (mapa[y-1][x]==21+128)
						))
					{
						mapa[y-1][x-1]=mapa[y-1][x];
						mapa[y-1][x]=128;
					}
				break;
			}

		//rysowanie calosci na mapie
		for (int y=1; y<=rozmy-2; y++)
			for (int x=1; x<=rozmx-2; x++)
				if ((mapa[y][x]!=mapacien[y][x]) && (mapa[y][x]!=3))
					rysujnamapie(mapa[y][x],x,y);

		if (niesmiertelnosc>0)
		{
			graczzyje=true;
			niesmiertelnosc--;
		}

		if (graczzyje)
		{
			if ((graczobr==3) && ((mapa[graczy-1][graczx]==4) || (mapa[graczy-1][graczx]==5) || (mapa[graczy-1][graczx]==6) || (mapa[graczy-1][graczx]==21)))
			{
				graczani=8;
				if (Math.abs(gra.random.nextInt() % 15)==0) graczani=34;
			} else 
			if (graczobr==3) 
			{
				graczani=0;
				if (Math.abs(gra.random.nextInt() % 15)==0) graczani=33;
			}

			rysujnamapieobrazek(graczobr+graczani,graczx,graczy);
		} else if (czasdokoncapokazywania<200) czasdokoncapokazywania++;

		sprawdzczymisjawypelniona();

		if (poprawwyjscia)
		{
			for (int y=1; y<=rozmy-2; y++)
				for (int x=1; x<=rozmx-2; x++)
					if (mapa[y][x]==22)
						rysujnamapieobrazek(30,x,y);
			poprawwyjscia=false;
		}

		for (int y=1; y<=rozmy-2; y++)
			for (int x=1; x<=rozmx-2; x++)
			{
				if (mapa[y][x]>=128)
					mapa[y][x]-=128;
				mapacien[y][x]=mapa[y][x];
			}

		if (uustawienia.wibrator)
		{
			if (dzwiek[0])
			{
				dzwiek[0]=false;
//				Vibrator.triggerVibrator(15);
			}
			if (dzwiek[1])
			{
				dzwiek[1]=false;
//				Vibrator.triggerVibrator(50);
			}
		}

	}


	protected void hideNotify()
	{
		dziala = false;
	}

	protected void showNotify()
	{
		dziala = true;
		przerysuj = true;
	}

    public final boolean przerwij() { 
		finito = true;
		return koniecgry;
	}

    public final void zabijgracza() { 
		niesmiertelnosc = 0;
		if (graczzyje)
			wybuch(graczx,graczy);
	}

	public void run() {
		finito = false;
		repaint();
		wczytujwszystko();
		nowyetap();

		while (!finito)
		{
			if (copokazuje==0)
			{
				petlagry();
			} else {
				if (klaw!=0) czasdokoncapokazywania=0;
				czasdokoncapokazywania--;
				if (czasdokoncapokazywania<=0)
					copokazuje=0;
			}

			if ((!graczzyje) && (jestcmdsamoboj))
			{
				removeCommand(gra.c_zabijsie);
				jestcmdsamoboj=false;
			} else
			if ((graczzyje) && (!jestcmdsamoboj) && (copokazuje==0))
			{
				addCommand(gra.c_zabijsie);
				jestcmdsamoboj=true;
			}

			if (zmienetap)
			{
				if (!graczzyje)
				{
					if (zycia>=1)
					{
						zakonczetap();
						zycia--;
						nowyetap();
					} else {
						koniecgry=true;
					}
				} else {
					if (czasdokoncapokazywania==0)
					{
						zakonczetap();
						etap++;
						if (etap>uustawienia.etapjest)
							etap = 1;
						if (etap>uustawienia.etapmax)
							uustawienia.etapmax = etap;
						uustawienia.etappoc = etap;
						nowyetap();
					} else czasdokoncapokazywania--;
				}
			}

			repaint();
			try {
				Thread.currentThread().sleep(100 /*10*/);
			} catch(java.lang.InterruptedException e) { }

			while ((!dziala) && (!finito)) { }
		
		}//while
	}//void

}

