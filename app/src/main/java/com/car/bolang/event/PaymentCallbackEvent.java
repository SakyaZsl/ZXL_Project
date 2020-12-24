package com.car.bolang.event;

public class PaymentCallbackEvent  {

    public PaymentCallbackEvent(int payStatus) {
        this.payStatus = payStatus;
    }

    public int payStatus;

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
