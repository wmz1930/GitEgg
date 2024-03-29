package com.gitegg.gateway.filter;

import com.gitegg.gateway.context.GatewayContext;
import com.gitegg.gateway.option.FilterOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultClientResponse;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 读取并缓存响应数据
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 *
 * @author: chenggang
 * @createTime: 2019-04-11
 * @version: v1.2.0
 */
@Slf4j
public class GatewayResponseContextFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if(!gatewayContext.getReadResponseData()){
            log.debug("[ResponseLogFilter]Properties Set Not To Read Response Data");
            return chain.filter(exchange);
        }
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                return DataBufferUtils.join(Flux.from(body))
                        .flatMap(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                                DataBufferUtils.retain(buffer);
                                return Mono.just(buffer).doFinally(s -> {
                                    DataBufferUtils.release(buffer);
                                });
                            });
                            BodyInserter<Flux<DataBuffer>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromDataBuffers(cachedFlux);
                            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getResponse().getHeaders());
                            DefaultClientResponse clientResponse = new DefaultClientResponse(new ResponseAdapter(cachedFlux, exchange.getResponse().getHeaders()), ExchangeStrategies.withDefaults());
                            Optional<MediaType> optionalMediaType = clientResponse.headers().contentType();
                            if(!optionalMediaType.isPresent()){
                                log.debug("[ResponseLogFilter]Response ContentType Is Not Exist");
                                return Mono.defer(()-> bodyInserter.insert(outputMessage, new BodyInserterContext())
                                        .then(Mono.defer(() -> {
                                            Flux<DataBuffer> messageBody = cachedFlux;
                                            HttpHeaders headers = getDelegate().getHeaders();
                                            if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                                messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                                            }
                                            return getDelegate().writeWith(messageBody);
                                        })));
                            }
                            MediaType contentType = optionalMediaType.get();
                            if(!contentType.equals(MediaType.APPLICATION_JSON) && !contentType.equals(MediaType.APPLICATION_JSON_UTF8)){
                                log.debug("[ResponseLogFilter]Response ContentType Is Not APPLICATION_JSON Or APPLICATION_JSON_UTF8");
                                return Mono.defer(()-> bodyInserter.insert(outputMessage, new BodyInserterContext())
                                        .then(Mono.defer(() -> {
                                            Flux<DataBuffer> messageBody = cachedFlux;
                                            HttpHeaders headers = getDelegate().getHeaders();
                                            if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                                messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                                            }
                                            return getDelegate().writeWith(messageBody);
                                        })));
                            }
                            return clientResponse.bodyToMono(Object.class)
                                    .doOnNext(originalBody -> {
                                        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
                                        gatewayContext.setResponseBody(originalBody);
                                        log.debug("[ResponseLogFilter]Read Response Data To Gateway Context Success");
                                    })
                                    .then(Mono.defer(()-> bodyInserter.insert(outputMessage, new BodyInserterContext())
                                            .then(Mono.defer(() -> {
                                                Flux<DataBuffer> messageBody = cachedFlux;
                                                HttpHeaders headers = getDelegate().getHeaders();
                                                if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                                    messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                                                }
                                                return getDelegate().writeWith(messageBody);
                                            }))));
                        });

            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body)
                        .flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.RESPONSE_DATA_FILTER.getOrder();
    }

    public class ResponseAdapter implements ClientHttpResponse {

        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;

        public ResponseAdapter(Publisher<? extends DataBuffer> body, HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = (Flux) body;
            } else {
                flux = ((Mono)body).flux();
            }
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public int getRawStatusCode() {
            return 0;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }
    }

}
