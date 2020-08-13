package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.data.repositories.IngredientRepository;
import com.softuni.cuisineonline.data.repositories.RecipeRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.recipe.RecipeBaseServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.util.Constants;
import com.softuni.cuisineonline.util.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static com.softuni.cuisineonline.util.TestUtils.*;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

class RecipeServiceIT extends TestServiceBase {

    private static final String ALL_FILTER_OPTION = "ALL";

    @Autowired
    RecipeService service;

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    AuthenticatedUserFacade authenticationFacade;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ApplianceRepository applianceRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    @MockBean
    IngredientRepository ingredientRepository;

    private List<User> users = TestUtils.getUsers();
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void beforeEach() {
        super.beforeEach();
        recipes.addAll(users.stream()
                .map(User::getProfile)
                .map(Profile::getRecipes)
                .flatMap(List::stream)
                .collect(toList()));
    }

    @Test
    void getById_WhenValidId_ShouldReturnTheCorrectModel() {
        User user = getRandomListValue(users);
        Recipe recipe = getRandomListValue(user.getProfile().getRecipes());
        Mockito.when(recipeRepository.findById(eq(recipe.getId())))
                .thenReturn(Optional.of(recipe));

        RecipeServiceModel actualRecipe = service.getById(recipe.getId());

        assertEqualsRecipes(recipe, actualRecipe);
    }

    @Test
    void getAllByFilterOption_WhenALLFilterOption_ShouldReturnAllRecipes() {
        Mockito.when(recipeRepository.findAll())
                .thenReturn(recipes);
        Mockito.when(authenticationFacade.getPrincipalName())
                .thenReturn(TestUtils.getRandomListValue(users).getUsername());
        Mockito.when(userService.canModify(anyString(), anyString()))
                .thenReturn(Boolean.TRUE);

        List<RecipeBaseServiceModel> actualRecipes =
                service.getAllByFilterOption(ALL_FILTER_OPTION);

        Assert.assertEquals(recipes.size(), actualRecipes.size());
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            RecipeBaseServiceModel actualRecipe = actualRecipes.get(i);
            assertEqualsRecipes(recipe, actualRecipe);
        }
    }

    @Test
    void getAllByFilterOption_WhenRecipeTypeFilterOption_ShouldReturnAllRecipesOfThatType() {
        Mockito.when(authenticationFacade.getPrincipalName())
                .thenReturn(TestUtils.getRandomListValue(users).getUsername());
        Mockito.when(userService.canModify(anyString(), anyString()))
                .thenReturn(Boolean.TRUE);
        Recipe.Type type = getRandomListValue(recipes).getType();
        List<Recipe> expectedRecipes = recipes.stream().filter(r -> r.getType().equals(type))
                .sorted(Comparator.comparing(Recipe::getTitle))
                .collect(toList());


        Mockito.when(recipeRepository.findAllByTypeOrderByTitleAsc(type))
                .thenReturn(expectedRecipes);

        String filterOption = type.toString();
        List<RecipeBaseServiceModel> actualRecipes = service.getAllByFilterOption(filterOption);

        Assert.assertEquals(expectedRecipes.size(), actualRecipes.size());
        for (int i = 0; i < expectedRecipes.size(); i++) {
            Recipe expectedRecipe = expectedRecipes.get(i);
            RecipeBaseServiceModel actualRecipe = actualRecipes.get(i);
            assertEqualsRecipes(expectedRecipe, actualRecipe);
        }
    }

    @Test
    void getRecipeTypesAsStringValues_ShouldReturnAllRecipeTypes() {
        List<String> expectedTypes = Arrays.stream(Recipe.Type.values())
                .map(Recipe.Type::toString).collect(toList());
        List<String> actualTypes = service.getRecipeTypesAsStringValues();

        Assert.assertTrue(actualTypes.containsAll(expectedTypes));
    }

    @Test
    void getRecipeFilterOptions_ShouldIncludeRecipeTypesAndALLFilterOption() {
        List<String> expectedOptions = getFilterOptions();

        List<String> actualOptions = service.getRecipeFilterOptions();

        Assert.assertTrue(actualOptions.containsAll(expectedOptions));
    }

    @Test
    void upload_WhenValidUploadMode_ShouldSetCorrectValues() {
        Recipe recipe = getRandomListValue(recipes);
        RecipeUploadServiceModel uploadModel = buildUploadModel(recipe);
        Mockito.when(applianceRepository.findAllByIdIn(any()))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Optional.of(recipe.getUploader().getUser()));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        Mockito.when(cloudinaryService.uploadImage(any()))
                .thenReturn(new Image());

        service.upload(uploadModel);

        ArgumentCaptor<Recipe> argument = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(argument.capture());
        Recipe uploadedRecipe = argument.getValue();

        Assert.assertEquals(uploadModel.getId(), uploadedRecipe.getId());
        Assert.assertEquals(uploadModel.getTitle(), uploadedRecipe.getTitle());
        Assert.assertEquals(uploadModel.getDescription(), uploadedRecipe.getDescription());
        Assert.assertEquals(uploadModel.getType(), uploadedRecipe.getType());
        Assert.assertEquals(uploadModel.getUploaderUsername(), uploadedRecipe.getUploader().getUser().getUsername());
        Assert.assertEquals(uploadModel.getDuration(), uploadedRecipe.getDuration());
        Assert.assertEquals(uploadModel.getPortions(), uploadedRecipe.getPortions());
    }

    @Test
    void upload_WhenObligatoryFieldIsNull_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setDescription(null);

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenImageIsNull_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setImage(null);

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenRecipeModelWithInvalidTitleLength_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setTitle(getRandomString(Constants.TITLE_LENGTH_LOWER_BOUND - 1));

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));

        uploadModel.setTitle(getRandomString(Constants.TITLE_LENGTH_UPPER_BOUND + 1));

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenRecipeModelWithInvalidDescriptionLength_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setDescription(getRandomString(Constants.RECIPE_DESCRIPTION_LENGTH_LOWER_BOUND - 1));

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));

        uploadModel.setTitle(getRandomString(Constants.RECIPE_DESCRIPTION_LENGTH_UPPER_BOUND + 1));

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenInvalidDurationValue_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setDuration(-1);

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenInvalidPortionsValue_ShouldThrow() {
        RecipeUploadServiceModel uploadModel = buildUploadModel(getRandomListValue(recipes));
        Mockito.when(userRepository.existsByUsername(eq(uploadModel.getUploaderUsername())))
                .thenReturn(Boolean.TRUE);
        uploadModel.setPortions((byte) -1);

        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void edit_WhenValidEditModel_ShouldSetCorrectValues() {
        Recipe recipe = getRandomListValue(recipes);
        Mockito.when(recipeRepository.findById(eq(recipe.getId())))
                .thenReturn(Optional.of(recipe));
        Mockito.when(applianceRepository.findAllByIdIn(any()))
                .thenReturn(new ArrayList<>());

        String id = recipe.getId();
        String title = getRandomString(15);
        Recipe.Type type = getRandomListValue(Arrays.stream(Recipe.Type.values()).collect(toList()));
        String description = getRandomString(50);
        int duration = 20;
        byte portions = (byte) 2;

        RecipeEditServiceModel editModel = new RecipeEditServiceModel();
        editModel.setId(id);
        editModel.setTitle(title);
        editModel.setType(type);
        editModel.setDescription(description);
        editModel.setDuration(duration);
        editModel.setPortions(portions);
        editModel.setIngredientsData(buildIngredientsString());

        service.edit(editModel);

        ArgumentCaptor<Recipe> argument = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(argument.capture());
        Recipe editedRecipe = argument.getValue();

        Assert.assertEquals(editModel.getId(), editedRecipe.getId());
        Assert.assertEquals(editModel.getTitle(), editedRecipe.getTitle());
        Assert.assertEquals(editModel.getDescription(), editedRecipe.getDescription());
        Assert.assertEquals(editModel.getType(), editedRecipe.getType());
        Assert.assertEquals(editModel.getDuration(), editedRecipe.getDuration());
        Assert.assertEquals(editModel.getPortions(), editedRecipe.getPortions());
    }

    @Test
    void deleteById_ShouldRemoveTheCorrectRecipe() {
        Recipe recipe = getRandomListValue(recipes);
        Mockito.when(recipeRepository.findById(eq(recipe.getId())))
                .thenReturn(Optional.of(recipe));

        service.deleteById(recipe.getId());

        verify(recipeRepository).deleteById(recipe.getId());
    }

    @Test
    void getRandomRecipes_WhenRandomRecipesCountIsGreaterThanAllRecipesCount_ShouldReturnAllRecipes() {
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);

        List<RecipeServiceModel> randomRecipes = service.getRandomRecipes(recipes.size() + 1);

        Assert.assertEquals(recipes.size(), randomRecipes.size());
        assertContainsRecipes(randomRecipes);
    }

    @Test
    void getRandomRecipes_WhenRandomRecipesCountIsLesserThanAllRecipesCount_ShouldReturnCorrectCountOfDistinctRecipes() {
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        int randomRecipesCount = recipes.size() - 1;
        List<RecipeServiceModel> randomRecipes = service.getRandomRecipes(randomRecipesCount);

        Assert.assertEquals(randomRecipesCount, randomRecipes.size());
        assertContainsRecipes(randomRecipes);
    }

    private List<String> getFilterOptions() {
        List<String> expectedOptions = Arrays.stream(Recipe.Type.values())
                .map(Recipe.Type::toString).collect(toList());
        expectedOptions.add(ALL_FILTER_OPTION);
        return expectedOptions;
    }

    private void assertEqualsRecipes(Recipe recipe, RecipeServiceModel actualRecipe) {
        Assert.assertEquals(recipe.getId(), actualRecipe.getId());
        Assert.assertEquals(recipe.getUploader().getUser().getUsername(), actualRecipe.getUploaderUsername());
        Assert.assertEquals(recipe.getTitle(), actualRecipe.getTitle());
        Assert.assertEquals(recipe.getDescription(), actualRecipe.getDescription());
        Assert.assertEquals(recipe.getType().toString(), actualRecipe.getType());
    }

    private void assertEqualsRecipes(Recipe recipe, RecipeBaseServiceModel actualRecipe) {
        Assert.assertEquals(recipe.getId(), actualRecipe.getId());
        Assert.assertEquals(recipe.getTitle(), actualRecipe.getTitle());
        Assert.assertEquals(
                recipe.getUploader().getUser().getUsername(),
                actualRecipe.getUploaderUsername());
    }

    private void assertContainsRecipes(List<RecipeServiceModel> randomRecipes) {
        Assert.assertTrue(
                recipes.stream().map(Recipe::getId).collect(toList())
                        .containsAll(
                                randomRecipes
                                        .stream()
                                        .map(RecipeServiceModel::getId)
                                        .collect(toList())));
    }

    private RecipeUploadServiceModel buildUploadModel(Recipe recipe) {
        RecipeUploadServiceModel uploadModel = new RecipeUploadServiceModel();
        uploadModel.setId(recipe.getId());
        uploadModel.setTitle(recipe.getTitle());
        uploadModel.setUploaderUsername(recipe.getUploader().getUser().getUsername());
        uploadModel.setType(recipe.getType());
        uploadModel.setDescription(recipe.getDescription());
        uploadModel.setDuration(30);
        uploadModel.setPortions((byte) 1);
        uploadModel.setIngredientsData(buildIngredientsString());
        MockMultipartFile mockFile =
                new MockMultipartFile("data",
                        "filename.txt",
                        "text/plain",
                        "some text".getBytes());
        uploadModel.setImage(mockFile);
        return uploadModel;
    }

    private String buildIngredientsString() {
        StringBuilder sb = new StringBuilder();
        for (final String[] row : VALID_INGREDIENTS_DATA) {
            sb.append(String.join(", ", row));
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}