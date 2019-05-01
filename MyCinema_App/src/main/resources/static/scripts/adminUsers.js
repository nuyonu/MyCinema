document.addEventListener("DOMContentLoaded", function(event) {
    var modalDeleteByUsername = document.querySelector(".modal-form-delete-by-username");

    var deleteButton = document.querySelector("#button-delete");
    var deleteByUsernameButton = document.querySelector("#button-delete-by-username");
    var closeAddModalButton = document.querySelector(".modal-form-delete-by-username .close-button");

    var checkboxes = document.querySelectorAll(".check-for-delete");

    function toggleModalDeleteByUsername() {
        modalDeleteByUsername.classList.toggle("show-modal");
        modalDeleteByUsername.parentElement.reset();
    }

    function windowOnClick(event) {
        if (event.target === modalDeleteByUsername) {
            toggleModalDeleteByUsername();
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
            deleteButton.innerHTML = "Delete " + checks + " users";
            deleteButton.disabled = false;
        }
        else if (checks == 1) {
            deleteButton.innerHTML = "Delete user";
            deleteButton.disabled = false;
        }
        else {
            deleteButton.innerHTML = "Delete user";
            deleteButton.disabled = true;
        }
    }

    deleteByUsernameButton.addEventListener("click", toggleModalDeleteByUsername);
    closeAddModalButton.addEventListener("click", toggleModalDeleteByUsername);
    window.addEventListener("click", windowOnClick);

    deleteButton.disabled = true;

    checkboxes.forEach(function(elem) {
        elem.checked = false;
        elem.addEventListener("click", renameButton);
    });
});
