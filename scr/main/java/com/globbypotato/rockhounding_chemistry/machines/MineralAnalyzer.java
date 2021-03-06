package com.globbypotato.rockhounding_chemistry.machines;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.handlers.GuiHandler;
import com.globbypotato.rockhounding_chemistry.machines.tileentity.TileEntityMineralAnalyzer;
import com.globbypotato.rockhounding_core.enums.EnumFluidNbt;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class MineralAnalyzer extends BaseMachine{
    public MineralAnalyzer(float hardness, float resistance, String name){
        super(name, Material.IRON, TileEntityMineralAnalyzer.class, GuiHandler.mineralAnalyzerID);
		setHardness(hardness);
		setResistance(resistance);	
		setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
    	super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
		if(stack.hasTagCompound()){
        	TileEntityMineralAnalyzer te = (TileEntityMineralAnalyzer) worldIn.getTileEntity(pos);
			if(te != null){
	    		if(stack.getTagCompound().hasKey(EnumFluidNbt.SULF.nameTag())){
	    			te.sulfTank.setFluid(FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(EnumFluidNbt.SULF.nameTag())));
	    		}
	    		if(stack.getTagCompound().hasKey(EnumFluidNbt.CHLO.nameTag())){
	    			te.chloTank.setFluid(FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(EnumFluidNbt.CHLO.nameTag())));
	    		}
	    		if(stack.getTagCompound().hasKey(EnumFluidNbt.FLUO.nameTag())){
	    			te.fluoTank.setFluid(FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(EnumFluidNbt.FLUO.nameTag())));
	    		}
			}
		}
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack){
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);
        java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
        ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        if(te != null && te instanceof TileEntityMineralAnalyzer){
  			addNbt(itemstack, te);
        }
        if (itemstack != null){ items.add(itemstack); }
        net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
        for (ItemStack item : items){ spawnAsEntity(worldIn, pos, item); }
    }

	private void addNbt(ItemStack itemstack, TileEntity tileentity) {
		TileEntityMineralAnalyzer analyzer = ((TileEntityMineralAnalyzer)tileentity);
		itemstack.setTagCompound(new NBTTagCompound());
    	addPowerNbt(itemstack, tileentity);
		NBTTagCompound sulf = new NBTTagCompound(); 
		NBTTagCompound chlo = new NBTTagCompound(); 
		NBTTagCompound fluo = new NBTTagCompound(); 
		if(analyzer.sulfTank.getFluid() != null){
			analyzer.sulfTank.getFluid().writeToNBT(sulf);
			itemstack.getTagCompound().setTag(EnumFluidNbt.SULF.nameTag(), sulf);
		}
		if(analyzer.chloTank.getFluid() != null){
			analyzer.chloTank.getFluid().writeToNBT(chlo);
			itemstack.getTagCompound().setTag(EnumFluidNbt.CHLO.nameTag(), chlo);
		}
		if(analyzer.fluoTank.getFluid() != null){
			analyzer.fluoTank.getFluid().writeToNBT(fluo);
			itemstack.getTagCompound().setTag(EnumFluidNbt.FLUO.nameTag(), fluo);
		}
	}
}