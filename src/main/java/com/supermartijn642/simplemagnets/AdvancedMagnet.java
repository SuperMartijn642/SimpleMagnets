package com.supermartijn642.simplemagnets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    public static final DataComponentType<Settings> SETTINGS = DataComponentType.<Settings>builder()
        .persistent(RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("collectItems").forGetter(Settings::collectItems),
            ExtraCodecs.POSITIVE_INT.fieldOf("itemRange").forGetter(Settings::itemRange),
            Codec.BOOL.fieldOf("collectXp").forGetter(Settings::collectXp),
            ExtraCodecs.POSITIVE_INT.fieldOf("xpRange").forGetter(Settings::xpRange),
            Codec.BOOL.fieldOf("whitelist").forGetter(Settings::isWhitelist),
            Codec.BOOL.fieldOf("filterDurability").forGetter(Settings::isFilterDurability),
            ExtraCodecs.optionalEmptyMap(ItemStack.SINGLE_ITEM_CODEC).listOf(9, 9).fieldOf("itemFilter").forGetter(s -> Arrays.stream(s.itemFilter).map(Optional::ofNullable).toList())
        ).apply(instance, (a, b, c, d, e, f, filter) -> new Settings(a, b, c, d, e, f, filter.stream().map(o -> o.orElse(null)).toArray(ItemStack[]::new)))))
        .networkSynchronized(new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buffer, Settings settings){
                buffer.writeBoolean(settings.collectItems);
                buffer.writeInt(settings.itemRange);
                buffer.writeBoolean(settings.collectXp);
                buffer.writeInt(settings.xpRange);
                buffer.writeBoolean(settings.isWhitelist);
                buffer.writeBoolean(settings.isFilterDurability);
                for(ItemStack stack : settings.itemFilter){
                    buffer.writeBoolean(stack != null);
                    if(stack != null)
                        ItemStack.STREAM_CODEC.encode(buffer, stack);
                }
            }

            @Override
            public Settings decode(RegistryFriendlyByteBuf buffer){
                boolean collectItems = buffer.readBoolean();
                int itemRange = buffer.readInt();
                boolean collectXp = buffer.readBoolean();
                int xpRange = buffer.readInt();
                boolean isWhitelist = buffer.readBoolean();
                boolean isFilterDurability = buffer.readBoolean();
                ItemStack[] itemFilter = new ItemStack[9];
                for(int i = 0; i < itemFilter.length; i++){
                    if(buffer.readBoolean())
                        itemFilter[i] = ItemStack.STREAM_CODEC.decode(buffer);
                }
                return new Settings(collectItems, itemRange, collectXp, xpRange, isWhitelist, isFilterDurability, itemFilter);
            }
        }).build();

    @Override
    public ItemUseResult interact(ItemStack stack, Player player, InteractionHand hand, Level level){
        if(!player.isShiftKeyDown())
            return super.interact(stack, player, hand, level);

        int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
        if(!level.isClientSide)
            CommonUtils.openContainer(new MagnetContainer(player, slot));
        return ItemUseResult.success(stack);
    }

    @Override
    protected boolean canPickupItems(ItemStack magnet){
        //noinspection DataFlowIssue
        return !magnet.has(SETTINGS) || magnet.get(SETTINGS).collectItems();
    }

    @Override
    protected boolean canPickupStack(ItemStack magnet, ItemStack stack){
        if(!magnet.has(SETTINGS))
            return true;
        Settings settings = magnet.get(SETTINGS);
        for(int slot = 0; slot < 9; slot++){
            //noinspection DataFlowIssue
            if(settings.itemFilter[slot] != null){
                ItemStack filterStack = settings.itemFilter[slot];
                // Check whether the stack and the filter match
                if(ItemStack.isSameItem(stack, filterStack)
                    && (!settings.isFilterDurability || ItemStack.isSameItemSameComponents(stack, filterStack)))
                    return settings.isWhitelist;
            }
        }
        return !settings.isWhitelist;
    }

    @Override
    protected boolean canPickupXp(ItemStack magnet){
        //noinspection DataFlowIssue
        return !magnet.has(SETTINGS) || magnet.get(SETTINGS).collectXp();
    }

    @Override
    protected int getRangeItems(ItemStack magnet){
        //noinspection DataFlowIssue
        return magnet.has(SETTINGS) ? magnet.get(SETTINGS).itemRange() : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected int getRangeXp(ItemStack magnet){
        //noinspection DataFlowIssue
        return magnet.has(SETTINGS) ? magnet.get(SETTINGS).xpRange() : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected Component getTooltip(){
        return TextComponents.translation("simplemagnets.advancedmagnet.info", TextComponents.number(SMConfig.advancedMagnetMaxRange.get()).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get();
    }

    public record Settings(boolean collectItems, int itemRange, boolean collectXp, int xpRange, boolean isWhitelist,
                           boolean isFilterDurability, ItemStack[] itemFilter) {

        public static Settings defaultSettings(){
            return new Settings(true, SMConfig.advancedMagnetRange.get(), true, SMConfig.advancedMagnetRange.get(), false, false, new ItemStack[9]);
        }

        public Settings collectItems(boolean value){
            if(this.collectItems == value)
                return this;
            return new Settings(value, this.itemRange, this.collectXp, this.xpRange, this.isWhitelist, this.isFilterDurability, this.itemFilter);
        }

        public Settings itemRange(int value){
            if(this.itemRange == value)
                return this;
            return new Settings(this.collectItems, value, this.collectXp, this.xpRange, this.isWhitelist, this.isFilterDurability, this.itemFilter);
        }

        public Settings collectXp(boolean value){
            if(this.collectXp == value)
                return this;
            return new Settings(this.collectItems, this.itemRange, value, this.xpRange, this.isWhitelist, this.isFilterDurability, this.itemFilter);
        }

        public Settings xpRange(int value){
            if(this.xpRange == value)
                return this;
            return new Settings(this.collectItems, this.itemRange, this.collectXp, value, this.isWhitelist, this.isFilterDurability, this.itemFilter);
        }

        public Settings whitelist(boolean value){
            if(this.isWhitelist == value)
                return this;
            return new Settings(this.collectItems, this.itemRange, this.collectXp, this.xpRange, value, this.isFilterDurability, this.itemFilter);
        }

        public Settings filterDurability(boolean value){
            if(this.isFilterDurability == value)
                return this;
            return new Settings(this.collectItems, this.itemRange, this.collectXp, this.xpRange, this.isWhitelist, value, this.itemFilter);
        }

        public Settings itemFilter(int index, ItemStack stack){
            if(this.itemFilter[index] == null ? stack == null : stack != null && ItemStack.isSameItemSameComponents(this.itemFilter[index], stack))
                return this;
            ItemStack[] filter = Arrays.copyOf(this.itemFilter, this.itemFilter.length);
            filter[index] = stack;
            return new Settings(this.collectItems, this.itemRange, this.collectXp, this.xpRange, this.isWhitelist, this.isFilterDurability, filter);
        }
    }
}
