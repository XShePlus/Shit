package org.xshe.sbx.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.xshe.sbx.tools.Register;

public class Shit {
    //创建物品屎
    public static final Item shit =
            Register.itemRegister("shit",
                    Item::new,
                    new Item.Settings());
    //初始化函数
    public static void initialize(){
        //使屎可以用来堆肥(70%概率提升等级)
        CompostingChanceRegistry.INSTANCE.add(shit, 0.7f);
    }
}
