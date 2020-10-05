package tuhljin.automagy.common.items;

import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public interface IAutomagyLocationLink {
    @Nonnull
    WorldSpecificCoordinates getLinkLocation(ItemStack stack);
}