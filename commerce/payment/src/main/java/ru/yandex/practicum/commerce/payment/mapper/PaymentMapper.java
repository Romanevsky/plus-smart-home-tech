package ru.yandex.practicum.commerce.payment.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.PaymentModel;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto modelToDto(PaymentModel model);
}
