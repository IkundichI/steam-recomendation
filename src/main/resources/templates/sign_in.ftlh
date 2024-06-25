<!doctype html>
<html>
<head>
    <title>Sign In</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: url('https://steamcommunity-a.akamaihd.net/economy/image/-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxH5rd9eDAjcFyv45SRYAFMIcKL_PArgVSL403ulRUWEndVKv4gJaEAQ07dl1U4r_9KQZh0KGZcDhGu4zuwYbbkqfxa7qJlzkBusR33LuRrd6i3RqemzSFSdPyqA?size=300x180f') no-repeat center center fixed;
            background-size: cover;
        }

        .form-style-3 {
            max-width: 400px;
            padding: 30px;
            background: rgba(23, 26, 33, 0.9);
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            color: #c4d1da;
        }

        .form-style-3 .title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .form-style-3 .title a {
            color: #66c0f4;
            text-decoration: none;
            font-size: 1.6em;
            margin: 0 10px;
        }

        .form-style-3 .title a.active {
            text-decoration: underline;
        }

        .form-style-3 fieldset {
            border: none;
            padding: 0;
            margin-bottom: 15px;
        }

        .form-style-3 label {
            display: block;
            margin-bottom: 15px;
            color: #c4d1da;
            font-size: 1em;
        }

        .form-style-3 label > span {
            display: inline-block;
            width: 100px;
            font-weight: bold;
        }

        .form-style-3 .input-field {
            width: calc(100% - 110px);
            padding: 10px;
            box-sizing: border-box;
            border: 1px solid #c4d1da;
            border-radius: 4px;
            background-color: #1b1e24;
            color: #c4d1da;
            font-size: 1em;
        }

        .form-style-3 .required {
            color: red;
        }

        .form-style-3 input[type="submit"] {
            background: #66c0f4;
            border: none;
            padding: 12px 25px;
            color: white;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1em;
        }

        .form-style-3 input[type="submit"]:hover {
            background: #4a9fd0;
        }

        .form-style-3 input[type="checkbox"] {
            margin-left: 5px;
        }

        .form-style-3 a {
            color: #66c0f4;
            text-decoration: none;
            display: block;
            margin-top: 15px;
            font-size: 1em;
        }

        .form-style-3 a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<div class="form-style-3">
    <div class="title">
        <a href="/signIn">Sign In</a>
        <a href="/signUp">Sign Up</a>
    </div>
    <#if error??>
        <h1>Неправильный логин или пароль</h1>
    </#if>
    <form method="post">
            <label for="login"><span>Логин <span class="required"></span></span><input type="text"
                                                                                        class="input-field"
                                                                                        name="login"
                                                                                        value=""/></label>
            <label for="password"><span>Пароль <span class="required"></span></span><input type="password"
                                                                                              class="input-field"
                                                                                              name="password"
                                                                                              value=""/></label>
            <label for="rememberMe"><span>Запомнить <span class="required"></span></span><input type="checkbox"
                                                                                                   name="remember-me-new" />
            </label>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <fieldset>
            <label><span> </span><input type="submit" value="Submit"/></label>
        </fieldset>
    </form>
</div>
</body>
</html>
