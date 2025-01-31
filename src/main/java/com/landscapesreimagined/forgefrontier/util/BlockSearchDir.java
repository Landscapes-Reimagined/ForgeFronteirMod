package com.landscapesreimagined.forgefrontier.util;

import net.createteleporters.init.CreateteleportersModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class BlockSearchDir {

    /**
     *
     *
     * @param entity entity to iterate under
     * @param world world to fetch blocks from
     * @param block block to find
     * @param dirflags the problem child: bitwise flag container, combine {@link BlockSearchDirFlags} with the bitwise OR(|) operator
     * @return true iff the block is found
     */

    public static boolean isStandingOnBlock(Entity entity, final LevelAccessor world, Block block, int dirflags) {

        boolean flag = false;

        //set x start and end
        int dx_start = -1;
        int dx_end = 1;
        int x_increment = 1;

        if((dirflags & BlockSearchDirFlags.X_ALONG_AXIS.flag) != 0){
            dx_start = 1;
            dx_end = 3;
        }

        if((dirflags & BlockSearchDirFlags.INVERT_X.flag) != 0){
            dx_start *= -1;
            dx_end *= -1;
            x_increment *= -1;
        }

        if((dirflags & BlockSearchDirFlags.X.flag) == 0){
            dx_start = 0;
            dx_end = 0;
        }

        //set y start and end
        int dy_start = -1;
        int dy_end = 1;
        int y_increment = 1;

        if((dirflags & BlockSearchDirFlags.Y_ALONG_AXIS.flag) != 0){
            dy_start = 1;
            dy_end = 3;
        }

        if((dirflags & BlockSearchDirFlags.INVERT_Y.flag) != 0){
            dy_start *= -1;
            dy_end *= -1;
            y_increment *= -1;
        }

        if((dirflags & BlockSearchDirFlags.Y.flag) == 0){
            dy_start = 0;
            dy_end = 0;
        }

        //set z start and end
        int dz_start = -1;
        int dz_end = 1;
        int z_increment = 1;

        if((dirflags & BlockSearchDirFlags.Z_ALONG_AXIS.flag) != 0){
            dz_start = 1;
            dz_end = 3;
        }

        if((dirflags & BlockSearchDirFlags.INVERT_Z.flag) != 0){
            dz_start *= -1;
            dz_end *= -1;
            z_increment *= -1;
        }

        if((dirflags & BlockSearchDirFlags.Z.flag) == 0){
            dz_start = 0;
            dz_end = 0;
        }

        for(int dx = dx_start; Math.abs(dx) <= Math.abs(dx_end); dx += x_increment){
            for(int dy = dy_start; Math.abs(dy) <= Math.abs(dy_end); dy += y_increment){
                for(int dz = dz_start; Math.abs(dz) <= Math.abs(dz_end); dz += z_increment){
                    Vec3 pos = entity.position().add(dx, dy, dz);

                    BlockPos blockPos = BlockPos.containing(pos);
//                    if((dirflags & BlockSearchDirFlags.STANDING_ON.flag) != 0){
//                        blockPos.below();
//                    }

                    if(world.getBlockState(blockPos).getBlock() == block){
                        flag = true;
                    }
                }
            }
        }
        return flag;

    }


    public static enum BlockSearchDirFlags{
        X(0b1),
        Y(0b10),
        Z(0b100),
        X_ALONG_AXIS(0b1000),
        Y_ALONG_AXIS(0b10000),
        Z_ALONG_AXIS(0b100000),
        INVERT_X(0b1000000),
        INVERT_Y(0b10000000),
        INVERT_Z(0b100000000);





        public final int flag;
        private BlockSearchDirFlags(int flag){
            this.flag = flag;
        }


    }
}
