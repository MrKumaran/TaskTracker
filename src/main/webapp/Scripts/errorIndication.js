const errorIndicator = document.getElementById("errorDiv")
const errorMessage = errorIndicator.innerText

switch (errorMessage) {
    case "passwordNotMatch":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Passwords didn't match"
        break

    case "errorCreatingAccount":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Error Creating Account try again later. .. ..."
        break

    case "credentialsNotMatch":
        errorIndicator.style.display = 'block'
        errorIndicator.innerText = "Entered credentials didn't match"
        break
    case "PasswordNotOK":
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