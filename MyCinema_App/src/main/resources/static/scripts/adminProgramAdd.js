document.getElementById("date").valueAsDate = new Date();

function getNextHour(nextHour) {
    if(nextHour < 9)
        return "0" + nextHour;
    else
        return nextHour;
}

function getNextMinutes(nextMinutes) {
    if(nextMinutes < 9)
        return "0" + nextMinutes;
    else
        return nextMinutes;
}

document.getElementById('time').value = getNextHour(new Date(new Date().setHours(new Date().getHours() + 1)).getHours()) + ":"
    + getNextMinutes(new Date().getMinutes());