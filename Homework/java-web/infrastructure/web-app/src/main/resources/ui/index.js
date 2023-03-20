const PAGES = {
    PRODUCTS: 'PRODUCTS',
    BUCKET: 'BUCKET'
}

const getButton = (caption, onClick) => {
    const button = document.createElement('button');
    button.textContent = caption;
    button.onclick = onClick;

    return button;
};

document.addEventListener('DOMContentLoaded', () => {
    const categoriesUrl = 'http://localhost:8080/web-api/categories';
    const productsUrl = 'http://localhost:8080/web-api/products';
    const reviewsUrl = 'http://localhost:8080/web-api/reviews';
    const bucketsUrl = 'http://localhost:8080/web-api/bucket';
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

        switch (currentPage) {
            case PAGES.PRODUCTS:
                loadProductsPage();
                break;

            case PAGES.BUCKET:
                loadBucketPage();
                break;
        }
    }
    const loadProductsPage = () => {
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
        let categoriesState = {
            categories: []
        }

        const contentRow = document.createElement('div');
        addClassesToElement(contentRow, 'row');
        const productsCol = document.createElement('div');
        addClassesToElement(productsCol, 'col-lg-9');
        productsCol.setAttribute('id', 'productsColl')
        const loadCategories = async () => {
            try {
                const fetchedCategories = [];
                const categoriesResponse = await fetch(categoriesUrl);
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
                const categoryResponse = await fetch(`${categoriesUrl}/${uuid}`);
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
                const reviewResponse = await fetch(`${reviewsUrl}/products/${uuid}/rating`);
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
                const productsResponse = await fetch(`${productsUrl}?${paramsString}`);
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
                (product) => addToBucket(product),
                (product) => console.log(`more info ${product}`)
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
                    productRating.innerText = fetchedRating + '♂';
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
                    productAddToBucket.onclick = addToBucket.bind(null, product);
                    productAddToBucket.innerText = 'Add to Bucket';
                    const productMoreInfo = document.createElement('button');
                    addClassesToElement(productMoreInfo, 'btn btn-secondary');
                    productMoreInfo.onclick = moreInfo.bind(null, Object.entries(product));
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
            addClassesToElement(pagination, 'pagination justify-content-center');
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

        const addToBucket = async (product) => {
            try {
                const params = new URLSearchParams({
                    uuid: product.uuid,
                    quantity: '1'
                });
                console.log(`${bucketsUrl}/products/add?${params}`);
                const response = await fetch(`${bucketsUrl}/products/add?${params}`,
                    {
                        method: 'PUT'
                    });
                const data = (await response.json()).data;
                console.log(data);
            } catch (e) {
                console.error(e);
            }

        };

        const moreInfo = (product) => {
            console.log(`more info ${product}`);
        }
    }
    const loadBucketPage = () => {
        let bucketState = {
            bucketProducts: {},
            totalPrice: 0
        }

        let productsState = {
            products: []
        }
        const loadBucketProducts = async () => {
            try {
                console.log(`${bucketsUrl}/products`);
                const response = await fetch(`${bucketsUrl}/products`)
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
                const response = await fetch(`${bucketsUrl}/products/${uuid}/quantity`);
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
            footerPrice.innerText = `₴${bucketState.totalPrice.toFixed(2)}`
            footerRaw.append(emptyCol, footerTotal, footerPrice);
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
            const productPrice = document.createElement('td');
            productPrice.innerText = `₴${product.price.toFixed(2)}`;
            const productQuantity = document.createElement('td');
            const quantityInput = document.createElement('input');
            addClassesToElement(quantityInput, 'form-control');
            quantityInput.setAttribute('type', 'number');
            quantityInput.setAttribute('value', quantity);
            quantityInput.setAttribute('min', '1');
            quantityInput.setAttribute('max', '10');
            quantityInput.onchange = onQuantityChange.bind(null, product);
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
            removeButton.onclick = onRemove.bind(null, product);
            const buttonImg = document.createElement('i');
            addClassesToElement(buttonImg, 'fa fa-trash');
            removeButton.append(buttonImg);
            productRemove.append(removeButton);
            productRow.append(productName, productPrice, productQuantity, productImg, productSubtotal, productRemove);
            return productRow;
        };

        const onChangeQuantity = (product) => {
            console.log(`Change ${product}`);
        }

        const onRemove = (product) => {
            console.log(`Remove ${product}`);
        }
    }

    loadProductsPage();

    rootNode.append(navbar);
    rootNode.append(mainContent);

    const loadProductById = async (uuid) => {
        const response = await fetch(`${productsUrl}/${uuid}`);
        return (await response.json()).data.product;
    }
});

function addClassesToElement(element, classesString) {
    const classes = classesString.split(/\s+/)
    classes.forEach(
        navClass => element.classList.add(navClass)
    );
}

