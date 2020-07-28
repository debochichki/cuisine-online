const APPLIANCE_URL = '/api/appliances/all';

const toString = ({id, name}) => {
    let checkbox =
        `
        <input type="checkbox" id="${id}" name="applianceIds" value="${id}">
        <label for="${id}">${name}</label>
        `
    return checkbox;
};

fetch(APPLIANCE_URL)
    .then(resp => resp.json())
    .then(appliances => {
        let result = '';
        appliances.forEach(appliance => {
            result += toString(appliance);
        });

        $('#appliances').html(result);
    });