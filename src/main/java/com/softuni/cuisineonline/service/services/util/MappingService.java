package com.softuni.cuisineonline.service.services.util;

public interface MappingService {

    <D> D map(Object source, Class<D> destinationClass);
}
