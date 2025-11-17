package net.gayland.raremobtracker.mixin;

import net.gayland.raremobtracker.RareMobTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.List;

@Mixin(DisplayEntity.TextDisplayEntity.class)
public abstract class TextDisplayEntityMixin {
    private static final Set<String> RARE_MOBS = Set.of(
        "The Elephelk", "Bob", "Unicorn"
    );
    
    @Unique
    private boolean rareMobTracker$hasNotified = false;
    
    @Inject(method = "onTrackedDataSet", at = @At("RETURN"))
    private void onTextDataUpdate(TrackedData<?> data, CallbackInfo ci) {
        if (!RareMobTracker.enabled) return;
        if (rareMobTracker$hasNotified) return; // Only notify once per entity
        
        DisplayEntity.TextDisplayEntity self = (DisplayEntity.TextDisplayEntity)(Object)this;
        
        // Only process on client side
        World world = self.getEntityWorld();
        if (!world.isClient) return;
        
        Text rawText = self.getText();
        if (rawText == null || rawText.getString().isEmpty()) return;
        
        String displayText = rawText.getString().trim();
        
        // Check if this text contains any of our rare mob names
        boolean isRareMob = RARE_MOBS.stream().anyMatch(displayText::contains);
        
        if (isRareMob) {
            rareMobTracker$hasNotified = true; // Mark as notified
            
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            
            // Create clickable message
            Text t = Text.of(String.format("Rare mob [%s] spawned at [%s]. Click to set compass target.", 
                displayText, self.getBlockPos().toShortString()));
            
            List<Text> a = t.getWithStyle(t.getStyle().withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
                    String.format("/compass %d %d %d", 
                        self.getBlockPos().getX(), 
                        self.getBlockPos().getY(), 
                        self.getBlockPos().getZ()))));
            
            MutableText mt = Text.empty();
            for (Text tt : a) {
                mt.append(tt);
            }
            
            // Send message to player
            client.player.sendMessage(mt, false);
            
            // Play sound
            world.playSound(client.player, 
                client.player.getBlockPos(), 
                SoundEvents.ENTITY_WITHER_SPAWN, 
                SoundCategory.MASTER, 
                1.5f, 1f);
        }
    }
}