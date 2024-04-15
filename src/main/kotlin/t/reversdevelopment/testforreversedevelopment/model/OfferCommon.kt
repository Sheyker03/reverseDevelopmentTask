package t.reversdevelopment.testforreversedevelopment.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Offer Common", description = "Common data with offers")
data class OfferCommon(
    val offers: List<Offer>,
    val meta: OfferMeta
)

data class OfferMeta(
    val total: Int
)
