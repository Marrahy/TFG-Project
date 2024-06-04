package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Socket(
    val display_string: String,
    val item: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.ItemX,
    val media: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Media,
    val socket_type: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.SocketType
)