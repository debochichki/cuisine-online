package com.softuni.cuisineonline.service.services.util;

import java.util.Collection;
import java.util.List;

public interface MappingService {

    <D> D map(Object source, Class<D> destinationClass);

    <D> List<D> mapAll(Collection<?> source, Class<D> destinationClass);
}
