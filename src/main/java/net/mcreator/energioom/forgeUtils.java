package net.mcreator.energioom;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class forgeUtils {
    public static void sendMessage(PlayerEntity entity, String message, boolean actionBar) {
        entity.sendStatusMessage((ITextComponent) new StringTextComponent(message), actionBar);
    }

    public static void sendMessage(Entity entity, String message, boolean actionBar) {
        if (entity instanceof PlayerEntity)
            sendMessage((PlayerEntity) entity, message, actionBar);
    }

    public static void damageItemStack(ItemStack itemStack, int amount) {
        if (itemStack.attemptDamageItem((int) 1, new Random(), null)) {
            itemStack.shrink(1);
            itemStack.setDamage(0);
        }
    }

    public static int getGameMode(Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return ((ServerPlayerEntity) entity).interactionManager.getGameType().getID();
        } else if (entity instanceof PlayerEntity && entity.world.isRemote) {
            NetworkPlayerInfo _npi = Objects.requireNonNull(Minecraft.getInstance().getConnection())
                    .getPlayerInfo(((ClientPlayerEntity) entity).getGameProfile().getId());
            return Objects.requireNonNull(Objects.requireNonNull(_npi).getGameType()).getID();
        }
        return -1;
    }

    public static sideBlock[] getSideBlocks(BlockPos pos, World world) {
        ArrayList<sideBlock> sides = new ArrayList<>();
        sideBlock blockNX = new sideBlock(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), world);
        sideBlock blockPX = new sideBlock(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), world);
        sideBlock blockNY = new sideBlock(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), world);
        sideBlock blockPY = new sideBlock(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), world);
        sideBlock blockNZ = new sideBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), world);
        sideBlock blockPZ = new sideBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), world);
        sides.add(blockNX);
        sides.add(blockPX);
        sides.add(blockNY);
        sides.add(blockPY);
        sides.add(blockNZ);
        sides.add(blockPZ);
        sideBlock[] sidesArr = new sideBlock[sides.size()];
        for (int i = 0; i < sides.size(); i++)
            sidesArr[i] = sides.get(i);
        return sidesArr;
    }

    public static sideBlock[] getSidesOf(BlockPos pos, Block block, World world) {
        sideBlock[] sidesArr = getSideBlocks(pos, world);
        ArrayList<sideBlock> sides = new ArrayList<>();
        for (sideBlock side : sidesArr) {
            if (side.block.equals(block))
                sides.add(side);
        }
        sideBlock[] sidesArrO = new sideBlock[sides.size()];
        for (int i = 0; i < sides.size(); i++)
            sidesArrO[i] = sides.get(i);
        return sidesArrO;
    }

    public static class sideBlock {
        public Block block;

        public BlockPos pos;

        public BlockState state;

        public sideBlock(BlockPos pos, World world) {
            this.pos = pos;
            this.state = world.getBlockState(pos);
            this.block = this.state.getBlock();
        }
    }

    public static void setBlock(BlockPos pos, Block block, World world) {
        world.setBlockState(pos, block.getDefaultState());
    }

    public static void setProp(BlockPos pos, IntegerProperty property, int value, World world) {
        world.setBlockState(pos, world.getBlockState(pos).with(property, value));
    }

    public static void setNBTProp(BlockPos pos, String property, double value, World world) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState _bs = world.getBlockState(pos);
            if (tileEntity != null)
                tileEntity.getTileData().putDouble(property, value);
            world.notifyBlockUpdate(pos, _bs, _bs, 3);
        }
    }

    public static void setNBTProp(BlockPos pos, String property, String value, World world) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState _bs = world.getBlockState(pos);
            if (tileEntity != null)
                tileEntity.getTileData().putString(property, value);
            world.notifyBlockUpdate(pos, _bs, _bs, 3);
        }
    }

    public static void setNBTProp(BlockPos pos, String property, boolean value, World world) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState _bs = world.getBlockState(pos);
            if (tileEntity != null)
                tileEntity.getTileData().putBoolean(property, value);
            world.notifyBlockUpdate(pos, _bs, _bs, 3);
        }
    }

    public static void setNBTProp(BlockPos pos, String property, int[] value, World world) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState _bs = world.getBlockState(pos);
            if (tileEntity != null)
                tileEntity.getTileData().putIntArray(property, value);
            world.notifyBlockUpdate(pos, _bs, _bs, 3);
        }
    }

    public static void setNBTProp(BlockPos pos, String property, float value, World world) {
        setNBTProp(pos, property, (double) value, world);
    }

    public static void setNBTProp(BlockPos pos, String property, int value, World world) {
        setNBTProp(pos, property, (double) value, world);
    }

    public static void setNBTProp(BlockPos pos, BlockPos property, int[] value, World world) {
        setNBTProp(pos, property, value, world);
    }

    public static void setNBTProp(BlockPos pos, String property, BlockPos value, World world) {
        setNBTProp(pos, property, new int[]{value.getX(), value.getY(), value.getZ()}, world);
    }

    public static void setNBTProp(BlockPos pos, String property, BlockPos[] value, World world) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState _bs = world.getBlockState(pos);
            if (tileEntity != null) {
                List<Integer> data = new ArrayList<>();
                for (BlockPos _pos : value) {
                    data.add(_pos.getX());
                    data.add(_pos.getY());
                    data.add(_pos.getZ());
                }
                tileEntity.getTileData().putIntArray(property, data);
            }
            world.notifyBlockUpdate(pos, _bs, _bs, 3);
        }
    }

    public static CompoundNBT getNBT(BlockPos pos, World world) {
        try {
            TileEntity tileEntity = world.getTileEntity(pos);
            CompoundNBT tag = tileEntity.serializeNBT();
            return tileEntity.getTileData();
        } catch (Exception ex) {
            return null;
        }
    }

    public static BlockPos getNBTBlockPos(BlockPos pos, String property, World world) {
        try {
            TileEntity tileEntity = world.getTileEntity(pos);
            CompoundNBT tag = tileEntity.serializeNBT();
            int[] arr = tileEntity.getTileData().getIntArray(property);
            return new BlockPos(arr[0], arr[1], arr[2]);
        } catch (Exception e) {
            return null;
        }
    }

    public static BlockPos[] getNBTBlockPosArray(BlockPos pos, String property, World world) {
        try {
            TileEntity tileEntity = world.getTileEntity(pos);
            CompoundNBT tag = tileEntity.serializeNBT();
            int[] arr = tileEntity.getTileData().getIntArray(property);
            List<BlockPos> blocks = new ArrayList<>();
            for (int i = 0; i < arr.length; i += 3) {
                blocks.add(new BlockPos(arr[i], arr[i + 1], arr[i + 2]));
            }
            return blocks.toArray(new BlockPos[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setProp(BlockPos pos, BooleanProperty property, boolean value, World world) {
        world.setBlockState(pos, world.getBlockState(pos).with(property, value));
    }

    public static Object getProp(BlockState state, Property property) {
        return state.getProperties().contains(property) ? state.get(property) : null;
    }

    public static Object getProp(BlockPos pos, Property property, World world) {
        return getProp(world.getBlockState(pos), property);
    }

    public static void removeBlock(BlockPos pos, boolean drop, World world) {
        world.destroyBlock(pos, drop);
    }

    public static boolean isBlockPowered(BlockPos pos, World world) {
        return world.isBlockPowered(pos);
    }

    public static void AddAchievement(ServerPlayerEntity entity, ResourceLocation adv) {
        Advancement _adv = ((MinecraftServer) ((ServerPlayerEntity) entity).server).getAdvancementManager()
                .getAdvancement(adv);
        assert _adv != null;
        AdvancementProgress _ap = ((ServerPlayerEntity) entity).getAdvancements().getProgress(_adv);
        if (!_ap.isDone()) {
            for (String _criterion : _ap.getRemaningCriteria()) {
                ((ServerPlayerEntity) entity).getAdvancements().grantCriterion(_adv, _criterion);
            }
        }
    }

    public static void AddAchievement(PlayerEntity entity, ResourceLocation adv) {
        if (entity instanceof ServerPlayerEntity) {
            AddAchievement((ServerPlayerEntity) entity, adv);
        }
    }

    public static class itemUtils {
        public static IItemHandler getInventoryCapability(BlockPos pos, World world) {
            TileEntity tileEntity = world.getTileEntity(pos);
            AtomicReference<IItemHandler> capability = new AtomicReference<>(null);
            if (tileEntity != null)
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability::set);
            return capability.get();
        }

        public static ItemStack getItemStackGUI(Entity entity, int slot) {
            PlayerEntity player = (PlayerEntity) entity;
            Container container = player.openContainer;
            if (container instanceof Supplier) {
                Object invobj = ((Supplier) container).get();
                if (invobj instanceof Map) {
                    return ((Slot) ((Map) invobj).get((int) (slot))).getStack();
                }
            }
            return null;
        }

        public static ItemStack getItemStack(BlockPos pos, int slot, World world) {
            return getInventoryCapability(pos, world).getStackInSlot(slot);
        }

        public static int getItemCount(BlockPos pos, int slot, World world) {
            return getInventoryCapability(pos, world).getStackInSlot(slot).getCount();
        }

        public static int getItemCountOfType(BlockPos pos, int slot, Item item, World world) {
            if (equals(pos, slot, item, world)) {
                return getInventoryCapability(pos, world).getStackInSlot(slot).getCount();
            }
            return 0;
        }

        public static boolean equals(BlockPos pos, int slot, Item item, World world) {
            return getItemStack(pos, slot, world).getItem() == item;
        }

        public static boolean equals(Item itemA, Item itemB, World world) {
            return itemA == itemB;
        }

        public static boolean equalsGUI(Entity entity, int slot, Item item) {
            return getItemStackGUI(entity, slot).getItem() == item;
        }

        public static boolean equalsGUI(Entity entity, int slot, ItemStack item) {
            return getItemStackGUI(entity, slot).getItem() == item.getItem();
        }

        public static boolean equalsGUI(Entity entity, int slot, Block item) {
            return getItemStackGUI(entity, slot).getItem() == item.asItem();
        }

        public static boolean consumeItem(BlockPos pos, Item item, int slot, World world) {
            if (forgeUtils.itemUtils.equals(pos, slot, item, world)) {
                if (forgeUtils.itemUtils.getItemCount(pos, slot, world) > 0) {
                    forgeUtils.itemUtils.changeStackAmountRelative(pos, slot, -1, world);
                    return true;
                }
            }
            return false;
        }

        public static boolean canFitItemStack(ItemStack existing, ItemStack itemstack, World world) {

            if (existing.getCount() + itemstack.getCount() <= 64) {
                if (equals(existing.getItem(), blockToItem(Blocks.AIR), world)) {
                    return true;
                }
                if (equals(existing.getItem(), itemstack.getItem(), world)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean canFitItemStack(BlockPos pos, int slot, ItemStack itemstack, World world) {
            ItemStack existing = getItemStack(pos, slot, world);
            return canFitItemStack(existing, itemstack, world);
        }


        public static boolean equals(BlockPos pos, int slot, Block item, World world) {
            return getItemStack(pos, slot, world).getItem() == new ItemStack(item, 0).getItem();
        }

        public static Item blockToItem(Block block) {
            return new ItemStack(block, 0).getItem();
        }

        public static void setItemStack(BlockPos pos, int slot, ItemStack change, World world) {
            IItemHandler capability = getInventoryCapability(pos, world);
            if (capability instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable) capability).setStackInSlot(slot, change);
            }
        }

        public static void changeStackAmountRelative(BlockPos pos, int slot, int change, World world) {
            IItemHandler capability = getInventoryCapability(pos, world);
            if (capability instanceof IItemHandlerModifiable) {
                ItemStack itemStack = capability.getStackInSlot(slot);
                itemStack.grow(change);

                ((IItemHandlerModifiable) capability).setStackInSlot(slot, itemStack);
            }
        }

        public static void changeStackAmountRelativeGUI(Entity entity, int slot, int change, World world) {
            if (!(entity instanceof ServerPlayerEntity)) return;
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            Container container = player.openContainer;
            if (container instanceof Supplier) {
                Object invobj = ((Supplier) container).get();
                if (invobj instanceof Map) {
                    ((Slot) ((Map) invobj).get((int) (slot))).decrStackSize(-change);
                    container.detectAndSendChanges();
                }
            }
        }

        public static void setStackAmount(BlockPos pos, int slot, int amount, World world) {
            IItemHandler capability = getInventoryCapability(pos, world);
            if (capability instanceof IItemHandlerModifiable) {
                ItemStack itemStack = capability.getStackInSlot(slot);
                itemStack.setCount(amount);

                ((IItemHandlerModifiable) capability).setStackInSlot(slot, itemStack);
            }
        }

        public static void setStackAmountGUI(Entity entity, int slot, int amount) {
            ItemStack prev = getItemStackGUI(entity, slot);
            prev.setCount(amount);
            setStackGUI(entity, prev, slot);
        }

        public static void setStackGUI(Entity entity, ItemStack itemStack, int slot) {
            if (!(entity instanceof ServerPlayerEntity)) return;
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            Container container = player.openContainer;
            if (container instanceof Supplier) {
                Object invobj = ((Supplier) container).get();
                if (invobj instanceof Map) {
                    ((Slot) ((Map) invobj).get((int) (slot))).putStack(itemStack);
                    container.detectAndSendChanges();
                }
            }
        }

        public static Item smeltingResult(Item item, World world) {
            ItemStack itemstack = new ItemStack(item);
            return ((world.getRecipeManager()
                    .getRecipe(IRecipeType.SMELTING, new Inventory((itemstack)), world).isPresent()
                    ? world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory((itemstack)), world).get()
                    .getRecipeOutput().copy()
                    : ItemStack.EMPTY).getItem());
        }

        public static boolean processItem(BlockPos pos, itemStackSlotInfo[] input, itemStackSlotInfo[] output, boolean simulation, World world) {
            for (itemStackSlotInfo item : input) {
                if (getItemCountOfType(pos, item.slot, item.item, world) < item.amount) {
                    return false;
                }
            }
            for (itemStackSlotInfo item : output) {
                if (!canFitItemStack(pos, item.slot, new ItemStack(item.item, item.amount), world)) return false;
            }
            if (simulation) return true;
            for (itemStackSlotInfo item : input) {
                changeStackAmountRelative(pos, item.slot, -item.amount, world);
            }
            for (itemStackSlotInfo item : output) {
                if (getItemStack(pos, item.slot, world).getItem() != item.item) {
                    setItemStack(pos, item.slot, new ItemStack(item.item, item.amount), world);
                } else {
                    changeStackAmountRelative(pos, item.slot, item.amount, world);
                }
            }
            return true;
        }

        public static boolean processItem(BlockPos pos, itemStackSlotInfo input, itemStackSlotInfo output, boolean simulation, World world) {
            return processItem(pos, new itemStackSlotInfo[]{input}, new itemStackSlotInfo[]{output}, simulation, world);
        }

        public static boolean smeltItem(BlockPos pos, int inputSlot, int outputSlot, boolean simulation, World world) {
            Item smeltingResult = smeltingResult(getItemStack(pos, inputSlot, world).getItem(), world);
            if (smeltingResult != Items.AIR)
                return processItem(pos, new itemStackSlotInfo(inputSlot, getItemStack(pos, inputSlot, world).getItem()), new itemStackSlotInfo(outputSlot, smeltingResult), simulation, world);
            return false;
        }

        public static class itemStackSlotInfo {
            public int slot;
            public Item item;
            public int amount;

            public itemStackSlotInfo(int _slot, int _amount, Item _item) {
                item = _item;
                slot = _slot;
                amount = _amount;
            }

            public itemStackSlotInfo(int _slot, Item _item) {
                item = _item;
                slot = _slot;
                amount = 1;
            }
        }
    }

    public static class guiUtils {
        public static void drawRect(int x, int y, int w, int h, Color c) {
            MatrixStack ms = new MatrixStack();
            AbstractGui.fill(ms, x, y, w, h, c.getRGB());
        }

        public static void drawRectHollow(int x, int y, int w, int h, int lineThickness, Color c) {
            MatrixStack ms = new MatrixStack();
            AbstractGui.fill(ms, x, y, x + w, y + lineThickness, c.getRGB());
            AbstractGui.fill(ms, x, y, x + lineThickness, y + h, c.getRGB());

            AbstractGui.fill(ms, x + w, y, x + w + lineThickness, y + h, c.getRGB());
            AbstractGui.fill(ms, x, y + h, x + w, y + h + lineThickness, c.getRGB());
        }

        public static void drawTexture(int x, int y, int w, int h, ResourceLocation location) {
            Minecraft.getInstance().getTextureManager().bindTexture(location);
            MatrixStack ms = new MatrixStack();
            AbstractGui.blit(ms, x, y, 0, 0, w, h, w, h);
        }

        public static int getHWFromProgress(int max, int progress) {
            double part = ((float) max / 100);
            if (progress > 100) progress = 100;
            return (int) Math.floor(part * progress);
        }

        public static void drawVProgress(int x, int y, int w, int h, int progress, Color c) {
            int h2 = getHWFromProgress(h, progress);

            drawRectHollow(x, y, w + 1, h + 1, 1, Color.black);
            drawRect(x, y, w, h2, c);
        }
    }
}
