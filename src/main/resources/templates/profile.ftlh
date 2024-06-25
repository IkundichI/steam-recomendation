<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
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

        .flex-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
            gap: 20px;
        }

        .container, .commentEntities {
            max-width: 800px;
            width: 100%;
            background: rgba(23, 26, 33, 0.9);
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            padding: 30px;
            color: #c4d1da;
        }

        h1, h2 {
            color: #66c0f4;
            text-align: center;
        }

        h1 {
            font-size: 2em;
            margin-bottom: 20px;
        }

        h2 {
            font-size: 1.5em;
            margin-bottom: 10px;
        }

        img.avatar {
            display: block;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            margin: 0 auto 20px;
        }

        p {
            font-size: 1em;
            margin-bottom: 10px;
            text-align: center;
        }

        .friends, .games {
            margin-top: 30px;
        }

        .friend, .game {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .friend img {
            width: 100px;
            height: 100px;
            border-radius: 4px;
            margin-right: 15px;
        }

        .game img {
            width: 210px;
            height: 100px;
            border-radius: 4px;
            margin-right: 15px;
        }

        .friend p, .game p {
            margin: 0;
        }

        a.steam-profile-link {
            color: #66c0f4;
            text-decoration: none;
            cursor: pointer;
        }

        a.steam-profile-link:hover {
            text-decoration: underline;
        }

        .friends .friend, .games .game {
            background: rgba(255, 255, 255, 0.1);
            padding: 10px;
            border-radius: 4px;
        }

        .top-games-button {
            display: block;
            margin: 20px auto;
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 5px;
            font-size: 1em;
        }

        .top-games-button:hover {
            background-color: #0056b3;
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

        @media (max-width: 1200px) {
            .container, .commentEntities {
                max-width: 80%;
            }
        }

        @media (max-width: 768px) {
            .container, .commentEntities {
                max-width: 90%;
            }
        }

        .commentEntities {
            margin-top: 20px;
        }

        .commentEntities form {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .commentEntities textarea {
            resize: vertical;
            min-height: 100px;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
            font-size: 1em;
            background-color: #1b1e25;
            color: #c4d1da;
        }

        .commentEntities button {
            background-color: #66c0f4;
            color: #171a21;
            border: none;
            padding: 10px;
            font-size: 1em;
            border-radius: 4px;
            cursor: pointer;
        }

        .commentEntities button:hover {
            background-color: #558cba;
        }

        .comments-list {
            margin-top: 20px;
        }

        .comment {
            background: rgba(23, 26, 33, 0.9);
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 10px;
        }

        .comment p {
            margin: 0;
        }

        .comment-author {
            font-weight: bold;
            color: #66c0f4;
        }

        .comments-section {
            margin-top: 40px;
        }

        .comments-section h2 {
            font-size: 1.5em;
            color: #66c0f4;
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
    <#if isAdmin>
        <a href="/admin" target="_blank">
            <img src="https://cdn.iconscout.com/icon/premium/png-512-thumb/admin-4291349-3594745.png?f=webp&w=512" alt="Profile Icon">
        </a>
    </#if>

</div>
<div class="flex-container">
    <div class="container">
        <h1>${name}</h1>
        <a href="https://steamcommunity.com/profiles/${steamId}">
            <img class="avatar" src="${avatar}" alt="Avatar">
        </a>
        <p>SteamId: ${steamId}</p>
        <div class="friends">
            <h2><a href="/user/friends/${steamId}">Friends (${friendsCount})</a></h2>
            <#list friends as friend>
                <div class="friend">
                    <a href="https://steamcommunity.com/profiles/${friend.steamId()}" target="_blank" class="steam-profile-link">
                        <img src="${friend.avatar()}" alt="Friend Avatar">
                    </a>
                    <p><a class="steam-profile-link" href="/user/profile?steamId=${friend.steamId()}">${friend.name()}</a></p>
                </div>
            </#list>
        </div>

        <a href="/top-games?steamId=${steamId}" class="top-games-button">View Top Games</a>

        <div class="games">
            <h2><a href="/my-games?steamId=${steamId}">Games (${gamesCount})</a></h2>
            <#list games as game>
                <div class="game">
                    <img src="${game.header_image}" alt="Game Header">
                    <p>${game.name}</p>
                </div>
            </#list>
        </div>
    </div>
    <div class="commentEntities comments-section">
        <h2>Comments</h2>
        <form id="commentForm">
            <textarea id="commentContent" name="content" placeholder="Write your comment..."></textarea>
            <input type="hidden" name="profileId" value="${steamId}">
            <input type="hidden" id="csrfToken" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Submit</button>
        </form>
        <div class="comments-list" id="commentsList">
            <#list comments as comment>
                <div class="comment">
                    <p class="comment-author">${comment.user.login}</p>
                    <p>${comment.content}</p>
                </div>
            </#list>
        </div>
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

    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('commentForm');
        const commentsList = document.getElementById('commentsList');
        const commentContent = document.getElementById('commentContent');

        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            const formData = new FormData(form);
            const content = formData.get('content');
            const profileId = formData.get('profileId');
            const csrfToken = document.getElementById('csrfToken').value;

            const params = new URLSearchParams();
            params.append('content', content);
            params.append('profileId', profileId);

            try {
                const response = await fetch('/comments/addUserComment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: params.toString()
                });

                if (!response.ok) {
                    alert('Пользователь нет в нашей базе, выберите друго-го или попросите этого зайти на наш сайт');
                    commentContent.value = '';
                    return;
                }

                const responseData = await response.json();
                const newComment = document.createElement('div');
                newComment.classList.add('comment');

                const authorElement = document.createElement('p');
                authorElement.className = 'comment-author';
                authorElement.textContent = responseData.login;

                const textElement = document.createElement('p');
                textElement.textContent = responseData.content;

                newComment.appendChild(authorElement);
                newComment.appendChild(textElement);

                commentsList.prepend(newComment);
                form.reset();
            } catch (error) {
                console.error('Error:', error);
            }
        });
    });

</script>
</body>
</html>
