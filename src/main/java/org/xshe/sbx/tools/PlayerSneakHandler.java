package org.xshe.sbx.tools;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSneakHandler {
    // 冷却时间系统 (玩家UUID -> 剩余冷却ticks)
    private static final Map<UUID, Integer> COOLDOWN_MAP = new HashMap<>();
    private static final int COOLDOWN_TICKS = 20; // 1秒冷却

    // 要生成的物品引用
    private static Item itemToSpawn;

    // 初始化方法（设置要生成的物品）
    public static void init(Item item) {
        itemToSpawn = item;
    }

    // 注册事件处理器
    public static void register() {
        // 玩家登出时清除冷却数据
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            COOLDOWN_MAP.remove(handler.player.getUuid());
        });

        // 服务器每tick处理玩家状态
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getWorld() instanceof ServerWorld world) {
                    handlePlayerSneak(player, world);
                }
            }
        });
    }

    // 处理玩家下蹲逻辑
    private static void handlePlayerSneak(PlayerEntity player, ServerWorld world) {
        UUID playerId = player.getUuid();
        int cooldown = COOLDOWN_MAP.getOrDefault(playerId, 0);

        // 更新冷却时间
        if (cooldown > 0) {
            COOLDOWN_MAP.put(playerId, cooldown - 1);
            return;
        }

        // 检查玩家是否下蹲且不是旁观者
        if (player.isSneaking() && !player.isSpectator()) {
            // 15%概率触发生成
            if (world.random.nextFloat() < 0.15f) {
                spawnItemAtPlayerFeet(player, world);
                COOLDOWN_MAP.put(playerId, COOLDOWN_TICKS);
            }
        }
    }

    // 在玩家脚底生成物品
    private static void spawnItemAtPlayerFeet(PlayerEntity player, ServerWorld world) {
        // 获取玩家位置并计算生成点（脚底位置）
        Vec3d playerPos = player.getPos();
        Vec3d spawnPos = playerPos.add(0, -0.5, 0); // 调整到脚底高度

        // 创建物品堆栈
        ItemStack itemStack = new ItemStack(itemToSpawn);

        // 创建物品实体
        ItemEntity itemEntity = new ItemEntity(
                world,
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                itemStack
        );

        // 添加轻微随机速度
        itemEntity.setVelocity(
                world.random.nextDouble() * 0.1 - 0.05,
                0.2,
                world.random.nextDouble() * 0.1 - 0.05
        );

        // 在世界中生成物品实体
        world.spawnEntity(itemEntity);
    }
}