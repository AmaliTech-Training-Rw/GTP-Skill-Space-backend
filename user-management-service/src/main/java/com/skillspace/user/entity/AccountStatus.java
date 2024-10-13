package com.skillspace.user.entity;

public enum AccountStatus {
    PENDING,               //initial state when account is created
    PENDING_APPROVAL,     //applicable to companies after otp verification but before admin approval
    ACTIVE,              //verified accounts and active
    REJECTED,           //company status when rejected by admin
    SUSPENDED          //any account that has been disabled by admin
}

