const uri = "http://localhost:8080/api/products";
let products = [];
let updateIndex = 0;

document.getElementById("productavailable").addEventListener("change", function() {
    if (this.value === "true") {
        this.value = true;
    } else if (this.value === "false") {
        this.value = false;
    }
});

function getItems() {
    fetch(uri)
        .then((response) => response.json())
        .then((data) => _displayItems(data))
        .catch((error) => console.error("Unable to get items.", error));
}

function addItem() {
    const item = {
        name: document.getElementById("productname").value,
        category: document.getElementById("productcategory").value,
        price: document.getElementById("productprice").value,
        available: document.getElementById("productavailable").value
    };

    fetch(uri, {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(item),
    })
        .then(() => {
            getItems();
            document.getElementById("productname").value = "";
            document.getElementById("productcategory").value = "";
            document.getElementById("productprice").value = "";
            document.getElementById("productavailable").value = "";
        });
}

function deleteItem(id) {
    const item = {
        name: document.getElementById("productname").value,
        category: document.getElementById("productcategory").value,
        price: document.getElementById("productprice").value,
        available: document.getElementById("productavailable").value
    };
    fetch(uri+"/"+id, {
        method: "DELETE",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(item),
    })
        .then(() => {
            getItems();
        });
}

function editItem(id) {
    document.getElementById("myBtn").innerHTML = "Update";
    const item = products.find((item) => item.id === id);
    document.getElementById("productname").value = item.name;
    document.getElementById("productcategory").value = item.category;
    document.getElementById("productprice").value = item.price;
    document.getElementById("productavailable").value = item.available;
    updateIndex = id;
}

function saveORupdateItem() {
    if (document.getElementById("myBtn").innerHTML == "Save") {
        addItem();
    } else {
        updateItem();
    }
}

function updateItem() {
    const item = {
        name: document.getElementById("productname").value,
        category: document.getElementById("productcategory").value,
        price: document.getElementById("productprice").value,
        available: document.getElementById("productavailable").value
    };
    fetch(uri+"/"+updateIndex, {
        method: "PUT",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(item),
    }).then(() => getItems());
    document.getElementById("myBtn").innerHTML = "Save";
    document.getElementById("productname").value = "";
    document.getElementById("productcategory").value = "";
    document.getElementById("productprice").value = "";
    document.getElementById("productavailable").value = "true";
    updateIndex = 0;
}

function _displayItems(data) {
    const tBody = document.getElementById("products");
    tBody.innerHTML = "";

    const button = document.createElement("button");

    data.forEach((item) => {

        let editButton = button.cloneNode(false);
        editButton.innerText = "Edit";
        editButton.setAttribute("onclick", `editItem(${item.id})`);

        let deleteButton = button.cloneNode(false);
        deleteButton.innerText = "Delete";
        deleteButton.setAttribute("onclick", `deleteItem(${item.id})`);

        let tr = tBody.insertRow();

        let td1 = tr.insertCell(0);
        let id = document.createTextNode(item.id);
        td1.appendChild(id);

        let td2 = tr.insertCell(1);
        let name = document.createTextNode(item.name);
        td2.appendChild(name);

        let td3 = tr.insertCell(2);
        let category = document.createTextNode(item.category);
        td3.appendChild(category);

        let td4 = tr.insertCell(3);
        let price = document.createTextNode(item.price);
        td4.appendChild(price);

        let td5 = tr.insertCell(4);
        let available = document.createTextNode(item.available);
        td5.appendChild(available);

        let td6 = tr.insertCell(5);
        td6.appendChild(editButton);

        let td7 = tr.insertCell(6);
        td7.appendChild(deleteButton);
    });

    products = data;
}