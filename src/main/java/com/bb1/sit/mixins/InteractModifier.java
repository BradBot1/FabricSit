package com.bb1.sit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.sit.Loader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
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
@Mixin(ServerPlayerInteractionManager.class)
public abstract class InteractModifier {
	
	@Shadow public ServerPlayerEntity player;
	@Shadow public GameMode gameMode;
	
	@Inject(method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void inject(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
		if (gameMode==GameMode.SPECTATOR || !player.inventory.getMainHandStack().isEmpty() || player.isSneaking()) return;
		BlockPos blockPos = hitResult.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (!(block instanceof StairsBlock || block instanceof SlabBlock)) return;
		Entity chair = Loader.createChair(world, blockPos, 1.2);
		Entity v = player.getVehicle();
		if (v!=null) {
			player.setSneaking(true);
			player.tickRiding();
		}
		player.startRiding(chair, true);
		callbackInfoReturnable.setReturnValue(ActionResult.success(true));
		callbackInfoReturnable.cancel();
	}
	
	@Inject(method = "setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V", at = @At("HEAD"))
	public void inject(GameMode gameMode, GameMode previousGameMode, CallbackInfo callbackInfo) {
		if (gameMode==GameMode.SPECTATOR && previousGameMode!=GameMode.SPECTATOR && player.getVehicle()!=null) {
			player.setSneaking(true);
			player.tickRiding();
		}
	}
	
}
