package com.teammoeg.modernloadingscreen.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammoeg.modernloadingscreen.MLSConfig;
import com.teammoeg.modernloadingscreen.texture.BlurredConfigTexture;
import com.teammoeg.modernloadingscreen.texture.ConfigTexture;
import com.teammoeg.modernloadingscreen.texture.EmptyTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

import static com.teammoeg.modernloadingscreen.MLS.rl;
import static net.minecraft.client.gui.AbstractGui.blit;
import static net.minecraft.client.gui.AbstractGui.fill;

@Mixin(ResourceLoadProgressGui.class)
public class MixinResourceLoadProgressGui {

    @Shadow
    @Final
    private static ResourceLocation MOJANG_LOGO_TEXTURE;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private boolean reloading;
    @Shadow
    @Final
    private IAsyncReloader asyncReloader;
    @Shadow
    private float progress;
    @Shadow
    private long fadeOutStart;
    @Shadow
    private long fadeInStart;
    @Shadow
    @Final
    private Consumer<Optional<Throwable>> completedCallback;

    private static final MLSConfig.Client CS_CONFIG = MLSConfig.CLIENT;

    private static final ResourceLocation EMPTY_TEXTURE = rl("empty.png");
    private static final ResourceLocation MOJANG_TEXTURE = rl("mojangstudios.png");
    private static final ResourceLocation ASPECT_1to1_TEXTURE = rl("mojank.png");
    private static final ResourceLocation BOSS_BAR_TEXTURE = rl("textures/gui/bars.png");
    private static final ResourceLocation CUSTOM_PROGRESS_BAR_TEXTURE = rl("progressbar.png");
    private static final ResourceLocation CUSTOM_PROGRESS_BAR_BACKGROUND_TEXTURE = rl("progressbar_background.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = rl("background.png");

    @Inject(method = "loadLogoTexture(Lnet/minecraft/client/Minecraft;)V", at = @At("HEAD"), cancellable = true)
    private static void init(Minecraft mc, CallbackInfo ci) { // Load our custom textures at game start //
        if (CS_CONFIG.logoStyle.get() == MLSConfig.LogoStyle.Mojang) {
            mc.getTextureManager().loadTexture(MOJANG_LOGO_TEXTURE, new BlurredConfigTexture(MOJANG_TEXTURE));
        } else {
            mc.getTextureManager().loadTexture(MOJANG_LOGO_TEXTURE, new EmptyTexture(EMPTY_TEXTURE));
        }
        mc.getTextureManager().loadTexture(ASPECT_1to1_TEXTURE, new ConfigTexture(ASPECT_1to1_TEXTURE));
        mc.getTextureManager().loadTexture(BACKGROUND_TEXTURE, new ConfigTexture(BACKGROUND_TEXTURE));

        mc.getTextureManager().loadTexture(CUSTOM_PROGRESS_BAR_TEXTURE, new ConfigTexture(CUSTOM_PROGRESS_BAR_TEXTURE));
        mc.getTextureManager().loadTexture(CUSTOM_PROGRESS_BAR_BACKGROUND_TEXTURE, new ConfigTexture(CUSTOM_PROGRESS_BAR_BACKGROUND_TEXTURE));

        ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "render", cancellable = false)
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int i = this.mc.getMainWindow().getScaledWidth();
        int j = this.mc.getMainWindow().getScaledHeight();
        long l = Util.milliTime();
        if (this.reloading && this.fadeInStart == -1L) {
            this.fadeInStart = l;
        }

        float f = this.fadeOutStart > -1L ? (float) (l - this.fadeOutStart) / 1000.0F : -1.0F;
        float g = this.fadeInStart > -1L ? (float) (l - this.fadeInStart) / 500.0F : -1.0F;
        float s;
        int m;

        // Render our custom color
        if (f >= 1.0F) {
            if (this.mc.currentScreen != null) {
                this.mc.currentScreen.render(matrices, 0, 0, delta);
            }

            m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(matrices, 0, 0, i, j, withAlpha(m));
            s = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (this.mc.currentScreen != null && g < 1.0F) {
                this.mc.currentScreen.render(matrices, mouseX, mouseY, delta);
            }

            m = MathHelper.ceil(MathHelper.clamp((double) g, 0.15D, 1.0D) * 255.0D);
            fill(matrices, 0, 0, i, j, withAlpha(m));
            s = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            m = getBackgroundColor();
            float p = (float) (m >> 16 & 255) / 255.0F;
            float q = (float) (m >> 8 & 255) / 255.0F;
            float r = (float) (m & 255) / 255.0F;
            GlStateManager.clearColor(p, q, r, 1.0F);
            GlStateManager.clear(16384, Minecraft.IS_RUNNING_ON_MAC);
            s = 1.0F;
        }

        m = (int) ((double) this.mc.getMainWindow().getScaledWidth() * 0.5D);
        int u = (int) ((double) this.mc.getMainWindow().getScaledHeight() * 0.5D);
        double d = Math.min((double) this.mc.getMainWindow().getScaledWidth() * 0.75D, (double) this.mc.getMainWindow().getScaledHeight()) * 0.25D;
        int v = (int) (d * 0.5D);
        double e = d * 4.0D;
        int w = (int) (e * 0.5D);

        // Render our custom background image
        if (CS_CONFIG.backgroundImage.get()) {
            this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.blendEquation(32774);
            RenderSystem.blendFunc(770, 1);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, s); //setShaderColor
            blit(matrices, 0, 0, 0, 0, 0, i, j, j, i);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }

        // Render the Logo
        this.mc.getTextureManager().bindTexture(CS_CONFIG.logoStyle.get() == MLSConfig.LogoStyle.Aspect1to1 ? ASPECT_1to1_TEXTURE : MOJANG_LOGO_TEXTURE);
        RenderSystem.enableBlend();

        if (CS_CONFIG.logoStyle.get() == MLSConfig.LogoStyle.Aspect1to1) {
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, s); //setShaderColor
            blit(matrices, m - (w / 2), v, w, w, 0, 0, 512, 512, 512, 512);
        } else if (CS_CONFIG.logoStyle.get() == MLSConfig.LogoStyle.Mojang) {
            RenderSystem.blendFunc(770, 1);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, s); //setShaderColor
            blit(matrices, m - w, u - v, w, (int) d, -0.0625F, 0.0F, 120, 60, 120, 120);
            blit(matrices, m, u - v, w, (int) d, 0.0625F, 60.0F, 120, 60, 120, 120);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        int x = (int) ((double) this.mc.getMainWindow().getScaledHeight() * 0.8325D);
        float y = this.asyncReloader.estimateExecutionSpeed();
        this.progress = MathHelper.clamp(this.progress * 0.95F + y * 0.050000012F, 0.0F, 1.0F);
        if (f < 1.0F) {
            this.renderProgressBar(matrices, i / 2 - w, x - 5, i / 2 + w, x + 5, 1.0F - MathHelper.clamp(f, 0.0F, 1.0F), null);
        }

        if (f >= 2.0F) {
            this.mc.setLoadingGui(null);
        }

        if (this.fadeOutStart == -1L && this.asyncReloader.fullyDone() && (!this.reloading || g >= 2.0F)) {
            try {
                this.asyncReloader.join();
                this.completedCallback.accept(Optional.empty());
            } catch (Throwable var23) {
                this.completedCallback.accept(Optional.of(var23));
            }

            this.fadeOutStart = Util.milliTime();
            if (this.mc.currentScreen != null) {
                this.mc.currentScreen.init(this.mc, this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight());
            }
        }
    }

    private static int getBackgroundColor() {
        if (CS_CONFIG.backgroundImage.get()) {
            return ColorHelper.PackedColor.packColor(0, 0, 0, 0);
        } else {
            return CS_CONFIG.backgroundColor.get();
        }
    }

    private static int withAlpha(int alpha) {
        return getBackgroundColor() | alpha << 24;
    }

    @Inject(at = @At("TAIL"), method = "func_238629_a_", cancellable = false)
    private void renderProgressBar(MatrixStack matrices, int x1, int y1, int x2, int y2, float opacity, CallbackInfo ci) {
        int i = MathHelper.ceil((float) (x2 - x1 - 2) * this.progress);

        // Bossbar Progress Bar
        if (CS_CONFIG.progressBarType.get() == MLSConfig.ProgressBarType.BossBar) {
            this.mc.getTextureManager().bindTexture(BOSS_BAR_TEXTURE);

            int overlay = 0;

            if (CS_CONFIG.bossBarType.get() == MLSConfig.BossBarType.NOTCHED_6) {
                overlay = 93;
            } else if (CS_CONFIG.bossBarType.get() == MLSConfig.BossBarType.NOTCHED_10) {
                overlay = 105;
            } else if (CS_CONFIG.bossBarType.get() == MLSConfig.BossBarType.NOTCHED_12) {
                overlay = 117;
            } else if (CS_CONFIG.bossBarType.get() == MLSConfig.BossBarType.NOTCHED_20) {
                overlay = 129;
            }

            int bbWidth = (int) ((x2 - x1 + 1) * 1.4f);
            int bbHeight = (y2 - y1) * 30;
            blit(matrices, x1, y1 + 1, 0, 0, 0, x2 - x1, (int) ((y2 - y1) / 1.4f), bbHeight, bbWidth);
            blit(matrices, x1, y1 + 1, 0, 0, 5f, i, (int) ((y2 - y1) / 1.4f), bbHeight, bbWidth);

            RenderSystem.enableBlend();
            RenderSystem.blendEquation(32774);
            RenderSystem.blendFunc(770, 1);
            if (overlay != 0) {
                blit(matrices, x1, y1 + 1, 0, 0, overlay, x2 - x1, (int) ((y2 - y1) / 1.4f), bbHeight, bbWidth);
            }
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }

        // Custom Progress Bar
        if (CS_CONFIG.progressBarType.get() == MLSConfig.ProgressBarType.Custom) {
            int customWidth = CS_CONFIG.customProgressBarMode.get() == MLSConfig.ProgressBarMode.Linear ? x2 - x1 : i;
            if (CS_CONFIG.customProgressBarBackground.get()) {
                this.mc.getTextureManager().bindTexture(CUSTOM_PROGRESS_BAR_BACKGROUND_TEXTURE);
                blit(matrices, x1, y1, 0, 0, 6, x2 - x1, y2 - y1, 10, x2 - x1);
            }
            this.mc.getTextureManager().bindTexture(CUSTOM_PROGRESS_BAR_TEXTURE);
            blit(matrices, x1, y1, 0, 0, 6, i, y2 - y1, 10, customWidth);
        }

        // Vanilla / With Color progress bar
        if (CS_CONFIG.progressBarType.get() == MLSConfig.ProgressBarType.Vanilla) {
            int j = Math.round(opacity * 255.0F);
            int k = CS_CONFIG.progressBarColor.get() | 255 << 24;
            int kk = CS_CONFIG.progressFrameColor.get() | 255 << 24;
            fill(matrices, x1 + 2, y1 + 2, x1 + i, y2 - 2, k);
            fill(matrices, x1 + 1, y1, x2 - 1, y1 + 1, kk);
            fill(matrices, x1 + 1, y2, x2 - 1, y2 - 1, kk);
            fill(matrices, x1, y1, x1 + 1, y2, kk);
            fill(matrices, x2, y1, x2 - 1, y2, kk);
        }

    }

}
