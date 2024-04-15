package t.reversdevelopment.testforreversedevelopment.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import t.reversdevelopment.testforreversedevelopment.model.OfferCommon
import t.reversdevelopment.testforreversedevelopment.service.OfferService

@RestController
@RequestMapping("/offers")
@Tag(name = "Offers", description = "Controller for Offers")
class OfferController(val offerService: OfferService) {
    @Operation(summary = "Get offers")
    @GetMapping("/{geo}")
    fun getOffers(
        @PathVariable geo: String,
        @RequestParam page: Int = 0,
        @Max(20)
        @RequestParam limit: Int = 5
    ): Mono<OfferCommon> {
        return offerService.findByPageLimit(geo, page, limit)
    }
}