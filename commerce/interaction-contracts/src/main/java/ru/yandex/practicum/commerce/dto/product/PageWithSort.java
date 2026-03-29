package ru.yandex.practicum.commerce.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;

public class PageWithSort<T> extends PagedModel<T> {
    private final Sort sort;

    public PageWithSort(Page<T> page, Sort sort) {
        super(page);
        this.sort = sort;
    }

    @JsonProperty
    public Sort getSort() {
        return sort;
    }
}
