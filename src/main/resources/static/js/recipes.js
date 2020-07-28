const URLS = {
    recipesBaseUrl: '/api/recipes',
    filterOptions: '/api/recipes/filter'
};

const toString = ({id, typeIconUrl, title, uploaderUsername, canModify}) => {
    let columns = '';
    columns +=
        `
        <td><img src=${typeIconUrl} alt="Recipe type img" width="50" height="50"/></td>
        <td class="text-white h5">${title}</td>
        <td class="text-white h5">${uploaderUsername}</td>
    `

    columns += !canModify
        ? '<td></td><td></td>'
        : `
        <td>
            <form action="/recipes/delete/${id}" method="post">
                <button class="btn btn-danger">Delete</button>
            </form>
        </td>
        <td>
            <form action="/recipes/edit/${id}" method="post">
                <button class="btn btn-warning">Edit</button>
            </form>
        </td>
    `

    columns +=
        `
        <td>
            <form action="/recipes/view/${id}" method="post">
                <button class="btn btn-success">View</button>
            </form>
        </td> 
    `

    return `<tr>${columns}</tr>`;
};

fetch(URLS.filterOptions)
    .then(resp => resp.json())
    .then(recipes => {
        let result = '';
        recipes.forEach(filterOption => {
            result += `<option value="${filterOption}">${filterOption}</option>`;
        });

        $('#recipes-filter-options').html(result);
    });

fetch(URLS.recipesBaseUrl + "?filterOption=ALL")
    .then(resp => resp.json())
    .then(recipes => {
        let result = '';
        recipes.forEach(recipe => {
            result += toString(recipe);
        });

        $('#recipes-table').html(result);
    });

$(document).ready(function () {
    $('#recipes-filter-options').on('change', function () {
        var option = $(this).val();
        var fullUrl = URLS.recipesBaseUrl + "?filterOption=" + option;
        fetch(fullUrl)
            .then(resp => resp.json())
            .then(recipes => {
                let result = '';
                recipes.forEach(recipe => {
                    result += toString(recipe);
                });

                $('#recipes-table').html(result);
            })
    })
});
