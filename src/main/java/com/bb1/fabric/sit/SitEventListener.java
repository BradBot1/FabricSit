package com.bb1.fabric.sit;

import java.util.HashSet;
import java.util.Set;

import com.bb1.fabric.bfapi.events.EventListener;
import com.bb1.fabric.bfapi.permissions.PermissionUtils;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.Input;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SitEventListener implements EventListener {
	
	private final Set<Entity> _chairs = new HashSet<Entity>();
	
	public SitEventListener() {
		register();
	}
	
	@EventHandler(eventIdentifier = "minecraft:command_registration")
	public void onCommandRegister(Input<CommandDispatcher<ServerCommandSource>> input) {
		var conf = Loader.getConfig();
		input.get().register(CommandManager.literal("sit").requires(p->p.getEntity()!=null&&(!conf.requirePermission||PermissionUtils.hasPermission(Field.of(p.getEntity()), conf.permission.node()))).executes(context -> {
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
        	Entity entity = Loader.createChair(player.getEntityWorld(), player.getBlockPos(), new Vec3d(0, -1.7, 0), player.getPos(), false);
        	player.startRiding(entity, true);
        	return 1;
        }));
	}
	
	@EventHandler(eventIdentifier = "minecraft:server_stop")
	public void onStopped(Input<MinecraftServer> server) {
		for (Entity entity : this._chairs) {
			if (entity.isAlive()) { entity.kill(); }
		}
	}
	
	@EventHandler(eventIdentifier = "fabricsit:chair_created")
	public void onChairCreated(Input<Entity> chair) {
		this._chairs.add(chair.get());
	}
	
}
