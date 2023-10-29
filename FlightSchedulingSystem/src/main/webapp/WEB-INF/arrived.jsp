<%@ page import="com.muxan.flightschedulingsystem.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.muxan.flightschedulingsystem.model.Flight" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.muxan.flightschedulingsystem.repository.FlightRepo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Arrived</title>
    <link rel="stylesheet" type="text/css" href="/style/blinkingCircle.css">
</head>
<body>

<% List<Flight> flights = (List<Flight>) request.getAttribute("flightsWithDirectionZero"); %>
<% FlightRepo flightRepo = new FlightRepo(); %>

<table>
    <tr>
        <td>is Arrived</td>
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
            <div class="blink" id="flightStatus_<%= flight.getFlightId() %>" style="display: <%= flightRepo.getIsActive(flight.getFlightId()) == 1 ? "inline-block" : "none" %>"></div>
            <span id="flightTextStatus_<%= flight.getFlightId() %>" style="display: <%= flightRepo.getIsActive(flight.getFlightId()) == 0 ? "inline" : "none" %>">Arrived</span>
        </td>
        <td><%= flight.getCountry() %> From </td>
        <td>Armenia</td>
        <td><%= flight.getAirplane().getNameAirplane() %></td>
        <td><%= flight.getAirplane().getAllSeats() %></td>
    </tr>
    <% } %>
    <% } %>
</table>


<button>  <a href="/logout">logout</a> </button>
<button>  <a href="/home">Previous</a> </button>

<script>
    // Function to check flight status
    function checkFlightStatus(flightId) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var response = JSON.parse(this.responseText);
                var statusDiv = document.getElementById("flightStatus_" + flightId);
                var textStatusDiv = document.getElementById("flightTextStatus_" + flightId);

                if (response.isActive) {
                    statusDiv.style.display = 'inline-block';
                    textStatusDiv.style.display = 'none';
                } else {
                    statusDiv.style.display = 'none';
                    textStatusDiv.style.display = 'inline';
                }
            }
        };
        xhttp.open("GET", "http://localhost:8080/checkFlightStatus?flightId=" + flightId, true);
        xhttp.send();
    }

    // Set interval to check flight status every 5 seconds
    <% for (Flight flight : flights) { %>
    checkFlightStatus(<%= flight.getFlightId() %>);
    setInterval(function() { checkFlightStatus(<%= flight.getFlightId() %>); }, 5000);
    <% } %>

</script>

</body>
</html>
