$(document).ready(() => {
    console.log("I am supplied");
    $("#addProductButton").click((event) => {
        event.preventDefault();
        console.log("I am clicked");
        const formData = new FormData(document.querySelector('#addProductForm'));
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/products",
            data: formData,

            timeout: 4000,
            processData: false,
            contentType: false,
            cache: false,
            success: (data) => {
                window.location.reload();
            },
            error: (error) => {
                console.error(error);
                const response = error.responseJSON;
                const wrongInputAlert = $("#wrongInputAlert");
                if (response.statusCode === 400 || response.statusCode === 500) {
                    wrongInputAlert.css('display', 'block');
                    wrongInputAlert.html(response.message)
                    return;
                } else {
                    wrongInputAlert.css('display', 'none');
                    wrongInputAlert.html('')
                }

                if (response.statusCode === 422) {
                    const validationErrors = response.data;

                    const nameInput = $("#nameInput");
                    const nameFeedback = $("#nameFeedback");

                    const priceInput = $("#priceInput");
                    const priceFeedback = $("#priceFeedback");

                    const fileInput = $("#fileInput");
                    const fileFeedback = $("#fileFeedback");

                    if ('name' in validationErrors) {
                        nameInput.addClass('is-invalid');
                        nameFeedback.html(validationErrors['name']);
                    } else {
                        removeErrors(nameInput, nameFeedback);
                    }
                    if ('price' in validationErrors) {
                        priceInput.addClass('is-invalid');
                        priceFeedback.html(validationErrors['price']);
                    } else {
                        removeErrors(priceInput, priceFeedback);
                    }

                    if ('file' in validationErrors) {
                        fileInput.addClass('is-invalid');
                        fileFeedback.html(validationErrors['file']);
                    } else {
                        removeErrors(fileInput, fileFeedback);
                    }
                }
            }
        })
    });
})

removeErrors = (input, feedback) => {
    input.removeClass('is-invalid');
    feedback.html('');
}