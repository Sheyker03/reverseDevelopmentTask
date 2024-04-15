package t.reversdevelopment.testforreversedevelopment.util.autoloader

import io.netty.channel.ChannelOption
import kotlinx.coroutines.*
import org.springframework.boot.CommandLineRunner
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.netty.http.client.HttpClient
import t.reversdevelopment.testforreversedevelopment.model.OfferCommon
import t.reversdevelopment.testforreversedevelopment.service.OfferService
import java.time.Duration


@Component
class LoaderOffers(private val offerService: OfferService) : CommandLineRunner {
    companion object {
        private const val PAGE = "page"
        private const val PERPAGE = "perpage"
        private const val PERPAGE_VALUE = 2
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun run(vararg args: String?) {
        offerService.clear()

        val httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(60))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)

        val webClient = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient)).build()
        val count = getTotalOffer(webClient)

        val countPage = count / PERPAGE_VALUE


        /**
         * Явно плохое решение в виде delay через корутины, должны быть методы через webflux...
         * */
        GlobalScope.launch {
            (0..(countPage)).map {
                getValuesByPageLimit(webClient, it, PERPAGE_VALUE)
                delay(100)
            }
        }


        val remains = count % PERPAGE_VALUE
        if (remains != 0)
            getValuesByPageLimit(webClient, countPage + 1, PERPAGE_VALUE)

    }


    /**
     * Я не лучшим образом знаю механики Spring Webflux
     * Flux можно сделать таким образом что бы он нормально работал в ограниченном worker-пуле + добавить задержки и проверки на запросы.
     * Но в моем конкретном случае было кучу ошибок 500 и 502, предполагаю что из за количества запросов в секунду.
     * Могу сделать корутинами ограничение по моно запросам, но через Flux не успел разобраться.
     * */
    private fun getValuesByPageLimit(
        webClient: WebClient,
        page: Int,
        count: Int
    ) {
        val uri = UriComponentsBuilder.fromHttpUrl("https://cityads.com/api/rest/webmaster/v2/offers/list")
            .queryParam(PERPAGE, count)
            .queryParam(PAGE, page)
            .build().toUri()


        webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(OfferCommon::class.java)
            .subscribe(offerService::save)
    }

    private fun getTotalOffer(webClient: WebClient): Int {
        val uri = UriComponentsBuilder.fromHttpUrl("https://cityads.com/api/rest/webmaster/v2/offers/list")
            .queryParam(PAGE, 0)
            .queryParam(PERPAGE, 0)
            .build().toUri()

        return webClient.get()
            .uri(uri)
            .retrieve().bodyToMono(OfferCommon::class.java).block()?.meta?.total
            ?: throw RuntimeException("Data is unloaded.")
    }
}

