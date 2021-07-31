package com.teammoeg.splashscreenforge;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
    public static File CONFIG_PATH = new File(Minecraft.getInstance().gameDir + "/splashscreenforge");

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SplashConfig.CLIENT_CONFIG);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Customizing Splash Screen");
        // do something that can only be done on the client
        System.out.println("CONFIG_PATH: " + CONFIG_PATH);
        Path backgroundTexture = Paths.get(CONFIG_PATH + "/background.png");
        Path mojangTexture = Paths.get(CONFIG_PATH + "/mojangstudios.png");
        Path mojankTexture = Paths.get(CONFIG_PATH + "/mojank.png");
        Path progressBarTexture = Paths.get(CONFIG_PATH + "/progressbar.png");
        Path progressBarBackgroundTexture = Paths.get(CONFIG_PATH + "/progressbar_background.png");

        if (!CONFIG_PATH.exists()) { // Run when config directory is nonexistant //
            CONFIG_PATH.mkdir(); // Create our custom config directory //

//            event.getMinecraftSupplier().get().getResourceManager().getResource(rl("background.png")).getInputStream();

            // Open Input Streams for copying the default textures to the config directory //
            InputStream background = Thread.currentThread().getContextClassLoader().getResourceAsStream("background.png");
            InputStream mojangstudios = Thread.currentThread().getContextClassLoader().getResourceAsStream("mojangstudios.png");
            InputStream mojank = Thread.currentThread().getContextClassLoader().getResourceAsStream("mojank.png");
            InputStream progressbar = Thread.currentThread().getContextClassLoader().getResourceAsStream("progressbar.png");
            InputStream progressbarBG = Thread.currentThread().getContextClassLoader().getResourceAsStream("progressbar_background.png");
            try {
                // Copy the default textures into the config directory //
                Files.copy(background, backgroundTexture, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(mojangstudios, mojangTexture, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(mojank, mojankTexture, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(progressbar, progressBarTexture, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(progressbarBG, progressBarBackgroundTexture, StandardCopyOption.REPLACE_EXISTING);
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
