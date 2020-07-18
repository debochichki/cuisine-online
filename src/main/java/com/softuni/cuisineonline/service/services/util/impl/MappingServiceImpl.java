package com.softuni.cuisineonline.service.services.util.impl;

import com.softuni.cuisineonline.service.services.util.MappingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MappingServiceImpl implements MappingService {

    private final ModelMapper mapper;

    public MappingServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <D> D map(Object source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }
}
