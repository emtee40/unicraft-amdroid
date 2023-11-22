package com.rk.unicraft;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
//import cc.catgasm.Minecraft;
//import com.itsaky.androidide.logsender;
import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;
/* Launches the Android application. */
public class MainActivity extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* You can adjust this configuration to fit your needs */
        
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		configuration.useImmersiveMode = true;
		initialize(new com.rk.unicraft.Minecraft(), configuration);
	}
}