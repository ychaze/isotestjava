package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class SqlRequestThread extends Thread {
	// CONSTANT DEFINE THE NUMBER OF ROWS PER ARRAY WHEN WE STREAM FROM DATABASE
	private final static int STREAM_LENGTH = 10000;
	// REQUEST TO BE EXECUTED
	private String request;
	// USEFULL OBJECT FROM SPRING
	private JdbcTemplate t;
	// FILE LOGGER
	private final Log logger = LogFactory.getLog(SqlRequestThread.class);

	// Constructor
	public SqlRequestThread(String request, JdbcTemplate t) {
		super();
		this.t = t;
		this.request = request;
	}
	// THREAD CONTENT
	public void run() {

		PreparedStatement stat;
		try {
			// CREATE AND EXECUTE QUERY ------------
			stat = t.getDataSource().getConnection().prepareStatement(request,
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			if (t.getDataSource().getConnection().getMetaData().getDriverName()
					.toUpperCase().contains("MYSQL"))
				stat.setFetchSize(Integer.MIN_VALUE);
			else
				stat.setFetchSize(1500);

			ResultSet results = stat.executeQuery();
			// -------------------------------------------
			
			// Grab the number of columns of the table
			int numberColum = results.getMetaData().getColumnCount();
			
			// Send message to air to notice that we start to send data
			try {
				Messenger.sendMessage(ApplicationConstants.SQL_START, null);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			
			// Prepare for amf serialization
			SerializationContext context = SerializationContext
			.getSerializationContext();
			
			// Index for the array (row index)
			int nb = 1;

			// Declare the data array
			String[][] data;
			
			// Instantiate the array with correct length
			data = new String[STREAM_LENGTH][numberColum];
			
			// first entry in the array is the header column
			for (int i = 0; i < numberColum; i++) {
				data[0][i] = results.getMetaData().getColumnName(i + 1);
			}
			// Boolean to know if the last rows has been sent
			boolean isSended = false;
			
			// Copying data
			while (results.next()) {
				isSended = false;
				// Copy the content of the row in the array
				for (int j = 0; j < numberColum; j++) {
					data[nb % STREAM_LENGTH][j] = results.getString(j + 1);
				}
				// Test if the array is full
				if (nb == STREAM_LENGTH-1) {
					// and serialize
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					Amf3Output amf3Output = new Amf3Output(context);
					amf3Output.setOutputStream(bout);
					amf3Output.writeObject(data);
					amf3Output.flush();
					byte[] b = bout.toByteArray();
					amf3Output.close();
					String filename = "" + System.nanoTime();
					File path = new File(filename + ".data");
					FileOutputStream outFile = new FileOutputStream(path);
					GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
					zipOut.write(b);
					zipOut.flush();
					zipOut.close();
					
					// Ok now sending the array to flex by merapi
					Messenger.sendMessage(ApplicationConstants.SQL_RESULT,
								path.getAbsoluteFile().toString());
					// Create a new array and the array has no reference on it, so it can be collected by the GC! Hope for this.
					data = new String[STREAM_LENGTH][numberColum];
					nb=-1;
					isSended=true;
				}
				nb++;
			}
			
			if(!isSended){
				String[][] lastData = new String [nb][numberColum];
				for (int i = 0 ; i < nb ; i++) {
					lastData[i] = data[i].clone();
					
				}
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				Amf3Output amf3Output = new Amf3Output(context);
				amf3Output.setOutputStream(bout);
				amf3Output.writeObject(lastData);
				amf3Output.flush();
				byte[] b = bout.toByteArray();
				amf3Output.close();
				String filename = "" + System.nanoTime();
				File path = new File(filename + ".data");
				FileOutputStream outFile = new FileOutputStream(path);
				GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
				zipOut.write(b);
				zipOut.flush();
				zipOut.close();
				Messenger.sendMessage(ApplicationConstants.SQL_RESULT, path.getAbsoluteFile().toString());
			}
			
			
			
			// Now saying to flex all is done
			Messenger.sendMessage(ApplicationConstants.SQL_STOP, "success");
			
			// --------------------------------------------------------------------
		} catch (Exception e2) {
			logger.error(e2.getMessage());
			try {
				Messenger.sendMessage(ApplicationConstants.SQL_INFO, "ERROR : " + e2.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

}
