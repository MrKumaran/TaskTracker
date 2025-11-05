const errorIndicator = document.getElementById("errorDiv")
const errorMessage = errorIndicator.innerText

switch (errorMessage) {
    case "PASSWORD_NOT_MATCH":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Passwords didn't match"
        break

    case "DB_ERROR":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Error occurred try again later. .. ..."
        break

    case "CREDENTIALS_NOT_MATCH":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Entered credentials didn't match"
        break

    case "PASSWORD_NOT_COMPLEX":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Password is not complex enough\n" +
            "Password should consist of\n" +
            "1 Uppercase, 1 Lowercase\n" +
            "1 number[0-9], 1 from @$!%*?&\n" +
            "and length should be 8 - 20"
        break
}

setTimeout( () => {
    errorIndicator.style.display = 'none'
    errorIndicator.innerText = ""
}, 3000)