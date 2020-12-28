package kimera.really.works.coalstone.common.recipes;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CoalstoneCondenserRecipe implements IRecipe<IInventory>
{
	public static final Serializer SERIALIZER = new Serializer();
	
	static int MAX_WIDTH = 5;
	
	public static void setCraftingSize(int width)
	{
		if(width > MAX_WIDTH) MAX_WIDTH = width;
	}
	
	private final ResourceLocation id;
	private final int recipeWidth;
	private final NonNullList<Ingredient> recipeItems;
	private final ItemStack recipeOutput;
	private final float experience;
	private final int cookTime;
	
	public CoalstoneCondenserRecipe(ResourceLocation id, int recipeWidth, NonNullList<Ingredient> recipeItems, ItemStack recipeOutput, float experience, int cookTime)
	{
		this.id = id;
		this.recipeWidth = recipeWidth;
		this.recipeItems = recipeItems;
		this.recipeOutput = recipeOutput;
		this.experience = experience;
		this.cookTime = cookTime;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		if(inv instanceof CoalstoneCondenserTileEntity)
		{
			CoalstoneCondenserTileEntity condenser = (CoalstoneCondenserTileEntity) inv;

			for(int inputIndex = 0; inputIndex < CoalstoneCondenserTileEntity.INPUT_SLOT_COUNT; inputIndex++)
			{
				int condenserTestSlot = CoalstoneCondenserTileEntity.FIRST_INPUT_SLOT_INDEX + inputIndex;
				boolean ingredientTest = this.recipeItems.get(inputIndex).test(condenser.getStackInSlot(condenserTestSlot));
				if(!ingredientTest)
				{
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv)
	{
		return this.getRecipeOutput().copy();
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= this.recipeWidth && height == 1;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	public int getRecipeWidth()
	{
		return this.recipeWidth;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return this.recipeItems;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.recipeOutput;
	}
	
	public float getExperience()
	{
		return this.experience;
	}
	
	public int getCookTime()
	{
		return this.cookTime;
	}
	
	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(BlockRegistry.coalstoneCondenser.get());
	}

	@Override
	public IRecipeType<?> getType()
	{
		return RecipeRegistry.coalstoneCondenserRecipe;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CoalstoneCondenserRecipe>
	{
		Serializer()
		{
			this.setRegistryName(new ResourceLocation("coalstone", "coalstone_condenser_recipe"));
		}
		
		@Override
		public CoalstoneCondenserRecipe read(ResourceLocation recipeId, JsonObject json)
		{
			String pattern = JSONUtils.getString(json, "pattern");
			
			final JsonObject keyElement = JSONUtils.getJsonObject(json, "key");
			Map<String, Ingredient> key = readKey(keyElement);
			
			int patternWidth = pattern.length();
			NonNullList<Ingredient> ingredientList = readIngredients(pattern, key, patternWidth);
			
			ItemStack output = readItemStack(JSONUtils.getJsonObject(json, "output"));
			
			float experience = JSONUtils.getFloat(json, "experience");
			int cookTime = JSONUtils.getInt(json, "cookingtime");
			
			return new CoalstoneCondenserRecipe(recipeId, patternWidth, ingredientList, output, experience, cookTime);
		}

		@Override
		public CoalstoneCondenserRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			int recipeWidth = buffer.readInt();
			
			NonNullList<Ingredient> ingredientList = NonNullList.withSize(recipeWidth, Ingredient.EMPTY);
			for(int ingredientIndex = 0; ingredientIndex < ingredientList.size(); ingredientIndex++)
			{
				ingredientList.set(ingredientIndex, Ingredient.read(buffer));
			}
			
			ItemStack output = buffer.readItemStack();
			
			float experience = buffer.readFloat();
			int cookTime = buffer.readInt();
			
			return new CoalstoneCondenserRecipe(recipeId, recipeWidth, ingredientList, output, experience, cookTime);
		}

		@Override
		public void write(PacketBuffer buffer, CoalstoneCondenserRecipe recipe)
		{
			buffer.writeInt(recipe.recipeWidth);
			
			for(int ingredientIndex = 0; ingredientIndex < recipe.recipeItems.size(); ingredientIndex++)
			{
				recipe.recipeItems.get(ingredientIndex).write(buffer);
			}
			
			buffer.writeItemStack(recipe.recipeOutput);
			
			buffer.writeFloat(recipe.experience);
			buffer.writeInt(recipe.cookTime);
		}
		
		private static ItemStack readItemStack(JsonObject object)
		{
			String itemId = JSONUtils.getString(object, "item");
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
			if(item == Items.AIR)
			{
				throw new JsonSyntaxException("Unknown item '" + itemId + "'");
			}
			
			if(object.has("data"))
			{
				throw new JsonSyntaxException("Disallowed data tag found");
			}
			else
			{
				int count = JSONUtils.getInt(object, "count");
				return CraftingHelper.getItemStack(object, true);
			}
		}
		
		private static NonNullList<Ingredient> readIngredients(String pattern, Map<String, Ingredient> keyMap, int patternWidth)
		{
			NonNullList<Ingredient> ingredientList = NonNullList.withSize(patternWidth, Ingredient.EMPTY);
			
			Set<String> keySet = Sets.newHashSet(keyMap.keySet());
			keySet.remove(" ");
			
			for(int x = 0; x < pattern.length(); x++)
			{
				String key = pattern.substring(x, x + 1);
				
				Ingredient ingredient = keyMap.get(key);
				if(ingredient == null)
				{
					throw new JsonSyntaxException("Pattern references symbol " + key + " without definition in key.");
				}
				
				keySet.remove(key);
				ingredientList.set(x, ingredient);
			}
			
			if(!keySet.isEmpty())
			{
				throw new JsonSyntaxException("Key defines symbols that aren't used in patterns " + keySet);
			}
			return ingredientList;
		}
		
		private static Map<String, Ingredient> readKey(JsonObject json)
		{
			Map<String, Ingredient> keyMap = Maps.newHashMap();
			
			for(Map.Entry<String, JsonElement> keyElement : json.entrySet())
			{
				if(keyElement.getKey().length() < 1 || keyElement.getKey().length() > 1)
				{
					throw new JsonSyntaxException("Invalid Key: Key " + keyElement.getKey() + " must be 1 character.");
				}
				else if(" " == keyElement.getKey())
				{
					throw new JsonSyntaxException("Invalid Key: ' ' is a reserved symbol.");
				}
				
				keyMap.put(keyElement.getKey(), Ingredient.deserialize(keyElement.getValue()));
			}
			
			keyMap.put(" ", Ingredient.EMPTY);
			return keyMap;
		}
		
		private static String shrinkPattern(String pattern)
		{
			int firstNonSpaceIndex = getFirstNonSpaceIndex(pattern);
			int lastNonSpaceIndex = getLastNonSpaceIndex(pattern);
			
			if(lastNonSpaceIndex < 0)
			{
				return "";
			}
			else
			{
				return pattern.substring(firstNonSpaceIndex, lastNonSpaceIndex + 1);
			}
		}
		
		private static int getFirstNonSpaceIndex(String input)
		{
			int i;
			for(i = 0; i < input.length(); i++)
			{
				if(input.charAt(i) == ' ') break;
			}
			
			return i;
		}
		
		private static int getLastNonSpaceIndex(String input)
		{
			int i;
			for(i = 0; i < input.length(); i++)
			{
				if(input.charAt(input.length() - 1 - i) == ' ') break;
			}
			
			return i;
		}
	}
}
