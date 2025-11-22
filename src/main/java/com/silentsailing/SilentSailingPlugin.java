package com.silentsailing;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.Set;
import com.google.common.collect.ImmutableSet;

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
        if (!config.muteOtherShips() || client.getLocalPlayer() == null)
        {
            return;
        }

        Actor actor = event.getActor();
        
        if (!(actor instanceof NPC))
        {
            return;
        }

        NPC npc = (NPC) actor;

        int npcId = npc.getId();

        if (!SAILING_CREW_NPCS.contains(npcId))
        {
            return;
        }

        if (client.getLocalPlayer().getInteracting() == npc)
        {
            return;
        }
        
        npc.setOverheadText("");
        log.debug("Muted overhead text from NPC ID: {}", npcId);
    }
}