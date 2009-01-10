package util;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

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
		// QUERY -----------------------
		try {
			l = (List) t.queryForList(request);
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
			  /*
			  SerializationContext context = SerializationContext.getSerializationContext();
			 
		      ByteArrayOutputStream bout = new ByteArrayOutputStream();
		      Amf3Output amf3Output = new Amf3Output(context);
		      amf3Output.setOutputStream(bout);
		      amf3Output.writeObject(l);
		      amf3Output.flush();
		      byte[] b = bout.toByteArray();
		      amf3Output.close();		      
		      //FileOutputStream f = new FileOutputStream("Data.dat");
		      
		      FileOutputStream outFile = new FileOutputStream("data.zip");
		      ZipOutputStream zipOut = new ZipOutputStream(outFile);
		      zipOut.putNextEntry(new ZipEntry("0"));
		      zipOut.write(b);
		      zipOut.flush();
		      zipOut.close();
		      */
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
		if (l != null) {
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
		}
		// -------------------------
	}
}
