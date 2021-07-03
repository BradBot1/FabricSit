package com.bb1.fabric.sit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class TeleportMixin extends Entity {
	
	public TeleportMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDismounted(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
	public void inject(Entity entity, CallbackInfo callbackInfo) {
		if (entity.getType()==EntityType.ARMOR_STAND && entity.getCustomName().asString().startsWith("FABRIC_SEAT")) { // Its a seat so we should delete the entity
			entity.removeAllPassengers();
			entity.kill();
		}
	}
	
}
