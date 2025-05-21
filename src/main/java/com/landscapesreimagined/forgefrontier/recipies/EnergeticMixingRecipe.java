package com.landscapesreimagined.forgefrontier.recipies;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingProcessingRecipeParams;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

public class EnergeticMixingRecipe extends WorldMatchingMixingRecipe {




    protected int requiredEnergy;
    protected EnergyCondition requiredEnergyLevel;
    protected HashMap<Item, Integer> itemCountMap = new HashMap<>();


    public EnergeticMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
        this.requiredEnergy = ((EnergeticMixingProcessingRecipeParams) params).getEnergy();
        this.requiredEnergyLevel = ((EnergeticMixingProcessingRecipeParams) params).getRequiredEnergyLevel();
    }

    public Integer getItemAmount(Item item){
        return this.itemCountMap.get(item);
    }

    public HashMap<Item, Integer> getItemCountMap(){
        var map = this.itemCountMap.clone();
        if(map instanceof HashMap<?, ?> itemMap)
            return (HashMap<Item, Integer>) itemMap;
        return null;
    }

    @Override
    public boolean matchesWorld(BasinBlockEntity basin) {

        Level world = basin.getLevel();
        BlockPos basinPos = basin.getBlockPos();

        if(world == null)
            return false;//until maybe needed?

        BlockEntity entityUnderBasin = world.getBlockEntity(basinPos.below());

        if(!(entityUnderBasin instanceof EnergeticBlazeBurnerBlockEntity ebb))
            return false;

        return this.requiredEnergyLevel.testEnergeticBlazeBurner(ebb.getEnergyLevelFromBlock());
    }

    @Override
    public void readAdditional(@NotNull JsonObject json) {
        super.readAdditional(json);


        if(json.has("itemCounts")){
            this.parseItemCountMap(json.getAsJsonArray("itemCounts"));
        }else {
            this.parseItemCountMap(json.getAsJsonArray("ingredients"));
        }

        this.requiredEnergy = GsonHelper.getAsInt(json, "requiredEnergy");
//                json.getAsJsonObject("requiredEnergy").getAsInt();
        this.requiredEnergyLevel = EnergyCondition.deserialize(GsonHelper.getAsString(json, "requiredEnergyLevel"));


    }

    public void parseItemCountMap(JsonArray itemMap){



        itemMap.forEach((jsonElement -> {
            JsonObject entry = jsonElement.getAsJsonObject();
            if(entry.has("item") && entry.has("count"))
                this.itemCountMap.put(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(entry.get("item").getAsString())), entry.get("count").getAsInt());
        }));



    }

    public int getRequiredEnergy() {
        return requiredEnergy;
    }

    public EnergyCondition getRequiredEnergyLevel() {
        return requiredEnergyLevel;
    }

    @Override
    public void readAdditional(@NotNull FriendlyByteBuf buffer) {
        super.readAdditional(buffer);

        this.requiredEnergy = buffer.readInt();
        this.requiredEnergyLevel = buffer.readEnum(EnergyCondition.class);
        int itemMapCount = buffer.readInt();

        for(int i = 0; i < itemMapCount; i++){
            var stack = buffer.readItem();
            this.itemCountMap.put(stack.getItem(), stack.getCount());
        }

    }

    @Override
    public void writeAdditional(@NotNull JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("requiredEnergy", this.requiredEnergy);
        json.addProperty("requiredEnergyLevel", this.requiredEnergyLevel.serialize());
        JsonArray c = new JsonArray();
        for(Item i : this.itemCountMap.keySet()){
            JsonObject item = new JsonObject();
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(i);
            if(key == null){
                throw new RuntimeException("Item in item count map must exist!");
            }
            item.addProperty("item", key.toString());
            item.addProperty("count", itemCountMap.get(i));
            c.add(item);
        }
        json.add("itemCounts", c);
    }

    @Override
    public void writeAdditional(@NotNull FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeInt(this.requiredEnergy);
        buffer.writeEnum(this.requiredEnergyLevel);
        buffer.writeInt(itemCountMap.size());
        for(Item i : this.itemCountMap.keySet()){
            ItemStack stack = i.getDefaultInstance();
            stack.setCount(this.itemCountMap.get(i));
            buffer.writeItemStack(stack, false);
        }
    }

}
