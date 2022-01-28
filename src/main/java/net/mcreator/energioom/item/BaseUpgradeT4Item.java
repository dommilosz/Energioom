
package net.mcreator.energioom.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.BlockState;

import net.mcreator.energioom.itemgroup.EnergioomItemGroup;
import net.mcreator.energioom.EnergioomModElements;

@EnergioomModElements.ModElement.Tag
public class BaseUpgradeT4Item extends EnergioomModElements.ModElement {
	@ObjectHolder("energioom:base_upgrade_t_4")
	public static final Item block = null;

	public BaseUpgradeT4Item(EnergioomModElements instance) {
		super(instance, 54);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(EnergioomItemGroup.tab).maxStackSize(64).rarity(Rarity.COMMON));
			setRegistryName("base_upgrade_t_4");
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
