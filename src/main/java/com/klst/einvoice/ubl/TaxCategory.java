package com.klst.einvoice.ubl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.klst.ebXml.reflection.SCopyCtor;
import com.klst.einvoice.ITaxCategory;
import com.klst.einvoice.ITaxCategoryFactory;
import com.klst.untdid.codelist.TaxCategoryCode;
import com.klst.untdid.codelist.TaxTypeCode;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxExemptionReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxExemptionReasonType;

/**
 * TaxCategory contains elements used in Groups DOCUMENT LEVEL ALLOWANCES (BG-20) and CHARGES (BG-21), 
 * VAT BREAKDOWN (BG-23) and VAT INFORMATION (BG-30)
 * <p>
 * BG-20 + 0..n DOCUMENT LEVEL ALLOWANCES / ABSCHLÄGE
 * <br>
 * BG-21 + 0..n DOCUMENT LEVEL CHARGES / ZUSCHLÄGE
 * <p> 
 * BG-23 : VAT BREAKDOWN - A group of business terms providing information about VAT breakdown
 * by different categories, rates and exemption reasons
 * <br>Cardinality: 1..n (mandatory)
 * <br>Req.ID: R38, R45, R47, R48, R49
 * <p> 
 * BG-30 : LINE VAT INFORMATION - A group of business terms providing information about the VAT
 * applicable for the goods and services invoiced on the Invoice line.
 * <br>Cardinality: 1..1 (mandatory)
 * <br>Req.ID: R45, R48
 */
public class TaxCategory extends TaxCategoryType implements ITaxCategory, ITaxCategoryFactory {

	/**
	 * {@inheritDoc}
	 */
	// implements ITaxCategoryFactory
	@Override
	public ITaxCategory createTaxCategory(TaxTypeCode taxType, TaxCategoryCode taxCode, BigDecimal taxRate) {
		return create(taxType, taxCode, taxRate);
	}
	static TaxCategory create(TaxTypeCode taxType, TaxCategoryCode taxCode, BigDecimal taxRate) {
		return new TaxCategory(taxType.getValue(), taxCode, taxRate);
	}

	// copy factory
	static TaxCategory create(TaxCategoryType object) {
		if(object instanceof TaxCategoryType && object.getClass()!=TaxCategoryType.class) {
			// object is instance of a subclass of TaxCategoryType, but not TaxCategoryType itself
			return (TaxCategory)object;
		} else {
			return new TaxCategory(object); 
		}
	}

	private static final Logger LOG = Logger.getLogger(TaxCategory.class.getName());
	
	// copy ctor
	private TaxCategory(TaxCategoryType doc) {
		super();
		if(doc!=null) {
			SCopyCtor.getInstance().invokeCopy(this, doc);
			LOG.fine("copy ctor:"+this);			
		}
	}
	
	private TaxCategory(String taxType, TaxCategoryCode taxCode, BigDecimal taxRate) {
		super();
		setTaxType(taxType);
		setTaxCategoryCode(taxCode);
		setTaxPercentage(taxRate);
	}
	
	// ALLOWANCES (BG-20.BT-95-0) and CHARGES (BG-21.BT-102-0)
	// VAT BREAKDOWN BG-23.BT-118-0 and LINE VAT INFORMATION BG-30.BT-151-0
	@Override // implements ITaxCategory , ITaxType
	public void setTaxType(String type) {
		super.setTaxScheme(TaxScheme.create(type));
	}

	@Override // implements ITaxCategory , ITaxType
	public String getTaxType() {
		return super.getTaxScheme()==null ? null : TaxScheme.getTaxType(super.getTaxScheme());
	}

	// BT-95, BT-102 (mandatory) Document level allowance/charge VAT category code
	// BG-23.BT-118
	@Override
	public void setTaxCategoryCode(String code) {
		super.setID(new ID(code));
	}

	/**
	 * Coded identification of a tax/VAT category. Entries of UNTDID 5305 are used.
	 * <p>
	 * Cardinality: 1..1 (mandatory)
	 * <br>ID: BT-118, BT-151 
	 * <br>Req.ID: R38, R45, R49; R37, R45, R48, R55
	 * 
	 * @return Code
	 */
	@Override
	public TaxCategoryCode getTaxCategoryCode() {
		return TaxCategoryCode.valueOf(this);
	}
	
	// BT-96, BT-103 0..1 Document level allowance/charge tax/VAT rate
	// BG-23.BT-119
	@Override
	public void setTaxPercentage(BigDecimal taxRate) {
		if(taxRate==null) return;
		super.setPercent(new Percent(taxRate));
	}

	/**
	 * The tax rate, represented as percentage that applies to the VAT category.
	 * <p>
	 * Cardinality: 0..1 (optional)
	 * <br>ID: BT-119, BT-152
	 * <br>Req.ID: R38, R49; R37, R45, R48
	 * 
	 * @return taxRate percentage
	 */
	@Override
	public BigDecimal getTaxPercentage() {
		return getTaxPercentage(this);
	}
	static BigDecimal getTaxPercentage(TaxCategoryType doc) {
		return doc.getPercent()==null? null : doc.getPercent().getValue();
	}
	
	private static final int SCALE = 2;
	private BigDecimal getTaxPercentage(RoundingMode roundingMode) {
		BigDecimal rate = getTaxPercentage();
		if(rate==null) return rate;
		return rate.setScale(SCALE, roundingMode);
	}

	private String getTaxRateAsString() {
		BigDecimal rate = getTaxPercentage(RoundingMode.HALF_UP);
		return rate==null ? "" : rate.toString()+"%";
	}
	
	/**
	 * VAT exemption reason text
	 * <p>
	 * A textual statement of the reason why the amount is exempted from VAT or why no VAT is being charged
	 * <p>
	 * Cardinality: 0..1 (optional)
	 * <br>ID: BT-120
	 * <br>Req.ID: R48, R49, R51
	 * 
	 * @return List<String>
	 */
	public List<String> getTaxExemptionReasons() {
		List<TaxExemptionReasonType> taxExemptionReasons = this.getTaxExemptionReason();
		List<String> resultList = new ArrayList<String>(taxExemptionReasons.size());
		taxExemptionReasons.forEach(taxExemptionReason -> {
			resultList.add(taxExemptionReason.getValue());
		});
		return resultList;
	}

	public void addTaxExemptionReason(String reasonText) {
		TaxExemptionReasonType taxExemptionReason = new TaxExemptionReasonType();
		taxExemptionReason.setValue(reasonText);
		this.getTaxExemptionReason().add(taxExemptionReason);
	}
	
	/**
	 * VAT exemption reason code
	 * <p>
	 * A coded statement of the reason for why the amount is exempted from VAT.
	 * Code list issued and maintained by the Connecting Europe Facility.
	 * <p>
	 * Cardinality: 0..1 (optional)
	 * <br>ID: BT-121
	 * <br>Req.ID: R48, R49, R51, R55
	 * 
	 * @return List<String>
	 */
	public String getTaxExemptionReasonCodeAsString() {
		TaxExemptionReasonCodeType taxExemptionReasonCode = this.getTaxExemptionReasonCode();	
		return taxExemptionReasonCode==null ? null : taxExemptionReasonCode.getValue();
	}

	public void setTaxExemptionReasonCode(String reasonCode) {
		TaxExemptionReasonCodeType taxExemptionReasonCode = new TaxExemptionReasonCodeType();
		taxExemptionReasonCode.setValue(reasonCode);
		super.setTaxExemptionReasonCode(taxExemptionReasonCode);
	}

	@Override
	public String toString() {
		List<String> reasonTexts = getTaxExemptionReasons();
		String joined = String.join("], [", reasonTexts);
		if(!joined.isEmpty()) {
			joined = " + ["+joined+"]";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[TaxType=");
		stringBuilder.append(getTaxType()==null ? "null" : getTaxType());
		stringBuilder.append(", TaxCategoryCode=");
		stringBuilder.append(getTaxCategoryCode()==null ? "null" : getTaxCategoryCode());
		stringBuilder.append(", TaxPercentage=");
		stringBuilder.append(getTaxRateAsString()==null ? "null" : getTaxRateAsString());
		stringBuilder.append(joined);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

}
