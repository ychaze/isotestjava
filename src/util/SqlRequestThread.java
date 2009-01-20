package util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


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
		    
		PreparedStatement stat;
		try {
			stat = t.getDataSource().getConnection().prepareStatement(request,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			if(t.getDataSource().getConnection().getMetaData().getDriverName().toUpperCase().contains("MYSQL"))
				stat.setFetchSize(Integer.MIN_VALUE);
			else
				stat.setFetchSize(1500);
		    ResultSet results = stat.executeQuery();

		    
		    int numberColum = results.getMetaData().getColumnCount();
		    
		    
			String[] line;
			String[][] vec = new String[1000][numberColum];
			int i = 1;
			//String[] ColumnName = new String[numberColum];
		     for(i=1;i<=numberColum;i++){
		    	 vec[0][i-1]=results.getMetaData().getColumnName(i);
				}
			/*try
			{
				Messenger.sendMessage("sqlResultStart",null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
*/
			/* ByteArrayOutputStream bout = new ByteArrayOutputStream();
			 DataOutputStream sortie=new DataOutputStream(bout);

			 SerializationContext context = SerializationContext.getSerializationContext();*/
			int j=1;
			while ((results.next())&& (j<1000))
			{line = new String[numberColum];
				for(i=1;i<=numberColum;i++)
					vec[j][i-1]=results.getString(i);
				 //vec.add(line);
				 
				 if (j==999)
				 {
					 try
					 {
						/* Amf3Output amf3Output = new Amf3Output(context);
					     amf3Output.setOutputStream(bout);
					    amf3Output.writeObject(vec);
					    amf3Output.flush();
					    byte[] b = bout.toByteArray();
					    amf3Output.close();*/
					    
						 Messenger.sendMessage("sqlResult",vec);
						 //vec.removeAllElements();
					 }
					 catch (Exception e)
					 {
						 e.printStackTrace();
						 logger.error(e.getMessage());
					 }
					 
				}
				 j++;
			}			
			/*try
			{
				Messenger.sendMessage("sqlResultStop",null);
			}
			catch (Exception e)
			{							
				e.printStackTrace();
				logger.error(e.getMessage());
			}	*/	    
		}
		catch (Exception e2)
		{				
			logger.error(e2.getMessage());
			try
			{
				Messenger.sendMessage("sqlInfo", "ERROR : "+e2.getMessage());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
}
/*import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

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

		PreparedStatement stat;
		try {
			stat = t.getDataSource().getConnection().prepareStatement(request,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			if(t.getDataSource().getConnection().getMetaData().getDriverName().toUpperCase().contains("MYSQL"))
				stat.setFetchSize(Integer.MIN_VALUE);
			else
				stat.setFetchSize(1500);
			
			long deb = System.currentTimeMillis();
		    ResultSet results = stat.executeQuery();

		    
		    int numberColum = results.getMetaData().getColumnCount();
			int i = 1;
			String[] line = new String[numberColum];
			try
			{
				Messenger.sendMessage("sqlResultStart",null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		//	 ---------------------------------------------------------------AMF
			List l = null;
			// QUERY -----------------------
			try {
				t.setFetchSize(Integer.MIN_VALUE);
				 long debut = System.currentTimeMillis();
				 // ----------------------------------
				 //rs = t.queryForRowSet(request);
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
			  SerializationContext context = SerializationContext.getSerializationContext();
			 
		      ByteArrayOutputStream bout = new ByteArrayOutputStream();
		      Amf3Output amf3Output = new Amf3Output(context);
		      amf3Output.setOutputStream(bout);
		      amf3Output.writeObject(l);
		      amf3Output.flush();
		      byte[] b = bout.toByteArray();
		      amf3Output.close();		      
		      //FileOutputStream f = new FileOutputStream("Data.dat");

		      File path=new File("data.gz");
		      FileOutputStream outFile = new FileOutputStream(path);
		      GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
//		      zipOut.setLevel(9);
	//	      zipOut.setMethod(ZipOutputStream.DEFLATED);
		//      zipOut.putNextEntry(new ZipEntry("0"));
		      
		      zipOut.write(b); 
		      zipOut.flush();
		      zipOut.close();
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			// --------------------------------------------------------------------
			
			/*
			// INITIALIZE FOR WRITING TO FILE
			 SerializationContext context = SerializationContext.getSerializationContext();
			 Character c = '\n';
		    
		     File path=new File("data.gz");
		      FileOutputStream outFile = new FileOutputStream(path);
		      GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
			// WORK ON EACH ROW
		     
		     Vector<String[]> list= new Vector<String[]>();
		    
		     String[] ColumnName = new String[numberColum];
		     for(i=1;i<=numberColum;i++){
		    	 ColumnName[i-1]=results.getMetaData().getColumnName(i);
				}
		     
		  // NOW WRITING TO FILE
		     
			 ByteArrayOutputStream bout = new ByteArrayOutputStream();
			 DataOutputStream sortie=new DataOutputStream(bout);
			 sortie.writeBytes("<sessions>"+'\n');
			 byte[] b;
			 b= bout.toByteArray();
			 
		      zipOut.write(b);
		      zipOut.flush();
		     int j = 0;
		     
			while (results.next())
			{ j++;
			String[] line = new String[numberColum];
				// CREATE THE ROW ARRAY
				for(i=1;i<=numberColum;i++){
					line[i-1]=results.getString(i);
				}
				 bout = new ByteArrayOutputStream();
				  sortie=new DataOutputStream(bout);
				
			 
				
				 sortie.writeBytes("<session");
				 for(i=1;i<=numberColum;i++){
					 sortie.writeBytes(' '+ColumnName[i-1]+"="+'"'+line[i-1]+'"');
					}
<<<<<<< HEAD:src/util/SqlRequestThread.java
				 sortie.writeBytes("/>"+'\n');
			     Amf3Output amf3Output = new Amf3Output(context);
=======
				 sortie.writeBytes("/>"+'\n');/*
			     /*Amf3Output amf3Output = new Amf3Output(context);
>>>>>>> origin/master:src/util/SqlRequestThread.java
			     amf3Output.setOutputStream(bout);
			    amf3Output.writeObject(list);
			    list.clear();
			    
			    //amf3Output.flush();
			    /*amf3Output.writeChars("\n");
<<<<<<< HEAD:src/util/SqlRequestThread.java
			    amf3Output.flush();*/
/*
=======
			    amf3Output.flush();
>>>>>>> origin/master:src/util/SqlRequestThread.java
				  b= bout.toByteArray();
			     // amf3Output.close();	
			      
			      zipOut.write(b);
			      zipOut.flush();
			}
			 
			sortie.writeBytes("</sessions>"+'\n');
			b = bout.toByteArray();
			 zipOut.write(b);
			 zipOut.flush();
		      zipOut.close();
		      long fin = System.currentTimeMillis();
		      System.out.println("Time total:"+(fin-deb));*/
			try
			{
				Messenger.sendMessage("sqlResult",path.getAbsolutePath());
			}
			catch (Exception e)
			{							
				e.printStackTrace();
				logger.error(e.getMessage());
			}	
		}
		catch (Exception e2)
		{				
			logger.error(e2.getMessage());
			try
			{
				Messenger.sendMessage("sqlInfo", "ERROR : "+e2.getMessage());

			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		}catch (Exception e){}
		
		
		
//		List l = null;
//		SqlRowSet rs = null;
//		// QUERY -----------------------
//		try {
//			t.setFetchSize(Integer.MIN_VALUE);
//			 long debut = System.currentTimeMillis();
//			 // ----------------------------------
//			 //rs = t.queryForRowSet(request);
//			 /*
//			 int nbRow = 0;
//			 
//			 while (rs.next()){
//				 rs.
//				 
//				 nbRow++;
//				 if (nbRow==100){
//					 //TO DISC
//				 }
//			 }
//			 */
//			 
//
//			 l = (List) t.queryForList(request);	 
//
//			 // ------------------------------------
//			
//	 		long fin = System.currentTimeMillis();
//			System.out.println("Load: "+(fin-debut));
//			try {
//				Messenger.sendMessage("sqlInfo", "Operation successful");
//			} catch (Exception e1) {
//				e1.printStackTrace();
//				logger.error(e1.getMessage());
//			}
//		} catch (Exception e) {
//			try {
//				Messenger.sendMessage("sqlInfo", "ERROR : "
//								+ e.getMessage());
//			} catch (Exception e1) {
//				e1.printStackTrace();
//				logger.error(e1.getMessage());
//			}
//		}
//		// -------------------------

//				
//				try {
//					Messenger.sendMessage("sqlResult", path.getAbsolutePath());
//				} catch (Exception e1) {
//					e1.printStackTrace();
//					logger.error(e1.getMessage());
//				}
//				
////		      FileOutputStream fout = new FileOutputStream("DATA.dat");
////		      byte[] b = bout.toByteArray();
////		      fout.write(b);
////		      ObjectOutputStream oos = new ObjectOutputStream(fout);
////		      oos.writeObject(l.toArray());
////		      oos.close();
//		      
//		      /*
//		      ZipOutputStream zos = new ZipOutputStream(bout);
//		      zos.setLevel(9);
//		      zos.setMethod(ZipOutputStream.DEFLATED);
//		      
//		      ZipEntry ze = new ZipEntry();
//		      byte[] b = bout.toByteArray();
//		      ze.setSize(b.length);
//		      zos.putNextEntry(ze);
//		      zos.write(b);
//		      zos.closeEntry();
//		      zos.flush();
//		      zos.close();
//		      */
//		      }
//		   catch (Exception e) { e.printStackTrace(); }
///*		if (l != null) {
//			long debut = System.currentTimeMillis();
//			System.out.println(debut);
//			try {
//					Messenger.sendMessage("sqlResult", l);
//					Thread.sleep(100);
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error(e.getMessage());
//				}
//				long fin = System.currentTimeMillis();
//				System.out.println(fin);
//				System.out.println("temps: "+(fin-debut));
//		}*/
//		// -------------------------
//	}
////	public Page getCompanies(final int pageNo, final int pageSize) throws SQLException {
////        PaginationHelper ph = new PaginationHelper();
////        return ph.fetchPage(
////                t,
////                request,
////                request,
////                null,
////                pageNo,
////                pageSize,
////                new RowMapper() {
////                    public Object mapRow(ResultSet rs, int i) throws SQLException {
////                        return new Object 
////                    }
////                }
////        );
////*/
   /* }

	
	private void dataToFile(Object line){
		// SEND LIST ---------------
		  try {
			  long debut = System.currentTimeMillis();
			  System.out.println(debut);


		      long fin = System.currentTimeMillis();
		      try{
		    	  Messenger.sendMessage("load", "OK'");
	      }catch(Exception e){
		    	  e.printStackTrace();
		      }
				System.out.println("Compression: "+(fin-debut));
		  }
	   catch (Exception e) { e.printStackTrace(); }
	}

}*/
