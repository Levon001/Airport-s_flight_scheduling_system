<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Page</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'DM Serif Display', serif;
            font-size: 19px;
            line-height: 1.6;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: auto;
            background: #fff;
            padding: 20px;
            text-align: center;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            min-height: 45vi;
        }

        h1 {
            font-size: 2em;
            color: #333;
            margin-bottom: 20px;
        }

        label {
            font-size: 1em;
            margin-bottom: 10px;
            display: block;
            text-align: left;
        }

        select, button {
            width: 100%;
            padding: 8px;
            margin: 10px 0 20px 0;
            border: 1px solid #ccc;
            box-sizing: border-box;
            border-radius: 5px;
        }

        button {
            background-color: #6a9dea;
            color: white;
            border: none;
            cursor: pointer;
            margin-top: 21%;
        }

        button:hover {
            background-color: rgba(58, 99, 161, 0.47);
        }

        .logout-btn:hover {
            background-color: rgba(108, 117, 125, 0.78);
        }

        form {
            display: flex;
            flex-direction: column;
        }

        .logout-btn {
            margin-right: 4%;
            text-decoration: none;
            color: white;
            width: 110px;
            background: #6c757d;
            border: none;
            border-radius: 5px;
            padding: 2px;
            font-size: 12px;
        }


    </style>
</head>
<body>

<div class="container">
    <h1>Admin Page</h1>

    <form action="/scheduleList" method="post">
        <label for="directionDropdown">Select Direction:</label>
        <select name="directionDropdown" id="directionDropdown">
            <option value="to">To</option>
            <option value="from">From</option>
        </select>

        <label for="countriesDropdown">Select a Country:</label>
        <select name="countriesDropdown" id="countriesDropdown">
            <option value="Aragatsotn">Aragatsotn</option>
            <option value="Ararat">Ararat</option>
            <option value="Armavir">Armavir</option>
            <option value="Gegharkunik">Gegharkunik</option>
            <option value="Kotayk">Kotayk</option>
            <option value="Lori">Lori</option>
            <option value="Shirak">Shirak</option>
            <option value="Syunik">Syunik</option>
            <option value="Tavush">Tavush</option>
            <option value="Vayots Dzor">Vayots Dzor</option>
        </select>

        <label for="airplaneDropdown">Select an Airplane:</label>
        <select name="airplaneDropdown" id="airplaneDropdown">
            <option value="1">SkyCruiser Horizon. seats:50</option>
            <option value="3">AeroSwift. seats 10</option>
            <option value="4">LuxJet Elite. seats 3</option>
            <option value="5">SilverWing Skyliner. seats 65</option>
            <option value="2">MuxanSky. seats 100</option>

        </select>

        <button type="submit">Submit</button>

        <a  href="/logout" class="logout-btn"><i class="fa-solid fa-arrow-right-from-bracket"> Logout</i> </a>

    </form>

</div>
</body>
</html>
