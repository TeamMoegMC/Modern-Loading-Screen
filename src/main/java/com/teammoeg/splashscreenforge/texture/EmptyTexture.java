package com.teammoeg.splashscreenforge.texture;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

public class EmptyTexture extends SimpleTexture {
    // Empty texture used for hiding the default mojang logo when using other logo styles //

    public EmptyTexture(ResourceLocation location) {
        super(location);
    }

    @Override
    protected TextureData getTextureData(IResourceManager resourceManager) {
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("empty.png");
            TextureData texture = null;

            if (input != null) {

                try {
                    texture = new TextureData(new TextureMetadataSection(true, true), NativeImage.read(input));
                } finally {
                    input.close();
                }

            }

            return texture;
        } catch (IOException var18) {
            return new TextureData(var18);
        }
    }

}
