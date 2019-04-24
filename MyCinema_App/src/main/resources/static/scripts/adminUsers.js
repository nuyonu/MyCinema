document.addEventListener("DOMContentLoaded", function(event) {
    var modalAdd = document.querySelector(".modal");

    var closeAddModalButton = document.querySelector(".modal .close-button");
    var deleteButton = document.querySelector("#button-deleteByName");

    function toggleModalAdd() {
        modalAdd.classList.toggle("show-modal");
        modalAdd.parentElement.reset();
    }

    function windowOnClick(event) {
        if (event.target === modalAdd) {
            toggleModalAdd();
        }
    }

    deleteButton.addEventListener("click", toggleModalAdd);
    window.addEventListener("click", windowOnClick);
    closeAddModalButton.addEventListener("click", toggleModalAdd);

});