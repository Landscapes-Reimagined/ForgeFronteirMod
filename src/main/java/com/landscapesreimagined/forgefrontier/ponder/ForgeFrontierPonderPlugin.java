package com.landscapesreimagined.forgefrontier.ponder;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;
import com.simibubi.create.foundation.ponder.PonderWorldBlockEntityFix;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderScenes;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class ForgeFrontierPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return ForgeFrontier.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ForgeFrontierPonders.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {
//        PonderWorldBlockEntityFix.fixControllerBlockEntities(ponderLevel);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
//        helper.excludeBlockVariants(ValveHandleBlock.class, AllBlocks.COPPER_VALVE_HANDLE.get());
//        helper.excludeBlockVariants(PostboxBlock.class, AllBlocks.PACKAGE_POSTBOXES.get(DyeColor.WHITE).get());
//        helper.excludeBlockVariants(TableClothBlock.class, AllBlocks.TABLE_CLOTHS.get(DyeColor.WHITE).get());
    }
}
