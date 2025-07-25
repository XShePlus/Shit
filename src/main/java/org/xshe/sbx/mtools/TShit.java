package org.xshe.sbx.mtools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import org.xshe.sbx.tools.Register;

public class TShit extends SwordItem {

    // 使用静态工厂方法代替单例
    public static TShit create(Item.Settings settings) {
        return new TShit(settings);
    }

    private TShit(Item.Settings settings) {
        super(
                ToolMaterial.STONE,      // 使用石剑材料
                1,                        // 附加基础伤害
                -2.4F,                    // 攻击速度修正值
                settings.maxCount(1)      // 物品设置：最大堆叠数为1
        );
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 给目标添加5秒凋零效果（等级1）
        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.WITHER,
                5 * 20,  // 5秒（20 ticks/秒）
                0         // 等级1
        ));

        // 给目标添加2秒力量效果（等级1）
        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.STRENGTH,
                2 * 20,  // 2秒
                0         // 等级1
        ));

        return super.postHit(stack, target, attacker);
    }
    public static final Item TSHIT=Register.itemRegister(
            "tshit",                     // 物品ID
            TShit::create,                // 使用工厂方法
            new Item.Settings()           // 物品基础设置
    );;

    public static void initialize() {

    }
}