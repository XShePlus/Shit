package org.xshe.sbx.blocks;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import org.xshe.sbx.tools.CustomSounds;
import org.xshe.sbx.tools.Register;

public class ShitBlock {
    public static final Block SHIT_BLOCK = Register.blockRegister(
            "shit_block",
            Block::new,
            AbstractBlock.Settings.create().sounds(CustomSounds.shitBlockGroup),
            true
    );
    //初始化函数
    public static void initialize(){

    }
}
