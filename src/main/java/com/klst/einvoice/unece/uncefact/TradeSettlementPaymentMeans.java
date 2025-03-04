package com.klst.einvoice.unece.uncefact;

import java.util.logging.Logger;

import com.klst.ebXml.reflection.SCopyCtor;
import com.klst.einvoice.CreditTransfer;
import com.klst.einvoice.DebitedAccountID;
import com.klst.einvoice.PaymentCard;
import com.klst.untdid.codelist.PaymentMeansEnum;

import un.unece.uncefact.data.standard.qualifieddatatype._100.PaymentMeansCodeType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._100.CreditorFinancialAccountType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._100.CreditorFinancialInstitutionType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._100.DebtorFinancialAccountType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._100.TradeSettlementPaymentMeansType;

//Gruppe CREDIT TRANSFER                   BG-17
//Gruppe PAYMENT CARD INFORMATION          BG-18
//Gruppe DIRECT DEBIT                      BG-19
/*
Bsp: 01.15a:
...
        <ram:ApplicableHeaderTradeSettlement>
            <ram:PaymentReference>0000123456</ram:PaymentReference>
            <ram:InvoiceCurrencyCode>EUR</ram:InvoiceCurrencyCode>
            <ram:SpecifiedTradeSettlementPaymentMeans>
                <ram:TypeCode>58</ram:TypeCode>                         <!-- BG-16.BT-81 -->
                <ram:PayeePartyCreditorFinancialAccount>                <!-- BG-17 0..n CREDIT TRANSFER
                    <ram:IBANID>DE75512108001245126199</ram:IBANID>
                    <ram:AccountName>[Payment account name]</ram:AccountName>
                </ram:PayeePartyCreditorFinancialAccount>
                <ram:PayeeSpecifiedCreditorFinancialInstitution>                 2-te Eintrag
                    <ram:BICID>[BIC]</ram:BICID>
                </ram:PayeeSpecifiedCreditorFinancialInstitution>              bis hierhin -->
            </ram:SpecifiedTradeSettlementPaymentMeans>

Testfälle für PAYMENT CARD INFORMATION
03.02a-INVOICE_uncefact.xml :
...
            <ram:SpecifiedTradeSettlementPaymentMeans>
                <ram:TypeCode>48</ram:TypeCode>
                <ram:ApplicableTradeSettlementFinancialCard>
                    <ram:ID>41234</ram:ID>
                    <ram:CardholderName>[Payment card holder name]</ram:CardholderName>
                </ram:ApplicableTradeSettlementFinancialCard>
            </ram:SpecifiedTradeSettlementPaymentMeans>


Testfälle für DIRECT DEBIT
0 .. 1 DirectDebitMandateID Kennung der Mandatsreferenz               BG-19.BT-89
0 .. 1 CreditorReferenceID Kennung des Gläubigers                     BG-19.BT-90
1 .. 1 IBANID Lastschriftverfahren: Kennung des zu belastenden Kontos BG-19.BT-91

Bsp: 03.01a:
        <ram:ApplicableHeaderTradeSettlement>
            <ram:CreditorReferenceID>[Bank assigned creditor identifier]</ram:CreditorReferenceID>   <!-- BG-19.BT-90 -->
            <ram:InvoiceCurrencyCode>EUR</ram:InvoiceCurrencyCode>
            <ram:SpecifiedTradeSettlementPaymentMeans>
                <ram:TypeCode>59</ram:TypeCode>                                                      <!-- BG-16.BT-81 -->
                <ram:PayerPartyDebtorFinancialAccount>
                    <!-- dies ist eine nicht existerende aber valide IBAN als test dummy -->
                    <ram:IBANID>DE75512108001245126199</ram:IBANID>                                  <!-- BG-19.BT-91 -->
                </ram:PayerPartyDebtorFinancialAccount>
            </ram:SpecifiedTradeSettlementPaymentMeans>
            ...
            <ram:SpecifiedTradePaymentTerms>
                <ram:Description>Dieses Guthaben werden wir auf Ihr Konto erstatten.</ram:Description>
                <ram:DueDateDateTime>
                    <udt:DateTimeString format="102">20190314</udt:DateTimeString>
                </ram:DueDateDateTime>
                <ram:DirectDebitMandateID>[Mandate reference identifier]</ram:DirectDebitMandateID>  <!-- BG-19.BT-89 -->
            </ram:SpecifiedTradePaymentTerms>

Bsp: 03.04a , 03.05a
 */
public class TradeSettlementPaymentMeans extends TradeSettlementPaymentMeansType 
	implements CreditTransfer, DebitedAccountID {

	// factory:
	static CreditTransfer create(IBANId iban, String accountName, BICId bic) {
		return new TradeSettlementPaymentMeans(iban, accountName, bic);
	}

	static CreditTransfer create(String accountId, String accountName, BICId bic) {
		return new TradeSettlementPaymentMeans(accountId, accountName, bic);
	}

	static TradeSettlementPaymentMeans create(String cardAccountID, String cardHolderName) {
		TradeSettlementFinancialCard paymentCard = TradeSettlementFinancialCard.create(cardAccountID, cardHolderName);
		return new TradeSettlementPaymentMeans(paymentCard);
	}

	static TradeSettlementPaymentMeans create(IBANId debitedIban) {
		return new TradeSettlementPaymentMeans(debitedIban);
	}
	static TradeSettlementPaymentMeans create(String debitedAccount, BICId bic) {
		return new TradeSettlementPaymentMeans(debitedAccount, bic);
	}

	static TradeSettlementPaymentMeans create() {
		return create((TradeSettlementPaymentMeansType)null);
	}
	// copy factory
	static TradeSettlementPaymentMeans create(TradeSettlementPaymentMeansType object) {
		// @see https://stackoverflow.com/questions/2699788/java-is-there-a-subclassof-like-instanceof
		if(object instanceof TradeSettlementPaymentMeansType && object.getClass()!=TradeSettlementPaymentMeansType.class) {
			// object is instance of a subclass of TradeSettlementPaymentMeansType, but not TradeSettlementPaymentMeansType itself
			return (TradeSettlementPaymentMeans)object;
		} else {
			return new TradeSettlementPaymentMeans(object); 
		}
	}

	private static final Logger LOG = Logger.getLogger(TradeSettlementPaymentMeans.class.getName());

	private TradeSettlementPaymentMeans(TradeSettlementPaymentMeansType tradeSettlementPaymentMeans) {
		super();
		if(tradeSettlementPaymentMeans!=null) {
			SCopyCtor.getInstance().invokeCopy(this, tradeSettlementPaymentMeans);
			LOG.fine("copy ctor:"+this);
		}
	}
	
	TradeSettlementPaymentMeans(PaymentMeansEnum code, String text) {
		super();
		this.setTypeCode(code);
		this.setPaymentMeansText(text);
		LOG.config("done:"+this);
	}
	private TradeSettlementPaymentMeans(PaymentMeansEnum code) {
		this(code, null);
	}

	private void setTypeCode(PaymentMeansEnum code) {
		PaymentMeansCodeType pmc = new PaymentMeansCodeType(); // BT-81
		pmc.setValue(code.getValueAsString());
		super.setTypeCode(pmc);
	}
	
	PaymentMeansEnum getPaymentMeansEnum() {
		PaymentMeansCodeType pmc = super.getTypeCode();
		if(pmc==null) return null;
		return PaymentMeansEnum.valueOf(pmc);
	}
	
	boolean isCreditTransfer() {
		return PaymentMeansEnum.isCreditTransfer(getPaymentMeansEnum());
	}
	
	boolean isBankCard() {
		return PaymentMeansEnum.isBankCard(getPaymentMeansEnum());
	}
	
	TradeSettlementFinancialCard getPaymentCard() {
		if(super.getApplicableTradeSettlementFinancialCard()==null) return null;
		return TradeSettlementFinancialCard.create(getApplicableTradeSettlementFinancialCard());
	}
	
	boolean isDirectDebit() {
		return PaymentMeansEnum.isDirectDebit(getPaymentMeansEnum());
	}
	
	// BT-82 
	void setPaymentMeansText(String text) {
		if(text!=null) {
			super.getInformation().add(Text.create(text));
		}
	}
	String getPaymentMeansText() {
		if(super.getInformation().isEmpty()) {
			return null;
		}
		return super.getInformation().get(0).getValue();
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("[");
		stringBuilder.append(this.getPaymentMeansEnum());
		if(isCreditTransfer()) {
			stringBuilder.append(", CREDIT TRANSFER PaymentAccountID:"); // BG-17.BT-84
			stringBuilder.append(getPaymentAccountID()==null ? "null" : getPaymentAccountID());
			stringBuilder.append(", PaymentAccountName:");               // BG-17.BT-85
			stringBuilder.append(getPaymentAccountName()==null ? "null" : getPaymentAccountName());
			stringBuilder.append(", PaymentServiceProviderID:");         // BG-17.BT-86
			stringBuilder.append(getPaymentServiceProviderID()==null ? "null" : getPaymentServiceProviderID());
		}
		if(isBankCard()) {
			stringBuilder.append(", PAYMENT CARD INFORMATION:");
			stringBuilder.append(getPaymentCard()==null ? "null" : getPaymentCard());
		}	
		if(isDirectDebit()) {
			stringBuilder.append(", DIRECT DEBIT DebitedAccountID:");
			stringBuilder.append(getDebitedAccountID()==null ? "null" : getDebitedAccountID());
		}	
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	TradeSettlementPaymentMeans(IBANId iban, String accountName, BICId bic) { // TODO private + factory
		this(PaymentMeansEnum.SEPACreditTransfer);
		setPaymentAccountID(iban);
		setPaymentAccountName(accountName);
		setPaymentServiceProviderID(bic);
	}

	TradeSettlementPaymentMeans(String accountId, String accountName, BICId bic) { // TODO private + factory
		this(PaymentMeansEnum.CreditTransfer);
		setPaymentAccountID(accountId);
		setPaymentAccountName(accountName);
		setPaymentServiceProviderID(bic);
	}

	static TradeSettlementPaymentMeans create(PaymentCard paymentCard) { 
		return new TradeSettlementPaymentMeans((TradeSettlementFinancialCard)paymentCard);
	}
	private TradeSettlementPaymentMeans(TradeSettlementFinancialCard financialCard) {
		this(PaymentMeansEnum.BankCard);
		super.setApplicableTradeSettlementFinancialCard(financialCard);
	}

	public TradeSettlementPaymentMeans(IBANId debitedIban) {
		this(PaymentMeansEnum.SEPADirectDebit);
		setDebitedAccountID(debitedIban);
	}

	public TradeSettlementPaymentMeans(String accountId, BICId bic) {
		this(PaymentMeansEnum.DirectDebit);
		setDebitedAccountID(accountId);
//		setDebtorServiceProviderID(bic); // TODO
	}

// ---------------- implements CreditTransfer:
	
	// BG-17.BT-84 BT-84-0
	@Override
	public String getPaymentAccountID() {
		if(isCreditTransfer()) {
			CreditorFinancialAccountType cfa = super.getPayeePartyCreditorFinancialAccount();
			return cfa==null ? null : (cfa.getIBANID()==null ? cfa.getProprietaryID().getValue() : cfa.getIBANID().getValue());
		}
		return null;
	}

	@Override
	public void setPaymentAccountID(IBANId iban) { // use ctor/factory
		if(iban==null) return;
		if(this.isCreditTransfer()) {
			CreditorFinancialAccountType creditorFinancialAccount = new CreditorFinancialAccountType();
			creditorFinancialAccount.setIBANID(new ID(iban.getValue(), iban.getSchemeID()));		
			super.setPayeePartyCreditorFinancialAccount(creditorFinancialAccount);
		}
	}

	@Override
	public void setPaymentAccountID(String accountId) { // use ctor/factory
		if(accountId==null) return;
		if(this.isCreditTransfer()) {
			CreditorFinancialAccountType creditorFinancialAccount = new CreditorFinancialAccountType();
			creditorFinancialAccount.setProprietaryID(new ID(accountId));		
			super.setPayeePartyCreditorFinancialAccount(creditorFinancialAccount);
		}
	}

	// BG-17.BT-85
	@Override
	public String getPaymentAccountName() {
		if(isCreditTransfer()) {
			CreditorFinancialAccountType cfa = super.getPayeePartyCreditorFinancialAccount();
			return cfa==null ? null : (cfa.getAccountName()==null ? null : cfa.getAccountName().getValue());
		}
		return null;
	}

	@Override
	public void setPaymentAccountName(String name) {
		if(name==null) return;
		if(this.isCreditTransfer()) {
			super.getPayeePartyCreditorFinancialAccount().setAccountName(Text.create(name));	
		}
	}

	@Override
	public String getPaymentServiceProviderID() {
		if(this.isCreditTransfer()) {
			CreditorFinancialInstitutionType cfi = super.getPayeeSpecifiedCreditorFinancialInstitution();
			return cfi==null ? null : (cfi.getBICID()==null ? null : cfi.getBICID().getValue());
		}
		return null;
	}

	@Override
	public void setPaymentServiceProviderID(BICId bic) { // use ctor/factory
		if(bic==null) return;
		if(this.isCreditTransfer()) {
			CreditorFinancialInstitutionType cfi = new CreditorFinancialInstitutionType();
			cfi.setBICID(new ID(bic.getValue(), bic.getSchemeID()));
			super.setPayeeSpecifiedCreditorFinancialInstitution(cfi);
		}
	}

	@Override
	public void setPaymentServiceProviderID(String id) { // use ctor/factory
		if(id==null) return;
		if(this.isCreditTransfer()) {
			CreditorFinancialInstitutionType cfi = new CreditorFinancialInstitutionType();
			cfi.setBICID(new ID(id));
			super.setPayeeSpecifiedCreditorFinancialInstitution(cfi);
		}
	}

// ---------------- implements DirectDebit:
//	BG-19.BT-89 0..1 Mandate reference identifier - Ebene höher, in ApplicableHeaderTradeSettlement
//	BG-19.BT-90 0..1 Bank assigned creditor identifier - ebenfalls

	// BG-19.BT-91
	@Override
	public String getDebitedAccountID() {
		if(isDirectDebit()) {
			DebtorFinancialAccountType dfa = super.getPayerPartyDebtorFinancialAccount();
			return dfa==null ? null : (dfa.getIBANID()==null ? dfa.getProprietaryID().getValue() : dfa.getIBANID().getValue());
		}
		return null;
	}

	@Override
	public void setDebitedAccountID(String accountId) { // use ctor/factory
		if(accountId==null) return;
		if(isDirectDebit()) {
			DebtorFinancialAccountType debtorFinancialAccount = new DebtorFinancialAccountType();
			debtorFinancialAccount.setProprietaryID(new ID(accountId));		
			super.setPayerPartyDebtorFinancialAccount(debtorFinancialAccount);
		}
	}

	@Override
	public void setDebitedAccountID(IBANId debitedIban) {  // use ctor/factory
		if(debitedIban==null) return;
		if(isDirectDebit()) {
			DebtorFinancialAccountType debtorFinancialAccount = new DebtorFinancialAccountType();
			debtorFinancialAccount.setIBANID(new ID(debitedIban.getValue(), debitedIban.getSchemeID()));		
			super.setPayerPartyDebtorFinancialAccount(debtorFinancialAccount);
		}
	}

}
