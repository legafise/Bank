package com.lashkevich.bank;

import com.lashkevich.bank.command.impl.StartCommand;
import com.lashkevich.bank.util.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankApplication {

    public static void main(String[] args) {
        new StartCommand()
                .execute(new String[]{LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT))});
    }

}
