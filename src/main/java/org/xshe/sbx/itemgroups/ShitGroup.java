package org.xshe.sbx.itemgroups;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.xshe.sbx.Sbx;
import org.xshe.sbx.blocks.ShitBlock;
import org.xshe.sbx.items.Shit;
import org.xshe.sbx.items.ShitStick;
import org.xshe.sbx.mtools.TShit;

public class ShitGroup {
    public static final RegistryKey<ItemGroup> shitGroupKey = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Sbx.MOD_ID, "item_group"));
    public static final ItemGroup shitGroup = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Shit.shit))
            .displayName(Text.translatable("itemGroup.sbx.shit"))
            .build();

    //物品组初始化函数
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, shitGroupKey, shitGroup);
        //添加物品
        ItemGroupEvents.modifyEntriesEvent(shitGroupKey).register(itemGroup -> {
            itemGroup.add(Shit.shit);
            itemGroup.add(ShitStick.shitStick);
            itemGroup.add(TShit.TSHIT);
            itemGroup.add(ShitBlock.SHIT_BLOCK);
        });
    }
}

