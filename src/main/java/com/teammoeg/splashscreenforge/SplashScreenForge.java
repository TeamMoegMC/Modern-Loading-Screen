package com.teammoeg.splashscreenforge;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("splashscreenforge")
public class SplashScreenForge {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static File CONFIG_PATH;

    private static Path BackgroundTexture = Paths.get(CONFIG_PATH + "/background.png");
    private static Path MojangTexture = Paths.get(CONFIG_PATH + "/mojangstudios.png");
    private static Path MojankTexture = Paths.get(CONFIG_PATH + "/mojank.png");
    private static Path ProgressBarTexture = Paths.get(CONFIG_PATH + "/progressbar.png");
    private static Path ProgressBarBackgroundTexture = Paths.get(CONFIG_PATH + "/progressbar_background.png");


    public static ResourceLocation rl(String path) {
        return new ResourceLocation(path);
    }

    public SplashScreenForge() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SplashConfig.clientSpec);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        CONFIG_PATH = new File(event.getMinecraftSupplier().get().gameDir + "/splashcreenforge");
        if (!CONFIG_PATH.exists()) { // Run when config directory is nonexistant //
            CONFIG_PATH.mkdir(); // Create our custom config directory //

            // Open Input Streams for copying the default textures to the config directory //
            InputStream background = Thread.currentThread().getContextClassLoader().getResourceAsStream("background.png");
            InputStream mojangstudios = Thread.currentThread().getContextClassLoader().getResourceAsStream("mojangstudios.png");
            InputStream mojank = Thread.currentThread().getContextClassLoader().getResourceAsStream("mojank.png");
            InputStream progressbar = Thread.currentThread().getContextClassLoader().getResourceAsStream("progressbar.png");
            InputStream progressbarBG = Thread.currentThread().getContextClassLoader().getResourceAsStream("progressbar_background.png");
            try {
                // Copy the default textures into the config directory //
                Files.copy(background,BackgroundTexture, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(mojangstudios,MojangTexture,StandardCopyOption.REPLACE_EXISTING);
                Files.copy(mojank,MojankTexture,StandardCopyOption.REPLACE_EXISTING);
                Files.copy(progressbar,ProgressBarTexture,StandardCopyOption.REPLACE_EXISTING);
                Files.copy(progressbarBG,ProgressBarBackgroundTexture,StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

        }
    }
}
