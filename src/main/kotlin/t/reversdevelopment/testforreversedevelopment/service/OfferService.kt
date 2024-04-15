package t.reversdevelopment.testforreversedevelopment.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import t.reversdevelopment.testforreversedevelopment.database.repositories.OfferRepository
import t.reversdevelopment.testforreversedevelopment.model.OfferCommon
import t.reversdevelopment.testforreversedevelopment.model.OfferMeta

@Service
class OfferService(private val offerRepository: OfferRepository) {

    fun save(offers: OfferCommon) {
        val finalOffers = offers.offers.filter { it.geo.filter { geo -> geo.code != "Wrld" }.isNotEmpty() }
        if (finalOffers.isNotEmpty())
            offerRepository.saveAll(finalOffers).subscribe { println("Entity saved - ${it.name}") }
    }

    fun findByPageLimit(geo: String, page: Int, limit: Int): Mono<OfferCommon> {
        val count = offerRepository.count().block()!!.toInt()
        val paging: Pageable = PageRequest.of(page, limit)

        return offerRepository.findAllByGeo(geo, paging).collectList()
            .flatMapMany { Mono.just(OfferCommon(it, OfferMeta(count))) }.next()
    }

    fun clear() = offerRepository.deleteAll().subscribe { println("Database is cleared") }
}