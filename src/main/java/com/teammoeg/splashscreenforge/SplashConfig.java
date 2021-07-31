package com.teammoeg.splashscreenforge;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class SplashConfig {
    public static class Client {
        public final ForgeConfigSpec.ConfigValue<ProgressBarType> progressBarType;
        public final ForgeConfigSpec.ConfigValue<LogoStyle> logoStyle;
        public final ForgeConfigSpec.ConfigValue<Boolean> backgroundImage;
        public final ForgeConfigSpec.ConfigValue<Integer> backgroundColor;
        public final ForgeConfigSpec.ConfigValue<Integer> progressBarColor;
        public final ForgeConfigSpec.ConfigValue<Integer> progressFrameColor;
        public final ForgeConfigSpec.ConfigValue<ProgressBarMode> customProgressBarMode;
        public final ForgeConfigSpec.ConfigValue<Boolean> customProgressBarBackground;
        public final ForgeConfigSpec.ConfigValue<BossBarType> bossBarType;

        Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("General configuration settings")
                    .push("general");

            progressBarType = builder
                    .comment("Change the design of the progress bar")
                    .define("progressBarType", ProgressBarType.Vanilla);

            logoStyle = builder
                    .comment("Change the texture of the logo")
                    .define("logoStyle", LogoStyle.Mojang);

            backgroundImage = builder
                    .comment("Enable/Disable the background image")
                    .define("backgroundImage", false);

            backgroundColor = builder
                    .comment("Change the color of the background")
                    .define("backgroundColor", 15675965);

            progressBarColor = builder
                    .comment("Change the color of the progress bar")
                    .define("progressBarColor", 16777215);

            progressFrameColor = builder
                    .comment("Change the color of the progress bar frame")
                    .define("progressFrameColor", 16777215);

            customProgressBarMode = builder
                    .comment("Change the mode of the custom loading bar")
                    .define("customProgressBarMode", ProgressBarMode.Linear);

            customProgressBarBackground = builder
                    .comment("Enable/Disable the custom progress bar background")
                    .define("customProgressBarBackground", false);

            bossBarType = builder
                    .comment("Change the style of the boss loading bar")
                    .define("bossBarType", BossBarType.NOTCHED_6);

            builder.pop();
        }

    }

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public enum ProgressBarType {
        Vanilla, BossBar, Custom, Hidden;
    }
    public enum LogoStyle {
        Mojang, Aspect1to1, Hidden;
    }
    public enum ProgressBarMode {
        Linear, Stretch;
    }
    public enum BossBarType {
        PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;
    }
}
