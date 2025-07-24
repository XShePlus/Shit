package org.xshe.sbx;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xshe.sbx.blocks.ShitBlock;
import org.xshe.sbx.itemgroups.ShitGroup;
import org.xshe.sbx.items.Shit;

public class Sbx implements ModInitializer {

    public static final String MOD_ID = "sbx";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //先初始化物品
        Shit.initialize();
        //初始化方块
        ShitBlock.initialize();
        //初始化物品组
        ShitGroup.initialize();
    }
}
