package com.teammoeg.splashscreenforge.texture;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.teammoeg.splashscreenforge.SplashScreenForge.CONFIG_PATH;

public class ConfigTexture extends SimpleTexture {
    // Load textures from the config directory //

    public ConfigTexture(ResourceLocation location) {
        super(location);
    }

    protected TextureData getTextureData(IResourceManager resourceManager) {
        try {
            InputStream input = new FileInputStream(CONFIG_PATH+"/splashscreenforge/"+this.textureLocation.toString().replace("minecraft:",""));
            TextureData texture;

            try {
                texture = new TextureData(new TextureMetadataSection(false, true), NativeImage.read(input));
            } finally {
                input.close();
            }

            return texture;
        } catch (IOException var18) {
            return new TextureData(var18);
        }
    }

}
