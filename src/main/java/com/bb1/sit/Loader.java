package com.bb1.sit;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Loader implements DedicatedServerModInitializer {
	
	@Override
	public void onInitializeServer() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("sit").executes(context -> {
            	final ServerCommandSource source = context.getSource();
            	ServerPlayerEntity player;
            	try {
            		player = source.getPlayer();
            	} catch (Exception e) {
            		source.sendError(new LiteralText("You must be a player to run this command"));
            		return 0;
            	}
            	BlockState blockState = player.getEntityWorld().getBlockState(new BlockPos(player.getX(), player.getY()-1, player.getZ()));
            	if (player.hasVehicle() || player.isFallFlying() || player.isSleeping() || player.isSwimming() || player.isSpectator() || blockState.isAir() || blockState.getMaterial().isLiquid()) return 0;
            	Entity entity = createChair(player.getEntityWorld(), player.getBlockPos(), 1.7, player.getPos());
            	player.startRiding(entity, true);
            	return 1;
            }));
        });
		System.out.println("[FabricSit] Loaded! Thank you for using FabricSit");
	}
	
	public static Entity createChair(World world, BlockPos blockPos, double yOffset) {
		ArmorStandEntity entity = new ArmorStandEntity(world, 0.5d+blockPos.getX(), blockPos.getY()-yOffset, 0.5d+blockPos.getZ()) {
			
			@Override
			public boolean canMoveVoluntarily() {
				return false;
			}
			
			@Override
			public boolean collides() {
				return false;
			}
			
		};
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setCustomName(new LiteralText("FABRIC_SEAT"));
		entity.setNoGravity(true);
		world.spawnEntity(entity);
		return entity;
	}
	
	public static Entity createChair(World world, BlockPos blockPos, double yOffset, Vec3d target) {
		ArmorStandEntity entity = new ArmorStandEntity(world, 0.5d+blockPos.getX(), blockPos.getY()-yOffset, 0.5d+blockPos.getZ()) {
			
			@Override
			public boolean canMoveVoluntarily() {
				return false;
			}
			
			@Override
			public boolean collides() {
				return false;
			}
			
		};
		entity.lookAt(EntityAnchor.EYES, target.subtract(0, (target.getY()*2), 0));
		// entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setCustomName(new LiteralText("FABRIC_SEAT"));
		entity.setNoGravity(true);
		world.spawnEntity(entity);
		return entity;
	}
	
	
}
