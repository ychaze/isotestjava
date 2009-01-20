package util;

import manager.ExcelManager;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Initialize classes...\n");
		ExcelManager ex = new ExcelManager();
		ex.process(new String ("C:\\Documents and Settings\\Administrateur\\Bureau\\wb.xls##0"));
	}

}
