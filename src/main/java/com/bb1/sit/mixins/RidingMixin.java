package com.bb1.sit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
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
@Mixin(PlayerEntity.class)
public abstract class RidingMixin extends Entity {
	
	public RidingMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tickRiding()V", at = @At("HEAD"), cancellable = true)
	public void inject(CallbackInfo callbackInfo) {
		if (isSneaking() && hasVehicle()) { // The server will kick them off the entity
			Entity entity = getVehicle();
			if (entity.getType()==EntityType.ARMOR_STAND && entity.getCustomName().asString().startsWith("FABRIC_SEAT")) { // Its a seat so we should delete the entity
				entity.removeAllPassengers();
				entity.kill();
				entity.updatePosition(entity.getX(), entity.getY()+1, entity.getZ());
				callbackInfo.cancel();
			}
		}
	}
	
}
