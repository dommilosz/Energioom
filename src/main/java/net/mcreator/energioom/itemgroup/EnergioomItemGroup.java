
package net.mcreator.energioom.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.energioom.item.EnergioomCrystalItem;
import net.mcreator.energioom.EnergioomModElements;

@EnergioomModElements.ModElement.Tag
public class EnergioomItemGroup extends EnergioomModElements.ModElement {
	public EnergioomItemGroup(EnergioomModElements instance) {
		super(instance, 17);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabenergioom") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(EnergioomCrystalItem.block);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
