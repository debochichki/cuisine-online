package com.softuni.cuisineonline.web.api.controllers;

import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.api.controllers.base.BaseApiController;
import com.softuni.cuisineonline.web.api.models.appliance.ApplianceResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApplianceApiController extends BaseApiController {

    private final ApplianceService applianceService;
    private final MappingService mappingService;

    public ApplianceApiController(
            ApplianceService applianceService,
            MappingService mappingService) {
        this.applianceService = applianceService;
        this.mappingService = mappingService;
    }

    @GetMapping("/appliances/all")
    public ResponseEntity<List<ApplianceResponseModel>> getAllAppliances() {
        final List<ApplianceResponseModel> responseModels =
                mappingService.mapAll(applianceService.getAll(), ApplianceResponseModel.class);
        return new ResponseEntity<>(responseModels, HttpStatus.OK);
    }
}
