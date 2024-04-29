package com.sergimarrahyarenas.api.models.characterstatistics

data class CharacterStatistics(
    val _links: Links? = null,
    val agility: Agility? = null,
    val armor: Armor,
    val attack_power: Int,
    val avoidance: Avoidance,
    val block: Block,
    val bonus_armor: Int,
    val character: Character,
    val dodge: Dodge,
    val health: Int,
    val intellect: Intellect,
    val lifesteal: Lifesteal,
    val main_hand_damage_max: Double,
    val main_hand_damage_min: Double,
    val main_hand_dps: Double,
    val main_hand_speed: Double,
    val mana_regen: Int,
    val mana_regen_combat: Int,
    val mastery: Mastery,
    val melee_crit: MeleeCrit,
    val melee_haste: MeleeHaste,
    val off_hand_damage_max: Double,
    val off_hand_damage_min: Double,
    val off_hand_dps: Double,
    val off_hand_speed: Double,
    val parry: Parry,
    val power: Int,
    val power_type: PowerType,
    val ranged_crit: RangedCrit,
    val ranged_haste: RangedHaste,
    val speed: Speed,
    val spell_crit: SpellCrit,
    val spell_haste: SpellHaste,
    val spell_penetration: Int,
    val spell_power: Int,
    val stamina: Stamina,
    val strength: Strength,
    val versatility: Int,
    val versatility_damage_done_bonus: Double,
    val versatility_damage_taken_bonus: Double,
    val versatility_healing_done_bonus: Double
)