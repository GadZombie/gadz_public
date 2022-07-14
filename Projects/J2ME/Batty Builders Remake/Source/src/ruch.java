import java.lang.System;
import java.lang.Runnable;
import java.lang.InterruptedException;
import java.lang.String;
import java.lang.Character;
import javax.microedition.lcdui.*;

public class ruch extends Canvas implements Runnable {

	public boolean finito;

    private Image offscreen = null;
    private Image tasmaobr = null;
    private int   height;
    private int   width;
        
	private boolean dziala, przerysuj, odswiezgracza, skonczonerysowanie;

 	private final Image[]    obrkloc,obrgraczg,obrgraczd,obrcyfr,obrbum;
	public static Font czcionkakoniecgry = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

	public int		graczx,graczy, 
						sgraczx,sgraczy, 
						recewgorze,
						graczani,
						bumani, bumx, bumy,
						klaw;
	public int		tasmax,tasmaoffx,tasmaszer;
	public int		kllecx,skllecx, 
						kllecy,skllecy, 
						kllecrodz,
						kllecetap; //etap klocka: 0-spada z gory, 1-trzymany, 2-leci w gore rzucony, 3-odbija sie od gory i spada znowu w dol

	public long		pkt,spkt;
	public int		zycia,szycia,
						etap,
						klockownatasmie, klockowwmurzebrakuje,
						czekajnadalszagre;

	private boolean klleci, koniecgry;

	public int[][] mur, tasma;

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
			if (recewgorze==0) //podnies rece
			{
				recewgorze=15;
				odswiezgracza=true;
			}
			if (kllecetap==1) //rzuc klocka
			{
				kllecetap=2;
				recewgorze=3;
				odswiezgracza=true;
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
			if (recewgorze==0) //podnies rece
			{
				recewgorze=15;
				odswiezgracza=true;
			}
			if (kllecetap==1) //rzuc klocka
			{
				kllecetap=2;
				recewgorze=3;
				odswiezgracza=true;
			}
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

   public void klawisze()
   {
      switch (klaw)
      {
      case 1:
			graczy-=2;
			if (graczy<55) graczy = 55;
			break;
      case 2:
			graczy+=2;
			if (graczy>66) graczy = 66;
         break;
      case 3:
			graczx-=2;
			if (graczx<3) graczx = 3;
         break;
      case 4:
			graczx+=2;
			if (graczx>97) graczx = 97;
         break;
      }

   }

    public void czyscekran(Graphics g) {
		g.setColor(0x00FFFFFF);
	    g.fillRect(0, 0, getWidth(), getHeight());
	}


	Graphics tasmobrgraf;

    public void rysujtasme() { //cala na osobnym obrazku
		tasmobrgraf = tasmaobr.getGraphics();

		tasmobrgraf.setColor(0x00FFFFFF);
		for (int y=0; y<=2; y++ )
			for (int x=0; x<=tasmaszer-1; x++  )
				if (tasma[y][x]>=1)
					tasmobrgraf.drawImage(obrkloc[tasma[y][x]-1], x*6, y*6,Graphics.TOP|Graphics.LEFT);
				else tasmobrgraf.fillRect(x*6, y*6, 6, 5);

	}

    public void rysujtasmyklocek(int y, int x) { //jeden wybrany klocek
		tasmobrgraf = tasmaobr.getGraphics();

		tasmobrgraf.setColor(0x00FFFFFF);
		if (tasma[y][x]>=1)
			tasmobrgraf.drawImage(obrkloc[tasma[y][x]-1], x*6, y*6,Graphics.TOP|Graphics.LEFT);
		else tasmobrgraf.fillRect(x*6, y*6, 6, 5);

	}

    public void pisz(Graphics g, String tekst, int xx, int yy, int dl) {
		if (dl>0)
		{
			while (tekst.length()<dl)
			{
				tekst = "0" + tekst;
			}
		}
		for (int a=0; a<=tekst.length()-1; a++)
			g.drawImage(obrcyfr[ (int) tekst.charAt(a) -48], xx+a*5, yy, Graphics.TOP|Graphics.LEFT);

	}

    public void paint(Graphics g) {
		skonczonerysowanie = false;

		/* to do wirtualnego ekranu */
			Graphics saved = g;
			if (offscreen != null ) g = offscreen.getGraphics();
		/* to do wirtualnego ekranu */

		if (przerysuj) //calosc
		{
			czyscekran(g);

			g.setColor(0x00000000);
		    g.fillRect(0, 19, getWidth(), 3);
		    g.fillRect(0, 47, getWidth(), 3);
		    g.fillRect(0, 72, getWidth(), 9);

			for (int y=0; y<=3; y++ )
				for (int x=0; x<=16; x++  )
					if (mur[y][x]>=1)
					{
						g.drawImage(obrkloc[mur[y][x]-1], x*6, y*6+23,Graphics.TOP|Graphics.LEFT);
					}
			
			rysujtasme();

			przerysuj = false;
			spkt = pkt+1;
			szycia = zycia+1;
			pisz(g, "" + etap, 70,73,1);
		} 

		/* FAZA WYMAZYWANIA */

		//klocki spadajace
		if ((skllecx!=kllecx) || (skllecy!=kllecy))
		{
			g.setColor(0x00ffffff);
		    g.fillRect(skllecx, skllecy, 5, 5);
		}

		//rozprysk
		if (bumani>=1)
		{
			g.setColor(0x00FFFFFF);
		    g.fillRect(bumx-10, bumy-14, 21, 15);
		}

		//gracz
		if ((graczx!=sgraczx) || (graczy!=sgraczy) || (odswiezgracza))
		{
			g.setColor(0x00FFFFFF);
		    g.fillRect(sgraczx-3, sgraczy-4, 7, 9);
		}
		
		/* /FAZA WYMAZYWANIA */

		/* FAZA ODSWIEZANIA WYMAZANYCH FRAGMENTOW EKRANU: MURU I KRESEK POZIOMYCH */

		//klocki spadajace
		if ((skllecx!=kllecx) || (skllecy!=kllecy))
		{
			if ((skllecy>=15) && (skllecy<=21))
			{
				g.setColor(0x00000000);
			    g.fillRect(0, 19, getWidth(), 3);
			}
			if ((skllecy>=19) && (skllecy<=45))
			{
				int x1,y1,x2,y2;
				x1 = (int) skllecx / 6;
				x2 = (int) (skllecx+4) / 6;
				y1 = (int) (skllecy-23) / 6;
				y2 = (int) (skllecy-19) / 6;
				g.setColor(0x00FFFFFF);
				for (int y=y1; y<=y2; y++ )
					for (int x=x1; x<=x2; x++ )
						if ((y>=0) && (y<=3) && (x>=0) && (x<=16))
						{
							if (mur[y][x]>=1)
								g.drawImage(obrkloc[mur[y][x]-1], x*6, y*6+23,Graphics.TOP|Graphics.LEFT);
							else g.fillRect(x*6,y*6+23,6,6);
						}
			}
			if ((skllecy>=43) && (skllecy<=49))
			{
				g.setColor(0x00000000);
			    g.fillRect(0, 47, getWidth(), 3);
			}
		}

		//rozprysk
		if (bumani>=1)
		{
			if ((bumy>=23) && (bumy<=66))
			{
				int x1,y1,x2,y2;
				x1 = (int) (bumx-10) / 6;
				x2 = (int) (bumx+10) / 6;
				y1 = (int) (bumy-39) / 6;
				y2 = (int) (bumy-23) / 6;
				g.setColor(0x00FFFFFF);
				for (int y=y1; y<=y2; y++ )
					for (int x=x1; x<=x2; x++ )
						if ((y>=0) && (y<=3) && (x>=0) && (x<=16))
						{
							if (mur[y][x]>=1)
								g.drawImage(obrkloc[mur[y][x]-1], x*6, y*6+23,Graphics.TOP|Graphics.LEFT);
							else g.fillRect(x*6,y*6+23,6,6);
						}
			}
			if ((bumy>=46) && (bumy<=64))
			{
				g.setColor(0x00000000);
			    g.fillRect(0, 47, getWidth(), 3);
			}
		}

		
		/* /FAZA ODSWIEZANIA WYMAZANYCH FRAGMENTOW EKRANU: MURU I KRESEK POZIOMYCH */
		
		/* FAZA RYSOWANIA */

		//tasma u gory
		g.drawImage(tasmaobr, -tasmax*6-tasmaoffx, 1,Graphics.TOP|Graphics.LEFT);
		g.drawImage(tasmaobr, -tasmax*6-tasmaoffx+tasmaobr.getWidth(), 1,Graphics.TOP|Graphics.LEFT);

		//klocki spadajace
		if ((klleci) && ((skllecx!=kllecx) || (skllecy!=kllecy)))
		{
			g.drawImage(obrkloc[kllecrodz-1], kllecx, kllecy,Graphics.TOP|Graphics.LEFT);
			skllecx=kllecx;
			skllecy=kllecy;
		}

		//rozprysk
		if (bumani>1)
		{
			g.drawImage(obrbum[5-bumani], bumx-10, bumy-14, Graphics.TOP|Graphics.LEFT);
		}

		//gracz
		if ((!koniecgry) && (czekajnadalszagre==0) && ((graczx!=sgraczx) || (graczy!=sgraczy) || (odswiezgracza)))
		{
			if (recewgorze==0)
				g.drawImage(obrgraczg[0], graczx-3, graczy-4, Graphics.TOP|Graphics.LEFT);
				else g.drawImage(obrgraczg[1], graczx-3, graczy-4, Graphics.TOP|Graphics.LEFT);
			g.drawImage(obrgraczd[graczani], graczx-3, graczy+3, Graphics.TOP|Graphics.LEFT);
			sgraczy=graczy;
			sgraczx=graczx;
			odswiezgracza = false;
		}

		/* /FAZA RYSOWANIA */


		//wyniki
		if (pkt!=spkt)
		{
			pisz(g, "" + pkt, 2,73,6);
			spkt=pkt;
		}

		if (zycia!=szycia)
		{
			pisz(g, "" + zycia, 50,73,1);
			szycia=zycia;
		}

		if (koniecgry)
		{
			g.setColor(0x00000000);
			g.setFont(czcionkakoniecgry);
			g.drawString(" KONIEC GRY ", 50,36, Graphics.TOP | Graphics.HCENTER);
		}

		/* to do wirtualnego ekranu */
			if (g != saved) saved.drawImage( offscreen, 0, 0, Graphics.LEFT | Graphics.TOP );
		/* to do wirtualnego ekranu */

		skonczonerysowanie = true;
	}

	void nowyetap() {
		int rozstep;
		etap++;
		graczy=60;
		graczx=50;
		sgraczy=graczy;
		sgraczx=graczx;
		graczani=0;
		recewgorze=0;
		odswiezgracza=true;
		tasmax=0;
		tasmaoffx=0;
		klleci=false;

		bumani=0;

		rozstep=etap-1;
		if (rozstep>3)
			rozstep=3;

		for (int y=0; y<=3; y++ )
			for (int x=0; x<=16; x++  )
			mur[y][x]=y+1;

		for (int y=0; y<=3; y++ )
			for (int x=3+y-rozstep; x<=13-y+rozstep; x++  )
			mur[y][x]=0;

		for (int y=0; y<=2; y++ )
			for (int x=0; x<=tasmaszer-1; x++  )
			{
			tasma[y][x]=(byte) Math.abs(gra.random.nextInt() % 4)+1;
			}

		klockownatasmie = 3*tasmaszer;

		klockowwmurzebrakuje = 32+(etap-1)*8;

		czekajnadalszagre=0;

		spkt=pkt-1;
		szycia=zycia+1;
		przerysuj = true;
		dziala = true;
		repaint();
	}

	protected ruch() {
		finito = false;

        height = getHeight();
        width = getWidth();

        offscreen = Image.createImage( width, height );
		tasmaszer=20;
        tasmaobr = Image.createImage( tasmaszer*6, 17 );
		
		obrkloc = new Image[4];
		obrgraczg = new Image[2];
		obrgraczd = new Image[3];
		obrcyfr = new Image[10];
		obrbum = new Image[4];

		try
		{
			obrkloc[0] = Image.createImage("/kloc1.png");
			obrkloc[1] = Image.createImage("/kloc2.png");
			obrkloc[2] = Image.createImage("/kloc3.png");
			obrkloc[3] = Image.createImage("/kloc4.png");
			obrgraczg[0] = Image.createImage("/graczg1.png");
			obrgraczg[1] = Image.createImage("/graczg2.png");
			obrgraczd[0] = Image.createImage("/graczd1.png");
			obrgraczd[1] = Image.createImage("/graczd2.png");
			obrgraczd[2] = Image.createImage("/graczd3.png");
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
			obrbum[0] = Image.createImage("/bum1.png");
			obrbum[1] = Image.createImage("/bum2.png");
			obrbum[2] = Image.createImage("/bum3.png");
			obrbum[3] = Image.createImage("/bum4.png");
		}
		catch (java.io.IOException e)	{	}
		mur = new int[4][17];
		tasma = new int[3][tasmaszer];

		pkt = 0;
		zycia = 5;
		etap = 0;
		koniecgry=false;
		nowyetap();
	}

	void petlagry() {
		//opuszczanie rak gracza
		if (recewgorze>0)
		{
			if (kllecetap!=1)
			{
				recewgorze--;
				if (recewgorze == 0)
					odswiezgracza = true;
			}
		}
		if ((graczx==sgraczx) && (graczy==sgraczy))
		{
			graczani=0;
			odswiezgracza=true;
		} else {
			graczani++;
			if (graczani>=3) graczani=1;
		}

		if (bumani>0)
		{
			bumani--;
		}
		//przesuwanie tasmy u gory
		tasmaoffx++;
		if (tasmaoffx>=6)
		{
			tasmax++;
			if (tasmax>=tasmaszer)
				tasmax=0;
			tasmaoffx=0;
		}

		if (czekajnadalszagre>0)
		{
			czekajnadalszagre--;
			if (czekajnadalszagre==0) odswiezgracza=true;
		}


		//spadajace klocki
		if ((!koniecgry) && (czekajnadalszagre==0) && (!klleci) && (klockownatasmie>0) && (Math.abs(gra.random.nextInt() % 4)==0))
		{ //spusc nowy klocek z gory
			int sx,sx2,prob;

			prob = 20;
			while (prob>0)
			{
				sx = Math.abs(gra.random.nextInt() % tasmaszer);
				for (int sy=0; sy<=2; sy++)
				{
					sx2 = sx-tasmax;
					if (sx2<2)
					{
						sx2+=tasmaszer;
					}
					if ((tasma[sy][sx]!=0) && 
						(sx2*6-tasmaoffx>=2) && (sx2*6-tasmaoffx<=95))
					{
						klleci=true;
						kllecetap=0;
						kllecrodz = tasma[sy][sx];
						kllecx=sx2*6-tasmaoffx;
						kllecy=sy*6+1;
						skllecx=kllecx;
						skllecy=kllecy;
						tasma[sy][sx] = 0;
						rysujtasmyklocek(sy,sx);
						prob=0;
						klockownatasmie--;
						break;
					}
				}
				prob--;
			}//while

			if ((!klleci) && (klockownatasmie==0))
			{
				koniecgry=true;
			}

		}//if
		if (klleci)
		{ //obsluga spadajacego klocka
			switch (kllecetap)
			{
			case 0:	//etap pierwszy - klocek tylko spada
				kllecy++;
				if (etap>=2) kllecy++;
				if (kllecy>=67)
				{
					klleci=false;
					bumx=kllecx+2;
					bumy=kllecy+2;
					bumani=5;
				}
				if ((kllecy>=graczy-10) && (kllecy<=graczy-8) && (kllecx>=graczx-6) && (kllecx<=graczx) && (recewgorze>=1))
				{
					kllecetap=1;
					pkt+=10;
				}
				break;
			case 1:  //etap drugi - gracz trzyma klocka w rekach
				kllecx = graczx-2;
				kllecy = graczy-9;
				break;
			case 2:  //etap trzeci - klocek leci w gore
				kllecy-=2;
				if (kllecy<=20)
				{
					kllecetap=3;
				}
				break;
			case 3:	//etap czwarty - klocek znow spada i jak moze, to zostaje w murze
				kllecy+=2;

				if ((kllecy>=22) && (kllecy<=42) && /*((kllecy-22) % 6 <= 1) && */
					(mur[(kllecy-23) / 6][(kllecx+3) / 6]==0) && (((kllecy-23) / 6==3) || (mur[(kllecy-23) / 6+1][(kllecx+3) / 6]!=0))
					)
				{ //klocek wchodzi w mur
					pkt+=50;
					if (kllecrodz == 1+(kllecy-23) / 6)
					{
						pkt+=80-kllecrodz*5;
					}
					mur[(kllecy-22) / 6][(kllecx+3) / 6] = kllecrodz;
					kllecx = ((kllecx+3) / 6)*6;
					kllecy = ((kllecy-23) / 6)*6+23;
					klleci=false;
					klockowwmurzebrakuje--;
				}

				if (kllecy>=67)
				{
					klleci=false;
					bumx=kllecx+2;
					bumy=kllecy+2;
					bumani=5;
				}
				break;

			}//switch
			if (
				((kllecetap==0) || (kllecetap==3)) &&
				(
					(kllecy>=graczy-9) && (kllecy<=graczy+4) && (kllecx>=graczx-6) && (kllecx<=graczx+3) &&
						(
							(recewgorze==0) || 
							(!((kllecy>=graczy-10) && (kllecy<=graczy-8) && (kllecx>=graczx-6) && (kllecx<=graczx)))
						)
				)
			)
			{
					klleci=false;
					bumx=kllecx+2;
					bumy=kllecy+2;
					bumani=5;

					if (zycia>0)
					{ zycia--; } else { koniecgry=true; }
					graczy=60;
					graczx=50;
					graczani=0;
					recewgorze=0;
					odswiezgracza=true;
					czekajnadalszagre=20;

			}
		}//if

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

	public void run() {
		finito = false;
		//repaint();
		//while (!skonczonerysowanie) {}

		while (!finito)
		{
			klawisze();

			petlagry();

			if ((klockowwmurzebrakuje==0) && (!koniecgry))
			{
				dziala=false;
				nowyetap();
			}

			repaint();
			try {
				Thread.currentThread().sleep(60);
			} catch(java.lang.InterruptedException e) { }

			while ((!dziala) && (!finito)) { }
		
		}//while
	}//void

}

