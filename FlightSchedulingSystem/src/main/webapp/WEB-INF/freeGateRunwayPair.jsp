<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.tuple.Pair" %>
<%@ page import="com.muxan.flightschedulingsystem.payload.MyPeriod" %>
<%@ page import="com.muxan.flightschedulingsystem.util.FlightUtils" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.muxan.flightschedulingsystem.service.FlyService" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GateRunwayPair</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/style/runwayGatePAir.css">
    
    <script>
        function onlyOne(checkbox) {
            var checkboxes = document.getElementsByName('check');
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false;
            });

            document.getElementById('submitButton').disabled = !checkbox.checked;
        }


        function showModal() {
            var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
                keyboard: false
            });
            myModal.show();
        }

        function closeModal() {
            var modalElement = document.getElementById('myModal');
            var modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();
        }
    </script>
</head>
<body>
<%
    String allAttribute = (String) request.getAttribute("attributeDirectionCountryAirplaneID");
    String[] strings = allAttribute.split(",");
    int direct = strings[0] != null && strings[0].equals("to") ? 1 : 0;
    FlyService flyService = new FlyService();
    LinkedHashMap<Pair<Integer, Integer>, MyPeriod> gateRunwayPair = flyService.createGateRunwayPair(direct);
%>


<div class="container mt-5">
    <h4 class="mb-4">Choose from Schedule</h4>

    <div class="modal fade" id="myModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Success!</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Your flight has been successfully scheduled!
                </div>
            </div>
        </div>
    </div>

    <form action="/createFlight" method="post" onsubmit="submitFormAndGoBack()">
        <input type="hidden" name="allAttribute" value="<%= allAttribute %>"/>
        <div class="table-responsive">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col"></th>
                    <% if (direct == 1) { %>
                    <th scope="col">Gate Number</th>
                    <th scope="col">Runway Number</th>
                    <th scope="col">Start Time of Gate</th>
                    <th scope="col">End Time of Runway</th>
                    <% } else { %>
                    <th scope="col">Runway Number</th>
                    <th scope="col">Gate Number</th>
                    <th scope="col">Start Time of Runway</th>
                    <th scope="col">End Time of Gate</th>
                    <% } %>
                </tr>
                </thead>
                <tbody>
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
                    <td><input type="checkbox" name="check" value="<%=gateNumber %>,<%=runwayNumber %>"
                               onclick="onlyOne(this)"/></td>

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
                </tbody>
            </table>
        </div>

        <button type="submit" id="submitButton" class="btn btn-primary" disabled>Submit</button>
    </form>

    <div class="d-flex justify-content-between mt-4">
        <a class="btn btn-secondary" href="/adminPage">Previous Page</a>
        <a class="btn btn-secondary" href="/">Logout</a>
    </div>
</div>

</body>
</html>

