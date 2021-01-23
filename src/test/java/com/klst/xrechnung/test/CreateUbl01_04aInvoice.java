package com.klst.xrechnung.test;

import java.util.logging.Logger;

public class CreateUbl01_04aInvoice extends CreateUblXXXInvoice {

	private static final Logger LOG = Logger.getLogger(CreateUbl01_04aInvoice.class.getName());
	private static final String UBL_XML = "01.04a-INVOICE_ubl.xml";
//	private static final String UBL_XML = "ubl001.xml"; 
//	private static final String UBL_XML = "example-peppol-ubl.xml";
	private static final String UNCEFACT_XML = "01.04a-INVOICE_uncefact.xml";

	public static void main(String[] args) {
		LOG.info("main");
		InvoiceFactory factory = new CreateUblXXXInvoice(UBL_XML);
		byte[] bytes = factory.toUbl();
		String xml = new String(bytes); // das xml muss kosit validierbar sein!
		LOG.info("check:"+factory.check(bytes));
		LOG.info("xml=\n"+xml);
	}

}
