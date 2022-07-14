import javax.microedition.rms.*;
import java.io.*;

class ustawienia {
	public int etappoc,etapmax,etapjest;
	public boolean wibrator;

	ustawienia() {
		etappoc = 1; //etap, od ktorego sie zaczyna gre
		etapmax = 1; //etap, do ktorego najdalej gracz doszedl
		etapjest = 19; //liczba etapow
		wibrator = true;

		wczytajustawienia();
		etapmax=etapjest;

	}

	public void wczytajustawienia() {
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore("rx800bustaw", true);
			if (recordStore.getNumRecords() > 0) {
				byte[] record = recordStore.getRecord(1);
				DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record, 0, record.length));
				etappoc = istream.readInt();
				etapmax = istream.readInt();
				wibrator = istream.readBoolean();
			} else {
				ByteArrayOutputStream bstream = new ByteArrayOutputStream(11);
				DataOutputStream ostream = new DataOutputStream(bstream);
				ostream.writeInt(etappoc);
				ostream.writeInt(etapmax);
				ostream.writeBoolean(wibrator);
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
			recordStore = RecordStore.openRecordStore("rx800bustaw", true);
			ByteArrayOutputStream bstream = new ByteArrayOutputStream(11);
			DataOutputStream ostream = new DataOutputStream(bstream);
			ostream.writeInt(etappoc);
			ostream.writeInt(etapmax);
			ostream.writeBoolean(wibrator);
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

