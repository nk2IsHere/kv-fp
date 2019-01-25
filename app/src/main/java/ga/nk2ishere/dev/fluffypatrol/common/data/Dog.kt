package ga.nk2ishere.dev.fluffypatrol.common.data

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToMany
import java.util.*

@Entity class Dog(
        @Id var id: Long = 0,
        var name: String = "",
        var age: Int = 0,
        @Convert(converter = UUIDConverter::class, dbType = String::class) var photo: UUID = UUID.randomUUID(),
        @Convert(converter = GenderConverter::class, dbType = String::class) var gender: Dog.Gender = Gender.M,
        @Convert(converter = TypeConverter::class, dbType = String::class) var type: Dog.Type = Type.GOLDEN_RETRIVER
) {
    constructor(): this(0, "", 0, UUID.randomUUID(), Dog.Gender.M, Dog.Type.GOLDEN_RETRIVER)

    @Backlink lateinit var health: ToMany<Health>

    internal data class HealthByAge(
            val weight: HashMap<Int, IntRange>,
            val growth: HashMap<Int, IntRange>
    )

    enum class Gender { M, F }
    enum class Type(
            val prettyName: String,
            val health: HashMap<Gender, HealthByAge>
    ) {
        GOLDEN_RETRIVER("Золотой ретривер", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 30..34), hashMapOf(0 to 56..61)),
                Gender.F to HealthByAge(hashMapOf(0 to 25..32), hashMapOf(0 to 51..56))
        )),
        BULLDOG("Бульдог", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 23..28), hashMapOf(0 to 31..40)),
                Gender.F to HealthByAge(hashMapOf(0 to 18..23), hashMapOf(0 to 31..40))
        )),
        BIGGLE("Биггль", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 18..22), hashMapOf(0 to 32..37)),
                Gender.F to HealthByAge(hashMapOf(0 to 16..20), hashMapOf(0 to 33..38))
        )),
        MOPS("Мопс", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 5..8), hashMapOf(0 to 22..28)),
                Gender.F to HealthByAge(hashMapOf(0 to 5..8), hashMapOf(0 to 27..33))
        )),
        MALTEZE("Мальтезе", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 3..4), hashMapOf(0 to 20..23)),
                Gender.F to HealthByAge(hashMapOf(0 to 3..4), hashMapOf(0 to 21..25))
        )),
        SHI_TSU("Ши-тцу", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 4..7), hashMapOf(0 to 20..28)),
                Gender.F to HealthByAge(hashMapOf(0 to 4..7), hashMapOf(0 to 20..28))
        )),
        TOY_PUDEL("Той-пудель", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 3..5), hashMapOf(0 to 24..28)),
                Gender.F to HealthByAge(hashMapOf(0 to 3..5), hashMapOf(0 to 24..28))
        )),
        SHPITS("Шпиц", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 1..4), hashMapOf(0 to 17..23)),
                Gender.F to HealthByAge(hashMapOf(0 to 1..4), hashMapOf(0 to 17..23))
        )),
        STANDART_PUDEL("Стандартный пудель", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 35..56), hashMapOf(0 to 28..35)),
                Gender.F to HealthByAge(hashMapOf(0 to 45..60), hashMapOf(0 to 28..35))
        )),
        MINIATURE_PUDEL("Миниатюрный пудель", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 8..12), hashMapOf(0 to 28..35)),
                Gender.F to HealthByAge(hashMapOf(0 to 8..12), hashMapOf(0 to 28..35))
        )),
        SIBERIAN_HASKI("Сибирский хаски", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 20..27), hashMapOf(0 to 54..60)),
                Gender.F to HealthByAge(hashMapOf(0 to 16..23), hashMapOf(0 to 50..56))
        )),
        ALASKA_MALAMUT("Аляскинский маламут", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 36..43), hashMapOf(0 to 61..66)),
                Gender.F to HealthByAge(hashMapOf(0 to 32..38), hashMapOf(0 to 56..61))
        )),
        CHAO_CHAO("Чау-чау", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 25..32), hashMapOf(0 to 48..56)),
                Gender.F to HealthByAge(hashMapOf(0 to 20..27), hashMapOf(0 to 46..51))
        )),
        AKITA_INU("Акита-ину", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 34..59), hashMapOf(0 to 66..71)),
                Gender.F to HealthByAge(hashMapOf(0 to 34..50), hashMapOf(0 to 61..66))
        )),
        ROTVEYLER("Ротвейлер", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 50..60), hashMapOf(0 to 61..69)),
                Gender.F to HealthByAge(hashMapOf(0 to 35..48), hashMapOf(0 to 56..63))
        )),
        DOBERMAN("Доберман", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 40..45), hashMapOf(0 to 66..72)),
                Gender.F to HealthByAge(hashMapOf(0 to 32..35), hashMapOf(0 to 61..68))
        )),
        GERMAN_DOG("Немецкий дог", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 45..90), hashMapOf(0 to 81..86)),
                Gender.F to HealthByAge(hashMapOf(0 to 45..59), hashMapOf(0 to 71..81))
        )),
        SELFEAT_DOG("Самоедская собака", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 20..23), hashMapOf(0 to 53..56)),
                Gender.F to HealthByAge(hashMapOf(0 to 16..20), hashMapOf(0 to 48..53))
        )),
        SIBA_INU("Сиба-ину", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 8..11), hashMapOf(0 to 35..43)),
                Gender.F to HealthByAge(hashMapOf(0 to 6..9), hashMapOf(0 to 33..41))
        )),
        SENBENBAR("Сенбернар", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 64..120), hashMapOf(0 to 65..80)),
                Gender.F to HealthByAge(hashMapOf(0 to 64..120), hashMapOf(0 to 70..90))
        )),
        DALMATIN("Далматин", hashMapOf(
                Gender.M to HealthByAge(hashMapOf(0 to 15..32), hashMapOf(0 to 58..61)),
                Gender.F to HealthByAge(hashMapOf(0 to 16..24), hashMapOf(0 to 56..58))
        ))
    }

    class GenderConverter: PropertyConverter<Gender, String> {
        override fun convertToDatabaseValue(entityProperty: Gender?): String? =
                entityProperty?.toString()

        override fun convertToEntityProperty(databaseValue: String?): Gender? =
                databaseValue?.let { Gender.valueOf(it) }

    }

    class TypeConverter: PropertyConverter<Type, String> {
        override fun convertToDatabaseValue(entityProperty: Type?): String? =
                entityProperty?.toString()

        override fun convertToEntityProperty(databaseValue: String?): Type? =
                databaseValue?.let { Type.valueOf(it) }
    }

    class UUIDConverter: PropertyConverter<UUID, String> {
        override fun convertToDatabaseValue(entityProperty: UUID?): String? =
                entityProperty?.toString()

        override fun convertToEntityProperty(databaseValue: String?): UUID? =
                databaseValue?.let { UUID.fromString(it) }
    }
}