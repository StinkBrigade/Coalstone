package kimera.really.works.coalstone.common.items;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum CoalstoneItemTiers implements IItemTier {
	COALSTONE(1, 210, 5.0F, 1.0F, 10, () ->
	{
		return Ingredient.fromItems(ItemRegistry.COALSTONE_SHARD.get());
	}),
	CARBONISED_DIAMOND(3, 1951, 8.0F, 4.0F, 12, () ->
	{
		return Ingredient.fromItems(ItemRegistry.CONDENSED_COALSTONE.get());
	});
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;
	
	CoalstoneItemTiers(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterialIngredient)
	{
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = new LazyValue<Ingredient>(repairMaterialIngredient);
	}

	@Override
	public int getHarvestLevel()
	{
		return this.harvestLevel;
	}
	
	@Override
	public int getMaxUses()
	{
		return this.maxUses;
	}

	@Override
	public float getEfficiency()
	{
		return this.efficiency;
	}

	@Override
	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	@Override
	public int getEnchantability()
	{
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairMaterial()
	{
		return this.repairMaterial.getValue();
	}
}
