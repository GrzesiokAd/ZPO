package com.project.service;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.exception.HttpException;

public final class ServiceUtil {

	private ServiceUtil() {
	}

	public static <T> RestResponsePage<T> getPage(URI uri, RestClient restClient,
			ParameterizedTypeReference<RestResponsePage<T>> responseType) {
		return restClient.get()
				.uri(uri.toString())
				.retrieve()
				.onStatus(HttpStatusCode::isError, (request, response) -> {
					throw new HttpException(response.getStatusCode(), response.getHeaders());
				})
				.body(responseType);
	}

	public static URI getURI(String resourcePath, Pageable pageable) {
		return getUriComponent(resourcePath, pageable).build().toUri();
	}

	public static UriComponentsBuilder getUriComponent(String resourcePath, Pageable pageable) {
		UriComponentsBuilder builder = getUriComponent(resourcePath);
		if (pageable != null) {
			builder.queryParam("page", pageable.getPageNumber());
			builder.queryParam("size", pageable.getPageSize());
			addSortParams(builder, pageable.getSort());
		}
		return builder;
	}

	public static UriComponentsBuilder getUriComponent(String resourcePath) {
		return UriComponentsBuilder.fromUriString(resourcePath);
	}

	private static void addSortParams(UriComponentsBuilder builder, Sort sort) {
		if (sort == null) {
			return;
		}
		for (Sort.Order order : sort) {
			builder.queryParam("sort", order.getProperty() + "," + order.getDirection());
		}
	}
}
