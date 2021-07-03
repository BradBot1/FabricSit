package com.bb1.fabric.sit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMixin extends Entity {
	
	public DeathMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("TAIL"))
	public void inject(DamageSource source, CallbackInfo callbackInfo) {
		if (hasVehicle()) {
			Entity entity = getVehicle();
			if (entity.getType()==EntityType.ARMOR_STAND && entity.getCustomName().asString().startsWith("FABRIC_SEAT")) { // Its a seat so we should delete the entity
				entity.removeAllPassengers();
				entity.kill();
			}
		}
	}
	
}
