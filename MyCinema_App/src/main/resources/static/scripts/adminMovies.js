document.addEventListener("DOMContentLoaded", function(event) {
    var deleteButton = document.querySelector("#button-delete");

    var checkboxes = document.querySelectorAll(".check-for-delete");

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
        else if (checks === 1) {
            deleteButton.innerHTML = "Delete Movie";
            deleteButton.disabled = false;
        }
        else {
            deleteButton.innerHTML = "Delete Movie";
            deleteButton.disabled = true;
        }
    }
    deleteButton.disabled = true;

    checkboxes.forEach(function(elem) {
        elem.checked = false;
        elem.addEventListener("click", renameButton);
    });
});

