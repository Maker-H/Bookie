package web.bookie.dto.request;

import web.bookie.domain.BaseEntity;

public interface BaseRequestDto<E extends BaseEntity> {
    E toEntity();
}
