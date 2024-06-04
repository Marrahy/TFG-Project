import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Display
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Type

data class Stat(
    val display: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Display,
    val is_equip_bonus: Boolean,
    val is_negated: Boolean,
    val type: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.Type,
    val value: Int
)