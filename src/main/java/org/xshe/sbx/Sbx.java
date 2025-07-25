package org.xshe.sbx;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xshe.sbx.blocks.ShitBlock;
import org.xshe.sbx.itemgroups.ShitGroup;
import org.xshe.sbx.items.Shit;
import org.xshe.sbx.items.ShitStick;
import org.xshe.sbx.mtools.TShit;
import org.xshe.sbx.tools.CustomSounds;
import org.xshe.sbx.tools.Register;

public class Sbx implements ModInitializer {

    public static final String MOD_ID = "sbx";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        //注册并初始化自定义声音
        Registry.register(Registries.SOUND_EVENT, Identifier.of(MOD_ID, "shit_block"),
                SoundEvent.of(Identifier.of(MOD_ID, "shit_block")));
        CustomSounds.initialize();
        //先初始化物品
        //大便
        Shit.initialize();
        //大便棍
        ShitStick.initialize();
        //TShit
        TShit.initialize();
        //初始化方块
        //大便块
        ShitBlock.initialize();

        //初始化物品组
        ShitGroup.initialize();

    }
}
