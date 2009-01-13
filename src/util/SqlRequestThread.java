package util;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class SqlRequestThread extends Thread {
	
	private String request;
	private JdbcTemplate t;
	private final Log logger = LogFactory.getLog(SqlRequestThread.class);
	
	public SqlRequestThread (String request, JdbcTemplate t){
		super();
		this.t=t;
		this.request=request;
	}
	
	public void run(){
		List l = null;
		SqlRowSet rs = null;
		// QUERY -----------------------
		try {
			t.setFetchSize(Integer.MIN_VALUE);
			 long debut = System.currentTimeMillis();
			 // ----------------------------------
			 rs = t.queryForRowSet(request);
			 /*
			 int nbRow = 0;
			 
			 while (rs.next()){
				 rs.
				 
				 nbRow++;
				 if (nbRow==100){
					 //TO DISC
				 }
			 }
			 */
			 
			 
			 
			 
			 l = (List) t.queryForList(request);	 
			 
			 // ------------------------------------
			
			long fin = System.currentTimeMillis();
			System.out.println("Load: "+(fin-debut));
			try {
				Messenger.sendMessage("sqlInfo", "Operation successful");
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		} catch (Exception e) {
			try {
				Messenger.sendMessage("sqlInfo", "ERROR : "
								+ e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
		// -------------------------
		// SEND LIST ---------------
		  try {
			  long debut = System.currentTimeMillis();
			  System.out.println(debut);
			  SerializationContext context = SerializationContext.getSerializationContext();
			 
		      ByteArrayOutputStream bout = new ByteArrayOutputStream();
		      Amf3Output amf3Output = new Amf3Output(context);
		      amf3Output.setOutputStream(bout);
		      amf3Output.writeObject(l);
		      amf3Output.flush();
		      byte[] b = bout.toByteArray();
		      amf3Output.close();		      
		      //FileOutputStream f = new FileOutputStream("Data.dat");
		      
		      FileOutputStream outFile = new FileOutputStream("data.gz");
		      GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
//		      zipOut.setLevel(9);
	//	      zipOut.setMethod(ZipOutputStream.DEFLATED);
//		      zipOut.putNextEntry(new ZipEntry("0"));
		      zipOut.write(b);
		      zipOut.flush();
		      zipOut.close();
		      long fin = System.currentTimeMillis();
		      try{
		    	  Messenger.sendMessage("load", "OK'");
		      }catch(Exception e){
		    	  e.printStackTrace();
		      }
				System.out.println("Compression: "+(fin-debut));
//		      FileOutputStream fout = new FileOutputStream("DATA.dat");
//		      byte[] b = bout.toByteArray();
//		      fout.write(b);
//		      ObjectOutputStream oos = new ObjectOutputStream(fout);
//		      oos.writeObject(l.toArray());
//		      oos.close();
		      
		      /*
		      ZipOutputStream zos = new ZipOutputStream(bout);
		      zos.setLevel(9);
		      zos.setMethod(ZipOutputStream.DEFLATED);
		      
		      ZipEntry ze = new ZipEntry();
		      byte[] b = bout.toByteArray();
		      ze.setSize(b.length);
		      zos.putNextEntry(ze);
		      zos.write(b);
		      zos.closeEntry();
		      zos.flush();
		      zos.close();
		      */
		      }
		   catch (Exception e) { e.printStackTrace(); }
/*		if (l != null) {
			long debut = System.currentTimeMillis();
			System.out.println(debut);
			try {
					Messenger.sendMessage("sqlResult", l);
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
				long fin = System.currentTimeMillis();
				System.out.println(fin);
				System.out.println("temps: "+(fin-debut));
		}*/
		// -------------------------
	}
	public Page getCompanies(final int pageNo, final int pageSize) throws SQLException {
        PaginationHelper ph = new PaginationHelper();
        return ph.fetchPage(
                t,
                request,
                request,
                null,
                pageNo,
                pageSize,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int i) throws SQLException {
                        return new Object 
                    }
                }
        );

    }
}
