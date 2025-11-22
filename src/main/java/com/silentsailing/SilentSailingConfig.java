package com.silentsailing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("silentsailing")
public interface SilentSailingConfig extends Config
{
    @ConfigItem(
            keyName = "muteOtherShips",
            name = "Mute Other Players' Ships",
            description = "Mutes NPC dialogue from other players' ships during Sailing.",
            position = 1
    )
    default boolean muteOtherShips()
    {
        return true;
    }
}