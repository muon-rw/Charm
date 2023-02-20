package svenhjol.charm_core.helper;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm_core.CharmCore;

import java.util.Locale;

public class EnumHelper {
    /**
     * Sound registration usually happens after the custom note block enum is processed.
     * soundEvent is made accessible so we can safely set it to the registered sound here.
     */
    public static void setNoteBlockSound(String noteBlockName, Holder<SoundEvent> soundEvent, NoteBlockInstrument.Type type) {
        try {
            NoteBlockInstrument noteBlock = NoteBlockInstrument.valueOf(noteBlockName.toUpperCase(Locale.ROOT));
            noteBlock.soundEvent = soundEvent;
            noteBlock.type = type;
        } catch (Exception e) {
            // Don't crash if this fails to assign.
            CharmCore.LOG.error("Failed to add sound " + soundEvent + " to the noteblock " + noteBlockName + ". Maybe enum mixin was not applied?");
        }
    }

    /**
     * Block registration usually happens after the custom boat type enum is processed.
     * planks is made accessible so we can safely set it to the registered block here.
     */
    public static void setBoatTypePlanks(String boatTypeName, Block planks) {
        try {
            Boat.Type boatType = Boat.Type.valueOf(boatTypeName.toUpperCase(Locale.ROOT));
            boatType.planks = planks;
        } catch (Exception e) {
            // Don't crash if this fails to assign.
            CharmCore.LOG.error("Failed to add planks " + planks + " to the boat type " + boatTypeName + ". Maybe enum mixin was not applied?");
        }
    }
}
