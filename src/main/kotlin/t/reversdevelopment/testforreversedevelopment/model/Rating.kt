package t.reversdevelopment.testforreversedevelopment.model


data class Rating(
    val coeff1: Double,
    val coeff2: Double,
    val ecpl: Double,
    val value: Double = if (coeff1.toInt() == 0) 1.0 else coeff1
            * if (coeff2.toInt() == 0) 1.0 else coeff2
            * if (ecpl.toInt() == 0) 1.0 else ecpl
)
