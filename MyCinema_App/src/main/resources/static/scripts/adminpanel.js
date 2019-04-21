/*
document.addEventListener("DOMContentLoaded", function(event) {
    var modal = document.querySelector(".modal-form");
    var trigger = document.querySelector("#button-add");
    var closeButton = document.querySelector(".close-button");
    var deleteButton = document.querySelector("#button-delete");

    function toggleModal() {
        modal.classList.toggle("show-modal");
    }

    function windowOnClick(event) {
        if (event.target === modal) {
            toggleModal();
        }
    }

    function totalChecks() {
        var checkboxes = document.querySelectorAll(".check-for-delete");
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
            deleteButton.innerHTML = "Delete " + checks + " Rooms";
            deleteButton.disabled = false;
        }
        else if (checks == 1) {
            deleteButton.innerHTML = "Delete Room";
            deleteButton.disabled = false;
        }
        else {
            deleteButton.innerHTML = "Delete Room";
            deleteButton.disabled = true;
        }
    }

    trigger.addEventListener("click", toggleModal);
    closeButton.addEventListener("click", toggleModal);
    window.addEventListener("click", windowOnClick);

    deleteButton.disabled = true;

    document.querySelectorAll(".check-for-delete").forEach(function(elem) {
        elem.checked = false;
        elem.addEventListener("click", renameButton);
    });

});

*/


