package util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;


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
		    

		    try {
				t.query(new PreparedStatementCreator()
				{
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
					{
						PreparedStatement pS = connection.prepareStatement(request, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						pS.setFetchSize(Integer.MIN_VALUE);
						return pS;
					}
				},new ResultSetExtractor()
				{
					public Object extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						int numberColum = rs.getMetaData().getColumnCount();
						int i = 1;
						String[] line = new String[numberColum];
						try
						{
							Messenger.sendMessage("sqlResultStart",line);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							logger.error(e.getMessage());
						}
						while (rs.next())
						{
							for(i=1;i<=numberColum;i++)
								line[i-1]=rs.getString(i);
							try
							{
								Messenger.sendMessage("sqlResult",line);
							}
							catch (Exception e)
							{
								e.printStackTrace();
								logger.error(e.getMessage());
							}
						}
						try
						{
							Messenger.sendMessage("sqlResultStop",null);
						}
						catch (Exception e)
						{							
							e.printStackTrace();
							logger.error(e.getMessage());
						}
						return null;
					}
					
				});
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
//		// SEND LIST ---------------
//		  try {
//			  long debut = System.currentTimeMillis();
//			  System.out.println(debut);
//			  SerializationContext context = SerializationContext.getSerializationContext();
//			 
//		      ByteArrayOutputStream bout = new ByteArrayOutputStream();
//		      Amf3Output amf3Output = new Amf3Output(context);
//		      amf3Output.setOutputStream(bout);
//		      amf3Output.writeObject(l);
//		      amf3Output.flush();
//		      byte[] b = bout.toByteArray();
//		      amf3Output.close();		      
//		      //FileOutputStream f = new FileOutputStream("Data.dat");
//
//		      File path=new File("data.gz");
//		      FileOutputStream outFile = new FileOutputStream(path);
//		      GZIPOutputStream zipOut = new GZIPOutputStream(outFile);
////		      zipOut.setLevel(9);
//	//	      zipOut.setMethod(ZipOutputStream.DEFLATED);
//		//      zipOut.putNextEntry(new ZipEntry("0"));
//		      
//		      zipOut.write(b); 
//		      zipOut.flush();
//		      zipOut.close();
//		      
//		      
//
//		      long fin = System.currentTimeMillis();
//		      try{
//		    	  Messenger.sendMessage("load", "OK'");
//		      }catch(Exception e){
//		    	  e.printStackTrace();
//		      }
//				System.out.println("Compression: "+(fin-debut));
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
////
    }
}
