// Shit.java
package org.xshe.sbx.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.*;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.xshe.sbx.tools.PlayerSneakHandler;
import org.xshe.sbx.tools.Register;

public class Shit {

    public static final ConsumableComponent SHIT_FOOD_CONSUMABLE_COMPONENT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 30 * 20, 1), 1.0f))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30 * 20, 1), 1.0f))
            .build();

    public static final FoodComponent SHIT_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .nutrition(-2)
            .build();

    // 创建物品屎 - 添加施肥功能
    public static final Item shit = Register.itemRegister("shit", settings ->
            new Item(settings.food(SHIT_FOOD_COMPONENT, SHIT_FOOD_CONSUMABLE_COMPONENT)) {

                // 添加施肥功能
                @Override
                public ActionResult useOnBlock(ItemUsageContext context) {
                    World world = context.getWorld();
                    BlockPos pos = context.getBlockPos();
                    PlayerEntity player = context.getPlayer();

                    // 先尝试施肥逻辑
                    if (tryFertilize(world, pos)) {
                        // 消耗物品（非创造模式）
                        if (player != null && !player.getAbilities().creativeMode) {
                            context.getStack().decrement(1);
                        }

                        // 播放音效
                        world.playSound(player, pos, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                        return ActionResult.SUCCESS;
                    }

                    // 如果施肥不适用，则调用原始行为（食用）
                    return super.useOnBlock(context);
                }

                // 核心施肥逻辑（效果弱于骨粉）
                private boolean tryFertilize(World world, BlockPos pos) {
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block instanceof Fertilizable fertilizable) {
                        if (fertilizable.isFertilizable(world, pos, state)) {
                            if (world instanceof ServerWorld serverWorld) {
                                // 20%成功率
                                if (world.random.nextFloat() < 0.20f) {
                                    fertilizable.grow(serverWorld, world.random, pos, state);
                                }
                                world.syncWorldEvent(1505, pos, 0);
                                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                            }
                            return true;
                        }
                    }

                    //  检查树苗（使用正确的方法）
                    if (block instanceof SaplingBlock sapling) {
                        if (world instanceof ServerWorld serverWorld) {
                            // 20%概率尝试生长
                            if (world.random.nextFloat() < 0.20f) {
                                // 1.21.4 兼容的方式：尝试生成树
                                sapling.generate(serverWorld, pos, state, world.random);
                            }
                            world.syncWorldEvent(1505, pos, 0);

                            // 1.21.4 兼容的游戏事件触发方式
                            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                        }
                        return true;
                    }


                    // 检查草方块（蔓延草和花）
                    if (block instanceof GrassBlock) {
                        if (world instanceof ServerWorld serverWorld) {
                            // 尝试在周围生成草和花（效果更弱）
                            for (int i = 0; i < 2; i++) { // 骨粉是3次，这里2次
                                BlockPos spreadPos = pos.add(
                                        world.random.nextInt(5) - 2,
                                        world.random.nextInt(2) - world.random.nextInt(2),
                                        world.random.nextInt(5) - 2
                                );

                                if (serverWorld.getBlockState(spreadPos).isOf(Blocks.DIRT)) {
                                    serverWorld.setBlockState(spreadPos, Blocks.GRASS_BLOCK.getDefaultState());
                                } else if (serverWorld.isAir(spreadPos)) {
                                    // 随机选择一种花
                                    BlockState flowerState = getRandomFlower(world.random);

                                    // 检查方块是否可以放置在这个位置
                                    if (canPlaceFlower(flowerState, serverWorld, spreadPos)) {
                                        serverWorld.setBlockState(spreadPos, flowerState);
                                    }
                                }
                            }
                            world.syncWorldEvent(1505, pos, 0);

                            // 1.21.4 兼容的游戏事件触发方式
                            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                        }
                        return true;
                    }

                    return false;
                }

                // 1.21.4 兼容的方式检查花朵是否可以放置
                private boolean canPlaceFlower(BlockState state, ServerWorld world, BlockPos pos) {
                    // 检查目标位置是空气
                    if (!world.isAir(pos)) {
                        return false;
                    }

                    // 检查下方方块是草方块或泥土
                    BlockState belowState = world.getBlockState(pos.down());
                    Block belowBlock = belowState.getBlock();

                    return belowBlock == Blocks.GRASS_BLOCK || belowBlock == Blocks.DIRT;
                }

                // 随机选择花朵类型
                private BlockState getRandomFlower(net.minecraft.util.math.random.Random random) {
                    Block[] flowers = {
                            Blocks.DANDELION,
                            Blocks.POPPY,
                            Blocks.BLUE_ORCHID,
                            Blocks.ALLIUM,
                            Blocks.AZURE_BLUET
                    };
                    return flowers[random.nextInt(flowers.length)].getDefaultState();
                }
            }, new Item.Settings());

    // 初始化函数
    public static void initialize() {
        // 使屎可以用来堆肥(70%概率提升等级)
        CompostingChanceRegistry.INSTANCE.add(shit, 0.7f);
        // 调用下蹲生成逻辑
        PlayerSneakHandler.init(shit);
        PlayerSneakHandler.register();
    }
}