function reserveTicket(airplaneId, flightDetails, userName, userSurname) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    // If reservation is successful, open the modal
                    openModalFreeSeats(response.message, flightDetails, userName, userSurname);
                } else {
                    // If reservation fails, show an alert
                    openModalNotFreeSeats(response.message, userName, userSurname);
                }
            }
        }
    };

    // Send a request to the server to reserve seats
    xhr.open("GET", "/reserveSeats?airplaneId=" + airplaneId, true);
    xhr.send();
}

function openModalFreeSeats(content, flightDetails, userName, userSurname) {
    var modal = document.getElementById("myModal");
    var modalContent = document.getElementById("modalContent");
    modal.style.display = "block";
    modalContent.innerHTML = content + "<br/>Reserved Flight: " + flightDetails +
        "<br/>Reserved by: " + userName + " " + userSurname;
}

function openModalNotFreeSeats(content, userName, userSurname) {
    var modal = document.getElementById("myModal");
    var modalContent = document.getElementById("modalContent");
    modal.style.display = "block";
    modalContent.innerHTML = content +  "<br/>Reserved by: " + userName + " " + userSurname;
}

function closeModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}
