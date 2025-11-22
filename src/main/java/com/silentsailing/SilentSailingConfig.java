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

    @ConfigItem(
            keyName = "muteOwnShip",
            name = "Mute My Ship's Crew",
            description = "Mutes ambient and action-related dialogue from your own ship's crew (e.g., \"Trimming the sails!\").",
            position = 2
    )
    default boolean muteOwnShip()
    {
        return false;
    }
}