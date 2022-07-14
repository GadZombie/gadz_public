import javax.microedition.lcdui.*;

import com.siemens.mp.game.Light;
import com.siemens.mp.game.Sound;

public class ruch extends Canvas implements Runnable {

private static final int[] sin={ /* cos [x] = sin [x+90] */
   0,   4,   9,  13,  18,  22,  27,  31,  35,  40,  44,  49,  53,  57,  62,  66,  70,  75,  79,  83,
  87,  91,  96, 100, 104, 108, 112, 116, 120, 124, 128, 131, 135, 139, 143, 146, 150, 153, 157, 160,
 164, 167, 171, 174, 177, 180, 183, 186, 190, 192, 195, 198, 201, 204, 206, 209, 211, 214, 216, 219,
 221, 223, 225, 227, 229, 231, 233, 235, 236, 238, 240, 241, 243, 244, 245, 246, 247, 248, 249, 250,
 251, 252, 253, 253, 254, 254, 254, 255, 255, 255, 255, 255, 255, 255, 254, 254, 254, 253, 253, 252,
 251, 250, 249, 248, 247, 246, 245, 244, 243, 241, 240, 238, 236, 235, 233, 231, 229, 227, 225, 223,
 221, 219, 216, 214, 211, 209, 206, 204, 201, 198, 195, 192, 190, 186, 183, 180, 177, 174, 171, 167,
 164, 160, 157, 153, 150, 146, 143, 139, 135, 131, 128, 124, 120, 116, 112, 108, 104, 100,  96,  91,
  87,  83,  79,  75,  70,  66,  62,  57,  53,  49,  44,  40,  35,  31,  27,  22,  18,  13,   9,   4,
  -0,  -4,  -9, -13, -18, -22, -27, -31, -35, -40, -44, -49, -53, -57, -62, -66, -70, -75, -79, -83,
 -87, -91, -96,-100,-104,-108,-112,-116,-120,-124,-128,-131,-135,-139,-143,-146,-150,-153,-157,-160,
-164,-167,-171,-174,-177,-180,-183,-186,-190,-192,-195,-198,-201,-204,-206,-209,-211,-214,-216,-219,
-221,-223,-225,-227,-229,-231,-233,-235,-236,-238,-240,-241,-243,-244,-245,-246,-247,-248,-249,-250,
-251,-252,-253,-253,-254,-254,-254,-255,-255,-255,-255,-255,-255,-255,-254,-254,-254,-253,-253,-252,
-251,-250,-249,-248,-247,-246,-245,-244,-243,-241,-240,-238,-236,-235,-233,-231,-229,-227,-225,-223,
-221,-219,-216,-214,-211,-209,-206,-204,-201,-198,-195,-192,-190,-186,-183,-180,-177,-174,-171,-167,
-164,-160,-157,-153,-150,-146,-143,-139,-135,-131,-128,-124,-120,-116,-112,-108,-104,-100, -96, -91,
 -87, -83, -79, -75, -70, -66, -62, -57, -53, -49, -44, -40, -35, -31, -27, -22, -18, -13,  -9,  -4};
	public boolean finito;


	private boolean dziala, przerysuj, skonczonerysowanie;
	private boolean koniecgry;
	public int		klaw;
 	private Image[]    obrcyfr;
    private int   height;
    private int   width;

	private boolean czekajnapis=false;
	private String czekajtekst;

	private ustaw uustaw;
	private granie ggranie;
	private stanowiska sstanowiska;

//--------------------zmienne globalne dla calej gry----------------------------
	public long[]	graczkasa,		//pieniadze, ktorymi gracz placi
					graczkasasuma;	//pieniadze, w sumie zarobione w czasie gry (nie odejmowane przy kupnie)
	public byte		ilgracz;		//ilosc graczy
	public byte		runda;			//ktora runda jest grana?
//------------------------------------------------------------------------------
	byte			krok=1;

	ustawienia uustawienia = gra.uustawienia;

/****************** PROCEDURY NADRZEDNE, DLA WSZYSTKICH KLAS ******************/

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
		case KEY_NUM1:
			if (ggranie!=null)
			{
				if (krok==1) krok=5;
					else krok=1;
			}
		break;
		case KEY_NUM3:
			if (ggranie!=null)
			{
				klaw=6;
			}
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
			return;
		}
      
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
	}

	protected ruch() {
		obrcyfr = new Image[49];
		String w;
		char c;

		try
		{
			for (int a=0; a<=9; a++)
			{
				w="/" + a + ".png";
				obrcyfr[a] = Image.createImage(w);

			}
			for (int a=0; a<=25; a++)
			{
				c=(char) (a+97);
				w="/" + c + ".png";
				obrcyfr[10+a] = Image.createImage(w);

			}
			obrcyfr[39] = Image.createImage("/a_.png");
			obrcyfr[40] = Image.createImage("/c_.png");
			obrcyfr[41] = Image.createImage("/e_.png");
			obrcyfr[42] = Image.createImage("/l_.png");
			obrcyfr[43] = Image.createImage("/n_.png");
			obrcyfr[44] = Image.createImage("/o_.png");
			obrcyfr[45] = Image.createImage("/s_.png");
			obrcyfr[46] = Image.createImage("/x_.png");
			obrcyfr[47] = Image.createImage("/z_.png");
			obrcyfr[48] = Image.createImage("/slesz.png");
		}
		catch (java.io.IOException e)	{	}
		w=null;

	}

    public void czyscekran(Graphics g) {
		g.setColor(0x00000000);
	    g.fillRect(0, 0, getWidth(), getHeight());
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

    public void pisz(Graphics g, String tekst, int xx, int yy) {
		for (int a=0; a<=tekst.length()-1; a++)
			if (((int) tekst.charAt(a) -48>=0) && ((int) tekst.charAt(a) -48<=9))
				g.drawImage(obrcyfr[ (int) tekst.charAt(a) -48], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else
			if (((int) tekst.charAt(a) >=65) && ((int) tekst.charAt(a) <=90))
				g.drawImage(obrcyfr[ (int) tekst.charAt(a) -55], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==97) g.drawImage(obrcyfr[39], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==99) g.drawImage(obrcyfr[40], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==101) g.drawImage(obrcyfr[41], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==108) g.drawImage(obrcyfr[42], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==110) g.drawImage(obrcyfr[43], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==111) g.drawImage(obrcyfr[44], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==115) g.drawImage(obrcyfr[45], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==120) g.drawImage(obrcyfr[46], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==122) g.drawImage(obrcyfr[47], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);
			else if ((int) tekst.charAt(a)==47) g.drawImage(obrcyfr[48], xx+a*4, yy, Graphics.TOP|Graphics.LEFT);

	}

    public void piszs(Graphics g, String tekst, int xx, int yy) {
		pisz(g,tekst,xx-tekst.length()*2,yy);
	}

    public void piszp(Graphics g, String tekst, int xx, int yy) {
		pisz(g,tekst,xx-tekst.length()*4,yy);
	}

	Graphics eg;

/******************************************************************************/
/******************************* KLASA GRANIA *********************************/
/******************************************************************************/
class granie
{
    private Image tlo = null;
 	private Image[]    obr,obrik;

	public int[]		graczx,graczy; //pozycja
	public short[]		graczs,graczk, //sila i kat
						gracze,			//energia
						graczou;		//dla komputera: przy ostatnim ukladzie k i s, ile zabralo komus sily?
	public boolean[]	graczzyje;		//czy gracz zyje?

	public byte[][]	tlon;

	public short	opcja, opcja1;
	public short	wiatr;
	public short	wybuchr, wybuchdr;
	public byte		tgracz; //teraz gracz
	public short	ekranx,ekrany;
	public short	rozmx,rozmy;

	public long		pocx, pocy, //pocisk
					pocdx, pocdy;

	private boolean odswiezpanel;

/***************************** OBSLUGA KLAWIATURY *****************************/
   public void klawisze()
   {
    switch (opcja)
    {
    case 0:
      switch (klaw) //granie
      {
      case 1:
			graczs[tgracz]+=krok;
			if (graczs[tgracz]>999) graczs[tgracz] = 999;
			odswiezpanel=true;
			break;
      case 2:
			graczs[tgracz]-=krok;
			if (graczs[tgracz]<1) graczs[tgracz] = 1;
			odswiezpanel=true;
         break;
      case 4:
			if (graczk[tgracz]-1==90)
				  przerysuj=true;
			graczk[tgracz]-=krok;
			if (graczk[tgracz]<0) graczk[tgracz] += 181;
			odswiezpanel=true;
         break;
      case 3:
			if (graczk[tgracz]+1==91)
				  przerysuj=true;
			graczk[tgracz]+=krok;
			if (graczk[tgracz]>180) graczk[tgracz] -= 181;
			odswiezpanel=true;
         break;
      case 5:
			strzel(graczx[tgracz],graczy[tgracz], graczs[tgracz],graczk[tgracz]);
			klaw=0;
			odswiezpanel=true;
		  break;
		case 6:
			zmienopcje(1);
		break;
      }//switch
    break;
    case 1:
      switch (klaw) //ogladanie terenu
      {
      case 1:
			ekrany-=3;
			if (ekrany<0) ekrany = 0;
			break;
      case 2:
			ekrany+=3;
			if (ekrany>rozmy-1-height) ekrany = (short) (rozmy-1-height);
         break;
      case 3:
			ekranx-=3;
			if (ekranx<0) ekranx = 0;
         break;
      case 4:
			ekranx+=3;
			if (ekranx>rozmx-1-width) ekranx = (short) (rozmx-1-width);
         break;
      case 5:
			zmienopcje(0);
			klaw=0;
         break;
      }//switch
    break;
    case 2:
      switch (klaw) //ogladanie mapki
      {
      case 5:
			zmienopcje(1);
			przeniesnaobraz();
			ustawekranna(graczx[tgracz],graczy[tgracz]);
			klaw=0;
			break;
      }//switch
    break;
    }//switch

   }

/***************************** PROCEDURY GLOWNE *******************************/

	protected granie() { //konstruktor
		finito = false;

		if (uustawienia.podswietlenie) Light.setLightOff();

        height = getHeight();
        width = getWidth();

		
		obr = new Image[10];
		obrik = new Image[4];

		try
		{
			obr[0] = Image.createImage("/strp.png");
			obr[1] = Image.createImage("/strpd.png");
			obr[2] = Image.createImage("/strd.png");
			obr[3] = Image.createImage("/strld.png");
			obr[4] = Image.createImage("/strl.png");
			obr[5] = Image.createImage("/strlg.png");
			obr[6] = Image.createImage("/strg.png");
			obr[7] = Image.createImage("/strpg.png");
			obr[8] = Image.createImage("/pocisk.png");
			obr[9] = Image.createImage("/czolg1.png");

			obrik[0] = Image.createImage("/zsila.png");
			obrik[1] = Image.createImage("/zenergia.png");
			obrik[2] = Image.createImage("/zkatp.png");
			obrik[3] = Image.createImage("/zkatl.png");
		}
		catch (java.io.IOException e)	{	}

		koniecgry=false;
		nowyetap();
		runda++;
	}

	void zmienwiatr() {
		wiatr = (short) (gra.random.nextInt() % uustawienia.silawiatru);
	}

	void nowyetap() {
		czekajnapis=true;
		czekajtekst="TWORZENIE ETAPU";
		repaint();
		serviceRepaints();
		graczx = new int[ilgracz];
		graczy = new int[ilgracz];
		graczs = new short[ilgracz];
		graczk = new short[ilgracz];
		gracze = new short[ilgracz];
		graczou = new short[ilgracz];
		graczzyje = new boolean[ilgracz];
		for (int a=0; a<ilgracz; a++)
		{
			graczzyje[a]=true;
			graczs[a]=20;
			graczk[a]=90;
			gracze[a]=200;
		}

		przerysuj = true;
		dziala = true;
		rozmx=240;
		rozmy=150;

		Runtime.getRuntime().gc();
		tlo = Image.createImage( rozmx,rozmy );
		tlon = new byte[rozmy][rozmx/8+1];

		rysujetap();
		if (uustawienia.mapka)
		{
			pokazmapke();
			zmienopcje(2);
		} else {
			zmienopcje(1);
			przeniesnaobraz();
			ustawekranna(graczx[tgracz],graczy[tgracz]);
		}

		tgracz=0;
		zmienwiatr();

		czekajnapis=false;
		repaint();
	}

	void zakonczetap() {
		tlo = null;
		graczx=null;
		graczy=null;
		graczs=null;
		graczk=null;
		gracze = null;
		graczou = null;
		graczzyje = null;
		tlon=null;
	}

	void zakonczgranie() {
		zakonczetap();
		obr=null;
		obrik=null;
	}

	void petlagry() {
		byte p;

		if ((opcja==0) && (uustawienia.graczrodz[tgracz]>=1))
		{
			strzelakomputer();
		}

		if ((opcja==1) && (uustawienia.graczrodz[tgracz]>=1))
		{
			zmienopcje(0);
		}
		if (opcja==3)
		{
			p=pociskleci(false);
			if (p==1)
			{
				zmienopcje(5);
			} else
			if (p==2)
			{
//				Sound.playTone(40,80);
				zmienopcje(4);
			}
		}
		if (opcja==4) //wybuchanie
		{
			if (wybuchdr>wybuchr)
			{
				wybuch(false, (int) pocx , (int) pocy);
				zmienopcje(5);
			}
			wybuchdr+=3;
		}
		if (opcja==5) //opadanie graczy
		{
			boolean ruch;

			ruch=false;
			for (int a=0; a<ilgracz; a++)
			{
				if (graczy[a]<rozmy-1)
				{
					if (wezpixel(graczy[a]+1,graczx[a])==0)
					{
						graczy[a]++;
						if (gracze[a]>0) gracze[a]--;
						if (a==tgracz)
							ustawekranna(graczx[tgracz],graczy[tgracz]);
						ruch=true;
					}
				}
			}//for
			if (!ruch) //juz nie opadaja, wiec przechodzimy do nastepnej kolejki i nastepnego gracza
			{
				zmienopcje(6);
			}
		}//if
		if (opcja==6) //wybuchanie graczy
		{
			if ((graczzyje[opcja1]) && (gracze[opcja1]<=0))
			{
				pocx=graczx[opcja1];
				pocy=graczy[opcja1];
				ustawekranna(graczx[opcja1],graczy[opcja1]);

				if (wybuchdr>wybuchr)
				{
					wybuch(false, graczx[opcja1],graczy[opcja1]);
					graczzyje[opcja1]=false;
				}

				wybuchdr+=2;
			} else {
				wybuchr=(short) (11+Math.abs(gra.random.nextInt() % 30));
				wybuchdr=1;
				opcja1++;
			}

			if (opcja1>=ilgracz) 
			{
				short zyje=0; //sprawdzenie, czy zyje wiecej niz jedna druzyna?
				short dr=-1;
				for (int a=0; a<ilgracz; a++)
				{
					if (graczzyje[a]) 
					{
						if (dr==-1)
						{
							dr=uustawienia.graczdruz[a];
							zyje=1;
						} else
						{
							if (uustawienia.graczdruz[a]!=dr)
								zyje++;
						}
					}//if
				}//for
				if (zyje>=2) //jezeli tak, to gramy dalej
				{
					if (uustawienia.zmiennywiatr) zmienwiatr();
					do
					{
						tgracz++;
						if (tgracz>=ilgracz) tgracz=0;
					}
					while (!graczzyje[tgracz]);
					zmienopcje(1);
				} else { // jezeli nie, to koniec gry
					zmienopcje(7);
				}
			}
		}//if

	}

	public void zmienopcje(int nopcja) {
		przerysuj=true;
		odswiezpanel=true;
		//zakanczanie obecnej opcji
/*		switch (opcja)
		{
			case 1:
			break;
		}//switch
*/
		//ustawianie nowej opcji
		opcja=(short) (nopcja);
		switch (opcja)
		{
			case 0: //granie
				ustawekranna(graczx[tgracz],graczy[tgracz]);
			break;
/*			case 1: //ogladanie
			break;*/
			// case 2:  //ogladanie mapki
			// case 3:  //lecenie pocisku
			case 4: //wybuchanie
				wybuchr=11;
				wybuchdr=1;
				pocx=pocx>>8;
				pocy=pocy>>8;
			break;
			// case 5:  //opadanie graczy
			case 6:  //wybuchanie graczy
				wybuchr=(short) (11+Math.abs(gra.random.nextInt() % 30));
				wybuchdr=1;
				opcja1=0;
			break;
			case 7:
//				gra.pzakonczgre();
			break;
		}//switch
	}

/*************************** RUCH I DZIALANIE GRY *****************************/

    public void strzel(int sx, int sy, int ss, int sk) {
		pocx=(sx+sin[90+graczk[tgracz]]/48)<<8;
		pocy=(sy-4-sin[graczk[tgracz]]/48)<<8;
		pocdx=(sin[(90+sk) % 360] *ss) >>3;
		pocdy=(-sin[sk % 360] *ss) >>3;
		zmienopcje(3);
	}

    public void strzeldd(int sx, int sy, int sdx, int sdy) {
		pocx=(sx+sin[90+graczk[tgracz]]/48)<<8;
		pocy=(sy-4-sin[graczk[tgracz]]/48)<<8;
		pocdx=sdx;
		pocdy=sdy;
		zmienopcje(3);
	}

    public byte pociskleci(boolean proba) {
		boolean sciana;
		int px,py;

		pocx=pocx+pocdx;
		pocy=pocy+pocdy;
		if (pocdy<20<<8) pocdy=pocdy+uustawienia.grawitacja;
		if (Math.abs(pocdx)<20<<8) pocdx=pocdx+wiatr;

		px = (int) (pocx>>8);
		py = (int) (pocy>>8);
		if (!proba) ustawekranna(px,py);

		if ((px>=0) && (px<rozmx) && (py>=0) && (py<rozmy)) 
		{
			if (wezpixel(py,px)==1) sciana=true;
				else sciana=false;

			if (!sciana)
			{
				for (int a=0; a<ilgracz; a++)
				{
					if ((graczzyje[a]) &&
						(px>=graczx[a]-4) && (py>=graczy[a]-5) &&
						(px<=graczx[a]+4) && (py<=graczy[a]))
					{
						sciana=true;
						a=ilgracz;
					}//if
				}//for
			}//if

		} else sciana=false;

		if ((sciana) || (pocy>>8 >= rozmy-1))
		{
			return 2;
		} else 
		if ((pocx>>8<-10) || (pocx>>8>rozmx+10))
		{
			return 1;
		} else {
			return 0;
		}
	}

	public int wybuch(boolean proba, int x, int y) {
		int r,r2;
		long rr;

		if (proba) r=wybuchr;
			  else r=wybuchr+Math.abs(gra.random.nextInt() % 5);

		y-=r>>1;

		if (!proba) //rysuj dziure
		{
			eg = tlo.getGraphics();
			eg.setColor(0x00FFFFFF);
			for (int a=0; a<r; a++)
			{
				rr=sin[(a*(180/r)) % 180] *(r>>1);

				r2=(int) rr>>8;

				eg.drawLine((int) (x-r2)  ,y+a,
								(int) (x+r2)  ,y+a);

				if ((y+a>=0) && (y+a<=rozmy-1))
				{
					for (int b=x-r2; b<=x+r2; b++)
						if ((b>=0) && (b<rozmx))
							ryspixel(y+a,b,0);
				}//if
			}//for
		}//if

		//uszkodz czolgi
		int e,x1,x2,y1,y2,dz;
		for (int a=0; a<ilgracz; a++)
		{
			if (graczzyje[a])
			{
				r+=3;
				x1=x;
				y1=y;
				x2=graczx[a];
				y2=graczy[a]-3;
				e=Math.abs(x2-x1)+Math.abs(y2-y1);

				if ((Math.abs(x2-x1)<=r) && (Math.abs(y2-y1)<=r))
				{
					if (!proba)
					{
						gracze[a] -= (4*r-2*e+2);
						graczkasa[tgracz]+=(4*r-2*e+2)*50;
						graczkasasuma[tgracz]+=(4*r-2*e+2)*50;
						if (gracze[a]<0) gracze[a]=0;
					} else 
					{
						if (uustawienia.graczdruz[a]==uustawienia.graczdruz[tgracz])
							return -Math.abs(2*r-e+2);
						else
							return Math.abs(2*r-e+2);
					}//if
				}//if

			// sqrt( sqr(x2-x1) + sqr (y2-y1) )
/*			if ((x2-x1)==0) dz=1;
				else dz=Math.abs(x2-x1);
			e = Math.abs( Math.abs(x2-x1) + ( ((y2-y1)*(y2-y1)) / (2* dz ) ) );*/

			}//if
		}//for
		return 0;
	}//void


/*	strzela losowo i gdy trafi celnie, to zapamietuje to miejsce  */
	void strzelakomputer() {
		int sk,ss,b,
			nb,nk,ns,
			odk,dok;

		byte a;
		boolean ok=false;


		nb=graczou[tgracz];
		nk=graczk[tgracz];
		ns=graczs[tgracz];
		wybuchr=11;

		odk=0;
		dok=179;

		for (a=0; a<ilgracz; a++)
		{
			if ((a!=tgracz) && (graczzyje[a]))
			{
				if (graczx[a]<graczx[tgracz])
				{
					odk=50;
					dok=179;
				} else
				if (graczx[a]>=graczx[tgracz])
				{
					odk=0;
					dok=130;
				}//if
			}//if
		}//for

		for (int i=0; i<=39; i++) // "i" prob losowego strzelania
		{
			sk=Math.abs(gra.random.nextInt() % (dok-odk))+odk;
			ss=Math.abs(gra.random.nextInt() % 50)+5;

			strzel(graczx[tgracz],graczy[tgracz], ss, sk); //strzel
			do {
				a=pociskleci(true);
			} while (a==0);  //sprawdzaj jak leci pocisk az spadnie
			b=wybuch(true, (int) (pocx>>8),(int) (pocy>>8)); //symuluj wybuch
			if ((a==2) && (b>0)) //jezeli trafione w czolg, to juz nie szukaj!
			{
				ok=true;
			}//if
			if ((b>nb) || (ok)) //jezeli odejmie komus sily wiecej niz przy 
			{					//poprzednim sprawdzaniu, to zapamietaj ten uklad
				nb=b;
				nk=sk;
				ns=ss;
				if (ok) 
				{
					i=999;
				}
			}//if
		}//for i

		//jesli nie znaleziono sensownego strzalu, to strzel na oslep w najblizszego
		if (nb<=0)
		{
			//najpierw znajdz najblizszego
			int odl=999999,odln,wybrany=0;
			for (a=0; a<ilgracz; a++)
				if (uustawienia.graczdruz[a]!=uustawienia.graczdruz[tgracz])
				{
					odln=Math.abs(graczx[tgracz]-graczx[a])+Math.abs(graczy[tgracz]-graczy[a]);
					if (odln<=odl)
						wybrany=a;					
				}

			sk = (graczx[wybrany]-graczx[tgracz])*256;
			ss = (graczy[wybrany]-(graczy[tgracz]-10))*256;
			if (Math.abs(ss)>Math.abs(sk))
			{
				ss=ss/256;
				sk=(sk/Math.abs(ss)*8);
				if (ss>0) ss=2048;
					else ss=-2048;
			} else {
				sk=sk/256;
				ss=(ss/Math.abs(sk)*8);
				if (sk>0) sk=2048;
					else sk=-2048;
			}
			if (ss>0) ss=0;

			graczs[tgracz]=40;
			graczk[tgracz]=(short) (90-(sk/23));
			strzeldd(graczx[tgracz],graczy[tgracz], sk, ss);
		} else {
			graczk[tgracz]=(short) (nk);
			graczs[tgracz]=(short) (ns);

			strzel(graczx[tgracz],graczy[tgracz], ns, nk);
		}
	}//void


/************************** PROCEDURY DO RYSOWANIA ****************************/
/*********************** RYSOWANIE ETAPU (TLO I MAPKA) ************************/

    public void ryspixel(int sy, int sx, int kol) {
		int nx;
		byte nxx;
		nx=sx / 8;
		nxx=(byte) (sx % 8);
		if (kol==1)	tlon[sy][nx] = (byte) (tlon[sy][nx] | (1 << nxx));
			    else tlon[sy][nx] = (byte) (tlon[sy][nx] & ~(1 << nxx));
	}

    public byte wezpixel(int sy, int sx) {
		int nx,nxx;
		nx=sx / 8;
		nxx=sx % 8;
		return (byte) ( (tlon[sy][nx] & (1 << nxx)) >> nxx );
	}

/* TWORZENIE NOWEGO TERENU */
	public void rysujetap() {
		int a,y,y2,sx,dy,sz;

		sx=-1; //ile jeszcze ta predkosc ma trwac?
		a=0; //pozycja x
		y=(Math.abs(gra.random.nextInt() % (rozmy-35))+30)<<4; //wysokosc
		dy=0; //predkosc zmiany wysokosci
		sz=Math.abs(gra.random.nextInt() % (3+uustawienia.gorzystosc*2))+(3+uustawienia.gorzystosc/2); //gorzystosc terenu, czyli szybkosc zmian dy

		while (a<rozmx)
		{
			if (sx<=0)
			{
				sx=Math.abs(gra.random.nextInt() % 3)+1;
				dy=dy+gra.random.nextInt() % sz;
				if (dy>>4 >8)
				{
					dy=8<<4;
				}
				if (dy>>4 <-8)
				{
					dy=-8<<4;
				}
			}

			y=y+dy;
			if (y>(rozmy-1) << 4) {
				y=rozmy-1 << 4;
				sx=-1;
				dy=0;
			}
			if (y<30 << 4) {
				y=30 << 4;
				sx=-1;
				dy=0;
			}

			y2=y >> 4;
			for (int t=y2; t<rozmy; t++) ryspixel(t,a,1);
			sx--;
			a++;
		}

		int kol[] = new int[ilgracz];

		for (a=0; a<ilgracz; a++) kol[a]=a;

		int n1,n2,nn;
		for (a=0; a<20; a++)
		{
			n1=Math.abs(gra.random.nextInt() % ilgracz);
			n2=Math.abs(gra.random.nextInt() % ilgracz);

			nn=kol[n1];
			kol[n1]=kol[n2];
			kol[n2]=nn;
		}

		for (a=0; a<ilgracz; a++)
		{
			graczx[a]= (rozmx/ilgracz)*kol[a] + ((rozmx/2)/ilgracz) + Math.abs(gra.random.nextInt() % ((rozmx/2)/ilgracz-5)) ;
			for (int b=0; b<rozmy; b++)
			{
				if (wezpixel(b,graczx[a])==1)
				{
					graczy[a]=b-1;
					b=rozmy;
				}//if
			}//for b
		}//for a

		kol = null;

		//pokazmapke();
		//przeniesnaobraz();
	}

	public void przeniesnaobraz() {
		czekajnapis=true;
		czekajtekst="RYSOWANIE ETAPU";
		repaint();
		serviceRepaints();
		eg = tlo.getGraphics();
		eg.setClip(0, 0, tlo.getWidth(), tlo.getHeight());
		eg.setColor(0x00FFFFFF);
	    eg.fillRect(0, 0, tlo.getWidth(), tlo.getHeight());
		eg.setColor(0x00000000);
/*zwyczajnie przenosi pixel po pixelu
	for (int y=0; y<=199; y++)
		{
			for (int x=0; x<=199; x++)
			{
				if (tlon[y][x]==1)
					eg.fillRect(x,y,1,1);
			}
		}
*/
/*jak natrafi na pixela to maluje juz do samego dolu ekranu*/
		for (int x=0; x<rozmx; x++)
		{
			for (int y=0; y<rozmy; y++)
			{
				if (wezpixel(y,x)==1)
				{
					eg.fillRect(x,y,1,rozmy);
					y=rozmy;
				}//if
			}//for
		}//for

		ekranx = (short) (100-width / 2);
		ekrany = (short) (100-height / 2);
		czekajnapis=false;
	}

	public void pokazmapke() {
		int rx, ry, ox,oy;
		czekajnapis=true;
		czekajtekst="TWORZENIE MAPKI";
		repaint();
		serviceRepaints();
		ekranx = 0;
		ekrany = 0;

		eg = tlo.getGraphics();
		eg.setColor(0x00FFFFFF);
	    eg.fillRect(0, 0, tlo.getWidth(), tlo.getHeight());
		eg.setColor(0x00000000);


		rx=(rozmx)/width;
		ry=(rozmy)/(height-7);
		if ((rozmx)%width>width/4) rx++;
		if ((rozmy)%(height-7)>(height-7)/4) ry++;

		ox=(width>>1) -(rozmx/2)/rx;
		oy=7+((height-7)>>1) -(rozmy/2)/ry;

		for (int x=0; x<rozmx/rx; x++)
		{
			for (int y=0; y<rozmy/ry; y++)
			{
				if (wezpixel(y*ry,x*rx)==1)
				{
					eg.fillRect(ox+x,oy+y,1,rozmy/ry-y);
					y=rozmy;
				}//if
			}//for
		}//for

		eg.drawRect(ox,oy,rozmx/rx,rozmy/ry);
		eg.setClip(ox,oy,rozmx/rx,rozmy/ry);

		for (int a=0; a<ilgracz; a++)
		{
			eg.setColor(0x00ffffff);
			eg.fillArc(ox+graczx[a]/rx-2,oy+graczy[a]/ry-2,5,5,0,360);
			eg.setColor(0x00000000);
			eg.drawArc(ox+graczx[a]/rx-2,oy+graczy[a]/ry-2,5,5,0,360);
		}

		eg.setClip(0,0,width,height);
		czekajnapis=false;
	}//void

/********************* POZOSTALE PROCEDURY DO RYSOWANIA ***********************/

    public void ustawekranna(int x, int y) {
		ekranx=(short) (x-((width>>1)));
		ekrany=(short) (y-((height>>1)+3));
		if (ekranx<0) ekranx=0;
		if (ekranx>rozmx-1-width) ekranx = (short) (rozmx-1-width);
		if (ekrany<0) ekrany=0;
		if (ekrany>rozmy-1-height) ekrany = (short) (rozmy-1-height);
	}

    public void rysujstrzalke(Graphics g, int px, int py) {
		int k=0;
		if (px<0) k=1;
		if (px>width) k=2;
		if (py<7) k+=10;
		if (py>height) k+=20;
		switch (k)
		{
			case 1: //lewo
			g.drawImage(obr[4],0,py-2,Graphics.TOP|Graphics.LEFT);
			break;
			case 2: //prawo
			g.drawImage(obr[0],width,py-2,Graphics.TOP|Graphics.RIGHT);
			break;
			case 10: //gora
			g.drawImage(obr[6],px-2,7,Graphics.TOP|Graphics.LEFT);
			break;
			case 20: //dol
			g.drawImage(obr[2],px-2,height,Graphics.BOTTOM|Graphics.LEFT);
			break;
			case 11: //lewo-gora
			g.drawImage(obr[5],0,7,Graphics.TOP|Graphics.LEFT);
			break;
			case 12: //prawo-gora
			g.drawImage(obr[7],width,7,Graphics.TOP|Graphics.RIGHT);
			break;
			case 21: //lewo-dol
			g.drawImage(obr[3],0,height,Graphics.BOTTOM|Graphics.LEFT);
			break;
			case 22: //prawo-dol
			g.drawImage(obr[1],width,height,Graphics.BOTTOM|Graphics.RIGHT);
			break;
		}//switch
	}

    public void maluj(Graphics g) {
		skonczonerysowanie = false;

		if ((opcja!=2) && (uustawienia.wskaznikiekranu)) g.setClip(1,8,width-1,height-8);
									else g.setClip(0,7,width,height-7);
		//rysuj teren
		int ofx,ofy;
		int py,px;

		if ((opcja==4) || (opcja==6))
		{
			py=(wybuchr-wybuchdr)/3+2;
			if (py>8) py=8;
			ofx = gra.random.nextInt() % py;
			ofy = gra.random.nextInt() % py;
		} else {
			ofx=0;
			ofy=0;
		}
		g.drawImage(tlo,-ekranx+ofx,-ekrany+ofy,Graphics.TOP|Graphics.LEFT);

		if (opcja==2) //mapka
		{
			wybuchr++;
			if (wybuchr>=10) //wykorzystany "wybuchr", ktory zwykle jest do ruchu wybuchu, do migania napisu
			{
				int rx, ry, ox,oy;
				rx=(rozmx)/width;
				ry=(rozmy)/(height-7);
				if ((rozmx)%width>width/4) rx++;
				if ((rozmy)%(height-7)>(height-7)/4) ry++;
				ox=(width>>1) -(rozmx/2)/rx;
				oy=7+((height-7)>>1) -(rozmy/2)/ry;
				for (int a=0; a<ilgracz; a++)
				{
					pisz(g,l2t(a+1,0),ox+graczx[a]/rx-1,oy+graczy[a]/ry-2);
					g.setColor(0x00000000);
					g.drawRect(ox+graczx[a]/rx-2,oy+graczy[a]/ry-3,4,6);
					g.setColor(0x00ffffff);
					g.drawRect(ox+graczx[a]/rx-3,oy+graczy[a]/ry-4,6,8);
				}//for				
			}//if
			if (wybuchr>=20) wybuchr=0;
		}//if

		if (opcja!=2) //rysuj graczy, tylko jak nie ma mapki
			for (int a=0; a<ilgracz; a++) 
			{
				if (graczzyje[a])
				{
					px=graczx[a]-ekranx+ofx;
					py=graczy[a]-3-ekrany+ofy;
	
					if ((px>=-3) && (px<=width+3) && (py>=5) && (py<=height+4))
					{ //rysuj czolg
						g.drawImage(obr[9],px-5,py-3,Graphics.TOP|Graphics.LEFT);
						g.setColor(0x00000000);
						g.drawLine(px, py-1, px+sin[90+graczk[a]]/48, py-1-sin[graczk[a]]/48);
						if (((a==tgracz) && (opcja==1) && (wybuchr<=3)) ||
							((a!=tgracz) && (opcja==1) && (uustawienia.graczdruz[a]==uustawienia.graczdruz[tgracz])) ||
							((opcja==0) && (uustawienia.graczrodz[tgracz]>=1)))
						{
							g.setColor(0x00ffffff);
							g.drawArc(px-8,py-9, 17,17,0,360);
							g.setColor(0x00000000);
							g.drawArc(px-9,py-10, 19,19,0,360);
						}

					} else 
						if (uustawienia.strzalki) { //lub strzalki
							if ((opcja!=1) ||
								((opcja==1) && ((a!=tgracz) || ((a==tgracz) && (wybuchr<=3)))))
								rysujstrzalke(g, px, py);
						}//if
				}//if
			}//for

		if (opcja==1) //obserwacja
		{
			wybuchr++;
			if (wybuchr>=5) //wykorzystany "wybuchr", ktory zwykle jest do ruchu wybuchu, do migania napisu
			{
				g.setColor(0x00000000);
				g.fillRect((width>>1)-15,height-7,29,7);
				pisz(g,"CZOlG "+l2t(tgracz+1,0),(width>>1)-14,height-6);
			}
			if (wybuchr>=10) wybuchr=0;

		}
		if ((opcja==0) && (uustawienia.graczrodz[tgracz]>=1)) //komputer mysli
		{
			g.setColor(0x00000000);
			g.fillRect((width>>1)-15,height-13,29,13);
			pisz(g,"MYsLe",(width>>1)-10,height-12);
			pisz(g,"CZOlG "+l2t(tgracz+1,0),(width>>1)-14,height-6);
		}


		if ((opcja==4) || (opcja==6)) //wybuchanie
		{
			g.setColor(0x00ffffff);
			g.fillArc((int) (pocx -ekranx -(wybuchdr>>1)), (int) (pocy -ekrany -(wybuchdr>>1)), wybuchdr, wybuchdr, 0,360);
			g.setColor(0x00000000);
			g.drawArc((int) (pocx -ekranx -(wybuchdr>>1)), (int) (pocy -ekrany -(wybuchdr>>1)), wybuchdr, wybuchdr, 0,360);
			if ((opcja==6) && (wybuchdr>1) && (wybuchdr<=wybuchr))
			{
				int a=Math.abs(gra.random.nextInt() % 13)+2;

				if (Math.abs(gra.random.nextInt() % 2)==0)
					g.setColor(0x00ffffff);
					else
					g.setColor(0x00000000);
				g.fillArc( (int) (pocx -ekranx -(a>>1) +(gra.random.nextInt() % 10)), 
					       (int) (pocy -ekrany -(a>>1) +(gra.random.nextInt() % 10)), 
							a, a, 0,360);
			}
		}

		if (opcja==3) //rysuj pocisk
		{ 
			px=(int) ((pocx >> 8) -ekranx);
			py=(int) ((pocy >> 8) -ekrany);

			if ((px>=0) && (px<=width) && (py>=7) && (py<=height))
			{ //rysuj pocisk
				g.drawImage(obr[8],px-1,py-1,Graphics.TOP|Graphics.LEFT);
			} else { //lub strzalki
				rysujstrzalke(g, px, py);
			}//else
		}//if

		if ((opcja!=2) && (uustawienia.wskaznikiekranu))
		{
			int rx,pzx;
			g.setClip(0,7,width,height-7);
			g.setColor(0x00ffffff);
			g.fillRect(0,7,1,height-7);
			g.fillRect(1,7,width,1);

			rx = (width*width/rozmx);
			pzx = (ekranx*width/rozmx);
			g.setColor(0x00000000);
			g.fillRect(pzx,7,rx,1);

			rx = ((height-6)*(height-6)/rozmy);
			pzx = (ekrany*(height-6)/rozmy)+7;
			g.fillRect(0,pzx,1,rx);

		}

		//wyniki
		g.setClip(0,0,width,7);
	 	if ((przerysuj) && (opcja!=3)) //przerysuj caly panel gorny
			{
				g.setColor(0x00000000);
				g.fillRect(0,0,width,7);
			}

		if ((przerysuj) || (odswiezpanel))
		{
			if ((opcja==0) || (opcja>=3)) //ustawienia strzelania
			{
				pisz(g, l2t(tgracz+1,0), 1,1);
				pisz(g, l2t(uustawienia.graczdruz[tgracz]+1,0), 7,1);
				g.setColor(0x00ffffff);
				g.drawLine(5,0,5,6);
				g.drawLine(11,0,11,6);

				short a;
				if (graczk[tgracz]<=90) a=2;
					else a=3;
				g.drawImage(obrik[a],13,1,Graphics.TOP|Graphics.LEFT);
				if (graczk[tgracz]<=90) a=graczk[tgracz];
					else a=(short) (180-graczk[tgracz]);
				pisz(g, l2t(a,2), 19,1);

				g.drawImage(obrik[0],29,1,Graphics.TOP|Graphics.LEFT);
				pisz(g, l2t(graczs[tgracz],3), 35,1);

				g.drawImage(obrik[1],49,1,Graphics.TOP|Graphics.LEFT);
				pisz(g, l2t(gracze[tgracz],3), 55,1);
			} else	
			/*if ((opcja==1) || (opcja==2)) */ //wiatr itd
			{
				g.setColor(0x00FFFFFF);
				g.drawLine((width>>1),1,(width>>1),5);
				g.drawLine((width>>1),3,(width>>1)+(wiatr<<1),3);
				if (wiatr>0)
				{
					g.drawLine((width>>1)+(wiatr<<1),3,(width>>1)+(wiatr<<1)-2,1);
					g.drawLine((width>>1)+(wiatr<<1),3,(width>>1)+(wiatr<<1)-2,5);
					pisz(g, l2t(wiatr,2), (width>>1)+(wiatr<<1)+2,1);
				} else
				if (wiatr<0)
				{
					g.drawLine((width>>1)+(wiatr<<1),3,(width>>1)+(wiatr<<1)+2,1);
					g.drawLine((width>>1)+(wiatr<<1),3,(width>>1)+(wiatr<<1)+2,5);
					pisz(g, l2t(wiatr,2), (width>>1)+(wiatr<<1)-12,1);
				}
			}//if
		}//if

/*		if (koniecgry)
		{
			g.setColor(0x00000000);
			g.setFont(czcionkakoniecgry);
			g.drawString(" KONIEC GRY ", 50,36, Graphics.TOP | Graphics.HCENTER);
		} */

		przerysuj=false;
		odswiezpanel=false;
		skonczonerysowanie = true;
	}

};//class

/******************************************************************************/
/***************************** KLASA USTAWIENIA *******************************/
/******************************************************************************/

class ustaw
{
	private final String[] rodzaje={"CZlOWIEK","KOMPUTER"};
	private final String[][] menu={
	/*0 glowne*/	{"ROZPOCZNIJ GRe","ILOsc RUND","LICZBA GRACZY","USTAWIENIA GRACZY","WARUNKI","OGoLNE OPCJE"},
	/*1 warunki*/	{"WRoc","ZMIENNY WIATR","SIlA WIATRU","GRAWITACJA","STROMOsc GoR"},
	/*2 ogolne op*/	{"WRoc","STRZAlKI DLA CZOlGoW","MAPKA PRZED GRa","WSKAxNIKI EKRANU","PODsWIETLENIE"}
		};

	boolean dalej=false;
	short odswiez=-1;
	byte sky=0,ky=1; //pozaycja Y kursora
	short my=0; //pozaycja Y menu
	byte ilemenu; //ile linii menu miesci sie na ekranie
	byte ktoremenu=0; //ktore menu wlasnie widac
	byte migkur=0; //miganie kursora

/***************************** OBSLUGA KLAWIATURY *****************************/
   public void klawisze()
   {
	switch (klaw) 
	{
	case 1:
		ky--;
		if (ktoremenu<99)
		{
			if (ky<0) ky=(byte) (menu[ktoremenu].length-1);
		} else
			if (ky<0) ky=(byte) (uustawienia.graczy*2);
		if ((ky/ilemenu)*ilemenu!=my)
		{
			my=(short) ((ky/ilemenu)*ilemenu);
			odswiez=-1;
		}
	break;
	case 2:
		ky++;
		if (ktoremenu<99)
		{
			if (ky>=menu[ktoremenu].length) ky=0;
		} else
			if (ky>uustawienia.graczy*2) ky=0;

		if ((ky/ilemenu)*ilemenu!=my)
		{
			my=(short) ((ky/ilemenu)*ilemenu);
			odswiez=-1;
		}
	break;
	case 3:
		switch (ktoremenu)
		{
		case 0:
			switch (ky)
			{
			case 1:
				uustawienia.rund--;
				if (uustawienia.rund<1) uustawienia.rund=99;
					odswiez=1;
			break;		
			case 2:
				uustawienia.graczy--;
				if (uustawienia.graczy<2) uustawienia.graczy=9;
				odswiez=2;
			break;		
			}//switch
		break;
		case 1:
			switch (ky)
			{
			case 2:
				uustawienia.silawiatru--;
				if (uustawienia.silawiatru<0) uustawienia.silawiatru=20;
				odswiez=2;
			break;		
			case 3:
				uustawienia.grawitacja--;
				if (uustawienia.grawitacja<0) uustawienia.grawitacja=99;
				odswiez=3;
			break;		
			case 4:
				uustawienia.gorzystosc--;
				if (uustawienia.gorzystosc<0) uustawienia.gorzystosc=30;
				odswiez=4;
			break;		
			}//switch
		break;
		case 99:
			if (ky>=1)
			{
				if ((ky % 2)==1) //rodz
				{
					if (uustawienia.graczrodz[ky/2]>0) uustawienia.graczrodz[ky/2]--;
					else uustawienia.graczrodz[ky/2]=1;
				} else //grupy
				{
					if (uustawienia.graczdruz[(ky/2)-1]>0) uustawienia.graczdruz[(ky/2)-1]--;
					else uustawienia.graczdruz[(ky/2)-1]=8;
				}
				odswiez=ky;//((ky-1)/2)-1;
			}//if
		break;
		}//switch
	break;
	case 4:
	case 5:
		switch (ktoremenu)
		{
		case 0:
			switch (ky)
			{
			case 0:
				dalej=true;
			break;
			case 1:
				uustawienia.rund++;
				if (uustawienia.rund>99) uustawienia.rund=1;
				odswiez=1;
			break;		
			case 2:
				uustawienia.graczy++;
				if (uustawienia.graczy>9) uustawienia.graczy=2;
				odswiez=2;
			break;		
			case 3:
				ktoremenu=99;
				ky=1;
				odswiez=-1;
			break;		
			case 4:
				ktoremenu=1;
				ky=1;
				odswiez=-1;
			break;		
			case 5:
				ktoremenu=2;
				ky=1;
				odswiez=-1;
			break;		
			}//switch
		break;
		case 1:
			switch (ky)
			{
			case 0:
				ktoremenu=0;
				odswiez=-1;
				ky=4;
			break;
			case 1:
				uustawienia.zmiennywiatr=!uustawienia.zmiennywiatr;
				odswiez=1;
			break;
			case 2:
				uustawienia.silawiatru++;
				if (uustawienia.silawiatru>20) uustawienia.silawiatru=0;
				odswiez=2;
			break;		
			case 3:
				uustawienia.grawitacja++;
				if (uustawienia.grawitacja>99) uustawienia.grawitacja=0;
				odswiez=3;
			break;		
			case 4:
				uustawienia.gorzystosc++;
				if (uustawienia.gorzystosc>30) uustawienia.gorzystosc=0;
				odswiez=4;
			break;		
			}//switch
		break;
		case 2:
			switch (ky)
			{
			case 0:
				ktoremenu=0;
				odswiez=-1;
				ky=5;
			break;
			case 1:
				uustawienia.strzalki=!uustawienia.strzalki;
				odswiez=1;
			break;
			case 2:
				uustawienia.mapka=!uustawienia.mapka;
				odswiez=2;
			break;
			case 3:
				uustawienia.wskaznikiekranu=!uustawienia.wskaznikiekranu;
				odswiez=3;
			break;
			case 4:
				uustawienia.podswietlenie=!uustawienia.podswietlenie;
				odswiez=4;
			break;
			}//switch
		break;
		case 99:
			if (ky==0)
			{
				ktoremenu=0;
				odswiez=-1;
				ky=3;
			} else
			if (ky>=1)
			{
				if ((ky % 2)==1) //rodz
				{
					if (uustawienia.graczrodz[ky/2]<1) uustawienia.graczrodz[ky/2]++;
					else uustawienia.graczrodz[ky/2]=0;
				} else //grupy
				{
					if (uustawienia.graczdruz[(ky/2)-1]<8) uustawienia.graczdruz[(ky/2)-1]++;
					else uustawienia.graczdruz[(ky/2)-1]=0;
				}
				odswiez=ky;//((ky-1)/2)-1;
			}//if
		break;
		}//switch
	break;
	}//switch
	klaw=0;

   }

/***************************** PROCEDURY GLOWNE *******************************/

	protected ustaw() { //konstruktor
        height = getHeight();
        width = getWidth();

		ilemenu = (byte) ((height-16) / 6);

		odswiezustawienia();
		czekajnapis=false;
	}

	void odswiezustawienia() {
		ilgracz=uustawienia.graczy;
		runda=0;
		if (graczkasa!=null) graczkasa=null;
		if (graczkasasuma!=null) graczkasasuma=null;

		graczkasa = new long[ilgracz];
		graczkasasuma = new long[ilgracz];

		for (int a=0; a<ilgracz; a++)
		{
			graczkasa[a]=0;
			graczkasasuma[a]=0;
		}
	}

	String taknie(boolean s)
	{
		if (s) 
			return "TAK";
		else return "NIE";
	}

	void petla() {
		migkur++;
		if (migkur>=10) migkur=0;
	}

	void zakoncz() {
		odswiezustawienia();
	}

/************************** PROCEDURY DO RYSOWANIA ****************************/

    public void maluj(Graphics g) {
		skonczonerysowanie = false;

		if (odswiez!=-9)
		{
			if (odswiez==-1) //odswiez caly ekran
			{
				g.setClip(0,0,width,height);
				czyscekran(g);
				piszs(g,"USTAWIENIA GRY",width/2,1);

				if (my>0)
				{
					g.setColor(0x00FFFFFF);
					for (int a=0; a<=2; a++)
						g.drawLine(1+a,8-a, 5-a,8-a);
				}
				if (((ktoremenu==99) && (my+ilemenu<uustawienia.graczy*2)) ||
					((ktoremenu<99) && (my+ilemenu<menu[ktoremenu].length)))
				{
					g.setColor(0x00FFFFFF);
					for (int a=0; a<=2; a++)
						g.drawLine(1+a,11+(ilemenu)*6+a, 5-a,11+(ilemenu)*6+a);
				}

				if (ktoremenu<99)
				{
					for (int a=0; a<menu[ktoremenu].length; a++)
					{
						if ((((a+1)/ilemenu)*ilemenu)==my)
							pisz(g,menu[ktoremenu][a],5,10+(a-my)*6);
					}//for
				} else
				{
					if (my==0) pisz(g,"WRoc",5,10);
					for (int a=0; a<uustawienia.graczy; a++)
					{
						if ((((a*2)/ilemenu)*ilemenu)==my)
						{
							pisz(g,"GRACZ "+l2t(a+1,0),5,16+((a<<1)-my)*6);
							piszp(g,rodzaje[uustawienia.graczrodz[a]],width-1,16+((a<<1)-my)*6);
						}//if
						if (((((a+1)*2)/ilemenu)*ilemenu)==my)
						{
							pisz(g,"DRUzYNA",10,22+((a<<1)-my)*6);
							piszp(g,l2t(uustawienia.graczdruz[a]+1,0),width-1,22+((a<<1)-my)*6);
						}//if
					}//for
				}
			}//if
			
			switch (ktoremenu)
			{
				case 0:
					if ((odswiez==1) || (odswiez==-1)) piszp(g,l2t(uustawienia.rund,2),width-1,10+(1-my)*6);
					if ((odswiez==2) || (odswiez==-1)) piszp(g,l2t(uustawienia.graczy,0),width-1,10+(2-my)*6);
				break;
				case 1:
					if ((odswiez==1) || (odswiez==-1)) piszp(g,taknie(uustawienia.zmiennywiatr),width-1,10+(1-my)*6);
					if ((odswiez==2) || (odswiez==-1)) piszp(g,l2t(uustawienia.silawiatru,2),width-1,10+(2-my)*6);
					if ((odswiez==3) || (odswiez==-1)) piszp(g,l2t(uustawienia.grawitacja,2),width-1,10+(3-my)*6);
					if ((odswiez==4) || (odswiez==-1)) piszp(g,l2t(uustawienia.gorzystosc,2),width-1,10+(4-my)*6);
				break;
				case 2:
					if ((odswiez==1) || (odswiez==-1)) piszp(g,taknie(uustawienia.strzalki),width-1,10+(1-my)*6);
					if ((odswiez==2) || (odswiez==-1)) piszp(g,taknie(uustawienia.mapka),width-1,10+(2-my)*6);
					if ((odswiez==3) || (odswiez==-1)) piszp(g,taknie(uustawienia.wskaznikiekranu),width-1,10+(3-my)*6);
					if ((odswiez==4) || (odswiez==-1)) piszp(g,taknie(uustawienia.podswietlenie),width-1,10+(4-my)*6);
				break;
				case 99:
					if (odswiez>=1)
					{
						if ((((((odswiez-1)/2)*2)/ilemenu)*ilemenu)==my)
							piszp(g,rodzaje[uustawienia.graczrodz[(odswiez-1)/2]],width-1,10+(((odswiez-1)/2)*2+1-my)*6);
						if ((((((odswiez-1)/2+1)*2)/ilemenu)*ilemenu)==my)
							piszp(g,l2t(uustawienia.graczdruz[(odswiez-1)/2]+1,0),width-1,16+(((odswiez-1)/2)*2+1-my)*6);
					}
				break;
			}//switch

		}//if
		if ((odswiez==-1) || (sky!=ky) || (migkur==0) || (migkur==5))
		{
			if (sky!=ky) migkur=0;
			if (odswiez!=-1)
			{
				g.setColor(0x00000000);
				g.fillRect(1,11+(sky-my)*6, 3,3);
			}
			if (migkur<=4)
				g.setColor(0x00FFFFFF);
			else
				g.setColor(0x00000000);
			g.fillRect(1,11+(ky-my)*6, 3,3);
			sky=ky;
		}
		odswiez=-9;


		skonczonerysowanie = true;
	}

};//class

/******************************************************************************/
/***************************** KLASA STANOWISKA *******************************/
/******************************************************************************/

class stanowiska
{
	boolean dalej=false;
	boolean odswiez=true;
	private Image[] ikonki;
	private byte[] kolejnosc;
	private byte strona,pozy,ilelinii;

/***************************** OBSLUGA KLAWIATURY *****************************/
   public void klawisze()
   {
	switch (klaw) 
	{
	case 1:
		if (pozy>0)
		{
		pozy--;
		}
		odswiez=true;
		klaw=0;
	break;
	case 2:
		if (pozy<ilgracz-ilelinii)
		{
		pozy++;
		}
		odswiez=true;
		klaw=0;
	break;
	case 5:
		dalej=true;
		klaw=0;
	break;
	}//switch

   }

/***************************** PROCEDURY GLOWNE *******************************/

	protected stanowiska() { //konstruktor
        height = getHeight();
        width = getWidth();

		ikonki = new Image[2];
		czekajnapis=false;
		try {
			ikonki[0] = Image.createImage("/ikoczl.png");
			ikonki[1] = Image.createImage("/ikokomp.png");
		}
		catch (java.io.IOException e)	{	}

		long l;
		byte b;
		boolean z;
		kolejnosc = new byte[ilgracz];
		for (int a=0; a<ilgracz; a++) kolejnosc[a]=(byte) a;

		do {
			z=false;
			for (int a=0; a<ilgracz-1; a++)
			{
				if (graczkasasuma[kolejnosc[a]]<graczkasasuma[kolejnosc[a+1]])
				{
					b=kolejnosc[a];
					kolejnosc[a]=kolejnosc[a+1];
					kolejnosc[a+1]=b;
					z=true;
				}//if
			}//for
		} while (z);

		pozy=0;
		strona=0;
		ilelinii = (byte) ((height-16) / 6);
	}//void

	void petla() {
	}

	void zakoncz() {
		ikonki=null;
		kolejnosc=null;
	}

/************************** PROCEDURY DO RYSOWANIA ****************************/

    public void maluj(Graphics g) {
		skonczonerysowanie = false;


		if (odswiez)
		{
			g.setClip(0,0,width,height);
			czyscekran(g);
			if (runda<uustawienia.rund)	{
				piszp(g,"RUNDA "+l2t(runda,0)+"/"+l2t(uustawienia.rund,0),width-1,1);
				pisz(g,"STANOWISKA",1,1);
			}
				else piszs(g,"STANOWISKA OSTATECZNE",width/2,1);

			for (int a=0; a<ilgracz; a++)
			{
				if (((a-pozy)>=0) && ((a-pozy)<ilelinii))
				{
					g.drawImage(ikonki[uustawienia.graczrodz[kolejnosc[a]]],1,8+(a-pozy)*6, Graphics.LEFT | Graphics.TOP);
					pisz(g, l2t(kolejnosc[a]+1,0), 11,8+(a-pozy)*6);
					pisz(g, l2t(uustawienia.graczdruz[kolejnosc[a]]+1,0), 17,8+(a-pozy)*6);
					piszp(g, l2t(graczkasasuma[kolejnosc[a]],0), width-1,8+(a-pozy)*6);
				}
			}
		}


		odswiez=false;
		skonczonerysowanie = true;
	}

};//class

/******************************************************************************/
/******************************* KLASA SKLEPU *********************************/
/******************************************************************************/



/******************************************************************************/
/*********************** PODSTAWA DZIALANIA PROGRAMU **************************/
/******************************************************************************/


    public void paint(Graphics g) {
	if (czekajnapis)
	{
		g.setClip(0,0,width,height);
		czyscekran(g);
		g.setColor(0x00000000);
		g.fillRect((width/2)-20,height-13,40,10);
		piszs(g,czekajtekst,width/2,height-10);
	} else {
		if (ggranie!=null)
		{
			ggranie.maluj(g);
		} else
		if (uustaw!=null)
		{
			uustaw.maluj(g);
		} else
		if (sstanowiska!=null)
		{
			sstanowiska.maluj(g);
		}
	}//if
	}//void


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
		if (ggranie!=null)
		{
			ggranie.zakonczgranie();
		} else
		if (uustaw!=null)
		{
			uustaw.zakoncz();
		} else
		if (sstanowiska!=null)
		{
			sstanowiska.zakoncz();
		}
		obrcyfr = null;
		finito = true;
		return koniecgry;
	}

	public void run() {
		finito = false;
		Runtime.getRuntime().gc();
		uustaw = new ustaw();

		while (!finito)
		{
			if (ggranie!=null) //jest gra, wiec strzelamy!
			{
				if (uustawienia.podswietlenie) Light.setLightOn();
				ggranie.klawisze();
				ggranie.petlagry();
				if (ggranie.opcja==7)
				{
					czekajnapis=true;
					czekajtekst="lADOWANIE";
					repaint();
					serviceRepaints();
					ggranie.zakonczgranie();
					ggranie=null;
					Runtime.getRuntime().gc();
					sstanowiska = new stanowiska();
				}
			} else
			if (uustaw!=null) //nie ma gry, sa ustawienia nowej gry
			{
				serviceRepaints();
				uustaw.klawisze();
				uustaw.petla();
				if (uustaw.dalej)
				{
					czekajnapis=true;
					czekajtekst="lADOWANIE";
					repaint();
					serviceRepaints();
					uustaw.zakoncz();
					uustaw=null;
					Runtime.getRuntime().gc();
					uustawienia.zapiszustawienia();
					ggranie = new granie();
				}
			} else
			if (sstanowiska!=null) //nie ma gry, sa stanowiska
			{
				serviceRepaints();
				sstanowiska.klawisze();
				sstanowiska.petla();
				if ((sstanowiska.dalej) && (runda<uustawienia.rund))
				{
					czekajnapis=true;
					czekajtekst="lADOWANIE";
					repaint();
					serviceRepaints();
					sstanowiska.zakoncz();
					sstanowiska=null;
					Runtime.getRuntime().gc();
					ggranie = new granie(); //powrot do grania dalej
				}
			} //else 
/*			if (ssklep!=null) //jest sklep, wiec nie gramy, tylko kupujemy itp
			{
				ssklep.klawisze();
				ssklep.petla();
			}*/


			repaint();
			try {
				Thread.currentThread().sleep(20);
			} catch(java.lang.InterruptedException e) { }

			while ((!dziala) && (!finito)) { }
		
		}//while
	}//void

}

