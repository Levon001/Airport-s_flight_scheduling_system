<%@ page import="com.muxan.flightschedulingsystem.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.muxan.flightschedulingsystem.model.Flight" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.muxan.flightschedulingsystem.repository.FlightRepo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="/style/popupWindow.css">
    <link rel="stylesheet" type="text/css" href="/style/blinkingCircle.css">
    <link rel="stylesheet" href="/style/home.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<% User user = (User) session.getAttribute("user"); %>

<br/>

<% List<Flight> flights = (List<Flight>) request.getAttribute("flightsWithDirectionOne"); %>
<% FlightRepo flightRepo = new FlightRepo(); %>


<div id="home-start">
    <% if (user != null) { %>
    <h1 id="welcome"> Welcome <%= user.getName() %> <%= user.getSurname() %>
    </h1>
    <% } %>
</div>

<div class="second-part">
<div class="buttons">
    <div id="departure">
        <i class="fa-solid fa-plane-departure"></i>
        <span>Departure</span>
    </div>
    <div id="arrived">
        <i class="fa-solid fa-plane-arrival"></i>
        <button><a href="/arrived">Arrivals</a></button>
    </div>
</div>
<table border="1px">
    <tr>
        <th>is Avaliable</th>
        <th>Direction</th>
        <th>Country</th>
        <th>Airplane</th>
        <th>Count of airplane seats</th>
        <th>Action</th>
    </tr>
    <% if (flights != null && !flights.isEmpty()) { %>
    <% for (Flight flight : flights) { %>
    <tr>
        <td>
            <div class="blink" id="flightStatus_<%= flight.getFlightId() %>"
                 style="display: <%= flightRepo.getIsActive(flight.getFlightId()) == 1 ? "inline-block" : "none" %>"></div>
         </td>
        <td><span>Armenia To</span></td>
        <td><%= flight.getCountry() %>
        </td>
        <td><%= flight.getAirplane().getNameAirplane() %>
        </td>
        <td><%= flight.getAirplane().getAllSeats() %>
        </td>
        <td>
            <button id="reserve_button_<%= flight.getFlightId() %>"
                    onclick="reserveTicket(<%= flight.getAirplane().getId() %>,'<%= flight.getDetails() %>','<%= user.getName() %>','<%= user.getSurname() %>')">
                Reserve Ticket
            </button>
        </td>
    </tr>
    <% } %>
    <% } %>
</table>


<button><a href="/logout">logout</a></button>


<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <p id="modalContent"></p>
    </div>

</div>
</div>
<script type="text/javascript" src="js/popupWindow.js"></script>


<script>
    // Function to check flight status
    function checkFlightStatus(flightId) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var response = JSON.parse(this.responseText);
                var statusDiv = document.getElementById("flightStatus_" + flightId);
                var reserveButton = document.getElementById("reserve_button_" + flightId);

                if (response.isActive) {
                    statusDiv.style.display = 'inline-block';
                    reserveButton.disabled = false; // Enable the button
                } else {
                    statusDiv.style.display = 'inline-block';
                    statusDiv.style.background = 'grey';
                    reserveButton.disabled = true; // Disable the button
                }
            }
        };
        xhttp.open("GET", "/checkFlightStatus?flightId=" + flightId, true);
        xhttp.send();
    }

    // Set interval to check flight status every 5 seconds
    <% for (Flight flight : flights) { %>
    checkFlightStatus(<%= flight.getFlightId() %>);
    setInterval(function () {
        checkFlightStatus(<%= flight.getFlightId() %>);
    }, 5000);
    <% } %>
</script>


</body>
</html>
