package dev.berke.app.shared.constants;

public class OrderConstants {

    public static final String ORDER_AMOUNT_POSITIVE_MESSAGE = "Order amount should be positive";
    public static final String PAYMENT_METHOD_NOT_NULL_MESSAGE = "Payment method should be precised";
    public static final String CUSTOMER_ID_NOT_NULL_MESSAGE = "Customer should be present";
    public static final String PRODUCTS_NOT_EMPTY_MESSAGE = "You should purchase at least one product";
    public static final String PRODUCT_ID_NOT_NULL_MESSAGE = "Product is mandatory";
    public static final String QUANTITY_POSITIVE_MESSAGE = "Quantity is mandatory";
    public static final String CUSTOMER_NOT_FOUND_MESSAGE =
            "Cannot create order:: No customer exists with the provided ID";
    public static final String ORDER_NOT_FOUND_ERROR_MESSAGE = "No order found with the provided ID: ";
    public static final String BASKET_EMPTY_MESSAGE =
            "Cannot create order:: Basket is empty...";
    public static final String PRODUCT_QUANTITY_NOT_AVAILABLE = "Insufficient product quantity...";
}