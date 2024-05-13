package com.sergimarrahyarenas.bloodstats.data

import android.util.Log

data class CharacterClassSpecialization(
    val characterClass: String?,
    val characterSpec: String?
) {
    object Resources {
        private val attributeMap = hashMapOf(
            "Mago" to mapOf(
                "Escarcha" to "Intelecto",
                "Fuego" to "Intelecto",
                "Arcano" to "Intelecto"
            ),
            "Evocador" to mapOf(
                "Devastación" to "Intelecto",
                "Preservación" to "Intelecto"
            ),
            "Druida" to mapOf(
                "Equilibrio" to "Intelecto",
                "Restauración" to "Intelecto",
                "Feral" to "Agilidad",
                "Guardián" to "Agilidad"
            ),
            "Monje" to mapOf(
                "Tejedor de niebla" to "Intelecto",
                "Maestro Cervecero" to "Agilidad"
            ),
            "Paladín" to mapOf(
                "Sagrado" to "Intelecto",
                "Protección" to "Intelecto",
                "Reprensión" to "Fuerza"
            ),
            "Sacerdote" to mapOf(
                "Disciplina" to "Intelecto",
                "Sagrado" to "Intelecto",
                "Sombra" to "Intelecto"
            ),
            "Chamán" to mapOf(
                "Elemental" to "Intelecto",
                "Restauración" to "Intelecto",
                "Mejora" to "Agilidad"
            ),
            "Brujo" to mapOf(
                "Aflicción" to "Intelecto",
                "Demonología" to "Intelecto",
                "Destrucción" to "Intelecto"
            ),
            "Caballero de la muerte" to mapOf(
                "Escarcha" to "Fuerza",
                "Sangre" to "Fuerza",
                "Profano" to "Fuerza"
            ),
            "Guerrero" to mapOf(
                "Furia" to "Fuerza",
                "Armas" to "Fuerza"
            ),
            "Cazador de demonios" to mapOf(
                "Venganza" to "Agilidad",
                "Devastación" to "Agilidad"
            ),
            "Cazador" to mapOf(
                "Bestias" to "Agilidad",
                "Puntería" to "Agilidad",
                "Supervivencia" to "Agilidad"
            ),
            "Pícaro" to mapOf(
                "Asesinato" to "Agilidad",
                "Forajido" to "Agilidad",
                "Sutileza" to "Agilidad"
            )
        )

        fun getAttribute(characterClassSpecialization: CharacterClassSpecialization): String? {
            Log.d("attribute: ", "${attributeMap[characterClassSpecialization.characterClass]?.get(characterClassSpecialization.characterSpec)}")
            return attributeMap[characterClassSpecialization.characterClass]?.get(characterClassSpecialization.characterSpec)
        }
    }
}
