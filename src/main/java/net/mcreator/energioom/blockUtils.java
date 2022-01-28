package net.mcreator.energioom;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class blockUtils {
    public static BlockPos HasConnectionTo(BlockPos from, Block block, World world) {
        Block block_from = world.getBlockState(from).getBlock();
        return HasConnectionTo(from, new Block[]{block_from}, block, world, 64, new BlockPos[]{});
    }

    public static BlockPos HasConnectionTo(BlockPos from, Block adj_block, Block block, World world) {
        return HasConnectionTo(from, new Block[]{adj_block}, block, world, 64, new BlockPos[]{});

    }

    public static BlockPos HasConnectionTo(BlockPos from, Block[] adj_blocks, Block block, World world) {
        return HasConnectionTo(from, adj_blocks, block, world, 64, new BlockPos[]{});

    }

    public static BlockPos HasConnectionTo(BlockPos from, Block[] adj_blocks, Block block, World world, int nest, BlockPos[] _already_checked) {
        if (nest <= 0) return null;
        List<BlockPos> already_checked = new ArrayList<>(Arrays.asList(_already_checked));
        if (already_checked.contains(from)) return null;
        already_checked.add(from);

        int sx = from.getX();
        int sy = from.getY();
        int sz = from.getZ();

        if (forgeUtils.getSidesOf(from, block, world).length > 0) {
            return forgeUtils.getSidesOf(from, block, world)[0].pos;
        }

        List<forgeUtils.sideBlock> blocks = new ArrayList<>();
        for (Block _block : adj_blocks) {
            blocks.addAll(Arrays.asList(forgeUtils.getSidesOf(from, _block, world)));
        }

        for (forgeUtils.sideBlock _block : blocks) {
            BlockPos p = HasConnectionTo(_block.pos, adj_blocks, block, world, nest - 1, already_checked.toArray(new BlockPos[0]));
            if (p != null) {
                return p;
            }
        }

        return null;
    }

    public static BlockPos[] GetConnectedBlocks(BlockPos from, Block[] adj_blocks, World world) {
        return GetConnectedBlocks(from, adj_blocks, world, 64, null);
    }

    public static BlockPos[] GetConnectedBlocks(BlockPos from, Block adj_blocks, World world) {
        return GetConnectedBlocks(from, new Block[]{adj_blocks}, world, 64, null);
    }

    public static BlockPos[] GetConnectedBlocks(BlockPos from, Block[] adj_blocks, World world, int nest, List<BlockPos> already_checked) {
        if (nest <= 0) return already_checked.toArray(new BlockPos[0]);
        if (already_checked == null) already_checked = new ArrayList<>();
        if (already_checked.contains(from)) return already_checked.toArray(new BlockPos[0]);
        already_checked.add(from);

        int sx = from.getX();
        int sy = from.getY();
        int sz = from.getZ();

        List<forgeUtils.sideBlock> blocks = new ArrayList<>();
        for (Block _block : adj_blocks) {
            blocks.addAll(Arrays.asList(forgeUtils.getSidesOf(from, _block, world)));
        }

        for (forgeUtils.sideBlock _block : blocks) {
            GetConnectedBlocks(_block.pos, adj_blocks, world, nest - 1, already_checked);
        }
        return already_checked.toArray(new BlockPos[0]);
    }
}
