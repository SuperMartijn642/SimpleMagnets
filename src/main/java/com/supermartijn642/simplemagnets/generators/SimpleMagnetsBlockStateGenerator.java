package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlock;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.util.EnumFacing;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsBlockStateGenerator extends BlockStateGenerator {

    public SimpleMagnetsBlockStateGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.blockState(SimpleMagnets.basic_demagnetization_coil)
            .variantsForAll((state, builder) -> {
                EnumFacing facing = state.get(DemagnetizationCoilBlock.FACING);
                int xRotation = facing == EnumFacing.UP ? 180 : facing == EnumFacing.DOWN ? 0 : 90;
                int yRotation = facing.getAxis() == EnumFacing.Axis.Y ? 0 : (int)facing.getHorizontalAngle();
                builder.model("basic_demagnetization_coil", xRotation, yRotation);
            });
        this.blockState(SimpleMagnets.advanced_demagnetization_coil)
            .variantsForAll((state, builder) -> {
                EnumFacing facing = state.get(DemagnetizationCoilBlock.FACING);
                int xRotation = facing == EnumFacing.UP ? 180 : facing == EnumFacing.DOWN ? 0 : 90;
                int yRotation = facing.getAxis() == EnumFacing.Axis.Y ? 0 : (int)facing.getHorizontalAngle();
                builder.model("advanced_demagnetization_coil", xRotation, yRotation);
            });
    }
}
