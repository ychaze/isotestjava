package manager;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.ApplicationConstants;
import util.Messenger;

public class ExcelManager {

	private final static int STREAM_INTERVAL = 10;
	private final Log logger = LogFactory.getLog(ExcelManager.class);
	public void getNbSheets(String path){
		Workbook wb;
		try {
			wb = Workbook.getWorkbook(new File(path));
			String[] names = new String[wb.getNumberOfSheets()];
			for ( int i = 0 ; i < wb.getNumberOfSheets() ; i++) {
				names[i] = wb.getSheet(i).getName();
			}
			Messenger.sendMessage(ApplicationConstants.NAME_SHEETS_EXCEL,names);
		} catch (BiffException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	/**
	 *  Grab the data in the Excel file
	 * @param info String path of the file and sheet to operate
	 */
	public void process(String info) {

		try {
			
			//Say to flex that we begin...
			
			Messenger.sendMessage(ApplicationConstants.SQL_START, null);
			
			// Split to  [path , sheet]
			String [] values = info.split("##");

			// get the excel workbook
			Workbook wb = Workbook.getWorkbook(new File(values[0]));

			System.out.println(wb.getSheets()[0].getName()+"\n");
			// get the specified sheet
			Sheet s = wb.getSheet(values[1]);

			// get rows
			Cell[] row = null;

			// create correct array
			String [][] data;

			// Prepare for streaming
			if (s.getRows()>STREAM_INTERVAL)
			{
				data = new String [STREAM_INTERVAL][s.getRow(0).length];	
			}
			else
			{
				data = new String [s.getRows()][s.getRow(0).length];	
			}

			//Print for test
			boolean isSended = true;
			int numberColumn = s.getRow(0).length;
			for (int i = 1 ; i <= s.getRows() ; i++)
			{
				isSended = false;
				// grab the new row
				row = s.getRow(i-1);

				// Add the row in the array
				// limit the column to the number of columns of first row
				if (row.length > 0)
				{
					int numberCase = s.getRow(i-1).length;
					//for each cells of the line i in the number of columns
					for (int j = 0; j < numberCase && j<numberColumn; j++)
					{
						data[(i-1)%STREAM_INTERVAL][j] = row[j].getContents();
					}
				}

				// if we create 1000 rows we send the array create new one and continue
				if (i%STREAM_INTERVAL == 0 )
				{
					//affiche(data);
					Messenger.sendMessage(ApplicationConstants.EXCEL_RESULT, data);
					isSended = true;
					// New array hope for the garbage collector to collect the old object to prevent memory overflow
					if (s.getRows() - i >= STREAM_INTERVAL)
						data = new String [STREAM_INTERVAL][s.getRow(0).length];
					else
						data = new String [s.getRows() - i][s.getRow(0).length];
				}
			}
			if(!isSended){
				Messenger.sendMessage(ApplicationConstants.EXCEL_RESULT, data);
			}
			// SAY TO FLEX THAT IT HAS THE COMPLETE LIST OF DATA
			Messenger.sendMessage(ApplicationConstants.EXCEL_STOP, null);
		}
		catch (BiffException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		catch (Exception e){
			logger.error(e.getMessage());
		}
	}
}
