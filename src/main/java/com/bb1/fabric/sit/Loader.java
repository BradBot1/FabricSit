package com.bb1.fabric.sit;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.events.Events;

import net.fabricmc.api.ModInitializer;
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
public class Loader implements ModInitializer {
	
	private static final Set<Entity> CHAIRS = new HashSet<Entity>();
	
	private static final Config CONFIG = new Config();
	
	public static final @NotNull Config getConfig() { return CONFIG; }
	
	@Override
	public void onInitialize() {
		CONFIG.load();
		CONFIG.save();
		Events.GameEvents.COMMAND_REGISTRATION_EVENT.register((dualInput)->{
			dualInput.get().register(CommandManager.literal("sit").executes(context -> {
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
		Events.GameEvents.STOP_EVENT.register((server)->{
			for (Entity entity : CHAIRS) {
				if (entity.isAlive()) { entity.kill(); }
			}
		});
		System.out.println("[FabricSit] Loaded! Thank you for using FabricSit");
	}
	
	public static Entity createChair(World world, BlockPos blockPos, double yOffset, @Nullable Vec3d target) {
		ArmorStandEntity entity = new ArmorStandEntity(world, 0.5d+blockPos.getX(), blockPos.getY()-yOffset, 0.5d+blockPos.getZ()) {
			
			private boolean v = false;
			
			@Override
			protected void addPassenger(Entity passenger) {
				super.addPassenger(passenger);
				v = true;
			}
			
			@Override
			public boolean canMoveVoluntarily() {
				return false;
			}
			
			@Override
			public boolean collides() {
				return false;
			}
			
			@Override
			public void tick() {
				if (v && getPassengerList().size()<1) { kill(); }
				super.tick();
			}
			
		};
		if (target!=null) entity.lookAt(EntityAnchor.EYES, target.subtract(0, (target.getY()*2), 0));
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setCustomName(new LiteralText("FABRIC_SEAT"));
		entity.setNoGravity(true);
		world.spawnEntity(entity);
		CHAIRS.add(entity);
		return entity;
	}
	
	
}
