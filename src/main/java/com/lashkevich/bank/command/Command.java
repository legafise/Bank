package com.lashkevich.bank.command;

@FunctionalInterface
public interface Command {

    void execute(String[] params);

}
