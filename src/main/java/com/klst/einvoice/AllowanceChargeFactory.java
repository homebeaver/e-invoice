package com.klst.einvoice;

import com.klst.einvoice.unece.uncefact.Amount;

public interface AllowanceChargeFactory {
	
	// (mandatory) props
	public AllowancesAndCharges createAllowance(Amount amount, String vatCode, String reason, String reasonCode);
	public AllowancesAndCharges createCharge(Amount amount, String vatCode, String reason, String reasonCode);
	
}
