package com.shinhan.review.crawler;

public enum OS {
    AND("1"),IOS("2");

    private String number;

    OS(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
