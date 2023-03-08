$(document).ready(() => {
    getProducts();
    getCount();
})

function initBucket() {
    $.ajax({
        type: "GET",
        enctype: 'multipart/form-data',
        url: "/bucket/bucket",

        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            const data = response.data;
            console.log(data);
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            console.log(response);
        }
    })
}

function getProducts() {
    $.ajax({
        type: "GET",
        enctype: 'multipart/form-data',
        url: "/bucket/products",

        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            const data = response.data;
            let i = 1;
            for (product of data.products) {
                console.log(product);
                $('#bucketBody').append(getProductRow(product, product.quantity, i));
                i++;
            }
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            console.log(response);
        }
    })
}


function getProductRow(product, quantity, index) {
    return $('<tr>')
        .css('text-align', 'center')
        .append($('<td>')
            .css('width', '5%')
            .html(index ? index : '#')
        )
        .append($('<td>')
            .css('width', '15%')
            .html(product.name)
        )
        .append($('<td>')
            .css('width', '5%')
            .html(product.price)
        )
        .append($('<td>')
            .css('width', '15%')
            .append($('<img alt="NO IMAGE">')
                .css('max-width', '8rem')
                .css('max-height', '8rem')
                .attr('src', getImagePath(product))
                .addClass('productImage')
            )
        )
        .append($('<td>')
            .css('width', '10%')
            .html(product.category)
        )
        .append($('<td>')
            .css('width', '10%')
            .html(quantity)
        )
        .append($('<td>')
            .css('width', '10%')
            .html(product.price * quantity)
        )
}

function getCount() {
    $.ajax({
        type: "GET",
        enctype: 'multipart/form-data',
        url: "/bucket/count",

        timeout: 4000,
        processData: false,
        contentType: false,
        cache: false,
        success: (response) => {
            const data = response.data;
            console.log(response);
        },
        error: (error) => {
            console.error(error);
            const response = error.responseJSON;
            console.log(response);
        }
    })
}

function getImagePath(product) {
    if (product.imagePath !== null) {
        return "/img/" + product.imagePath
    } else return "/static/no_image.jpg"
}