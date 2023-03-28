package com.atm.controller;

public enum AccountType {
    SAVING("SAVING"),
    CURRENT("CURRENT"),
    SALARY("SALARY");


    private final String type;
    private AccountType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
