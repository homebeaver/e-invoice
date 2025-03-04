package com.klst.einvoice.ubl;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.klst.ebXml.reflection.SCopyCtor;
import com.klst.einvoice.AllowancesAndCharges;
import com.klst.einvoice.unece.uncefact.Amount;
import com.klst.untdid.codelist.TaxCategoryCode;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AllowanceChargeReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AllowanceChargeReasonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BaseAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ChargeIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.MultiplierFactorNumericType;

/*

Bsp 02.01:
  <cac:AllowanceCharge>
    <cbc:ChargeIndicator>true</cbc:ChargeIndicator>                    <!-- true : ZUSCHLAG / CHARGES -->
    <cbc:AllowanceChargeReasonCode>TAC</cbc:AllowanceChargeReasonCode> <!-- TAC : kein gültiger Cade -->
    <cbc:AllowanceChargeReason>Testing</cbc:AllowanceChargeReason>
    <cbc:MultiplierFactorNumeric>0</cbc:MultiplierFactorNumeric>
    <cbc:Amount currencyID="EUR">0</cbc:Amount>
    <cbc:BaseAmount currencyID="EUR">0</cbc:BaseAmount>
    <cac:TaxCategory>
      <cbc:ID>E</cbc:ID>
      <cbc:Percent>0</cbc:Percent>
      <cac:TaxScheme>
        <cbc:ID>VAT</cbc:ID>
      </cac:TaxScheme>
    </cac:TaxCategory>
  </cac:AllowanceCharge>
  <cac:AllowanceCharge>
    <cbc:ChargeIndicator>false</cbc:ChargeIndicator>                   <!-- false : ABSCHLAG / ALLOWANCE -->
    <cbc:AllowanceChargeReasonCode>102</cbc:AllowanceChargeReasonCode> <!-- Fixed long term -->
    <cbc:AllowanceChargeReason>Fixed long term</cbc:AllowanceChargeReason>
    <cbc:MultiplierFactorNumeric>0</cbc:MultiplierFactorNumeric>
    <cbc:Amount currencyID="EUR">0</cbc:Amount>
    <cbc:BaseAmount currencyID="EUR">0</cbc:BaseAmount>
    <cac:TaxCategory>
      <cbc:ID>E</cbc:ID>
      <cbc:Percent>0</cbc:Percent>
      <cac:TaxScheme>
        <cbc:ID>VAT</cbc:ID>
      </cac:TaxScheme>
    </cac:TaxCategory>
  </cac:AllowanceCharge>

Example for BG-29 1..1 PRICE DETAILS
BG-29.BT-147 +++ 0..1 Item price discount, Nachlass auf den Artikelpreis
                      der gesamte zur Berechnung des Nettopreises vom Bruttopreis subtrahierte Rabatt
BG-29.BT-148 +++ 0..1 Item gross price, Bruttopreis des Artikels
                      der Einheitspreis ohne Umsatzsteuer vor Abzug des Nachlass auf den Artikelpreis
ubl-tc434-example5.xml :

        <cac:Price>
            <cbc:PriceAmount currencyID="DKK">1.00</cbc:PriceAmount> <!-- BG-29.BT-146 -->
            <cbc:BaseQuantity unitCode="EA">1</cbc:BaseQuantity>
            <cac:AllowanceCharge>                               <!-- UnitPriceAllowance : discount+gross price -->
                <cbc:ChargeIndicator>false</cbc:ChargeIndicator>
                <cbc:Amount currencyID="DKK">0.10</cbc:Amount>         <!-- BG-29.BT-147 -->
                <cbc:BaseAmount currencyID="DKK">1.10</cbc:BaseAmount> <!-- BG-29.BT-148 -->
            </cac:AllowanceCharge>
        </cac:Price>

 */
public class AllowanceCharge extends AllowanceChargeType implements AllowancesAndCharges {

	// factory:
	@Override
	public AllowancesAndCharges createAllowance(Amount amount, Amount baseAmount, BigDecimal percentage) {
		return create(AllowancesAndCharges.ALLOWANCE, amount, baseAmount, percentage);
	}
	@Override
	public AllowancesAndCharges createCharge(Amount amount, Amount baseAmount, BigDecimal percentage) {
		return create(AllowancesAndCharges.CHARGE, amount, baseAmount, percentage);
	}

	/*
	 * used in (Line)Price
	 * @see Price#setPriceDiscount
	 */
	static AllowanceCharge create(boolean chargeIndicator, Amount amount, Amount baseAmount, BigDecimal percentage) {
		return new AllowanceCharge(chargeIndicator, amount, baseAmount, percentage);
	}
	static AllowanceCharge create() {
		return new AllowanceCharge(null);
	}
	// copy factory
	static AllowanceCharge create(AllowanceChargeType object) {
		if(object instanceof AllowanceChargeType && object.getClass()!=AllowanceChargeType.class) {
			// object is instance of a subclass of AllowanceChargeType, but not AllowanceChargeType itself
			return (AllowanceCharge)object;
		} else {
			return new AllowanceCharge(object); 
		}
	}

	private static final Logger LOG = Logger.getLogger(AllowanceCharge.class.getName());
	
	// das erste element der Liste taxCategory aus super, die anderen werden nicht genutzt
	TaxCategory taxCategory = null;
	
	private AllowanceCharge(boolean chargeIndicator, Amount amount, Amount baseAmount, BigDecimal percentage) {
		super();
		setChargeIndicator(chargeIndicator);
		setAmountWithoutTax(amount);
		setAssessmentBase(baseAmount);
		setPercentage(percentage);
	}

	// copy ctor
	private AllowanceCharge(AllowanceChargeType doc) {
		super();
		if(doc!=null) {
			SCopyCtor.getInstance().invokeCopy(this, doc);
			TaxCategoryType tc = doc.getTaxCategory().isEmpty() ? null : doc.getTaxCategory().get(0);
			taxCategory = TaxCategory.create(tc); 
			LOG.fine("copy ctor:"+this);
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("[");
		if(isAllowance()) stringBuilder.append("ALLOWANCE");
		if(isCharge()) stringBuilder.append("CHARGE"); // BG-21
		
		stringBuilder.append(", AmountWithoutTax:"); // BT-99
		stringBuilder.append(getAmountWithoutTax()==null ? "null" : getAmountWithoutTax());
		stringBuilder.append(", AssessmentBase:");   // BT-100
		stringBuilder.append(getAssessmentBase()==null ? "null" : getAssessmentBase());
		stringBuilder.append(", %rate:");            // BT-101
		stringBuilder.append(getPercentage()==null ? "null" : getPercentage());
		
		stringBuilder.append(", tax:");   // BT-102-0 + BT-102 + BT-103
		stringBuilder.append(taxCategory==null ? "null" : taxCategory);
		
		stringBuilder.append(", Reasoncode:"); // BT-104
		stringBuilder.append(getReasoncode()==null ? "null" : getReasoncode());
		stringBuilder.append(", ReasonText:"); // BT-105
		stringBuilder.append(getReasonText()==null ? "null" : getReasonText());
		
		stringBuilder.append("]");
		return stringBuilder.toString();
	}


	@Override
	public void setChargeIndicator(boolean indicator) {
		ChargeIndicatorType chargeIndicator = new ChargeIndicatorType();
		chargeIndicator.setValue(indicator);
		super.setChargeIndicator(chargeIndicator);
	}

	@Override
	public boolean isAllowance() {
		return AllowancesAndCharges.ALLOWANCE==super.getChargeIndicator().isValue();
	}

	@Override
	public boolean isCharge() {
		return AllowancesAndCharges.CHARGE==super.getChargeIndicator().isValue();
	}

	@Override
	public void setAmountWithoutTax(Amount amount) {
		AmountType amt = new AmountType();
		amount.copyTo(amt);
		super.setAmount(amt);
	}

	@Override
	public Amount getAmountWithoutTax() {
		AmountType amount = super.getAmount();
		return amount==null? null : new Amount(amount.getCurrencyID(), amount.getValue());	
	}

	@Override
	public void setAssessmentBase(Amount amount) {
		if(amount==null) return;
		BaseAmountType baseAmount = new BaseAmountType();
		amount.copyTo(baseAmount);
		super.setBaseAmount(baseAmount);
	}

	@Override
	public Amount getAssessmentBase() {
		BaseAmountType amount = super.getBaseAmount();
		return amount==null? null : new Amount(amount.getCurrencyID(), amount.getValue());	
	}

	@Override
	public void setPercentage(BigDecimal percentage) {
		if(percentage==null) return;
		MultiplierFactorNumericType factor = new MultiplierFactorNumericType();
		factor.setValue(percentage);
		super.setMultiplierFactorNumeric(factor);
	}

	@Override
	public BigDecimal getPercentage() {
		MultiplierFactorNumericType factor = super.getMultiplierFactorNumeric();
		return factor==null? null : factor.getValue();
	}

	@Override
	public void setTaxType(String code) {
		if(code==null) return;
		if(taxCategory==null) taxCategory = TaxCategory.create(null); 
		taxCategory.setTaxType(code);
	}

	@Override
	public String getTaxType() {
		return taxCategory.getTaxType();
	}

	@Override
	public void setTaxCategoryCode(String code) {
		if(taxCategory==null) taxCategory = TaxCategory.create(null); 
		taxCategory.setTaxCategoryCode(code);
	}

	@Override
	public void setTaxCategoryCode(TaxCategoryCode code) {
		if(taxCategory==null) taxCategory = TaxCategory.create(null); 
		taxCategory.setTaxCategoryCode(code);
	}

	@Override
	public TaxCategoryCode getTaxCategoryCode() {
		return taxCategory.getTaxCategoryCode();
	}

	@Override
	public void setTaxPercentage(BigDecimal percentage) {
		if(taxCategory==null) taxCategory = TaxCategory.create(null); 
		taxCategory.setTaxPercentage(percentage);
	}

	@Override
	public BigDecimal getTaxPercentage() {
		return taxCategory.getTaxPercentage();
	}

	@Override
	public void setReasonText(String text) {
		if(text==null) return;
		AllowanceChargeReasonType allowanceChargeReason = new AllowanceChargeReasonType();
		allowanceChargeReason.setValue(text);
		super.getAllowanceChargeReason().add(allowanceChargeReason);
	}

	@Override
	public String getReasonText() {
		List<AllowanceChargeReasonType> list = super.getAllowanceChargeReason();
		return list.isEmpty()? null : list.get(0).getValue();
	}

	@Override
	public void setReasoncode(String code) {
		if(code==null) return;
		AllowanceChargeReasonCodeType allowanceChargeReasonCode = new AllowanceChargeReasonCodeType();
		allowanceChargeReasonCode.setValue(code);
		super.setAllowanceChargeReasonCode(allowanceChargeReasonCode);
	}

	@Override
	public String getReasoncode() {
		AllowanceChargeReasonCodeType code = super.getAllowanceChargeReasonCode();
		return code==null? null : code.getValue();
	}

}
