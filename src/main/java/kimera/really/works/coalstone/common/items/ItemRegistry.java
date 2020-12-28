package kimera.really.works.coalstone.common.items;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Coalstone.MODID);
	
	public static final RegistryObject<Item> coalstoneClump = ITEMS.register("coalstone_clump", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> coalstoneShard = ITEMS.register("coalstone_shard", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> condensedCoalstone = ITEMS.register("condensed_coalstone", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	
	public static final RegistryObject<Item> coalstoneSword = ITEMS.register("coalstone_sword", () -> new SwordItem(CoalstoneItemTiers.COALSTONE, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> coalstoneShovel = ITEMS.register("coalstone_shovel", () -> new ShovelItem(CoalstoneItemTiers.COALSTONE, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> coalstonePickaxe = ITEMS.register("coalstone_pickaxe", () -> new PickaxeItem(CoalstoneItemTiers.COALSTONE, 1, -2.8F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> coalstoneAxe = ITEMS.register("coalstone_axe", () -> new AxeItem(CoalstoneItemTiers.COALSTONE, 7.0F, -3.2F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> coalstoneHoe = ITEMS.register("coalstone_hoe", () -> new HoeItem(CoalstoneItemTiers.COALSTONE, -1, -2.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));

	public static final RegistryObject<Item> carbonisedDiamondSword = ITEMS.register("carbonised_diamond_sword", () -> new SwordItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> carbonisedDiamondShovel = ITEMS.register("carbonised_diamond_shovel", () -> new ShovelItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> carbonisedDiamondPickaxe = ITEMS.register("carbonised_diamond_pickaxe", () -> new PickaxeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 1, -2.8F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> carbonisedDiamondAxe = ITEMS.register("carbonised_diamond_axe", () -> new AxeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 5.0F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> carbonisedDiamondHoe = ITEMS.register("carbonised_diamond_hoe", () -> new HoeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, -3, 0.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));

	public static final RegistryObject<Item> coalstoneBricks = ITEMS.register("coalstone_bricks", () -> new BlockItem(BlockRegistry.coalstoneBricks.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneBrickStairs = ITEMS.register("coalstone_brick_stairs", () -> new BlockItem(BlockRegistry.coalstoneBrickStairs.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneBrickSlab = ITEMS.register("coalstone_brick_slab", () -> new BlockItem(BlockRegistry.coalstoneBrickSlab.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneTiless = ITEMS.register("coalstone_tiles", () -> new BlockItem(BlockRegistry.coalstoneTiles.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneTileStairs = ITEMS.register("coalstone_tile_stairs", () -> new BlockItem(BlockRegistry.coalstoneTileStairs.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneTileSlab = ITEMS.register("coalstone_tile_slab", () -> new BlockItem(BlockRegistry.coalstoneTileSlab.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> coalstoneFurnace = ITEMS.register("coalstone_furnace", () -> new BlockItem(BlockRegistry.coalstoneFurnace.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<Item> coalstoneCondenser = ITEMS.register("coalstone_condenser", () -> new BlockItem(BlockRegistry.coalstoneCondenser.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
}
