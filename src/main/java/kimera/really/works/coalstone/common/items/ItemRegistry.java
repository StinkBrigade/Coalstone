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
	
	public static final RegistryObject<Item> COALSTONE_CLUMP = ITEMS.register("coalstone_clump", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> COALSTONE_SHARD = ITEMS.register("coalstone_shard", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> CONDENSED_COALSTONE = ITEMS.register("condensed_coalstone", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));

	public static final RegistryObject<Item> COALSTONE_SWORD = ITEMS.register("coalstone_sword", () -> new SwordItem(CoalstoneItemTiers.COALSTONE, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> COALSTONE_SHOVEL = ITEMS.register("coalstone_shovel", () -> new ShovelItem(CoalstoneItemTiers.COALSTONE, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> COALSTONE_PICKAXE = ITEMS.register("coalstone_pickaxe", () -> new PickaxeItem(CoalstoneItemTiers.COALSTONE, 1, -2.8F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> COALSTONE_AXE = ITEMS.register("coalstone_axe", () -> new AxeItem(CoalstoneItemTiers.COALSTONE, 7.0F, -3.2F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> COALSTONE_HOE = ITEMS.register("coalstone_hoe", () -> new HoeItem(CoalstoneItemTiers.COALSTONE, -1, -2.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));

	public static final RegistryObject<Item> CARBONISED_DIAMOND_SWORD = ITEMS.register("carbonised_diamond_sword", () -> new SwordItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> CARBONISED_DIAMOND_SHOVEL = ITEMS.register("carbonised_diamond_shovel", () -> new ShovelItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> CARBONISED_DIAMOND_PICKAXE = ITEMS.register("carbonised_diamond_pickaxe", () -> new PickaxeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 1, -2.8F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> CARBONISED_DIAMOND_AXE = ITEMS.register("carbonised_diamond_axe", () -> new AxeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, 5.0F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));
	public static final RegistryObject<Item> CARBONISED_DIAMOND_HOE = ITEMS.register("carbonised_diamond_hoe", () -> new HoeItem(CoalstoneItemTiers.CARBONISED_DIAMOND, -3, 0.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));

	public static final RegistryObject<Item> COALSTONE_BRICKS = ITEMS.register("coalstone_bricks", () -> new BlockItem(BlockRegistry.COALSTONE_BRICKS.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_BRICK_STAIRS = ITEMS.register("coalstone_brick_stairs", () -> new BlockItem(BlockRegistry.COALSTONE_BRICK_STAIRS.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_BRICK_SLAB = ITEMS.register("coalstone_brick_slab", () -> new BlockItem(BlockRegistry.COALSTONE_BRICK_SLAB.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_TILES = ITEMS.register("coalstone_tiles", () -> new BlockItem(BlockRegistry.COALSTONE_TILES.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_TILE_STAIRS = ITEMS.register("coalstone_tile_stairs", () -> new BlockItem(BlockRegistry.COALSTONE_TILE_STAIRS.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_TILE_SLAB = ITEMS.register("coalstone_tile_slab", () -> new BlockItem(BlockRegistry.COALSTONE_TILE_SLAB.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COALSTONE_FURNACE = ITEMS.register("coalstone_furnace", () -> new BlockItem(BlockRegistry.COALSTONE_FURNACE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
	public static final RegistryObject<Item> COALSTONE_CONDENSER = ITEMS.register("coalstone_condenser", () -> new BlockItem(BlockRegistry.COALSTONE_CONDENSER.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
}
