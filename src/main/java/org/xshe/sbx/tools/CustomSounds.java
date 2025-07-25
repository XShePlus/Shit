package org.xshe.sbx.tools;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.xshe.sbx.Sbx;

public class CustomSounds {
    private CustomSounds() {
        // private empty constructor to avoid accidental instantiation
    }

    public static final SoundEvent SHIT_BLOCK_SOUND = registerSound("shit_block_sound");

    // actual registration of all the custom SoundEvents
    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(Sbx.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }
    // 创建自定义声音组：只修改破坏声音
    public static BlockSoundGroup shitBlockGroup = new BlockSoundGroup(
            BlockSoundGroup.BAMBOO.getVolume(),
            BlockSoundGroup.BAMBOO.getPitch(),
            SHIT_BLOCK_SOUND,  // 破坏声音设为null
            SHIT_BLOCK_SOUND,
            SHIT_BLOCK_SOUND,
            SHIT_BLOCK_SOUND,
            SHIT_BLOCK_SOUND
    );
    // This static method starts class initialization, which then initializes
    // the static class variables (e.g. ITEM_METAL_WHISTLE).
    public static void initialize() {
        Sbx.LOGGER.info("Registering " + Sbx.MOD_ID + " Sounds");
        // Technically this method can stay empty, but some developers like to notify
        // the console, that certain parts of the mod have been successfully initialized
    }
}