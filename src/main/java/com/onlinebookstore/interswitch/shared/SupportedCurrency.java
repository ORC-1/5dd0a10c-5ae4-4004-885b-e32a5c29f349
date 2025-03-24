package com.onlinebookstore.interswitch.shared;

public enum SupportedCurrency {
    NGN, GHS, ZAR, KES, USD;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
