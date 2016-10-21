package com.globbypotato.rockhounding_chemistry.machines.gui;

import java.util.Arrays;
import java.util.List;

import com.globbypotato.rockhounding_chemistry.handlers.EnumFluid;
import com.globbypotato.rockhounding_chemistry.handlers.ModRecipes;
import com.globbypotato.rockhounding_chemistry.handlers.Reference;
import com.globbypotato.rockhounding_chemistry.machines.container.ContainerLabOven;
import com.globbypotato.rockhounding_chemistry.machines.tileentity.TileEntityLabOven;
import com.globbypotato.rockhounding_chemistry.proxy.CommonProxy;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLabOven extends GuiBase {
	private final InventoryPlayer playerInventory;
	private final TileEntityLabOven labOven;
	public static final int WIDTH = 176;
	public static final int HEIGHT = 195;

	public GuiLabOven(InventoryPlayer playerInv, TileEntityLabOven tile){
		super(tile,new ContainerLabOven(playerInv, tile));
		this.playerInventory = playerInv;
		TEXTURE = new ResourceLocation(Reference.MODID + ":textures/gui/guilaboven.png");
		this.labOven = tile;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		//solvent icon
		if(mouseX >= 118+x && mouseX <= 130+x && mouseY >= 77+y && mouseY <= 92+y){
			String toolStr = "";
			if(labOven.currentRecipeIndex() < 0) toolStr = EnumFluid.EMPTY.getName();
			else toolStr = ModRecipes.getLabOvenSolvent(labOven.getTemplate().getStackInSlot(0)).getName();
			List<String> tooltip = Arrays.asList(toolStr);
			drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
		}

		//recipe buttons
		if(mouseX >= 29+x && mouseX <= 42+x && mouseY >= 20+y && mouseY <= 35+y){
			String[] text = {"Previous Recipe"};
			List<String> tooltip = Arrays.asList(text);
			drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
		}
		if(mouseX >= 134+x && mouseX <= 146+x && mouseY >= 20+y && mouseY <= 35+y){
			String[] text = {"Next Recipe"};
			List<String> tooltip = Arrays.asList(text);
			drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
		}
		//bars progression (fuel-redstone)
		if(mouseX >= 11+x && mouseX <= 20+x && mouseY >= 40+y && mouseY <= 89+y){
			String[] text = {this.labOven.getField(0) + "/" + this.labOven.getField(1) + " ticks"};
			List<String> tooltip = Arrays.asList(text);
			drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
		}
		if(mouseX >= 155+x && mouseX <= 164+x && mouseY >= 40+y && mouseY <= 89+y){
			String[] text = {this.labOven.getField(4) + "/" + this.labOven.getField(5) + " RF"};
			List<String> tooltip = Arrays.asList(text);
			drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
		}
	}

	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 92 + 2, 4210752);

		String name = "";
		if(labOven.recipeDisplayIndex == -1) name = "No Recipe";
		else name = ModRecipes.labOvenRecipes.get(labOven.recipeDisplayIndex).getOutput().getName();
		this.fontRendererObj.drawString(name, 44, 24, 4210752);

	}

	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		//power bar
		if (this.labOven.powerCount > 0){
	        int k = this.getBarScaled(50, this.labOven.getField(0), this.labOven.getField(1));
			this.drawTexturedModalRect(i + 11, j + 40 + (50 - k), 176, 27, 10, k);
		}
		//redstone
		if (this.labOven.redstoneCount > 0){
	        int k = this.getBarScaled(50, this.labOven.getField(4), this.labOven.getField(5));
			this.drawTexturedModalRect(i + 155, j + 40 + (50 - k), 176, 81, 10, k);
		}
		//smelt bar
        int k = this.getBarScaled(14, this.labOven.getField(2), this.labOven.getField(3));
		this.drawTexturedModalRect(i + 61, j + 58, 176, 0, k, 15);
		//process icons
		if(this.labOven.isBurning()){
			this.drawTexturedModalRect(i + 82, j + 79, 176, 131, 12, 14);
			this.drawTexturedModalRect(i + 100, j + 63, 176, 145, 14, 9);
		}
		//recipe solvent icon
		if(this.labOven.recipeDisplayIndex == 0 || this.labOven.recipeDisplayIndex == 3){
			this.drawTexturedModalRect(i + 118, j + 77, 195, 27, 12, 16);
		}else if(this.labOven.recipeDisplayIndex == 1 || this.labOven.recipeDisplayIndex == 2){
			this.drawTexturedModalRect(i + 118, j + 77, 195, 43, 12, 16);
		}else{
			this.drawTexturedModalRect(i + 118, j + 77, 195, 11, 12, 16);
		}
	}

	private int getBarScaled(int pixels, int count, int max) {
        int i = max;
        if (i == 0){i = max;}
        return count > 0 && max > 0 ? count * pixels / max : 0;
	}
}