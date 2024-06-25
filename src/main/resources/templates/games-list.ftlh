<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${type}</title>
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
            position: relative;
        }

        .container {
            max-width: 1000px;
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

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            background: rgba(255, 255, 255, 0.1);
            margin: 10px 0;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            display: flex;
            align-items: center;
        }

        .game-name {
            font-size: 1.2em;
            font-weight: bold;
            margin-left: 15px;
            color: #66c0f4;
            text-decoration: none;
        }

        .game-short {
            color: #999;
            margin-left: 15px;
        }

        img.avatar {
            width: 320px;
            height: 150px;
            border-radius: 4px;
        }

        .game-details {
            display: flex;
            flex-direction: column;
            margin-left: 15px;
        }

        .game-name:hover {
            text-decoration: underline;
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

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
            gap: 10px;
        }

        .pagination a, .pagination span {
            padding: 10px 20px;
            border: 1px solid #66c0f4;
            border-radius: 4px;
            text-decoration: none;
            color: #66c0f4;
        }

        .pagination a:hover {
            background-color: #66c0f4;
            color: #171a21;
        }

        .pagination span {
            background-color: #66c0f4;
            color: #171a21;
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
    <h1>${type}</h1>
    <ul>
        <#list allGames as game>
            <li>
                <img class="avatar" src="${game.header_image()}" alt="Game Image">
                <div class="game-details">
                    <a href="/game/${game.appid()}"class="game-name">${game.name()}</a>
                    <div class="game-short">${game.short_description()?no_esc}</div>
                </div>
            </li>
        </#list>
    </ul>

    <div class="pagination">
        <#if currentPage gt 0>
            <a href="?page=${currentPage - 1}&size=10">Previous</a>
        </#if>

        <span>Page ${currentPage + 1} of ${totalPages}</span>

        <#if currentPage lt (totalPages - 1)>
            <a href="?page=${currentPage + 1}&size=10">Next</a>
        </#if>
    </div>
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
