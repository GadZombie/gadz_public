import javax.microedition.rms.*;
import java.io.*;

class ustawienia {
	public byte rund;
	public byte graczy,silawiatru,grawitacja,gorzystosc;
	public byte[]	graczrodz,	//0:gracz, 1:komputer
					graczdruz;	//druzyna
	public boolean strzalki,mapka,zmiennywiatr,wskaznikiekranu,podswietlenie;

	ustawienia() {
		rund = 5;
		graczy = 2;
		strzalki=true;
		mapka=true;			
		zmiennywiatr=false;
		wskaznikiekranu=true;
		silawiatru=20;
		grawitacja = 32;
		gorzystosc = 11;
		graczrodz = new byte[9];
		graczdruz = new byte[9];
		for (byte a=1; a<9; a++) graczrodz[a]=1;
		graczrodz[0]=0;
		for (byte a=0; a<9; a++) graczdruz[a]=a;

		wczytajustawienia();

	}

	public void wczytajustawienia() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("tankzustaw", true);
			if (recordStore.getNumRecords() > 0) {
				byte[] record = recordStore.getRecord(1);
				DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record, 0, record.length));
				
				rund			= istream.readByte();
				graczy			= istream.readByte();
				for (int a=0; a<9; a++)
				{
					graczrodz[a] = istream.readByte();
					graczdruz[a] = istream.readByte();
				}
				
				strzalki		= istream.readBoolean();
				mapka			= istream.readBoolean();
				zmiennywiatr	= istream.readBoolean();
				silawiatru		= istream.readByte();
				wskaznikiekranu = istream.readBoolean();
				podswietlenie	= istream.readBoolean();
				grawitacja		= istream.readByte();
				gorzystosc		= istream.readByte();
			} else {
				ByteArrayOutputStream bstream = new ByteArrayOutputStream(11);
				DataOutputStream ostream = new DataOutputStream(bstream);

				ostream.writeByte(rund);
				ostream.writeByte(graczy);
				for (int a=0; a<9; a++)
				{
					ostream.writeByte(graczrodz[a]);
					ostream.writeByte(graczdruz[a]);
				}
				ostream.writeBoolean(strzalki);
				ostream.writeBoolean(mapka);
				ostream.writeBoolean(zmiennywiatr);
				ostream.writeByte(silawiatru);
				ostream.writeBoolean(wskaznikiekranu);
				ostream.writeBoolean(podswietlenie);
				ostream.writeByte(grawitacja);
				ostream.writeByte(gorzystosc);

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

	public void zapiszustawienia() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("tankzustaw", true);
			ByteArrayOutputStream bstream = new ByteArrayOutputStream(11);
			DataOutputStream ostream = new DataOutputStream(bstream);

			ostream.writeByte(rund);
			ostream.writeByte(graczy);
			for (int a=0; a<9; a++)
			{
				ostream.writeByte(graczrodz[a]);
				ostream.writeByte(graczdruz[a]);
			}
			ostream.writeBoolean(strzalki);
			ostream.writeBoolean(mapka);
			ostream.writeBoolean(zmiennywiatr);
			ostream.writeByte(silawiatru);
			ostream.writeBoolean(wskaznikiekranu);
			ostream.writeBoolean(podswietlenie);
			ostream.writeByte(grawitacja);
			ostream.writeByte(gorzystosc);

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


}

