package com.teammoeg.modernloadingscreen;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MLSConfig {
    public static class Client {
        public final ForgeConfigSpec.EnumValue<ProgressBarType> progressBarType;
        public final ForgeConfigSpec.EnumValue<LogoStyle> logoStyle;
        public final ForgeConfigSpec.BooleanValue backgroundImage;
        public final ForgeConfigSpec.IntValue backgroundColor;
        public final ForgeConfigSpec.IntValue progressBarColor;
        public final ForgeConfigSpec.IntValue progressFrameColor;
        public final ForgeConfigSpec.EnumValue<ProgressBarMode> customProgressBarMode;
        public final ForgeConfigSpec.BooleanValue customProgressBarBackground;
        public final ForgeConfigSpec.EnumValue<BossBarType> bossBarType;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client configuration settings")
                    .push("client");

            progressBarType = builder
                    .comment("Change the design of the progress bar")
                    .defineEnum("progressBarType", ProgressBarType.Vanilla);

            logoStyle = builder
                    .comment("Change the texture of the logo")
                    .defineEnum("logoStyle", LogoStyle.Mojang);

            backgroundImage = builder
                    .comment("Enable/Disable the background image")
                    .define("backgroundImage", false);

            backgroundColor = builder
                    .comment("Change the color of the background")
                    .defineInRange("backgroundColor", 15675965, 0, 16777215);

            progressBarColor = builder
                    .comment("Change the color of the progress bar")
                    .defineInRange("progressBarColor", 16777215, 0, 16777215);

            progressFrameColor = builder
                    .comment("Change the color of the progress bar frame")
                    .defineInRange("progressFrameColor", 16777215, 0, 16777215);

            customProgressBarMode = builder
                    .comment("Change the mode of the custom loading bar")
                    .defineEnum("customProgressBarMode", ProgressBarMode.Linear);

            customProgressBarBackground = builder
                    .comment("Enable/Disable the custom progress bar background")
                    .define("customProgressBarBackground", false);

            bossBarType = builder
                    .comment("Change the style of the boss loading bar")
                    .defineEnum("bossBarType", BossBarType.NOTCHED_6);

            builder.pop();
        }

    }

    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final Client CLIENT;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        CLIENT = new Client(BUILDER);
        CLIENT_CONFIG = BUILDER.build();
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

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {

    }
}
