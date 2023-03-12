$(document).ready(() => {
    initProducts();
    let timeoutId = null;
    $("#addProductButton").click((event) => {
        event.preventDefault();
        appProduct();
    });

    $("#searchButton").click((event) => {
        event.preventDefault();
        filterProducts();
    });

    $("#namePattern").on('input', (event) => {
        console.log('i changed');
        clearTimeout(timeoutId);
        timeoutId = setTimeout(filterProducts, 400);
    })
})

function initProducts() {
    const alertBlock = $('#productsListError');
    $.ajax({
        type: "GET",
        enctype: 'multipart/form-data',
        url: "/web-api/products",

        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            hideElement(alertBlock);
            const data = response.data;
            const productComponent = $('#productComponent');
            if ('products' in data) {
                for (const product of data.products) {
                    productComponent.append(createCard(product));
                }
                return data.products;
            }
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            alertBlock.css('display', 'block');
            alertBlock.html(response.message)
            return {};
        }
    })
}

function removeErrors(input, feedback) {
    input.removeClass('is-invalid');
    feedback.html('');
}

function appProduct() {
    console.log("add product clicked");
    const formData = new FormData(document.querySelector('#addProductForm'));
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/web-api/products",
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
            const nameInput = $("#nameInput");
            const nameFeedback = $("#nameFeedback");

            const priceInput = $("#priceInput");
            const priceFeedback = $("#priceFeedback");

            const fileInput = $("#fileInput");
            const fileFeedback = $("#fileFeedback");

            if (response.statusCode === 400 || response.statusCode === 500) {
                wrongInputAlert.css('display', 'block');
                wrongInputAlert.html(response.message)
                removeErrors(nameInput, nameFeedback);
                removeErrors(priceInput, priceFeedback);
                removeErrors(fileInput, fileFeedback);
                return;
            } else {
                hideElement(wrongInputAlert);
            }

            if (response.statusCode === 422) {
                const validationErrors = response.data;
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
}

function addProductToBucket(sender) {
    const alertBlock = $('#productsListError');
    const infoBlock = $('#productsListInfo');
    const data = {"productUuid" : $(sender).attr('productuuid')};
    console.log(data);
    console.log(jQuery.param(data));
    $.ajax({
        type: "PUT",
        enctype: 'multipart/form-data',
        url: `/web-api/bucket/add?${jQuery.param(data)}`,


        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            infoBlock.css('display', 'block');
            infoBlock.html(response.message);
            hideElementTimeOut(infoBlock, 2000);
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            alertBlock.css('display', 'block');
            alertBlock.html(response.message)
            return {};
        }
    })
}

function createCard(product) {
    return $('<div>')
        .attr('id', product.uuid)
        .addClass('coll product-coll')
        .append($('<div>')
            .addClass('card border-dark mb-3 product-card')
            .css('max-width', '18rem')
            .css('max-height', '72rem')
            .append($('<img alt="NO IMAGE">')
                .attr('src', getImagePath(product))
                .addClass('productImage')
            )
            .append($('<div>')
                .addClass('card-body')
                .append($('<ul>')
                    .addClass('list-group list-group-flush')
                    .append($('<li>')
                        .addClass('list-group-item')
                        .html(`Product name: ${product.name}`)
                    )
                    .append($('<li>')
                        .addClass('list-group-item')
                        .html(`Product price: ${product.price}`)
                    )
                    .append($('<li>')
                        .addClass('list-group-item')
                        .html(`Product rating: ${product.rating}`)
                    )
                )
                .append($('<div>')
                    .addClass('d-grid gap-2')
                    .append($('<button>')
                        .addClass('btn btn-outline-dark')
                        .attr('onclick', 'addProductToBucket(this)')
                        .attr('productuuid', product.uuid)
                        .html('Add to the bucket')
                    )
                    .append($('<button>')
                        .addClass('btn btn-outline-dark')
                        .attr('href', '#')
                        .html('More information')
                    )
                )
            )
        )
}

function hideElement(alertBlock) {
    alertBlock.css('display', 'none');
    alertBlock.html('')
}
function hideElementTimeOut(element, timeOut) {
    setTimeout(() => hideElement(element), timeOut);
}

function getImagePath(product) {
    if (product.imagePath !== null) {
        return "/img/" + product.imagePath
    } else return "/static/no_image.jpg"
}

function filterProducts() {
    const alertBlock = $('#productsListError');
    $.ajax({
        type: "GET",
        enctype: 'multipart/form-data',
        url: "/web-api/products",
        data: $('#productFilter').serialize(),

        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            console.log('filtered');
            clearProducts();
            hideElement(alertBlock);

            const data = response.data;
            const productComponent = $('#productComponent');
            if ('products' in data) {
                for (const product of data.products) {
                    productComponent.append(createCard(product));
                }
            }
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            alertBlock.css('display', 'block');
            alertBlock.html(response.message);
        }
    })
}

function clearProducts() {
    $("div").remove(".product-coll");
    console.log('Products cleared');
}