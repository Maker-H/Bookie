package web.bookie.dto.request;

import web.bookie.domain.BaseEntity;

public interface BaseRequestDTO<E extends BaseEntity> {
    E toEntity();
}
