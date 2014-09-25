package com.chaos.starke.db;

import java.util.Date;

import com.orm.SugarRecord;

public class Account extends SugarRecord<Account> {

    String name;
    String email;
    Locale locale;
    String token;
    Date expires;

    enum Locale {Facebook, Runkeeper}

}
