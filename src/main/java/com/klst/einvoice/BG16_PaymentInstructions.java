package com.klst.einvoice;

/**
 * BG-16 (mandatory) PAYMENT INSTRUCTIONS
 * <p>
 * A group of business terms providing information about the payment.
 * <p>
 * Cardinality: 	0..1
 * <br>EN16931-ID: 	BG-16
 * <br>Rule ID: 	BR-49 , BR-DE-1 (1..1 this cius rule makes it mandatory)
 * <br>Request ID: 	R58
 * 
 * @see <a href="https://standards.cen.eu">standards.cen.eu</a> (en)EN_16931_1_2017 for rule and request IDs
 */

/*   (en) rules
 * 
 * BR-49  Target / context: Payment instructions, Business term / group: BT-81
 * A Payment instruction (BG-16) shall specify the Payment means type code (BT-81).
 *
 *  (de) rules / Geschäftsregel:
 * 
 * BR-49   : Zahlungsanweisungen
 * Eine Zahlungsanweisung (BG-16) muss den Code für die Zahlungsart (BT-81) angeben.
 * 
 * BR-DE-1 : Eine Gruppe von Informationselementen, die Informationen darüber liefern, wie die Zahlung erfolgen soll.
 * Eine Rechnung (INVOICE) muss Angaben zu „PAYMENT INSTRUCTIONS“ (BG-16) enthalten.
 * 
 * BR-DE-13: In der Rechnung müssen Angaben zu genau einer der drei Gruppen sein: 
 * CREDIT TRANSFER, PAYMENT CARD INFORMATION, DIRECT DEBIT 
 * 
 */

public interface BG16_PaymentInstructions extends PaymentInstructions , PaymentInstructionsFactory {

/*

// PaymentInstructions , PaymentInstructionsFactory existieren bereits!!!
// CoreInvoice extends PaymentInstructionsFactory, dh in UBL/CII existiert 
	// PaymentInstructions createPaymentInstructions(PaymentMeansEnum code, String paymentMeansText);
	// in CoreInvoice: public PaymentInstructions getPaymentInstructions();
// ABER welche Klassen implementieren PaymentInstructions?
UBL:
class PaymentMeans extends PaymentMeansType implements PaymentInstructions
		
		
CII:
class ApplicableHeaderTradeSettlement extends HeaderTradeSettlementType implements PaymentInstructions,
		

 */

	// BT-81 ++ 1..1 Payment means type code / Zahlungsart
	
	// BT-82 ++ 0..1 Payment means text
	
	// BT-83 ++ 0..1 Remittance information / Verwendungszweck
	
	// BG-17 ++ 0..n CREDIT TRANSFER
	// BG-18 ++ 0..1 PAYMENT CARD INFORMATION
	// BG-19 ++ 0..1 DIRECT DEBIT
}
