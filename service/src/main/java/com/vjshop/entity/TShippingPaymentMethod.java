
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 配送方式-支付方式 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TShippingPaymentMethod implements Serializable {

    private static final long serialVersionUID = 183510670;

    private Long shippingMethods;
    private Long paymentMethods;

    public TShippingPaymentMethod() {}

    public TShippingPaymentMethod(TShippingPaymentMethod value) {
        this.shippingMethods = value.shippingMethods;
        this.paymentMethods = value.paymentMethods;
    }

    public TShippingPaymentMethod(
        Long shippingMethods,
        Long paymentMethods
    ) {
        this.shippingMethods = shippingMethods;
        this.paymentMethods = paymentMethods;
    }

    public Long getShippingMethods() {
        return this.shippingMethods;
    }

    public void setShippingMethods(Long shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public Long getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(Long paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TShippingPaymentMethod (");

        sb.append(shippingMethods);
        sb.append(", ").append(paymentMethods);

        sb.append(")");
        return sb.toString();
    }
}
