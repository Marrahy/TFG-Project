package com.sergimarrahyarenas.bloodstats.models.characterequipment

data class Enchantment(
    val display_string: String,
    val enchantment_id: Int,
    val enchantment_slot: EnchantmentSlot,
    val source_item: SourceItem
)