package com.klst.einvoice;

/*
 
Used in
	BG-1  INVOICE NOTE 0..n: code (optional) + noteText (Mandatory) 
	BG-32 ITEM ATTRIBUTES -> dort als Properties implemetiert, name+value mandatory


org.omg.CORBA
Class NameValuePair
public final class NameValuePair
 */
public interface NameValue {

	public void setName(String name);
	public String getName();
	
	public void setValue(String value);
	public String getValue();
	
}
