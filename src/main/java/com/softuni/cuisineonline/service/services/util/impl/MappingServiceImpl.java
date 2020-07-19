package com.softuni.cuisineonline.service.services.util.impl;

import com.softuni.cuisineonline.service.services.util.MappingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Override
    public <D> List<D> mapAll(final Collection<?> source, final Class<D> destinationClass) {
        return source.stream().map(o -> mapper.map(o, destinationClass)).collect(toList());
    }
}
