
package net.mcreator.energioom.gui;

import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.energioom.EnergioomMod;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;

import javax.naming.CompoundName;

@OnlyIn(Dist.CLIENT)
public class EnergioomShieldProjectorGUIGuiWindow extends ContainerScreen<EnergioomShieldProjectorGUIGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	private final static HashMap guistate = EnergioomShieldProjectorGUIGui.guistate;

	public EnergioomShieldProjectorGUIGuiWindow(EnergioomShieldProjectorGUIGui.GuiContainerMod container, PlayerInventory inventory,
			ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 176;
		this.ySize = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("energioom:textures/energioom_shield_projector_gui.png");

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeScreen();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		BlockPos pos = new BlockPos(this.x, this.y, this.z);
		CompoundNBT nbt = forgeUtils.getNBT(pos,world);
		if(nbt==null){
			this.font.drawString(ms, "Error:", 6, 25, -65536);
			return;
		}

		boolean enabled = nbt.getBoolean("protection_enabled");
		boolean tnt = nbt.getBoolean("explosionProtection_enabled");
		boolean self_prevent = nbt.getBoolean("selfPrevent_enabled");
		String owner = nbt.getString("owner-nick");
		double energy_usage = nbt.getDouble("energy_usage");
		double energy = energyUtils.getEnergy(pos,world);

		this.font.drawString(ms, "Enabled: "+enabled, 6, 25, -12829636);
		this.font.drawString(ms, "Anti TNT:"+tnt, 6, 52, -12829636);
		this.font.drawString(ms, "Self Prevent:"+self_prevent, 6, 79, -12829636);
		this.font.drawString(ms, "Power Usage: "+energy_usage, 6, 115, -12829636);
		this.font.drawString(ms, "Owner: "+owner, 6, 133, -12829636);
		this.font.drawString(ms, "Stored energy: "+energy+"/5000", 6, 151, -12829636);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		minecraft.keyboardListener.enableRepeatEvents(true);
		this.addButton(new Button(this.guiLeft + 105, this.guiTop + 16, 56, 20, new StringTextComponent("Toggle"), e -> {
			if (true) {
				EnergioomMod.PACKET_HANDLER.sendToServer(new EnergioomShieldProjectorGUIGui.ButtonPressedMessage(0, x, y, z));
				EnergioomShieldProjectorGUIGui.handleButtonAction(entity, 0, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 105, this.guiTop + 43, 56, 20, new StringTextComponent("Toggle"), e -> {
			if (true) {
				EnergioomMod.PACKET_HANDLER.sendToServer(new EnergioomShieldProjectorGUIGui.ButtonPressedMessage(1, x, y, z));
				EnergioomShieldProjectorGUIGui.handleButtonAction(entity, 1, x, y, z);
			}
		}));
		this.addButton(new Button(this.guiLeft + 105, this.guiTop + 70, 56, 20, new StringTextComponent("Toggle"), e -> {
			if (true) {
				EnergioomMod.PACKET_HANDLER.sendToServer(new EnergioomShieldProjectorGUIGui.ButtonPressedMessage(2, x, y, z));
				EnergioomShieldProjectorGUIGui.handleButtonAction(entity, 2, x, y, z);
			}
		}));
	}
}
