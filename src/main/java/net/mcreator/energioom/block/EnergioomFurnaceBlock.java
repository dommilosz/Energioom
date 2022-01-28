
package net.mcreator.energioom.block;

import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.loot.LootContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.NetworkManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.energioom.procedures.EnergioomFurnaceUpdateTickProcedure;
import net.mcreator.energioom.itemgroup.EnergioomItemGroup;
import net.mcreator.energioom.gui.EnergioomFurnaceGUIGui;
import net.mcreator.energioom.EnergioomModElements;

import javax.annotation.Nullable;

import java.util.stream.IntStream;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

import io.netty.buffer.Unpooled;

@EnergioomModElements.ModElement.Tag
public class EnergioomFurnaceBlock extends EnergioomModElements.ModElement {
    @ObjectHolder("energioom:energioom_furnace")
    public static final Block block = null;
    @ObjectHolder("energioom:energioom_furnace")
    public static final TileEntityType<CustomTileEntity> tileEntityType = null;
    public static final BooleanProperty ACTIVE = BlockStateProperties.ENABLED;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public EnergioomFurnaceBlock(EnergioomModElements instance) {
        super(instance, 36);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @Override
    public void initElements() {
        elements.blocks.add(() -> new CustomBlock());
        elements.items.add(() -> new BlockItem(block, new Item.Properties().group(EnergioomItemGroup.tab)).setRegistryName(block.getRegistryName()));
    }

    @SubscribeEvent
    public void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(CustomTileEntity::new, block).build(null).setRegistryName("energioom_furnace"));
    }

    public static class CustomBlock extends Block {
        public CustomBlock() {
            super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1f, 10f).setLightLevel(s -> 0).harvestLevel(1)
                    .harvestTool(ToolType.PICKAXE));
            this.setDefaultState(this.stateContainer.getBaseState().with(ACTIVE, false).with(FACING, Direction.NORTH));
            setRegistryName("energioom_furnace");
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(ACTIVE);
            builder.add(FACING);
            super.fillStateContainer(builder);
        }

        public BlockState rotate(BlockState state, Rotation rot) {
            return state.with(FACING, rot.rotate(state.get(FACING)));
        }

        public BlockState mirror(BlockState state, Mirror mirrorIn) {
            return state.rotate(mirrorIn.toRotation(state.get(FACING)));
        }

        @Override
        public BlockState getStateForPlacement(BlockItemUseContext context) {
            ;
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
            List<ItemStack> dropsOriginal = super.getDrops(state, builder);
            if (!dropsOriginal.isEmpty())
                return dropsOriginal;
            return Collections.singletonList(new ItemStack(this, 1));
        }

        @Override
        public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
            super.onBlockAdded(state, world, pos, oldState, moving);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            world.getPendingBlockTicks().scheduleTick(pos, this, 10);
        }

        @Override
        public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            super.tick(state, world, pos, random);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            {
                Map<String, Object> $_dependencies = new HashMap<>();
                $_dependencies.put("x", x);
                $_dependencies.put("y", y);
                $_dependencies.put("z", z);
                $_dependencies.put("world", world);
                EnergioomFurnaceUpdateTickProcedure.executeProcedure($_dependencies);
            }
            world.getPendingBlockTicks().scheduleTick(pos, this, 10);
        }

        @Override
        public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity entity, Hand hand,
                                                 BlockRayTraceResult hit) {
            super.onBlockActivated(state, world, pos, entity, hand, hit);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            if (entity instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) entity, new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("Energioom Furnace");
                    }

                    @Override
                    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
                        return new EnergioomFurnaceGUIGui.GuiContainerMod(id, inventory,
                                new PacketBuffer(Unpooled.buffer()).writeBlockPos(new BlockPos(x, y, z)));
                    }
                }, new BlockPos(x, y, z));
            }
            return ActionResultType.SUCCESS;
        }

        @Override
        public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            return tileEntity instanceof INamedContainerProvider ? (INamedContainerProvider) tileEntity : null;
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new CustomTileEntity();
        }

        @Override
        public boolean eventReceived(BlockState state, World world, BlockPos pos, int eventID, int eventParam) {
            super.eventReceived(state, world, pos, eventID, eventParam);
            TileEntity tileentity = world.getTileEntity(pos);
            return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
        }

        @Override
        public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
            if (state.getBlock() != newState.getBlock()) {
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof CustomTileEntity) {
                    InventoryHelper.dropInventoryItems(world, pos, (CustomTileEntity) tileentity);
                    world.updateComparatorOutputLevel(pos, this);
                }
                super.onReplaced(state, world, pos, newState, isMoving);
            }
        }

        @Override
        public boolean hasComparatorInputOverride(BlockState state) {
            return true;
        }

        @Override
        public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof CustomTileEntity)
                return Container.calcRedstoneFromInventory((CustomTileEntity) tileentity);
            else
                return 0;
        }
    }

    public static class CustomTileEntity extends LockableLootTileEntity implements ISidedInventory {
        private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

        protected CustomTileEntity() {
            super(tileEntityType);
        }

        @Override
        public void read(BlockState blockState, CompoundNBT compound) {
            super.read(blockState, compound);

            if (!this.checkLootAndRead(compound)) {
                this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            }

            ItemStackHelper.loadAllItems(compound, this.stacks);

            if (compound.get("energyStorage") != null)
                CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("energyStorage"));

        }

        @Override
        public CompoundNBT write(CompoundNBT compound) {
            super.write(compound);

            if (!this.checkLootAndWrite(compound)) {
                ItemStackHelper.saveAllItems(compound, this.stacks);
            }

            compound.put("energyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));

            return compound;
        }

        @Override
        public SUpdateTileEntityPacket getUpdatePacket() {
            return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
        }

        @Override
        public CompoundNBT getUpdateTag() {
            return this.write(new CompoundNBT());
        }

        @Override
        public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
            this.read(this.getBlockState(), pkt.getNbtCompound());
        }

        @Override
        public int getSizeInventory() {
            return stacks.size();
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack itemstack : this.stacks)
                if (!itemstack.isEmpty())
                    return false;
            return true;
        }

        @Override
        public ITextComponent getDefaultName() {
            return new StringTextComponent("energioom_furnace");
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public Container createMenu(int id, PlayerInventory player) {
            return new EnergioomFurnaceGUIGui.GuiContainerMod(id, player, new PacketBuffer(Unpooled.buffer()).writeBlockPos(this.getPos()));
        }

        @Override
        public ITextComponent getDisplayName() {
            return new StringTextComponent("Energioom Furnace");
        }

        @Override
        protected NonNullList<ItemStack> getItems() {
            return this.stacks;
        }

        @Override
        protected void setItems(NonNullList<ItemStack> stacks) {
            this.stacks = stacks;
        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return true;
        }

        @Override
        public int[] getSlotsForFace(Direction side) {
            return IntStream.range(0, this.getSizeInventory()).toArray();
        }

        @Override
        public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
            return this.isItemValidForSlot(index, stack);
        }

        @Override
        public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
            return true;
        }

        private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());
        private final EnergyStorage energyStorage = new EnergyStorage(2500, 500, 500, 0) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int retval = super.receiveEnergy(maxReceive, simulate);
                if (!simulate) {
                    markDirty();
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                }
                return retval;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int retval = super.extractEnergy(maxExtract, simulate);
                if (!simulate) {
                    markDirty();
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                }
                return retval;
            }
        };

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return handlers[facing.ordinal()].cast();
            if (!this.removed && capability == CapabilityEnergy.ENERGY)
                return LazyOptional.of(() -> energyStorage).cast();
            return super.getCapability(capability, facing);
        }

        @Override
        public void remove() {
            super.remove();
            for (LazyOptional<? extends IItemHandler> handler : handlers)
                handler.invalidate();
        }
    }
}