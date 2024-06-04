package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata

import Stat
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Quality

data class PreviewItem(
    val armor: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Armor,
    val binding: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Binding,
    val bonus_list: List<Int>,
    val context: Int,
    val durability: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Durability,
    val inventory_type: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.InventoryType,
    val item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Item,
    val item_class: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemClass,
    val item_subclass: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemSubclass,
    val level: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Level,
    val media: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Media,
    val name: String,
    val quality: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Quality,
    val requirements: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Requirements,
    val sell_price: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.SellPrice,
    val `set`: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Set,
    val stats: List<Stat>
)