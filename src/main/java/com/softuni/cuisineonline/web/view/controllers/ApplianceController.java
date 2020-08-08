package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.appliance.ApplianceCreateFormModel;
import com.softuni.cuisineonline.web.view.models.appliance.ApplianceDeleteFormModel;
import com.softuni.cuisineonline.web.view.models.appliance.ApplianceViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/appliances")
public class ApplianceController extends BaseController {

    private final MappingService mappingService;
    private final ApplianceService applianceService;

    public ApplianceController(
            MappingService mappingService,
            ApplianceService applianceService) {
        this.mappingService = mappingService;
        this.applianceService = applianceService;
    }

    @GetMapping("/all")
    public ModelAndView getAllAppliances(ModelAndView modelAndView) {
        modelAndView.setViewName("appliance/all-appliances");
        List<ApplianceViewModel> viewModels =
                mappingService.mapAll(applianceService.getAll(), ApplianceViewModel.class);
        modelAndView.addObject("appliances", viewModels);
        return modelAndView;
    }

    @GetMapping("/create")
    public String getCreateForm() {
        return "appliance/create-appliance.html";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView getDeleteForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("appliance/delete-appliance");
        ApplianceViewModel viewModel =
                mappingService.map(applianceService.getById(id), ApplianceViewModel.class);
        modelAndView.addObject("appliance", viewModel);
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ApplianceCreateFormModel createModel) {
        ApplianceCreateServiceModel serviceModel =
                mappingService.map(createModel, ApplianceCreateServiceModel.class);
        applianceService.create(serviceModel);
        return redirect("/appliances/all");
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute ApplianceDeleteFormModel deleteModel) {
        applianceService.deleteById(deleteModel.getId());
        return redirect("/appliances/all");
    }

}
