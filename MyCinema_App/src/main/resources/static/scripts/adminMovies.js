document.addEventListener("DOMContentLoaded", function(event) {
    var modalAdd = document.querySelector(".modal-form-add");

    var deleteButton = document.querySelector("#button-delete");
    var addButton = document.querySelector("#button-add");
    var closeAddModalButton = document.querySelector(".modal-form-add .close-button");

    var checkboxes = document.querySelectorAll(".check-for-delete");

    function toggleModalAdd() {
        modalAdd.classList.toggle("show-modal");
        modalAdd.parentElement.reset();
    }

    function windowOnClick(event) {
        if (event.target === modalAdd) {
            toggleModalAdd();
        }
    }

    function totalChecks() {
        var total = 0;

        for (var i = 0; i < checkboxes.length; ++i)
            if (checkboxes[i].checked)
                total++;

        return total;
    }

    function renameButton()
    {
        var checks = totalChecks();

        if (checks > 1) {
            deleteButton.innerHTML = "Delete " + checks + " Movies";
            deleteButton.disabled = false;
        }
        else if (checks == 1) {
            deleteButton.innerHTML = "Delete Movie";
            deleteButton.disabled = false;
        }
        else {
            deleteButton.innerHTML = "Delete Movie";
            deleteButton.disabled = true;
        }
    }

    addButton.addEventListener("click", toggleModalAdd);
    closeAddModalButton.addEventListener("click", toggleModalAdd);
    window.addEventListener("click", windowOnClick);

    deleteButton.disabled = true;

    checkboxes.forEach(function(elem) {
        elem.checked = false;
        elem.addEventListener("click", renameButton);
    });
});

