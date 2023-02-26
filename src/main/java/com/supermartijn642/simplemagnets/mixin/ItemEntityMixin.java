package com.supermartijn642.simplemagnets.mixin;

import com.supermartijn642.simplemagnets.extensions.SimpleMagnetsItemEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Created 22/02/2023 by SuperMartijn642
 */
@Mixin(ItemEntity.class)
public class ItemEntityMixin implements SimpleMagnetsItemEntity {

    private boolean simplemagnetsCanBePickedUp = true;

    @Override
    public boolean simplemagnetsCanBePickedUp(){
        return this.simplemagnetsCanBePickedUp;
    }

    @Override
    public void simplemagnetsMarkDontPickUp(){
        this.simplemagnetsCanBePickedUp = false;
    }
}
