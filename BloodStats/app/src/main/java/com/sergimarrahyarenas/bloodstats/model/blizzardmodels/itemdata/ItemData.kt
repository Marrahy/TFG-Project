package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata

data class ItemData(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Links,
    val id: Int,
    val inventory_type: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.InventoryType,
    val is_equippable: Boolean,
    val is_stackable: Boolean,
    val item_class: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemClass,
    val item_subclass: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemSubclass,
    val level: Int,
    val max_count: Int,
    val media: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Media,
    val name: String,
    val preview_item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.PreviewItem,
    val purchase_price: Int,
    val purchase_quantity: Int,
    val quality: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.QualityX,
    val required_level: Int,
    val sell_price: Int
)