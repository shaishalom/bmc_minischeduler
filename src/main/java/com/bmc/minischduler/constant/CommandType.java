package com.bmc.minischduler.constant;

public enum CommandType {
    COMMAND("Command"),
    SCRIPT("Script"),
    REST("Rest");

    private final String CommandType;

    public String getCommandType() {
        return CommandType;
    }

    CommandType(String CommandType) {
        this.CommandType = CommandType;
    }
}
