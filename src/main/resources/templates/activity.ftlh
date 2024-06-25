<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Activity Feed</title>
    <style>
        body {
            background-color: #171a21;
            color: #c4d1da;
            font-family: Arial, sans-serif;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            min-height: 100vh;
            background: url('https://steamcommunity-a.akamaihd.net/economy/image/-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxH5rd9eDAjcFyv45SRYAFMIcKL_PArgVSL403ulRUWEndVKv4gJaEAQ07dl1U4r_9KQZh0KGZcDhGu4zuwYbbkqfxa7qJlzkBusR33LuRrd6i3RqemzSFSdPyqA?size=300x180f') no-repeat center center fixed;
            background-size: cover;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            width: 100%;
            background: rgba(23, 26, 33, 0.9);
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            padding: 30px;
            color: #c4d1da;
        }

        h1 {
            color: #66c0f4;
            text-align: center;
            font-size: 1.8em;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background: rgba(255, 255, 255, 0.1);
        }

        tr:hover {
            background: rgba(255, 255, 255, 0.1);
        }

        .user-id {
            font-weight: bold;
            color: #66c0f4;
        }

        .event-type {
            color: #66c0f4;
        }

        .details {
            color: #c4d1da;
        }

        .timestamp {
            color: #999;
        }

        .icons {
            position: absolute;
            top: 20px;
            left: 20px;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .icons img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            border: 2px solid #66c0f4;
            cursor: pointer;
        }
    </style>
</head>
<body>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<div class="icons">
    <a href="/signIn">
        <img src="https://cdn.icon-icons.com/icons2/2072/PNG/512/crime_finger_fingerprint_identity_internet_print_security_icon_127061.png" alt="Security Icon">
    </a>
    <a href="#" id="logout-button">
        <img src="https://cdn.icon-icons.com/icons2/514/PNG/512/logout_icon-icons.com_51025.png" alt="Logout Icon">
    </a>
    <a href="/activity">
        <img src="https://cdn.icon-icons.com/icons2/4003/PNG/512/activity_sports_game_hobby_sport_winner_rest_competition_tennis_icon_255122.png" alt="Activity Icon">
    </a>
    <a href="/user/profile">
        <img src="https://cdn.icon-icons.com/icons2/2859/PNG/512/avatar_face_man_boy_male_profile_smiley_happy_people_icon_181658.png" alt="Profile Icon">
    </a>
</div>
<div class="container">
    <h1>Activity Feed</h1>
    <table>
        <thead>
        <tr>
            <th>User ID</th>
            <th>Event Type</th>
            <th>Details</th>
            <th>Timestamp</th>
        </tr>
        </thead>
        <tbody>
        <#list activityEvents as event>
            <tr>
                <td class="user-id">
                    <a href="/user/profile?steamId=${event.userId}" class="profile-link">${event.userId}</a>
                </td>
                <td class="event-type">${event.eventType}</td>
                <td class="details">${event.details}</td>
                <td class="timestamp">${event.timestamp}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<script>
    document.getElementById('logout-button').addEventListener('click', function() {
        fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            },
            body: JSON.stringify({})
        }).then(response => {
            if (response.ok) {
                window.location.href = '/';
            } else {
                alert('Logout failed.');
            }
        });
    });
</script>
</body>
</html>
