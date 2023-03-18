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
        navbarBodyUl.append(navbarBodyLiProduct, navbarBodyLiBucket, navbarBodyLiOrder);
        navbarBody.append(navbarBodyUl);
        navbarHeader.append(navbarTitle, navbarButton, navbarBody);
        navbar.append(navbarHeader);
        return navbar;
    }

    let productsState = {
        products: [],
        pageNumber: 1,
        pageSize: 6,
        namePattern: '',
        categoryUuid: '',
        orderBy: 'name',
        orderSwitch: false,
        totalPageNumber: 0
    }
    let categoriesState = {
        categories: []
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
        const contentRow = document.createElement('div');
        addClassesToElement(contentRow, 'row');
        const productsCol = document.createElement('div');
        addClassesToElement(productsCol, 'col-lg-9');
        productsCol.setAttribute('id', 'productsColl')
        const loadCategories = async () => {
            try {
                const categories = [];
                const categoriesUrl = 'http://localhost:8080/web-api/category';
                const categoriesResponse = await fetch(categoriesUrl);
                const data = (await categoriesResponse.json()).data;
                for (const uuid of data.uuids) {
                    const categoryResponse = await fetch(`${categoriesUrl}/${uuid}`);
                    const category = (await categoryResponse.json()).data.category;
                    categories.push(category);
                }
                categoriesState.categories = categories;
                return categories;
            } catch (error) {
                console.error(error);
            }
        }
        const loadProducts = async () => {
            try {
                const productsUrl = 'http://localhost:8080/web-api/products';
                const params = getProductsRequestParameters();
                const paramsString = new URLSearchParams(params);
                console.log(`request : ${productsUrl}?${paramsString}`);
                const productsResponse = await fetch(`${productsUrl}?${paramsString}`);
                const page = (await productsResponse.json()).data.page;
                productsState.totalPageNumber = page.totalPages;
                productsState.products = page.content;
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
                (product) => console.log(`add to bucket ${product}`),
                (product) => console.log(`more info ${product}`)
            ))
            .then(() => console.log(productsState));

        mainContent.append(contentRow);

        const getSearchCol = () => {
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
            patterInput.onchange = e => {
                productsState = {...productsState, namePattern : e.currentTarget.value}
                loadProducts().then(() =>  drawProducts(
                    (product) => console.log(`add to bucket ${product}`),
                    (product) => console.log(`more info ${product}`)
                ));

            };
            patterInput.value = productsState.namePattern;
            const buttonSearch = document.createElement('button');
            addClassesToElement(buttonSearch, 'btn btn-outline-secondary');
            buttonSearch.setAttribute('type', 'button');
            buttonSearch.onclick = e => {
                loadProducts().then(() =>  drawProducts(
                    (product) => console.log(`add to bucket ${product}`),
                    (product) => console.log(`more info ${product}`)
                ));
            }
            buttonSearch.innerText = 'Search';

            const selectCategory = document.createElement('select');
            addClassesToElement(selectCategory, 'form-select custom-select border-dark mb-3');
            selectCategory.setAttribute('id', 'sortParameter');
            selectCategory.setAttribute('name', 'parameter');
            selectCategory.onchange = e => {
                productsState = {...productsState, categoryUuid : e.currentTarget.value};
                loadProducts().then(() =>  drawProducts(
                    (product) => console.log(`add to bucket ${product}`),
                    (product) => console.log(`more info ${product}`)
                ));
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
                productsState = {...productsState, orderBy : e.currentTarget.value};
                loadProducts().then(() =>  drawProducts(
                    (product) => console.log(`add to bucket ${product}`),
                    (product) => console.log(`more info ${product}`)
                ));
            };
            const optionName = document.createElement('option');
            optionName.setAttribute('value', 'name');
            optionName.innerText = 'Name';
            const optionPrice= document.createElement('option');
            optionPrice.setAttribute('value', 'price');
            optionPrice.innerText = 'Price';
            const optionRating= document.createElement('option');
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
                productsState = {...productsState, orderSwitch: orderSwitch.checked};
                loadProducts().then(() =>  drawProducts(
                    (product) => console.log(`add to bucket ${product}`),
                    (product) => console.log(`more info ${product}`)
                ));
            }
            const orderLabel = document.createElement('label');
            addClassesToElement(orderLabel, 'form-check-label');
            orderLabel.setAttribute('for','sortSwitch');
            orderLabel.innerText = 'Change order';
            orderSwitch.checked = productsState.orderSwitch;

            orderCheck.append(orderSwitch, orderLabel);
            inputGroup.append(patterInput, buttonSearch);

            col.append(inputGroup, selectCategory, selectOrderBy, orderCheck);
            return col;
        }
        const drawProducts = (addToBucket, moreInfo) => {

            const productsColl = document.querySelector('#productsColl')
            while (productsColl.firstChild) {
                productsColl.removeChild(productsColl.lastChild);
            }
            let products = productsState.products;
            if(productsState.orderSwitch) {
                products = products.reverse();
            }
            const row = document.createElement('row');
            addClassesToElement(row, 'row');
            products.forEach(product => {
                row.append(getProductCol(product, addToBucket, moreInfo));
            })
            productsColl.append(row);
        }
        const getProductCol = (product, addToBucket, moreInfo) => {
            const productCol = document.createElement('div');
            addClassesToElement(productCol, 'col-md-4 mb-3');
            const card = document.createElement('div');
            addClassesToElement(card, 'card');
            const img = document.createElement('img');
            addClassesToElement(img, 'card-img-top');
            img.setAttribute('src', 'https://via.placeholder.com/150');
            img.setAttribute('alt', 'Product Image');
            const cardBody = document.createElement('div');
            addClassesToElement(cardBody, 'card-body');
            const productName = document.createElement('h5');
            addClassesToElement(productName, 'card-title');
            productName.innerText = product.name;
            const productPrice = document.createElement('p');
            addClassesToElement(productPrice, 'card-text');
            productPrice.innerText = '₴' + product.price;
            const productCategory = document.createElement('p');
            addClassesToElement(productCategory, 'card-text');
            productCategory.innerText = 'Category place';
            const productAddToBucket = document.createElement('a');
            addClassesToElement(productAddToBucket, 'btn btn-primary me-1');
            productAddToBucket.onclick = addToBucket.bind(null, product.toString());
            productAddToBucket.innerText = 'Add to Bucket';
            const productMoreInfo = document.createElement('button');
            addClassesToElement(productMoreInfo, 'btn btn-secondary');
            productMoreInfo.onclick = moreInfo.bind(null, Object.entries(product));
            productMoreInfo.innerText = "More Info";
            cardBody.append(productName, productPrice, productCategory, productAddToBucket, productMoreInfo);
            card.append(img, cardBody);
            productCol.append(card);
            return productCol;
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
            return params;
        }
        const getProductCategoryName = (product) => {
            if(!product.categoryUuid) {
                return 'No category';
            }
            const url = `http://localhost:8080/web-api/category/${product.categoryUuid}`;

        }
    }
    const loadBucketPage = () => {

    }

    loadProductsPage();

    rootNode.append(navbar);
    rootNode.append(mainContent);
});

function addClassesToElement(element, classesString) {
    const classes = classesString.split(/\s+/)
    classes.forEach(
        navClass => element.classList.add(navClass)
    );
}

