package com.landscapesreimagined.forgefrontier.mixin.CreateTeleporters;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.AccessFluidTank;
import com.landscapesreimagined.forgefrontier.util.BlockSearchDir;
import net.createteleporters.CreateteleportersMod;
import net.createteleporters.block.CustomPortalOnBlock;
import net.createteleporters.block.entity.CustomPortalOnTileEntity;
import net.createteleporters.init.CreateteleportersModBlocks;
import net.createteleporters.init.CreateteleportersModFluids;
import net.createteleporters.init.CreateteleportersModParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Comparator;
import java.util.function.Predicate;

@Mixin(CustomPortalOnBlock.class)
public class RewritePortalTicker {

    @Unique
    private static final Predicate<Entity> forgefrontier$TP_EXCEPTIONS = (entity -> !(entity instanceof Boat) && !(entity instanceof AbstractMinecartContainer) && !(entity instanceof ItemEntity) && !(entity instanceof Minecart) && !(entity instanceof MinecartTNT) && !(entity instanceof MinecartFurnace) && !(entity instanceof PrimedTnt));



    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/createteleporters/procedures/CustomPortalOnUpdateTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDD)V", remap = false), remap = true)
    public void execute(final LevelAccessor world, double x, double y, double z) {
        BlockPos block = BlockPos.containing(x, y, z);


        //don't run the tick loop on the client

        //don't continue ticking if the block entity isn't the CustomPortalOnTileEntity
        if(!(world.getBlockEntity(block) instanceof CustomPortalOnTileEntity portalBlockEntity)) return;

        //We need access to the fluid tank of the block entity
        if(!(portalBlockEntity instanceof AccessFluidTank tankAccessor)) return;


        Vec3 center = new Vec3(x, y, z);
        AABB searchBox = new AABB(center, center).inflate(3.5);

        //Clean up the code from the mod
        var entities = world.getEntitiesOfClass(Entity.class, searchBox, forgefrontier$TP_EXCEPTIONS);
        entities.sort(Comparator.comparingDouble((entity) -> entity.distanceToSqr(center)));



        //Stop ticking if there's not enough fluid in the tank
        if(tankAccessor.getFluidTank().getFluidAmount() < 250 || tankAccessor.getFluidTank().getFluid().getFluid() != CreateteleportersModFluids.QUANTUM_FLUID.get()){
            //get the nearest Player in the box to center
            Player player = entities.stream().filter(
                    //filter for Players
                    (entity) -> entity.getType() == EntityType.PLAYER
                ).map(
                    //make Java recognize the stream as Stream<Player>
                    (entity) -> (Player) entity
                //get the first entity and return null if there isn't one
                ).findFirst().orElse(null);

            //return if there are no players or the player is on the client
            if(player == null || player.level().isClientSide()) return;

            //send the failure message
            player.displayClientMessage(Component.literal("Not enough fluid"), true);

            return;
        }

        BlockState portalState = world.getBlockState(block);
        Direction facing = portalState.getValue(CustomPortalOnBlock.FACING);

        Block portalBlock = CreateteleportersModBlocks.CUSTOM_PORTAL_ON.get();

        ItemStack tpLink = portalBlockEntity.getItem(0);

        for(Entity entity : entities) {

            int searchFlags = (facing.getAxis() == Direction.Axis.Z ? BlockSearchDir.BlockSearchDirFlags.X.flag : BlockSearchDir.BlockSearchDirFlags.Z.flag) |
                    BlockSearchDir.BlockSearchDirFlags.Y.flag |
                    BlockSearchDir.BlockSearchDirFlags.Y_ALONG_AXIS.flag |
                    BlockSearchDir.BlockSearchDirFlags.INVERT_Y.flag;

            //only activate if the entity is standing on the portal
            if(!BlockSearchDir.isStandingOnBlock(entity, world, portalBlock, searchFlags)) continue;

            entity.fallDistance = 0;

            String dim = tpLink.getOrCreateTag().getString("dimension");

            dim = dim.strip();


            System.out.println(dim);

            ResourceLocation dimensionID = new ResourceLocation(dim);
            ResourceKey<Level> resourcekey = ResourceKey.create(Registries.DIMENSION, dimensionID);

            double tpX = tpLink.getOrCreateTag().getDouble("xpo");
            double tpY = tpLink.getOrCreateTag().getDouble("ypo");
            double tpZ = tpLink.getOrCreateTag().getDouble("zpo");

            Vec3 tpPos = new Vec3(tpX, tpY, tpZ);

            CreateteleportersMod.LOGGER.info("Teleporting " + entity.getDisplayName().getString() + " to " + tpPos + " in dimension " + dimensionID);

            if(!(world instanceof ServerLevel serverLevel)){
                if(world instanceof ClientLevel clientLevel) clientLevel.playLocalSound(x, y, z, SoundEvents.SHULKER_TELEPORT, SoundSource.BLOCKS, 0.2F, 1.0F, false);
                continue;
            }

            ServerLevel toDimension = serverLevel.getServer().getLevel(resourcekey);

            if(toDimension == null){

                if(entity instanceof Player p) p.displayClientMessage(Component.literal("Invalid Dimension!"), true);

                return;

            }

            forgefrontier$teleport(entity, tpPos, toDimension);

            serverLevel.playSound(null, block, SoundEvents.SHULKER_TELEPORT, SoundSource.BLOCKS, 0.2F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 20, 1.5F, 1.5F, 1.5F, 1.0F);
            serverLevel.sendParticles(CreateteleportersModParticleTypes.TP_PARTICLE.get(), entity.getX(), entity.getY(), entity.getZ(), 35, 1.0F, 1.0F, 1.0F, 0.2);


            CreateteleportersMod.queueServerWork(25, () -> {
                BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x, y, z));
                int _amount = 250;
                if (_ent != null) {
                    _ent.getCapability(ForgeCapabilities.FLUID_HANDLER, null).ifPresent((capability) -> capability.drain(_amount, IFluidHandler.FluidAction.EXECUTE));
                }

            });

            CreateteleportersMod.queueServerWork(25, () -> {
                tankAccessor.getFluidTank().drain(250, IFluidHandler.FluidAction.EXECUTE);
            });


        }













    }

    //this is how
    @Unique
    public void forgefrontier$teleport(Entity entity, Vec3 target, ServerLevel dimension) {

        entity.teleportTo(dimension, target.x, target.y, target.z, RelativeMovement.ALL, entity.getYRot(), entity.getXRot());

    }



}
