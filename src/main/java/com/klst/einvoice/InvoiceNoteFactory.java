package com.klst.einvoice;

/**
 * abstract-factory, aka Kit for BG-1 INVOICE NOTE
 * 
 * @see <a href="https://java-design-patterns.com/patterns/abstract-factory/">java-design-patterns</a> for patterns abstract-factory
 */

/* Freitext zur Rechnung BG-1
 * 
 * 
 * 
 */
public interface InvoiceNoteFactory {
	
	/**
	 * factory for InvoiceNote BG-1
	 * 
	 * @param subjectCode optional
	 * @param content to be chosen from the entries in <a href="https://unece.org/fileadmin/DAM/trade/untdid/d16b/tred/tred4451.htm">UNTDID 4451</a>
	 * 
	 * @return BG-1 INVOICE NOTE Object
	 */
	public InvoiceNote createNote(String subjectCode, String content);
	
}
