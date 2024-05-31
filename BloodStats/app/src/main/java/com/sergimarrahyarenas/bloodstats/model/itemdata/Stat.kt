import com.sergimarrahyarenas.bloodstats.model.itemdata.Display
import com.sergimarrahyarenas.bloodstats.model.itemdata.Type

data class Stat(
    val display: Display,
    val is_equip_bonus: Boolean,
    val is_negated: Boolean,
    val type: Type,
    val value: Int
)