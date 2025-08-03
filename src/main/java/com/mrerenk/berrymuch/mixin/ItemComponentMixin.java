package com.mrerenk.berrymuch.mixin;

import com.mrerenk.berrymuch.Berrymuch;
import com.mrerenk.berrymuch.config.ModConfig;
import com.mrerenk.berrymuch.util.ComponentCacheManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemComponentMixin {

    @Shadow
    @Final
    private ComponentMap components;

    @Inject(method = "getComponents", at = @At("HEAD"), cancellable = true)
    private void modifyComponentsOnAccess(
        CallbackInfoReturnable<ComponentMap> cir
    ) {
        Item thisItem = (Item) (Object) this;
        Identifier itemId = Registries.ITEM.getId(thisItem);

        if (itemId != null) {
            String itemIdString = itemId.toString();

            // Check cache first
            ComponentMap cachedComponents =
                ComponentCacheManager.getCachedComponent(itemIdString);
            if (cachedComponents != null) {
                cir.setReturnValue(cachedComponents);
                return;
            }

            // Check if this item is in our config
            ModConfig config = Berrymuch.getConfig();
            if (config != null) {
                ModConfig.ItemConfig itemConfig = config.getItemConfig(
                    itemIdString
                );

                if (itemConfig != null) {
                    // Create modified food component using config values
                    FoodComponent newFoodComponent = new FoodComponent.Builder()
                        .nutrition(itemConfig.nutrition)
                        .saturationModifier(itemConfig.saturation)
                        .build();

                    // Build new component map
                    ComponentMap modifiedComponents = ComponentMap.builder()
                        .addAll(this.components)
                        .add(DataComponentTypes.FOOD, newFoodComponent)
                        .build();

                    // Cache the result
                    ComponentCacheManager.cacheComponent(
                        itemIdString,
                        modifiedComponents
                    );

                    cir.setReturnValue(modifiedComponents);
                }
            }
        }
    }
}
