package core.nxg.subscription.enums;

public class APIConstants {

    public static final String BASE_URL = "https://api.paystack.co";
    public static final String PAYSTACK_INIT_TRANSACTIONS = BASE_URL + "/transaction/initialize";
    public static final String PAYSTACK_VERIFY_TRANSACTIONS = BASE_URL + "/transaction/verify/";
    public static final String PAYSTACK_CHARGE_AUTHORIZATION = BASE_URL + "/transaction/charge_authorization";

    //URL definitions for customer endpoint
    public static final String PAYSTACK_CUSTOMER_URL = BASE_URL + "/customer";

    //URL definitions for transaction endpoints
    public static final String PAYSTACK_TRANSACTIONS_INITIALIZE_TRANSACTION = BASE_URL + "/transaction/initialize";
    public static final String PAYSTACK_TRANSACTIONS_VERIFY_TRANSACTION = BASE_URL + "/transaction/verify/";
    public static final String PAYSTACK_TRANSACTIONS_LIST_TRANSACTIONS = BASE_URL + "/transaction";
    public static final String PAYSTACK_TRANSACTIONS_CHARGE_TOKEN = BASE_URL + "/transaction/charge_token";
    public static final String PAYSTACK_TRANSACTIONS_EXPORT_TRANSACTIONS = BASE_URL + "/transaction/export";

    //URL definitions for plan endpoint
    public static final String PAYSTACK_PLANS_CREATE_PLAN = BASE_URL + "/plan";
    public static final String PAYSTACK_PLANS_LIST_PLANS = BASE_URL + "/plan";
    public static final String PAYSTACK_PLANS_FETCH_PLAN = BASE_URL + "/plan/";
    public static final String PAYSTACK_PLANS_UPDATE_PLAN = BASE_URL + "/plan/";

    //URL definitions for subscription endpoints
    public static final String PAYSTACK_SUBSCRIPTIONS_CREATE_SUBSCRIPTION = BASE_URL + "/subscription";
    public static final String PAYSTACK_SUBSCRIPTIONS_DISABLE_SUBSCRIPTION = BASE_URL + "/subscription/disable";
    public static final String PAYSTACK_SUBSCRIPTIONS_ENABLE_SUBSCRIPTION = BASE_URL + "/subscription/enable";
    public static final String PAYSTACK_SUBSCRIPTIONS_FETCH_SUBSCRIPTION = BASE_URL + "/subscription/";

    //URL definitions for page endpoint
    public static final String PAYSTACK_PAGES_CREATE_PAGE = BASE_URL + "/page";
    public static final String PAYSTACK_PAGES_LIST_PAGES = BASE_URL + "/page";
    public static final String PAYSTACK_PAGES_FETCH_PAGE = BASE_URL + "/page/";
    public static final String PAYSTACK_PAGES_UPDATE_PAGE = BASE_URL + "/page/";



}
