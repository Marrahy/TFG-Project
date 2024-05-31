package com.sergimarrahyarenas.bloodstats.model.itemdata

data class ItemData(
    val _links: Links,
    val id: Int,
    val inventory_type: InventoryType,
    val is_equippable: Boolean,
    val is_stackable: Boolean,
    val item_class: ItemClass,
    val item_subclass: ItemSubclass,
    val level: Int,
    val max_count: Int,
    val media: Media,
    val name: String,
    val preview_item: PreviewItem,
    val purchase_price: Int,
    val purchase_quantity: Int,
    val quality: QualityX,
    val required_level: Int,
    val sell_price: Int
)