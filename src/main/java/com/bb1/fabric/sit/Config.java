package com.bb1.fabric.sit;

import static com.bb1.fabric.bfapi.Constants.ID;

import com.bb1.fabric.bfapi.config.ConfigComment;
import com.bb1.fabric.bfapi.config.ConfigSub;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.PermissionLevel;

import net.minecraft.util.Identifier;

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
public class Config extends com.bb1.fabric.bfapi.config.Config {
	
	public Config() { super(new Identifier(ID, "fabricsit")); }
	
	@ConfigComment(contents = "If the player has to be standing in order to sitdown")
	public boolean requireStanding = true;
	/**
	 * @ApiNote If not >0 we ignore
	 */
	@ConfigComment(contents = "The max distance a player can br from a chair in order to sit down (if -1 its ignored)")
	public double maxDistanceToSit = -1;
	
	@ConfigComment(contents = "If the player needs an empty hand in order to sitdown")
	public boolean requireEmptyHand = true;
	
	@ConfigComment(contents = "If the block above a chair has to be air")
	public boolean noOpaqueBlockAbove = true;
	
	@ConfigComment(contents = "If the player needs a permission to use /sit")
	@ConfigSub(subOf = "permissions")
	public boolean requirePermission = false;
	
	@ConfigComment(contents = "The permission needed to use /sit")
	@ConfigSub(subOf = "permissions")
	public Permission permission = new Permission("fabricsit.sit", PermissionLevel.DEFAULT);
	
}
