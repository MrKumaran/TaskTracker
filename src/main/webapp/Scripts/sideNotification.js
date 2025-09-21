export function showNotification(message, type = "true") {
    const notif = document.getElementById("sideNotification")

    notif.textContent = message

    notif.classList.remove("success", "fail", "hide")

    if (type === "true") {
        notif.classList.add("success")
    } else if (type === "false") {
        notif.classList.add("fail")
    }

    notif.classList.add("show")

    setTimeout(() => {
        notif.classList.add("hide")
        setTimeout(() => {
            notif.textContent = "";
            notif.classList.remove("show", "success", "fail")
        }, 600);
    }, 3000)
}