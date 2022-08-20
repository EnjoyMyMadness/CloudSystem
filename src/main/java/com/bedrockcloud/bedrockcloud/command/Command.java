package com.bedrockcloud.bedrockcloud.command;

import com.bedrockcloud.bedrockcloud.console.Loggable;

public class Command implements Loggable
{
    public String cmd;
    
    public Command(final String cmd) {
        this.cmd = cmd;
    }
    
    protected void onCommand(final String[] args) {
    }
    
    public String getCommand() {
        return this.cmd;
    }
}
