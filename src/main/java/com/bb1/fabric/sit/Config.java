package com.bb1.fabric.sit;

import com.bb1.api.config.Storable;

public class Config extends com.bb1.api.config.Config {
	
	public Config() { super("fabricsit"); }
	
	@Storable public boolean requireStanding = true;
	/**
	 * @ApiNote If not >0 we ignore
	 */
	@Storable public double maxDistanceToSit = -1;
	
}
