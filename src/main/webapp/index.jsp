<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MistGameProject</title>
    <link href="static/main.css" rel="stylesheet">
    <script src="static/jquery-3.6.0.min.js"></script>
</head>
<body>

<div class="session">
    <table>
        <tr>
            <td>История:</td>
            <td>${storyId}</td>
        </tr>
        <tr>
            <td>Время:</td>
            <td>${time} мин.</td>
        </tr>
        <tr>
            <td>Здоровье:</td>
            <td>
                <% for (int i = 0; i < (int)session.getAttribute("health"); i++) { %>
                <img src ="static/images/health.png" alt="*">
                <% } %>
            </td>
        </tr>
        <tr>
            <td>Оружие:</td>
            <td><img src ="static/images/weapon-${weapon}.png" alt="Оружие"></td>
        </tr>
        <tr>
            <td>Патроны:</td>
            <td>
                <% for (int i = 0; i < (int)session.getAttribute("bullets"); i++) { %>
                <img src ="static/images/bullets.png" alt="*">
                <% } %>
            </td>
        </tr>
        <tr>
            <td>Рюкзак:</td>
            <td>
                <% if ((int)session.getAttribute("backpack") == 0) { %>
                    Пусто
                <% }
                else { %>
                    <img src ="static/images/backpack.png" alt="Вода, бинт, монтировка">
                <% } %>
            </td>
        </tr>
    </table>
</div>

<div>
    <img class="picture" src ="static/images/image-${storyId}.png" alt="Квест ~ Мгла ~">
    ${storyText}

    <% if((boolean)session.getAttribute("loss")) { %>
        <p class="warning">К сожалению, один из параметров игры стал менее 1, поэтому квест завершен.<br>
        Но всегда можно начать его заново и успешно дойти до финала:</p>
    <% } %>

    <table>
        <tr>
            <!-- Если параметр "loss" имеет значение true или первая ссылка имеет значение "empty" -->
            <% if((boolean)session.getAttribute("loss") || session.getAttribute("firstLinkId").equals("empty")) { %>
                <td class="choice" onclick="restart()">Начать прохождение квеста заново</td>
            <% }
            else { %>
            <!-- Вывод первой кнопки на экран -->
                <td class="choice" onclick="window.location='/logic?storyId=${firstLinkId}'">${firstLinkText}</td>

            <!-- Вывод второй кнопки на экран, если имеется ее значение -->
            <%  String secondButtom = (String)session.getAttribute("secondLinkId");
                if(!secondButtom.equals("empty")) { %>
                    <td class="choice"  onclick="window.location='/logic?storyId=${secondLinkId}'">${secondLinkText}</td>
                <% }
            } %>
        </tr>
    </table>

    <!-- Скрипт, отвечающий за перезагрузку игры -->
    <script>
        function restart() {
            $.ajax({
                url: '/restart',
                type: 'POST',
                contentType: 'application/json;charset=UTF-8',
                async: false,
                success: function () {
                    location.reload();
                }
            });
        }
    </script>
</div>
</body>
</html>