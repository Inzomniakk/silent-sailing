package com.silentsailing;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.coords.WorldPoint;

@PluginDescriptor(
        name = "Silent Sailing",
        description = "Mutes NPC dialogue from other players' ships during Sailing.",
        tags = {"sailing", "mute", "dialogue", "npc"}
)
@Slf4j
public class SilentSailingPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private SilentSailingConfig config;

    private static final Set<Integer> SAILING_CREW_NPCS = ImmutableSet.of(
            15344, // Jobless Jim
            15334, // Ex-Captain Siad
            15256, // Adventurer Ada
            15265, // Cabin Boy Jenkins (ghost)
            15305, // Oarswoman Olga
            15275, // Jittery Jim
            15315, // Bosun Zarah
            15285, // Jolly Jim
            15326, // Spotter Virginia
            15295  // Sailor Jakob
    );

    @Override
    protected void startUp()
    {
        log.info("Silent Sailing started!");
    }

    @Override
    protected void shutDown()
    {
        log.info("Silent Sailing stopped!");
    }

    @Provides
    SilentSailingConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(SilentSailingConfig.class);
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event)
    {
        final Player localPlayer = client.getLocalPlayer();
        
        if (!config.muteOtherShips() && !config.muteOwnShip() || localPlayer == null)
        {
            return;
        }

        final Actor actor = event.getActor();

        if (!(actor instanceof NPC))
        {
            return;
        }

        final NPC npc = (NPC) actor;
        final int npcId = npc.getId();

        if (!SAILING_CREW_NPCS.contains(npcId))
        {
            return;
        }
        
        boolean shouldMute = false;

        if (config.muteOwnShip())
        {
            shouldMute = true;
        }
        else if (config.muteOtherShips())
        {
            boolean isLocalCrew = false;

            if (localPlayer.getInteracting() == npc)
            {
                isLocalCrew = true;
            }

            if (!isLocalCrew)
            {
                final WorldPoint localPlayerLocation = localPlayer.getWorldLocation();
                final WorldPoint npcLocation = npc.getWorldLocation();

                final int MAX_DISTANCE_FOR_LOCAL_SHIP = 8;

                if (localPlayerLocation != null && npcLocation != null && npcLocation.distanceTo(localPlayerLocation) <= MAX_DISTANCE_FOR_LOCAL_SHIP)
                {
                    isLocalCrew = true;
                }
            }

            if (!isLocalCrew)
            {
                shouldMute = true;
            }
        }

        if (shouldMute)
        {
            npc.setOverheadText("");
            log.debug("Muted overhead text from crew NPC ID: {}", npcId);
        }
        else
        {
            log.debug("Preserved overhead text from crew NPC ID: {}", npcId);
        }
    }
}