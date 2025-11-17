package net.gayland.raremobtracker.mixin;

import net.gayland.raremobtracker.RareMobTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    private static final Set<String> LIST = Set.of(
    "The Elephelk", "Bob", "Unicorn"
);
    @Inject(at = @At("RETURN"), method = "addEntity")
    public void onAddEntity(Entity entity, CallbackInfo ci) {
        String name = "";
        String getText = "";
        String getStyledDisplayName = "";
        String getCustomName = "";
        String getName = "";
        String getDisplayName = "";
        if(!RareMobTracker.enabled) return;
        /*
        if (entity.getType().toString().equals("entity.minecraft.text_display")){
            TrackedData<Text> textKey = TextDisplayAccessor.getTextTrackedData();
            Text text = entity.getDataTracker().get(textKey);
            Text newText = ((TextDisplayEntity)entity).getText();
            Text CustomName = entity.getCustomName();
            if(CustomName != null){
                getCustomName = CustomName.toString();
            }
            Text DisplayName = entity.getDisplayName();
            if(DisplayName != null){
                getDisplayName = DisplayName.toString();
            }
            Text Name = entity.getName();
            if(Name != null){
                getName = Name.toString();
            }
            Text StyledDisplayName = entity.getStyledDisplayName();
            if(StyledDisplayName != null){
                getStyledDisplayName = StyledDisplayName.toString();
            }

            Text t = Text.of(String.format("getDisplayName %s getStyledDisplayName %s getCustomName %s getName %s Text %s.", getDisplayName, getStyledDisplayName, getCustomName, getName, newText));
                List<Text> a = t.getWithStyle(t.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/compass %d %d %d", entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ()))));
                MutableText mt = Text.empty();
                for(Text tt : a) {
                    mt.append(tt);
                }
                MinecraftClient.getInstance().player.sendMessage(mt, false);
                World world = entity.getEntityWorld();
                if (!world.isClient) {
                    //world.playSound(null, MinecraftClient.getInstance().player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.5f, 1f);
            }
        }
        */

        if (entity instanceof DisplayEntity.TextDisplayEntity textDisplay) {
            TrackedData<Text> textKey = TextDisplayAccessor.getTextTrackedData();
            Text text = textDisplay.getDataTracker().get(textKey);
            Text directText = textDisplay.getText();
            if (text != null) {
                String displayText = text.getString().trim();
                
                // ... rest of your logic
                Text t = Text.of(String.format("Rare mob [%s] [%s] type [%s] spawned at [%s]. Click to set compass target.", displayText, directText, entity.getType().toString(), entity.getBlockPos().toShortString()));
                List<Text> a = t.getWithStyle(t.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/compass %d %d %d", entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ()))));
                MutableText mt = Text.empty();
                for(Text tt : a) {
                    mt.append(tt);
                }
                MinecraftClient.getInstance().player.sendMessage(mt, false);
                World world = entity.getEntityWorld();
                if (!world.isClient) {
                    //world.playSound(null, MinecraftClient.getInstance().player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.5f, 1f);
            }
        }
}










        /* 
        if (entity instanceof TextDisplayEntity textDisplay) {
            Text rawText = textDisplay.getText();      // <-- IMPORTANT
            if(rawText != null){
                getText = rawText.getString().trim();      // Extracts all visible text

                getStyledDisplayName = rawText.getString().trim();      // Extracts all visible text

                getCustomName = rawText.getString().trim();      // Extracts all visible text

                getName = rawText.getString().trim();      // Extracts all visible text
                if(getText == null){
                    getText = "null";
                }
                if(getStyledDisplayName == null){
                    getText = "null";
                }
                if(getCustomName == null){
                    getText = "null";
                }
                if(getName == null){
                    getText = "null";
                }
                Text t = Text.of(String.format("getText %s getStyledDisplayName %s getCustomName %s getName %s.", getText, getStyledDisplayName, getCustomName, getName));
                List<Text> a = t.getWithStyle(t.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/compass %d %d %d", entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ()))));
                MutableText mt = Text.empty();
                for(Text tt : a) {
                    mt.append(tt);
                }
                //MinecraftClient.getInstance().player.sendMessage(mt, false);
                World world = entity.getEntityWorld();
                if (!world.isClient) {
                    //world.playSound(null, MinecraftClient.getInstance().player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.5f, 1f);
                }
            }
        }
            */
        // If no name was detected, stop here.
        //if (name.isEmpty()) return;
        //LIST.contains(name)
        if(!name.isEmpty()) {
            Text t = Text.of(String.format("Rare mob [%s] type [%s] spawned at [%s]. Click to set compass target.", name, entity.getType().toString(), entity.getBlockPos().toShortString()));
            List<Text> a = t.getWithStyle(t.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/compass %d %d %d", entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ()))));
            MutableText mt = Text.empty();
            for(Text tt : a) {
                mt.append(tt);
            }
            //MinecraftClient.getInstance().player.sendMessage(mt, false);
            World world = entity.getEntityWorld();
            if (!world.isClient) {
                world.playSound(null, MinecraftClient.getInstance().player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.5f, 1f);
            }
        }
    }
}
