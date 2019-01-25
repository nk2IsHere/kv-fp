package ga.nk2ishere.dev.fluffypatrol.common.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity data class Health(
        @Id var id: Long = 0,
        var date: Long,
        var weight: Double,
        var growth: Double,
        var coefficient: Double
) {
    constructor(): this(0, 0L,.0, .0, .0)
    
    lateinit var dog: ToOne<Dog>
}