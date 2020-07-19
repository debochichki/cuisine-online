package com.softuni.cuisineonline.web.controllers;

import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.models.appliance.ApplianceCreateFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appliances")
public class ApplianceController {

    private final MappingService mappingService;
    private final ApplianceService applianceService;

    public ApplianceController(
            MappingService mappingService, ApplianceService applianceService) {
        this.mappingService = mappingService;
        this.applianceService = applianceService;
    }

    @GetMapping("create")
    private String getCreateForm() {
        return "appliance/create-appliance.html";
    }

    @PostMapping("create")
    private String create(@ModelAttribute ApplianceCreateFormModel createModel) {
        ApplianceCreateServiceModel serviceModel =
                mappingService.map(createModel, ApplianceCreateServiceModel.class);
        applianceService.create(serviceModel);
        return "redirect:/appliances/create";
    }

}
