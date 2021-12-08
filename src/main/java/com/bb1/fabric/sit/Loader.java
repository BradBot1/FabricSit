package com.bb1.fabric.sit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.fabricmc.api.ModInitializer;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
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
	
	public static final Event<Input<Loader>> LOADER_LOADED = new Event<Input<Loader>>(new Identifier("fabricsit:loaded"));
	
	public static final Event<Input<Entity>> CHAIR_CREATED = new Event<Input<Entity>>(new Identifier("fabricsit:chair_created"));
	
	private static final Config CONFIG = new Config();
	
	public static final @NotNull Config getConfig() { return CONFIG; }
	
	@Override
	public void onInitialize() {
		CONFIG.load();
		CONFIG.save();
		new SitEventListener();
		System.out.println("[FabricSit] Loaded! Thank you for using FabricSit");
		LOADER_LOADED.emit(Input.of(this));
	}
	
	public static Entity createChair(World world, BlockPos blockPos, double yOffset, @Nullable Vec3d target, boolean boundToBlock) {
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
				if (getEntityWorld().getBlockState(getCameraBlockPos()).isAir() && boundToBlock) { kill(); }
				super.tick();
			}
			
		};
		if (target!=null) entity.lookAt(EntityAnchor.EYES, target.subtract(0, (target.getY()*2), 0));
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setCustomName(new LiteralText("FABRIC_SEAT"));
		entity.setNoGravity(true);
		world.spawnEntity(entity);
		CHAIR_CREATED.emit(Input.of(entity));
		return entity;
	}
	
	
}
