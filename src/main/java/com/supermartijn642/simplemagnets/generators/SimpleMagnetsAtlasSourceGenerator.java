package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.AtlasSourceGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.gui.*;

/**
 * Created 30/06/2025 by SuperMartijn642
 */
public class SimpleMagnetsAtlasSourceGenerator extends AtlasSourceGenerator {

    public SimpleMagnetsAtlasSourceGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.guiAtlas()
            .texture(DemagnetizationCoilContainerScreen.BACKGROUND)
            .texture(FilteredDemagnetizationCoilContainerScreen.BACKGROUND)
            .texture(CheckBox.BUTTONS)
            .texture(DurabilityButton.BUTTONS)
            .texture(WhitelistButton.BUTTONS)
            .texture(MagnetContainerScreen.BACKGROUND)
            .texture(PlusMinusButton.BUTTONS)
            .texture(ShowRangeButton.BUTTONS)
            .texture(UpDownArrowButton.BUTTONS);
    }
}
