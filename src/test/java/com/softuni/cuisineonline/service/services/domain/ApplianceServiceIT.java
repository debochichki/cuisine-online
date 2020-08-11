package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Appliance;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.data.repositories.RecipeRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.util.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_UPPER_BOUND;
import static org.mockito.Mockito.verify;

class ApplianceServiceIT extends TestServiceBase {

    private static final String APPLIANCE_NAME = "Mixer";
    private static final String INVALID_SHORT_APPLIANCE_NAME = "M";
    private static final String INVALID_LONG_APPLIANCE_NAME =
            TestUtils.getRandomString(TITLE_LENGTH_UPPER_BOUND + 1);
    private static final String IMAGE_ID = "imageId";

    @Autowired
    ApplianceService service;

    @Autowired
    MappingService mappingService;

    @MockBean
    CloudinaryService cloudinaryService;

    @MockBean
    ApplianceRepository applianceRepository;

    @MockBean
    RecipeRepository recipeRepository;

    private final ApplianceCreateServiceModel createServiceModel = new ApplianceCreateServiceModel();
    private final Image uploadedImage = new Image();
    private final List<Appliance> dbAppliances = new ArrayList<>();


    @Override
    protected void beforeEach() {
        super.beforeEach();
        createServiceModel.setName(APPLIANCE_NAME);
        MockMultipartFile mockFile =
                new MockMultipartFile("data",
                        "filename.txt",
                        "text/plain",
                        "some text".getBytes());
        createServiceModel.setImage(mockFile);
        uploadedImage.setId(IMAGE_ID);
        Mockito.when(cloudinaryService.uploadImage(mockFile))
                .thenReturn(uploadedImage);

        Image image1 = new Image();
        image1.setUrl("firstUrl");
        Image image2 = new Image();
        image2.setUrl("secondUrl");
        dbAppliances.addAll(List.of(new Appliance("model1", image1, Collections.emptyList()),
                new Appliance("model2", image2, Collections.emptyList())));
    }

    @Test
    void create_WhenValidCreateModel_ShouldSetCorrectValues() {
        service.create(createServiceModel);

        ArgumentCaptor<Appliance> argument = ArgumentCaptor.forClass(Appliance.class);
        verify(applianceRepository).save(argument.capture());
        final Appliance createdAppliance = argument.getValue();

        Assert.assertEquals(APPLIANCE_NAME, createdAppliance.getName());
        Assert.assertEquals(uploadedImage.getId(), createdAppliance.getImage().getId());
    }

    @Test
    void create_WhenCreateModelWithNullNameField_ShouldThrow() {
        createServiceModel.setName(null);

        Assert.assertThrows(ValidationException.class, () ->
                service.create(createServiceModel));
    }

    @Test
    void create_WhenCreateModelWithNullMultipartFile_ShouldThrow() {
        createServiceModel.setImage(null);

        Assert.assertThrows(ValidationException.class, () ->
                service.create(createServiceModel));
    }

    @Test
    void create_WhenCreateModelWithInvalidNameLength_ShouldThrow() {
        createServiceModel.setName(INVALID_SHORT_APPLIANCE_NAME);

        Assert.assertThrows(ValidationException.class, () ->
                service.create(createServiceModel));

        createServiceModel.setName(INVALID_LONG_APPLIANCE_NAME);

        Assert.assertThrows(ValidationException.class, () ->
                service.create(createServiceModel));
    }

    @Test
    void getAll_ShouldReturnTheCorrectServiceModels() {
        Mockito.when(applianceRepository.findAll())
                .thenReturn(dbAppliances);

        List<ApplianceServiceModel> expected = mappingService.mapAll(dbAppliances, ApplianceServiceModel.class);
        List<ApplianceServiceModel> actual = service.getAll();

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getName(), actual.get(i).getName());
            Assert.assertEquals(expected.get(i).getImageUrl(), actual.get(i).getImageUrl());
        }
    }

    @Test
    void getById_ShouldReturnTheCorrectServiceModel() {
        Appliance appliance = dbAppliances.get(0);
        String id = appliance.getId();
        Mockito.when(applianceRepository.findById(id))
                .thenReturn(Optional.of(appliance));

        ApplianceServiceModel serviceModel = service.getById(id);
        ApplianceServiceModel expectedModel = mappingService.map(appliance, ApplianceServiceModel.class);

        Assert.assertNotNull(serviceModel);
        Assert.assertEquals(expectedModel.getName(), serviceModel.getName());
        Assert.assertEquals(expectedModel.getImageUrl(), serviceModel.getImageUrl());
    }

    @Test
    void deleteById_ShouldRemoveTheCorrectAppliance() {
        Appliance appliance = dbAppliances.get(1);
        Mockito.when(applianceRepository.findById(appliance.getId()))
                .thenReturn(Optional.of(appliance));

        service.deleteById(appliance.getId());

        verify(cloudinaryService).deleteImage(appliance.getImage().getPublicId());
        verify(applianceRepository).delete(appliance);
    }
}