package com.sergimarrahyarenas.bloodstats.models.itemdata

data class Data(
    val id: Int,
    val inventory_type: InventoryType,
    val is_equippable: Boolean,
    val is_stackable: Boolean,
    val item_class: ItemClass,
    val item_subclass: ItemSubclass,
    val level: Int,
    val max_count: Int,
    val media: Media,
    val name: NameXXX,
    val purchase_price: Int,
    val purchase_quantity: Int,
    val quality: Quality,
    val required_level: Int,
    val sell_price: Int
)