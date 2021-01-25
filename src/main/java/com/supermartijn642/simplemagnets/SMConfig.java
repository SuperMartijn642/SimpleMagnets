package com.supermartijn642.simplemagnets;

import com.supermartijn642.configlib.ModConfigBuilder;

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

    static {
        ModConfigBuilder builder = new ModConfigBuilder("simplemagnets");

        builder.push("Client");

        playToggleSound = builder.dontSync().comment("Should the magnet play a ding sound when turned on or off?").define("playToggleSound", true);
        showToggleMessage = builder.dontSync().comment("Should the magnet display a message on screen when turned on or off?").define("showToggleMessage", false);

        builder.pop();

        builder.push("General");

        basicMagnetRange = builder.comment("In what range should the Basic Magnet pickup items and xp?").define("basicMagnetRange", 5, 1, 20);
        advancedMagnetRange = builder.comment("In what range should the Basic Magnet pickup items and xp by default?").define("advancedMagnetRange", 8, 1, 10);
        advancedMagnetMinRange = builder.comment("What is the minimum range of the Advanced Magnet?").define("advancedMagnetMinRange", 3, 1, 20);
        advancedMagnetMaxRange = builder.comment("What is the maximum range of the Advanced Magnet?").define("advancedMagnetMaxRange", 11, 1, 20);

        builder.pop();

        builder.build();
    }

}
