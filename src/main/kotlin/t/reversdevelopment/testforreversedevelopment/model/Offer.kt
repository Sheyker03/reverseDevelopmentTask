package t.reversdevelopment.testforreversedevelopment.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "offers")
data class Offer(
    @Id
    @JsonIgnore
    val id: String? = null,
    val name: String,
    @JsonProperty("offer_currency")
    val offerCurrency: OfferCurrency,
    @JsonProperty("payment_time")
    val paymentTime: Double,
    @JsonProperty("approval_time")
    val approvalTime: Double,
    @JsonProperty("site_url")
    val siteUrl: String,
    val logo: String,
    val geo: List<OfferGeo>,
    val stat: OfferStat,
    val rating: Rating = Rating(
        10 * (1 - approvalTime / 90),
        100 * (1 - paymentTime / 90),
        stat.ecpl
    )
)