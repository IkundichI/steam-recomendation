<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${game.name}</title>
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
            background: url('<#if game.background_raw?trim != "">${game.background_raw}<#else>https://steamcommunity-a.akamaihd.net/economy/image/-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxH5rd9eDAjcFyv45SRYAFMIcKL_PArgVSL403ulRUWEndVKv4gJaEAQ07dl1U4r_9KQZh0KGZcDhGu4zuwYbbkqfxa7qJlzkBusR33LuRrd6i3RqemzSFSdPyqA?size=300x180f</#if>') no-repeat center center fixed;
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
            font-size: 2em;
            margin-bottom: 20px;
        }

        .game-details {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .game-details img {
            width: 200px;
            height: auto;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .game-details p {
            font-size: 1.2em;
            margin: 10px 0;
        }

        .game-description {
            margin-top: 20px;
            color: #999;
        }

        .game-description h2 {
            font-size: 1.5em;
            color: #66c0f4;
        }

        .game-description p, .game-description ul {
            font-size: 1em;
            text-align: justify;
        }

        .bb_ul {
            list-style-type: disc;
            padding-left: 20px;
        }

        .bb_ul > li {
            margin-bottom: 10px;
        }

        .comments-section {
            margin-top: 40px;
        }

        .comments-section h2 {
            font-size: 1.5em;
            color: #66c0f4;
        }

        .comments-section form {
            display: flex;
            flex-direction: column;
        }

        .comments-section textarea {
            resize: vertical;
            min-height: 100px;
            margin-bottom: 10px;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
            font-size: 1em;
            background-color: #1b1e25;
            color: #c4d1da;
        }

        .comments-section button {
            background-color: #66c0f4;
            color: #171a21;
            border: none;
            padding: 10px;
            font-size: 1em;
            border-radius: 4px;
            cursor: pointer;
        }

        .comments-section button:hover {
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
        }

        .comment-rating {
            color: #66c0f4;
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
    <h1>${game.name}</h1>
    <div class="game-details">
        <img src="${game.header_image}" alt="Game Image">
        <#if game.metacritic_score != 0>
            <p><strong>Metacritic Score:</strong> ${game.metacritic_score}</p>
            <p><strong>Metacritic Website:</strong> <a href="${game.metacritic_url}" target="_blank">Link</a></p>
        </#if>
        <#if game.website??>
            <p><strong>Website:</strong> <a href="${game.website}" target="_blank">Link</a></p>
        </#if>
        <p><strong>Is free:</strong> ${game.is_free()?string('Yes', 'No')}</p>

        <#if game.supported_languages??>
            <p><strong>Supported Languages:</strong> ${game.supported_languages?no_esc}</p>
        </#if>
    </div>
    <div class="game-description">
        <h2>Detailed Description</h2>
        <p>${game.detailed_description?no_esc}</p>
    </div>
    <div class="comments-section">
        <h2>Comments</h2>
        <form id="commentForm">
            <textarea id="commentContent" name="content" placeholder="Write your comment..."></textarea>
            <input type="hidden" name="gameId" value="${game.appid}">
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

        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            const formData = new FormData(form);
            const content = formData.get('content');
            const gameId = formData.get('gameId');
            const csrfToken = document.getElementById('csrfToken').value;

            const params = new URLSearchParams();
            params.append('content', content);
            params.append('gameId', gameId);

            try {
                const response = await fetch('/comments/addGameComment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: params.toString()
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || 'Network response was not ok');
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
