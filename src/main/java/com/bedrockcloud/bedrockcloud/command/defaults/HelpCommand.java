package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;

public class HelpCommand extends Command implements Loggable
{
    public HelpCommand() {
        super("help");
    }
    
    @Override
    protected void onCommand(final String[] args) {
    }
}
