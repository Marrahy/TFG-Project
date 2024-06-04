package com.sergimarrahyarenas.bloodstats.data.specializations

import com.sergimarrahyarenas.bloodstats.R

data class CharacterClassSpecialization(
    val characterClass: String?,
    val characterSpec: String?
) {
    object Resources {
        private val attributeMap = hashMapOf(
            "Mago" to mapOf(
                "Escarcha" to R.string.intellect_text,
                "Fuego" to R.string.intellect_text,
                "Arcano" to R.string.intellect_text
            ),
            "Evocador" to mapOf(
                "Devastación" to R.string.intellect_text,
                "Preservación" to R.string.intellect_text
            ),
            "Druida" to mapOf(
                "Equilibrio" to R.string.intellect_text,
                "Restauración" to R.string.intellect_text,
                "Feral" to R.string.agility_text,
                "Guardián" to R.string.agility_text
            ),
            "Monje" to mapOf(
                "Tejedor de niebla" to R.string.intellect_text,
                "Maestro Cervecero" to R.string.agility_text
            ),
            "Paladín" to mapOf(
                "Sagrado" to R.string.intellect_text,
                "Protección" to R.string.intellect_text,
                "Reprensión" to R.string.strength_text
            ),
            "Sacerdote" to mapOf(
                "Disciplina" to R.string.intellect_text,
                "Sagrado" to R.string.intellect_text,
                "Sombra" to R.string.intellect_text
            ),
            "Chamán" to mapOf(
                "Elemental" to R.string.intellect_text,
                "Restauración" to R.string.intellect_text,
                "Mejora" to R.string.agility_text
            ),
            "Brujo" to mapOf(
                "Aflicción" to R.string.intellect_text,
                "Demonología" to R.string.intellect_text,
                "Destrucción" to R.string.intellect_text
            ),
            "Caballero de la muerte" to mapOf(
                "Escarcha" to R.string.strength_text,
                "Sangre" to R.string.strength_text,
                "Profano" to R.string.strength_text
            ),
            "Guerrero" to mapOf(
                "Furia" to R.string.strength_text,
                "Armas" to R.string.strength_text
            ),
            "Cazador de demonios" to mapOf(
                "Venganza" to R.string.agility_text,
                "Devastación" to R.string.agility_text
            ),
            "Cazador" to mapOf(
                "Bestias" to R.string.agility_text,
                "Puntería" to R.string.agility_text,
                "Supervivencia" to R.string.agility_text
            ),
            "Pícaro" to mapOf(
                "Asesinato" to R.string.agility_text,
                "Forajido" to R.string.agility_text,
                "Sutileza" to R.string.agility_text
            )
        )

        fun getAttribute(characterClassSpecialization: CharacterClassSpecialization): Int? {
            return attributeMap[characterClassSpecialization.characterClass]?.get(characterClassSpecialization.characterSpec)
        }
    }
}
