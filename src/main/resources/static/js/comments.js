const URLS = {
    comments: '/api/comments/all',
};

const toString = ({id, content, instant, uploaderUsername, canModify}) => {
    let result = '<textarea name="comment" class="text-area-comment area-bg-blur w-75 text-white" rows="7" disabled>';
    result +=
        `
        AUTHOR:  ${uploaderUsername}  /  POSTED:  ${instant}
        
        ${content}
    </textarea>
        `

    if (canModify) {
        result +=
            `
            <div class="d-flex justify-content-center">
                <a href="/comments/edit/${id}" class="btn btn-warning">Edit</a>
                <form  action="/comments/delete/${id}" method="post">
                    <button class="btn btn-danger" type="submit">Delete</a>
                </form>
            </div>
            `
    }

    result += '<hr class="hr-3"/>'
    return result;
};

fetch(URLS.comments)
    .then(resp => resp.json())
    .then(comments => {
        let result = '';
        comments.forEach(comment => {
            result += toString(comment);
        });

        $('#comments-section').html(result);
    });
