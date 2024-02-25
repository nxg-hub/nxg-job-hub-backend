package core.nxg.subscription.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    CUSTOMER_IDENTIFICATION_FAILED("customeridentification.failed"),
    CUSTOMER_IDENTIFICATION_SUCCESS("customeridentification.success"),
    CHARGE_DISPUTE_CREATE ("charge.dispute.create")	,
    CHARGE_DISPUTE_REMIND("charge.dispute.remind")	,
    CHARGE_DISPUTE_RESOLVE("charge.dispute.resolve")	,
    CHARGE_FAILED("charge.failed")	,
    CHARGE_SUCCESS("charge.success")	,
    DEDICATED_ACCOUNT_ASSIGN_FAILED("dedicatedaccount.assign.failed")	,
    DEDICATED_ACCOUNT_ASSIGN_SUCCESS("dedicatedaccount.assign.success")	,
    INVOICE_CREATE("invoice.create")	,
    INVOICE_PAYMENT_FAILED("invoice.payment_failed")	,
    INVOICE_UPDATE("invoice.update")	,
    PAYMENT_REQUEST_PENDING("paymentrequest.pending")	,
    PAYMENT_REQUEST_SUCCESS("paymentrequest.success")	,
    REFUND_FAILED("refund.failed")	,
    REFUND_PENDING("refund.pending")	,
    REFUND_PROCESSED("refund.processed")	,
    REFUND_PROCESSING("refund.processing")	,
    SUBSCRIPTION_CREATE("subscription.create")	,
    SUBSCRIPTION_DISABLE("subscription.disable")	,
    SUBSCRIPTION_EXPIRING_CARDS("subscription.expiring_cards")	,
    SUBSCRIPTION_NOT_RENEW("subscription.not_renew")	,
    TRANSFER_FAILED("transfer.failed")	,
    TRANSFER_SUCCESS("transfer.success")	,
    TRANSFER_REVERSED("transfer.reversed");


   private final String event;




}
