/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.energioom as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.energioom;

import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.atomic.AtomicReference;

public class VariableUtils {

	public static void SyncPlayerVariables(PlayerEntity entity) {
		GetPlayerVariables(entity).syncPlayerVariables(entity);
	}

	public static EnergioomModVariables.PlayerVariables GetPlayerVariables(PlayerEntity entity) {
		AtomicReference<EnergioomModVariables.PlayerVariables> cap = new AtomicReference<>(null);
		entity.getCapability(EnergioomModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(cap::set);
		return cap.get();
	}
}
