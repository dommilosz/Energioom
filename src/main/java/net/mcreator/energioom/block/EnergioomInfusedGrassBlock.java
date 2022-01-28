
package net.mcreator.energioom.block;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.World;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.energioom.procedures.EnergioomInfusedGrassEntityWalksOnTheBlockProcedure;
import net.mcreator.energioom.itemgroup.EnergioomItemGroup;
import net.mcreator.energioom.EnergioomModElements;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@EnergioomModElements.ModElement.Tag
public class EnergioomInfusedGrassBlock extends EnergioomModElements.ModElement {
	@ObjectHolder("energioom:energioom_infused_grass")
	public static final Block block = null;

	public EnergioomInfusedGrassBlock(EnergioomModElements instance) {
		super(instance, 20);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new BlockItem(block, new Item.Properties().group(EnergioomItemGroup.tab)).setRegistryName(block.getRegistryName()));
	}

	public static class CustomBlock extends Block {
		public CustomBlock() {
			super(Block.Properties.create(Material.ROCK).sound(SoundType.GROUND).hardnessAndResistance(50f, 10f).setLightLevel(s -> 4).harvestLevel(1)
					.harvestTool(ToolType.SHOVEL).setRequiresTool());
			setRegistryName("energioom_infused_grass");
		}

		@Override
		public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
			return 15;
		}

		@Override
		public void onEntityWalk(World world, BlockPos pos, Entity entity) {
			super.onEntityWalk(world, pos, entity);
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			BlockState blockstate = world.getBlockState(pos);

			EnergioomInfusedGrassEntityWalksOnTheBlockProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("entity", entity))
					.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}
	}
}
