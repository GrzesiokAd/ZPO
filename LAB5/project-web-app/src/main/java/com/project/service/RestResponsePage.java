package com.project.service;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponsePage<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	@ConstructorProperties({ "content", "number", "size", "totalElements", "pageable", "last", "totalPages", "sort",
			"first", "numberOfElements" })
	public RestResponsePage(@JsonProperty("content") List<T> content,
			@JsonProperty("number") int number,
			@JsonProperty("size") int size,
			@JsonProperty("totalElements") Long totalElements,
			@JsonProperty("pageable") Object pageable,
			@JsonProperty("last") boolean last,
			@JsonProperty("totalPages") int totalPages,
			@JsonProperty("sort") Object sort,
			@JsonProperty("first") boolean first,
			@JsonProperty("numberOfElements") int numberOfElements) {
		super(content == null ? new ArrayList<>() : content, PageRequest.of(number, size),
				totalElements == null ? 0 : totalElements);
	}

	public RestResponsePage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public RestResponsePage(List<T> content) {
		super(content);
	}

	public RestResponsePage() {
		super(new ArrayList<>());
	}
}
