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
}

setTimeout( () => {
    errorIndicator.style.display = 'none'
    errorIndicator.innerText = ""
}, 3000)