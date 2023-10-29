<%@ page import="com.muxan.flightschedulingsystem.repository.FlightRepo" %>
<%@ page import="com.muxan.flightschedulingsystem.model.Flight" %>
<%@ page import="java.util.List" %>
<%@ page import="com.muxan.flightschedulingsystem.service.FlyService" %>
<%@ page import="org.apache.commons.lang3.tuple.Pair" %>
<%@ page import="com.muxan.flightschedulingsystem.payload.MyPeriod" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.muxan.flightschedulingsystem.util.FlightUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GateRunwayPair</title>
    <link href="/style/runwayGatePAir.css">

    <script>
        function onlyOne(checkbox) {
            var checkboxes = document.getElementsByName('check');
            var submitButton = document.getElementById('submitButton');
            var isChecked = false;

            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false;
                if (item.checked) isChecked = true;
            });

            submitButton.disabled = !isChecked;
        }

        function showModal() {
            document.getElementById("myModal").style.display = "block";
        }

        function closeModal() {
            document.getElementById("myModal").style.display = "none";
        }

        function submitFormAndGoBack() {
            showModal();

            // Set a timeout to wait for 3 seconds (3000 milliseconds)
            setTimeout(function() {
                closeModal(); // Close the modal (optional)
                window.history.back(); // Go back to the previous page
            }, 3000); // Adjust the time as needed
        }

    </script>
</head>
<body>
<h4>choose from schedule</h4>

<%
    String allAttribute = (String) request.getAttribute("attributeDirectionCountryAirplaneID");
    String[] strings = allAttribute.split(",");
    int direct = strings[0] != null && strings[0].equals("to") ? 1 : 0;
    FlyService flyService = new FlyService();
    LinkedHashMap<Pair<Integer, Integer>, MyPeriod> gateRunwayPair = flyService.createGateRunwayPair(direct);
%>


<div class="modal" id="myModal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <p>Your flight has been successfully scheduled!</p>
    </div>
</div>



<form action="/createFlight" method="post" onsubmit="event.preventDefault(); submitFormAndGoBack();">
    <input type="hidden" name="allAttribute" value="<%= allAttribute %>"/>
    <table>
        <tr>
            <th></th>
            <%if (direct == 1) {%>
            <th>Gate Number</th>
            <th>Runway Number</th>
            <th>Start Time of Gate</th>
            <th>End Time of Runway</th>
            <% } else { %>
            <th>Runway Number</th>
            <th>Gate Number</th>
            <th>Start Time of Runway</th>
            <th>End Time of Gate</th>
            <%}%>
        </tr>
        <%
            int counter = 0;
            for (Map.Entry<Pair<Integer, Integer>, MyPeriod> entry : gateRunwayPair.entrySet()) {
                if (counter >= 15) break;
                Pair<Integer, Integer> key = entry.getKey();
                MyPeriod value = entry.getValue();
                int gateNumber = key.getLeft();
                int runwayNumber = key.getRight();
        %>
        <tr>
            <td><input type="checkbox" name="check" value="<%=gateNumber %>,<%=runwayNumber %>" onclick="onlyOne(this)"/></td>

            <% if (direct == 1) {%>
            <td>Gate Number <%= FlightUtils.takeOutNumberGateOrRunway(key.getLeft()) %>
            </td>
            <td>Runway Number <%= FlightUtils.takeOutNumberGateOrRunway(key.getRight()) %>
            </td>
            <%} else { %>
            <td>Runway Number <%= FlightUtils.takeOutNumberGateOrRunway(key.getLeft()) %>
            </td>
            <td>Gate Number <%= FlightUtils.takeOutNumberGateOrRunway(key.getRight()) %>
            </td>
            <%}%>
            <td><%= value.getStart() %>
            </td>
            <td><%= value.getEnd() %>
            </td>
        </tr>
        <%
                counter++;
            }
        %>
    </table>
    <button type="submit" id="submitButton" disabled>Submit</button>
    <a href="/">logout</a>
</form>

</body>
</html>
