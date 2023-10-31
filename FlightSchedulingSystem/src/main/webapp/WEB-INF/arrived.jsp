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
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        .blink {
            border-radius: 50%;
            width: 20px;
            height: 20px;
            background-color: red;
            animation: blinking 1s infinite;
        }

        @keyframes blinking {
            0% { background-color: red; }
            50% { background-color: transparent; }
            100% { background-color: red; }
        }

        .table th, .table td {
            text-align: center;
            vertical-align: middle;
        }

        .container {
            margin-top: 30px;
        }

        a.btn {
            text-decoration: none;
            color: white;
        }

        .btn-box {
            margin-top: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <% List<Flight> flights = (List<Flight>) request.getAttribute("flightsWithDirectionZero"); %>
    <% FlightRepo flightRepo = new FlightRepo(); %>

    <h2 class="text-center mb-4">Arrived Flights</h2>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="thead-dark">
            <tr>
                <th>is Arrived</th>
                <th>Direction</th>
                <th>Country</th>
                <th>Airplane</th>
                <th>Will Arrived</th>
            </tr>
            </thead>
            <tbody>
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
                <td><%= flight.getRunwayWithPeriod().getStart() %></td>
            </tr>
            <% } %>
            <% } else { %>
            <tr>
                <td colspan="5">No flights available</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <div class="btn-box text-center">
        <a href="/logout" class="btn btn-danger">Logout</a>
        <a href="/home" class="btn btn-secondary">Previous</a>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script>
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

    <% for (Flight flight : flights) { %>
    checkFlightStatus(<%= flight.getFlightId() %>);
    setInterval(function() { checkFlightStatus(<%= flight.getFlightId() %>); }, 5000);
    <% } %>
</script>
</body>
</html>


