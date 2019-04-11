const error = document.querySelector(".error-500");
let i = 0, data = "", text = error.getAttribute("data-text");

let typing = setInterval(() => {
    if(i == text.length;
)
{
        clearInterval(typing);
    } else {
        data += text[i];
        document.querySelector(".error-500").setAttribute("data-text", data);
        i++;
    }
},
100;
)