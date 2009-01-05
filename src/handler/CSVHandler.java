package handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;

public class CSVHandler implements IMessageHandler {

	public void handleMessage(IMessage message) {
		CsvReader csread = null;
		String st = (String) message.getData();
		String[] values = st.split("##");
		char delimiter = values[1].charAt(0);
		try {
			csread = new CsvReader(values[0],delimiter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List l = new ArrayList(0);
		
		try {
			while(csread.readRecord()){
				l.add(csread.getValues());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		csread.close();
		
	}

}
