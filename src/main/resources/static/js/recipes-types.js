const RECIPE_TYPE_URL = '/api/recipes/types';

fetch(RECIPE_TYPE_URL)
    .then(resp => resp.json())
    .then(types => {
        let result = '';
        types.forEach(type => {
            result += `<option value="${type}">${type}</option>`
        });

        $('#type-select').html(result);
    });

