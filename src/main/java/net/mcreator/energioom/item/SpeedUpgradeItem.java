
package net.mcreator.energioom.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.BlockState;

import net.mcreator.energioom.itemgroup.EnergioomItemGroup;
import net.mcreator.energioom.EnergioomModElements;

@EnergioomModElements.ModElement.Tag
public class SpeedUpgradeItem extends EnergioomModElements.ModElement {
	@ObjectHolder("energioom:speed_upgrade")
	public static final Item block = null;

	public SpeedUpgradeItem(EnergioomModElements instance) {
		super(instance, 42);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(EnergioomItemGroup.tab).maxStackSize(2).rarity(Rarity.COMMON));
			setRegistryName("speed_upgrade");
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
		}
	}
}
