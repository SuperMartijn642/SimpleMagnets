package com.supermartijn642.simplemagnets;

import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

import java.util.function.Supplier;

/**
 * Created 1/25/2021 by SuperMartijn642
 */
public class SMConfig {

    public static final Supplier<Integer> basicMagnetRange;
    public static final Supplier<Integer> advancedMagnetRange;
    public static final Supplier<Integer> advancedMagnetMinRange;
    public static final Supplier<Integer> advancedMagnetMaxRange;

    public static final Supplier<Boolean> playToggleSound;
    public static final Supplier<Boolean> showToggleMessage;
    public static final Supplier<Boolean> showDemagnetizationArea;

    public static final Supplier<Integer> basicCoilRange;
    public static final Supplier<Integer> basicCoilMinRange;
    public static final Supplier<Integer> basicCoilMaxRange;
    public static final Supplier<Boolean> basicCoilFilter;
    public static final Supplier<Integer> advancedCoilRange;
    public static final Supplier<Integer> advancedCoilMinRange;
    public static final Supplier<Integer> advancedCoilMaxRange;
    public static final Supplier<Boolean> advancedCoilFilter;

    static{
        IConfigBuilder builder = ConfigBuilders.newTomlConfig("simplemagnets", null, false);

        builder.push("Client");

        playToggleSound = builder.dontSync().comment("Should the magnet play a ding sound when turned on or off?").define("playToggleSound", true);
        showToggleMessage = builder.dontSync().comment("Should the magnet display a message on screen when turned on or off?").define("showToggleMessage", true);
        showDemagnetizationArea = builder.dontSync().comment("Should the Demagnetization Coil's range be highlighted when looking at it?").define("showDemagnetizationArea", true);

        builder.pop();

        builder.push("General");

        basicMagnetRange = builder.comment("In what range should the Basic Magnet pickup items and xp?").define("basicMagnetRange", 5, 1, 20);
        advancedMagnetRange = builder.comment("In what range should the Advanced Magnet pickup items and xp by default?").define("advancedMagnetRange", 8, 1, 20);
        advancedMagnetMinRange = builder.comment("What is the minimum range of the Advanced Magnet?").define("advancedMagnetMinRange", 3, 1, 20);
        advancedMagnetMaxRange = builder.comment("What is the maximum range of the Advanced Magnet?").define("advancedMagnetMaxRange", 11, 1, 20);

        builder.push("Demagnetization Coils");

        basicCoilRange = builder.comment("In what range should the Basic Demagnetization Coil demagnetize items by default?").define("basicCoilRange", 2, 1, 10);
        basicCoilMinRange = builder.comment("What is the minimum range of the Basic Demagnetization Coil?").define("basicCoilMinRange", 1, 1, 10);
        basicCoilMaxRange = builder.comment("What is the maximum range of the Basic Demagnetization Coil?").define("basicCoilMaxRange", 3, 1, 10);
        basicCoilFilter = builder.comment("Should the Basic Demagnetization Coil be able to filter items?").define("basicCoilFilter", false);
        advancedCoilRange = builder.comment("In what range should the Advanced Demagnetization Coil pickup items and xp by default?").define("advancedCoilRange", 3, 1, 10);
        advancedCoilMinRange = builder.comment("What is the minimum range of the Advanced Demagnetization Coil?").define("advancedCoilMinRange", 1, 1, 10);
        advancedCoilMaxRange = builder.comment("What is the maximum range of the Advanced Demagnetization Coil?").define("advancedCoilMaxRange", 5, 1, 10);
        advancedCoilFilter = builder.comment("Should the Advanced Demagnetization Coil be able to filter items?").define("advancedCoilFilter", true);

        builder.pop();

        builder.pop();

        builder.build();
    }

}
