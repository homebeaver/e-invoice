package com.klst.einvoice;

// BG-20 + 0..n DOCUMENT LEVEL ALLOWANCES / ABSCHLÄGE
/**
 * BG-20 + 0..n DOCUMENT LEVEL ALLOWANCES
 * <p>
 * A group of business terms providing information about allowances applicable to the Invoice as a whole.
 * <p>
 * Deductions, such as withheld tax may also be specified in this group.
 * <p>
 * Cardinality: 	0..n
 * <br>EN16931-ID: 	BG-20
 * <br>Rule ID: 	BR-31 , BR-32 , BR-33
 * <br>Request ID: 	R15
 * 
 * @see <a href="https://standards.cen.eu">standards.cen.eu</a> (en)EN_16931_1_2017 for rule and request IDs
 */
/*

BR-31 
Jeder Abschlag auf Dokumentenebene (BG-20) muss einen Zuschlagsbetrag auf der Dokumentenebene (BT-92) haben.

BR-32 
Jeder Abschlag auf Dokumentenebene (BG-20) muss einen Code für die für diesen Abschlag geltende 
Umsatzsteuer auf Dokumentenebene (BT-95) haben

BR-33 
Jeder Abschlag auf Dokumentenebene (BG-20) muss einen Grund für diesen Abschlag auf Dokumentenebene (BT-97) oder 
einen Code für den Grund für diesen Abschlag auf Dokumentenebene (BT-98) haben.

BR-CO-5 
Der Code des Grundes für den Abschlag auf Dokumentenebene (BT-98) und 
der Grund für den Abschlag auf Dokumentenebene (BT-97) müssen dieselbe Zuschlagsart anzeigen.

 */
// Abzüge, wie z. B. für einbehaltene Steuern, dürfen ebenfalls in dieser Gruppe angegeben werden.
public interface BG20_DocumentAllowances extends AllowancesAndCharges {
	
//	public static boolean getChargeIndicator() {
//		return AllowancesAndCharges.ALLOWANCE;
//	}

}
