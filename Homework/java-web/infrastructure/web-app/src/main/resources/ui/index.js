const PAGES = {
    PRODUCTS: 'PRODUCTS',
    PRODUCT: 'PRODUCT',
    BUCKET: 'BUCKET',
    REGISTRATION: 'REGISTRATION',
    LOGIN: 'LOGIN'
}
const JWT = 'jwt';

function getHeaders() {
    const jwt = localStorage.getItem(JWT);
    if (jwt) {
        return {
            Authorization: `Bearer ${jwt}`
        }
    }
    return {};
}

function isEmpty(obj) {
    return Object.keys(obj).length === 0;
}

const hidePortals = () => {
    const portalHolder = document.querySelector('#portal-holder');

    while (portalHolder.firstChild) {
        portalHolder.removeChild(portalHolder.lastChild);
    }
}

const getOverlay = (children, closeOnClick) => {
    const overlay = document.createElement('div');
    const overlayBackground = document.createElement('div');
    addClassesToElement(overlayBackground, 'position-fixed top-0 start-0 w-100 h-100 bg-dark');
    overlayBackground.style.opacity = '0.5';
    overlayBackground.style.zIndex = '999';
    const overlayData = document.createElement('div');
    overlayData.setAttribute('id', 'overlay')
    addClassesToElement(overlayData, 'position-fixed top-50 start-50 translate-middle w-50 p-3 rounded-3 bg-light shadow-lg');
    overlayData.style.zIndex = '1000';
    overlayData.append(children);
    overlay.append(overlayBackground, overlayData);
    if (closeOnClick) {
        overlay.addEventListener("mouseup", function (e) {
            if (!overlayData.contains(e.target)) {
                hidePortals();
            }
        });
    }
    return overlay;
}

const getErrorMessage = (message) => {
    const error = document.createElement('div');
    const errorTitle = document.createElement('h2');
    addClassesToElement(errorTitle, 'text-danger');
    errorTitle.innerText = 'Error';
    const errorText = document.createElement('p');
    errorText.innerText = message;
    addClassesToElement(errorText, 'text-dark')
    const closeButton = document.createElement('button');
    addClassesToElement(closeButton, 'btn btn-danger');
    closeButton.innerText = 'Close';
    closeButton.onclick = hidePortals;
    error.append(errorTitle, errorText, closeButton);

    return getOverlay(error, true);
}

const getSuccessMessage = (message) => {
    const success = document.createElement('div');
    const successTitle = document.createElement('h2');
    addClassesToElement(success, 'text-success');
    successTitle.innerText = 'Info';
    const successText = document.createElement('p');
    addClassesToElement(successText, 'text-dark')
    successText.innerText = message;
    const closeButton = document.createElement('button');
    addClassesToElement(closeButton, 'btn btn-success');
    closeButton.innerText = 'Close';
    closeButton.onclick = hidePortals;
    success.append(successTitle, successText, closeButton);

    return getOverlay(success, true);
}

const getButton = (caption, onClick) => {
    const button = document.createElement('button');
    button.textContent = caption;
    button.onclick = onClick;

    return button;
};

document.addEventListener('DOMContentLoaded', () => {
    window.addEventListener('popstate', function (event) {
        if (event.state) {
            currentPage = event.state.page;
            buildMainContent();
        }
    });


    let productPageState = {
        'productUuid': ''
    };

    let categoriesState = {
        categories: []
    }

    const MAX_BUCKET_QUANTITY = '128';
    const categoriesUrl = 'http://localhost:8080/web-api/categories';
    const productsUrl = 'http://localhost:8080/web-api/products';
    const reviewsUrl = 'http://localhost:8080/web-api/reviews';
    const bucketsUrl = 'http://localhost:8080/web-api/buckets';
    const ordersUrl = 'http://localhost:8080/web-api/orders';
    const registrationUrl = 'http://localhost:8080/web-api/registration';
    const getNavbar = (onProductClick, onBucketClick) => {
        const navbar = document.createElement('nav');
        addClassesToElement(navbar, 'navbar navbar-expand-lg navbar-light bg-light');
        const navbarHeader = document.createElement('div');
        navbarHeader.classList.add('container-fluid');
        const navbarTitle = document.createElement('a');
        navbarTitle.classList.add('navbar-brand');
        navbarTitle.setAttribute('href', '#');
        navbarTitle.textContent = 'Online Shop';
        const navbarButton = document.createElement('div');
        navbarButton.innerHTML = " <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\"\n            aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n      <span class=\"navbar-toggler-icon\"></span>\n    </button>"
        const navbarBody = document.createElement('div');
        addClassesToElement(navbarBody, 'collapse navbar-collapse');
        navbarBody.setAttribute('id', 'navbarNav');
        const navbarBodyUl = document.createElement('ul');
        addClassesToElement(navbarBodyUl, 'navbar-nav me-auto');
        const navbarBodyLiProduct = document.createElement('li');
        addClassesToElement(navbarBodyLiProduct, 'nav-item');
        const navbarProductLink = document.createElement('a');
        navbarProductLink.classList.add('nav-link');
        navbarProductLink.onclick = onProductClick;
        navbarProductLink.setAttribute('href', '#');
        navbarProductLink.textContent = "Products";
        navbarBodyLiProduct.append(navbarProductLink);
        const navbarBodyLiBucket = document.createElement('li');
        addClassesToElement(navbarBodyLiBucket, 'nav-item');
        const navbarBucketLink = document.createElement('a');
        navbarBucketLink.classList.add('nav-link');
        navbarBucketLink.onclick = onBucketClick;
        navbarBucketLink.setAttribute('href', '#');
        navbarBucketLink.textContent = 'Bucket';
        navbarBodyLiBucket.append(navbarBucketLink);
        const navbarBodyLiOrder = document.createElement('li');
        addClassesToElement(navbarBodyLiOrder, 'nav-item');
        const navbarOrderLink = document.createElement('a');
        navbarOrderLink.classList.add('nav-link');
        navbarOrderLink.setAttribute('href', '#');
        navbarOrderLink.textContent = "Orders";
        navbarBodyLiOrder.append(navbarOrderLink);
        const navbarBodyLiAdmin = document.createElement('li');
        addClassesToElement(navbarBodyLiAdmin, 'nav-item');
        const navbarAdminLink = document.createElement('a');
        navbarAdminLink.classList.add('nav-link');
        navbarAdminLink.setAttribute('href', '#');
        navbarAdminLink.textContent = "Admin";
        navbarBodyLiAdmin.append(navbarAdminLink);
        navbarBodyUl.append(navbarBodyLiProduct, navbarBodyLiBucket, navbarBodyLiOrder, navbarBodyLiAdmin);
        navbarBody.append(navbarBodyUl);
        navbarHeader.append(navbarTitle, navbarButton, navbarBody);
        navbar.append(navbarHeader);
        return navbar;
    }

    let currentPage = PAGES.PRODUCTS;

    const rootNode = document.querySelector("#root");
    const portalHolder = document.querySelector('#portal-holder');

    const navbar = getNavbar(
        () => {
            console.log(PAGES.PRODUCTS);
            currentPage = PAGES.PRODUCTS;
            buildMainContent();
        },
        () => {
            console.log(PAGES.BUCKET);
            currentPage = PAGES.BUCKET;
            buildMainContent();
        }
    );

    const mainContent = document.createElement('div');
    addClassesToElement(mainContent, 'container mt-3');

    const buildMainContent = () => {
        while (mainContent.firstChild) {
            mainContent.removeChild(mainContent.lastChild);
        }


        if (localStorage.getItem(JWT)) {
            switch (currentPage) {
                case PAGES.PRODUCTS:
                    loadProductsPage();
                    break;

                case PAGES.BUCKET:
                    loadBucketPage();
                    break;

                case PAGES.PRODUCT:
                    if (productPageState.productUuid) {
                        loadProductPage(productPageState.productUuid);
                    } else {
                        loadProductsPage();
                    }
                    break;
            }
        } else {
            switch (currentPage) {
                case PAGES.REGISTRATION:
                    loadLoginPage();
                    break;
                case PAGES.LOGIN:
                    loadLoginPage();
                    break;
                default:
                    loadLoginPage();
                    break;
            }

        }
    }
    const loadProductsPage = () => {
        history.pushState({page: PAGES.PRODUCTS}, null, '');
        let productsState = {
            products: [],
            pageNumber: 1,
            pageSize: 6,
            namePattern: '',
            categoryUuid: '',
            orderBy: 'name',
            orderSwitch: true,
            totalPageNumber: 0,
            firstPage: true,
            lastPage: false
        }

        const contentRow = document.createElement('div');
        addClassesToElement(contentRow, 'row');
        const productsCol = document.createElement('div');
        addClassesToElement(productsCol, 'col-lg-9');
        productsCol.setAttribute('id', 'productsColl')
        const loadCategories = async () => {
            try {
                const fetchedCategories = [];
                const categoriesResponse = await fetch(categoriesUrl, {
                    headers: getHeaders()
                });
                const data = (await categoriesResponse.json()).data;
                for (const uuid of data.uuids) {
                    const category = await loadCategoryById(uuid)
                    fetchedCategories.push(category);
                }
                categoriesState = {...categoriesState, categories: fetchedCategories};
                return fetchedCategories;
            } catch (error) {
                console.error(error);
            }
        }
        const loadCategoryById = async (uuid) => {
            if (!uuid) {
                return null;
            }
            try {
                const categoryResponse = await fetch(`${categoriesUrl}/${uuid}`, {
                    headers: getHeaders()
                });
                return (await categoryResponse.json()).data.category;
            } catch (e) {
                console.error(e);
            }
        }
        const loadProductRatingById = async (uuid) => {
            if (!uuid) {
                return null;
            }
            try {
                const reviewResponse = await fetch(`${reviewsUrl}/products/${uuid}/rating`, {
                    headers: getHeaders()
                });
                return (await reviewResponse.json()).data.rating;
            } catch (e) {
                console.error(e);
            }
        }
        const loadProducts = async () => {
            try {
                const params = getProductsRequestParameters();
                const paramsString = new URLSearchParams(params);
                console.log(`request : ${productsUrl}?${paramsString}`);
                const productsResponse = await fetch(`${productsUrl}?${paramsString}`, {
                    headers: getHeaders()
                });
                const page = (await productsResponse.json()).data.page;
                productsState = {
                    ...productsState,
                    totalPageNumber: page.totalPages, products: page.content,
                    firstPage: page.first, lastPage: page.last
                }
                console.log(productsState);
                return page.content;
            } catch (error) {
                console.error(error);
            }
        }

        loadCategories().then()
            .then(() => contentRow.append(getSearchCol()))
            .then(() => loadProducts())
            .then(() => contentRow.append(productsCol))
            .then(() => drawProducts(
                (product, quantity) => addToBucket(product, quantity),
                (product) => moreInfo(product)
            ))
            .then(() => {
                if (productsState.products.length !== 0) {
                    drawPagination()
                }
            })
            .then(() => console.log(productsState));

        mainContent.append(contentRow);

        const getSearchCol = () => {
            let searchInputTimeout;
            const col = document.createElement('div');
            addClassesToElement(col, 'col-lg-3');
            col.setAttribute('id', 'searchColl')
            const inputGroup = document.createElement('div');
            addClassesToElement(inputGroup, 'input-group mb-3');
            const patterInput = document.createElement('input');
            addClassesToElement(patterInput, 'form-control');
            patterInput.setAttribute('id', 'searchInput');
            patterInput.setAttribute('type', 'text');
            patterInput.setAttribute('placeholder', 'Search products...');
            patterInput.oninput = e => {
                productsState = {...productsState, namePattern: e.currentTarget.value, pageNumber: 1}
                if (searchInputTimeout) {
                    clearTimeout(searchInputTimeout);
                }
                searchInputTimeout = setTimeout(
                    () => loadAndRedrawProducts(), 400);
            };
            patterInput.value = productsState.namePattern;
            const buttonSearch = document.createElement('button');
            addClassesToElement(buttonSearch, 'btn btn-outline-secondary');
            buttonSearch.setAttribute('type', 'button');
            buttonSearch.onclick = e => {
                productsState = {...productsState, pageNumber: 1};
                loadAndRedrawProducts();
            }
            buttonSearch.innerText = 'Search';

            const selectCategory = document.createElement('select');
            addClassesToElement(selectCategory, 'form-select custom-select border-dark mb-3');
            selectCategory.setAttribute('id', 'sortParameter');
            selectCategory.setAttribute('name', 'parameter');
            selectCategory.onchange = e => {
                productsState = {...productsState, categoryUuid: e.currentTarget.value, pageNumber: 1};
                loadAndRedrawProducts();
            };
            const optionAll = document.createElement('option');
            optionAll.setAttribute('value', '');
            optionAll.innerText = 'All';
            selectCategory.append(optionAll);
            categoriesState.categories.forEach(category => {
                const categoryOption = document.createElement('option');
                categoryOption.setAttribute('value', category.uuid);
                categoryOption.innerText = category.name;
                selectCategory.append(categoryOption);
            })
            selectCategory.value = productsState.categoryUuid;

            const selectOrderBy = document.createElement('select');
            addClassesToElement(selectOrderBy, 'form-select custom-select border-dark mb-3');
            selectOrderBy.setAttribute('id', 'selectOrderBy');
            selectOrderBy.setAttribute('name', 'parameter');
            selectOrderBy.onchange = e => {
                productsState = {...productsState, orderBy: e.currentTarget.value, pageNumber: 1};
                loadAndRedrawProducts();
            };
            const optionName = document.createElement('option');
            optionName.setAttribute('value', 'name');
            optionName.innerText = 'Name';
            const optionPrice = document.createElement('option');
            optionPrice.setAttribute('value', 'price');
            optionPrice.innerText = 'Price';
            const optionRating = document.createElement('option');
            optionRating.setAttribute('value', 'rating');
            optionRating.innerText = 'Rating';
            selectOrderBy.append(optionName, optionPrice, optionRating);
            selectOrderBy.value = productsState.orderBy;

            const orderCheck = document.createElement('div');
            addClassesToElement(orderCheck, 'form-check form-switch mb-3');
            const orderSwitch = document.createElement('input');
            addClassesToElement(orderSwitch, 'form-check-input');
            orderSwitch.setAttribute('type', 'checkbox');
            orderSwitch.setAttribute('id', 'orderSwitch');
            orderSwitch.onchange = e => {
                productsState = {...productsState, orderSwitch: orderSwitch.checked, pageNumber: 1};
                loadAndRedrawProducts();
            }
            const orderLabel = document.createElement('label');
            addClassesToElement(orderLabel, 'form-check-label');
            orderLabel.setAttribute('for', 'sortSwitch');
            orderLabel.innerText = 'Change order';
            orderSwitch.checked = productsState.orderSwitch;

            orderCheck.append(orderSwitch, orderLabel);
            inputGroup.append(patterInput, buttonSearch);

            col.append(inputGroup, selectCategory, selectOrderBy, orderCheck);
            return col;
        }
        const drawProducts = async (addToBucket, moreInfo) => {
            const productsColl = document.querySelector('#productsColl')
            while (productsColl.firstChild) {
                productsColl.removeChild(productsColl.lastChild);
            }
            let products = productsState.products;
            const row = document.createElement('row');
            addClassesToElement(row, 'row');
            row.setAttribute('id', 'productsRow');
            for (const product of products) {
                let productColl = await getProductCol(product, addToBucket, moreInfo)
                row.append(productColl);
            }
            productsColl.append(row);
        }
        const getProductCol = (product, addToBucket, moreInfo) => {
            let categoryName = 'No category specified';
            let fetchedRating = 0;
            let imageUrl = 'https://via.placeholder.com/150';
            if (product.imageBytes) {
                imageUrl = `data:image/png;base64,${product.imageBytes}`
            }
            const category = categoriesState.categories.find(category => {
                return category.uuid === product.categoryUuid;
            })

            if (category) {
                categoryName = category.name;
            }

            return loadProductRatingById(product.uuid)
                .then(rating => {
                    if (rating) {
                        fetchedRating = rating;
                    }
                })
                .then(() => {
                    const productCol = document.createElement('div');
                    addClassesToElement(productCol, 'col-md-4 mb-3');
                    const card = document.createElement('div');
                    addClassesToElement(card, 'card');
                    const img = document.createElement('img');
                    addClassesToElement(img, 'card-img-top');
                    img.setAttribute('src', imageUrl);
                    img.setAttribute('alt', 'Product Image');
                    const cardBody = document.createElement('div');
                    addClassesToElement(cardBody, 'card-body');
                    const productHeader = document.createElement('div');
                    productHeader.style.display = 'flex';
                    productHeader.style.justifyContent = 'space-between';
                    const productName = document.createElement('h5');
                    addClassesToElement(productName, 'fw-bold card-text');
                    productName.innerText = product.name;
                    const productRating = document.createElement('p');
                    addClassesToElement(productRating, 'card-text');
                    productRating.innerText = fetchedRating.toFixed(1) + '♂';
                    productHeader.append(productName, productRating);
                    addClassesToElement(productHeader, 'mb-2')
                    const productPrice = document.createElement('p');
                    addClassesToElement(productPrice, 'card-text');
                    productPrice.innerText = '₴' + product.price;
                    const productCategory = document.createElement('p');
                    addClassesToElement(productCategory, 'card-text');
                    productCategory.innerText = categoryName;
                    const productAddToBucket = document.createElement('a');
                    addClassesToElement(productAddToBucket, 'btn btn-primary me-1');
                    productAddToBucket.onclick = () => {
                        try {
                            addToBucket.bind(null, product, 1).call()
                            portalHolder.append(getSuccessMessage(`Product ${product.name} successfully added to the bucket`))
                        } catch (e) {
                            portalHolder.append(getErrorMessage(`${e}`))
                        }
                    };
                    productAddToBucket.innerText = 'Add to Bucket';
                    const productMoreInfo = document.createElement('button');
                    addClassesToElement(productMoreInfo, 'btn btn-secondary');
                    productMoreInfo.onclick = moreInfo.bind(null, product);
                    productMoreInfo.innerText = "More Info";
                    cardBody.append(productHeader, productPrice, productCategory, productAddToBucket, productMoreInfo);
                    card.append(img, cardBody);
                    productCol.append(card);
                    return productCol;
                })
        }
        const getProductsRequestParameters = () => {
            const params = new URLSearchParams();
            if (productsState.pageNumber) {
                params.append('page', productsState.pageNumber);
            }
            if (productsState.pageSize) {
                params.append('size', productsState.pageSize);
            }
            if (productsState.namePattern) {
                params.append('pattern', productsState.namePattern);
            }
            if (productsState.categoryUuid) {
                params.append('categoryUuid', productsState.categoryUuid);
            }
            if (productsState.orderBy) {
                params.append('orderBy', productsState.orderBy);
            }

            params.append('ascending', productsState.orderSwitch);

            return params;
        }
        const drawPagination = () => {
            const productsColl = document.querySelector('#productsColl')

            const pageNav = document.createElement('nav');
            pageNav.setAttribute('aria-label', 'Page navigation');
            const pagination = document.createElement('ul');
            addClassesToElement(pagination, 'pagination fixed justify-content-center');
            const previousPage = document.createElement('li');
            const previousPageLink = document.createElement('a');
            previousPageLink.innerText = 'Previous';
            if (productsState.firstPage) {
                addClassesToElement(previousPageLink, 'page-link');
                addClassesToElement(previousPage, 'page-item disabled');
                previousPageLink.setAttribute('aria-disabled', 'true');
            } else {
                addClassesToElement(previousPage, 'page-item');
                addClassesToElement(previousPageLink, 'page-link');
                previousPageLink.onclick = () => {
                    productsState = {...productsState, pageNumber: productsState.pageNumber - 1};
                    loadAndRedrawProducts();
                    drawPagination();
                }
            }
            previousPage.append(previousPageLink);
            pagination.append(previousPage);
            for (let i = 1; i <= productsState.totalPageNumber; i++) {
                const page = document.createElement('li');
                const pageLink = document.createElement('a');
                if (i === productsState.pageNumber) {
                    addClassesToElement(page, 'page-item active');
                    page.setAttribute('aria-current', 'page')
                    addClassesToElement(pageLink, 'page-link');
                    pageLink.innerText = i.toString();
                    pageLink.onclick = () => {
                    }
                } else {
                    addClassesToElement(page, 'page-item');
                    page.setAttribute('aria-current', 'page')
                    addClassesToElement(pageLink, 'page-link');
                    pageLink.innerText = i.toString();
                    pageLink.onclick = () => {
                        productsState = {...productsState, pageNumber: i};
                        loadAndRedrawProducts();
                    }
                }
                page.append(pageLink);
                pagination.append(page);
            }
            const nextPage = document.createElement('li');
            const nextPageLink = document.createElement('a');
            nextPageLink.innerText = 'Next';
            if (productsState.lastPage) {
                addClassesToElement(nextPageLink, 'page-link');
                addClassesToElement(nextPage, 'page-item disabled');
                nextPageLink.setAttribute('aria-disabled', 'true');
            } else {
                addClassesToElement(nextPage, 'page-item');
                addClassesToElement(nextPageLink, 'page-link');
                nextPageLink.onclick = () => {
                    productsState = {...productsState, pageNumber: productsState.pageNumber + 1};
                    loadAndRedrawProducts();
                }
            }
            nextPage.append(nextPageLink);
            pagination.append(nextPage);
            pageNav.append(pagination);
            productsColl.append(pageNav);
        }
        const getProductCategoryName = (product) => {
            if (!product.categoryUuid) {
                return 'No category';
            }
            const url = `http://localhost:8080/web-api/category/${product.categoryUuid}`;

        }
        const loadAndRedrawProducts = () => {
            loadProducts()
                .then(() => drawProducts(
                    addToBucket,
                    moreInfo
                ))
                .then(() => {
                    if (productsState.products.length !== 0) {
                        drawPagination()
                    }
                });

        }
        const moreInfo = (product) => {
            productPageState['productUuid'] = product['uuid'];
            currentPage = PAGES.PRODUCT;
            buildMainContent();
        }
    }
    const loadBucketPage = () => {
        history.pushState({page: PAGES.BUCKET}, null, '');
        let bucketState = {
            bucketProducts: {},
            totalPrice: 0
        }

        let productsState = {
            products: []
        }

        const getTotalPrice = () => {
            let sum = 0;
            for (const [uuid, quantity] of Object.entries(bucketState.bucketProducts)) {
                const product = productsState.products.find(p => {
                    return p.uuid === uuid;
                })
                sum += product.price * quantity;
            }
            return sum;
        }
        const loadBucketProducts = async () => {
            try {
                console.log(`${bucketsUrl}/products`);
                const response = await fetch(`${bucketsUrl}/products`, {
                    headers: getHeaders()
                })
                const data = (await response.json()).data;
                for (const uuid of data.uuids) {
                    bucketState.bucketProducts[uuid] = await getProductQuantityById(uuid);
                    productsState.products.push(await loadProductById(uuid));
                }
            } catch (e) {
                console.error(e);
            }
        }

        const getProductQuantityById = async (uuid) => {
            try {
                console.log(`${bucketsUrl}/products/${uuid}/quantity`);
                const response = await fetch(`${bucketsUrl}/products/${uuid}/quantity`, {
                    headers: getHeaders()
                });
                const data = (await response.json()).data;
                return data.quantity;
            } catch (e) {
                console.error(e);
            }

        }

        loadBucketProducts().then(() => getProductsTable(
            onChangeQuantity,
            onRemove
        )).then(table => mainContent.append(table));

        const getProductsTable = (onQuantityChange, onRemove) => {
            const productsTable = document.createElement('table');
            addClassesToElement(productsTable, 'table table-striped');
            const tableHead = document.createElement('thead');
            const thName = document.createElement('th');
            thName.innerText = 'Product Name';
            const thPrice = document.createElement('th');
            thPrice.innerText = 'Price';
            const thQuantity = document.createElement('th');
            thQuantity.innerText = 'Quantity';
            const thImage = document.createElement('th');
            thImage.innerText = 'Image';
            const thSubtotal = document.createElement('th');
            thSubtotal.innerText = 'Subtotal';
            const thAction = document.createElement('th');
            thAction.innerText = 'Remove';
            tableHead.append(thName, thPrice, thQuantity, thImage, thSubtotal, thAction);
            const tableBody = document.createElement('tbody');
            for (const [uuid, quantity] of Object.entries(bucketState.bucketProducts)) {
                tableBody.append(getProductTableRow(uuid, quantity, onQuantityChange, onRemove));
            }
            const tableFooter = document.createElement('tfoot');
            const footerRaw = document.createElement('tr');
            const emptyCol = document.createElement('td');
            emptyCol.setAttribute('colspan', '3');
            const footerTotal = document.createElement('td');
            addClassesToElement(footerTotal, 'fw-bold');
            footerTotal.innerText = "Total :"
            const footerPrice = document.createElement('td');
            footerPrice.setAttribute('id', 'totalPrice');
            footerPrice.innerText = `₴${bucketState.totalPrice.toFixed(2)}`
            const createOrder = document.createElement('td');
            const createButton = document.createElement('button');
            createButton.innerText = 'Create Order';
            addClassesToElement(createButton, 'btn btn-success');
            if (!isEmpty(bucketState.bucketProducts)) {
                createButton.onclick = () => {
                    fetch(`${ordersUrl}`, {
                        method: 'POST',
                        headers: getHeaders()
                    }).then(() => {
                        buildMainContent()
                        portalHolder.append(getSuccessMessage('Order was successfully created'));
                    }).catch(e => {
                        portalHolder.append(getErrorMessage(e))
                    })

                }
                createOrder.append(createButton);
            }
            footerRaw.append(emptyCol, footerTotal, footerPrice, createOrder);

            tableFooter.append(footerRaw);
            productsTable.append(tableHead, tableBody, tableFooter);
            return productsTable;
        };

        const getProductTableRow = (uuid, quantity, onQuantityChange, onRemove) => {
            const product = productsState.products.find(p => p.uuid === uuid);
            let imageUrl = 'https://via.placeholder.com/50';
            const subtotalPrice = (product.price * quantity);
            bucketState.totalPrice += subtotalPrice;
            if (product.imageBytes) {
                imageUrl = `data:image/png;base64,${product.imageBytes}`
            }
            const productRow = document.createElement('tr');
            const productName = document.createElement('td');
            productName.innerText = product.name;
            productName.onclick = () => {
                productPageState.productUuid = uuid;
                currentPage = PAGES.PRODUCT;
                buildMainContent();
            };
            const productPrice = document.createElement('td');
            productPrice.innerText = `₴${product.price.toFixed(2)}`;
            const productQuantity = document.createElement('td');
            const quantityInput = document.createElement('input');
            addClassesToElement(quantityInput, 'form-control');
            quantityInput.setAttribute('type', 'number');
            quantityInput.setAttribute('value', quantity);
            quantityInput.setAttribute('min', '1');
            quantityInput.setAttribute('max', MAX_BUCKET_QUANTITY);
            productQuantity.append(quantityInput);
            const productImg = document.createElement('td');
            const img = document.createElement('img');
            addClassesToElement(img, 'img-bucket img-thumbnail');
            img.setAttribute('alt', 'NO IMAGE');
            img.setAttribute('src', imageUrl);
            productImg.append(img);
            const productSubtotal = document.createElement('td');
            productSubtotal.innerText = `₴${subtotalPrice.toFixed(2)}`;
            const productRemove = document.createElement('td');
            const removeButton = document.createElement('button');
            addClassesToElement(removeButton, 'btn btn-danger btn-sm');
            const buttonImg = document.createElement('i');
            addClassesToElement(buttonImg, 'fa fa-trash');
            removeButton.append(buttonImg);
            productRemove.append(removeButton);
            productRow.append(productName, productPrice, productQuantity, productImg, productSubtotal, productRemove);

            removeButton.onclick = onRemove.bind(null, product, quantityInput);
            quantityInput.onchange = onQuantityChange.bind(null, product, quantityInput, productSubtotal);

            return productRow;
        };

        const onChangeQuantity = (product, quantityField, subTotalField) => {
            const quantity = quantityField.value;
            const response = fetch(`${bucketsUrl}/products/${product.uuid}/quantity?quantity=${quantity}`, {
                method: 'PUT',
                headers: getHeaders()
            }).then(response => {
                return response.json();
            }).then(response => {
                if (response.statusCode === 200) {
                    return true;
                } else {
                    portalHolder.append(getErrorMessage(response.message));
                    return false;
                }
            }).then(isSuccess => {
                if (isSuccess) {
                    bucketState.bucketProducts[product.uuid] = quantity;
                    bucketState.totalPrice = getTotalPrice();
                    document.querySelector('#totalPrice').innerText = `₴${bucketState.totalPrice.toFixed(2)}`;
                    subTotalField.innerText = `₴${(product.price * quantity).toFixed(2)}`;
                }
            }).catch(e => {
                portalHolder.append(getErrorMessage(e));
            });
        }

        const onRemove = (product, quantityField) => {
            const quantity = quantityField.value;
            const params = new URLSearchParams({
                'uuid': product.uuid,
                'quantity': quantity
            })
            console.log(`${bucketsUrl}/products/remove?${params}`);
            fetch(`${bucketsUrl}/products/remove?${params}`, {
                method: 'DELETE',
                headers: getHeaders()
            }).then(() => {
                buildMainContent();
            }).catch(console.error);
        }
    }
    const loadProductPage = (uuid) => {
        history.pushState({page: PAGES.PRODUCT}, null, '');
        const drawProductView = () => {
            let product;
            let reviews = [];
            let category;
            return loadProductById(uuid).then(data => {
                product = data;
                return product
            }).then(product => {
                return loadReviewsIdByProductId(product.uuid);
            }).then(reviewsId => {
                if (!reviewsId) {
                    return;
                }
                return loadProductReviews(reviewsId)
            }).then(loadedReviews => {
                reviews = loadedReviews;
            }).then(() => {
                if (product.categoryUuid) {
                    category = loadCategoryById(product.categoryUuid);
                }
            }).then(() => {
                let imageUrl = 'https://via.placeholder.com/400';
                if (product.imageBytes) {
                    imageUrl = `data:image/png;base64,${product.imageBytes}`
                }
                const productRow = document.createElement('div');
                addClassesToElement(productRow, 'row');
                const imageCol = document.createElement('div');
                addClassesToElement(imageCol, 'col-md-6');
                const image = document.createElement('img');
                addClassesToElement(image, 'product-page-img  img-fluid  img-thumbnail')
                image.setAttribute('atl', 'Product image');
                image.setAttribute('src', imageUrl);
                imageCol.append(image);
                const productCol = document.createElement('div');
                addClassesToElement(productCol, 'col-md-6');
                const productName = document.createElement('h2');
                addClassesToElement(productName, 'mb-3');
                productName.innerText = product.name;
                const productPrice = document.createElement('p');
                addClassesToElement(productPrice, 'mb-4');
                productPrice.innerHTML = `<strong>Price</strong>: ₴${product.price}`;
                const productDescription = document.createElement('p');
                addClassesToElement(productDescription, 'mb-4');
                productDescription.innerHTML = `<strong>Description</strong>: ${product.description ? product.description : ''}`;
                const productCategory = document.createElement('p');
                addClassesToElement(productCategory, 'mb-4');
                productCategory.innerHTML = `<strong>Category</strong>: ${category ? category.name : ''}`;
                const bucketForm = document.createElement('form');
                const bucketContainer = document.createElement('div');
                addClassesToElement(bucketContainer, 'mb-3');
                const bucketLabel = document.createElement('label');
                addClassesToElement(bucketLabel, 'form-label');
                bucketLabel.setAttribute('for', 'quantity');
                bucketLabel.innerText = 'Quantity';
                const bucketInput = document.createElement('input');
                addClassesToElement(bucketInput, 'form-control');
                bucketInput.setAttribute('type', 'number');
                bucketInput.setAttribute('id', 'quantity');
                bucketInput.setAttribute('name', 'quantity');
                bucketInput.setAttribute('min', '1');
                bucketInput.setAttribute('max', MAX_BUCKET_QUANTITY);
                bucketInput.setAttribute('value', '1');
                const formButton = document.createElement('button');
                addClassesToElement(formButton, 'btn btn-primary mb-3');
                formButton.innerText = 'Add to bucket';
                formButton.onclick = () => {
                    const quantity = document.querySelector('#quantity').value;
                    addToBucket(product, quantity).then(response => {
                        if (response.statusCode === 200) {
                            portalHolder.append(getSuccessMessage(`Product ${product.name} was successfully added to the bucket`))
                        } else {
                            portalHolder.append(getErrorMessage(`${response.message}`))
                        }
                    }).catch(e => {
                        portalHolder.append(getErrorMessage(`${e}`))
                    })
                }
                bucketContainer.append(bucketLabel, bucketInput);
                bucketForm.append(bucketContainer, formButton);
                const sendReviewTitle = document.createElement('h3');
                sendReviewTitle.innerText = `Send review`;
                const formDiv = document.createElement('div');
                addClassesToElement(formDiv, 'row mb-3');
                const reviewForm = document.createElement('form');
                const formComment = document.createElement('div');
                addClassesToElement(formComment, 'form-group');
                formComment.innerHTML = `<label for="comment">Leave a Comment:</label>
                        <textarea class="form-control" id="comment" rows="3"></textarea>
                        <div id="invalidCommentMessage" class="invalid-feedback"></div>`;
                const formRating = document.createElement('div');
                addClassesToElement(formRating, 'form-group');
                formRating.innerHTML = `<label for="rating">Rating:</label>
                        <select class="form-control" id="rating">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                        <div id="invalidRatingMessage" class="invalid-feedback">
                            Please provide a valid city.
                        </div>`;
                const sendButton = document.createElement('button');
                addClassesToElement(sendButton, 'btn btn-primary mb-3 me-2');
                sendButton.innerHTML = `Send button`;
                sendButton.onclick = () => {
                    const commentInput = document.querySelector(`#comment`);
                    const ratingInput = document.querySelector(`#rating`);
                    const message = commentInput.value;
                    const rating = ratingInput.value;
                    sendReview(message, rating, uuid).then(response => {
                            if (response.statusCode === 200) {
                                let createdReview = {};
                                createdReview.uuid = response.data.uuid;
                                createdReview.message = message;
                                createdReview.rating = rating;
                                reviewList.append(getReviewElement(createdReview));
                                commentInput.value = '';
                                ratingInput.value = '1';
                                removeClass(commentInput, 'is-invalid');
                                removeClass(ratingInput, 'is-invalid');
                                portalHolder.append(getSuccessMessage('Review was successfully created'))
                            } else if (response.statusCode === 422) {
                                if (response.data.message) {
                                    document.querySelector('#invalidCommentMessage').innerText = response.data.message;
                                    document.querySelector('#comment').classList.add('is-invalid');
                                }
                                if (response.data.rating) {
                                    document.querySelector('#invalidRatingMessage').innerText = response.data.rating;
                                    document.querySelector('#rating').classList.add('is-invalid');
                                }
                            } else if (response.statusCode >= 400) {
                                portalHolder.append(getErrorMessage(response.message));
                            }
                        }
                    ).catch(e => {
                        portalHolder.append(getErrorMessage(e))
                    });
                };
                const editButton = document.createElement('button');
                addClassesToElement(editButton, 'btn btn-primary mb-3');
                editButton.innerHTML = `Edit product`;
                editButton.onclick = () => {
                    const productDiv = getProductInputTable(product);
                    const buttonClose = productDiv.querySelector('#close');
                    const buttonSave = productDiv.querySelector('#save');
                    const productForm = productDiv.querySelector('#productForm');

                    const name = productDiv.querySelector('#name');
                    const price = productDiv.querySelector('#price');
                    const description = productDiv.querySelector('#description');
                    const category = productDiv.querySelector('#category');
                    const file = productDiv.querySelector('#file');

                    const categorySelect = productDiv.querySelector('#category');
                    for (const category of categoriesState.categories) {
                        const option = document.createElement('option');
                        option.setAttribute('value', category.uuid)
                        option.innerText = category.name;
                        categorySelect.append(option);
                    }
                    categorySelect.value = product.categoryUuid ? product.categoryUuid : '';
                    buttonClose.onclick = (e) => {
                        e.preventDefault();
                        hidePortals();
                    }
                    buttonSave.onclick = (e) => {
                        e.preventDefault();
                        updateProduct(uuid, {
                            name: name.value,
                            price: price.value,
                            description: description.value,
                            category: category.value
                        }, file).then(response => {
                            if (response.statusCode === 200) {
                                hidePortals();
                                buildMainContent();
                            } else if (response.statusCode === 422) {
                                name.classList.remove('is-invalid');
                                price.classList.remove('is-invalid');
                                description.classList.remove('is-invalid');
                                category.classList.remove('is-invalid');
                                file.classList.remove('is-invalid');
                                if (response.data.name) {
                                    productDiv.querySelector('#invalidName').innerText = response.data.name;
                                    name.classList.add('is-invalid');
                                }
                                if (response.data.price) {
                                    productDiv.querySelector('#invalidPrice').innerText = response.data.price;
                                    price.classList.add('is-invalid');
                                }
                                if (response.data.description) {
                                    productDiv.querySelector('#invalidDescription').innerText = response.data.description;
                                    description.classList.add('is-invalid');
                                }
                                if (response.data.category) {
                                    productDiv.querySelector('#invalidCategory').innerText = response.data.category;
                                    category.classList.add('is-invalid');
                                }
                                if (response.data.file) {
                                    productDiv.querySelector('#invalidFile').innerText = response.data.file;
                                    file.classList.add('is-invalid');
                                }
                            } else if (response.statusCode >= 400) {
                                hidePortals();
                                buildMainContent();
                                portalHolder.append(getErrorMessage(response.message));
                            }
                        }).catch(e => {
                            hidePortals();
                            portalHolder.append(getErrorMessage(e))
                        });
                    }

                    portalHolder.append(getOverlay(productDiv, false))
                }
                const reviewTitle = document.createElement('h1');
                if (reviews.length >= 1) {
                    reviewTitle.innerText = `Reviews`;
                }
                const reviewList = document.createElement('ul');
                addClassesToElement(reviewList, 'list-group');
                for (const review of reviews) {
                    reviewList.append(getReviewElement(review));
                }
                reviewForm.append(formComment, formRating);
                formDiv.append(reviewForm);
                productCol.append(productName, productPrice, productDescription, productCategory, bucketForm, sendReviewTitle, formDiv, sendButton, editButton);
                productRow.append(imageCol, productCol, reviewTitle, reviewList);

                return productRow;
            });
        }

        const getReviewElement = (review) => {
            const reviewsElement = document.createElement('li');
            addClassesToElement(reviewsElement, 'list-group-item');
            const reviewAuthor = document.createElement('div');
            addClassesToElement(reviewAuthor, 'd-flex justify-content-between');
            reviewAuthor.innerHTML = `<h5 class="mb-1">Username</h5>
                    <small>${review.rating}/5</small>`;
            const reviewText = document.createElement('p');
            addClassesToElement(reviewText, 'mb-1');
            reviewText.innerText = `${review.message ? review.message : ''}`;
            reviewsElement.append(reviewAuthor, reviewText);
            return reviewsElement;
        }

        drawProductView().then(view => {
            mainContent.append(view);
        })
    }
    const loadLoginPage = () => {
        history.pushState({page: PAGES.LOGIN}, null, '');
        currentPage = PAGES.LOGIN;
        const getLoginForm = () => {
            const container = document.createElement('div');
            addClassesToElement(container, 'container');
            container.innerHTML = ` 
             <h1 class="mt-5">Login Form</h1>
                  <form>
                    <div class="mb-3">
                      <label for="email" class="form-label">Email:</label>
                      <input type="text" class="form-control" id="email" name="email">
                       <div id="invalidEmail" class="invalid-feedback"></div>
                    </div>
            
                    <div class="mb-3">
                      <label for="password" class="form-label">Password:</label>
                      <input type="password" class="form-control" id="password" name="password">
                       <div id="invalidPassword" class="invalid-feedback"></div>
                    </div>
            
                    <button id="loginButton"  class="btn btn-primary">Log in</button>
                    <button id="registerButton" class="btn btn-primary">Register</button>
                  </form>`
            return container;
        }

        const loginContainer = getLoginForm();
        const loginButton = loginContainer.querySelector('#loginButton');
        const emailInput = loginContainer.querySelector('#email');
        const passwordInput = loginContainer.querySelector('#password');
        const invalidEmail = loginContainer.querySelector('#invalidEmail');
        const invalidPassword = loginContainer.querySelector('#invalidPassword');
        loginButton.onclick = (e) => {
            e.preventDefault();
            const email = emailInput.value;
            const password = passwordInput.value;
            login(email, password).then(response => {
                if (response.statusCode === 200) {
                    localStorage.setItem(JWT, response.data.jwtToken);
                    currentPage = PAGES.PRODUCTS;
                    buildMainContent();
                } else if (response.statusCode === 422) {
                    emailInput.classList.remove('is-invalid');
                    passwordInput.classList.remove('is-invalid');
                    if (response.data.email) {
                        emailInput.classList.add('is-invalid');
                        invalidEmail.innerText = response.data.email;
                    }
                    if (response.data.password) {
                        passwordInput.classList.add('is-invalid');
                        invalidPassword.innerText = response.data.password;
                    }
                } else if (response.statusCode >= 400) {
                    portalHolder.append(getErrorMessage(response.message));
                }
            })
        }

        mainContent.append(loginContainer);
    }


    rootNode.append(navbar);
    rootNode.append(mainContent);
    buildMainContent();

    const loadProductReviews = async (reviewsId) => {
        const reviews = [];
        for (const reviewId of reviewsId) {
            reviews.push(await loadReviewById(reviewId));
        }
        return reviews;
    }
    const sendReview = async (message, rating, productUuid) => {
        try {
            const requestBody = {
                'message': message,
                'rating': rating,
                'productUuid': productUuid
            }
            const response = await fetch(`${reviewsUrl}`, {
                method: 'POST',
                body: JSON.stringify(requestBody),
                headers: {
                    ...getHeaders(),
                    'Content-Type': 'application/json'
                }
            })
            return await response.json();
        } catch (e) {
            console.error(e);
        }
    }
    const loadProductById = async (uuid) => {
        const response = await fetch(`${productsUrl}/${uuid}`, {
            headers: getHeaders()
        });
        return (await response.json()).data.product;
    }
    const loadReviewsIdByProductId = async (uuid) => {
        const response = await fetch(`${reviewsUrl}/products/${uuid}`, {
            headers: getHeaders()
        });
        return (await response.json()).data.uuids;
    }
    const loadReviewById = async (uuid) => {
        const response = await fetch(`${reviewsUrl}/${uuid}`, {
            headers: getHeaders()
        });
        const data = (await response.json()).data;
        console.log(data);
        return data.review;
    }
    const loadCategoryById = async (uuid) => {
        const response = await fetch(`${categoriesUrl}/${uuid}`, {
            headers: getHeaders()
        });
        return (await response.json()).data.category;
    }
    const addToBucket = async (product, quantity) => {
        try {
            const params = new URLSearchParams({
                uuid: product.uuid,
                quantity: quantity
            });
            console.log(`${bucketsUrl}/products/add?${params}`);
            const response = await fetch(`${bucketsUrl}/products/add?${params}`,
                {
                    method: 'PUT',
                    headers: getHeaders()
                });
            return await response.json();
        } catch (e) {
            console.error(e);
        }

    };
    const getProductInputTable = (product) => {
        const container = document.createElement('div');
        container.innerHTML = `<h1>Add Product</h1>
      <form id="productForm">
        <div class="mb-3">
          <label for="name" class="form-label">Name</label>
          <input type="text" class="form-control" id="name" name="name" value="${product.name ? product.name : ''}">
          <div id="invalidName" class="invalid-feedback"></div>
        </div>
        <div class="mb-3">
          <label for="price" class="form-label">Price</label>
          <input type="number" class="form-control" id="price" name="price" min="0" step="0.1" value="${product.price ? product.price : ''}">
          <div id="invalidPrice" class="invalid-feedback"></div>
        </div>
        <div class="mb-3">
          <label for="description" class="form-label">Description</label>
          <textarea class="form-control" id="description" name="description" rows="3">${product.description ? product.description : ''}</textarea>
          <div id="invalidDescription" class="invalid-feedback"></div>
        </div>
        <div class="mb-3">
          <label for="category" class="form-label">Category</label>
          <select class="form-select" id="category" name="category">
            <option value="">No category specified</option>            
          </select>
          <div id="invalidCategory" class="invalid-feedback"></div>
        </div>
        <div class="mb-3">
          <label for="file" class="form-label">Image</label>
          <input type="file" class="form-control" id="file" name="image">
            <div id="invalidFile" class="invalid-feedback"></div>
        </div>      
        <button id="save" class="btn btn-primary">Save Product</button>
        <button id="close" class="btn btn-primary">Close</button>`;
        return container;
    }
    const updateProduct = async (uuid, productDto, fileInput) => {
        try {
            const formdata = new FormData();
            formdata.append("name", valueOrEmpty(productDto.name));
            formdata.append("price", valueOrEmpty(productDto.price));
            formdata.append("description", valueOrEmpty(productDto.description));
            formdata.append("categoryUuid", valueOrEmpty(productDto.category));
            if (fileInput.files[0]) {
                formdata.append("file", fileInput.files[0]);
            }

            const requestOptions = {
                method: 'PUT',
                body: formdata,
                headers: getHeaders()
            };

            const response = await fetch(`${productsUrl}/${uuid}`, requestOptions);
            return await response.json();
        } catch (e) {
            throw e;
        }
    }

    const login = async (email, password) => {
        try {
            const raw = JSON.stringify({
                "email": email,
                "password": password
            });

            const requestOptions = {
                method: 'POST',
                headers: {...getHeaders(), "Content-Type": "application/json"},
                body: raw,
                redirect: 'follow',

            };
            const response = await fetch(`${registrationUrl}/login`, requestOptions);
            return await response.json();
        } catch (e) {
            throw e;
        }
    }
});

function addClassesToElement(element, classesString) {
    const classes = classesString.split(/\s+/)
    classes.forEach(
        navClass => element.classList.add(navClass)
    );
}

function removeClass(element, className) {
    element.classList.remove(className);
}

function valueOrEmpty(value) {
    return value ? value : '';
}