package items


interface IsDigable {
    val materialStrength: Int
    val materialDensity: Double
}

enum class State {
    SOLID, LIQUID, GASEOUS
}


enum class Material (var state: State) : IsDigable {
    SOIL(State.SOLID) {
        override val materialDensity: Double
            get() = 1200.0
        override val materialStrength: Int
            get() = 3
    },

    ROCK(State.SOLID) {
        override val materialDensity: Double
            get() = 3000.0
        override val materialStrength: Int
            get() = 30
    },

    WOOD(State.SOLID) {
        override val materialDensity: Double
            get() = 800.0
        override val materialStrength: Int
            get() = 10
    },

    WATER(State.LIQUID) {
        override val materialDensity: Double
            get() = 1000.0
        override val materialStrength: Int
            get() = 0
    },

    AIR(State.GASEOUS) {
        override val materialDensity: Double
            get() = 1.4
        override val materialStrength: Int
            get() = 0
    }
}
