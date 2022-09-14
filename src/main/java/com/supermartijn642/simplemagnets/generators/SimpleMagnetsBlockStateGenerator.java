package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlock;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.util.Direction;

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
                Direction facing = state.get(DemagnetizationCoilBlock.FACING);
                int xRotation = facing == Direction.UP ? 180 : facing == Direction.DOWN ? 0 : 90;
                int yRotation = facing.getAxis() == Direction.Axis.Y ? 0 : (int)facing.toYRot();
                builder.model("basic_demagnetization_coil", xRotation, yRotation);
            });
        this.blockState(SimpleMagnets.advanced_demagnetization_coil)
            .variantsForAll((state, builder) -> {
                Direction facing = state.get(DemagnetizationCoilBlock.FACING);
                int xRotation = facing == Direction.UP ? 180 : facing == Direction.DOWN ? 0 : 90;
                int yRotation = facing.getAxis() == Direction.Axis.Y ? 0 : (int)facing.toYRot();
                builder.model("advanced_demagnetization_coil", xRotation, yRotation);
            });
    }
}
