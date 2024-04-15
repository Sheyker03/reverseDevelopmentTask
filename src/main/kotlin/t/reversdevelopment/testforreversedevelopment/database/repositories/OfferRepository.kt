package t.reversdevelopment.testforreversedevelopment.database.repositories

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import t.reversdevelopment.testforreversedevelopment.model.Offer


@Repository
interface OfferRepository : ReactiveMongoRepository<Offer, String> {

    @Query("{'geo.code': ?0}")
    fun findAllByGeo(geo: String, pageable: Pageable): Flux<Offer>
}