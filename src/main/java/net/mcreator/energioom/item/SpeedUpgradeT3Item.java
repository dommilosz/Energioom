
package net.mcreator.energioom.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.BlockState;

import net.mcreator.energioom.itemgroup.EnergioomItemGroup;
import net.mcreator.energioom.EnergioomModElements;

@EnergioomModElements.ModElement.Tag
public class SpeedUpgradeT3Item extends EnergioomModElements.ModElement {
	@ObjectHolder("energioom:speed_upgrade_t_3")
	public static final Item block = null;

	public SpeedUpgradeT3Item(EnergioomModElements instance) {
		super(instance, 52);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(EnergioomItemGroup.tab).maxStackSize(32).rarity(Rarity.COMMON));
			setRegistryName("speed_upgrade_t_3");
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
