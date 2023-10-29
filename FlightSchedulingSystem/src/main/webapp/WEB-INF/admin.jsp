<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>

    <style>
        form {
            display: inline-block;
            margin-top: 20px;
        }

        label, select, input {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <h1>Hello World</h1>

    <form action="scheduleList" method="post">
        <label for="directionDropdown">Select Direction:</label>
        <select name="directionDropdown" id="directionDropdown">
            <option value="to">To</option>
            <option value="from">From</option>
        </select>

        <label for="countriesDropdown">Select a Country:</label>
        <select name="countriesDropdown" id="countriesDropdown" >
            <option value="usa">USA</option>
            <option value="canada">Canada</option>
            <option value="uk">UK</option>
        </select>


        <label for="airplaneDropdown">Select a Country:</label>
        <select name="airplaneDropdown" id="airplaneDropdown" >
            <option value="1">SkyCruiser Horizon. seats:50</option>
            <option value="3">AeroSwift. seats 10</option>
            <option value="4">LuxJet Elite. seats 3</option>
            <option value="5">SilverWing Skyliner. seats 65</option>
            <option value="2">MuxanSky. seats 100</option>
        </select>


    <button type="submit">Submit</button>
    </form>

</body>
</html>
