package ccm.compresstion.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ccm.compresstion.client.renderer.block.CompressedRenderer;
import ccm.compresstion.tileentity.CompressedTile;
import ccm.compresstion.utils.lib.Archive;
import ccm.nucleum.omnium.utils.helper.NBTItemHelper;

public class Compressed extends BlockContainer
{
    public Compressed(final int id, final Material material)
    {
        super(id, material);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public TileEntity createNewTileEntity(final World world)
    {
        return new CompressedTile();
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack item)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, item);
        CompressedTile tile = (CompressedTile) world.getBlockTileEntity(x, y, z);
        tile.setBlockID(NBTItemHelper.getInt(item, Archive.NBT_COMPRESSED_BLOCK_ID));
        tile.setBlockMeta(NBTItemHelper.getByte(item, Archive.NBT_COMPRESSED_BLOCK_META));
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(final World world, final int x, final int y, final int z, final int metadata, final int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        CompressedTile tile = (CompressedTile) world.getBlockTileEntity(x, y, z);
        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++)
        {
            int id = idDropped(metadata, world.rand, fortune);
            if (id > 0)
            {
                ItemStack stack = new ItemStack(id, 1, damageDropped(metadata));
                NBTItemHelper.setInteger(stack, Archive.NBT_COMPRESSED_BLOCK_ID, tile.getBlock().blockID);
                NBTItemHelper.setByte(stack, Archive.NBT_COMPRESSED_BLOCK_ID, tile.getMeta());
                ret.add(stack);
            }
        }
        return ret;
    }

    private Block getBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        return ((CompressedTile) world.getBlockTileEntity(x, y, z)).getBlock();
    }

    @Override
    public Icon getBlockTexture(final IBlockAccess world, final int x, final int y, final int z, final int side)
    {
        return getBlock(world, x, y, z).getBlockTexture(world, x, y, z, side);
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(final World world, final int x, final int y, final int z, final EntityPlayer player)
    {
        getBlock(world, x, y, z).onBlockClicked(world, x, y, z, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        getBlock(world, x, y, z).randomDisplayTick(world, x, y, z, rand);
    }

    /**
     * Called right before the block is destroyed by a player. Args: world, x, y, z, metaData
     */
    @Override
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int meta)
    {
        getBlock(world, x, y, z).onBlockDestroyedByPlayer(world, x, y, z, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
     */
    public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).getMixedBrightnessForBlock(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    public float getBlockBrightness(final IBlockAccess world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).getBlockBrightness(world, x, y, z);
    }

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    @Override
    public void velocityToAddToEntity(final World world, final int x, final int y, final int z, final Entity entity, final Vec3 par6Vec3)
    {
        getBlock(world, x, y, z).velocityToAddToEntity(world, x, y, z, entity, par6Vec3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(final World world, final int x, final int y, final int z)
    {
        return getBlock(world, x, y, z).canPlaceBlockAt(world, x, y, z);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(final World world, final int x, final int y, final int z)
    {
        onNeighborBlockChange(world, x, y, z, 0);
        getBlock(world, x, y, z).onBlockAdded(world, x, y, z);
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a different metadata value, but before the new metadata value
     * is set. Args: World, x, y, z, old block ID, old metadata
     */
    @Override
    public void breakBlock(final World world, final int x, final int y, final int z, final int par5, final int par6)
    {
        getBlock(world, x, y, z).breakBlock(world, x, y, z, par5, par6);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity)
    {
        getBlock(world, x, y, z).onEntityWalking(world, x, y, z, entity);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        getBlock(world, x, y, z).updateTick(world, x, y, z, rand);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int par6, final float par7, final float par8,
            final float par9)
    {
        return getBlock(world, x, y, z).onBlockActivated(world, x, y, z, player, 0, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion)
    {
        getBlock(world, x, y, z).onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return CompressedRenderer.id;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(final int pass)
    {
        // Set the static var in the client proxy
        CompressedRenderer.currentRenderPass = (byte) pass;
        // the block can render in both passes, so return true always
        return true;
    }
}