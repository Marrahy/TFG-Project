package com.sergimarrahyarenas.bloodstats.models.itemdata

import Stat
import com.sergimarrahyarenas.bloodstats.models.characterequipment.Quality

data class PreviewItem(
    val armor: Armor,
    val binding: Binding,
    val bonus_list: List<Int>,
    val context: Int,
    val durability: Durability,
    val inventory_type: InventoryType,
    val item: Item,
    val item_class: ItemClass,
    val item_subclass: ItemSubclass,
    val level: Level,
    val media: Media,
    val name: String,
    val quality: Quality,
    val requirements: Requirements,
    val sell_price: SellPrice,
    val `set`: Set,
    val stats: List<Stat>
)