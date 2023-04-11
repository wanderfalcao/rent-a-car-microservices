package br.com.infnet.wander.domain.mapper;

import br.com.infnet.wander.domain.dto.OrderRequest;
import br.com.infnet.wander.domain.dto.OrderResponse;
import br.com.infnet.wander.model.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    Order create(OrderRequest request);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    Order update(OrderRequest request, @MappingTarget Order order);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    OrderResponse create(Order request);
}
