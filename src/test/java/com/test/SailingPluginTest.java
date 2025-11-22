package com.test;

import com.silentsailing.SilentSailingPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SailingPluginTest
{
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(SilentSailingPlugin.class);
        RuneLite.main(args);
    }
}